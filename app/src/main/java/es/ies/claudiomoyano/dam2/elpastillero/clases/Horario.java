package es.ies.claudiomoyano.dam2.elpastillero.clases;

import es.ies.claudiomoyano.dam2.elpastillero.db.HorarioContract;
import es.ies.claudiomoyano.dam2.elpastillero.db.MedicamentoContract;

public class Horario {

    private String fecha;
    private String hora;
    private String diasSemana;
    private String activo;

    public Horario(String fecha, String hora) {
        this.fecha = fecha;
        this.hora = hora;
    }

    public Horario(String activo, String diasSemana, String hora, String fecha) {
        this.activo = activo;
        this.diasSemana = diasSemana;
        this.hora = hora;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}
