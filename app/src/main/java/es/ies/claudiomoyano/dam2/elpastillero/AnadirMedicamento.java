package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnadirMedicamento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.actiivity4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText nombre = findViewById(R.id.nombre);
        EditText categoria = findViewById(R.id.categoria);
        EditText laboratorio = findViewById(R.id.laboratorio);
        EditText dosis = findViewById(R.id.Dosis);
        EditText hora= findViewById(R.id.horaToma);
        Button menos = findViewById(R.id.menos);
        TextView cantidad = findViewById(R.id.cantidad);
        Button mas = findViewById(R.id.mas);
        EditText inicio = findViewById(R.id.inicio);
        EditText fin = findViewById(R.id.fin);
        menos.setOnClickListener(v->{
            String txt =cantidad.getText().toString();
            int n = Integer.parseInt(txt);
            n--;
            if (n >0) {
                cantidad.setText(String.valueOf(n));
            }

        });
        mas.setOnClickListener(v-> {
            String txt =cantidad.getText().toString();
            int n = Integer.parseInt(txt);
            n++;
            if (n <=10) {
                cantidad.setText(String.valueOf(n));
            }
        }  );
        Button validar = findViewById(R.id.btnValidar);
        validar.setOnClickListener(v->{
            boolean validado =true;
            if (nombre.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Falta el nombre del medicamento", Toast.LENGTH_SHORT).show();
                validado= false;
            }
            if (categoria.getText().toString().trim().isEmpty() && validado){
                Toast.makeText(this, "Falta la categoría", Toast.LENGTH_SHORT).show();
                validado= false;
            }
            if (laboratorio.getText().toString().trim().isEmpty() && validado){
                Toast.makeText(this, "Falta nombre del laboratorio", Toast.LENGTH_SHORT).show();
                validado= false;
            }
            if (dosis.getText().toString().trim().isEmpty() &&validado){
                Toast.makeText(this, "Falta la dosis a tomar", Toast.LENGTH_SHORT).show();
                validado= false;
            }

            if (hora.getText().toString().trim().isEmpty() && validado){
                Toast.makeText(this, "Falta la hora", Toast.LENGTH_SHORT).show();
                validado= false;
            }
            String txt =cantidad.getText().toString();
            int n = Integer.parseInt(txt);
            if (n==0){
                Toast.makeText(this, "Falta la cantidad de pastillas", Toast.LENGTH_SHORT).show();
                validado= false;
            }
            if (inicio.getText().toString().trim().isEmpty() &&  validado){
                Toast.makeText(this, "Falta la fecha de inicio", Toast.LENGTH_SHORT).show();
                validado= false;
            }
            if (validado){
                Intent nuevo = new Intent(this, Validar.class);
                nuevo.putExtra("nombre", nombre.getText().toString());
                nuevo.putExtra("categoria", categoria.getText().toString());
                nuevo.putExtra("laboratorio", laboratorio.getText().toString());
                nuevo.putExtra("dosis", dosis.getText().toString());
                nuevo.putExtra("hora", hora.getText().toString());
                nuevo.putExtra("cantidad", cantidad.getText().toString());
                nuevo.putExtra("inicio", inicio.getText().toString());
                nuevo.putExtra("fin", fin.getText().toString());
                setResult(RESULT_OK, nuevo);
                finish();
            }
            else{
                Toast.makeText(this, "NOP ESTA PERFECTO ", Toast.LENGTH_SHORT).show();

            }
        });
    }
}