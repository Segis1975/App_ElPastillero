package es.ies.claudiomoyano.dam2.elpastillero.clases;

public class clUsuario {
    private String nombre;
    private String DNI;
    private String domicilio;
    private String localidad;
    private String codigopostal;
    private String provincia;
    private String  fechanacimiento;
    private String telefono;
    private String email;
    private  String activo;

    public clUsuario() {
    }

    public clUsuario(String nombre, String DNI, String domicilio, String localidad, String codigopostal, String provincia, String fechanacimiento, String telefono, String email, String activo) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.codigopostal = codigopostal;
        this.provincia = provincia;
        this.fechanacimiento = fechanacimiento;
        this.telefono = telefono;
        this.email = email;
        this.activo = this.activo;
    }

    public clUsuario(String nombre, String DNI, String activo) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public String getprovincia() {
        return provincia;
    }

    public void setprovincia(String provincia) {
        this.provincia = provincia;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
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

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}