package ar.edu.unlar.paradigmas3.dao.interfaces;

import ar.edu.unlar.paradigmas3.modelo.DetalleFactura;

import java.sql.Connection;
import java.util.List;

public interface IDetalleFacturaDAO {
    List<DetalleFactura> buscarPorNumeroFactura(int numeroFactura);
    void insertarDetalles(Connection conn,List<DetalleFactura> detalles, int numeroFactura)throws Exception;
    DetalleFactura buscarPorId(int id);
}
