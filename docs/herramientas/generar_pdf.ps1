# Genera un PDF A4 a partir de un manual en Markdown.
# Uso:
#   powershell -ExecutionPolicy Bypass -File docs\herramientas\generar_pdf.ps1 `
#       -Md docs\manual_usuario.md -Pdf docs\manual_usuario.pdf -Titulo "Manual de usuario"
#
# Requiere Google Chrome o Microsoft Edge instalados (se usan en modo headless).
# El script es ASCII puro: los acentos del contenido vienen del propio Markdown,
# que se lee siempre como UTF-8.

param(
    [Parameter(Mandatory = $true)][string]$Md,
    [Parameter(Mandatory = $true)][string]$Pdf,
    [Parameter(Mandatory = $true)][string]$Titulo,
    [string]$Subtitulo = '',
    [string]$Autor = 'Sergio',
    [string]$Centro = 'IES Claudio Moyano (DAM 2)',
    [string]$Curso = 'PMDM - Curso 2026-27',
    [string]$Repo = 'https://github.com/Sergio/ElPastillero',
    [string]$CarpetaCapturas = ''
)

$ErrorActionPreference = 'Stop'

function Escape-Html([string]$s) {
    if ($null -eq $s) { return '' }
    return $s.Replace('&', '&amp;').Replace('<', '&lt;').Replace('>', '&gt;')
}

# Formato en linea: negrita, codigo, enlaces Markdown y autollenlaces <url>.
function Format-Inline([string]$s) {
    $t = Escape-Html $s
    $t = [regex]::Replace($t, '\*\*(.+?)\*\*', '<strong>$1</strong>')
    $t = [regex]::Replace($t, '`(.+?)`', '<code>$1</code>')
    $t = [regex]::Replace($t, '\[(.+?)\]\((.+?)\)', '<a href="$2">$1</a>')
    $t = [regex]::Replace($t, '&lt;(https?://[^&]+?)&gt;', '<a href="$1">$1</a>')
    return $t
}

$script:salida = New-Object System.Collections.Generic.List[string]
$script:titulos = New-Object System.Collections.Generic.List[string]
$script:parrafo = New-Object System.Collections.Generic.List[string]
$script:lista = $null

function Cerrar-Parrafo {
    if ($script:parrafo.Count -gt 0) {
        $texto = $script:parrafo -join ' '
        $script:salida.Add('<p>' + (Format-Inline $texto) + '</p>')
        $script:parrafo.Clear()
    }
}

function Cerrar-Bloque {
    Cerrar-Parrafo
    if ($script:lista) {
        $script:salida.Add("</$($script:lista)>")
        $script:lista = $null
    }
}

function Abrir-Lista([string]$tipo) {
    Cerrar-Parrafo
    if ($script:lista -ne $tipo) {
        if ($script:lista) { $script:salida.Add("</$($script:lista)>") }
        $script:salida.Add("<$tipo>")
        $script:lista = $tipo
    }
}

# ------------------------------------------------------------------ Markdown

$rutaMd = (Resolve-Path $Md).Path
$markdown = [System.IO.File]::ReadAllText($rutaMd, [System.Text.Encoding]::UTF8)
$lineas = $markdown -split "`r?`n"

$i = 0
$enCodigo = $false
$codigo = New-Object System.Collections.Generic.List[string]

while ($i -lt $lineas.Count) {
    $linea = $lineas[$i]

    if ($enCodigo) {
        if ($linea -match '^\s*```') {
            $enCodigo = $false
            $script:salida.Add('<pre><code>' + (($codigo | ForEach-Object { Escape-Html $_ }) -join "`n") + '</code></pre>')
            $codigo.Clear()
        } else {
            $codigo.Add($linea)
        }
        $i++
        continue
    }

    if ($linea -match '^\s*```') {
        Cerrar-Bloque
        $enCodigo = $true
        $i++
        continue
    }

    # Tabla: linea de celdas + linea separadora
    if ($linea -match '^\s*\|' -and ($i + 1) -lt $lineas.Count -and $lineas[$i + 1] -match '^\s*\|[\s:\-\|]+\|\s*$') {
        Cerrar-Bloque
        $cabecera = @(($linea.Trim().Trim('|') -split '\|') | ForEach-Object { $_.Trim() })
        $html = '<table><thead><tr>'
        foreach ($celda in $cabecera) { $html += '<th>' + (Format-Inline $celda) + '</th>' }
        $html += '</tr></thead><tbody>'
        $i += 2
        while ($i -lt $lineas.Count -and $lineas[$i] -match '^\s*\|') {
            $celdas = @(($lineas[$i].Trim().Trim('|') -split '\|') | ForEach-Object { $_.Trim() })
            $html += '<tr>'
            foreach ($celda in $celdas) { $html += '<td>' + (Format-Inline $celda) + '</td>' }
            $html += '</tr>'
            $i++
        }
        $html += '</tbody></table>'
        $script:salida.Add($html)
        continue
    }

    if ($linea -match '^(#{1,4})\s+(.*)$') {
        $nivel = $Matches[1].Length
        $texto = $Matches[2]
        Cerrar-Bloque
        # El titulo del documento lo pone la portada, no se repite en el cuerpo.
        if ($nivel -eq 1) { $i++; continue }
        if ($nivel -eq 2) { $script:titulos.Add($texto) }
        $script:salida.Add("<h$nivel>" + (Format-Inline $texto) + "</h$nivel>")
        $i++
        continue
    }

    if ($linea -match '^\s*[-*]\s+(.*)$') {
        Abrir-Lista 'ul'
        $script:salida.Add('<li>' + (Format-Inline $Matches[1]) + '</li>')
        $i++
        continue
    }

    if ($linea -match '^\s*\d+\.\s+(.*)$') {
        Abrir-Lista 'ol'
        $script:salida.Add('<li>' + (Format-Inline $Matches[1]) + '</li>')
        $i++
        continue
    }

    if ($linea -match '^>\s?(.*)$') {
        Cerrar-Bloque
        $script:salida.Add('<blockquote>' + (Format-Inline $Matches[1]) + '</blockquote>')
        $i++
        continue
    }

    if ($linea -match '^\s*---+\s*$') {
        Cerrar-Bloque
        $script:salida.Add('<hr>')
        $i++
        continue
    }

    if ($linea.Trim().Length -eq 0) {
        Cerrar-Bloque
        $i++
        continue
    }

    $script:parrafo.Add($linea.Trim())
    $i++
}

