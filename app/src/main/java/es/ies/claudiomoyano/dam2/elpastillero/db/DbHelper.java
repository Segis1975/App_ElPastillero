package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.ies.claudiomoyano.dam2.elpastillero.FechaUtil;
import es.ies.claudiomoyano.dam2.elpastillero.Seguridad;
import es.ies.claudiomoyano.dam2.elpastillero.model.Cita;
import es.ies.claudiomoyano.dam2.elpastillero.model.Favorito;
import es.ies.claudiomoyano.dam2.elpastillero.model.Usuario;

/**
 * Base de datos SQLite del proyecto: usuarios, lista personal (favoritos) y citas.
 * Room se encarga de la entidad principal (medicamentos) y las tomas.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NOMBRE = "pastillero.db";
    public static final int DB_VERSION = 1;

    public static final String T_USUARIOS = "usuarios";
    public static final String T_FAVORITOS = "favoritos";
    public static final String T_CITAS = "citas";

    // Columnas de usuarios
    public static final String C_ID = "_id";
    public static final String C_NOMBRE = "nombre";
    public static final String C_EMAIL = "email";
    public static final String C_PASSWORD = "password";
    public static final String C_CREADO = "creado";

    // Columnas de favoritos
    public static final String C_ID_USUARIO = "idUsuario";
    public static final String C_ID_MEDICAMENTO = "idMedicamento";
    public static final String C_NOMBRE_MEDICAMENTO = "nombreMedicamento";
    public static final String C_NOTA = "nota";
    public static final String C_FECHA = "fecha";

    // Columnas de citas
    public static final String C_ESPECIALIDAD = "especialidad";
    public static final String C_MEDICO = "medico";
    public static final String C_LUGAR = "lugar";
    public static final String C_FECHA_HORA = "fechaHora";
    public static final String C_NOTAS = "notas";

    private static final String CREAR_USUARIOS =
            "CREATE TABLE " + T_USUARIOS + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    C_NOMBRE + " TEXT NOT NULL, " +
                    C_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    C_PASSWORD + " TEXT NOT NULL, " +
                    C_CREADO + " INTEGER NOT NULL)";

    private static final String CREAR_FAVORITOS =
            "CREATE TABLE " + T_FAVORITOS + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    C_ID_USUARIO + " INTEGER NOT NULL, " +
                    C_ID_MEDICAMENTO + " INTEGER NOT NULL, " +
                    C_NOMBRE_MEDICAMENTO + " TEXT NOT NULL, " +
                    C_NOTA + " TEXT, " +
                    C_FECHA + " INTEGER NOT NULL, " +
                    "UNIQUE(" + C_ID_USUARIO + ", " + C_ID_MEDICAMENTO + "), " +
                    "FOREIGN KEY (" + C_ID_USUARIO + ") REFERENCES " + T_USUARIOS + "(" + C_ID + ") ON DELETE CASCADE)";

    private static final String CREAR_CITAS =
            "CREATE TABLE " + T_CITAS + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    C_ID_USUARIO + " INTEGER NOT NULL, " +
                    C_ESPECIALIDAD + " TEXT NOT NULL, " +
                    C_MEDICO + " TEXT, " +
                    C_LUGAR + " TEXT, " +
                    C_FECHA_HORA + " INTEGER NOT NULL, " +
                    C_NOTAS + " TEXT, " +
                    "FOREIGN KEY (" + C_ID_USUARIO + ") REFERENCES " + T_USUARIOS + "(" + C_ID + ") ON DELETE CASCADE)";

    public DbHelper(Context contexto) {
        super(contexto, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // SQLite ignora las claves ajenas si no se activan explícitamente.
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_USUARIOS);
        db.execSQL(CREAR_FAVORITOS);
        db.execSQL(CREAR_CITAS);
        sembrar(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + T_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + T_FAVORITOS);
        db.execSQL("DROP TABLE IF EXISTS " + T_USUARIOS);
        onCreate(db);
    }

    /** Carga inicial: 2 usuarios, 2 elementos de lista personal y 2 citas. */
    private void sembrar(SQLiteDatabase db) {
        long ahora = System.currentTimeMillis();

        long sergio = insertarUsuario(db, "Sergio", "sergio@pastillero.es", "1234", ahora);
        long ana = insertarUsuario(db, "Ana", "ana@pastillero.es", "1234", ahora);

        // Cada usuario tiene su propia lista personal.
        insertarFavorito(db, sergio, 1, "Paracetamol", "Para el dolor de cabeza", ahora);
        insertarFavorito(db, sergio, 2, "Ibuprofeno", "Solo si hay inflamación", ahora);
        // Ana empieza con la lista vacía para demostrar que son independientes.

        Cita cita1 = new Cita();
        cita1.idUsuario = sergio;
        cita1.especialidad = "Medicina general";
        cita1.medico = "Dra. Ruiz";
        cita1.lugar = "Centro de salud";
        cita1.fechaHora = FechaUtil.manana(10, 0);
        cita1.notas = "Revisión de la medicación";
        insertarCita(db, cita1);

        Cita cita2 = new Cita();
        cita2.idUsuario = sergio;
        cita2.especialidad = "Cardiología";
        cita2.medico = "Dr. Molina";
        cita2.lugar = "Hospital Clínico";
        cita2.fechaHora = FechaUtil.dentroDeDias(10, 17, 30);
        cita2.notas = "Llevar el informe de tomas";
        insertarCita(db, cita2);
    }

    private long insertarUsuario(SQLiteDatabase db, String nombre, String email, String password, long creado) {
        ContentValues valores = new ContentValues();
        valores.put(C_NOMBRE, nombre);
        valores.put(C_EMAIL, email.toLowerCase(Locale.ROOT));
        valores.put(C_PASSWORD, Seguridad.sha256(password));
        valores.put(C_CREADO, creado);
        return db.insert(T_USUARIOS, null, valores);
    }

    private void insertarFavorito(SQLiteDatabase db, long idUsuario, long idMedicamento,
                                  String nombre, String nota, long fecha) {
        ContentValues valores = new ContentValues();
        valores.put(C_ID_USUARIO, idUsuario);
        valores.put(C_ID_MEDICAMENTO, idMedicamento);
        valores.put(C_NOMBRE_MEDICAMENTO, nombre);
        valores.put(C_NOTA, nota);
        valores.put(C_FECHA, fecha);
        db.insertWithOnConflict(T_FAVORITOS, null, valores, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private long insertarCita(SQLiteDatabase db, Cita cita) {
        ContentValues valores = new ContentValues();
        valores.put(C_ID_USUARIO, cita.idUsuario);
        valores.put(C_ESPECIALIDAD, cita.especialidad);
        valores.put(C_MEDICO, cita.medico);
        valores.put(C_LUGAR, cita.lugar);
        valores.put(C_FECHA_HORA, cita.fechaHora);
        valores.put(C_NOTAS, cita.notas);
        return db.insert(T_CITAS, null, valores);
    }

    // ---------------------------------------------------------------- Usuarios

    /** Devuelve el usuario si el correo y la contraseña coinciden, o null si no. */
    public Usuario login(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_USUARIOS, new String[]{C_ID, C_NOMBRE, C_EMAIL},
                C_EMAIL + " = ? AND " + C_PASSWORD + " = ?",
                new String[]{email.trim().toLowerCase(Locale.ROOT), Seguridad.sha256(password)},
                null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return new Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    /** Crea un usuario. Devuelve el id, o -1 si el correo ya existe. */
    public long registrar(String nombre, String email, String password) {
        if (emailEnUso(email, -1)) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        return insertarUsuario(db, nombre.trim(), email.trim(), password, System.currentTimeMillis());
    }

    public Usuario usuario(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_USUARIOS, new String[]{C_ID, C_NOMBRE, C_EMAIL},
                C_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return new Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public List<Usuario> usuarios() {
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_USUARIOS, new String[]{C_ID, C_NOMBRE, C_EMAIL},
                null, null, null, null, C_NOMBRE + " COLLATE NOCASE");
        try {
            while (cursor.moveToNext()) {
                lista.add(new Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    public boolean emailEnUso(String email, long exceptoId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_USUARIOS, new String[]{C_ID},
                C_EMAIL + " = ? AND " + C_ID + " <> ?",
                new String[]{email.trim().toLowerCase(Locale.ROOT), String.valueOf(exceptoId)},
                null, null, null);
        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
        }
    }

    public int actualizarUsuario(long id, String nombre, String email) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(C_NOMBRE, nombre.trim());
        valores.put(C_EMAIL, email.trim().toLowerCase(Locale.ROOT));
        return db.update(T_USUARIOS, valores, C_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /** Al borrar el usuario, sus favoritos y citas se borran en cascada. */
    public int borrarUsuario(long id) {
        return getWritableDatabase().delete(T_USUARIOS, C_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // -------------------------------------------------------------- Favoritos

    public List<Favorito> favoritos(long idUsuario) {
        List<Favorito> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_FAVORITOS, null,
                C_ID_USUARIO + " = ?", new String[]{String.valueOf(idUsuario)},
                null, null, C_FECHA + " DESC");
        try {
            while (cursor.moveToNext()) {
                Favorito f = new Favorito();
                f.id = cursor.getLong(cursor.getColumnIndexOrThrow(C_ID));
                f.idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(C_ID_USUARIO));
                f.idMedicamento = cursor.getLong(cursor.getColumnIndexOrThrow(C_ID_MEDICAMENTO));
                f.nombreMedicamento = cursor.getString(cursor.getColumnIndexOrThrow(C_NOMBRE_MEDICAMENTO));
                f.nota = cursor.getString(cursor.getColumnIndexOrThrow(C_NOTA));
                f.fecha = cursor.getLong(cursor.getColumnIndexOrThrow(C_FECHA));
                lista.add(f);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    public boolean esFavorito(long idUsuario, long idMedicamento) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_FAVORITOS, new String[]{C_ID},
                C_ID_USUARIO + " = ? AND " + C_ID_MEDICAMENTO + " = ?",
                new String[]{String.valueOf(idUsuario), String.valueOf(idMedicamento)},
                null, null, null);
        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
        }
    }

    /** Devuelve el id de la fila o -1 si ya estaba en la lista. */
    public long anadirFavorito(long idUsuario, long idMedicamento, String nombre, String nota) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(C_ID_USUARIO, idUsuario);
        valores.put(C_ID_MEDICAMENTO, idMedicamento);
        valores.put(C_NOMBRE_MEDICAMENTO, nombre);
        valores.put(C_NOTA, nota);
        valores.put(C_FECHA, System.currentTimeMillis());
        return db.insertWithOnConflict(T_FAVORITOS, null, valores, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public int quitarFavorito(long idUsuario, long idMedicamento) {
        return getWritableDatabase().delete(T_FAVORITOS,
                C_ID_USUARIO + " = ? AND " + C_ID_MEDICAMENTO + " = ?",
                new String[]{String.valueOf(idUsuario), String.valueOf(idMedicamento)});
    }

    /** Quita el medicamento de las listas de todos los usuarios al borrarlo. */
    public int borrarFavoritosDeMedicamento(long idMedicamento) {
        return getWritableDatabase().delete(T_FAVORITOS,
                C_ID_MEDICAMENTO + " = ?", new String[]{String.valueOf(idMedicamento)});
    }

    public int actualizarNota(long idUsuario, long idMedicamento, String nota) {
        ContentValues valores = new ContentValues();
        valores.put(C_NOTA, nota);
        return getWritableDatabase().update(T_FAVORITOS, valores,
                C_ID_USUARIO + " = ? AND " + C_ID_MEDICAMENTO + " = ?",
                new String[]{String.valueOf(idUsuario), String.valueOf(idMedicamento)});
    }

    // ------------------------------------------------------------------ Citas

    public List<Cita> citas(long idUsuario) {
        List<Cita> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(T_CITAS, null,
                C_ID_USUARIO + " = ?", new String[]{String.valueOf(idUsuario)},
                null, null, C_FECHA_HORA + " ASC");
        try {
            while (cursor.moveToNext()) {
                Cita c = new Cita();
                c.id = cursor.getLong(cursor.getColumnIndexOrThrow(C_ID));
                c.idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow(C_ID_USUARIO));
                c.especialidad = cursor.getString(cursor.getColumnIndexOrThrow(C_ESPECIALIDAD));
                c.medico = cursor.getString(cursor.getColumnIndexOrThrow(C_MEDICO));
                c.lugar = cursor.getString(cursor.getColumnIndexOrThrow(C_LUGAR));
                c.fechaHora = cursor.getLong(cursor.getColumnIndexOrThrow(C_FECHA_HORA));
                c.notas = cursor.getString(cursor.getColumnIndexOrThrow(C_NOTAS));
                lista.add(c);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    public long insertarCita(Cita cita) {
        return insertarCita(getWritableDatabase(), cita);
    }

    public int actualizarCita(Cita cita) {
        ContentValues valores = new ContentValues();
        valores.put(C_ESPECIALIDAD, cita.especialidad);
        valores.put(C_MEDICO, cita.medico);
        valores.put(C_LUGAR, cita.lugar);
        valores.put(C_FECHA_HORA, cita.fechaHora);
        valores.put(C_NOTAS, cita.notas);
        return getWritableDatabase().update(T_CITAS, valores,
                C_ID + " = ? AND " + C_ID_USUARIO + " = ?",
                new String[]{String.valueOf(cita.id), String.valueOf(cita.idUsuario)});
    }

    public int borrarCita(long idCita, long idUsuario) {
        return getWritableDatabase().delete(T_CITAS,
                C_ID + " = ? AND " + C_ID_USUARIO + " = ?",
                new String[]{String.valueOf(idCita), String.valueOf(idUsuario)});
    }
}
