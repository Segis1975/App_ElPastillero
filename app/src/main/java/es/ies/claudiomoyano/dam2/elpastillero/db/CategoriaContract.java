package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class CategoriaContract {

    private CategoriaContract(){
    }
    public static class CategoriaEntry implements BaseColumns {
        public static final String TABLE_NAME = "categoria";

        public static final String COLUMN_idcategoria = "id_categoria";
        public static final String COLUMN_nombre = "nombre";
        public static final String COLUMN_descripcion = "descripcion";
    }
}
