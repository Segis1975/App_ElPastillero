package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class Categoria {
    private String nombre, desctipcion;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(String nombre, String desctipcion) {
        this.nombre = nombre;
        this.desctipcion = desctipcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDesctipcion() {
        return desctipcion;
    }

    public void setDesctipcion(String desctipcion) {
        this.desctipcion = desctipcion;
    }
}
