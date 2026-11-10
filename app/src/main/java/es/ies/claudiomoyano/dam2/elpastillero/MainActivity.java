package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.ies.claudiomoyano.dam2.elpastillero.clases.clUsuario;
import es.ies.claudiomoyano.dam2.elpastillero.db.BaseDatos;
import es.ies.claudiomoyano.dam2.elpastillero.db.UsuarioRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activty_main);
        // Los insets los gestiona android:fitsSystemWindows="true" en la raiz del layout.
        // Asi el contenido queda por debajo de la ActionBar y de la barra de navegacion.
        // (Antes se aplicaban al boton btnMedicamentos, que no es la vista raiz.)
   /*     TextView cont = findViewById(R.id.contador);
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
            Intent actividad2 = new Intent(this, Activity2.class);
            startActivity(actividad2);

        });

        Button practica3 = findViewById(R.id.btnPractica3);
        practica3.setOnClickListener(v->{
            Intent actividad3 = new Intent(this, Activity3.class);
            startActivity(actividad3);
        });
        Button practica4 = findViewById(R.id.btnPractica4);
        practica4.setOnClickListener(v->{
            Intent actividad4 = new Intent(this, Activity4.class);
            startActivity(actividad4);
        });
        Button practica5 = findViewById(R.id.btnPractica5);
        practica5.setOnClickListener(v->{
            Intent actividad5 = new Intent(this, Activity5.class);
            startActivity(actividad5);
        });
        Button practica6 = findViewById(R.id.btnPractica6);
        practica6.setOnClickListener(v->{
            Intent actividad6 = new Intent(this, Activity7.Activity6.class);
            startActivity(actividad6);
        });
        Button practica7 = findViewById(R.id.btnPractica7);
        practica7.setOnClickListener(v->{
            Intent actividad7 = new Intent(this, Activity7.class);
            startActivity(actividad7);
        });
        Button practica8 = findViewById(R.id.btnPractica8);
        practica8.setOnClickListener(v->{
            Intent actividad8 = new Intent(this, Activity8.class);
            startActivity(actividad8);
        });
        Button practica9 = findViewById(R.id.btnPractica9);
        practica9.setOnClickListener(v->{
            Intent actividad9 = new Intent(this, Activity9.class);
            startActivity(actividad9);
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int op = item.getItemId();
        if (op == R.id.ajustes){
            startActivity(new Intent(this, Ajustes.class));
        }
        else if (op == R.id.Usuario){

            startActivity(new Intent(this, Usuario.class));
        } else if (op == R.id.familiar) {
            Toast.makeText(this, "Familiar", Toast.LENGTH_LONG).show();
        }else if (op == R.id.acDe) {
           startActivity(new Intent(this, AcercaDe.class));
        }
        return true;
    }
}