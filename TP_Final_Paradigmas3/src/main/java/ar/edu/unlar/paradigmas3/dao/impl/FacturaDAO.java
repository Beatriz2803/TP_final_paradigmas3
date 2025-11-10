package ar.edu.unlar.paradigmas3.dao.impl;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.IClienteDAO;
import ar.edu.unlar.paradigmas3.dao.interfaces.IDetalleFacturaDAO;
import ar.edu.unlar.paradigmas3.dao.interfaces.IFacturaDAO;
import ar.edu.unlar.paradigmas3.dao.interfaces.IFormaPagoDAO;
import ar.edu.unlar.paradigmas3.modelo.Factura;
import ar.edu.unlar.paradigmas3.modelo.DetalleFactura;
import ar.edu.unlar.paradigmas3.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class FacturaDAO implements IFacturaDAO {

    // Dependencias para mapear objetos asociados
    private final IClienteDAO clienteDAO = new ClienteDAO();
    private final IFormaPagoDAO formaPagoDAO = new FormaPagoDAO();
    private final IDetalleFacturaDAO detalleFacturaDAO = new DetalleFacturaDAO();
    private final ProductoDAO productoDAO = new ProductoDAO(); // Necesario para actualizar stock

    // Metodo Auxiliar para mapear Factura ---
    private Factura mapearFactura(ResultSet rs) throws Exception {
        Factura factura = new Factura();
        factura.setNumeroFactura(rs.getInt("numero_factura"));

        // Mapeo de Date a LocalDate
        factura.setFechaGeneracion(rs.getDate("fecha_generacion").toLocalDate());

        // **ASOCIACIONES:** Reconstruir Cliente y FormaPago
        int idCliente = rs.getInt("id_cliente");
        factura.setCliente(clienteDAO.buscarPorId(idCliente));

        int idFormaPago = rs.getInt("id_forma_pago");
        factura.setFormaPago(formaPagoDAO.buscarPorId(idFormaPago));

        factura.setTotal(rs.getDouble("total"));
        factura.setObservaciones(rs.getString("observaciones"));

        return factura;
    }


    /**
     * MÉTODO TRANSACCIONAL CLAVE:
     * 1. Inserta la factura.
     * 2. Inserta los detalles.
     * 3. Descuenta stock.
     */
    @Override
    public boolean generar(Factura factura) {
        Connection conn = null;
        PreparedStatement psFactura = null;

        String sqlFactura = "INSERT INTO facturas (fecha_generacion, id_cliente, id_forma_pago, total, observaciones) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING numero_factura";

        try {
            conn = Conexion.getconexion();
            conn.setAutoCommit(false); // INICIA LA TRANSACCIÓN

            // 1. Inserción de la Factura
            psFactura = conn.prepareStatement(sqlFactura);

            Date fechaSQL = Date.valueOf(factura.getFechaGeneracion()); // Convertir LocalDate a java.sql.Date

            psFactura.setDate(1, fechaSQL);
            psFactura.setInt(2, factura.getCliente().getIdCliente());
            psFactura.setInt(3, factura.getFormaPago().getId_forma_pago());
            psFactura.setDouble(4, factura.calcularTotal());
            // Nota: Insertamos el total calculado en Java. El trigger de la DB lo validará/recalculará.
            psFactura.setString(5, factura.getObservaciones());

            ResultSet rs = psFactura.executeQuery();
            int numeroFacturaGenerado = 0;
            if (rs.next()) {
                numeroFacturaGenerado = rs.getInt("numero_factura");
                factura.setNumeroFactura(numeroFacturaGenerado);
            }
            Conexion.close(rs);

            if (numeroFacturaGenerado == 0) {
                throw new Exception("Error al obtener el número de factura generado.");
            }

            // 2. Inserción de los Detalles (Llama a DetalleFacturaDAO con la misma conexión)
            detalleFacturaDAO.insertarDetalles(conn, factura.getDetalleFacturas(), numeroFacturaGenerado);

            // 3. Actualizar el Stock de Productos
            for (DetalleFactura detalle : factura.getDetalleFacturas()) {
                Producto producto = detalle.getProducto();
                int cantidadVendida = detalle.getCantidad();

                producto.disminuirStock(cantidadVendida);// Lógica de negocio: Disminuir stock en el objeto

                // Persistencia: Actualizar el stock en la DB
                boolean stockActualizado = productoDAO.modificarStock(conn, producto.getIdProducto(), producto.getStock());
                if (!stockActualizado) {
                    throw new Exception("Error al descontar stock del producto ID: " + producto.getIdProducto());
                }
            }

            conn.commit(); // CONFIRMA LA TRANSACCIÓN
            return true;

        } catch (Exception e) {
            System.err.println("FATAL - Error al generar la factura. Intentando ROLLBACK. Detalle: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // DESHACE LA TRANSACCIÓN
                }
            } catch (Exception ex) {
                System.err.println("Error durante el ROLLBACK: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Restaurar el modo autocommit
                }
            } catch (Exception ex) {
            }
            Conexion.close(psFactura);
            Conexion.close(conn);
        }
    }


    @Override
    public Factura buscarPorNumero(int numeroFactura) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Factura factura = null;

        String sql = "SELECT * FROM facturas WHERE numero_factura = ?";

        try {
            conn = Conexion.getconexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroFactura);
            rs = ps.executeQuery();

            if (rs.next()) {
                factura = mapearFactura(rs);
                // **CARGA EAGER (Ansiosa):** Cargar los detalles inmediatamente
                List<DetalleFactura> detalles = detalleFacturaDAO.buscarPorNumeroFactura(numeroFactura);
                factura.setDetalleFacturas(detalles);
            }

        } catch (Exception e) {
            System.err.println("Error al buscar factura por número: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conn);
        }
        return factura;
    }

    @Override
    public List<Factura> listar() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Factura> facturas = new ArrayList<>();

        // 1. SQL para seleccionar todas las facturas
        String sql = "SELECT * FROM facturas ORDER BY numero_factura DESC";

        try {
            conn = Conexion.getconexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Factura factura = mapearFactura(rs);
                int numeroFactura = factura.getNumeroFactura();

                // Usamos el DAO de DetalleFactura para obtener todos los ítems de esa factura
                List<DetalleFactura> detalles = detalleFacturaDAO.buscarPorNumeroFactura(numeroFactura);
                factura.setDetalleFacturas(detalles);

                facturas.add(factura);// Agregar la factura completa a la lista
            }

        } catch (Exception e) {
            System.err.println("FATAL ERROR - Fallo completo en listar facturas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conn);
        }
        return facturas;
    }
}