package es.ies.claudiomoyano.dam2.elpastillero.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Acceso a datos de las tomas (entidad relacionada).
 */
@Dao
public interface TomaDao {

    @Query("SELECT * FROM tomas WHERE idMedicamento = :idMedicamento ORDER BY fechaHora DESC")
    List<Toma> deMedicamento(long idMedicamento);

    @Query("SELECT t.*, m.nombre AS nombreMedicamento FROM tomas t " +
            "INNER JOIN medicamentos m ON m.id = t.idMedicamento ORDER BY t.fechaHora DESC")
    List<TomaDetalle> detalles();

    @Query("SELECT t.*, m.nombre AS nombreMedicamento FROM tomas t " +
            "INNER JOIN medicamentos m ON m.id = t.idMedicamento " +
            "WHERE t.idMedicamento = :idMedicamento ORDER BY t.fechaHora DESC")
    List<TomaDetalle> detallesDe(long idMedicamento);

    @Query("SELECT * FROM tomas WHERE tomada = 0 ORDER BY fechaHora ASC")
    List<Toma> pendientes();

    @Query("SELECT COUNT(*) FROM tomas WHERE tomada = 1 AND fechaHora >= :desde")
    int tomadasDesde(long desde);

    @Insert
    long insertar(Toma toma);

    @Update
    int actualizar(Toma toma);

    @Delete
    int borrar(Toma toma);
}
