package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class clMedicamento {
    public String nombre;
    public String familia;
    public String laboratorio;
    public String formato;
    public String dosis;
    public String cantidad;
    public String stock;
    public String fechainicio;
    public String fechafin;
    public String imagen;
    public String activo;


    public clMedicamento(String nombre) {
        this.nombre = nombre;
    }

    public clMedicamento(String nombre, String familia, String laboratorio, String formato, String dosis, String cantidad, String stock, String fechainicio, String fechafin, String imagen, String activo) {
        this.nombre = nombre;
        this.familia = familia;
        this.laboratorio = laboratorio;
        this.formato = formato;
        this.dosis = dosis;
        this.cantidad = cantidad;
        this.stock = stock;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
        this.imagen = imagen;
        this.activo = activo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}
