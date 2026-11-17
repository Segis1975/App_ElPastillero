package es.ies.claudiomoyano.dam2.elpastillero.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ies.claudiomoyano.dam2.elpastillero.FechaUtil;
import es.ies.claudiomoyano.dam2.elpastillero.R;
import es.ies.claudiomoyano.dam2.elpastillero.data.TomaDetalle;

/**
 * Adaptador de tomas. Marca la toma con la casilla y permite editar o eliminar
 * desde el menú contextual.
 */
public class TomaAdapter extends RecyclerView.Adapter<TomaAdapter.ViewHolder>
        implements View.OnCreateContextMenuListener {

    public interface OnTomaListener {
        void onMarcarToma(TomaDetalle detalle, boolean tomada);
    }

    private final Context contexto;
    private final OnTomaListener listener;
    private final boolean mostrarMedicamento;
    private final List<TomaDetalle> elementos = new ArrayList<>();
    private TomaDetalle seleccionado;

    public TomaAdapter(Context contexto, boolean mostrarMedicamento, OnTomaListener listener) {
        this.contexto = contexto;
        this.mostrarMedicamento = mostrarMedicamento;
        this.listener = listener;
    }

    public void setElementos(List<TomaDetalle> lista) {
        elementos.clear();
        if (lista != null) {
            elementos.addAll(lista);
        }
        notifyDataSetChanged();
    }

    public TomaDetalle getSeleccionado() {
        return seleccionado;
    }

    public void quitarSeleccionado() {
        if (seleccionado == null) {
            return;
        }
        int posicion = elementos.indexOf(seleccionado);
        if (posicion >= 0) {
            elementos.remove(posicion);
            notifyItemRemoved(posicion);
        }
        seleccionado = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_toma, parent, false);
        vista.setOnCreateContextMenuListener(this);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TomaDetalle detalle = elementos.get(position);

        holder.medicamento.setText(contexto.getString(R.string.toma_medicamento, detalle.nombreMedicamento));
        holder.medicamento.setVisibility(mostrarMedicamento ? View.VISIBLE : View.GONE);
        holder.fecha.setText(FechaUtil.fechaHora(detalle.toma.fechaHora));
        holder.cantidad.setText(contexto.getResources().getQuantityString(
                R.plurals.toma_cantidad, detalle.toma.cantidad, detalle.toma.cantidad));
        holder.estado.setText(detalle.toma.tomada ? R.string.toma_tomada : R.string.toma_pendiente);
        holder.estado.setTextColor(ContextCompat.getColor(contexto,
                detalle.toma.tomada ? R.color.verde_primario : R.color.texto_secundario));
        holder.icono.setVisibility(detalle.toma.tomada ? View.VISIBLE : View.GONE);

        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(detalle.toma.tomada);
        holder.check.setOnCheckedChangeListener((buttonView, checked) ->
                listener.onMarcarToma(detalle, checked));

        holder.itemView.setOnLongClickListener(v -> {
            seleccionado = detalle;
            v.showContextMenu();
            return true;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (seleccionado == null) {
            return;
        }
        menu.setHeaderTitle(FechaUtil.fechaHora(seleccionado.toma.fechaHora));
        new MenuInflater(contexto).inflate(R.menu.menu_contextual_toma, menu);
        menu.findItem(R.id.ctx_marcar_toma).setVisible(!seleccionado.toma.tomada);
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox check;
        final TextView medicamento;
        final TextView fecha;
        final TextView cantidad;
        final TextView estado;
        final ImageView icono;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.chkToma);
            medicamento = itemView.findViewById(R.id.txtTomaMedicamento);
            fecha = itemView.findViewById(R.id.txtTomaFecha);
            cantidad = itemView.findViewById(R.id.txtTomaCantidad);
            estado = itemView.findViewById(R.id.txtTomaEstado);
            icono = itemView.findViewById(R.id.imgTomaEstado);
        }
    }
}
