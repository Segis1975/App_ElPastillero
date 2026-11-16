package es.ies.claudiomoyano.dam2.elpastillero.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Base de datos Room con la entidad principal (Medicamento) y la relacionada (Toma).
 * <p>
 * La primera vez que se crea el archivo se cargan seis medicamentos y una toma de
 * ejemplo. La carga se hace de forma síncrona dentro de {@code onCreate} usando la
 * propia {@link SupportSQLiteDatabase}: así la primera consulta que haga la interfaz
 * ya encuentra los datos. Las consultas normales de la app sí se lanzan fuera del
 * hilo principal con {@code AppExecutors}.
 */
@Database(entities = {Medicamento.class, Toma.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String NOMBRE = "pastillero_room.db";
    private static final String TABLA_MEDICAMENTOS = "medicamentos";
    private static final String TABLA_TOMAS = "tomas";

    private static volatile AppDatabase instancia;

    public abstract MedicamentoDao medicamentoDao();

    public abstract TomaDao tomaDao();

    public static AppDatabase get(Context contexto) {
        if (instancia == null) {
            synchronized (AppDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(
                                    contexto.getApplicationContext(),
                                    AppDatabase.class,
                                    NOMBRE)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Carga inicial: 6 medicamentos y 1 relación (toma).
                                    sembrar(db);
                                }
                            })
                            .build();
                }
            }
        }
        return instancia;
    }

    /** Inserta los datos de ejemplo directamente sobre la base de datos recién creada. */
    private static void sembrar(SupportSQLiteDatabase db) {
        long ahora = System.currentTimeMillis();

        long idParacetamol = insertarMedicamento(db, "Paracetamol", "Paracetamol",
                "1 comprimido cada 8 h", 1, 24, 6, 8,
                "pastilla", "Comprimidos", true, 4, "activo",
                "Tomar con agua, después de las comidas", true, ahora);

        insertarMedicamento(db, "Ibuprofeno", "Ibuprofeno",
                "1 comprimido cada 12 h", 1, 14, 5, 12,
                "pastilla", "Comprimidos", true, 3, "activo",
                "No tomar en ayunas", true, ahora);

        insertarMedicamento(db, "Omeprazol", "Omeprazol",
                "1 cápsula en ayunas", 1, 30, 10, 24,
                "pastilla", "Cápsulas", false, 5, "activo",
                "30 minutos antes del desayuno", true, ahora);

        insertarMedicamento(db, "Metformina", "Metformina",
                "1 comprimido con la comida", 1, 8, 10, 24,
                "pastilla", "Comprimidos", false, 4, "activo",
                "Revisar stock: queda poco", true, ahora);

        insertarMedicamento(db, "Jarabe para la tos", "Dextrometorfano",
                "10 ml cada 8 h", 10, 4, 2, 8,
                "jarabe", "Jarabe", false, 3, "pausado",
                "Solo si hay tos", false, ahora);

        insertarMedicamento(db, "Vitamina D", "Colecalciferol",
                "1 gota al día", 1, 20, 5, 24,
                "inyeccion", "Ampolla", false, 5, "activo",
                "Una ampolla al mes", false, ahora);

        // Relación 1-N: una toma del primer medicamento.
        ContentValues toma = new ContentValues();
        toma.put("idMedicamento", idParacetamol);
        toma.put("fechaHora", ahora - 3 * 60 * 60 * 1000L);
        toma.put("cantidad", 1);
        toma.put("tomada", 1);
        toma.put("observaciones", "Toma de la mañana");
        db.insert(TABLA_TOMAS, SQLiteDatabase.CONFLICT_IGNORE, toma);
    }

    private static long insertarMedicamento(SupportSQLiteDatabase db, String nombre,
                                            String principio, String dosis, int unidades,
                                            int stock, int stockMinimo, int frecuencia,
                                            String tipo, String presentacion, boolean favorito,
                                            int valoracion, String estado, String notas,
                                            boolean recordatorio, long fecha) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("principioActivo", principio);
        valores.put("dosis", dosis);
        valores.put("unidadesPorToma", unidades);
        valores.put("stock", stock);
        valores.put("stockMinimo", stockMinimo);
        valores.put("frecuenciaHoras", frecuencia);
        valores.put("tipo", tipo);
        valores.put("presentacion", presentacion);
        valores.put("favorito", favorito ? 1 : 0);
        valores.put("valoracion", valoracion);
        valores.put("estado", estado);
        valores.put("notas", notas);
        valores.put("fechaInicio", fecha);
        valores.put("recordatorio", recordatorio ? 1 : 0);
        valores.put("creado", fecha);
        return db.insert(TABLA_MEDICAMENTOS, SQLiteDatabase.CONFLICT_ABORT, valores);
    }
}
