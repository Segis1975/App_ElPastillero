package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity7 extends AppCompatActivity {

    String categoria="" ,lugar="", marca="";
    Button btninfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_7);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btninfo  = findViewById(R.id.btnInformacion);
        btninfo.setEnabled(false);
        String[] categorias = {
                "Selecciona una categoría",
                "Analgésicos",
                "Antibióticos",
                "Vitaminas",
                "Digestivos"};
        Spinner sp_categorias = findViewById(R.id.spCategoria);
        ArrayAdapter<String> adaptadorC = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        sp_categorias.setAdapter(adaptadorC);
        sp_categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position >0) {
                    categoria = parent.getItemAtPosition(position).toString();
                }else{
                    categoria="";
                }
                comprobarDatos();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        String[] medicamentos ={
                "Seleccione una marca",
                "Gelocatil",
                "Nolotil",
                "Augmentine",
                "Clamoxyl",
                "Redoxon",
                "Hidroferol",
                "Almax",
                "Gaviscon"};
        Spinner sp_medicamentos = findViewById(R.id.spMedicamento);
        ArrayAdapter<String> adapatadorM = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicamentos);
        sp_medicamentos.setAdapter(adapatadorM);
        sp_medicamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position>0){
                    marca = adapterView.getItemAtPosition(position).toString();
                } else {
                    marca ="";
                }
                comprobarDatos();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] lugares = {
                "Dormitorio",
                "Cocina",
                "Cuarto de baño",
                "Nevera",
                "Botiquín"};
        ListView lista = findViewById(R.id.lista);
        ArrayAdapter<String> adaptadorLista = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, lugares);
        lista.setAdapter(adaptadorLista);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               lugar = adapterView.getItemAtPosition(position).toString();
               comprobarDatos();
            }
        });
        btninfo.setOnClickListener(v->{
            Toast.makeText(this, "caetg.: "+categoria+ " marca: "+ marca+" y esta en :"+lugar, Toast.LENGTH_SHORT).show();
        });
        Button volverAtras = findViewById(R.id.btnprincipio);
        volverAtras.setOnClickListener(v ->{
                finish();
        });

    }

    private void comprobarDatos() {
        Toast.makeText(this, "EEE", Toast.LENGTH_SHORT).show();
        if (!categoria.isEmpty() && !marca.isEmpty() && !lugar.isEmpty()) {
            btninfo.setEnabled(true);
        }else {
            btninfo.setEnabled(false);
        }
    }


}