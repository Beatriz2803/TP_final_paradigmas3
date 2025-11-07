package ar.edu.unlar.paradigmas3.dao.interfaces;


import ar.edu.unlar.paradigmas3.modelo.FormaPago;

import java.util.List;

public interface IFormaPagoDAO {
    List<FormaPago> listar();
    FormaPago buscarPorId(int id);
    boolean agregar(FormaPago formaPago);
    boolean modificar(FormaPago formapago);
    boolean eliminar(int id);
}
