package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class CitaMedicaContract {
    private CitaMedicaContract() {
    }
    public static class CitaMedicaEntry implements BaseColumns{
        public static final String TABLE_NAME="CitaMedica";

        public static final String COLUMN_idcita = "idcita";
        public static final String COLUMN_idusuario = "idusuaio";
        public static final String COLUMN_fecha="fecha";
        public static final String COLUMN_hora="hora";
        public static final String COLUMN_lugar = "lugar";
        public static final  String COLUMN_especialista="espedialista";
        public static final  String COLUMN_nombremedico="nombremedico";
        public static final  String COLUMN_motivo="motivo";
        public static final  String COLUMN_observaciones= "observaciones";
        public static final  String COLUMN_estado="estado";
    }
}






