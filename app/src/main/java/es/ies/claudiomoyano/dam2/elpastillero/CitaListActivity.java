package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.ies.claudiomoyano.dam2.elpastillero.adapter.CitaAdapter;
import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogConfirmar;
import es.ies.claudiomoyano.dam2.elpastillero.model.Cita;

/**
 * Listado de citas médicas (SQLite) con alta, edición y borrado.
 */
public class CitaListActivity extends BaseActivity
        implements DialogConfirmar.OnConfirmarListener {

    private static final String ESTADO_PENDIENTE = "estado_pendiente_cita";

    private DbHelper db;
    private CitaAdapter adapter;
    private TextView txtVacio;
    private long pendienteBorrarId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita_list);

        if (savedInstanceState != null) {
            pendienteBorrarId = savedInstanceState.getLong(ESTADO_PENDIENTE, -1);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.citas_titulo);
        }

        db = new DbHelper(this);
        txtVacio = findViewById(R.id.txtVacio);

        RecyclerView recyclerView = findViewById(R.id.rvCitas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitaAdapter(this, this::abrirEdicion);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAnadirCita).setOnClickListener(v -> abrirEdicion(null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> db.citas(idUsuario), lista -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            adapter.setElementos(lista);
            boolean vacio = lista == null || lista.isEmpty();
            txtVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
        });
    }

    private void abrirEdicion(Cita cita) {
        Intent intent = new Intent(this, CitaEdicionActivity.class);
        if (cita != null) {
            intent.putExtra(CitaEdicionActivity.EXTRA_ID, cita.id);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citas, menu);
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
            abrirEdicion(null);
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
        Cita cita = adapter.getSeleccionada();
        if (cita == null) {
            return super.onContextItemSelected(item);
        }
        int itemId = item.getItemId();
        if (itemId == R.id.ctx_editar_cita) {
            abrirEdicion(cita);
            return true;
        }
        if (itemId == R.id.ctx_eliminar_cita) {
            pendienteBorrarId = cita.id;
            DialogConfirmar.newInstance(R.string.cita_eliminar_titulo, R.string.cita_eliminar_msg)
                    .show(getSupportFragmentManager(), "borrar_cita");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onConfirmar() {
        if (pendienteBorrarId <= 0) {
            return;
        }
        final long idCita = pendienteBorrarId;
        pendienteBorrarId = -1;
        long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> db.borrarCita(idCita, idUsuario), filas -> {
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
    }
}
