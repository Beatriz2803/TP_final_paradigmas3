package ar.edu.unlar.paradigmas3.dao.interfaces;

import ar.edu.unlar.paradigmas3.modelo.Factura;

import java.util.List;

public interface IFacturaDAO {
    //Metodo principal
    boolean Generar(Factura factura);
    Factura buscarPorNumero(int numeroFactura);
    List<Factura> listar();
}
