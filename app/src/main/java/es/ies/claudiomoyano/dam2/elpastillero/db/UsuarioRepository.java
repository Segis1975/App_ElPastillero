package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbInterface;
import android.widget.Toast;

import org.jetbrains.annotations.TestOnly;

import es.ies.claudiomoyano.dam2.elpastillero.clases.clUsuario;


public class UsuarioRepository {
    private SQLiteDatabase midb;
    public UsuarioRepository( BaseDatos helper) {
        midb = helper.getWritableDatabase();
    }

    public long insertarusuario(clUsuario u){
        ContentValues r = new ContentValues();
        r.put(UsuarioContact.UsuarioEntry.COLUMN_NOMBRE, u.getNombre());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_DNI, u.getDNI());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_DOMICILIO, u.getDomicilio());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_LOCALIDAD,u.getLocalidad());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_CODIGOPOSTAL, u.getCodigopostal());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_PROVINCIA, u.getprovincia());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_FECHANACIMIENTO, u.getFechanacimiento());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_TELEFONO, u.getTelefono());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_EMAIL, u.getEmail());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_ACTIVO,"SI");
        long resultado = midb.insert(UsuarioContact.UsuarioEntry.TABLE_NAME, null, r);
        return resultado;
    }

    public clUsuario Busqueda() {
        clUsuario usu = new clUsuario();
        Cursor cursor = midb.rawQuery(
                "SELECT * FROM " + UsuarioContact.UsuarioEntry.TABLE_NAME +
                        " WHERE " + UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO + " = ?",
                new String[]{"1"}
        );

        if (cursor.moveToFirst()) {
            usu.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_NOMBRE)));
            usu.setDNI(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_DNI)));
            usu.setDomicilio(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_DOMICILIO)));
            usu.setLocalidad(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_LOCALIDAD)));
            usu.setCodigopostal(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_CODIGOPOSTAL)));
            usu.setprovincia(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_PROVINCIA)));
            usu.setFechanacimiento(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_FECHANACIMIENTO)));
            usu.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_TELEFONO)));
            usu.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(
                    UsuarioContact.UsuarioEntry.COLUMN_EMAIL)));
        }
        cursor.close();
        return usu;
    }

    public int modificar(clUsuario u){
        ContentValues r = new ContentValues();
        r.put(UsuarioContact.UsuarioEntry.COLUMN_NOMBRE, u.getNombre());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_DNI, u.getDNI());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_DOMICILIO, u.getDomicilio());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_LOCALIDAD,u.getLocalidad());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_CODIGOPOSTAL, u.getCodigopostal());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_PROVINCIA, u.getprovincia());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_FECHANACIMIENTO, u.getFechanacimiento());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_TELEFONO, u.getTelefono());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_EMAIL, u.getEmail());
        r.put(UsuarioContact.UsuarioEntry.COLUMN_ACTIVO,"SI");
        int filas =  midb.update(
                UsuarioContact.UsuarioEntry.TABLE_NAME,
                r,
                UsuarioContact.UsuarioEntry.COLUMN_IDUSUARIO + " = ?",
                new String[]{String.valueOf("1")}
        );

        return filas;
    }
}
