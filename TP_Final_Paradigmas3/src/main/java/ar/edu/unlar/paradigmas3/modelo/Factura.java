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
    private List<DetalleFactura> detalleFacturas;

    public Factura() {
    }

    public Factura(int numeroFactura, LocalDate fechaGeneracion, Cliente cliente, FormaPago formaPago, List<DetalleFactura> detalleFacturas) {
        this.numeroFactura = numeroFactura;
        this.fechaGeneracion = fechaGeneracion;
        this.cliente = cliente;
        this.formaPago = formaPago;
        this.detalleFacturas = detalleFacturas;
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

    public List<DetalleFactura> getDetalleFacturas() {
        return detalleFacturas;
    }

    public void setDetalleFacturas(List<DetalleFactura> detalleFacturas) {
        this.detalleFacturas = detalleFacturas;
    }

    public double calcularTotal() {
        double total = 0.0;
        for (DetalleFactura detalleFactura : this.detalleFacturas) {
            total += detalleFactura.calcularSubtotal();
        }
        return total;
    }
    // Crea y añade un nuevo item de detalle a la factura.

    public void agregarItem(Producto producto, int cantidad) {
        DetalleFactura nuevoDetalle = new DetalleFactura();
        nuevoDetalle.setProducto(producto);
        nuevoDetalle.setCantidad(cantidad);

        this.detalleFacturas.add(nuevoDetalle);
    }

    @Override
    public String toString() {
        // Calculamos el total primero para poder mostrarlo
        double totalFactura = this.calcularTotal();

        System.out.printf("""
            
            --- FACTURA ---
            Número: %s
            Fecha: %s
            Cliente: %s
            Forma de Pago: %s
            Total: $%.2f
            Detalles: %s
            """,
                this.numeroFactura,
                this.fechaGeneracion,
                this.cliente,         // Usa el toString() de Cliente
                this.formaPago,         // Usa el toString() de FormaPago
                totalFactura,
                this.detalleFacturas  // Usa el toString() de la Lista
        );

        return "";
    }
}


