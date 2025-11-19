package ar.edu.unlar.paradigmas3.models;

import ar.edu.unlar.paradigmas3.modelo.FormaPago;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class FormaPagoComboBoxModel extends AbstractListModel<FormaPago> implements ComboBoxModel<FormaPago> {
    private final List<FormaPago> formas;
    private FormaPago selectedItem;

    public FormaPagoComboBoxModel(List<FormaPago> formas) {
        this.formas = formas;
        if (!formas.isEmpty()) {
            selectedItem = formas.get(0);
        }
    }

    @Override public int getSize() { return formas.size(); }
    @Override public FormaPago getElementAt(int index) { return formas.get(index); }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof FormaPago) {
            selectedItem = (FormaPago) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override public FormaPago getSelectedItem() { return selectedItem; }
}