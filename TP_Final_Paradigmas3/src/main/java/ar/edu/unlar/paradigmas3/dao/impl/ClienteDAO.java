package ar.edu.unlar.paradigmas3.dao.impl;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.IClienteDAO;
import ar.edu.unlar.paradigmas3.dao.interfaces.ITipoClienteDAO;
import ar.edu.unlar.paradigmas3.modelo.Cliente;
import ar.edu.unlar.paradigmas3.modelo.TipoCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {
    //Creamos tipo cliente (asociacion) y mapear cliente para construir el objeto cliente.
    private final ITipoClienteDAO tipoClienteDAO = new TipoClienteDAO();
    private Cliente mapearCliente(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombreCompleto(rs.getString("nombre_completo"));
        cliente.setDni(rs.getString("dni"));
        cliente.setDomicilio(rs.getString("domicilio"));
        cliente.setTelefono(rs.getString("telefono"));

        // PUNTO CLAVE: Reconstruir la asociación TipoCliente
        int idTipo = rs.getInt("id_tipo_cliente");
        TipoCliente tipo = tipoClienteDAO.buscarPorId(idTipo);
        cliente.setTipoCliente(tipo);

        return cliente;
    }

    @Override
    public List<Cliente> listar() {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        var sql = "SELECT id_cliente, nombre_completo, dni, domicilio, telefono, id_tipo_cliente FROM clientes ORDER BY id_cliente";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return clientes;
    }

    @Override
    public Cliente buscarPorId(int id) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente cliente = null;

        var sql = "SELECT id_cliente, nombre_completo, dni, domicilio, telefono, id_tipo_cliente FROM clientes WHERE id_cliente = ?";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                cliente = mapearCliente(rs);
            }

        } catch (Exception e) {
            System.err.println("Error al buscar cliente por id: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return cliente;
    }

    @Override
    public boolean agregar(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement ps = null;

        var sql = "INSERT INTO clientes (nombre_completo, dni, domicilio, telefono, id_tipo_cliente) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id_cliente";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);

            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getTelefono());

            ps.setInt(5, cliente.getTipoCliente().getId_tipo_cliente());

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cliente.setIdCliente(rs.getInt("id_cliente"));
            }
            Conexion.close(rs);
            return true;

        } catch (Exception e) {
            System.err.println("Error al agregar cliente: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }

    @Override
    public boolean modificar(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement ps = null;
        String sql = "UPDATE clientes SET " +
                "nombre_completo=?, " +
                "dni=?, " +
                "domicilio=?, " +
                "telefono=?, " +
                "id_tipo_cliente=? " + // Se incluye el campo para modificación
                "WHERE id_cliente =?";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getTelefono());

            ps.setInt(5, cliente.getTipoCliente().getId_tipo_cliente());

            ps.setInt(6, cliente.getIdCliente()); // ID de la cláusula WHERE

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al modificar un cliente: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }

    @Override
    public boolean eliminar(int id) {
        Connection conexion = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar un cliente: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }
}