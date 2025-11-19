package ar.edu.unlar.paradigmas3.modeloTablas;

import ar.edu.unlar.paradigmas3.modelo.Producto;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class ProductoComboBoxModel extends AbstractListModel<Producto> implements ComboBoxModel<Producto> {
    private final List<Producto> productos;
    private Producto selectedItem;

    public ProductoComboBoxModel(List<Producto> productos) {
        this.productos = productos;
        if (!productos.isEmpty()) {
            selectedItem = productos.get(0);
        }
    }

    @Override public int getSize() { return productos.size(); }
    @Override public Producto getElementAt(int index) { return productos.get(index); }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof Producto) {
            selectedItem = (Producto) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override public Producto getSelectedItem() { return selectedItem; }
}