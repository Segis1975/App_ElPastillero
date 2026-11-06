package es.ies.claudiomoyano.dam2.elpastillero;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.Calendar;

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
        EditText domicilio = findViewById(R.id.txtDomicilioU);
        EditText localidad = findViewById(R.id.txtLocalidadU);
        EditText codigoPostal = findViewById(R.id.txtCodigoPostalU);
        EditText provincia = findViewById(R.id.txtProvinc1aU);
        EditText fechaNaci = findViewById(R.id.txtFechaNacimientoU);
        EditText Telefono = findViewById(R.id.txtTelefonoU);
        EditText email = findViewById(R.id.txtEMailU);
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

    }
}