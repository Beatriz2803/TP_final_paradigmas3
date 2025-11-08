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
        this.subtotal = calcularSubtotal();
    }

    public DetalleFactura(Integer idDetalleFactura, Integer cantidad, double precioUnitario, double subtotal, Factura factura, Producto producto) {
        this.idDetalleFactura = idDetalleFactura;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.factura = factura;
        this.producto = producto;
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
        this.subtotal = calcularSubtotal(); //poner calcularsubtotal pq al cambiar la cantidad se debe calcular el subtotal
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal(); // misma logica del setCantidad
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
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
        if (producto != null){
         this.setPrecioUnitario(producto.getPrecioUnitario());//si se cambia el producto se actualiza el precio unitario
        }
    }

    public double calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
        return this.subtotal;
    }
    @Override
    public String toString() {
        // Asumimos que Producto tiene un getNombre()
        String nombreProducto = (producto != null) ? producto.getNombre() : "Producto no encontrado";

        return String.format(
                "\n\t- Producto: %s (x%d) | Precio: $%.2f | Subtotal: $%.2f",
                nombreProducto, this.cantidad, this.precioUnitario, this.subtotal
        );
    }
}
