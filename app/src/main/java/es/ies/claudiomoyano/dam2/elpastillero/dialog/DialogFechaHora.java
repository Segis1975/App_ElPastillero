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

        DatePicker selectorFecha = vista.findViewById(R.id.datePicker);
        TimePicker selectorHora = vista.findViewById(R.id.timePicker);

        long millis = getArguments() != null
                ? getArguments().getLong(ARG_MILLIS, System.currentTimeMillis())
                : System.currentTimeMillis();

        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(millis);
        selectorFecha.init(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH), null);

        selectorHora.setIs24HourView(true);
        selectorHora.setHour(calendario.get(Calendar.HOUR_OF_DAY));
        selectorHora.setMinute(calendario.get(Calendar.MINUTE));

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
}
