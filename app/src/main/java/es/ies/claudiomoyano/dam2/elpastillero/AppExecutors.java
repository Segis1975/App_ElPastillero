package es.ies.claudiomoyano.dam2.elpastillero;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ejecuta las consultas a la base de datos fuera del hilo principal y devuelve
 * el resultado en el hilo de interfaz. Así se cumple el requisito de no bloquear la UI.
 */
public final class AppExecutors {

    private static final ExecutorService IO = Executors.newSingleThreadExecutor();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private AppExecutors() {
    }

    public interface Callback<T> {
        void onResult(T valor);
    }

    public static void io(Runnable tarea) {
        IO.execute(tarea);
    }

    public static <T> void io(Callable<T> tarea, Callback<T> callback) {
        IO.execute(() -> {
            T resultado;
            try {
                resultado = tarea.call();
            } catch (Exception e) {
                resultado = null;
            }
            final T valor = resultado;
            MAIN.post(() -> callback.onResult(valor));
        });
    }

    public static void main(Runnable tarea) {
        MAIN.post(tarea);
    }
}
