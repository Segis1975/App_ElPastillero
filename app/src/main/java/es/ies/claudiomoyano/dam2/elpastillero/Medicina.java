package es.ies.claudiomoyano.dam2.elpastillero;

public class Medicina {
    int imagen;
    String nombre;
    String dosis;
    String hora;
    public Medicina(int imagen, String nombre, String dosis, String hora) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.dosis = dosis;
        this.hora = hora;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public String getHora() {
        return hora;
    }
}
