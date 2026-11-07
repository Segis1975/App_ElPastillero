package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class MedicamentoContract {
    private MedicamentoContract(){}

    public static class MedicamentoEntry implements BaseColumns{

        public static final String TABLE_NAME ="medicamentos";
        public static final String COLUMN_idmedicamento=  "id_medicamento";
        public static final String COLUMN_idusuario= "id_usuario";
        public static final String COLUMN_idcategoria="id_categoria";
        public static final String COLUMN_nombre="nombre";
        public static final String COLUMN_familia="familia";
        public static final String COLUMN_laboratorio= "laboratorio";
        public static final String COLUMN_dosis="dosis";
        public static final String COLUMN_formato="formato";
        public static final String COLUMN_cantidad ="cantidad";
        public static final String COLUMN_stock=  "stock_minimo";
        public static final String COLUMN_fecha_inicio="fecha_inicio";
        public static final String COLUMN_fecha_fin= "fecha_fin";
        public static final String COLUMN_imagen="imagen";
        public static final String COLUMN_activo="activo";

    }
}
