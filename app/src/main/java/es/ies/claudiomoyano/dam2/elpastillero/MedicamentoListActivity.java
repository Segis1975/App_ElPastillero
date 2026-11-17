package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ies.claudiomoyano.dam2.elpastillero.adapter.MedicamentoAdapter;
import es.ies.claudiomoyano.dam2.elpastillero.data.AppDatabase;
import es.ies.claudiomoyano.dam2.elpastillero.data.Medicamento;
import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogConfirmar;

/**
 * Listado principal: RecyclerView con tarjetas, búsqueda, ordenación,
 * menú contextual y alta de medicamentos.
 */
public class MedicamentoListActivity extends BaseActivity
        implements DialogConfirmar.OnConfirmarListener {

    private static final int ORDEN_NOMBRE = 0;
    private static final int ORDEN_STOCK = 1;
    private static final int ORDEN_VALORACION = 2;

    private AppDatabase room;
    private DbHelper db;
    private MedicamentoAdapter adapter;
    private TextView txtVacio;
    private final List<Medicamento> datos = new ArrayList<>();
    private static final String ESTADO_PENDIENTE = "estado_pendiente_borrar";
    private static final String ESTADO_FILTRO = "estado_filtro";
    private static final String ESTADO_ORDEN = "estado_orden";

    private String filtro = "";
    private int orden = ORDEN_NOMBRE;
    // Se guarda el id (no el objeto) para que el borrado siga funcionando al rotar.
    private long pendienteBorrarId = -1;
    // Evita que una búsqueda antigua sobrescriba el resultado de la más reciente.
    private int generacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento_list);

        if (savedInstanceState != null) {
            pendienteBorrarId = savedInstanceState.getLong(ESTADO_PENDIENTE, -1);
            // Búsqueda y ordenación se conservan al girar la pantalla.
            filtro = savedInstanceState.getString(ESTADO_FILTRO, "");
            orden = savedInstanceState.getInt(ESTADO_ORDEN, ORDEN_NOMBRE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.med_titulo);
        }

        room = AppDatabase.get(this);
        db = new DbHelper(this);
        txtVacio = findViewById(R.id.txtVacio);

        RecyclerView recyclerView = findViewById(R.id.rvMedicamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicamentoAdapter(this, this::abrirFicha);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAnadir).setOnClickListener(v -> abrirFormulario(-1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        final int miGeneracion = ++generacion;
        AppExecutors.io(() -> {
            if (filtro.isEmpty()) {
                return room.medicamentoDao().todos();
            }
            return room.medicamentoDao().buscar("%" + filtro + "%");
        }, lista -> {
            // Si mientras tanto se ha lanzado otra búsqueda, se descarta.
            if (miGeneracion != generacion || isFinishing() || isDestroyed()) {
                return;
            }
            datos.clear();
            if (lista != null) {
                datos.addAll(lista);
            }
            ordenarYMostrar();
        });
    }

    private void ordenarYMostrar() {
        Comparator<Medicamento> comparador;
        if (orden == ORDEN_STOCK) {
            comparador = (a, b) -> Integer.compare(a.stock, b.stock);
        } else if (orden == ORDEN_VALORACION) {
            comparador = (a, b) -> Integer.compare(b.valoracion, a.valoracion);
        } else {
            comparador = (a, b) -> a.nombre.compareToIgnoreCase(b.nombre);
        }
        datos.sort(comparador);
        adapter.setElementos(datos);
        txtVacio.setVisibility(datos.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void abrirFicha(Medicamento medicamento) {
        Intent intent = new Intent(this, MedicamentoDetalleActivity.class);
        intent.putExtra(MedicamentoDetalleActivity.EXTRA_ID, medicamento.id);
        startActivity(intent);
    }

    private void abrirFormulario(long id) {
        Intent intent = new Intent(this, MedicamentoEdicionActivity.class);
        intent.putExtra(MedicamentoEdicionActivity.EXTRA_ID, id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_medicamentos, menu);

        MenuItem buscar = menu.findItem(R.id.action_buscar);
        if (buscar != null) {
            SearchView searchView = (SearchView) buscar.getActionView();
            searchView.setQueryHint(getString(R.string.med_buscar));
            // Al girar, la caja de búsqueda vuelve a mostrar el filtro activo.
            if (!filtro.isEmpty()) {
                searchView.setIconified(false);
                searchView.setQuery(filtro, false);
            }
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String nuevoTexto) {
                    filtro = nuevoTexto == null ? "" : nuevoTexto.trim();
                    cargar();
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_anadir) {
            abrirFormulario(-1);
            return true;
        }
        if (id == R.id.orden_nombre) {
            orden = ORDEN_NOMBRE;
            ordenarYMostrar();
            return true;
        }
        if (id == R.id.orden_stock) {
            orden = ORDEN_STOCK;
            ordenarYMostrar();
            return true;
        }
        if (id == R.id.orden_valoracion) {
            orden = ORDEN_VALORACION;
            ordenarYMostrar();
            return true;
        }
        if (id == R.id.action_acerca) {
            startActivity(new Intent(this, AcercaDeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Medicamento medicamento = adapter.getSeleccionado();
        if (medicamento == null) {
            return super.onContextItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.ctx_editar) {
            abrirFormulario(medicamento.id);
            return true;
        }
        if (id == R.id.ctx_eliminar) {
            pendienteBorrarId = medicamento.id;
            DialogConfirmar.newInstance(R.string.detalle_eliminar_titulo, R.string.detalle_eliminar_msg)
                    .show(getSupportFragmentManager(), "borrar_medicamento");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onConfirmar() {
        if (pendienteBorrarId <= 0) {
            return;
        }
        final long idBorrar = pendienteBorrarId;
        pendienteBorrarId = -1;
        AppExecutors.io(() -> {
            Medicamento medicamento = room.medicamentoDao().porId(idBorrar);
            if (medicamento != null) {
                room.medicamentoDao().borrar(medicamento);
            }
            db.borrarFavoritosDeMedicamento(idBorrar);
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
        outState.putLong(ESTADO_PENDIENTE, pendienteBorrarId);
        outState.putString(ESTADO_FILTRO, filtro);
        outState.putInt(ESTADO_ORDEN, orden);
    }
}
