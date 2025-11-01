package ar.edu.unlar.paradigmas3.modelo;

public class DetalleFactura {
    private Integer idDetalleFactura;
    private Integer cantidad;
    private double precioUnitario;
    private double subtotal;
    private Factura factura;
    private Producto producto;

    public DetalleFactura() {
    }

    public DetalleFactura(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioUnitario();
        this.calcularSubtotal();
    }

    public Integer getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(Integer idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
        return this.subtotal;
    }
    @Override
    public String toString() {
        // Asumimos que Producto tiene un getNombre()
        String nombreProducto = (producto != null) ? producto.getNombre() : "N/A";

        return "Detalle{" +
                "producto=" + nombreProducto +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}
