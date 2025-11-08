package ar.edu.unlar.paradigmas3.modelo;

public class FormaPago {
    private int id_forma_pago;
    private String nombre;

    public FormaPago() {
    }

    public FormaPago(int id_forma_pago, String nombre) {
        this.id_forma_pago = id_forma_pago;
        this.nombre = nombre;
    }

    public int getId_forma_pago() {
        return id_forma_pago;
    }

    public void setId_forma_pago(int id_forma_pago) {
        this.id_forma_pago = id_forma_pago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "FormaPago:" +
                "id_forma_pago=" + id_forma_pago +
                ", nombre=" + nombre;
    }
}