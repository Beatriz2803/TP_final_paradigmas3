package ar.edu.unlar.paradigmas3.dao.interfaces;

import ar.edu.unlar.paradigmas3.modelo.TipoCliente;

import java.util.List;

public interface ITipoClienteDAO {
    List<TipoCliente> listar();
    TipoCliente buscarPorId(int id);
    boolean agregar(TipoCliente tipoCliente);
    boolean modificar(TipoCliente tipoCliente);
    boolean eliminar(int id);
    TipoCliente buscarPorNombre(String nombre);
}
