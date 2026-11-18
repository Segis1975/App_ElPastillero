package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.switchmaterial.SwitchMaterial;

import es.ies.claudiomoyano.dam2.elpastillero.data.AppDatabase;
import es.ies.claudiomoyano.dam2.elpastillero.data.Medicamento;
import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogFechaHora;

/**
 * Formulario de alta y edición de medicamentos. Usa componentes básicos y
 * avanzados (Switch, SeekBar, RatingBar, RadioGroup) y spinners dependientes.
 */
public class MedicamentoEdicionActivity extends BaseActivity
        implements DialogFechaHora.OnFechaHoraListener {

    public static final String EXTRA_ID = "extra_id_medicamento";
    private static final String ESTADO_FECHA = "estado_fecha";
    private static final String ESTADO_TIPO = "estado_tipo";
    private static final String ESTADO_PRESENTACION = "estado_presentacion";

    private AppDatabase room;
    private DbHelper db;
    private long id = -1;
    private Medicamento medicamento;
    private long fechaMillis = System.currentTimeMillis();
    private String presentacionPendiente;

    private EditText etNombre;
    private EditText etPrincipio;
    private EditText etDosis;
    private EditText etStock;
    private EditText etStockMinimo;
    private EditText etFrecuencia;
    private EditText etNotas;
    private SeekBar seekUnidades;
    private TextView txtUnidades;
    private TextView txtFechaInicio;
    private TextView txtTituloForm;
    private Spinner spTipo;
    private Spinner spPresentacion;
    private RadioButton rbActivo;
    private RadioButton rbPausado;
    private RadioButton rbFinalizado;
    private SwitchMaterial swRecordatorio;
    private SwitchMaterial swFavorito;
    private RatingBar ratingValoracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento_edicion);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        id = getIntent().getLongExtra(EXTRA_ID, -1);
        room = AppDatabase.get(this);
        db = new DbHelper(this);

        enlazarVistas();
        configurarSpinners();
        configurarSeekBar();

        if (savedInstanceState != null) {
            fechaMillis = savedInstanceState.getLong(ESTADO_FECHA, System.currentTimeMillis());
            restaurarSpinners(savedInstanceState);
        }
        txtFechaInicio.setText(FechaUtil.fechaHora(fechaMillis));

        if (id > 0) {
            txtTituloForm.setText(R.string.form_editar);
            cargar(savedInstanceState == null);
        } else {
            txtTituloForm.setText(R.string.form_nuevo);
        }

        Button btnFecha = findViewById(R.id.btnFechaInicio);
        btnFecha.setOnClickListener(v -> DialogFechaHora
                .newInstance(fechaMillis)
                .show(getSupportFragmentManager(), "fecha_inicio"));

        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardar());
        findViewById(R.id.btnCancelarForm).setOnClickListener(v -> finish());
    }

    private void enlazarVistas() {
        etNombre = findViewById(R.id.etNombre);
        etPrincipio = findViewById(R.id.etPrincipio);
        etDosis = findViewById(R.id.etDosis);
        etStock = findViewById(R.id.etStock);
        etStockMinimo = findViewById(R.id.etStockMinimo);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        etNotas = findViewById(R.id.etNotas);
        seekUnidades = findViewById(R.id.seekUnidades);
        txtUnidades = findViewById(R.id.txtUnidades);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtTituloForm = findViewById(R.id.txtTituloForm);
        spTipo = findViewById(R.id.spTipo);
        spPresentacion = findViewById(R.id.spPresentacion);
        RadioGroup rgEstado = findViewById(R.id.rgEstado);
        rbActivo = findViewById(R.id.rbActivo);
        rbPausado = findViewById(R.id.rbPausado);
        rbFinalizado = findViewById(R.id.rbFinalizado);
        swRecordatorio = findViewById(R.id.swRecordatorio);
        swFavorito = findViewById(R.id.swFavorito);
        ratingValoracion = findViewById(R.id.ratingValoracion);
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(
                this, R.array.tipos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipo);

        actualizarPresentaciones(0);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarPresentaciones(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void configurarSeekBar() {
        seekUnidades.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtUnidades.setText(getString(R.string.form_unidades, progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekUnidades.setProgress(0);
        txtUnidades.setText(getString(R.string.form_unidades, 1));
    }

    /** Carga el spinner de presentaciones según el tipo elegido (spinners encadenados). */
    private void actualizarPresentaciones(int posicionTipo) {
        // Se conserva la presentación que ya estaba elegida (o la que llega al
        // cargar un medicamento) para que el cambio de tipo no la pierda.
        String seleccionada = presentacionPendiente;
        if (seleccionada == null && spPresentacion.getSelectedItem() != null) {
            seleccionada = spPresentacion.getSelectedItem().toString();
        }
        presentacionPendiente = null;

        int array;
        if (posicionTipo == 1) {
            array = R.array.presentaciones_jarabe;
        } else if (posicionTipo == 2) {
            array = R.array.presentaciones_inyeccion;
        } else {
            array = R.array.presentaciones_pastilla;
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresentacion.setAdapter(adapter);

        if (seleccionada != null) {
            int posicion = adapter.getPosition(seleccionada);
            if (posicion >= 0) {
                spPresentacion.setSelection(posicion);
            }
        }
    }

    /**
     * Reaplica el tipo y la presentación tras un giro de pantalla. Se usa
     * {@code post()} para ejecutarlo cuando Android ya ha restaurado las vistas.
     */
    private void restaurarSpinners(Bundle estado) {
        final int posicionTipo = estado.getInt(ESTADO_TIPO, 0);
        final String presentacion = estado.getString(ESTADO_PRESENTACION);
        spTipo.post(() -> {
            spTipo.setSelection(posicionTipo);
            presentacionPendiente = presentacion;
            actualizarPresentaciones(posicionTipo);
        });
    }

    private void cargar(boolean aplicar) {
        AppExecutors.io(() -> room.medicamentoDao().porId(id), m -> {
            if (m == null || isFinishing() || isDestroyed()) {
                return;
            }
            medicamento = m;
            if (aplicar) {
                mostrar(m);
            }
        });
    }

    private void mostrar(Medicamento m) {
        etNombre.setText(m.nombre);
        etPrincipio.setText(m.principioActivo);
        etDosis.setText(m.dosis);
        etStock.setText(String.valueOf(m.stock));
        etStockMinimo.setText(String.valueOf(m.stockMinimo));
        etFrecuencia.setText(String.valueOf(m.frecuenciaHoras));
        etNotas.setText(m.notas);

        int unidades = Math.max(1, m.unidadesPorToma);
        seekUnidades.setProgress(unidades - 1);
        txtUnidades.setText(getString(R.string.form_unidades, unidades));

        if ("pausado".equals(m.estado)) {
            rbPausado.setChecked(true);
        } else if ("finalizado".equals(m.estado)) {
            rbFinalizado.setChecked(true);
        } else {
            rbActivo.setChecked(true);
        }

        swRecordatorio.setChecked(m.recordatorio);
        swFavorito.setChecked(m.favorito);
        ratingValoracion.setRating(m.valoracion);

        fechaMillis = m.fechaInicio;
        txtFechaInicio.setText(FechaUtil.fechaHora(fechaMillis));

        // Se desengancha el listener para que al seleccionar el tipo no se
        // pierda la presentación guardada del medicamento.
        spTipo.setOnItemSelectedListener(null);
        int posicionTipo = posicionTipo(m.tipo);
        spTipo.setSelection(posicionTipo);
        presentacionPendiente = m.presentacion;
        actualizarPresentaciones(posicionTipo);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarPresentaciones(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int posicionTipo(String tipo) {
        if ("jarabe".equals(tipo)) {
            return 1;
        }
        if ("inyeccion".equals(tipo)) {
            return 2;
        }
        return 0;
    }

    private String claveTipo(int posicion) {
        if (posicion == 1) {
            return "jarabe";
        }
        if (posicion == 2) {
            return "inyeccion";
        }
        return "pastilla";
    }

    private void guardar() {
        String nombre = etNombre.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, R.string.form_nombre_obligatorio, Toast.LENGTH_SHORT).show();
            etNombre.requestFocus();
            return;
        }
        if (id > 0 && medicamento == null) {
            Toast.makeText(this, R.string.form_error, Toast.LENGTH_SHORT).show();
            return;
        }

        int stock;
        int stockMinimo;
        int frecuencia;
        try {
            stock = entero(etStock, 0);
            stockMinimo = entero(etStockMinimo, 0);
            frecuencia = entero(etFrecuencia, 8);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.form_numero_invalido, Toast.LENGTH_SHORT).show();
            return;
        }

        final boolean esNuevo = id <= 0;
        final Medicamento m = esNuevo ? new Medicamento() : medicamento;

        m.nombre = nombre;
        m.principioActivo = etPrincipio.getText().toString().trim();
        m.dosis = etDosis.getText().toString().trim();
        m.unidadesPorToma = seekUnidades.getProgress() + 1;
        m.stock = stock;
        m.stockMinimo = stockMinimo;
        m.frecuenciaHoras = Math.max(1, frecuencia);
        m.tipo = claveTipo(spTipo.getSelectedItemPosition());
        m.presentacion = spPresentacion.getSelectedItem() == null
                ? "" : spPresentacion.getSelectedItem().toString();
        m.estado = rbPausado.isChecked() ? "pausado" : rbFinalizado.isChecked() ? "finalizado" : "activo";
        m.recordatorio = swRecordatorio.isChecked();
        m.favorito = swFavorito.isChecked();
        m.valoracion = (int) ratingValoracion.getRating();
        m.notas = etNotas.getText().toString().trim();
        m.fechaInicio = fechaMillis;
        if (esNuevo) {
            m.creado = System.currentTimeMillis();
        }

        final long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> {
            long idFinal;
            if (esNuevo) {
                idFinal = room.medicamentoDao().insertar(m);
            } else {
                room.medicamentoDao().actualizar(m);
                idFinal = m.id;
            }
            if (m.favorito) {
                db.anadirFavorito(idUsuario, idFinal, m.nombre, "");
            } else {
                db.quitarFavorito(idUsuario, idFinal);
            }
            return idFinal;
        }, idFinal -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (idFinal == null || idFinal <= 0) {
                Toast.makeText(this, R.string.form_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, esNuevo ? R.string.form_guardado : R.string.form_actualizado,
                    Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

    private int entero(EditText campo, int porDefecto) {
        String texto = campo.getText().toString().trim();
        if (texto.isEmpty()) {
            return porDefecto;
        }
        return Integer.parseInt(texto);
    }

    @Override
    public void onFechaHora(int anio, int mes, int dia, int hora, int minuto) {
        fechaMillis = FechaUtil.aMillis(anio, mes, dia, hora, minuto);
        txtFechaInicio.setText(FechaUtil.fechaHora(fechaMillis));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ESTADO_FECHA, fechaMillis);
        outState.putInt(ESTADO_TIPO, spTipo.getSelectedItemPosition());
        Object presentacion = spPresentacion.getSelectedItem();
        outState.putString(ESTADO_PRESENTACION, presentacion == null ? null : presentacion.toString());
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
