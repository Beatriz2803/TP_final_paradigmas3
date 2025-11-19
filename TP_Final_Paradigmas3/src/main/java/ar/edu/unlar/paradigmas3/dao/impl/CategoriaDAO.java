package ar.edu.unlar.paradigmas3.dao.impl;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.ICategoriaDAO;
import ar.edu.unlar.paradigmas3.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements ICategoriaDAO {
    @Override
    public List<Categoria> listar() {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Categoria> categorias = new ArrayList<>();
        var sql = "select * from categorias order by nombre";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                categorias.add(categoria);
            }
        }catch (Exception e){
            System.err.println("error al listar categorias: " + e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return categorias;
    }

    @Override
    public Categoria buscarPorId(int id) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Categoria categoria = null;
        var sql = "Select * from categorias where id_categoria = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error a buscar categoria por id: "+ e.getMessage());
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return categoria;
    }

    @Override
    public boolean agregar(Categoria categoria) {
        Connection conexion = null;
        PreparedStatement ps = null;
        var sql = "Insert into categorias (nombre) values (?) returning id_categoria";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1,categoria.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                categoria.setIdCategoria(rs.getInt("id_categoria"));
            }
            Conexion.close(rs);
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar categoria: "+ e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }

    @Override
    public boolean modificar(Categoria categoria) {
        Connection conexion = null;
        PreparedStatement ps = null;
        var sql = "update categorias set nombre = ? where id_categoria = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1,categoria.getNombre());
            ps.setInt(2, categoria.getIdCategoria());
            int filasAfectadas = ps.executeUpdate(); //obtiene numero de las filas modificadas
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al modificar categoria: "+ e.getMessage());
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
        var sql = "delete from categorias where id_categoria = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,id);
            int filasAfectadas = ps.executeUpdate(); //obtiene numero de las filas modificadas
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar categoria: "+ e.getMessage());
            return false;
        } finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }
}
