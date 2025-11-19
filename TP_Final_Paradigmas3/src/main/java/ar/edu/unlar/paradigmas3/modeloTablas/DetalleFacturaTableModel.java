package ar.edu.unlar.paradigmas3.modeloTablas;

import ar.edu.unlar.paradigmas3.modelo.DetalleFactura;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DetalleFacturaTableModel extends AbstractTableModel {

    private final List<DetalleFactura> detalles;
    private final String[] columnas = {"PRODUCTO", "CANTIDAD", "P. UNITARIO", "SUBTOTAL"};

    public DetalleFacturaTableModel(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    @Override public int getRowCount() { return detalles.size(); }
    @Override public int getColumnCount() { return columnas.length; }
    @Override public String getColumnName(int column) { return columnas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetalleFactura detalle = detalles.get(rowIndex);

        switch (columnIndex) {
            case 0: return detalle.getProducto().getNombre();
            case 1: return detalle.getCantidad();
            case 2: return detalle.getPrecioUnitario();
            case 3: return detalle.getSubtotal();
            default: return null;
        }
    }

    // El método que permite al JTable saber si el stock está disponible
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 1 || columnIndex == 3) ? Integer.class : String.class;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }
}