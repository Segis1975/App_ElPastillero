package es.ies.claudiomoyano.dam2.elpastillero;

import static es.ies.claudiomoyano.dam2.elpastillero.R.id.btnGrabarU;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import es.ies.claudiomoyano.dam2.elpastillero.clases.clUsuario;
import es.ies.claudiomoyano.dam2.elpastillero.db.BaseDatos;
import es.ies.claudiomoyano.dam2.elpastillero.db.UsuarioRepository;

public class Usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.idApp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText nombre = findViewById(R.id.txtNombreU);
        EditText dniU = findViewById(R.id.txtDNIu);
        EditText domicilio = findViewById(R.id.txtDomicilioU);
        EditText localidad = findViewById(R.id.txtLocalidadU);
        EditText codigoPostal = findViewById(R.id.txtCodigoPostalU);
        EditText provincia = findViewById(R.id.txtProvinc1aU);
        EditText fechaNaci = findViewById(R.id.txtFechaNacimientoU);
        EditText telefono = findViewById(R.id.txtTelefonoU);
        EditText email = findViewById(R.id.txtEMailU);
        Button grabar = findViewById(btnGrabarU);
        Button cancelar = findViewById(R.id.btnCancelarU);
        BaseDatos bdHelper = new BaseDatos(this);
        clUsuario u = new clUsuario();
        UsuarioRepository usuRepo = new UsuarioRepository(bdHelper);
        u = usuRepo.Busqueda();
        nombre.setText(u.getNombre());
        dniU.setText(u.getDNI());
        domicilio.setText(u.getDomicilio());
        codigoPostal.setText(u.getCodigopostal());
        localidad.setText(u.getLocalidad());
        provincia.setText(u.getprovincia());
        telefono.setText(u.getTelefono());
        fechaNaci.setText(u.getFechanacimiento());
        email.setText(u.getEmail());
        if (!nombre.getText().toString().trim().isEmpty()){
            grabar.setText("Guardar cambios");
        }
        else{
            Toast.makeText(this, "nOOOOO  HAT", Toast.LENGTH_LONG).show();

        }
        cancelar.setOnClickListener(v -> finish());
        codigoPostal.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus)
                if (codigoPostal.getText().toString().trim().length()== 5){
                   Funciones f =new Funciones();
                   provincia.setText(f.sacarProvincia(codigoPostal.getText().toString().trim()));
                }

        });

        fechaNaci.setOnClickListener(v-> {
            Toast.makeText(this, "444444444", Toast.LENGTH_SHORT).show();
            Calendar calendario = Calendar.getInstance();
            int dd = calendario.get(Calendar.DAY_OF_MONTH);
            int mm = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);
            DatePickerDialog dialogoFcha = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String fecha = String.format("%02d/%02d/%04d", dayOfMonth, month+1, year);
                fechaNaci.setText(fecha);
            }, ano, mm,dd);
            dialogoFcha.show();
        });
        clUsuario finalU = u;
        grabar.setOnClickListener(v->{
            if (nombre.getText().toString().trim().isEmpty()){
                AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                builder.setTitle("ERROR");
                builder.setMessage("El campo nombre es obligatorio");
                builder.setNeutralButton("Aceptar", ((dialog, which) ->{} ));
                builder.create().show();
            }
            else {
                if (grabar.getText().toString().equals("Guardar cambios")){
                   if (!nombre.getText().toString().trim().equals(finalU.getNombre())){
                       Toast.makeText(this, "Cambio", Toast.LENGTH_SHORT).show();
                   } else{
                       Toast.makeText(this, "NO", Toast.LENGTH_LONG).show();
                   }
                } else {
                    clUsuario usu = new clUsuario();
                    usu.setNombre(nombre.getText().toString().trim());
                    usu.setDNI(dniU.getText().toString().trim());
                    usu.setDomicilio(domicilio.getText().toString().trim());
                    usu.setLocalidad(localidad.getText().toString().trim());
                    usu.setCodigopostal(codigoPostal.getText().toString().trim());
                    usu.setprovincia(provincia.getText().toString().trim());
                    usu.setFechanacimiento(fechaNaci.getText().toString().trim());
                    usu.setTelefono(telefono.getText().toString().trim());
                    usu.setEmail(email.getText().toString().trim());
                    usu.setActivo("SI");

                    // 1) Abrimos la base de datos (crea las tablas la primera vez).
                    BaseDatos bdHelper1 = new BaseDatos(this);
                    try {
                        // 2) Insertamos el usuario y recogemos el id generado.
                        UsuarioRepository usuRepo1 = new UsuarioRepository(bdHelper1);
                        long id = usuRepo1.insertarusuario(usu);

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
    }
}