package es.ies.claudiomoyano.dam2.elpastillero.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ies.claudiomoyano.dam2.elpastillero.R;
import es.ies.claudiomoyano.dam2.elpastillero.data.Medicamento;

/**
 * Adaptador del listado de medicamentos. La tarjeta abre la ficha con un clic
 * y muestra el menú contextual con una pulsación larga.
 */
public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder>
        implements View.OnCreateContextMenuListener {

    public interface OnMedicamentoClick {
        void onMedicamento(Medicamento medicamento);
    }

    private final Context contexto;
    private final OnMedicamentoClick click;
    private final List<Medicamento> elementos = new ArrayList<>();
    private Medicamento seleccionado;

    public MedicamentoAdapter(Context contexto, OnMedicamentoClick click) {
        this.contexto = contexto;
        this.click = click;
    }

    public void setElementos(List<Medicamento> lista) {
        elementos.clear();
        if (lista != null) {
            elementos.addAll(lista);
        }
        notifyDataSetChanged();
    }

    public Medicamento getSeleccionado() {
        return seleccionado;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_medicamento, parent, false);
        vista.setOnCreateContextMenuListener(this);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicamento medicamento = elementos.get(position);

        holder.nombre.setText(medicamento.nombre);
        holder.principio.setText(TextUtils.isEmpty(medicamento.principioActivo)
                ? "" : medicamento.principioActivo);
        holder.dosis.setText(medicamento.dosis);
        holder.valoracion.setRating(medicamento.valoracion);
        holder.stock.setText(contexto.getString(R.string.med_unidades, medicamento.stock));
        holder.alerta.setVisibility(medicamento.stockBajo() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> click.onMedicamento(medicamento));
        holder.itemView.setOnLongClickListener(v -> {
            seleccionado = medicamento;
            v.showContextMenu();
            return true;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (seleccionado == null) {
            return;
        }
        menu.setHeaderTitle(seleccionado.nombre);
        new MenuInflater(contexto).inflate(R.menu.menu_contextual_medicamento, menu);
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nombre;
        final TextView principio;
        final TextView dosis;
        final TextView stock;
        final RatingBar valoracion;
        final ImageView alerta;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombre);
            principio = itemView.findViewById(R.id.txtPrincipio);
            dosis = itemView.findViewById(R.id.txtDosis);
            stock = itemView.findViewById(R.id.txtStock);
            valoracion = itemView.findViewById(R.id.ratingItem);
            alerta = itemView.findViewById(R.id.imgAlerta);
        }
    }
}
