package ar.edu.unlar.paradigmas3.modelo;

public class Producto {
    private String idProducto;
    private String nombre;
    private String descripcion;
    private Double precioUnitario;
    private Integer stock;
    private Categoria categoria;

    public Producto() {
    }

    public Producto(String idProducto, String nombre, String descripcion, Double precioUnitario, Integer stock, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.categoria = categoria;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean stockDisponible(int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        return this.stock >= cantidad;
    }

    public void disminuirStock(int cantidad) {
        if (stockDisponible(cantidad)) {
            this.stock -= cantidad;
        } else {
            System.err.println("Advertencia: No se puede disminuir el stock por debajo de 0.");
        }
    }
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.stock += cantidad;
        }
    }
}
