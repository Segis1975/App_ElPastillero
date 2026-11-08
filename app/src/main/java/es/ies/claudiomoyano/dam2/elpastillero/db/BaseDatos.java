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
        // TABLA USUARIO
        db.execSQL(
            "CREATE TABLE " + UsuarioContact.UsuarioEntry.TABLE_NAME + "("+
             UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             UsuarioContact.UsuarioEntry.COLUMN_NOMBRE + " TEXT NOT NULL, " +
             UsuarioContact.UsuarioEntry.COLUMN_DNI + " TEXT UNIQUE, "+
             UsuarioContact.UsuarioEntry.COLUMN_DOMICILIO + " TEXT, "+
             UsuarioContact.UsuarioEntry.COLUMN_LOCALIDAD  + " TEXT, "+
             UsuarioContact.UsuarioEntry.COLUMN_CODIGOPOSTAL + " INT, "+
             UsuarioContact.UsuarioEntry.COLUMN_PROVINCIA + " TEXT, "+
             UsuarioContact.UsuarioEntry.COLUMN_FECHANACIMIENTO + " TEXT, " +
             UsuarioContact.UsuarioEntry.COLUMN_TELEFONO + " TEXT, "+
             UsuarioContact.UsuarioEntry.COLUMN_EMAIL + " TEXT, "+
             UsuarioContact.UsuarioEntry.COLUMN_ACTIVO + " TEXT)" );

        // TABLA CONTACTO
        db.execSQL(
            "CREATE TABLE " + ContactoContact.ContactoEntry.TABLE_NAME + " ("+
            ContactoContact.ContactoEntry.COLUMN_idfamiliar +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
           // ContactoContact.ContactoEntry.COLUMN_idusuario + " INTEGER NOT NULL, " +
            ContactoContact.ContactoEntry.COLUMN_nombre + " TEXT NOT NULL, " +
            ContactoContact.ContactoEntry.COLUMN_telefono + " TEXT, " +
            ContactoContact.ContactoEntry.COLUMN_email + " TEXT, " +
            ContactoContact.ContactoEntry.COLUMN_parentesco + " TEXT, " +
            ContactoContact.ContactoEntry.COLUMN_alertas + " INTEGER, " +
            "FOREIGN KEY (" + ContactoContact.ContactoEntry.COLUMN_idusuario +
                    ") REFERENCES "+ UsuarioContact.UsuarioEntry.TABLE_NAME+"("+
                    UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO+"))");

        // TABLA CATEGORIA

        db.execSQL(
            "CREATE TABLE " + CategoriaContract.CategoriaEntry.TABLE_NAME + " (" +
            CategoriaContract.CategoriaEntry.COLUMN_idcategoria + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CategoriaContract.CategoriaEntry.COLUMN_nombre + " TEXT NOT NULL UNIQUE, " +
            CategoriaContract.CategoriaEntry.COLUMN_descripcion + " TEXT)");

        // 4. TABLA MEDICAMENTO
        db.execSQL(
            "CREATE TABLE " + MedicamentoContract.MedicamentoEntry.TABLE_NAME + " (" +
            MedicamentoContract.MedicamentoEntry.COLUMN_idmedicamento + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
           // MedicamentoContract.MedicamentoEntry.COLUMN_idusuario + " INTEGER NOT NULL, " +
           // MedicamentoContract.MedicamentoEntry.COLUMN_idcategoria + "INTEGER NOT NULL, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_nombre + " TEXT NOT NULL, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_familia + " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_laboratorio +  " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_formato + " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_dosis + " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_cantidad + " INTEGER DEFAULT 0, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_stock +  "INTEGER DEFAULT 0, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_fecha_inicio + " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_fecha_fin + " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_imagen + " TEXT, " +
            MedicamentoContract.MedicamentoEntry.COLUMN_activo + "INTEGER DEFAULT 1, " +
            // tabla contacto
           "FOREIGN KEY (" + MedicamentoContract.MedicamentoEntry.COLUMN_idusuario +
                    ") REFERENCES "+ UsuarioContact.UsuarioEntry.TABLE_NAME+"("+
                    UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO+"), " +
           "FOREIGN KEY (" + MedicamentoContract.MedicamentoEntry.COLUMN_idcategoria +
                    ") REFERENCES " + CategoriaContract.CategoriaEntry.TABLE_NAME+"("+
                    CategoriaContract.CategoriaEntry.COLUMN_idcategoria+"))");


        // 5. TABLA HORARIO
        db.execSQL(
            "CREATE TABLE " + HorarioContract.HorarioEntry.TABLE_NAME + " (" +
            HorarioContract.HorarioEntry.COLUMN_idhorario + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            //HorarioContract.HorarioEntry.COLUMN_idmedicamento + " INTEGER NOT NULL, " +
            HorarioContract.HorarioEntry.COLUMN_hora + "TEXT NOT NULL, " +
            HorarioContract.HorarioEntry.COLUMN_dosis + " TEXT, " +
            HorarioContract.HorarioEntry.COLUMN_diasSemana + " TEXT, " +
            HorarioContract.HorarioEntry.COLUMN_activo + "INTEGER, "+
            "FOREIGN KEY ("+HorarioContract.HorarioEntry.COLUMN_idmedicamento +
                    ") REFERENCES "+ MedicamentoContract.MedicamentoEntry.TABLE_NAME+"("+
                     MedicamentoContract.MedicamentoEntry.COLUMN_idmedicamento+"))");

        // 6. TABLA TOMA
        db.execSQL(
            "CREATE TABLE " + TomaContract.TomaEntry.TABLE_NAME +" (" +
            TomaContract.TomaEntry.COLUMN_idtoma + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            //TomaContract.TomaEntry.COLUMN_idhorario + " INTEGER NOT NULL, " +
            TomaContract.TomaEntry.COLUMN_fecha + " TEXT NOT NULL, " +
            TomaContract.TomaEntry.COLUMN_horaprevista + " TEXT, " +
            TomaContract.TomaEntry.COLUMN_horareal + " TEXT, " +
            TomaContract.TomaEntry.COLUMN_estado + " TEXT DEFAULT 'PENDIENTE', " +
  //          "CHECK (estado IN ('PENDIENTE','TOMADA','OMITIDA')), " +
            TomaContract.TomaEntry.COLUMN_observaciones + " TEXT, " +
            "FOREIGN KEY (" + TomaContract.TomaEntry.COLUMN_idhorario +
                    ") REFERENCES " + HorarioContract.HorarioEntry.TABLE_NAME+"("+
                    HorarioContract.HorarioEntry.COLUMN_idhorario+"))");

        // 7. TABLA ALERTA
        db.execSQL(
            "CREATE TABLE "+AlertaContract.AlertaEntry.TABLE_NAME + " (" +
            AlertaContract.AlertaEntry.COLUMN_idalerta + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            // "id_usuario INTEGER NOT NULL, " +
            //"id_medicamento INTEGER, " +
            AlertaContract.AlertaEntry.COLUMN_tipo + " TEXT NOT NULL, " +
            AlertaContract.AlertaEntry.COLUMN_mensaje + " TEXT NOT NULL, " +
            AlertaContract.AlertaEntry.COLUMN_fechahora + " TEXT NOT NULL, " +
            AlertaContract.AlertaEntry.COLUMN_leida + " INTEGER DEFAULT 0, " +
            "FOREIGN KEY ("+ AlertaContract.AlertaEntry.COLUMN_idusuatrio +") REFERENCES "+
                    UsuarioContact.UsuarioEntry.TABLE_NAME+"("+UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO+"), "+
            "FOREIGN KEY ("+AlertaContract.AlertaEntry.COLUMN_idmedicamento +") REFERENCES "+
                    MedicamentoContract.MedicamentoEntry.TABLE_NAME+"("+
                    MedicamentoContract.MedicamentoEntry.COLUMN_idmedicamento+"))");
        // 8. TABLA CITA_MEDICA
        db.execSQL(
            "CREATE TABLE " +CitaMedicaContract.CitaMedicaEntry.TABLE_NAME +" (" +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_idcita + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_fecha + " TEXT NOT NULL, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_hora + " TEXT NOT NULL, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_lugar +" TEXT, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_especialista+ " TEXT, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_nombremedico+ " TEXT, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_motivo+ " TEXT, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_observaciones+ " TEXT, " +
            CitaMedicaContract.CitaMedicaEntry.COLUMN_estado+" TEXT DEFAULT 'PENDIENTE', " +
            "FOREIGN KEY ("+ CitaMedicaContract.CitaMedicaEntry.COLUMN_idusuario +
                    ") REFERENCES " + UsuarioContact.UsuarioEntry.TABLE_NAME+"("+
                    UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO+"))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
