package es.ies.claudiomoyano.dam2.elpastillero.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import es.ies.claudiomoyano.dam2.elpastillero.R;

/**
 * Diálogo de confirmación reutilizable. La actividad que lo abre implementa
 * OnConfirmarListener para recibir la respuesta.
 */
public class DialogConfirmar extends DialogFragment {

    public interface OnConfirmarListener {
        void onConfirmar();
    }

    private static final String ARG_TITULO = "titulo";
    private static final String ARG_MENSAJE = "mensaje";

    public static DialogConfirmar newInstance(int titulo, int mensaje) {
        DialogConfirmar dialogo = new DialogConfirmar();
        Bundle argumentos = new Bundle();
        argumentos.putInt(ARG_TITULO, titulo);
        argumentos.putInt(ARG_MENSAJE, mensaje);
        dialogo.setArguments(argumentos);
        return dialogo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int titulo = requireArguments().getInt(ARG_TITULO);
        int mensaje = requireArguments().getInt(ARG_MENSAJE);

        return new AlertDialog.Builder(requireContext())
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(R.string.aceptar, (dialogo, which) -> {
                    if (getActivity() instanceof OnConfirmarListener) {
                        ((OnConfirmarListener) getActivity()).onConfirmar();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create();
    }
}
