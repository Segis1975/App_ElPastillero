package es.ies.claudiomoyano.dam2.elpastillero;

import java.io.Serializable;

public class Medicamento implements Serializable {
    String nombre;
    String categoria;
    String laboratorio;
    String dosis;
    String hora;
    String cantidad;
    String fechaI;
    String fechaF;

    int image;

    public Medicamento(String nombre, String categoria, String laboratorio, String dosis, String hora, String cantidad, String fechaI, String fechaF) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.laboratorio = laboratorio;
        this.dosis = dosis;
        this.hora = hora;
        this.cantidad = cantidad;
        this.fechaI = fechaI;
        this.fechaF = fechaF;
    }

    public Medicamento(String nombre, String categoria, String laboratorio, String dosis, String hora, String cantidad, String fechaI, String fechaF, int image) {
        this.nombre = nombre;
        this.image = image;
        this.fechaF = fechaF;
        this.fechaI = fechaI;
        this.cantidad = cantidad;
        this.hora = hora;
        this.dosis = dosis;
        this.categoria = categoria;
        this.laboratorio = laboratorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaI() {
        return fechaI;
    }

    public void setFechaI(String fechaI) {
        this.fechaI = fechaI;
    }

    public String getFechaF() {
        return fechaF;
    }

    public void setFechaF(String fechaF) {
        this.fechaF = fechaF;
    }

    public void setImage(int image) {this.image = image;}

    public int getImage() {return image;    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "nombre='" + nombre + '\'' +
                ", fechaF='" + fechaF + '\'' +
                ", fechaI='" + fechaI + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", dosis='" + dosis + '\'' +
                ", hora='" + hora + '\'' +
                ", laboratorio='" + laboratorio + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }

}
