package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import androidx.annotation.Nullable;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_9);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ArrayList<Medicamento> medicamentos = new ArrayList<>();

        int imagen = R.drawable.pastilla;

        medicamentos.add(new Medicamento(
                "Ibuprofeno", "30/11/2026", "02/10/2026",
                "12", "14:00", "1 comprimido",
                "Antiinflamatorios", "Normon", imagen));

        medicamentos.add(new Medicamento(
                "Paracetamol", "30/12/2026", "01/10/2026",
                "20", "08:00", "1 comprimido",
                "Analgésicos", "Cinfa", imagen));


        medicamentos.add(new Medicamento(
                "Omeprazol", "31/05/2027", "03/10/2026",
                "28", "07:30", "1 cápsula",
                "Digestivos", "Cinfa", imagen));

        medicamentos.add(new Medicamento(
                "Amoxicilina", "30/11/2026", "04/10/2026",
                "21", "09:00", "1 cápsula",
                "Antibióticos", "Sandoz", imagen));

        medicamentos.add(new Medicamento(
                "Loratadina", "31/08/2027", "05/10/2026",
                "10", "21:00", "1 comprimido",
                "Antialérgicos", "Kern Pharma", imagen));

        medicamentos.add(new Medicamento(
                "Metformina", "31/07/2027", "06/10/2026",
                "60", "09:00", "1 comprimido",
                "Antidiabéticos", "Teva", imagen));

        medicamentos.add(new Medicamento(
                "Enalapril", "30/06/2027", "07/10/2026",
                "30", "08:00", "1 comprimido",
                "Cardiovasculares", "Cinfa", imagen));

        medicamentos.add(new Medicamento(
                "Atorvastatina", "31/08/2027", "08/10/2026",
                "30", "22:00", "1 comprimido",
                "Colesterol", "Normon", imagen));

        medicamentos.add(new Medicamento(
                "Amlodipino", "30/09/2027", "09/10/2026",
                "30", "08:00", "1 comprimido",
                "Cardiovasculares", "Sandoz", imagen));

        medicamentos.add(new Medicamento(
                "Cetirizina", "31/12/2027", "10/10/2026",
                "20", "21:00", "1 comprimido",
                "Antialérgicos", "UCB", imagen));

        medicamentos.add(new Medicamento(
                "Azitromicina", "31/03/2027", "11/10/2026",
                "6", "10:00", "1 comprimido",
                "Antibióticos", "Teva", imagen));

        medicamentos.add(new Medicamento(
                "Metamizol", "31/01/2027", "12/10/2026",
                "15", "20:00", "1 cápsula",
                "Analgésicos", "Stada", imagen));

        medicamentos.add(new Medicamento(
                "Vitamina D", "31/10/2027", "13/10/2026",
                "10", "10:00", "1 cápsula",
                "Vitaminas", "Arkopharma", imagen));

        medicamentos.add(new Medicamento(
                "Ácido fólico", "31/12/2027", "14/10/2026",
                "30", "08:00", "1 comprimido",
                "Vitaminas", "Kern Pharma", imagen));

        medicamentos.add(new Medicamento(
                "Acetilcisteína", "30/04/2027", "15/10/2026",
                "12", "09:00", "1 sobre",
                "Respiratorios", "Cinfa", imagen));

        medicamentos.add(new Medicamento(
                "Salbutamol", "31/03/2027", "16/10/2026",
                "1", "Según necesidad", "1 inhalación",
                "Respiratorios", "GSK", imagen));

        medicamentos.add(new Medicamento(
                "Famotidina", "30/06/2027", "17/10/2026",
                "14", "20:00", "1 comprimido",
                "Digestivos", "Normon", imagen));

        medicamentos.add(new Medicamento(
                "Hierro", "31/01/2028", "18/10/2026",
                "30", "12:00", "1 comprimido",
                "Minerales", "Bayer", imagen));

        medicamentos.add(new Medicamento(
                "Magnesio", "31/12/2027", "19/10/2026",
                "20", "19:00", "1 comprimido",
                "Minerales", "Aquilea", imagen));

        medicamentos.add(new Medicamento(
                "Cefalexina", "31/12/2026", "20/10/2026",
                "14", "08:00", "1 cápsula",
                "Antibióticos", "Normon", imagen));
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