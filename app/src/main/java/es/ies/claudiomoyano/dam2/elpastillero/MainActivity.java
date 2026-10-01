package es.ies.claudiomoyano.dam2.elpastillero;

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
        acero.setOnClickListener(v->{
            cont.setText(String.valueOf(0));
        });
    }
}