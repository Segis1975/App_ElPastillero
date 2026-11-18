package es.ies.claudiomoyano.dam2.elpastillero.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ies.claudiomoyano.dam2.elpastillero.FechaUtil;
import es.ies.claudiomoyano.dam2.elpastillero.R;
import es.ies.claudiomoyano.dam2.elpastillero.model.Cita;

/**
 * Adaptador de citas médicas (SQLite). Un clic abre la edición y una pulsación
 * larga muestra el menú contextual.
 */
public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.ViewHolder>
        implements View.OnCreateContextMenuListener {

    public interface OnCitaClick {
        void onCita(Cita cita);
    }

    private final Context contexto;
    private final OnCitaClick click;
    private final List<Cita> elementos = new ArrayList<>();
    private Cita seleccionada;

    public CitaAdapter(Context contexto, OnCitaClick click) {
        this.contexto = contexto;
        this.click = click;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setElementos(List<Cita> lista) {
        elementos.clear();
        if (lista != null) {
            elementos.addAll(lista);
        }
        notifyDataSetChanged();
    }

    public Cita getSeleccionada() {
        return seleccionada;
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_cita, parent, false);
        vista.setOnCreateContextMenuListener(this);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cita cita = elementos.get(position);

        holder.especialidad.setText(cita.especialidad);
        holder.medico.setText(TextUtils.isEmpty(cita.medico) ? "" : cita.medico);
        holder.lugar.setText(TextUtils.isEmpty(cita.lugar) ? "" : cita.lugar);

        String fecha = FechaUtil.fecha(cita.fechaHora);
        String hora = FechaUtil.hora(cita.fechaHora);
        holder.fechaHora.setText(contexto.getString(R.string.cita_fecha_hora, fecha, hora));

        holder.itemView.setOnClickListener(v -> click.onCita(cita));
        holder.itemView.setOnLongClickListener(v -> {
            seleccionada = cita;
            v.showContextMenu();
            return true;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (seleccionada == null) {
            return;
        }
        menu.setHeaderTitle(seleccionada.especialidad);
        new MenuInflater(contexto).inflate(R.menu.menu_contextual_cita, menu);
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView especialidad;
        final TextView medico;
        final TextView fechaHora;
        final TextView lugar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            especialidad = itemView.findViewById(R.id.txtEspecialidad);
            medico = itemView.findViewById(R.id.txtMedico);
            fechaHora = itemView.findViewById(R.id.txtFechaHora);
            lugar = itemView.findViewById(R.id.txtLugar);
        }
    }
}
