package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Activity9 extends AppCompatActivity {
    ArrayList<Medicamento> lista = new ArrayList<>();
    MedicamentoAdapter adapter;
    RecyclerView vista;
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
                        int ima = R.drawable.pastilla;
                        lista.add(new Medicamento(nombre,categoria,laboratorio,dosis,hora,cantidad,inicio, fin, ima));
                        vista.setAdapter(adapter);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity9);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.idApp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        medicamentos.add(new Medicamento("Ibuprofeno", "Antiinflamatorio","Bayer", "12", "20:00", "1","01/10/2026","10/10/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Paracetamol", "Analgésicos","Cinfa", "20","08:00", "1","20/10/2026","01/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Omeoprazol", "Digestivos","Cinfa", "10", "19:00", "1","01/11/2026","11/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Amoxicilina", "Antibióticos","Sandoz", "12", "19:00", "1","01/11/2026","11/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Loratadina", "Antialergicoa","Kern Pharma", "12", "18:00", "1","01/11/2026","11/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Metformina", "Antialergicoa","Kern Pharma", "12", "18:00", "1","01/11/2026","11/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Enalpril","Cardiovasculares","Cinfa", "12", "18:00", "1","01/11/2026","11/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Atorvastatina","Coresterol", "Normnon", "30","22.00","1", "31/08/2026", "08/10/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Amlodipino", "CardioVasculares", "Sandoz", "15","12:00", "2", "30/09/2026", "09/10/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Cetirizina", "Antialergicos", "UCB", "20", "21:00", "1", "11/11/2026", "20/11/2026", R.drawable.pastilla));
        medicamentos.add(new Medicamento("Azitromicina", "Antibióticos", "Teva","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Metamizol", "Analgésicos","Arkopharma", "12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Vitamina D", "analgésicos,","Stada","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Ácido fólico","Vitaminas","Kern Pharma","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Acetilcisteína", "Respiratorios","Cinfa","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Salbutamol", "Respiratorios", "GSK","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Famotidina", "Digestivos", "NOrmon","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Hierro", "Minerales","Bayer","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Magnesio", "Minerales","Bayer","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        medicamentos.add(new Medicamento("Cefalexina", "Antibioticos","Normon","12", "22:30", "1","26/10/2026", "11/11/2026",R.drawable.pastilla));
        vista = findViewById(R.id.recyclerMedicamentos);
        Button volver = findViewById(R.id.volverAtras9);
        volver.setOnClickListener(v->{
            finish();
        });
        Button anadir = findViewById(R.id.btnmas9);

        anadir.setOnClickListener(v->{
            Intent i = new Intent(this, AnadirMedicamento.class);
            lanzar.launch(i);
        });

        adapter = new MedicamentoAdapter(medicamentos);
        vista.setLayoutManager(new LinearLayoutManager(this));
        vista.setAdapter(adapter);

    }


    public class MedicamentoAdapter extends
            RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder> {

        private ArrayList<Medicamento> listaMedicamentos;

        // Constructor
        public MedicamentoAdapter(ArrayList<Medicamento> listaMedicamentos) {
            this.listaMedicamentos = listaMedicamentos;
        }

        // 1. Crear la tarjeta y el ViewHolder
        @NonNull
        @Override
        public MedicamentoViewHolder onCreateViewHolder(
                @NonNull ViewGroup parent, int viewType) {

            View vista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_medicamento, parent, false);

            return new MedicamentoViewHolder(vista);
        }

        // 2. Cargar los datos de cada medicamento
        @Override
        public void onBindViewHolder(
                @NonNull MedicamentoViewHolder holder, int position) {

            Medicamento medicamento = listaMedicamentos.get(position);

            holder.txtNombre.setText(medicamento.getNombre());
            holder.txtDosis.setText(medicamento.getDosis());
            holder.txtHora.setText(medicamento.getHora());
            holder.imgMedicamento.setImageResource(medicamento.getImage());
            holder.itemView.setOnClickListener(v -> {
               int posicion = holder.getBindingAdapterPosition();
               Intent nuevo = new Intent(v.getContext(), Validar.class);

                nuevo.putExtra("nombre", listaMedicamentos.get(posicion).getNombre());
                nuevo.putExtra("categoria", listaMedicamentos.get(posicion).getCategoria());
                nuevo.putExtra("laboratorio", listaMedicamentos.get(posicion).getLaboratorio());
                nuevo.putExtra("dosis", listaMedicamentos.get(posicion).getDosis());
                nuevo.putExtra("hora", listaMedicamentos.get(posicion).getHora());
                nuevo.putExtra("cantidad", listaMedicamentos.get(posicion).getCantidad());
                nuevo.putExtra("inicio", listaMedicamentos.get(posicion).getFechaI());
                nuevo.putExtra("fin", listaMedicamentos.get(posicion).getFechaF());
                startActivity(nuevo);

                Toast.makeText(
                        v.getContext(),
                        "Cotor  Medicamento: " + medicamento.getNombre(),
                        Toast.LENGTH_SHORT
                ).show();

            });

            // CLICK LARGO: eliminar el medicamento
            holder.itemView.setOnLongClickListener(v -> {
                int posicion = holder.getBindingAdapterPosition();
                if (posicion != RecyclerView.NO_POSITION) {

                    listaMedicamentos.remove(posicion);
                    notifyItemRemoved(posicion);

                    Toast.makeText(v.getContext(),"Elemento eliminado",Toast.LENGTH_SHORT).show();
                }

                return true;
            });
        }


        // 3. Número de elementos de la lista
        @Override
        public int getItemCount() {
            return listaMedicamentos.size();
        }

        // ViewHolder
        public class MedicamentoViewHolder
                extends RecyclerView.ViewHolder {

            ImageView imgMedicamento;
            TextView txtNombre;
            TextView txtDosis;
            TextView txtHora;

            public MedicamentoViewHolder(@NonNull View itemView) {
                super(itemView);

                imgMedicamento = itemView.findViewById(R.id.imagenA9);
                txtNombre = itemView.findViewById(R.id.nombreA9);
                txtDosis = itemView.findViewById(R.id.cantidadA9);
                txtHora = itemView.findViewById(R.id.horaA9);

            }
        }

    }
}