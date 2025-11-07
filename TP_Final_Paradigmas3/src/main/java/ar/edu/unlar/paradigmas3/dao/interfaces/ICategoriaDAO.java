package ar.edu.unlar.paradigmas3.dao.interfaces;

import ar.edu.unlar.paradigmas3.modelo.Categoria;

import java.util.List;

public interface ICategoriaDAO {
    List<Categoria> listar();
    Categoria buscarPorId(int id);
    boolean agregar(Categoria categoria);
    boolean modificar(Categoria categoria);
    boolean eliminar(int id);
}
