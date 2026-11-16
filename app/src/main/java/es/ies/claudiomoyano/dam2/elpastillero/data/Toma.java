package es.ies.claudiomoyano.dam2.elpastillero.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad relacionada 1-N con Medicamento. Si se borra un medicamento,
 * sus tomas se borran automáticamente (ON DELETE CASCADE).
 */
@Entity(
        tableName = "tomas",
        foreignKeys = @ForeignKey(
                entity = Medicamento.class,
                parentColumns = "id",
                childColumns = "idMedicamento",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("idMedicamento")}
)
public class Toma {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long idMedicamento;
    public long fechaHora;
    public int cantidad;
    public boolean tomada;
    public String observaciones;
}
