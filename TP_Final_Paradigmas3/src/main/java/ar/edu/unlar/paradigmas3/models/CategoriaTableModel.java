package ar.edu.unlar.paradigmas3.models;

import ar.edu.unlar.paradigmas3.modelo.Categoria;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CategoriaTableModel extends AbstractTableModel {

    private final List<Categoria> categorias;
    private final String[] columnas = {"ID", "NOMBRE"};

    public CategoriaTableModel(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public int getRowCount() {
        return categorias.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    // Define el nombre de las columnas en el encabezado
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    // Define el valor a mostrar en cada celda
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Categoria categoria = categorias.get(rowIndex);

        switch (columnIndex) {
            case 0: // Columna ID
                return categoria.getIdCategoria();
            case 1: // Columna NOMBRE
                return categoria.getNombre();
            default:
                return null;
        }
    }

    // Metodo auxiliar para obtener el objeto completo de una fila
    public Categoria getCategoria(int rowIndex) {
        return categorias.get(rowIndex);
    }

    // Permite al JTable saber qué tipo de dato hay en cada columna
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Integer.class : String.class;
    }
}
