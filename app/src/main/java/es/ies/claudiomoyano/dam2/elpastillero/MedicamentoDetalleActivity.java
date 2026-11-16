package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import es.ies.claudiomoyano.dam2.elpastillero.adapter.TomaAdapter;
import es.ies.claudiomoyano.dam2.elpastillero.data.AppDatabase;
import es.ies.claudiomoyano.dam2.elpastillero.data.Medicamento;
import es.ies.claudiomoyano.dam2.elpastillero.data.Toma;
import es.ies.claudiomoyano.dam2.elpastillero.data.TomaDetalle;
import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogConfirmar;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogFechaHora;

/**
 * Ficha del medicamento: datos, lista personal, tomas y acciones de edición
 * y borrado. También permite registrar nuevas tomas.
 */
public class MedicamentoDetalleActivity extends BaseActivity
        implements DialogConfirmar.OnConfirmarListener, DialogFechaHora.OnFechaHoraListener {

    public static final String EXTRA_ID = "extra_id_medicamento";

    private AppDatabase room;
    private DbHelper db;
    private long id;
    private Medicamento medicamento;
    private TomaAdapter adapter;
    private TextView txtSinTomas;
    private SwitchMaterial swFavorito;
    private boolean cargandoSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento_detalle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.detalle_titulo);
        }

        id = getIntent().getLongExtra(EXTRA_ID, -1);
        room = AppDatabase.get(this);
        db = new DbHelper(this);
        txtSinTomas = findViewById(R.id.txtSinTomas);
        swFavorito = findViewById(R.id.swFavorito);

        RecyclerView recyclerView = findViewById(R.id.rvTomas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new TomaAdapter(this, false, this::marcarToma);
        recyclerView.setAdapter(adapter);

        swFavorito.setOnCheckedChangeListener((buttonView, checked) -> {
            if (cargandoSwitch || medicamento == null) {
                return;
            }
            actualizarFavorito(checked);
        });

        Button btnEditar = findViewById(R.id.btnEditar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnAnadirToma = findViewById(R.id.btnAnadirToma);

        btnEditar.setOnClickListener(v -> {
            if (medicamento == null) {
                return;
            }
            Intent intent = new Intent(this, MedicamentoEdicionActivity.class);
            intent.putExtra(MedicamentoEdicionActivity.EXTRA_ID, medicamento.id);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> confirmarBorrado());
        btnAnadirToma.setOnClickListener(v -> DialogFechaHora
                .newInstance(System.currentTimeMillis())
                .show(getSupportFragmentManager(), "fecha_toma"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        if (id <= 0) {
            Toast.makeText(this, R.string.detalle_no_encontrado, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        final long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> {
            Datos datos = new Datos();
            datos.medicamento = room.medicamentoDao().porId(id);
            datos.tomas = room.tomaDao().detallesDe(id);
            // La consulta a SQLite también se hace fuera del hilo principal.
            datos.favorito = db.esFavorito(idUsuario, id);
            return datos;
        }, datos -> {
            if (datos == null || isFinishing() || isDestroyed()) {
                return;
            }
            if (datos.medicamento == null) {
                Toast.makeText(this, R.string.detalle_no_encontrado, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            medicamento = datos.medicamento;
            mostrar(datos.medicamento, datos.tomas, datos.favorito);
        });
    }

    private void mostrar(Medicamento m, List<TomaDetalle> tomas, boolean favorito) {
        TextView txtNombre = findViewById(R.id.txtNombreDetalle);
        TextView txtPrincipio = findViewById(R.id.txtPrincipioDetalle);
        TextView txtStock = findViewById(R.id.txtStock);
        TextView txtDosis = findViewById(R.id.txtDosis);
        TextView txtEstado = findViewById(R.id.txtEstado);
        TextView txtFechaInicio = findViewById(R.id.txtFechaInicio);
        TextView txtNotas = findViewById(R.id.txtNotas);
        RatingBar rating = findViewById(R.id.ratingDetalle);
        ProgressBar progreso = findViewById(R.id.pbStockDetalle);

        txtNombre.setText(m.nombre);
        txtPrincipio.setText(TextUtils.isEmpty(m.principioActivo) ? "" : m.principioActivo);
        txtStock.setText(getString(R.string.detalle_stock_valor, m.stock, m.stockMinimo));
        txtDosis.setText(TextUtils.isEmpty(m.dosis) ? getString(R.string.no_disponible) : m.dosis);
        txtEstado.setText(getString(textoEstado(m.estado)));
        txtFechaInicio.setText(FechaUtil.fechaHora(m.fechaInicio));
        txtNotas.setText(TextUtils.isEmpty(m.notas) ? getString(R.string.sin_notas) : m.notas);
        rating.setRating(m.valoracion);

        progreso.setMax(Math.max(1, Math.max(m.stock, m.stockMinimo)));
        progreso.setProgress(m.stock);

        cargandoSwitch = true;
        swFavorito.setChecked(favorito);
        cargandoSwitch = false;

        adapter.setElementos(tomas);
        boolean vacio = tomas == null || tomas.isEmpty();
        txtSinTomas.setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    private void actualizarFavorito(boolean favorito) {
        Medicamento m = medicamento;
        long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> {
            m.favorito = favorito;
            room.medicamentoDao().actualizar(m);
            if (favorito) {
                db.anadirFavorito(idUsuario, m.id, m.nombre, "");
            } else {
                db.quitarFavorito(idUsuario, m.id);
            }
            return true;
        }, resultado -> {
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

    private void confirmarBorrado() {
        DialogConfirmar.newInstance(R.string.detalle_eliminar_titulo, R.string.detalle_eliminar_msg)
                .show(getSupportFragmentManager(), "borrar_medicamento");
    }

    @Override
    public void onConfirmar() {
        if (id <= 0) {
            return;
        }
        final long idBorrar = id;
        AppExecutors.io(() -> {
            Medicamento m = room.medicamentoDao().porId(idBorrar);
            if (m != null) {
                room.medicamentoDao().borrar(m);
            }
            db.borrarFavoritosDeMedicamento(idBorrar);
            return true;
        }, resultado -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            Toast.makeText(this, R.string.borrado_ok, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onFechaHora(int anio, int mes, int dia, int hora, int minuto) {
        if (medicamento == null) {
            return;
        }
        long millis = FechaUtil.aMillis(anio, mes, dia, hora, minuto);
        int cantidad = Math.max(1, medicamento.unidadesPorToma);
        long idMedicamento = medicamento.id;
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

    private int textoEstado(String estado) {
        if ("pausado".equals(estado)) {
            return R.string.estado_pausado;
        }
        if ("finalizado".equals(estado)) {
            return R.string.estado_finalizado;
        }
        return R.string.estado_activo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        if (itemId == R.id.action_editar) {
            if (medicamento != null) {
                Intent intent = new Intent(this, MedicamentoEdicionActivity.class);
                intent.putExtra(MedicamentoEdicionActivity.EXTRA_ID, medicamento.id);
                startActivity(intent);
            }
            return true;
        }
        if (itemId == R.id.action_eliminar) {
            confirmarBorrado();
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

    private static class Datos {
        Medicamento medicamento;
        List<TomaDetalle> tomas;
        boolean favorito;
    }
}
