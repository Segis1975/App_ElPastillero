package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FichaMedicamento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ficha_medicamento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imagen = findViewById(R.id.imgFicha);
        TextView nombre = findViewById(R.id.txtFichaNombre);
        TextView cantidad = findViewById(R.id.txtFichaCantidad);
        TextView categoria = findViewById(R.id.txtCategoria);
        TextView laboratorio = findViewById(R.id.txtLaboratorio);
        TextView fechaI = findViewById(R.id.txtFechaInicio);
        TextView fechaF = findViewById(R.id.txtFechaFin);
        TextView dosis = findViewById(R.id.txtDosis);
        TextView hora = findViewById(R.id.txtHora);
        RatingBar rating = findViewById(R.id.ratingMedicamento);

        nombre.setText(getIntent().getStringExtra("nombre"));

        cantidad.setText(
                "Unidades disponibles: "
                        + getIntent().getStringExtra("cantidad"));

        categoria.setText(
                "Categoría: " + getIntent().getStringExtra("categoria"));

        laboratorio.setText(
                "Laboratorio: " + getIntent().getStringExtra("laboratorio"));

        fechaI.setText(
                "Fecha de inicio: " + getIntent().getStringExtra("fechaI"));

        fechaF.setText(
                "Fecha de finalización: "
                        + getIntent().getStringExtra("fechaF"));

        dosis.setText(
                "Dosis: " + getIntent().getStringExtra("dosis"));

        hora.setText(
                "Hora de toma: " + getIntent().getStringExtra("hora"));

        int recursoImagen = getIntent().getIntExtra(
                "image", R.drawable.pastilla);

        imagen.setImageResource(recursoImagen);

        float valoracion = getIntent().getFloatExtra(
                "valoracion", 0);

        rating.setRating(valoracion);
    }
}