Cerrar-Bloque

# ------------------------------------------------------------------ Anexo de capturas

$anexo = ''
if ($CarpetaCapturas -and (Test-Path $CarpetaCapturas)) {
    # Pie de foto de cada captura. Se escribe en ASCII con entidades HTML para
    # que el script siga siendo compatible con PowerShell 5.1.
    $pies = @{
        '01_login'                  = 'Acceso a la aplicaci&#243;n'
        '02_login_credenciales'     = 'Acceso con las credenciales de demostraci&#243;n'
        '03_inicio'                 = 'Inicio: resumen del d&#237;a'
        '04_menu_lateral'           = 'Men&#250; lateral con la cabecera del usuario'
        '05_medicamentos'           = 'Listado de medicamentos con RecyclerView y CardView'
        '06_ficha_medicamento'      = 'Ficha del medicamento y tomas registradas'
        '07_formulario_medicamento' = 'Formulario de edici&#243;n: datos y componentes'
        '08_formulario_abajo'       = 'Formulario de edici&#243;n: valoraci&#243;n y notas'
        '09_dialogo_fecha_hora'     = 'DialogFragment con DatePicker y TimePicker'
        '10_tomas'                  = 'Control de tomas'
        '11_citas'                  = 'Citas m&#233;dicas'
        '12_lista_personal'         = 'Lista personal del usuario'
        '13_ajustes'                = 'Ajustes de accesibilidad y de la cuenta'
        '14_acerca_de'              = 'Acerca de, con la URL del repositorio'
        '15_inicio_ingles'          = 'Interfaz en ingl&#233;s (values-en)'
    }
    $imagenes = @(Get-ChildItem $CarpetaCapturas -File |
        Where-Object { $_.Extension -match '^\.(png|jpg|jpeg|webp)$' } |
        Sort-Object Name)
    if ($imagenes.Count -gt 0) {
        $anexo = '<h2 style="page-break-before: always;">Anexo. Capturas de pantalla</h2><div class="anexo">'
        foreach ($imagen in $imagenes) {
            $uri = ([System.Uri]$imagen.FullName).AbsoluteUri
            $clave = [System.IO.Path]::GetFileNameWithoutExtension($imagen.Name)
            if ($pies.ContainsKey($clave)) {
                $pie = $pies[$clave]
            } else {
                $pie = Escape-Html ($clave.Replace('_', ' '))
            }
            $anexo += '<figure><img src="' + $uri + '" alt="' + (Escape-Html $clave) + '">'
            $anexo += '<figcaption>' + $pie + '</figcaption></figure>'
        }
        $anexo += '</div>'
    }
}

# ------------------------------------------------------------------ HTML

$indice = '<ol>'
foreach ($titulo in $script:titulos) {
    $indice += '<li>' + (Format-Inline $titulo) + '</li>'
}
$indice += '</ol>'

$cuerpo = $script:salida -join "`n"

