package es.ies.claudiomoyano.dam2.elpastillero.model;

/**
 * Cita médica (tabla citas de SQLite). La fecha se guarda en milisegundos.
 */
public class Cita {

    public long id;
    public long idUsuario;
    public String especialidad;
    public String medico;
    public String lugar;
    public long fechaHora;
    public String notas;
}
