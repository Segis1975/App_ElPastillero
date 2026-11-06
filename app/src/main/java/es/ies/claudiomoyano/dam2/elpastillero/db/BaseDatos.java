package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    public static final String DB_NAME="ElPastillero.db";
    public static final int DB_Version = 1;
    public BaseDatos(Context context){
        super(context, DB_NAME, null, DB_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USUARIO (" +
            "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT NOT NULL, " +
            "dni TEXT UNIQUE, " +
            "fecha_nacimiento TEXT, " +
            "telefono TEXT, " +
            "email TEXT, " +
            "direccion TEXT, " +
            "localidad TEXT, "+
            "cp INT, "+
            "provincia TEXT, "    +
            "activo INTEGER DEFAULT 1)");

        // 2. TABLA FAMILIAR
        db.execSQL("CREATE TABLE FAMILIAR (" +
            "id_familiar INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "nombre TEXT NOT NULL, " +
            "telefono TEXT, " +
            "email TEXT, " +
            "parentesco TEXT, " +
            "recibe_alertas INTEGER DEFAULT 1, " +
            "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE)");

        // 3. TABLA CATEGORIA
        db.execSQL("CREATE TABLE CATEGORIA (" +
            "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT NOT NULL UNIQUE, " +
            "descripcion TEXT)");

        // 4. TABLA MEDICAMENTO
        db.execSQL("CREATE TABLE MEDICAMENTO (" +
            "id_medicamento INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "id_categoria INTEGER, " +
            "nombre TEXT NOT NULL, " +
            "principio_activo TEXT, " +
            "laboratorio TEXT, " +
            "dosis TEXT, " +
            "formato TEXT, " +
            "cantidad_total INTEGER DEFAULT 0, " +
            "stock_minimo INTEGER DEFAULT 0, " +
            "fecha_inicio TEXT, " +
            "fecha_fin TEXT, " +
            "instrucciones TEXT, " +
            "imagen TEXT, " +
            "activo INTEGER DEFAULT 1, " +
            "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE, " +
            "FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria) ON DELETE SET NULL)");

        // 5. TABLA HORARIO
        db.execSQL("CREATE TABLE HORARIO (" +
            "id_horario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_medicamento INTEGER NOT NULL, " +
            "hora TEXT NOT NULL, " +
            "dosis TEXT, " +
            "dias_semana TEXT, " +
            "frecuencia TEXT, " +
            "activo INTEGER DEFAULT 1, " +
            "FOREIGN KEY (id_medicamento) REFERENCES MEDICAMENTO(id_medicamento) ON DELETE CASCADE)");

        // 6. TABLA TOMA
        db.execSQL("CREATE TABLE TOMA (" +
            "id_toma INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_horario INTEGER NOT NULL, " +
            "fecha TEXT NOT NULL, " +
            "hora_prevista TEXT, " +
            "hora_real TEXT, " +
            "estado TEXT DEFAULT 'PENDIENTE' " +
            "CHECK (estado IN ('PENDIENTE','TOMADA','OMITIDA')), " +
            "observaciones TEXT, " +
            "FOREIGN KEY (id_horario) REFERENCES HORARIO(id_horario) ON DELETE CASCADE)");

        // 7. TABLA ALERTA
        db.execSQL("CREATE TABLE ALERTA (" +
            "id_alerta INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "id_medicamento INTEGER, " +
            "tipo TEXT NOT NULL, " +
            "mensaje TEXT NOT NULL, " +
            "fecha_hora TEXT NOT NULL, " +
            "leida INTEGER DEFAULT 0, " +
            "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE, " +
            "FOREIGN KEY (id_medicamento) REFERENCES MEDICAMENTO(id_medicamento) ON DELETE SET NULL)");

        // 8. TABLA CITA_MEDICA
        db.execSQL("CREATE TABLE CITA_MEDICA (" +
            "id_cita INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "fecha TEXT NOT NULL, " +
            "hora TEXT NOT NULL, " +
            "centro_medico TEXT, " +
            "especialidad TEXT, " +
            "medico TEXT, " +
            "motivo TEXT, " +
            "observaciones TEXT, " +
            "estado TEXT DEFAULT 'PENDIENTE', " +
            "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE)");

        // 9. TABLA HISTORIAL
        db.execSQL("CREATE TABLE HISTORIAL (" +
            "id_historial INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "fecha_hora TEXT NOT NULL, " +
            "tipo TEXT NOT NULL, " +
            "descripcion TEXT, " +
            "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE)");

        //10. TABLA CONFIGURACION
        db.execSQL("CREATE TABLE CONFIGURACION (" +
            "id_configuracion INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER NOT NULL UNIQUE, " +
            "tamano_letra INTEGER DEFAULT 16, " +
            "voz_activada INTEGER DEFAULT 0, " +
            "sonido_activado INTEGER DEFAULT 1, " +
            "vibracion INTEGER DEFAULT 1, " +
            "notificaciones INTEGER DEFAULT 1, " +
            "idioma TEXT DEFAULT 'es', " +
            "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
