package es.ies.claudiomoyano.dam2.elpastillero.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Acceso a datos de la entidad principal. Todas las consultas se lanzan
 * desde AppExecutors, nunca en el hilo principal.
 */
@Dao
public interface MedicamentoDao {

    @Query("SELECT * FROM medicamentos ORDER BY nombre COLLATE NOCASE")
    List<Medicamento> todos();

    @Query("SELECT * FROM medicamentos WHERE nombre LIKE :filtro OR principioActivo LIKE :filtro ORDER BY nombre COLLATE NOCASE")
    List<Medicamento> buscar(String filtro);

    @Query("SELECT * FROM medicamentos WHERE id = :id")
    Medicamento porId(long id);

    @Query("SELECT COUNT(*) FROM medicamentos")
    int contar();

    @Query("SELECT * FROM medicamentos WHERE stock <= stockMinimo ORDER BY stock ASC")
    List<Medicamento> stockBajo();

    @Query("SELECT IFNULL(SUM(stock), 0) FROM medicamentos")
    int stockTotal();

    @Insert
    long insertar(Medicamento medicamento);

    @Update
    int actualizar(Medicamento medicamento);

    @Delete
    int borrar(Medicamento medicamento);

    @Query("UPDATE medicamentos SET stock = CASE WHEN stock >= :cantidad THEN stock - :cantidad ELSE 0 END WHERE id = :id")
    void descontarStock(long id, int cantidad);

    @Query("UPDATE medicamentos SET stock = stock + :cantidad WHERE id = :id")
    void devolverStock(long id, int cantidad);
}
