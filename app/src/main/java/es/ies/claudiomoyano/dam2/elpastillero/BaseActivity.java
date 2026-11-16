package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Context;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad base que aplica el tamaño de letra elegido en Ajustes.
 * Se aplica en attachBaseContext para que afecte a todos los textos de la pantalla.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration configuracion = new Configuration(newBase.getResources().getConfiguration());
        configuracion.fontScale = Sesion.escalaLetra(newBase);
        super.attachBaseContext(newBase.createConfigurationContext(configuracion));
    }
}
