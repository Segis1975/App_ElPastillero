# Manual técnico

## Introducción

Documentación de las decisiones de diseño y de los puntos clave del código de
**El Pastillero**, la aplicación Android del proyecto de la 1.ª evaluación de PMDM.

El manual está pensado para que cualquier persona que conozca Java y Android pueda
entender cómo está organizado el proyecto, dónde está cada pieza y por qué se tomaron
las decisiones principales: dos bases de datos complementarias, consultas fuera del hilo
principal, estado conservado al girar la pantalla e internacionalización completa.

## 1. Arquitectura general

La aplicación sigue una separación sencilla en capas, sin librerías de inyección de
dependencias para que el código sea fácil de leer en el contexto de clase:

```
┌──────────────────────────────────────────────┐
│ Presentación   Activities, Adapters, Dialogs │
├──────────────────────────────────────────────┤
│ Acceso a datos DbHelper (SQLite), DAOs (Room) │
├──────────────────────────────────────────────┤
│ Modelo         Medicamento, Toma, Usuario…   │
├──────────────────────────────────────────────┤
│ Utilidades     Sesion, AppExecutors, Fecha…  │
└──────────────────────────────────────────────┘
```

### Paquetes Java

| Paquete | Contenido |
|---|---|
| `elpastillero` | Actividades, `BaseActivity`, `Sesion`, `AppExecutors`, `Seguridad`, `FechaUtil` |
| `elpastillero.data` | Entidades y DAOs de Room + `AppDatabase` |
| `elpastillero.db` | `DbHelper` (SQLite: usuarios, lista personal y citas) |
| `elpastillero.model` | `Usuario`, `Favorito`, `Cita` |
| `elpastillero.adapter` | Adaptadores de RecyclerView |
| `elpastillero.dialog` | `DialogFechaHora` y `DialogConfirmar` |

## 2. Hilos y trabajo en segundo plano

Ninguna consulta a base de datos se ejecuta en el hilo de interfaz. `AppExecutors` ofrece
dos operaciones:

```java
// Ejecuta una tarea en el hilo IO (sin devolver resultado)
AppExecutors.io(() -> ...);

// Ejecuta una tarea en el hilo IO y entrega el resultado en el hilo principal
AppExecutors.io(() -> db.login(email, password), usuario -> { ... });
```

- Hay **un único hilo IO** (`Executors.newSingleThreadExecutor()`), lo que evita
  condiciones de carrera entre consultas y escrituras y serializa el acceso a SQLite.
- El resultado se publica con un `Handler` del `Looper` principal.
- Todas las llamadas de vuelta comprueban `isFinishing()` / `isDestroyed()` antes de tocar
  la interfaz, para no actualizar una pantalla que ya se ha cerrado.

Ejemplo real en `MedicamentoListActivity`:

```java
AppExecutors.io(() -> room.medicamentoDao().todos(), lista -> {
    if (isFinishing() || isDestroyed()) return;
    datos.clear();
    datos.addAll(lista);
    ordenarYMostrar();
});
```

## 3. Las dos bases de datos

### 3.1 Room

- Entidad principal `Medicamento` y entidad relacionada `Toma`.
- Relación 1-N declarada con `@ForeignKey(... onDelete = ForeignKey.CASCADE)` e índice
  sobre `idMedicamento`.
- `TomaDetalle` es un POJO con un `@Embedded Toma` y el nombre del medicamento: sirve para
  pintar el listado de tomas con una sola consulta con `INNER JOIN`.
- `AppDatabase.get(contexto)` implementa el patrón *singleton* con doble comprobación y
  `volatile`.

La carga inicial se hace de forma **síncrona** dentro del callback `onCreate`, escribiendo
directamente sobre el `SupportSQLiteDatabase` que Room entrega:

```java
@Override
public void onCreate(@NonNull SupportSQLiteDatabase db) {
    super.onCreate(db);
    sembrar(db);          // 6 medicamentos + 1 toma
}
```

Así la primera consulta de la interfaz ya encuentra datos. Si se lanzara en otro hilo, el
primer listado aparecería vacío.

### 3.2 SQLite con `SQLiteOpenHelper`

`DbHelper` gestiona `usuarios`, `favoritos` y `citas`:

- Claves ajenas activadas en `onConfigure()` (`setForeignKeyConstraintsEnabled(true)`).
- `UNIQUE(idUsuario, idMedicamento)` en `favoritos`.
- `ON DELETE CASCADE` de `usuarios` hacia `favoritos` y `citas`.

### 3.3 Enlace entre ambas

`favoritos.idMedicamento` guarda el `id` de `medicamentos`. Como las dos bases son
independientes, el borrado se propaga a mano:

```java
room.medicamentoDao().borrar(m);
db.borrarFavoritosDeMedicamento(m.id);   // limpia las listas de todos los usuarios
```

## 4. Sesión y preferencias

`Sesion` centraliza `SharedPreferences`:

