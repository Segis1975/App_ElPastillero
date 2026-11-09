package es.ies.claudiomoyano.dam2.elpastillero;

import static es.ies.claudiomoyano.dam2.elpastillero.R.id.btnGrabarU;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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
import es.ies.claudiomoyano.dam2.elpastillero.db.UsuarioRepository;

public class Usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
        codigoPostal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                    if (codigoPostal.getText().toString().trim().length()== 5){
                       Funciones f =new Funciones();
                       provincia.setText(f.sacarProvincia(codigoPostal.getText().toString().trim()));
                    }

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
        grabar.setOnClickListener(v->{
            if (nombre.getText().toString().trim().isEmpty()){
                AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                builder.setTitle("ERROR");
                builder.setMessage("El campo nombre es obligatorio");
                builder.setNeutralButton("Aceptar", ((dialog, which) ->{} ));
                builder.create().show();
            }
            else{
                String activo="Si";
                clUsuario usu = new clUsuario(
                        nombre.getText().toString().trim(),
                        dniU.getText().toString().trim(),
                        domicilio.getText().toString().trim(),
                        localidad.getText().toString().trim(),
                        codigoPostal.getText().toString().trim().trim(),
                        provincia.getText().toString().trim(),
                        fechaNaci.getText().toString().trim(),
                        telefono.getText().toString().trim(),
                        email.getText().toString().trim(),
                        activo);
                UsuarioRepository usuRe = new UsuarioRepository();



            }

            Toast.makeText(this,"GRanbar", Toast.LENGTH_SHORT).show();
        });
    }
}