package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ies.claudiomoyano.dam2.elpastillero.adapter.FavoritoAdapter;
import es.ies.claudiomoyano.dam2.elpastillero.data.AppDatabase;
import es.ies.claudiomoyano.dam2.elpastillero.data.Medicamento;
import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogConfirmar;
import es.ies.claudiomoyano.dam2.elpastillero.model.Favorito;

/**
 * Lista personal de cada usuario (tabla favoritos de SQLite). Permite añadir
 * medicamentos, editar la nota y quitarlos de la lista.
 */
public class ListaPersonalActivity extends BaseActivity
        implements DialogConfirmar.OnConfirmarListener {

    private static final String ESTADO_PENDIENTE = "estado_pendiente_quitar";

    private DbHelper db;
    private AppDatabase room;
    private FavoritoAdapter adapter;
    private TextView txtVacio;
    private long pendienteQuitarId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_personal);

        if (savedInstanceState != null) {
            pendienteQuitarId = savedInstanceState.getLong(ESTADO_PENDIENTE, -1);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.fav_titulo);
        }

        db = new DbHelper(this);
        room = AppDatabase.get(this);
        txtVacio = findViewById(R.id.txtVacio);

        RecyclerView recyclerView = findViewById(R.id.rvFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritoAdapter(this, this::abrirFicha);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAnadirFavorito).setOnClickListener(v -> elegirMedicamento());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> db.favoritos(idUsuario), lista -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            adapter.setElementos(lista);
            boolean vacio = lista == null || lista.isEmpty();
            txtVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
        });
    }

    private void abrirFicha(Favorito favorito) {
        Intent intent = new Intent(this, MedicamentoDetalleActivity.class);
        intent.putExtra(MedicamentoDetalleActivity.EXTRA_ID, favorito.idMedicamento);
        startActivity(intent);
    }

    private void elegirMedicamento() {
        AppExecutors.io(() -> room.medicamentoDao().todos(), lista -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (lista == null || lista.isEmpty()) {
                Toast.makeText(this, R.string.fav_sin_medicamentos, Toast.LENGTH_SHORT).show();
                return;
            }
            String[] nombres = new String[lista.size()];
            for (int i = 0; i < lista.size(); i++) {
                nombres[i] = lista.get(i).nombre;
            }
            new AlertDialog.Builder(this)
                    .setTitle(R.string.fav_elegir)
                    .setItems(nombres, (dialogo, which) -> anadir(lista.get(which)))
                    .setNegativeButton(R.string.cancelar, null)
                    .show();
        });
    }

    private void anadir(Medicamento medicamento) {
        long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> {
            long resultado = db.anadirFavorito(idUsuario, medicamento.id, medicamento.nombre, "");
            if (resultado > 0) {
                medicamento.favorito = true;
                room.medicamentoDao().actualizar(medicamento);
            }
            return resultado;
        }, resultado -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (resultado == null || resultado <= 0) {
                Toast.makeText(this, R.string.fav_ya_esta, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.guardado_ok, Toast.LENGTH_SHORT).show();
            }
            cargar();
        });
    }

    private void editarNota(Favorito favorito) {
        View vista = getLayoutInflater().inflate(R.layout.dialog_nota, null);
        EditText etNota = vista.findViewById(R.id.etNotaFav);
        etNota.setText(favorito.nota);

        new AlertDialog.Builder(this)
                .setTitle(R.string.fav_nota)
                .setView(vista)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.guardar, (dialogo, which) -> {
                    String nota = etNota.getText().toString().trim();
                    long idUsuario = Sesion.id(this);
                    AppExecutors.io(
                            () -> db.actualizarNota(idUsuario, favorito.idMedicamento, nota),
                            filas -> {
                                if (isFinishing() || isDestroyed()) {
                                    return;
                                }
                                Toast.makeText(this, R.string.fav_nota_guardada, Toast.LENGTH_SHORT).show();
                                cargar();
                            });
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favoritos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        if (itemId == R.id.action_anadir) {
            elegirMedicamento();
            return true;
        }
        if (itemId == R.id.action_acerca) {
            startActivity(new Intent(this, AcercaDeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Favorito favorito = adapter.getSeleccionado();
        if (favorito == null) {
            return super.onContextItemSelected(item);
        }
        int itemId = item.getItemId();
        if (itemId == R.id.ctx_nota) {
            editarNota(favorito);
            return true;
        }
        if (itemId == R.id.ctx_quitar) {
            pendienteQuitarId = favorito.idMedicamento;
            DialogConfirmar.newInstance(R.string.fav_quitar_titulo, R.string.fav_quitar_msg)
                    .show(getSupportFragmentManager(), "quitar_favorito");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onConfirmar() {
        if (pendienteQuitarId <= 0) {
            return;
        }
        final long idMedicamento = pendienteQuitarId;
        pendienteQuitarId = -1;
        long idUsuario = Sesion.id(this);

        AppExecutors.io(() -> {
            db.quitarFavorito(idUsuario, idMedicamento);
            Medicamento medicamento = room.medicamentoDao().porId(idMedicamento);
            if (medicamento != null) {
                medicamento.favorito = false;
                room.medicamentoDao().actualizar(medicamento);
            }
            return true;
        }, resultado -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            Toast.makeText(this, R.string.borrado_ok, Toast.LENGTH_SHORT).show();
            cargar();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ESTADO_PENDIENTE, pendienteQuitarId);
    }
}
