package es.ies.claudiomoyano.dam2.elpastillero;

import java.io.Serializable;

public class Clases {

    public class usuario implements Serializable {
        private int id_usuario;
        private String nombre;
        private String dni;
        private String fech_naci;
        private String telefono;
        private String direccion;
        private String localidad;
        private String codigoP;
        private String provincia;
        private String foto;
        private char activo;

        public usuario() {
        }

        public usuario(String nombre, String dni, String fech_naci, String telefono, String direccion, String localidad, String codigoP, String provincia, String foto, char activo) {
            this.nombre = nombre;
            this.dni = dni;
            this.fech_naci = fech_naci;
            this.telefono = telefono;
            this.direccion = direccion;
            this.localidad = localidad;
            this.codigoP = codigoP;
            this.provincia = provincia;
            this.foto = foto;
            this.activo = activo;
        }

        public usuario(int id_usuario, String nombre, String dni, String fech_naci, String telefono, String direccion, String localidad, String codigoP, String provincia, String foto, char activo) {
            this.id_usuario = id_usuario;
            this.nombre = nombre;
            this.dni = dni;
            this.fech_naci = fech_naci;
            this.telefono = telefono;
            this.direccion = direccion;
            this.localidad = localidad;
            this.codigoP = codigoP;
            this.provincia = provincia;
            this.foto = foto;
            this.activo = activo;
        }
    }

    class familiar implements Serializable {
        private int id_familiar;
        private int id_usuario;
        private String nombre;
        private String telefono;
        private String email;
        private String parentesco;
        private char recibe_alertas;

        public familiar() {
        }

        public familiar(int id_usuario, String nombre, String telefono, String email, String parentesco, char recibe_alertas) {
            this.id_usuario = id_usuario;
            this.nombre = nombre;
            this.telefono = telefono;
            this.email = email;
            this.parentesco = parentesco;
            this.recibe_alertas = recibe_alertas;
        }

        public familiar(int id_familiar, int id_usuario, String nombre, String telefono, String email, String parentesco, char recibe_alertas) {
            this.id_familiar = id_familiar;
            this.id_usuario = id_usuario;
            this.nombre = nombre;
            this.telefono = telefono;
            this.email = email;
            this.parentesco = parentesco;
            this.recibe_alertas = recibe_alertas;
        }
    }

    class categoria implements Serializable {
        private int id_categoria;
        private String nombre;
        private String descricion;

        public categoria() {
        }

        public categoria(int id_categoria, String nombre, String descricion) {
            this.id_categoria = id_categoria;
            this.nombre = nombre;
            this.descricion = descricion;
        }

        public categoria(String nombre, String descricion) {
            this.nombre = nombre;
            this.descricion = descricion;
        }
    }

    class medicamento implements Serializable {
        private int id_medicamento;
        private int id_Usuario;
        private int id_Categoria;
        private String nombre;
        private String principio_Activo;
        private String laboratorio;
        private String dosis;
        private String formato;
        private String cantidad_total;
        private String stock_minimo;
        private String fecha_inicio;
        private String fecha_fin;
        private String instrucciones;
        private String imagen;
        private String activo;

        public medicamento() {
        }

        public medicamento(int id_Usuario, int id_Categoria, String nombre, String principio_Activo, String laboratorio, String dosis, String formato, String cantidad_total, String stock_minimo, String fecha_inicio, String fecha_fin, String instrucciones, String imagen, String activo) {
            this.id_Usuario = id_Usuario;
            this.id_Categoria = id_Categoria;
            this.nombre = nombre;
            this.principio_Activo = principio_Activo;
            this.laboratorio = laboratorio;
            this.dosis = dosis;
            this.formato = formato;
            this.cantidad_total = cantidad_total;
            this.stock_minimo = stock_minimo;
            this.fecha_inicio = fecha_inicio;
            this.fecha_fin = fecha_fin;
            this.instrucciones = instrucciones;
            this.imagen = imagen;
            this.activo = activo;
        }

        public medicamento(int id_medicamento, int id_Usuario, int id_Categoria, String nombre, String principio_Activo, String laboratorio, String dosis, String formato, String cantidad_total, String stock_minimo, String fecha_inicio, String fecha_fin, String instrucciones, String imagen, String activo) {
            this.id_medicamento = id_medicamento;
            this.id_Usuario = id_Usuario;
            this.id_Categoria = id_Categoria;
            this.nombre = nombre;
            this.principio_Activo = principio_Activo;
            this.laboratorio = laboratorio;
            this.dosis = dosis;
            this.formato = formato;
            this.cantidad_total = cantidad_total;
            this.stock_minimo = stock_minimo;
            this.fecha_inicio = fecha_inicio;
            this.fecha_fin = fecha_fin;
            this.instrucciones = instrucciones;
            this.imagen = imagen;
            this.activo = activo;
        }
    }

