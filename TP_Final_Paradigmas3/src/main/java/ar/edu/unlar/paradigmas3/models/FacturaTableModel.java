package ar.edu.unlar.paradigmas3.models;

import ar.edu.unlar.paradigmas3.modelo.Factura;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FacturaTableModel extends AbstractTableModel {

    private final List<Factura> facturas;
    private final String[] columnas = {"N° FACTURA", "FECHA", "CLIENTE", "FORMA DE PAGO", "TOTAL", "OBSERVACIONES"};
    public FacturaTableModel (List<Factura> facturas){
        this.facturas = facturas;
    }

    @Override
    public int getRowCount() {
        return facturas.size();
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
        Factura factura = facturas.get(rowIndex);
        switch (columnIndex){
            case 0: return factura.getNumeroFactura();
            case 1: return factura.getFechaGeneracion();
            case 2: return factura.getCliente() != null ? factura.getCliente().getNombreCompleto() : "N/A";
            case 3: return factura.getFormaPago() != null ? factura.getFormaPago().getNombre() : "N/A";
            case 4: return String.format("$%.2f", factura.getTotal());
            case 5 : return factura.getObservaciones();
            default: return null;
        }
    }

    public Factura getFactura (int rowIndex){
        return facturas.get(rowIndex);
    }

}
