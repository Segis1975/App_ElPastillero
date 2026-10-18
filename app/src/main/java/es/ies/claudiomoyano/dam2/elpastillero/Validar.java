package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Validar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_validar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView nombre = findViewById(R.id.nombre);
        TextView categoria = findViewById(R.id.categoria);
        TextView laboratorio = findViewById(R.id.laboratorio);
        TextView dosis = findViewById(R.id.Dosis);
        TextView hora= findViewById(R.id.horaToma);
        TextView cantidad = findViewById(R.id.cantidad);
        TextView inicio = findViewById(R.id.inicio);
        TextView fin = findViewById(R.id.fin);
        Intent i = getIntent();
        String n  = i.getStringExtra("nombre");
        nombre.setText(n);
        n= i.getStringExtra("categoria");
        categoria.setText(n);
        n= i.getStringExtra("laboratorio");
        laboratorio.setText(n);
        n= i.getStringExtra("dosis");
        dosis.setText(n);
        n= i.getStringExtra("hora");
        hora.setText(n);
        n= i.getStringExtra("cantidad");
        cantidad.setText(n);
        n= i.getStringExtra("inicio");
        inicio.setText(n);
        n= i.getStringExtra("fin");
        fin.setText(n);
        Button validar = findViewById(R.id.btnValidar);
        validar.setOnClickListener(v->{
            finish();
        });
    }
}