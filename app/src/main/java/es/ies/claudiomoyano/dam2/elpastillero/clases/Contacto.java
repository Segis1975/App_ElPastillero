package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class Contacto {

    private String nombre;
    private String telefono;
    private String email;
    private  String parentesco;
    private String alertas;

    public Contacto(String nombre) {
        this.nombre = nombre;
    }

    public Contacto(String nombre, String telefono, String email, String parentesco, String alertas) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.parentesco = parentesco;
        this.alertas = alertas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getAlertas() {
        return alertas;
    }

    public void setAlertas(String alertas) {
        this.alertas = alertas;
    }
}
