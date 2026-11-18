package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.ies.claudiomoyano.dam2.elpastillero.adapter.TomaAdapter;
import es.ies.claudiomoyano.dam2.elpastillero.data.AppDatabase;
import es.ies.claudiomoyano.dam2.elpastillero.data.Medicamento;
import es.ies.claudiomoyano.dam2.elpastillero.data.Toma;
import es.ies.claudiomoyano.dam2.elpastillero.data.TomaDetalle;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogFechaHora;

/**
 * Control de tomas: lista todas las tomas con el medicamento asociado,
 * permite marcarlas como tomadas (descontando stock) y registrar nuevas.
 */
public class TomaListActivity extends BaseActivity
        implements DialogFechaHora.OnFechaHoraListener {

    private AppDatabase room;
    private TomaAdapter adapter;
    private TextView txtVacio;
    // Se guarda el id (no el objeto) para que el alta de toma sobreviva a un giro
    // de pantalla mientras el diálogo de fecha y hora está abierto.
    private long medicamentoElegidoId = -1;
    private int cantidadElegida = 1;

    private static final String ESTADO_MEDICAMENTO = "estado_medicamento_toma";
    private static final String ESTADO_CANTIDAD = "estado_cantidad_toma";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toma_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.tomas_titulo);
        }

        room = AppDatabase.get(this);
        txtVacio = findViewById(R.id.txtVacio);

        if (savedInstanceState != null) {
            medicamentoElegidoId = savedInstanceState.getLong(ESTADO_MEDICAMENTO, -1);
            cantidadElegida = savedInstanceState.getInt(ESTADO_CANTIDAD, 1);
        }

        RecyclerView recyclerView = findViewById(R.id.rvTomas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TomaAdapter(this, true, this::marcarToma);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAnadirToma).setOnClickListener(v -> elegirMedicamento());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        AppExecutors.io(() -> room.tomaDao().detalles(), lista -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            adapter.setElementos(lista);
            boolean vacio = lista == null || lista.isEmpty();
            txtVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
        });
    }

    private void marcarToma(TomaDetalle detalle, boolean tomada) {
        AppExecutors.io(() -> {
            Toma toma = detalle.toma;
            toma.tomada = tomada;
            room.tomaDao().actualizar(toma);
            if (tomada) {
                room.medicamentoDao().descontarStock(toma.idMedicamento, toma.cantidad);
            } else {
                room.medicamentoDao().devolverStock(toma.idMedicamento, toma.cantidad);
            }
            return true;
        }, resultado -> cargar());
    }

    private void elegirMedicamento() {
        AppExecutors.io(() -> room.medicamentoDao().todos(), lista -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (lista == null || lista.isEmpty()) {
                Toast.makeText(this, R.string.toma_sin_medicamentos, Toast.LENGTH_SHORT).show();
                return;
            }
            String[] nombres = new String[lista.size()];
            for (int i = 0; i < lista.size(); i++) {
                nombres[i] = lista.get(i).nombre;
            }
            new AlertDialog.Builder(this)
                    .setTitle(R.string.toma_elegir_medicamento)
                    .setItems(nombres, (dialogo, which) -> {
                        Medicamento elegido = lista.get(which);
                        medicamentoElegidoId = elegido.id;
                        cantidadElegida = Math.max(1, elegido.unidadesPorToma);
                        DialogFechaHora.newInstance(System.currentTimeMillis())
                                .show(getSupportFragmentManager(), "fecha_toma");
                    })
                    .setNegativeButton(R.string.cancelar, null)
                    .show();
        });
    }

    @Override
    public void onFechaHora(int anio, int mes, int dia, int hora, int minuto) {
        if (medicamentoElegidoId <= 0) {
            return;
        }
        final long millis = FechaUtil.aMillis(anio, mes, dia, hora, minuto);
        final long idMedicamento = medicamentoElegidoId;
        final int cantidad = Math.max(1, cantidadElegida);
        medicamentoElegidoId = -1;
        cantidadElegida = 1;

        AppExecutors.io(() -> {
            Toma toma = new Toma();
            toma.idMedicamento = idMedicamento;
            toma.fechaHora = millis;
            toma.cantidad = cantidad;
            toma.tomada = false;
            toma.observaciones = "";
            return room.tomaDao().insertar(toma);
        }, nuevoId -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (nuevoId != null && nuevoId > 0) {
                Toast.makeText(this, R.string.toma_marcada, Toast.LENGTH_SHORT).show();
            }
            cargar();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tomas, menu);
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
        TomaDetalle detalle = adapter.getSeleccionado();
        if (detalle == null) {
            return super.onContextItemSelected(item);
        }
        int itemId = item.getItemId();
        if (itemId == R.id.ctx_marcar_toma) {
            marcarToma(detalle, true);
            return true;
        }
        if (itemId == R.id.ctx_eliminar_toma) {
            AppExecutors.io(() -> {
                room.tomaDao().borrar(detalle.toma);
                return true;
            }, resultado -> cargar());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ESTADO_MEDICAMENTO, medicamentoElegidoId);
        outState.putInt(ESTADO_CANTIDAD, cantidadElegida);
    }
}
