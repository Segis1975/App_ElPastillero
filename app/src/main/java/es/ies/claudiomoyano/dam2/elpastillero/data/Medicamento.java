package es.ies.claudiomoyano.dam2.elpastillero.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad principal de Room: un medicamento del usuario.
 * Se relaciona 1-N con la entidad Toma.
 */
@Entity(tableName = "medicamentos")
public class Medicamento {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String nombre;
    public String principioActivo;
    public String dosis;
    public int unidadesPorToma;
    public int stock;
    public int stockMinimo;
    public int frecuenciaHoras;
    public String tipo;
    public String presentacion;
    public boolean favorito;
    public int valoracion;
    public String estado;
    public String notas;
    public long fechaInicio;
    public boolean recordatorio;
    public long creado;

    /** Devuelve true cuando el stock está en el mínimo o por debajo. */
    public boolean stockBajo() {
        return stock <= stockMinimo;
    }
}