$estilos = @'
@page { size: A4; margin: 2.2cm 2.2cm 2.2cm 2.4cm; }
body { font-family: Calibri, Carlito, "Segoe UI", sans-serif; font-size: 11pt; line-height: 1.45; color: #1a1a1a; }
h1, h2, h3, h4 { font-family: Calibri, Carlito, sans-serif; color: #0d5c8a; page-break-after: avoid; }
h2 { font-size: 14pt; border-bottom: 1px solid #cfd8dc; padding-bottom: 3px; margin-top: 24px; }
h3 { font-size: 12pt; margin-top: 18px; }
h4 { font-size: 11pt; margin-top: 14px; }
p { margin: 6px 0 9px 0; text-align: justify; }
code { font-family: Consolas, "Courier New", monospace; font-size: 9.5pt; background: #f2f4f6; padding: 1px 3px; border-radius: 3px; }
pre { background: #f6f8f9; border: 1px solid #dde3e7; border-radius: 4px; padding: 8px 10px; font-size: 9pt; white-space: pre-wrap; page-break-inside: avoid; }
pre code { background: none; padding: 0; font-size: 9pt; }
table { border-collapse: collapse; width: 100%; margin: 10px 0 14px 0; font-size: 10pt; }
th, td { border: 1px solid #c7d0d6; padding: 5px 7px; text-align: left; vertical-align: top; }
th { background: #e8f0f5; }
blockquote { border-left: 3px solid #90a4ae; margin: 10px 0; padding: 4px 12px; background: #f5f7f8; color: #37474f; }
ul, ol { margin: 6px 0 12px 0; padding-left: 22px; }
li { margin: 3px 0; }
a { color: #0b62a4; text-decoration: none; word-break: break-word; }
hr { border: 0; border-top: 1px solid #dde3e7; margin: 18px 0; }
.cover { page-break-after: always; text-align: center; padding-top: 4.5cm; }
.cover .kicker { font-size: 12pt; letter-spacing: 2px; color: #607d8b; text-transform: uppercase; }
.cover h1 { font-size: 32pt; margin: 14px 0 8px 0; }
.cover .sub { font-size: 14pt; color: #455a64; margin-bottom: 4cm; }
.cover .meta { font-size: 11pt; color: #37474f; line-height: 1.9; }
.toc { page-break-after: always; }
.toc h2 { border: none; margin-top: 0; }
.toc ol { list-style: none; padding-left: 0; }
.toc li { margin: 6px 0; font-size: 11pt; }
.anexo figure { page-break-inside: avoid; margin: 0 0 14px 0; text-align: center; }
.anexo img { max-width: 13cm; max-height: 17cm; border: 1px solid #cfd8dc; border-radius: 4px; }
.anexo figcaption { font-size: 9.5pt; color: #546e7a; margin-top: 4px; }
'@

$html = @"
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8">
<title>$Titulo - El Pastillero</title>
<style>
$estilos
</style>
</head>
<body>
<div class="cover">
  <div class="kicker">$Curso</div>
  <h1>$Titulo</h1>
  <div class="sub">$Subtitulo</div>
  <div class="meta">
    <p><strong>$Centro</strong></p>
    <p>Autor: $Autor</p>
    <p>Repositorio: $Repo</p>
    <p>Noviembre de 2026</p>
  </div>
</div>
<div class="toc">
  <h2>&#205;ndice</h2>
  $indice
</div>
$cuerpo
$anexo
</body>
</html>
"@

$rutaPdf = [System.IO.Path]::GetFullPath($Pdf)
$carpetaPdf = [System.IO.Path]::GetDirectoryName($rutaPdf)
if (-not (Test-Path $carpetaPdf)) { New-Item -ItemType Directory -Path $carpetaPdf -Force | Out-Null }

$rutaHtml = Join-Path $env:TEMP ('opencode\manual_' +
    [System.IO.Path]::GetFileNameWithoutExtension($rutaPdf) + '.html')
$carpetaHtml = [System.IO.Path]::GetDirectoryName($rutaHtml)
if (-not (Test-Path $carpetaHtml)) { New-Item -ItemType Directory -Path $carpetaHtml -Force | Out-Null }
[System.IO.File]::WriteAllText($rutaHtml, $html, (New-Object System.Text.UTF8Encoding($true)))

# ------------------------------------------------------------------ Chrome headless

$navegadores = @(
    'C:\Program Files\Google\Chrome\Application\chrome.exe',
    'C:\Program Files (x86)\Google\Chrome\Application\chrome.exe',
    'C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe',
    'C:\Program Files\Microsoft\Edge\Application\msedge.exe'
)
$navegador = $null
foreach ($candidato in $navegadores) {
    if (Test-Path $candidato) { $navegador = $candidato; break }
}
if (-not $navegador) { throw 'No se ha encontrado Chrome ni Edge para generar el PDF.' }

$perfil = Join-Path $env:TEMP 'opencode\chrome-pdf-profile'
if (-not (Test-Path $perfil)) { New-Item -ItemType Directory -Path $perfil -Force | Out-Null }

$uriHtml = ([System.Uri]$rutaHtml).AbsoluteUri
$argumentos = @(
    '--headless=new',
    '--disable-gpu',
    '--no-first-run',
    '--no-pdf-header-footer',
    '--virtual-time-budget=6000',
    "--user-data-dir=`"$perfil`"",
    "--print-to-pdf=`"$rutaPdf`"",
    $uriHtml
)

Start-Process -FilePath $navegador -ArgumentList $argumentos -Wait -NoNewWindow

if (Test-Path $rutaPdf) {
    $tamano = [math]::Round((Get-Item $rutaPdf).Length / 1KB, 1)
    Write-Output "OK  $rutaPdf  ($tamano KB)"
} else {
    throw "No se ha generado el PDF: $rutaPdf"
}
