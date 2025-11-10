package ar.edu.unlar.paradigmas3.dao.impl;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.ITipoClienteDAO;
import ar.edu.unlar.paradigmas3.modelo.TipoCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoClienteDAO implements ITipoClienteDAO {

    @Override
    public List<TipoCliente> listar() {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TipoCliente> tipos = new ArrayList<>();
        var sql = "SELECT * FROM tipo_cliente ORDER BY nombre";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                TipoCliente tipo = new TipoCliente();
                tipo.setId_tipo_cliente(rs.getInt("id_tipo_cliente"));
                tipo.setNombre(rs.getString("nombre"));
                tipos.add(tipo);
            }
        } catch (Exception e) {
            System.err.println("Error al listar tipos de cliente: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return tipos;
    }

    @Override
    public TipoCliente buscarPorId(int id) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoCliente tipo = null;
        var sql = "SELECT * FROM tipo_cliente WHERE id_tipo_cliente = ?";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                tipo = new TipoCliente();
                tipo.setId_tipo_cliente(rs.getInt("id_tipo_cliente"));
                tipo.setNombre(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar tipo de cliente por id: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return tipo;
    }

    @Override
    public TipoCliente buscarPorNombre(String nombre) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoCliente tipo = null;
        var sql = "SELECT * FROM tipo_cliente WHERE nombre = ?";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                tipo = new TipoCliente();
                tipo.setId_tipo_cliente(rs.getInt("id_tipo_cliente"));
                tipo.setNombre(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar tipo de cliente por nombre: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return tipo;
    }

    @Override
    public boolean agregar(TipoCliente tipoCliente) {
        Connection conexion = null;
        PreparedStatement ps = null;
        // Usamos RETURNING para obtener el ID generado y actualizar el objeto Java
        var sql = "INSERT INTO tipo_cliente (nombre) VALUES (?) RETURNING id_tipo_cliente";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, tipoCliente.getNombre());

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tipoCliente.setId_tipo_cliente(rs.getInt("id_tipo_cliente"));
            }
            Conexion.close(rs);
            return true;

        } catch (Exception e) {
            System.err.println("Error al agregar tipo de cliente: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }

    @Override
    public boolean modificar(TipoCliente tipoCliente) {
        Connection conexion = null;
        PreparedStatement ps = null;
        String sql = "UPDATE tipo_cliente SET nombre = ? WHERE id_tipo_cliente = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, tipoCliente.getNombre());
            ps.setInt(2, tipoCliente.getId_tipo_cliente());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al modificar tipo de cliente: " + e.getMessage());
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
        String sql = "DELETE FROM tipo_cliente WHERE id_tipo_cliente = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            // NOTA: Si este tipo de cliente está asociado a un Cliente, la DB lanzará un error de FK.
            System.err.println("Error al eliminar tipo de cliente: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }
}
