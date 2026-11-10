package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class clAlerta {
    private String alerta;
    private String tipo;
    private String mensaje;
    private String fechahora;
    private String leida;

    public clAlerta(String alerta, String tipo, String mensaje, String fechahora, String leida) {
        this.alerta = alerta;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechahora = fechahora;
        this.leida = leida;
    }

    public clAlerta(String tipo, String mensaje, String fechahora) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechahora = fechahora;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public String getLeida() {
        return leida;
    }

    public void setLeida(String leida) {
        this.leida = leida;
    }
}
