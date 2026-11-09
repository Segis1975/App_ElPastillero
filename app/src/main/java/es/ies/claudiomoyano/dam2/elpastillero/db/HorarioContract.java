package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class HorarioContract {
    private HorarioContract(){}
    public static class HorarioEntry implements BaseColumns{
        public static final String TABLE_NAME = "horario";

        public static final String COLUMN_idhorario="id_horario";
        public static final String COLUMN_idmedicamento = "id_medicamento";
        public static final String COLUMN_fecha=  "fecha";
        public static final String COLUMN_hora="hora";
        public static final String COLUMN_dosis= "dosis";
        public static final String COLUMN_diasSemana= "dias_semana";

        public static final String COLUMN_activo= "activo";

    }
}
