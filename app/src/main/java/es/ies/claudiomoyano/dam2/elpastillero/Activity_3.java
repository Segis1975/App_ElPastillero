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

public class Activity_3 extends AppCompatActivity {

    public void ponerTexto(String txt){
        TextView texto = findViewById(R.id.textotecla);
        texto.setText(txt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actiivdad3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button bt1 = findViewById(R.id.btn1);
        bt1.setOnClickListener(v->{
            ponerTexto("Has tocado a la tecla 1");
        });
        Button bt2 = findViewById(R.id.btn2);
        bt2.setOnClickListener(v-> ponerTexto("Has tocado el número 2"));
        Button bt3 = findViewById(R.id.btn3);
        bt3.setOnClickListener(v-> ponerTexto("Has tocado el número 3"));
        Button bt4 = findViewById(R.id.btn4);
        bt4.setOnClickListener(v-> ponerTexto("Has tocado el número 4"));
        Button bt5 = findViewById(R.id.btn5);
        bt5.setOnClickListener(v-> ponerTexto("Has tocado el número 5"));
        Button bt6 = findViewById(R.id.btn6);
        bt6.setOnClickListener(v-> ponerTexto("Has tocado el número 6"));
        Button bt7 = findViewById(R.id.btn7);
        bt7.setOnClickListener(v-> ponerTexto("Has tocado el número 7"));
        Button bt8 = findViewById(R.id.btn8);
        bt8.setOnClickListener(v-> ponerTexto("Has tocado el número 8"));
        Button bt9 = findViewById(R.id.btn9);
        bt9.setOnClickListener(v-> ponerTexto("Has tocado el número 9"));


    }
}