package es.ies.claudiomoyano.dam2.elpastillero.model;

/**
 * Usuario de la aplicación (tabla usuarios de SQLite).
 * La contraseña no se expone en este objeto: solo la usa el helper para validar.
 */
public class Usuario {

    public long id;
    public String nombre;
    public String email;

    public Usuario(long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }
}
