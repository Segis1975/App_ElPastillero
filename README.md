# El Pastillero

Aplicación Android en Java para organizar la medicación diaria: medicamentos, dosis,
stock, control de tomas, citas médicas y una lista personal independiente para cada
usuario, con opciones de accesibilidad.

Proyecto de la 1.ª evaluación de **PMDM** · Curso 2026-27 · IES Claudio Moyano (DAM 2).

**Repositorio:** <https://github.com/Segis1975/App_ElPastillero>

---

## 1. Puesta en marcha

1. Abre la carpeta del proyecto con **Android Studio** (versión Hedgehog o superior).
2. Deja que Gradle sincronice. La primera vez descargará las dependencias
   (AppCompat, Material, RecyclerView, CardView, ConstraintLayout y Room).
3. Ejecuta el módulo `app` en un emulador o dispositivo con **Android 8.0 (API 26)** o superior.

| Elemento | Valor |
|---|---|
| `applicationId` | `es.ies.claudiomoyano.dam2.elpastillero` |
| `compileSdk` / `targetSdk` | 34 |
| `minSdk` | 26 |
| Java | 17 |
| Gradle | 9.6.0 |
| Android Gradle Plugin | 9.4.1 |

> `local.properties` contiene la ruta del SDK de este equipo. Si abres el proyecto en
> otro ordenador, Android Studio la regenerará automáticamente.

## 2. Credenciales de demostración

La base de datos se crea sola la primera vez, con dos usuarios ya dados de alta:

| Usuario | Contraseña | Contenido inicial |
|---|---|---|
| `sergio@pastillero.es` | `1234` | Lista personal con 2 medicamentos y 2 citas médicas |
| `ana@pastillero.es` | `1234` | Cuenta vacía (demuestra que cada usuario tiene su propia lista) |

Desde la pantalla de login se puede pulsar el texto de la demo para rellenar los campos
automáticamente, o crear una cuenta nueva con el enlace **«¿No tienes cuenta? Crear una»**.

## 3. Pantallas

La aplicación tiene **11 actividades** conectadas entre sí:

1. `LoginActivity` — acceso y registro contra la tabla `usuarios` de SQLite.
2. `MainActivity` — resumen del día y **Navigation Drawer** con 7 entradas.
3. `MedicamentoListActivity` — listado con RecyclerView + CardView, búsqueda y ordenación.
4. `MedicamentoDetalleActivity` — ficha, tomas del medicamento y switch de lista personal.
5. `MedicamentoEdicionActivity` — alta y edición con Spinner, SeekBar, Switch, RatingBar y RadioGroup.
6. `TomaListActivity` — control de tomas de todos los medicamentos.
7. `CitaListActivity` — citas médicas.
8. `CitaEdicionActivity` — alta y edición de citas con `DatePicker` y `TimePicker`.
9. `ListaPersonalActivity` — lista personal del usuario con notas propias.
10. `AjustesActivity` — accesibilidad (tamaño de letra), tema, perfil y borrado de cuenta.
11. `AcercaDeActivity` — información, versión y enlace al repositorio.

El detalle de la navegación está en [`docs/navegacion.md`](docs/navegacion.md).

## 3.b Hitos del proyecto

El proyecto se ha construido por partes, siguiendo las prácticas del trimestre:

| Hito | Práctica | Qué se añadió |
|---|---|---|
| H1 | P1–P3 | Estructura del proyecto, tema Material 3, recursos e iconos vectoriales |
| H2 | P4–P5 | Pantallas de login, alta de usuario y resumen con menú lateral |
| H3 | P6–P7 | Listado con RecyclerView + CardView, ficha y formulario con componentes avanzados |
| H4 | P8–P9 | CRUD de citas, control de tomas y lista personal por usuario |
| H5 | P11 | SQLite (`usuarios`, `favoritos`, `citas`) con claves ajenas y CASCADE |
| H6 | P12 | Room: `Medicamento` 1-N `Toma` con FK y CASCADE, carga inicial de ejemplo |
| H7 | P13 | Diálogos propios (`DatePicker` + `TimePicker`), ajustes y accesibilidad |
| H8 | Proyecto | Internacionalización completa, rotación, pruebas y documentación |

Cada hito corresponde a un bloque de trabajo del historial de Git. El historial sirve para
comprobar la evolución del proyecto, no para puntuar el número de commits.

## 3.c Capturas

Las capturas reales de cada pantalla están en [`docs/capturas/`](docs/capturas/) y se
incluyen como anexo en los dos manuales en PDF. Se tomaron en un emulador Pixel 9
(1080 × 2424) con una instalación limpia y la aplicación en español:

| Archivo | Pantalla |
|---|---|
| `01_login.png` | Acceso con la cuenta de demostración |
| `02_login_credenciales.png` | Acceso con las credenciales rellenadas |
| `03_inicio.png` | Inicio con el resumen del día |
| `04_menu_lateral.png` | Menú lateral desplegado con la cabecera del usuario |
| `05_medicamentos.png` | Listado en tarjetas, con búsqueda y ordenación |
| `06_ficha_medicamento.png` | Ficha de un medicamento y sus tomas |
| `07_formulario_medicamento.png` | Formulario de edición (parte superior) |
| `08_formulario_abajo.png` | Formulario de edición (valoración, notas y botones) |
| `09_dialogo_fecha_hora.png` | `DialogFragment` con `DatePicker` y `TimePicker` |
| `10_tomas.png` | Control de tomas |
| `11_citas.png` | Citas médicas |
| `12_lista_personal.png` | Lista personal del usuario |
| `13_ajustes.png` | Ajustes de accesibilidad y cuenta |
| `14_acerca_de.png` | Acerca de, con la URL del repositorio |
| `15_inicio_ingles.png` | Inicio con la interfaz en inglés (`values-en`) |

