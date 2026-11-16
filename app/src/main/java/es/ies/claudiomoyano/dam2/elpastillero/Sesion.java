package es.ies.claudiomoyano.dam2.elpastillero;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Guarda la sesión del usuario y las preferencias de accesibilidad.
 * El identificador del usuario viaja entre pantallas tanto por Intent como por aquí.
 */
public final class Sesion {

    private static final String PREF = "sesion";
    private static final String K_ID = "idUsuario";
    private static final String K_NOMBRE = "nombre";
    private static final String K_EMAIL = "email";
    private static final String K_RECORDAR = "recordar";
    private static final String K_LETRA = "escalaLetra";
    private static final String K_NOTIF = "notificaciones";
    private static final String K_TEMA = "tema";

    public static final int TEMA_SISTEMA = 0;
    public static final int TEMA_CLARO = 1;
    public static final int TEMA_OSCURO = 2;

    private Sesion() {
    }

    private static SharedPreferences prefs(Context contexto) {
        return contexto.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static void guardar(Context contexto, long id, String nombre, String email, boolean recordar) {
        prefs(contexto).edit()
                .putLong(K_ID, id)
                .putString(K_NOMBRE, nombre)
                .putString(K_EMAIL, email)
                .putBoolean(K_RECORDAR, recordar)
                .apply();
    }

    public static long id(Context contexto) {
        return prefs(contexto).getLong(K_ID, -1);
    }

    public static String nombre(Context contexto) {
        return prefs(contexto).getString(K_NOMBRE, "");
    }

    public static String email(Context contexto) {
        return prefs(contexto).getString(K_EMAIL, "");
    }

    public static boolean recordar(Context contexto) {
        return prefs(contexto).getBoolean(K_RECORDAR, false);
    }

    public static boolean activa(Context contexto) {
        return id(contexto) > 0;
    }

    public static void cerrar(Context contexto) {
        prefs(contexto).edit().remove(K_ID).remove(K_NOMBRE).remove(K_EMAIL).remove(K_RECORDAR).apply();
    }

    // --- Preferencias de accesibilidad ---

    public static float escalaLetra(Context contexto) {
        return prefs(contexto).getFloat(K_LETRA, 1f);
    }

    public static void setEscalaLetra(Context contexto, float escala) {
        prefs(contexto).edit().putFloat(K_LETRA, escala).apply();
    }

    public static boolean notificaciones(Context contexto) {
        return prefs(contexto).getBoolean(K_NOTIF, true);
    }

    public static void setNotificaciones(Context contexto, boolean activas) {
        prefs(contexto).edit().putBoolean(K_NOTIF, activas).apply();
    }

    public static int tema(Context contexto) {
        return prefs(contexto).getInt(K_TEMA, TEMA_SISTEMA);
    }

    public static void setTema(Context contexto, int tema) {
        prefs(contexto).edit().putInt(K_TEMA, tema).apply();
    }
}
