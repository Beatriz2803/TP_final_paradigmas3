package ar.edu.unlar.paradigmas3.dao.interfaces;

import ar.edu.unlar.paradigmas3.modelo.Cliente;

import java.util.List;

public interface IClienteDAO {
    List<Cliente> listar();
    Cliente buscarPorId(int id);
    boolean agregar(Cliente cliente);
    boolean modificar(Cliente cliente);
    boolean eliminar(int id);
}
