package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogFechaHora;
import es.ies.claudiomoyano.dam2.elpastillero.model.Cita;

/**
 * Formulario de alta y edición de citas. La fecha y la hora se eligen con el
 * DialogFragment propio que contiene DatePicker y TimePicker.
 */
public class CitaEdicionActivity extends BaseActivity
        implements DialogFechaHora.OnFechaHoraListener {

    public static final String EXTRA_ID = "extra_id_cita";
    private static final String ESTADO_FECHA = "estado_fecha_cita";

    private DbHelper db;
    private long id = -1;
    private Cita cita;
    private long fechaHora = 0;

    private Spinner spEspecialidad;
    private EditText etMedico;
    private EditText etLugar;
    private EditText etNotas;
    private TextView txtFechaHoraCita;
    private TextView txtTituloCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita_edicion);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        id = getIntent().getLongExtra(EXTRA_ID, -1);
        db = new DbHelper(this);

        spEspecialidad = findViewById(R.id.spEspecialidad);
        etMedico = findViewById(R.id.etMedico);
        etLugar = findViewById(R.id.etLugar);
        etNotas = findViewById(R.id.etNotasCita);
        txtFechaHoraCita = findViewById(R.id.txtFechaHoraCita);
        txtTituloCita = findViewById(R.id.txtTituloCita);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.especialidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEspecialidad.setAdapter(adapter);

        if (savedInstanceState != null) {
            fechaHora = savedInstanceState.getLong(ESTADO_FECHA, 0);
            mostrarFecha();
        }

        if (id > 0) {
            txtTituloCita.setText(R.string.cita_editar);
            cargar(savedInstanceState == null);
        } else {
            txtTituloCita.setText(R.string.cita_nueva);
        }

        Button btnFechaHora = findViewById(R.id.btnElegirFechaHora);
        btnFechaHora.setOnClickListener(v -> DialogFechaHora
                .newInstance(fechaHora > 0 ? fechaHora : System.currentTimeMillis())
                .show(getSupportFragmentManager(), "fecha_cita"));

        findViewById(R.id.btnGuardarCita).setOnClickListener(v -> guardar());
        findViewById(R.id.btnCancelarCita).setOnClickListener(v -> finish());
    }

    private void cargar(boolean aplicar) {
        AppExecutors.io(() -> db.citas(Sesion.id(this)), lista -> {
            if (lista == null || isFinishing() || isDestroyed()) {
                return;
            }
            Cita encontrada = null;
            for (Cita c : lista) {
                if (c.id == id) {
                    encontrada = c;
                    break;
                }
            }
            if (encontrada == null) {
                return;
            }
            cita = encontrada;
            if (aplicar) {
                mostrar(encontrada);
            }
        });
    }

    private void mostrar(Cita c) {
        etMedico.setText(c.medico);
        etLugar.setText(c.lugar);
        etNotas.setText(c.notas);
        if (!TextUtils.isEmpty(c.especialidad)) {
            // Se busca la posición sin convertir el adaptador: así no hay
            // "unchecked cast" y funciona con cualquier ArrayAdapter.
            android.widget.Adapter adaptador = spEspecialidad.getAdapter();
            for (int i = 0; i < adaptador.getCount(); i++) {
                if (c.especialidad.contentEquals(String.valueOf(adaptador.getItem(i)))) {
                    spEspecialidad.setSelection(i);
                    break;
                }
            }
        }
        fechaHora = c.fechaHora;
        mostrarFecha();
    }

    private void mostrarFecha() {
        if (fechaHora > 0) {
            txtFechaHoraCita.setText(getString(R.string.cita_fecha_hora,
                    FechaUtil.fecha(fechaHora), FechaUtil.hora(fechaHora)));
        } else {
            txtFechaHoraCita.setText(R.string.cita_sin_fecha);
        }
    }

    private void guardar() {
        if (fechaHora <= 0) {
            Toast.makeText(this, R.string.cita_falta_fecha, Toast.LENGTH_SHORT).show();
            return;
        }
        if (id > 0 && cita == null) {
            Toast.makeText(this, R.string.error_generico, Toast.LENGTH_SHORT).show();
            return;
        }

        final boolean esNueva = id <= 0;
        final Cita c = esNueva ? new Cita() : cita;

        c.idUsuario = Sesion.id(this);
        c.especialidad = spEspecialidad.getSelectedItem() == null
                ? "" : spEspecialidad.getSelectedItem().toString();
        c.medico = etMedico.getText().toString().trim();
        c.lugar = etLugar.getText().toString().trim();
        c.notas = etNotas.getText().toString().trim();
        c.fechaHora = fechaHora;

        AppExecutors.io(() -> {
            if (esNueva) {
                return db.insertarCita(c);
            }
            return (long) db.actualizarCita(c);
        }, resultado -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (resultado == null || resultado <= 0) {
                Toast.makeText(this, R.string.error_generico, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, esNueva ? R.string.cita_guardada : R.string.cita_actualizada,
                    Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void onFechaHora(int anio, int mes, int dia, int hora, int minuto) {
        fechaHora = FechaUtil.aMillis(anio, mes, dia, hora, minuto);
        mostrarFecha();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ESTADO_FECHA, fechaHora);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
