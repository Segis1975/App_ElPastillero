package es.ies.claudiomoyano.dam2.elpastillero.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import es.ies.claudiomoyano.dam2.elpastillero.R;

/**
 * DialogFragment propio con DatePicker y TimePicker.
 * Devuelve la fecha y la hora elegidas a la actividad que lo abre.
 */
public class DialogFechaHora extends DialogFragment {

    public interface OnFechaHoraListener {
        void onFechaHora(int anio, int mes, int dia, int hora, int minuto);
    }

    private static final String ARG_MILLIS = "millis";
    // Claves para conservar la selección del usuario al girar la pantalla.
    private static final String ESTADO_ANIO = "estado_anio";
    private static final String ESTADO_MES = "estado_mes";
    private static final String ESTADO_DIA = "estado_dia";
    private static final String ESTADO_HORA = "estado_hora";
    private static final String ESTADO_MINUTO = "estado_minuto";

    private DatePicker selectorFecha;
    private TimePicker selectorHora;

    public static DialogFechaHora newInstance(long millis) {
        DialogFechaHora dialogo = new DialogFechaHora();
        Bundle argumentos = new Bundle();
        argumentos.putLong(ARG_MILLIS, millis);
        dialogo.setArguments(argumentos);
        return dialogo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View vista = requireActivity().getLayoutInflater().inflate(R.layout.dialog_fecha_hora, null);

        selectorFecha = vista.findViewById(R.id.datePicker);
        selectorHora = vista.findViewById(R.id.timePicker);

        long millis = getArguments() != null
                ? getArguments().getLong(ARG_MILLIS, System.currentTimeMillis())
                : System.currentTimeMillis();

        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(millis);

        // Si el diálogo se recrea tras un giro, se recupera lo que el usuario
        // había elegido en lugar de volver a la fecha inicial.
        int anio = savedInstanceState != null
                ? savedInstanceState.getInt(ESTADO_ANIO, calendario.get(Calendar.YEAR))
                : calendario.get(Calendar.YEAR);
        int mes = savedInstanceState != null
                ? savedInstanceState.getInt(ESTADO_MES, calendario.get(Calendar.MONTH))
                : calendario.get(Calendar.MONTH);
        int dia = savedInstanceState != null
                ? savedInstanceState.getInt(ESTADO_DIA, calendario.get(Calendar.DAY_OF_MONTH))
                : calendario.get(Calendar.DAY_OF_MONTH);
        int hora = savedInstanceState != null
                ? savedInstanceState.getInt(ESTADO_HORA, calendario.get(Calendar.HOUR_OF_DAY))
                : calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = savedInstanceState != null
                ? savedInstanceState.getInt(ESTADO_MINUTO, calendario.get(Calendar.MINUTE))
                : calendario.get(Calendar.MINUTE);

        selectorFecha.init(anio, mes, dia, null);

        selectorHora.setIs24HourView(true);
        selectorHora.setHour(hora);
        selectorHora.setMinute(minuto);

        Dialog dialogo = new Dialog(requireContext());
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(vista);
        if (dialogo.getWindow() != null) {
            dialogo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        vista.findViewById(R.id.btnCancelarFecha).setOnClickListener(v -> dismiss());
        vista.findViewById(R.id.btnAceptarFecha).setOnClickListener(v -> {
            if (getActivity() instanceof OnFechaHoraListener) {
                ((OnFechaHoraListener) getActivity()).onFechaHora(
                        selectorFecha.getYear(),
                        selectorFecha.getMonth(),
                        selectorFecha.getDayOfMonth(),
                        selectorHora.getHour(),
                        selectorHora.getMinute());
            }
            dismiss();
        });

        return dialogo;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectorFecha != null && selectorHora != null) {
            outState.putInt(ESTADO_ANIO, selectorFecha.getYear());
            outState.putInt(ESTADO_MES, selectorFecha.getMonth());
            outState.putInt(ESTADO_DIA, selectorFecha.getDayOfMonth());
            outState.putInt(ESTADO_HORA, selectorHora.getHour());
            outState.putInt(ESTADO_MINUTO, selectorHora.getMinute());
        }
    }
}
