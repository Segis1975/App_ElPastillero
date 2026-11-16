package es.ies.claudiomoyano.dam2.elpastillero.model;

/**
 * Elemento de la lista personal de un usuario (tabla favoritos de SQLite).
 * Guarda el id del medicamento de Room y una nota propia del usuario.
 */
public class Favorito {

    public long id;
    public long idUsuario;
    public long idMedicamento;
    public String nombreMedicamento;
    public String nota;
    public long fecha;
}
