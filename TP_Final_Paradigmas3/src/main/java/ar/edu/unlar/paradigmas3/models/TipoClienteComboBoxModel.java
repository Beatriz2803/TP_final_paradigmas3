package ar.edu.unlar.paradigmas3.modelotablas; // O donde guardes tus modelos

import ar.edu.unlar.paradigmas3.modelo.TipoCliente;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class TipoClienteComboBoxModel extends AbstractListModel<TipoCliente> implements ComboBoxModel<TipoCliente> {

    private final List<TipoCliente> tipos;
    private TipoCliente selectedItem;

    public TipoClienteComboBoxModel(List<TipoCliente> tipos) {
        this.tipos = tipos;
        if (!tipos.isEmpty()) {
            selectedItem = tipos.get(0);
        }
    }

    @Override
    public int getSize() {
        return tipos.size();
    }

    @Override
    public TipoCliente getElementAt(int index) {
        return tipos.get(index);
    }

    //Implementación de ComboBoxModel
    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof TipoCliente) {
            selectedItem = (TipoCliente) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public TipoCliente getSelectedItem() {
        return selectedItem;
    }
}
