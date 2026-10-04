package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button mas = findViewById(R.id.mas);
        Button menos = findViewById(R.id.menos);
        TextView cont = findViewById(R.id.contador2);
        mas.setOnClickListener(v->{
            int numero = Integer.parseInt(cont.getText().toString());
            numero++;
            cont.setText(String.valueOf(numero));

        });
        menos.setOnClickListener(view -> {
            int numero = Integer.parseInt(cont.getText().toString());
            numero--;
            if (numero <1)
                numero=0;
            cont.setText(String.valueOf(numero));
        });
        TextView texto = findViewById(R.id.txtVacio);
        TextView textoI = findViewById(R.id.txtInsertado);
        Button insertar = findViewById(R.id.ponerTexto);
        insertar.setOnClickListener(v->{
            textoI.setText(texto.getText());
            texto.setText("");

        });
        CheckBox ch = findViewById(R.id.checkB);
        Button bch = findViewById(R.id.btnChec);
        ch.setChecked(true);
        ch.setOnClickListener(v->{
            if (ch.isChecked()){
                bch.setEnabled(false);
            }
            else{
                bch.setEnabled(true);
            }
        });
        bch.setOnClickListener(v->{
            bch.setText("Botón pulsado");
        });
        Button atras = findViewById(R.id.volver);
        atras.setOnClickListener(v->{
            finish();
        });

    }
}