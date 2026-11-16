# El Pastillero

Aplicación Android en Java para organizar la medicación diaria: medicamentos, dosis,
stock, control de tomas, citas médicas y una lista personal independiente para cada
usuario, con opciones de accesibilidad.

Proyecto de la 1.ª evaluación de **PMDM** · Curso 2026-27 · IES Claudio Moyano (DAM 2).

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
| Gradle | 8.4 |
| Android Gradle Plugin | 8.3.2 |

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

- [`docs/manual_usuario.md`](docs/manual_usuario.md) — cómo se usa la aplicación paso a paso.
- [`docs/manual_tecnico.md`](docs/manual_tecnico.md) — arquitectura, decisiones y puntos clave del código.
- [`docs/esquema_bd.md`](docs/esquema_bd.md) — tablas, relaciones y datos iniciales.
- [`docs/navegacion.md`](docs/navegacion.md) — mapa de pantallas y flujos.

## 9. Pendiente antes de entregar

- Sustituir la URL de ejemplo `https://github.com/usuario/ElPastillero` en
  `AcercaDeActivity.java` por la dirección real del repositorio.
