package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import es.ies.claudiomoyano.dam2.elpastillero.clases.clUsuario;


public class UsuarioRepository {
    private SQLiteDatabase midb;
    public UsuarioRepository( BaseDatos helper) {
        midb = helper.getWritableDatabase();
    }

    public void insertarusuario(clUsuario u){
        ContentValues values = new ContentValues();
        values.put(UsuarioContact.UsuarioEntry.COLUMN_NOMBRE, u.getNombre());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_DNI, u.getDNI());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_DOMICILIO, u.getDomicilio());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_LOCALIDAD, u.getLocalidad());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_CODIGOPOSTAL, u.getCodigopostal());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_PROVINCIA, u.getprovincia());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_FECHANACIMIENTO, u.getFechanacimiento());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_TELEFONO, u.getTelefono());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_EMAIL, u.getEmail());
        values.put(UsuarioContact.UsuarioEntry.COLUMN_ACTIVO, u.getActivo());
        long resultdo = midb.insert(UsuarioContact.UsuarioEntry.TABLE_NAME, null, values);

    }
}
