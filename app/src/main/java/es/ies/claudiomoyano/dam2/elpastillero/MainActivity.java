package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnch), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView cont = findViewById(R.id.contador);
        Button mas = findViewById(R.id.incre);
        Button acero=findViewById(R.id.reset);
        mas.setOnClickListener(v->{
                int numero = Integer.parseInt(cont.getText().toString());
                numero++;
                cont.setText(String.valueOf(numero));
        });
        acero.setOnClickListener(v->
                cont.setText(String.valueOf(0))
        );
        Button practica2 = findViewById(R.id.btnPractica2);

        practica2.setOnClickListener(v->{
            Intent actividad2 = new Intent(this, Activity_2.class);
            startActivity(actividad2);

        });

        Button practica3 = findViewById(R.id.btnPractica3);
        practica3.setOnClickListener(v->{
            Intent actividad3 = new Intent(this, Activity_3.class);
            startActivity(actividad3);
        });
        Button practica4 = findViewById(R.id.btnPractica4);
        practica4.setOnClickListener(v->{
            Intent actividad4 = new Intent(this, Actiivity4.class);
            startActivity(actividad4);
        });
        Button practica5 = findViewById(R.id.btnPractica5);
        practica5.setOnClickListener(v->{
            Intent actividad5 = new Intent(this, Activity5.class);
            startActivity(actividad5);
        });
        Button practica6 = findViewById(R.id.btnPractica6);
        practica6.setOnClickListener(v->{
            Intent actividad6 = new Intent(this, Activity6.class);
            startActivity(actividad6);
        });
        Button practica7 = findViewById(R.id.btnPractica7);
        practica7.setOnClickListener(v->{
            Intent actividad7 = new Intent(this, Activity7.class);
            startActivity(actividad7);
        });

    }

}