| Clave | Uso |
|---|---|
| `idUsuario`, `nombre`, `email` | Identidad del usuario activo |
| `recordar` | Entrada automática |
| `escalaLetra` | Factor de tamaño de letra (0,8 – 1,4) |
| `notificaciones` | Avisos internos |
| `tema` | 0 sistema, 1 claro, 2 oscuro |

`Sesion.activa()` comprueba si hay sesión iniciada. `Sesion.cerrar()` borra la identidad
pero conserva las preferencias de accesibilidad.

## 5. Accesibilidad

`BaseActivity` aplica la escala de letra a **todas** las pantallas:

```java
@Override
protected void attachBaseContext(Context newBase) {
    Configuration c = new Configuration(newBase.getResources().getConfiguration());
    c.fontScale = Sesion.escalaLetra(newBase);
    super.attachBaseContext(newBase.createConfigurationContext(c));
}
```

Como se aplica en `attachBaseContext`, el cambio afecta a los textos de la actividad, los
diálogos y los menús. Al soltar la `SeekBar` de Ajustes se guarda el valor y se llama a
`recreate()`.

El tema claro/oscuro se resuelve con `AppCompatDelegate.setDefaultNightMode(...)` y una
carpeta `values-night/colors.xml`.

## 6. Rotación de pantalla

| Pantalla | Qué se conserva |
|---|---|
| `MedicamentoEdicionActivity` | `fechaMillis`, el tipo y la presentación de los dos Spinner encadenados; además evita recargar el formulario si ya hay estado guardado |
| `CitaEdicionActivity` | `fechaHora` en `onSaveInstanceState` |
| `MedicamentoListActivity` | `id` del medicamento pendiente de borrar, el texto de búsqueda y el criterio de ordenación |
| `CitaListActivity` | `id` de la cita pendiente de borrar |
| `ListaPersonalActivity` | `id` del medicamento pendiente de quitar |
| `AjustesActivity` | Confirmación de borrado de cuenta pendiente |
| `TomaListActivity` | `id` y cantidad del medicamento elegido mientras está abierto el diálogo de fecha y hora |
| `DialogFechaHora` | Año, mes, día, hora y minuto seleccionados en los pickers |

Los diálogos se guardan el identificador, no el objeto: tras rotar, la actividad vuelve a
consultar la base de datos y la operación sigue funcionando.

Los `EditText`, `SeekBar`, `RatingBar`, `RadioButton`, `Switch` y `Spinner` conservan su
valor automáticamente porque tienen `android:id`. Lo que Android no puede deducir solo
(la fecha elegida, la presentación del spinner o el medicamento de una toma) se guarda a
mano en `onSaveInstanceState`.

## 7. Diálogos

- `DialogFechaHora`: `DialogFragment` con un `DatePicker` y un `TimePicker` en modo
  *spinner*. Devuelve año, mes, día, hora y minuto mediante la interfaz
  `OnFechaHoraListener`. Al girar la pantalla conserva la fecha y la hora que el usuario
  acababa de elegir.
- `DialogConfirmar`: `DialogFragment` reutilizable con el mensaje y el título recibidos por
  argumentos. La actividad que lo abre implementa `OnConfirmarListener`.

Ambos se crean con `newInstance(...)` y `show(getSupportFragmentManager(), tag)`, por lo que
el sistema los restaura solo tras una rotación.

## 8. Recursos e internacionalización

- **Textos**: `values/strings.xml` (español, idioma por defecto) y `values-en/strings.xml`.
  Las dos listas tienen exactamente las mismas claves.
- **Arrays**: `values/arrays.xml` y `values-en/arrays.xml` (tipos, presentaciones y
  especialidades).
- **Colores**: `values/colors.xml` y `values-night/colors.xml`.
- **Estilos**: `Theme.ElPastillero`, `Estilo.Titulo`, `Estilo.Tarjeta`, `Estilo.Etiqueta`.
- **Iconos**: vectores en `drawable/`; icono adaptativo en `mipmap-anydpi-v26` y versiones
  de respaldo en `mipmap-mdpi` … `mipmap-xxxhdpi`.

No hay ningún texto literal visible en los layouts: todo pasa por `strings.xml`.

## 9. Tipos de layout usados

| Tipo | Dónde |
|---|---|
| `DrawerLayout` | `activity_main.xml` |
| `ConstraintLayout` | login, listados y lista personal |
| `LinearLayout` | formularios, tarjetas, cabecera del menú |
| `TableLayout` + `TableRow` | ficha del medicamento |
| `ScrollView` / `NestedScrollView` | formularios y detalle |
| `CardView` | todas las tarjetas de los listados |

## 10. Componentes de interfaz

**Básicos**: `TextView`, `EditText`, `Button`, `ImageView`, `Spinner`, `CheckBox`,
`RadioButton`, `ProgressBar`, `SeekBar`, `RatingBar`, `DatePicker`, `TimePicker`.

**Avanzados**: `RecyclerView` con `CardView`, `NavigationView` + `DrawerLayout`,
`MaterialToolbar`, `FloatingActionButton`, `SwitchMaterial`, `SearchView`, `DialogFragment`.

## 11. Menús

