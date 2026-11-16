package es.ies.claudiomoyano.dam2.elpastillero.data;

import androidx.room.Embedded;

/**
 * Resultado de la consulta que une tomas y medicamentos para el listado.
 */
public class TomaDetalle {

    @Embedded
    public Toma toma;

    public String nombreMedicamento;
}
