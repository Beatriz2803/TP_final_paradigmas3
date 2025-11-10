package ar.edu.unlar.paradigmas3.dao.impl;


import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.dao.interfaces.IFormaPagoDAO;
import ar.edu.unlar.paradigmas3.modelo.FormaPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FormaPagoDAO implements IFormaPagoDAO {

    @Override
    public List<FormaPago> listar() {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<FormaPago> formas = new ArrayList<>();

        var sql = "select * from forma_pago order by nombre";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                FormaPago forma = new FormaPago();
                forma.setId_forma_pago(rs.getInt("Id_forma_pago"));
                forma.setNombre(rs.getNString("Nombre"));
                formas.add(forma);
            }
        } catch (Exception e) {
            System.err.println("Error al listar formas de pago: "+e.getMessage());
        }finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return formas;
    }

    @Override
    public FormaPago buscarPorId(int id) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FormaPago forma = null;
        var sql =  "select * from forma_pago where id_forma_pago = ?";

        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if(rs.next()){
                forma=new FormaPago();
                forma.setId_forma_pago(rs.getInt("Id_forma_pago"));
                forma.setNombre(rs.getString("Nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar forma de pago por id: "+e.getMessage());
        }finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conexion);
        }
        return forma;
    }

    @Override
    public boolean agregar(FormaPago formaPago) {
        Connection conexion = null;
        PreparedStatement ps = null;
        var sql = "insert into forma_pago (nombre) values (?) returning id_forma_pago";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, formaPago.getNombre());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                formaPago.setId_forma_pago(rs.getInt("id_forma_pago"));
            }Conexion.close(rs);
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar formas de pago: "+e.getMessage());
            return false;
        }finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }

    @Override
    public boolean modificar(FormaPago formapago) {
        Connection conexion = null;
        PreparedStatement ps = null;
        var sql = "update forma_pago set nombre = ? where id_forma_pago = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, formapago.getNombre());
            ps.setInt(2,formapago.getId_forma_pago());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al modificar formas de pago: "+e.getMessage());
            return false;
        }finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }

    @Override
    public boolean eliminar(int id) {
        Connection conexion = null;
        PreparedStatement ps = null;
        var sql = "delete from forma_pago where id_forma_pago = ?";
        try {
            conexion = Conexion.getconexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar formas de pago: "+e.getMessage());
            return false;
        }finally {
            Conexion.close(ps);
            Conexion.close(conexion);
        }
    }
}
