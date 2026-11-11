package es.ies.claudiomoyano.dam2.elpastillero;

import static es.ies.claudiomoyano.dam2.elpastillero.R.id.btnGrabarU;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Objects;

import es.ies.claudiomoyano.dam2.elpastillero.clases.clContacto;
import es.ies.claudiomoyano.dam2.elpastillero.db.BaseDatos;
import es.ies.claudiomoyano.dam2.elpastillero.db.ContactoRepository;
import es.ies.claudiomoyano.dam2.elpastillero.db.UsuarioRepository;

public class Contacto extends AppCompatActivity {
    clContacto con = new clContacto();
    BaseDatos bd;

    ContactoRepository conRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.idApp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText nombre = findViewById(R.id.txtNombreF);
        EditText telefono = findViewById(R.id.txtTelefonoC);
        EditText email = findViewById(R.id.txtEMailC);
        EditText parentesco = findViewById(R.id.txtParentescC);
        CheckBox ch = findViewById(R.id.chNotificacionesC);
        Button grabar = findViewById(R.id.btnGuardarF);
        Button cancelar = findViewById(R.id.btnCancelarC);
        BaseDatos bdHelper = new BaseDatos(this);
        conRepo = new ContactoRepository(bdHelper);
        try{

            con = conRepo.Busqueda();
            nombre.setText(con.getNombre());
            telefono.setText(con.getTelefono());
            email.setText(con.getEmail());
            parentesco.setText(con.getParentesco());
            int n = con.getAlertas();
            if (n == 1) {
                ch.setChecked(true);
            } else {
                ch.setChecked(false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
                }
        /*if (!nombre.getText().toString().trim().isEmpty()) {
            grabar.setText("Guardar cambios");
        }
        grabar.setOnClickListener(v -> {
            if (nombre.getText().toString().trim().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ERROR");
                builder.setMessage("El campo nombre es obligatorio");
                builder.setNeutralButton("Aceptar", ((dialog, which) -> {
                }));
                builder.create().show();
            } else {
                clContacto cont = new clContacto();
                cont.setId(1);
                cont.setNombre(nombre.getText().toString().trim());
                cont.setTelefono(telefono.getText().toString().trim());
                cont.setEmail(email.getText().toString().trim());
                cont.setParentesco(parentesco.getText().toString().trim());
                if (ch.isChecked()) {
                    cont.setAlertas(1);
                } else {
                    cont.setAlertas(0);
                }
                conRepo = new ContactoRepository(bd);
                if (grabar.getText().toString().equals("Guardar cambios")) {
                    if (!con.equals(cont)) {
                        int nu = conRepo.modificar(cont);
                        if (nu > 0) {
                            Toast.makeText(this, "Contacto modificado correctamente", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        finish();
                    }
                } else {
                    try {
                        // 2) Insertamos el usuario y recogemos el id generado.
                        long id = conRepo.insertarContacto(cont);

                        // 3) insert() devuelve -1 si no se pudo insertar
                        //    (por ejemplo, si el DNI ya existe: columna UNIQUE).
                        if (id == -1) {
                            Toast.makeText(this,
                                    "No se pudo guardar: el DNI ya existe o los datos no son válidos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this,
                                    "Usuario guardado correctamente (id " + id + ")",
                                    Toast.LENGTH_LONG).show();
                            finish(); // volvemos a la pantalla anterior
                        }
                    } catch (Exception e) {
                        Toast.makeText(this,
                                "Error al guardar: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } finally {
                        // 4) Cerramos siempre la conexión.
                        bdHelper.close();
                    }
                }
            }
        });
        cancelar.setOnClickListener(v -> {
            clContacto cont = new clContacto();
            cont.setId(1);
            cont.setNombre(nombre.getText().toString().trim());
            cont.setTelefono(telefono.getText().toString().trim());
            cont.setEmail(email.getText().toString().trim());
            cont.setParentesco(parentesco.getText().toString().trim());
            if (ch.isChecked()) {
                cont.setAlertas(1);
            } else {
                cont.setAlertas(0);
            }
            conRepo = new ContactoRepository(bd);
            if (Objects.equals(con, cont)) {
                AlertDialog.Builder pantalla = new AlertDialog.Builder(this);
                pantalla.setTitle("Advertencia");
                pantalla.setMessage("Se han modificado los datos.\n ¿Deseas guardar los cambios realizados?");
                pantalla.setPositiveButton("Si", (dialog, which) -> {
                    int nu = conRepo.modificar(cont);
                    if (nu > 0) {
                        Toast.makeText(this, "Usuario modificado correctamente", Toast.LENGTH_LONG).show();
                    }
                    finish();
                });
                pantalla.setNegativeButton("No", (dialog, which) -> finish());
                pantalla.create().show();
            } else {
                finish();
            }
        });*/
    }
}