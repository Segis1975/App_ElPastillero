package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * Información de la aplicación, versión y enlace al repositorio.
 */
public class AcercaDeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.menu_acerca);
        }

        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(getString(R.string.acerca_version, BuildConfig.VERSION_NAME));

        TextView txtRepo = findViewById(R.id.txtRepo);
        final String urlRepositorio = getString(R.string.url_repositorio);
        txtRepo.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlRepositorio)));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.acerca_no_navegador, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