## 4. Trazabilidad con la rúbrica

| Requisito | Dónde se cumple |
|---|---|
| Login contra tabla de usuarios | `DbHelper.login()` + `LoginActivity` (contraseñas con SHA-256) |
| Lista personal por usuario | Tabla `favoritos` con `idUsuario` y `UNIQUE(idUsuario, idMedicamento)` |
| CRUD completo | Medicamentos (Room), citas (SQLite) y lista personal |
| Búsqueda y ordenación | `SearchView` + orden por nombre, stock o valoración |
| 8 o más pantallas conectadas | 11 actividades |
| SQLite | `DbHelper` → `usuarios`, `favoritos`, `citas` |
| Room | `Medicamento` (principal) **1-N** `Toma` con FK `ON DELETE CASCADE` |
| Consultas fuera del hilo principal | `AppExecutors` (hilo IO propio + `Handler` del hilo principal) |
| Carga inicial de datos | 2 usuarios, 6 medicamentos, 1 toma, 2 favoritos y 2 citas |
| RecyclerView + CardView | Listados de medicamentos, tomas, citas y lista personal |
| Navigation Drawer | `menu_drawer.xml` con 7 entradas |
| Menú de opciones y contextual | `menu_medicamentos`, `menu_detalle`, etc. + pulsación larga |
| DialogFragment con DatePicker y TimePicker | `DialogFechaHora` |
| Tres o más tipos de layout | ConstraintLayout, LinearLayout, TableLayout, DrawerLayout, ScrollView/NestedScrollView |
| Rotación | `onSaveInstanceState` en formularios y borrados pendientes; escala de letra en `attachBaseContext` |
| Componentes básicos y avanzados | `RadioButton`, `SwitchMaterial`, `ProgressBar`, `SeekBar`, `RatingBar`, `Spinner`, `CheckBox` |
| Todos los textos en `strings.xml` | `values/strings.xml` + `values-en/strings.xml` |
| Documentación y código comentado | Este README, carpeta `docs/` y Javadoc en las clases |

## 5. Estructura del código

```
app/src/main/java/es/ies/claudiomoyano/dam2/elpastillero/
├── LoginActivity, MainActivity, ...        Actividades (interfaz)
├── BaseActivity                            Aplica la escala de letra de Ajustes
├── Sesion                                  Sesión y preferencias (SharedPreferences)
├── AppExecutors                            Trabajo en segundo plano
├── Seguridad                               Hash SHA-256 de contraseñas
├── FechaUtil                               Formato y conversión de fechas
├── data/                                   Room: Medicamento, Toma, DAOs, AppDatabase
├── db/DbHelper                             SQLite: usuarios, favoritos y citas
├── model/                                  Usuario, Favorito, Cita
├── adapter/                                Adaptadores de RecyclerView
└── dialog/                                 DialogFragment (fecha/hora y confirmación)
```

## 6. Base de datos

Hay **dos bases de datos**, tal y como pide el enunciado:

- **Room** (`pastillero_room.db`): entidad principal `Medicamento` y entidad relacionada
  `Toma` (1-N con clave ajena y borrado en cascada).
- **SQLite** (`pastillero.db`): usuarios, lista personal de cada usuario y citas médicas.

El esquema completo está en [`docs/esquema_bd.md`](docs/esquema_bd.md).

## 7. Idiomas

La interfaz está traducida al español (idioma por defecto) y al inglés (`values-en`).
Todas las claves de `values/strings.xml` existen también en `values-en/strings.xml`.

## 8. Documentación

- [`docs/manual_usuario.md`](docs/manual_usuario.md) y **[`docs/manual_usuario.pdf`](docs/manual_usuario.pdf)** — cómo se usa la aplicación paso a paso (A4, portada, índice y bibliografía).
- [`docs/manual_tecnico.md`](docs/manual_tecnico.md) y **[`docs/manual_tecnico.pdf`](docs/manual_tecnico.pdf)** — arquitectura, decisiones y puntos clave del código.
- [`docs/esquema_bd.md`](docs/esquema_bd.md) — tablas, relaciones y datos iniciales.
- [`docs/navegacion.md`](docs/navegacion.md) — mapa de pantallas y flujos.
- [`docs/capturas/`](docs/capturas/) — capturas de todas las pantallas.

## 9. Estado de la entrega

- Compilación verificada con Gradle: `gradlew assembleDebug` → `BUILD SUCCESSFUL`.
- Las 11 pantallas están conectadas y cada ida tiene vuelta.
- La carga inicial se comprueba en una instalación limpia (2 usuarios, 6 medicamentos,
  1 toma, 2 favoritos y 2 citas).
- Los textos visibles salen de `strings.xml` y `values-en/strings.xml` tiene las mismas
  185 claves (más dos `plurals` para las unidades).
- El enlace del repositorio está en `url_repositorio` y apunta a
  <https://github.com/Segis1975/App_ElPastillero>.
