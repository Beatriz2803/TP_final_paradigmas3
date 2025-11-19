package ar.edu.unlar.paradigmas3.models;

import ar.edu.unlar.paradigmas3.modelo.Producto;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ProductoTableModel extends AbstractTableModel {

    private final List<Producto> productos;
    private final String[] columnas = {"ID", "NOMBRE", "DESCRIPCIÓN", "PRECIO", "STOCK", "CATEGORÍA"};

    public ProductoTableModel(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public int getRowCount() {
        return productos.size();
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
        Producto producto = productos.get(rowIndex);

        switch (columnIndex) {
            case 0: return producto.getIdProducto();
            case 1: return producto.getNombre();
            case 2: return producto.getDescripcion();
            case 3: return producto.getPrecioUnitario();
            case 4: return producto.getStock();
            case 5:
                //  PUNTO CLAVE: Mostrar el nombre de la Categoría
                return producto.getCategoria() != null ? producto.getCategoria().getNombre() : "N/A";
            default: return null;
        }
    }

    public Producto getProducto(int rowIndex) {
        return productos.get(rowIndex);
    }
}