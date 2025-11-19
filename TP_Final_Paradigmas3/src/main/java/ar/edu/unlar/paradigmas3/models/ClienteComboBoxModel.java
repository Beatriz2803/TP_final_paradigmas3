package ar.edu.unlar.paradigmas3.models;

import ar.edu.unlar.paradigmas3.modelo.Cliente;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class ClienteComboBoxModel extends AbstractListModel<Cliente> implements ComboBoxModel<Cliente> {
    private final List<Cliente> clientes;
    private Cliente selectedItem;

    public ClienteComboBoxModel(List<Cliente> clientes) {
        this.clientes = clientes;
        if (!clientes.isEmpty()) {
            selectedItem = clientes.get(0);
        }
    }

    @Override public int getSize() { return clientes.size(); }
    @Override public Cliente getElementAt(int index) { return clientes.get(index); }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof Cliente) {
            selectedItem = (Cliente) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override public Cliente getSelectedItem() { return selectedItem; }
}

