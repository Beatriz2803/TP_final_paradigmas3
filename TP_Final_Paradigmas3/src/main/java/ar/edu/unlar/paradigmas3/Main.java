package ar.edu.unlar.paradigmas3;

import ar.edu.unlar.paradigmas3.modelo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDate fechaDeHoy = LocalDate.now();
        Categoria categoria1 = new Categoria("Bebida", 01);
        Cliente cliente1 = new Cliente(2, "juan cabrera", "40.256.598", "av. porongol", "26985416", TipoCliente.MINORISTA);
        Producto producto1 = new Producto("03QL", "Cerveza", "Bebida Alcoholica", 3000.0, 5, categoria1);
        Producto producto2 = new Producto("04L", "Coca Cola", "Bebida", 2000.0, 5, categoria1);
        DetalleFactura detalleFactura1 = new DetalleFactura(producto1, 2);
        DetalleFactura detalleFactura2 = new DetalleFactura(producto2, 3);
        List<DetalleFactura> listaDeDetalles = new ArrayList<>();

        listaDeDetalles.add(detalleFactura1);
        listaDeDetalles.add(detalleFactura2);
        Factura factura1 = new Factura(01, fechaDeHoy, cliente1, FormaPago.CONTADO, listaDeDetalles);
        System.out.println("Factura creada para: " + factura1.getCliente().getNombreCompleto() );
        System.out.println(factura1.calcularTotal());
        System.out.println(factura1);

    }
}