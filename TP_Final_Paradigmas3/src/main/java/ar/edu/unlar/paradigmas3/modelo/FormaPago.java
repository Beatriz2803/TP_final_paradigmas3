package ar.edu.unlar.paradigmas3.modelo;

public enum FormaPago {
    // Lista de valores constantes
    CONTADO("Contado"),
    DEBITO("Tarjeta de Débito"),
    TARJETA("Tarjeta de Crédito"),
    TRANSFERENCIA("Transferencia Bancaria");


    private final String descripcion;


    FormaPago(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return this.descripcion;
    }
}