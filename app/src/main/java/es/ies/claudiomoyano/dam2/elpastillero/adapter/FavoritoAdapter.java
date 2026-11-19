package es.ies.claudiomoyano.dam2.elpastillero.adapter;

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
import es.ies.claudiomoyano.dam2.elpastillero.model.Favorito;

/**
 * Adaptador de la lista personal (SQLite). Cada usuario ve únicamente sus filas.
 */
public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.ViewHolder>
        implements View.OnCreateContextMenuListener {

    public interface OnFavoritoClick {
        void onFavorito(Favorito favorito);
    }

    private final Context contexto;
    private final OnFavoritoClick click;
    private final List<Favorito> elementos = new ArrayList<>();
    private Favorito seleccionado;

    public FavoritoAdapter(Context contexto, OnFavoritoClick click) {
        this.contexto = contexto;
        this.click = click;
    }

    public void setElementos(List<Favorito> lista) {
        elementos.clear();
        if (lista != null) {
            elementos.addAll(lista);
        }
        notifyDataSetChanged();
    }

    public Favorito getSeleccionado() {
        return seleccionado;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_favorito, parent, false);
        vista.setOnCreateContextMenuListener(this);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorito favorito = elementos.get(position);

        holder.nombre.setText(favorito.nombreMedicamento);
        holder.nota.setText(TextUtils.isEmpty(favorito.nota)
                ? contexto.getString(R.string.sin_notas) : favorito.nota);
        holder.fecha.setText(FechaUtil.fecha(favorito.fecha));

        holder.itemView.setOnClickListener(v -> click.onFavorito(favorito));
        holder.itemView.setOnLongClickListener(v -> {
            seleccionado = favorito;
            v.showContextMenu();
            return true;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (seleccionado == null) {
            return;
        }
        menu.setHeaderTitle(seleccionado.nombreMedicamento);
        new MenuInflater(contexto).inflate(R.menu.menu_contextual_favorito, menu);
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nombre;
        final TextView nota;
        final TextView fecha;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreFav);
            nota = itemView.findViewById(R.id.txtNotaFav);
            fecha = itemView.findViewById(R.id.txtFechaFav);
        }
    }
}
