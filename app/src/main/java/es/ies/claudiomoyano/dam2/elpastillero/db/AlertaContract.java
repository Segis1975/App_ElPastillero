package es.ies.claudiomoyano.dam2.elpastillero.db;

import android.provider.BaseColumns;

public final class AlertaContract {
    private AlertaContract() {
    }

    public static class AlertaEntry implements BaseColumns {
        public static final String TABLE_NAME = "alerta";
        public static final String COLUMN_idusuatrio = "id_usuario";
        public static final String COLUMN_idmedicamento = "id_medicamento";
        public static final String COLUMN_idalerta = "id_alerta";
        public static final String COLUMN_tipo = "tipo";
        public static final String COLUMN_mensaje = "mensaje";
        public static final String COLUMN_fechahora = "fecha_hora";
        public static final String COLUMN_leida ="leida";
    }
}