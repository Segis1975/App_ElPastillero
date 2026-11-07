package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class TomaContract {
    private TomaContract(){}

    public static class TomaEntry implements BaseColumns{
        public static final String TABLE_NAME ="toma";

        public static final String COLUMN_idtoma = "id_toma";
        public static final String COLUMN_idhorario= "id_horario";
        public static final String COLUMN_fecha=  "fecha";
        public static final String COLUMN_horaprevista = "hora_prevista";
        public static final String COLUMN_horareal = "hora_real";
        public static final String COLUMN_estado = "estado";
        public static final String COLUMN_observaciones = "observaciones";


    }
}
