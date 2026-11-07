package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class UsuarioContact {

    public UsuarioContact(){
    }

    public static class UsuarioEntry implements BaseColumns{
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_IDUSUARIO="idusuario";
        public static final String COLUMN_NOMBRE ="nombre";
        public static final String COLUMN_DNI="dni";
        public static final String COLUMN_DOMICILIO=     "domicilio";
        public static final String COLUMN_LOCALIDAD="localidad";
        public static final String COLUMN_CODIGOPOSTAL="codigopostal";
        public static final String COLUMN_PROVINCIA=     "prorvincia";
        public static final String COLUMN_TELEFONO="telefono";
        public static final String COLUMN_FECHANACIMIENTO="fecha_nacimiento";
        public static final String COLUMN_EMAIL="email";
        public static final String COLUMN_ACTIVO="activo";
   }
}
