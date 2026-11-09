package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class CitaMedica {
   private String fecha;
   private String hora;
   private String lugar;
   private String especialista;
   private String nombremedico;
   private String motivo;
   private String observaciones;
   private String estado;

   public CitaMedica(String fecha, String hora, String lugar, String especialista) {
      this.fecha = fecha;
      this.hora = hora;
      this.lugar = lugar;
      this.especialista = especialista;
   }

   public CitaMedica(String fecha, String hora, String lugar, String especialista, String nombremedico, String motivo, String observaciones, String estado) {
      this.fecha = fecha;
      this.hora = hora;
      this.lugar = lugar;
      this.especialista = especialista;
      this.nombremedico = nombremedico;
      this.motivo = motivo;
      this.observaciones = observaciones;
      this.estado = estado;
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

   public String getLugar() {
      return lugar;
   }

   public void setLugar(String lugar) {
      this.lugar = lugar;
   }

   public String getEspecialista() {
      return especialista;
   }

   public void setEspecialista(String especialista) {
      this.especialista = especialista;
   }

   public String getNombremedico() {
      return nombremedico;
   }

   public void setNombremedico(String nombremedico) {
      this.nombremedico = nombremedico;
   }

   public String getMotivo() {
      return motivo;
   }

   public void setMotivo(String motivo) {
      this.motivo = motivo;
   }

   public String getObservaciones() {
      return observaciones;
   }

   public void setObservaciones(String observaciones) {
      this.observaciones = observaciones;
   }

   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }
}
