# Esquema de la base de datos

El proyecto usa dos bases de datos con propósitos distintos:

| Base de datos | Tecnología | Archivo | Contenido |
|---|---|---|---|
| Datos clínicos del catálogo | Room | `pastillero_room.db` | Medicamentos y sus tomas |
| Datos de la cuenta | SQLite (`SQLiteOpenHelper`) | `pastillero.db` | Usuarios, lista personal y citas |

Las dos se crean automáticamente la primera vez que se abre la aplicación.

---

## 1. Room — `pastillero_room.db`

### 1.1 `medicamentos` (entidad principal)

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INTEGER PK AUTOINCREMENT | Identificador |
| `nombre` | TEXT | Nombre comercial |
| `principioActivo` | TEXT | Principio activo |
| `dosis` | TEXT | Texto libre, por ejemplo «1 comprimido cada 8 h» |
| `unidadesPorToma` | INTEGER | Unidades que se descuentan en cada toma |
| `stock` | INTEGER | Unidades disponibles |
| `stockMinimo` | INTEGER | Umbral de aviso de stock bajo |
| `frecuenciaHoras` | INTEGER | Cada cuántas horas se toma |
| `tipo` | TEXT | `pastilla`, `jarabe` o `inyeccion` |
| `presentacion` | TEXT | Comprimidos, cápsulas, vial… |
| `favorito` | INTEGER (boolean) | Marca de favorito |
| `valoracion` | INTEGER | 0 a 5 estrellas |
| `estado` | TEXT | `activo`, `pausado` o `finalizado` |
| `notas` | TEXT | Observaciones |
| `fechaInicio` | INTEGER | Milisegundos desde 1970 |
| `recordatorio` | INTEGER (boolean) | Recordatorio activo |
| `creado` | INTEGER | Fecha de alta |

### 1.2 `tomas` (entidad relacionada 1-N)

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INTEGER PK AUTOINCREMENT | Identificador |
| `idMedicamento` | INTEGER | Clave ajena a `medicamentos.id` |
| `fechaHora` | INTEGER | Milisegundos desde 1970 |
| `cantidad` | INTEGER | Unidades de esa toma |
| `tomada` | INTEGER (boolean) | 0 pendiente, 1 tomada |
| `observaciones` | TEXT | Notas de la toma |

**Relación:** `medicamentos` **1 — N** `tomas`.

```java
@ForeignKey(
        entity = Medicamento.class,
        parentColumns = "id",
        childColumns = "idMedicamento",
        onDelete = ForeignKey.CASCADE
)
```

Al borrar un medicamento, Room borra automáticamente todas sus tomas. Además se declara
un índice sobre `idMedicamento` para acelerar las consultas.

### 1.3 Datos iniciales

Se insertan directamente en el callback `onCreate` de `AppDatabase`:

- 6 medicamentos: Paracetamol, Ibuprofeno, Omeprazol, Metformina, Jarabe para la tos y Vitamina D.
- 1 toma asociada al primer medicamento (Paracetamol), para que la relación 1-N se vea
  desde el primer arranque.

---

## 2. SQLite — `pastillero.db`

Definida en `db/DbHelper.java` (`DB_VERSION = 1`).

### 2.1 `usuarios`

| Columna | Tipo | Restricciones |
|---|---|---|
| `_id` | INTEGER | PRIMARY KEY AUTOINCREMENT |
| `nombre` | TEXT | NOT NULL |
| `email` | TEXT | NOT NULL UNIQUE |
| `password` | TEXT | NOT NULL (SHA-256 en hexadecimal) |
| `creado` | INTEGER | NOT NULL |

Las contraseñas nunca se guardan en claro: `Seguridad.sha256()` calcula el hash antes de
insertar y antes de comparar en el login.

### 2.2 `favoritos` (lista personal de cada usuario)

| Columna | Tipo | Restricciones |
|---|---|---|
| `_id` | INTEGER | PRIMARY KEY AUTOINCREMENT |
| `idUsuario` | INTEGER | NOT NULL, FK → `usuarios._id` ON DELETE CASCADE |
| `idMedicamento` | INTEGER | NOT NULL (id de Room) |
| `nombreMedicamento` | TEXT | NOT NULL (copia para poder listar sin abrir Room) |
| `nota` | TEXT | Nota personal del usuario |
| `fecha` | INTEGER | NOT NULL |

Restricción `UNIQUE(idUsuario, idMedicamento)`: un mismo medicamento no puede repetirse
en la lista de un mismo usuario, pero sí puede estar en las listas de usuarios distintos.
Esa es la clave para que cada cuenta tenga su propia lista personal.

### 2.3 `citas`

| Columna | Tipo | Restricciones |
|---|---|---|
| `_id` | INTEGER | PRIMARY KEY AUTOINCREMENT |
| `idUsuario` | INTEGER | NOT NULL, FK → `usuarios._id` ON DELETE CASCADE |
| `especialidad` | TEXT | NOT NULL |
| `medico` | TEXT | |
| `lugar` | TEXT | |
| `fechaHora` | INTEGER | NOT NULL |
| `notas` | TEXT | |

Las claves ajenas se activan en `onConfigure()` con
`db.setForeignKeyConstraintsEnabled(true)`, porque SQLite las ignora por defecto.

### 2.4 Datos iniciales

- Usuario 1: `Sergio` · `sergio@pastillero.es` · contraseña `1234`.
- Usuario 2: `Ana` · `ana@pastillero.es` · contraseña `1234`.
- 2 elementos de la lista personal del usuario 1 (Paracetamol e Ibuprofeno).
- 2 citas del usuario 1 (Medicina general mañana a las 10:00 y Cardiología dentro de 10 días).

Ana no tiene favoritos ni citas: sirve para comprobar de un vistazo que los datos están
separados por usuario.

---

## 3. Relación entre las dos bases de datos

Room no conoce la tabla `favoritos`, así que el enlace se hace por el identificador:

- `favoritos.idMedicamento` guarda el `id` de `medicamentos`.
- Al abrir un elemento de la lista personal se consulta Room con ese id.
- Al borrar un medicamento se llama a `DbHelper.borrarFavoritosDeMedicamento(id)` para
  limpiar las listas de todos los usuarios, porque las dos bases de datos son
  independientes y Room no puede aplicar la cascada sobre SQLite.

`nombreMedicamento` se guarda también en `favoritos` para poder pintar la lista al instante,
aunque el medicamento se hubiera borrado.
