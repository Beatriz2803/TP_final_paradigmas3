package ar.edu.unlar.paradigmas3.models;

import ar.edu.unlar.paradigmas3.modelo.Cliente;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ClienteTableModel extends AbstractTableModel {

    private final List<Cliente> clientes;
    private final String[] columnas = {"ID", "NOMBRE COMPLETO", "DNI", "DOMICILIO", "TELÉFONO", "TIPO CLIENTE"};

    public ClienteTableModel(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cliente cliente = clientes.get(rowIndex);

        switch (columnIndex) {
            case 0: return cliente.getIdCliente();
            case 1: return cliente.getNombreCompleto();
            case 2: return cliente.getDni();
            case 3: return cliente.getDomicilio();
            case 4: return cliente.getTelefono();
            case 5: 

                return cliente.getTipoCliente() != null ? cliente.getTipoCliente().getNombre() : "N/A";
            default: return null;
        }
    }
    
    public Cliente getCliente(int rowIndex) {
        return clientes.get(rowIndex);
    }
}
