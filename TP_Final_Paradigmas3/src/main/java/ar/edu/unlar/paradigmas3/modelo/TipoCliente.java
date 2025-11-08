package ar.edu.unlar.paradigmas3.modelo;

public class TipoCliente {
   private int id_tipo_cliente;
   private String nombre;

    public TipoCliente() {
    }

    public TipoCliente(int id_tipo_cliente, String nombre) {
        this.id_tipo_cliente = id_tipo_cliente;
        this.nombre = nombre;
    }

    public int getId_tipo_cliente() {
        return id_tipo_cliente;
    }

    public void setId_tipo_cliente(int id_tipo_cliente) {
        this.id_tipo_cliente = id_tipo_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "TipoCliente: " +
                "id_tipo_cliente: " + id_tipo_cliente +
                ", nombre: " + nombre;
    }
}