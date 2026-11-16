package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.dialog.DialogConfirmar;

/**
 * Ajustes de accesibilidad (tamaño de letra, tema, avisos) y gestión de la
 * cuenta: editar perfil, cerrar sesión o borrarla.
 */
public class AjustesActivity extends BaseActivity
        implements DialogConfirmar.OnConfirmarListener {

    private static final String ESTADO_BORRAR_CUENTA = "estado_borrar_cuenta";

    private DbHelper db;
    private SeekBar seekLetra;
    private TextView txtTamano;
    private TextView txtEjemplo;
    private SwitchMaterial swNotificaciones;
    private RadioGroup rgTema;
    private boolean pendienteBorrarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        if (savedInstanceState != null) {
            pendienteBorrarCuenta = savedInstanceState.getBoolean(ESTADO_BORRAR_CUENTA, false);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.ajustes_titulo);
        }

        db = new DbHelper(this);
        seekLetra = findViewById(R.id.seekLetra);
        txtTamano = findViewById(R.id.txtTamano);
        txtEjemplo = findViewById(R.id.txtEjemplo);
        swNotificaciones = findViewById(R.id.swNotificaciones);
        rgTema = findViewById(R.id.rgTema);

        configurarLetra();
        configurarNotificaciones();
        configurarTema();

        findViewById(R.id.btnPerfil).setOnClickListener(v -> dialogoPerfil());
        findViewById(R.id.btnCerrarSesion).setOnClickListener(v -> {
            Sesion.cerrar(this);
            irLogin();
        });
        findViewById(R.id.btnBorrarCuenta).setOnClickListener(v -> {
            pendienteBorrarCuenta = true;
            DialogConfirmar.newInstance(R.string.ajustes_borrar_cuenta, R.string.ajustes_borrar_cuenta_msg)
                    .show(getSupportFragmentManager(), "borrar_cuenta");
        });
    }

    private void configurarLetra() {
        float escala = Sesion.escalaLetra(this);
        int progreso = Math.round((escala - 0.8f) / 0.1f);
        progreso = Math.max(0, Math.min(6, progreso));
        seekLetra.setProgress(progreso);
        actualizarEtiquetaLetra(progreso);
        txtEjemplo.setTextSize(14 * (0.8f + progreso * 0.1f));

        seekLetra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                actualizarEtiquetaLetra(progress);
                txtEjemplo.setTextSize(14 * (0.8f + progress * 0.1f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float nueva = 0.8f + seekBar.getProgress() * 0.1f;
                Sesion.setEscalaLetra(AjustesActivity.this, nueva);
                recreate();
            }
        });
    }

    private void actualizarEtiquetaLetra(int progreso) {
        int porcentaje = Math.round((0.8f + progreso * 0.1f) * 100);
        txtTamano.setText(getString(R.string.ajustes_tamano_letra, porcentaje));
    }

    private void configurarNotificaciones() {
        swNotificaciones.setChecked(Sesion.notificaciones(this));
        swNotificaciones.setOnCheckedChangeListener((buttonView, checked) -> {
            Sesion.setNotificaciones(this, checked);
            Toast.makeText(this,
                    checked ? R.string.ajustes_notif_activadas : R.string.ajustes_notif_desactivadas,
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void configurarTema() {
        int tema = Sesion.tema(this);
        RadioButton rbSistema = findViewById(R.id.rbSistema);
        RadioButton rbClaro = findViewById(R.id.rbClaro);
        RadioButton rbOscuro = findViewById(R.id.rbOscuro);

        if (tema == Sesion.TEMA_CLARO) {
            rbClaro.setChecked(true);
        } else if (tema == Sesion.TEMA_OSCURO) {
            rbOscuro.setChecked(true);
        } else {
            rbSistema.setChecked(true);
        }

        rgTema.setOnCheckedChangeListener((group, checkedId) -> {
            int nuevoTema;
            int modo;
            if (checkedId == R.id.rbClaro) {
                nuevoTema = Sesion.TEMA_CLARO;
                modo = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.rbOscuro) {
                nuevoTema = Sesion.TEMA_OSCURO;
                modo = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                nuevoTema = Sesion.TEMA_SISTEMA;
                modo = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }
            Sesion.setTema(this, nuevoTema);
            AppCompatDelegate.setDefaultNightMode(modo);
        });
    }

    private void dialogoPerfil() {
        View vista = getLayoutInflater().inflate(R.layout.dialog_usuario, null);
        EditText etNombre = vista.findViewById(R.id.etNombre);
        EditText etEmail = vista.findViewById(R.id.etEmailDialog);
        EditText etPassword = vista.findViewById(R.id.etPasswordDialog);
        etPassword.setVisibility(View.GONE);

        etNombre.setText(Sesion.nombre(this));
        etEmail.setText(Sesion.email(this));

        AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(R.string.perfil_titulo)
                .setView(vista)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.guardar, null)
                .create();

        dialogo.setOnShowListener(d -> dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String nombre = etNombre.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();

                    if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email)) {
                        Toast.makeText(this, R.string.error_campos, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(this, R.string.error_email, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long idUsuario = Sesion.id(this);
                    AppExecutors.io(() -> {
                        if (db.emailEnUso(email, idUsuario)) {
                            return 0;
                        }
                        return db.actualizarUsuario(idUsuario, nombre, email);
                    }, filas -> {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        if (filas == null || filas <= 0) {
                            Toast.makeText(this, R.string.perfil_email_ocupado, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Sesion.guardar(this, idUsuario, nombre, email, Sesion.recordar(this));
                        dialogo.dismiss();
                        Toast.makeText(this, R.string.perfil_guardado, Toast.LENGTH_SHORT).show();
                    });
                }));
        dialogo.show();
    }

    @Override
    public void onConfirmar() {
        if (!pendienteBorrarCuenta) {
            return;
        }
        pendienteBorrarCuenta = false;
        long idUsuario = Sesion.id(this);
        AppExecutors.io(() -> db.borrarUsuario(idUsuario), filas -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            Toast.makeText(this, R.string.ajustes_cuenta_borrada, Toast.LENGTH_SHORT).show();
            Sesion.cerrar(this);
            irLogin();
        });
    }

    private void irLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ESTADO_BORRAR_CUENTA, pendienteBorrarCuenta);
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
