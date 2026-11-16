# Navegación de la aplicación

Todas las actividades heredan de `BaseActivity`, que aplica el tamaño de letra elegido en
Ajustes. El identificador de usuario se guarda en `Sesion` (SharedPreferences) y se pasa
también por `Intent` entre pantallas cuando hace falta.

## 1. Mapa general

```
                    ┌─────────────────┐
                    │  LoginActivity  │◄──────────────────────────┐
                    └────────┬────────┘                           │
                             │ login correcto                     │
                             ▼                                    │
                    ┌─────────────────┐                           │
              ┌────►│  MainActivity   │── cerrar sesión ──┐       │
              │     │  (DrawerLayout) │                   │       │
              │     └────┬───┬───┬────┘                   │       │
              │          │   │   │                        │       │
   menú lateral│         │   │   └───────────► AcercaDeActivity   │
              │          │   │                                    │
              │          │   └──────────────► AjustesActivity ────┤
              │          │                          (borrar cuenta)
              │          ▼
              │   ┌──────────────────────────┐
              └───┤ MedicamentoListActivity  │
                  └────┬──────────────┬──────┘
                       │ clic tarjeta │ FAB / menú «+»
                       ▼              ▼
        ┌──────────────────────┐  ┌────────────────────────────┐
        │ MedicamentoDetalle   │  │ MedicamentoEdicionActivity │
        │ Activity             │◄─┤ (nuevo o edición)          │
        └──────┬───────────────┘  └────────────────────────────┘
               │ editar / menú
               └──────────────► MedicamentoEdicionActivity

MainActivity (menú lateral) ──► TomaListActivity
                             ──► CitaListActivity ──► CitaEdicionActivity
                             ──► ListaPersonalActivity ──► MedicamentoDetalleActivity
```

## 2. Pantalla de inicio (Login)

| Acción | Resultado |
|---|---|
| Rellenar correo y contraseña + **Entrar** | Valida contra la tabla `usuarios` y abre `MainActivity` |
| Marcar **Recordarme** | La próxima vez que se abra la app se entra directamente |
| Pulsar el texto de la demo | Rellena `sergio@pastillero.es` / `1234` |
| **¿No tienes cuenta? Crear una** | Diálogo de registro (nombre, correo, contraseña) |
| Menú → **Acerca de** | Abre `AcercaDeActivity` |

## 3. Menú lateral (Navigation Drawer)

`MainActivity` incluye 7 entradas:

| Entrada | Destino |
|---|---|
| Inicio | Cierra el cajón y permanece en el resumen |
| Medicamentos | `MedicamentoListActivity` |
| Control de tomas | `TomaListActivity` |
| Citas médicas | `CitaListActivity` |
| Mi lista personal | `ListaPersonalActivity` |
| Ajustes | `AjustesActivity` |
| Acerca de | `AcercaDeActivity` |

Además, la pantalla de resumen tiene tres botones directos a Medicamentos, Tomas y Citas,
y una tarjeta con el stock global, el número de medicamentos, los que tienen stock bajo y
las tomas pendientes.

## 4. Medicamentos

```
Listado ──clic tarjeta──► Ficha
   │  ├── menú «+» / FAB ──► Formulario (nuevo)
   │  ├── menú contextual (pulsación larga) ──► Editar / Eliminar
   │  └── menú de opciones: buscar, ordenar (nombre, stock, valoración), añadir, acerca de
   │
Ficha ──► botón Editar / menú ──► Formulario (edición)
      ──► botón Eliminar / menú ──► diálogo de confirmación
      ──► botón «Registrar toma» ──► diálogo de fecha y hora
      ──► pulsación larga en una toma ──► marcar como tomada / eliminar
      ──► switch «Añadir a mi lista personal»
```

## 5. Control de tomas

```
TomaListActivity
 ├── FAB / menú «+» ──► elegir medicamento ──► diálogo de fecha y hora
 ├── casilla de una toma ──► marcar/desmarcar y ajustar stock
 ├── pulsación larga ──► menú contextual (marcar como tomada / eliminar)
 └── menú de opciones: añadir, acerca de
```

## 6. Citas médicas

```
CitaListActivity
 ├── FAB / menú «+» ──► CitaEdicionActivity (nueva)
 ├── clic en una tarjeta ──► CitaEdicionActivity (edición)
 ├── pulsación larga ──► menú contextual (editar / eliminar)
 └── menú de opciones: añadir, acerca de
```

## 7. Mi lista personal

```
ListaPersonalActivity
 ├── FAB / menú «+» ──► elegir medicamento y añadirlo
 ├── clic en una tarjeta ──► MedicamentoDetalleActivity
 ├── pulsación larga ──► menú contextual (editar nota / quitar de la lista)
 └── menú de opciones: añadir, acerca de
```

## 8. Ajustes

```
AjustesActivity
 ├── SeekBar de tamaño de letra ──► recrea la pantalla con la nueva escala
 ├── Switch de avisos
 ├── RadioGroup de tema (sistema / claro / oscuro)
 ├── Mi perfil ──► diálogo para cambiar nombre y correo
 ├── Cerrar sesión ──► vuelve a LoginActivity
 └── Borrar mi cuenta ──► diálogo de confirmación y vuelta a LoginActivity
```

## 9. Volver atrás

Todas las pantallas hijas tienen la flecha de la barra de acción (`android.R.id.home`),
que cierra la actividad y devuelve al listado o al inicio.
