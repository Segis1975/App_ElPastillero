package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Compartir extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.compartir);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText tlf = findViewById(R.id.telefono);
        EditText msg = findViewById(R.id.mensaje);
        Button enviar = findViewById(R.id.btnCompartir);
        String tfno = tlf.getText().toString();

        msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int caracteres = msg.getText().toString().length();
                if (caracteres <0){
                    enviar.setEnabled(false);
                } else{
                    enviar.setEnabled(true);
                }
                return false;
            }
        });
    }
}