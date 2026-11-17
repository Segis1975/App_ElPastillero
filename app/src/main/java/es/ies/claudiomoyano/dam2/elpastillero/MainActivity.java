package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import es.ies.claudiomoyano.dam2.elpastillero.data.AppDatabase;

/**
 * Pantalla principal con menú lateral. Muestra un resumen de la medicación y
 * sirve de punto de navegación para el resto de pantallas.
 */
public class MainActivity extends BaseActivity {

    public static final String EXTRA_ID_USUARIO = "extra_id_usuario";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private AppDatabase room;
    private long idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idUsuario = getIntent().getLongExtra(EXTRA_ID_USUARIO, Sesion.id(this));
        if (idUsuario <= 0) {
            irLogin();
            return;
        }

        room = AppDatabase.get(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);

        // La pantalla usa una toolbar propia (tema sin ActionBar) y el toggle
        // del menú lateral se engancha directamente a ella.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View cabecera = navView.getHeaderView(0);
        TextView txtNavNombre = cabecera.findViewById(R.id.txtNavNombre);
        TextView txtNavEmail = cabecera.findViewById(R.id.txtNavEmail);
        txtNavNombre.setText(Sesion.nombre(this));
        txtNavEmail.setText(Sesion.email(this));

        TextView txtBienvenida = findViewById(R.id.txtBienvenida);
        txtBienvenida.setText(getString(R.string.bienvenida_usuario, Sesion.nombre(this)));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.abrir_menu, R.string.cerrar_menu);
        drawerLayout.addDrawerListener(toggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                drawerLayout.closeDrawers();
                return true;
            }
            if (id == R.id.nav_medicamentos) {
                abrir(MedicamentoListActivity.class);
                return true;
            }
            if (id == R.id.nav_tomas) {
                abrir(TomaListActivity.class);
                return true;
            }
            if (id == R.id.nav_citas) {
                abrir(CitaListActivity.class);
                return true;
            }
            if (id == R.id.nav_favoritos) {
                abrir(ListaPersonalActivity.class);
                return true;
            }
            if (id == R.id.nav_ajustes) {
                abrir(AjustesActivity.class);
                return true;
            }
            if (id == R.id.nav_acerca) {
                startActivity(new Intent(this, AcercaDeActivity.class));
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });

        findViewById(R.id.btnIrMedicamentos).setOnClickListener(v -> abrir(MedicamentoListActivity.class));
        findViewById(R.id.btnIrTomas).setOnClickListener(v -> abrir(TomaListActivity.class));
        findViewById(R.id.btnIrCitas).setOnClickListener(v -> abrir(CitaListActivity.class));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (toggle != null) {
            toggle.syncState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idUsuario > 0) {
            cargarResumen();
        }
    }

    private void cargarResumen() {
        AppExecutors.io(() -> {
            int total = room.medicamentoDao().contar();
            int stockBajo = room.medicamentoDao().stockBajo().size();
            int stock = room.medicamentoDao().stockTotal();
            int pendientes = room.tomaDao().pendientes().size();
            return new int[]{total, stockBajo, stock, pendientes};
        }, datos -> {
            if (datos == null || isFinishing() || isDestroyed()) {
                return;
            }
            TextView txtNumMedicamentos = findViewById(R.id.txtNumMedicamentos);
            TextView txtNumStockBajo = findViewById(R.id.txtNumStockBajo);
            TextView txtNumTomas = findViewById(R.id.txtNumTomas);
            ProgressBar pbStock = findViewById(R.id.pbStock);
            TextView txtStock = findViewById(R.id.txtStock);

            txtNumMedicamentos.setText(String.valueOf(datos[0]));
            txtNumStockBajo.setText(String.valueOf(datos[1]));
            txtNumTomas.setText(String.valueOf(datos[3]));

            int stock = datos[2];
            pbStock.setMax(Math.max(100, stock));
            pbStock.setProgress(stock);
            if (datos[0] == 0) {
                txtStock.setText(R.string.main_sin_datos);
            } else {
                txtStock.setText(getString(R.string.med_unidades, stock));
            }
        });
    }

    private void abrir(Class<?> destino) {
        Intent intent = new Intent(this, destino);
        intent.putExtra(EXTRA_ID_USUARIO, idUsuario);
        startActivity(intent);
        drawerLayout.closeDrawers();
    }

    private void irLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
