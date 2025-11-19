package ar.edu.unlar.paradigmas3.dao.impl;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.IDetalleFacturaDAO;
import ar.edu.unlar.paradigmas3.dao.interfaces.IProductoDAO;
import ar.edu.unlar.paradigmas3.modelo.DetalleFactura;
import ar.edu.unlar.paradigmas3.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaDAO implements IDetalleFacturaDAO {

    // Dependencia para buscar el Producto asociado
    private final IProductoDAO productoDAO = new ProductoDAO();

    // mapear ResultSet a Objeto DetalleFactura
    private DetalleFactura mapearDetalle(ResultSet rs) throws Exception {
        DetalleFactura detalle = new DetalleFactura();
        detalle.setIdDetalleFactura(rs.getInt("id_detalle_factura"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
        detalle.setSubtotal(rs.getDouble("subtotal")); // El subtotal viene calculado de la DB

        //ASOCIACIÓN: Reconstruir el Producto (no necesitamos la Factura aquí)
        int idProducto = rs.getInt("id_producto");
        Producto producto = productoDAO.buscarPorId(idProducto);
        detalle.setProducto(producto);

        return detalle;
    }

    @Override
    public List<DetalleFactura> buscarPorNumeroFactura(int numeroFactura) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DetalleFactura> detalles = new ArrayList<>();

        var sql = "SELECT id_detalle_factura, cantidad, precio_unitario, subtotal, id_producto " +
                "FROM detalle_factura WHERE numero_factura = ?";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, numeroFactura);
            rs = ps.executeQuery();

            while (rs.next()) {
                detalles.add(mapearDetalle(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al listar detalles por factura: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return detalles;
    }

     // usa el parametro conn que es La conexión activa de FacturaDAO.

    @Override
    public void insertarDetalles(Connection conn, List<DetalleFactura> detalles, int numeroFactura) throws Exception {
        PreparedStatement ps = null;

        var sql = "INSERT INTO detalle_factura (cantidad, precio_unitario, subtotal, id_producto, numero_factura) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            ps = conn.prepareStatement(sql);

            for (DetalleFactura detalle : detalles) {
                double subtotalcalculado = detalle.calcularSubtotal();

                ps.setInt(1, detalle.getCantidad());
                ps.setDouble(2, detalle.getPrecioUnitario());
                ps.setDouble(3, subtotalcalculado);
                ps.setInt(4, detalle.getProducto().getIdProducto());
                ps.setInt(5, numeroFactura);


                // Usamos addBatch y executeBatch
                ps.addBatch();
            }

            ps.executeBatch(); // Ejecuta todas las inserciones en un solo viaje a la DB

        } catch (Exception e) {
            // Relanza la excepción para que FacturaDAO pueda hacer el Rollback
            throw new Exception("Fallo al insertar detalles de factura: " + e.getMessage(), e);
        } finally {
            Conexion.close(ps);
        }
    }

}
