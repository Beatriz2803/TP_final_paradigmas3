package ar.edu.unlar.paradigmas3.modelo;

public enum TipoCliente {
    MINORISTA("Minorista"),
    MAYORISTA("Mayorista");

    private final String descripcion;

    TipoCliente(String descripcion) {
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