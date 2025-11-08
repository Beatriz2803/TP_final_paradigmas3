package ar.edu.unlar.paradigmas3.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Factura {
    private int numeroFactura; // el tp pide que sea de valor unico, podemos usarlo como pk y sacar el id
    private LocalDate fechaGeneracion;
    private Cliente cliente;
    private FormaPago formaPago;
    private double total; //agregado para mapear campo total de DB
    private String observaciones;
    private List<DetalleFactura> detalleFacturas;

    public Factura() {
        this.detalleFacturas = new ArrayList<>();
    }

    public Factura(int numeroFactura, LocalDate fechaGeneracion, Cliente cliente, FormaPago formaPago, double total, String observaciones, List<DetalleFactura> detalleFacturas) {
        this.numeroFactura = numeroFactura;
        this.fechaGeneracion = fechaGeneracion;
        this.cliente = cliente;
        this.formaPago = formaPago;
        this.total = total;
        this.observaciones = observaciones;
        this.detalleFacturas = detalleFacturas != null ? detalleFacturas : new ArrayList<>(); //comprueba si el DF es != de null para iniciar
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleFactura> getDetalleFacturas() {
        return detalleFacturas;
    }

    public void setDetalleFacturas(List<DetalleFactura> detalleFacturas) {
        this.detalleFacturas = detalleFacturas;
    }

    public double calcularTotal() {
        double totalCalculado = 0.0;
        if (this.detalleFacturas != null) {
        for (DetalleFactura detalleFactura : this.detalleFacturas) {
            totalCalculado += detalleFactura.calcularSubtotal();
        } }
        return totalCalculado;
    }
    // Crea y añade un nuevo item de detalle a la factura.

    public void agregarItem(Producto producto, int cantidad) {
        // asegura inicialización de la list si no se hizo en el constructor y si es null, la crea
        if (this.detalleFacturas == null) {
        this.detalleFacturas = new ArrayList<>();
        }
        DetalleFactura nuevoDetalle = new DetalleFactura();
        nuevoDetalle.setProducto(producto);
        nuevoDetalle.setCantidad(cantidad);
        nuevoDetalle.setPrecioUnitario(producto.getPrecioUnitario());
        this.detalleFacturas.add(nuevoDetalle);
    }

    public void limpiarDetalles (){
        this.detalleFacturas.clear();
    }

    @Override
    public String toString() {
        // Calculamos el total primero para poder mostrarlo
        double totalFactura = (this.total > 0) ? this.total : this.calcularTotal();

        return String.format("""
            
            --- FACTURA ---
            Número: %d
            Fecha: %s
            Cliente: %s
            Forma de Pago: %s
            Observación: %s
            Total: $%.2f
            Detalles: %s
            """,
                this.numeroFactura,
                this.fechaGeneracion,
                this.cliente != null ? this.cliente.getNombreCompleto() : "N/A",
                this.formaPago != null ? this.formaPago.getNombre() : "N/A",
                this.observaciones != null ? this.observaciones : "Sin observaciones",
                totalFactura,
                this.detalleFacturas != null ? this.detalleFacturas.toString(): "Ninguno"  // Usa el toString() de la Lista
        );

    }
}


