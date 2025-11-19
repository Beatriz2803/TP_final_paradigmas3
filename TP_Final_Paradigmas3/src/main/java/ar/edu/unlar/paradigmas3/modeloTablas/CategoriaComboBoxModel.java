package ar.edu.unlar.paradigmas3.modeloTablas;

import ar.edu.unlar.paradigmas3.modelo.Categoria;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class CategoriaComboBoxModel extends AbstractListModel<Categoria> implements ComboBoxModel<Categoria> {

    private final List<Categoria> categorias;
    private Categoria selectedItem;

    public CategoriaComboBoxModel(List<Categoria> categorias) {
        this.categorias = categorias;
        if (!categorias.isEmpty()) {
            selectedItem = categorias.get(0);
        }
    }

    @Override
    public int getSize() {
        return categorias.size();
    }

    @Override
    public Categoria getElementAt(int index) {
        return categorias.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof Categoria) {
            selectedItem = (Categoria) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Categoria getSelectedItem() {
        return selectedItem;
    }
}
