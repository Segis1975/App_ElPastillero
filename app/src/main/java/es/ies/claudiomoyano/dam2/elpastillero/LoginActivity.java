package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import es.ies.claudiomoyano.dam2.elpastillero.db.DbHelper;
import es.ies.claudiomoyano.dam2.elpastillero.model.Usuario;

/**
 * Pantalla de entrada. Valida el correo y la contraseña contra la tabla de
 * usuarios de SQLite y guarda la sesión para el resto de pantallas.
 */
public class LoginActivity extends BaseActivity {

    private DbHelper db;
    private EditText etEmail;
    private EditText etPassword;
    private CheckBox chkRecordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DbHelper(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        chkRecordar = findViewById(R.id.chkRecordar);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtRegistro = findViewById(R.id.txtRegistro);
        TextView txtDemo = findViewById(R.id.txtDemo);

        // Si el usuario marcó "Recordarme", entra directamente.
        if (Sesion.activa(this) && Sesion.recordar(this)) {
            abrirPrincipal();
            return;
        }

        btnLogin.setOnClickListener(v -> iniciarSesion());
        txtRegistro.setOnClickListener(v -> dialogoRegistro());
        txtDemo.setOnClickListener(v -> {
            etEmail.setText(getString(R.string.demo_email));
            etPassword.setText(getString(R.string.demo_password));
        });
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            aviso(R.string.error_campos);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            aviso(R.string.error_email);
            return;
        }

        AppExecutors.io(() -> db.login(email, password), usuario -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            if (usuario == null) {
                aviso(R.string.error_credenciales);
                return;
            }
            Sesion.guardar(this, usuario.id, usuario.nombre, usuario.email, chkRecordar.isChecked());
            abrirPrincipal();
        });
    }

    private void dialogoRegistro() {
        View vista = getLayoutInflater().inflate(R.layout.dialog_usuario, null);
        EditText etNombre = vista.findViewById(R.id.etNombre);
        EditText etEmailDialog = vista.findViewById(R.id.etEmailDialog);
        EditText etPasswordDialog = vista.findViewById(R.id.etPasswordDialog);

        AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(R.string.registro_titulo)
                .setView(vista)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.btn_registrar, null)
                .create();

        dialogo.setOnShowListener(d -> dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String nombre = etNombre.getText().toString().trim();
                    String email = etEmailDialog.getText().toString().trim();
                    String password = etPasswordDialog.getText().toString();

                    if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        aviso(R.string.error_campos);
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        aviso(R.string.error_email);
                        return;
                    }
                    if (password.length() < 4) {
                        aviso(R.string.error_password_corta);
                        return;
                    }

                    AppExecutors.io(() -> db.registrar(nombre, email, password), id -> {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        if (id == null || id <= 0) {
                            aviso(R.string.error_email_existe);
                            return;
                        }
                        dialogo.dismiss();
                        Sesion.guardar(this, id, nombre, email, true);
                        abrirPrincipal();
                    });
                }));
        dialogo.show();
    }

    private void abrirPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_ID_USUARIO, Sesion.id(this));
        startActivity(intent);
        finish();
    }

    private void aviso(int texto) {
        android.widget.Toast.makeText(this, texto, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_acerca) {
            startActivity(new Intent(this, AcercaDeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