    class horario implements Serializable {
        private int id_horario;
        private int id_medicamento;
        private String hora;
        private String dosis;
        private String diasSemana;
        private String frecuencia;
        private char activo;

        public horario() {
        }

        public horario(int id_medicamento, String hora, String dosis, String diasSemana, String frecuencia, char activo) {
            this.id_medicamento = id_medicamento;
            this.hora = hora;
            this.dosis = dosis;
            this.diasSemana = diasSemana;
            this.frecuencia = frecuencia;
            this.activo = activo;
        }

        public horario(int id_horario, int id_medicamento, String hora, String dosis, String diasSemana, String frecuencia, char activo) {
            this.id_horario = id_horario;
            this.id_medicamento = id_medicamento;
            this.hora = hora;
            this.dosis = dosis;
            this.diasSemana = diasSemana;
            this.frecuencia = frecuencia;
            this.activo = activo;
        }
    }

    class toma implements Serializable {
        private int id_toma;
        private int id_horario;
        private String fecha;
        private String hora_prevista;
        private String hora_real;
        private String estado;
        private String observaciones;


        public toma(int id_horario, String fecha, String hora_prevista, String hora_real, String estado, String observaciones) {
            this.id_horario = id_horario;
            this.fecha = fecha;
            this.hora_prevista = hora_prevista;
            this.hora_real = hora_real;
            this.estado = estado;
            this.observaciones = observaciones;
        }

        public toma(int id_toma, int id_horario, String fecha, String hora_prevista, String hora_real, String estado, String observaciones) {
            this.id_toma = id_toma;
            this.id_horario = id_horario;
            this.fecha = fecha;
            this.hora_prevista = hora_prevista;
            this.hora_real = hora_real;
            this.estado = estado;
            this.observaciones = observaciones;
        }
    }

    public class Alerta {

        private int id_alerta;
        private int id_usuario;
        private Integer id_medicamento;
        private String tipo;
        private String mensaje;
        private String fecha_hora;
        private int leida;

        // Constructor vacío
        public Alerta() {
        }

        public Alerta(int id_usuario, Integer id_medicamento, String tipo, String mensaje, String fecha_hora, int leida) {
            this.id_usuario = id_usuario;
            this.id_medicamento = id_medicamento;
            this.tipo = tipo;
            this.mensaje = mensaje;
            this.fecha_hora = fecha_hora;
            this.leida = leida;
        }

        public Alerta(int id_alerta, int id_usuario, Integer id_medicamento, String tipo, String mensaje, String fecha_hora, int leida) {
            this.id_alerta = id_alerta;
            this.id_usuario = id_usuario;
            this.id_medicamento = id_medicamento;
            this.tipo = tipo;
            this.mensaje = mensaje;
            this.fecha_hora = fecha_hora;
            this.leida = leida;
        }
    }

    class citaMedica implements Serializable {
        private int id_citaMedica;
        private int id_usuario;
        private String fecha;
        private String hora;
        private String centromedico;
        private String especialidad;
        private String medico;
        private String motivo;
        private String observaciones;
        private String estado;

        public citaMedica() {
        }

        public citaMedica(int id_usuario, String fecha, String hora, String centromedico, String especialidad, String medico, String motivo, String observaciones, String estado) {
            this.id_usuario = id_usuario;
            this.fecha = fecha;
            this.hora = hora;
            this.centromedico = centromedico;
            this.especialidad = especialidad;
            this.medico = medico;
            this.motivo = motivo;
            this.observaciones = observaciones;
            this.estado = estado;
        }

        public citaMedica(int id_citaMedica, int id_usuario, String fecha, String hora, String centromedico, String especialidad, String medico, String motivo, String observaciones, String estado) {

            this.id_citaMedica = id_citaMedica;
            this.id_usuario = id_usuario;
            this.fecha = fecha;
            this.hora = hora;
            this.centromedico = centromedico;
            this.especialidad = especialidad;
            this.medico = medico;
            this.motivo = motivo;
            this.observaciones = observaciones;
            this.estado = estado;
        }
    }

    class historisl implements Serializable {
        public int id_historial;
        public int id_usuario;

        private String hora;
        private String tipo;
        private String descripcion;

        public historisl() {
        }

        public historisl(int id_usuario, String hora, String tipo, String descripcion) {
            this.id_usuario = id_usuario;
            this.hora = hora;
            this.tipo = tipo;
            this.descripcion = descripcion;
        }

        public historisl(int id_historial, int id_usuario, String hora, String tipo, String descripcion) {
            this.id_historial = id_historial;
            this.id_usuario = id_usuario;
            this.hora = hora;
            this.tipo = tipo;
            this.descripcion = descripcion;
        }
    }
}