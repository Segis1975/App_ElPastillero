package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class clContacto {

    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private  String parentesco;
    private int alertas;

    public clContacto() {
    }

    public clContacto(String nombre) {
        this.nombre = nombre;
    }

    public clContacto(String nombre, String telefono, String email, String parentesco, int alertas) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.parentesco = parentesco;
        this.alertas = alertas;
    }

    public clContacto(int id, String nombre, String telefono, String email, String parentesco, int alertas) {
        this.id = id;
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

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
