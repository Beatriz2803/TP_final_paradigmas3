package ar.edu.unlar.paradigmas3.dao.interfaces;

import ar.edu.unlar.paradigmas3.modelo.Producto;

import java.util.List;

public interface IProductoDAO {
    List<Producto> listar();
    Producto buscarPorId(int id);
    boolean agregar(Producto producto);
    boolean modificar(Producto producto);
    boolean eliminar(int id);
    List<Producto> buscarPorNombreCategoria(String nombreCategoria);
    List<Producto> buscarPorNombre(String nombre);
}
