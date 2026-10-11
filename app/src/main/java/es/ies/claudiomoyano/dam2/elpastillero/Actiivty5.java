package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Actiivty5 extends AppCompatActivity {

    ActivityResultLauncher<Intent> lanzar = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), resultado->{
                if (resultado.getResultCode() == RESULT_OK){
                    Toast.makeText(this, "MEDICAMENTO TOMADO",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "MEDICAMENTO NO TOMADO",Toast.LENGTH_LONG).show();
                }
            });


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.actiivty5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button toma = findViewById(R.id.btnToma);
        toma.setOnClickListener(v->{
            Intent tomar = new Intent(this, toma.class);
            lanzar.launch(tomar);
        });
        Button invitar = findViewById(R.id.btnInvitacion);
        invitar.setOnClickListener(v ->{
            Intent i = new Intent(this, Invitacion.class);
            startActivity(i);
        });
        Button btnvolver = findViewById(R.id.btnVolver);
        btnvolver.setOnClickListener(v-> finish());
    }

}