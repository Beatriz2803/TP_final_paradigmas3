package ar.edu.unlar.paradigmas3.modelo;

public class Cliente {
    private Integer idCliente;
    private String nombreCompleto;
    private String dni;
    private String domicilio;
    private String telefono;
    private TipoCliente tipoCliente;

    public Cliente() {
    }

    public Cliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
//para insertar un cliente, sin id pq se genera automaticamente en tabla
    public Cliente(String nombreCompleto, String dni, String domicilio, String telefono, TipoCliente tipoCliente) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
    }
//para leer o modificar en BD
    public Cliente(Integer idCliente, String nombreCompleto, String dni, String domicilio, String telefono, TipoCliente tipoCliente) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }


    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public boolean esFinal() {
        // validamos que tipoCliente y su descripción no sean NULL
        if (this.tipoCliente == null || this.tipoCliente.getNombre() == null) {
            return false;
        }
        return this.tipoCliente.getNombre().equalsIgnoreCase("Final");
    }

    public boolean esMayorista() {
        if (this.tipoCliente == null || this.tipoCliente.getNombre() == null) {
            return false;
        }
        return this.tipoCliente.getNombre().equalsIgnoreCase("Mayorista");
    }

    @Override
    public String toString() {
        return String.format(
               "Clienteid: %d | Nombre: %s | DNI: %s | Tipo: %s ",
                this.idCliente,this.nombreCompleto,this.dni,
                this.tipoCliente != null ? this.tipoCliente.getNombre() : "N/A"
        );
    }
}
