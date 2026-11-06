package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class ContactoContact {
    private ContactoContact(){}

    public static class ContactoEntry implements BaseColumns{
        public static final String TABLE_NAME = "contacto";

        public static final String COLUMN_idfamiliar ="id_familiar";
        public static final String COLUMN_idusuario="id_usuario";
        public static final String COLUMN_nombre="nombre";
        public static final String COLUMN_telefono= "telefono";
        public static final String COLUMN_email="email";
        public static final String COLUMN_parentesco= "parentesco";
        public static final String COLUMN_alertas="recibe_alertas";
    }
}
