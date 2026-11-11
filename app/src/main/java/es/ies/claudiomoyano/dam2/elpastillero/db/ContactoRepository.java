package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import es.ies.claudiomoyano.dam2.elpastillero.clases.clContacto;
import es.ies.claudiomoyano.dam2.elpastillero.clases.clUsuario;


public class ContactoRepository {
    private SQLiteDatabase midb;
    public ContactoRepository(BaseDatos helper) {
        midb = helper.getWritableDatabase();
    }

    public long insertarContacto(clContacto c){
        ContentValues r = new ContentValues();
        r.put(ContactoContact.ContactoEntry.COLUMN_idusuario, "1");
        r.put(ContactoContact.ContactoEntry.COLUMN_idfamiliar, "1");
        r.put(ContactoContact.ContactoEntry.COLUMN_nombre, c.getNombre());
        r.put(ContactoContact.ContactoEntry.COLUMN_telefono, c.getTelefono());
        r.put(ContactoContact.ContactoEntry.COLUMN_email, c.getEmail());
        r.put(ContactoContact.ContactoEntry.COLUMN_parentesco, c.getParentesco());
        r.put(ContactoContact.ContactoEntry.COLUMN_alertas, c.getAlertas());
        long resultado = midb.insert(ContactoContact.ContactoEntry.TABLE_NAME, null, r);
        return resultado;
    }

    public clContacto Busqueda() {
        clContacto con = new clContacto();
        Cursor cursor = midb.rawQuery(
                "SELECT * FROM " + ContactoContact.ContactoEntry.TABLE_NAME +
                        " WHERE " + ContactoContact.ContactoEntry._ID + " = ?",
                new String[]{"1"}
        );

        if (cursor.moveToFirst()) {
            con.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactoContact.ContactoEntry.COLUMN_nombre)));
            con.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactoContact.ContactoEntry.COLUMN_telefono)));
            con.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactoContact.ContactoEntry.COLUMN_email)));
            con.setParentesco(cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactoContact.ContactoEntry.COLUMN_parentesco)));
            con.setAlertas(cursor.getInt(cursor.getColumnIndexOrThrow(
                    ContactoContact.ContactoEntry.COLUMN_alertas)));
         }
        cursor.close();
        return con;
    }

    public int modificar(clContacto c){
        ContentValues r = new ContentValues();
        r.put(UsuarioContact.UsuarioEntry.COLUMN_NOMBRE, c.getNombre());
        r.put(ContactoContact.ContactoEntry.COLUMN_nombre, c.getNombre());
        r.put(ContactoContact.ContactoEntry.COLUMN_telefono, c.getTelefono());
        r.put(ContactoContact.ContactoEntry.COLUMN_email, c.getEmail());
        r.put(ContactoContact.ContactoEntry.COLUMN_parentesco, c.getParentesco());
        r.put(ContactoContact.ContactoEntry.COLUMN_alertas, c.getAlertas());
        int filas =  midb.update(
                ContactoContact.ContactoEntry.TABLE_NAME, r,
                ContactoContact.ContactoEntry.COLUMN_idfamiliar + " = ?",
                new String[]{String.valueOf("1")}
        );
        return filas;
    }
}
