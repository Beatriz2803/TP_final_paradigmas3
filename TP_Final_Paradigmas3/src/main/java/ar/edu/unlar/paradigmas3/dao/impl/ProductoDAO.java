package ar.edu.unlar.paradigmas3.dao.impl;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.IProductoDAO;
import ar.edu.unlar.paradigmas3.modelo.Categoria;
import ar.edu.unlar.paradigmas3.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    // Metodo auxiliar para mapear ResultSet a Objeto Producto
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        // Crear categoría asociada (La buscamos con el JOIN)
        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria_nombre")
        );

        // Crear el producto completo
        Producto producto = new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio_unitario"),
                rs.getInt("stock"),
                categoria
        );
        return producto;
    }

    @Override
    public List<Producto> listar() {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT p.*, c.id_categoria, c.nombre AS categoria_nombre FROM productos p " +
                "JOIN categorias c ON p.id_categoria = c.id_categoria ORDER BY p.nombre";

        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaProductos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return listaProductos;
    }

    @Override
    public Producto buscarPorId(int id) {
        Producto producto = null;
        var sql =
                "SELECT p.id_producto, p.nombre, p.descripcion, p.precio_unitario, p.stock, " +
                        "c.id_categoria, c.nombre AS categoria_nombre " +
                        "FROM productos p " +
                        "INNER JOIN categorias c ON p.id_categoria = c.id_categoria " +
                        "WHERE p.id_producto = ?";

        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                producto = mapearProducto(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
        }
        return producto;
    }

    @Override
    public boolean agregar(Producto producto) {

        // Se agrega RETURNING id_producto para recuperar el ID generado
        var sql = "INSERT INTO productos(nombre, descripcion, precio_unitario, stock, id_categoria)" +
                "VALUES(?, ?, ?, ?, ?) RETURNING id_producto";

        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecioUnitario());
            ps.setInt(4, producto.getStock());
            // Se usa la FK de la Categoría
            ps.setInt(5, producto.getCategoria().getIdCategoria());

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                // Asignamos el ID generado al objeto Java
                producto.setIdProducto(rs.getInt("id_producto"));
            }
            // El ResultSet se cierra automáticamente por try-with-resources
            return true;

        } catch (SQLException e) {
            System.err.println("Error al agregar un producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modificar(Producto producto) {
        String sql = "UPDATE productos SET " +
                "nombre=?, "+
                "descripcion=?, "+
                "precio_unitario=?, " +
                "stock=?, " +
                "id_categoria=? " +
                "WHERE id_producto =?";

        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecioUnitario());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getCategoria().getIdCategoria()); // FK de Categoría
            ps.setInt(6, producto.getIdProducto());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al modificar un producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ? ";
        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar un producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Producto> buscarPorNombreCategoria(String nombreCategoria) {
        List<Producto> productos = new ArrayList<>();

        var sql =
                "SELECT p.id_producto, p.nombre, p.descripcion, p.precio_unitario, p.stock, " +
                        "c.id_categoria, c.nombre AS categoria_nombre " +
                        "FROM productos p " +
                        "INNER JOIN categorias c ON p.id_categoria = c.id_categoria " +
                        "WHERE c.nombre ILIKE ?"; // Usamos ILIKE para búsqueda insensible a mayúsculas/minúsculas

        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreCategoria + "%"); // Búsqueda parcial (contiene)

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
        }

        return productos;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();

        var sql =
                "SELECT p.id_producto, p.nombre, p.descripcion, p.precio_unitario, p.stock, " +
                        "c.id_categoria, c.nombre AS categoria_nombre " +
                        "FROM productos p " +
                        "INNER JOIN categorias c ON p.id_categoria = c.id_categoria " +
                        "WHERE p.nombre ILIKE ?";

        try (Connection conexion = Conexion.getconexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%"); // Búsqueda parcial (contiene)

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        return productos;
    }


//    Para que el FacturaDAO.generar() funcione, debes añadir el siguiente metodo
//    Este metodo es especial porque recibe la conexión de la transacción (conn) y solo actualiza el stock, sin cerrar la conexión.

    public boolean modificarStock(Connection conn, int idProducto, int nuevoStock) throws Exception {
        PreparedStatement ps = null;
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";

        // NO usamos try-with-resources ni obtenemos una nueva conexión, usamos la conexión que nos pasa FacturaDAO.
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            // Relanzamos la excepción para que FacturaDAO pueda hacer el Rollback
            throw e;
        } finally {
            Conexion.close(ps);
        }
    }


}


/**
 Nptita: l objetivo del metodo mapearProducto es doble:
 1. Reconstruir Objetos a partir de Filas (ResultSet)
 2. Manejar Asociaciones (JOINs)
 */
