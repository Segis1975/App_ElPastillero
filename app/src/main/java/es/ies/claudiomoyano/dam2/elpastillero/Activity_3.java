package es.ies.claudiomoyano.dam2.elpastillero;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_3 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView ponerTexto= findViewById(R.id.teclaPulsada);
        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(v->{
                ponerTexto.setText("Has pulsado la tecla 1");
        });
        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(v-> {
            ponerTexto.setText("Has pulsado la tecla 2");
        });
        Button btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(v-> {
            ponerTexto.setText("Has pulsado la tecla 3");
        });
        Button btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(v-> {
            ponerTexto.setText("Has pulsado la tecla 4");
        });
        Button btn5 = findViewById(R.id.btn5);
        btn5.setOnClickListener(v-> {
                    ponerTexto.setText("Has pulsado la tecla 5");
        });
        Button btn6 = findViewById(R.id.btn6);
        btn6.setOnClickListener(v-> {
            ponerTexto.setText("Has pulsado la tecla 6");
        });
        Button btn7 = findViewById(R.id.btn7);
        btn6.setOnClickListener(v-> {
            ponerTexto.setText("Has pulsado la tecla 7");
        });
        Button btn8 = findViewById(R.id.btn8);
        btn8.setOnClickListener(v-> {
            ponerTexto.setText("Has pulsado la tecla 8");
        });
        Button btn9 = findViewById(R.id.btn9);
        btn9.setOnClickListener(v -> {
                ponerTexto.setText("Has pulsado la tecla 9");
        });
    }
}