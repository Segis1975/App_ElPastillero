package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Activity8 extends AppCompatActivity {

    ArrayList<Medicamento> listaMedicamento = new ArrayList<>();
    ListView lista;
    AdaptadorMedicamentos adaptador;
    ActivityResultLauncher<Intent> lanzar = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent datos = result.getData();
                        String nombre = datos.getStringExtra("nombre");
                        String categoria = datos.getStringExtra("categoria");
                        String laboratorio = datos.getStringExtra("laboratorio");
                        String dosis = datos.getStringExtra("dosis");
                        String hora =datos.getStringExtra("hora");
                        String cantidad =datos.getStringExtra("cantidad");
                        String inicio =datos.getStringExtra("inicio");
                        String fin =datos.getStringExtra("fin");
                        listaMedicamento.add(new Medicamento(
                                nombre,categoria,laboratorio,dosis,hora,cantidad,inicio, fin));
                        lista.setAdapter(adaptador
                        );
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_8);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lista = findViewById(R.id.listaMedicamentos);
         adaptador = new AdaptadorMedicamentos(this, listaMedicamento);
        lista.setAdapter(adaptador);
        listaMedicamento.add(new Medicamento("a","b","c", "d", "e", "f", "g", "h"));
        Button volverA = findViewById(R.id.volverAtras);
        volverA.setOnClickListener(v-> finish());
        Button anadir = findViewById(R.id.btnmas);

        anadir.setOnClickListener(v->{
            Intent i = new Intent(this, AnadirMedicamento.class);
            lanzar.launch(i);
        });
    }



    public class AdaptadorMedicamentos extends ArrayAdapter{

        ArrayList<Medicamento> lista;
        Context context;
        public AdaptadorMedicamentos(Context context, ArrayList<Medicamento> lista){
            super(context, R.layout.itemmedicamento,lista);
            this.lista = new ArrayList<Medicamento>();
            this.lista = lista;
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.itemmedicamento, null);
            ImageView ima = item.findViewById(R.id.itemima);
            TextView nombre = item.findViewById(R.id.nombre);
            TextView hora = item.findViewById(R.id.hora);
            TextView cantidad = item.findViewById(R.id.cantidad);
            ima.setImageResource(R.drawable.pastilla);
            nombre.setText(lista.get(position).getNombre().toString());
            hora.setText(lista.get(position).getHora());
            cantidad.setText(lista.get(position).getCantidad());
            return item;
        }
    }
}