- **Menú de opciones** en cada pantalla principal: buscar, ordenar, añadir, editar,
  eliminar y acerca de.
- **Menú contextual** por pulsación larga en todas las tarjetas:
  `menu_contextual_medicamento`, `menu_contextual_toma`, `menu_contextual_cita` y
  `menu_contextual_favorito`.
- **Menú lateral** (`menu_drawer.xml`) con 7 entradas.

La pantalla principal usa `Theme.ElPastillero.SinActionBar` y una `MaterialToolbar`
propia dentro de `activity_main.xml`. Así la toolbar y el `DrawerLayout` no se solapan:
el `ActionBarDrawerToggle` se engancha a la toolbar y la cabecera del menú (avatar,
nombre y correo) se ve completa.

Los adaptadores implementan `View.OnCreateContextMenuListener`; la actividad recoge la
selección en `onContextItemSelected` y consulta el elemento seleccionado con
`adapter.getSeleccionado()`.

## 12. Seguridad

`Seguridad.sha256()` guarda el hash de la contraseña en hexadecimal y compara hashes en el
login. Nunca se almacena la contraseña en claro.

> Nota académica: para un producto real convendría un algoritmo lento con sal (bcrypt,
> scrypt o Argon2). SHA-256 se ha elegido aquí por simplicidad y porque no añade
> dependencias.

## 13. Estructura de `DbHelper`

Métodos principales:

| Grupo | Métodos |
|---|---|
| Usuarios | `login`, `registrar`, `usuario`, `usuarios`, `emailEnUso`, `actualizarUsuario`, `borrarUsuario` |
| Lista personal | `favoritos`, `esFavorito`, `anadirFavorito`, `quitarFavorito`, `actualizarNota`, `borrarFavoritosDeMedicamento` |
| Citas | `citas`, `insertarCita`, `actualizarCita`, `borrarCita` |

Todos los `Cursor` se cierran en un bloque `finally`.

## 14. Cómo ampliar la aplicación

- **Nuevo campo en un medicamento**: añadirlo a la entidad `Medicamento`, subir
  `version` en `@Database` y añadir una `Migration` (o recrear la base en desarrollo).
- **Nueva pantalla**: crear la actividad, heredar de `BaseActivity`, registrarla en
  `AndroidManifest.xml` y añadir su entrada en `menu_drawer.xml` si procede.
- **Nuevo idioma**: duplicar `values/strings.xml` en `values-xx/strings.xml`.
- **Avisos reales**: sustituir el switch de Ajustes por `AlarmManager` +
  `NotificationManager` (requiere permisos de notificaciones en Android 13+).

## 15. Comprobaciones realizadas

- **Compilación real con Gradle**: `gradlew assembleDebug` termina con
  `BUILD SUCCESSFUL` (AGP 9.4.1, Gradle 9.6.0, JDK 17).
- Las referencias `R.string`, `R.id`, `R.layout`, `R.menu`, `R.drawable`, `R.array`,
  `R.color`, `R.dimen` y `R.style` del código existen en los recursos.
- Las 185 claves de `values/strings.xml` y `values-en/strings.xml` coinciden una a una,
  y los `plurals` de unidades existen en los dos idiomas.
- Todas las actividades declaradas en el `AndroidManifest.xml` tienen su clase Java.
- Los 19 layouts y los 11 menús se analizan sin errores.
- No queda ningún texto visible escrito a mano en los layouts ni en el código Java.

## 16. Bibliografía y webgrafía

- Documentación oficial de Android Developers: <https://developer.android.com/docs>
- Guía de Room: <https://developer.android.com/training/data-storage/room>
- Guía de `SQLiteOpenHelper`: <https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper>
- RecyclerView: <https://developer.android.com/develop/ui/views/layout/recyclerview>
- Navigation Drawer: <https://developer.android.com/develop/ui/views/navigation/navigation-drawer>
- `DialogFragment`: <https://developer.android.com/guide/fragments/dialogs>
- Material Components for Android: <https://m3.material.io/>
- Documentación de Java 17: <https://docs.oracle.com/en/java/javase/17/docs/api/>
- Apuntes y prácticas de la asignatura PMDM (IES Claudio Moyano, curso 2026-27).

## 17. Herramientas de IA utilizadas

Para la revisión final del proyecto se ha usado un agente de IA (OpenCode) como apoyo:

- **Qué se pidió**: revisar el proyecto contra el enunciado, detectar huecos y errores,
  y generar la documentación y los manuales en PDF.
- **Qué se revisó a mano**: el diseño de las pantallas, las decisiones de base de datos,
  los datos de ejemplo y todas las pruebas en el emulador.
- **Qué se corrigió tras la revisión**: el estado de los Spinner al girar la pantalla,
  la fecha del `DialogFechaHora` al rotar, el medicamento elegido en el alta de tomas,
  la búsqueda y el orden del listado, y la internacionalización de las credenciales
  de demostración.
- **Criterio**: la IA no ha decidido la arquitectura ni los datos; ha servido para
  localizar defectos concretos y para redactar documentación a partir del código real.
