package ar.edu.unlar.paradigmas3.gui;

import ar.edu.unlar.paradigmas3.dao.impl.ClienteDAO;
import ar.edu.unlar.paradigmas3.dao.impl.TipoClienteDAO;
import ar.edu.unlar.paradigmas3.modelo.Cliente;
import ar.edu.unlar.paradigmas3.modelo.TipoCliente;
import ar.edu.unlar.paradigmas3.models.ClienteTableModel;
import ar.edu.unlar.paradigmas3.models.TipoClienteComboBoxModel;
import ar.edu.unlar.paradigmas3.utilidades.Validaciones;

import java.awt.Container;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.GroupLayout;


public class FormCliente extends javax.swing.JPanel {

     // Dependencias y Estado
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final TipoClienteDAO tipoClienteDAO = new TipoClienteDAO();
    private Cliente clienteSeleccionado = null;
    private List<TipoCliente> listaTipos;

    private final JTextField txtNombreCompleto;
    private final JTextField txtDni;
    private final JTextField txtDomicilio;
    private final JTextField txtTelefono;

    private final JComboBox<TipoCliente> cmbTipoCliente = new JComboBox<>(); 
    private final JTextField txtBuscarId;
    private final javax.swing.JTable tblClientes;
    private final javax.swing.JButton btnAgregar;
    private final javax.swing.JButton btnModificar;
    private final javax.swing.JButton btnEliminar;
    
    public FormCliente() {
        initComponents();

        // ASIGNACIÓN DE VARIABLES INTERNAS
        this.txtNombreCompleto = jTextField1;
        this.txtDni = jTextField3;
        this.txtDomicilio = jTextField4;
        this.txtTelefono = jTextField5;
        this.txtBuscarId = jTextField2;
        this.tblClientes = jTable1;
        this.btnAgregar = jButtonAgregar;
        this.btnModificar = jButtonModificar;
        this.btnEliminar = jButtonEliminar;


        reemplazarTextFieldPorComboBox(jTextField6, cmbTipoCliente);

        cargarTiposDeCliente();
        cargarTabla();

        tblClientes.getSelectionModel().addListSelectionListener(this::tblClientesSelectionChanged);
        
        // Enlaces manuales (para evitar advertencias de unused)
        btnAgregar.addActionListener(this::jButtonAgregarActionPerformed);
        btnModificar.addActionListener(this::jButtonModificarActionPerformed);
        btnEliminar.addActionListener(this::jButtonEliminarActionPerformed);
        txtBuscarId.addActionListener(this::jTextField2ActionPerformed); 
    }

private void reemplazarTextFieldPorComboBox(JTextField oldField, JComboBox<TipoCliente> newCombo) {
    
    // Convertir el padre (parent) a un Container para poder usar métodos como add/remove/getLayout
    Container parent = oldField.getParent();
    
    if (parent != null) {
        
        // Obtiene las dimensiones exactas del campo viejo (oldField)
        Rectangle bounds = oldField.getBounds();
        
        // Remueve el campo viejo
        parent.remove(oldField);
        
        //  Añade el nuevo JComboBox
        parent.add(newCombo);
        
        // Aplica las mismas restricciones de tamaño/posición
        newCombo.setBounds(bounds);
        
        // actualiza layout
        if (parent.getLayout() instanceof GroupLayout) {
            parent.revalidate();
            parent.repaint();
        }
    }
}
    
    // --- Lógica de ComboBox ---
    private void cargarTiposDeCliente() {
        try {
            listaTipos = tipoClienteDAO.listar();
            TipoClienteComboBoxModel modelo = new TipoClienteComboBoxModel(listaTipos);
            cmbTipoCliente.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de cliente: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        List<Cliente> listaClientes = clienteDAO.listar();
        tblClientes.setModel(new ClienteTableModel(listaClientes));
        limpiarCampos();
    }
    
    private void tblClientesSelectionChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && tblClientes.getSelectedRow() != -1) {
            int fila = tblClientes.getSelectedRow();
            ClienteTableModel modelo = (ClienteTableModel) tblClientes.getModel();
            
            clienteSeleccionado = modelo.getCliente(fila);
            

            txtNombreCompleto.setText(clienteSeleccionado.getNombreCompleto());
            txtDni.setText(clienteSeleccionado.getDni());
            txtDomicilio.setText(clienteSeleccionado.getDomicilio());
            txtTelefono.setText(clienteSeleccionado.getTelefono());
            
            // Selecciona el TipoCliente en el ComboBox
            if (clienteSeleccionado.getTipoCliente() != null) {
                 cmbTipoCliente.setSelectedItem(clienteSeleccionado.getTipoCliente());
            }

            btnAgregar.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }
    
    private void limpiarCampos() {
        txtNombreCompleto.setText("");
        txtDni.setText("");
        txtDomicilio.setText("");
        txtTelefono.setText("");
        txtBuscarId.setText("");
        cmbTipoCliente.setSelectedIndex(0);
        clienteSeleccionado = null;
        tblClientes.clearSelection();
        
        btnAgregar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);

        
    }

    private boolean validarCamposCliente() {
        if (!Validaciones.validarTextoMinimo(txtNombreCompleto, "Nombre Completo")) return false;
        if (!Validaciones.validarTextoMinimo(txtDomicilio, "Domicilio")) return false;
        if (!Validaciones.validarTextoMinimo(txtTelefono, "Teléfono")) return false;

        if (!Validaciones.validarNumeroMayorCero(txtDni, "DNI")) return false;
        
        if (cmbTipoCliente.getSelectedItem() == null) {
             JOptionPane.showMessageDialog(this, "Debe seleccionar un Tipo de Cliente.", "Validación", JOptionPane.ERROR_MESSAGE);
             return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButtonAgregar = new javax.swing.JButton();
        jButtonModificar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Ingrese nombre completo");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Ingrese DNI");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Ingrese domicilio");

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Ingrese telefono");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Ingrese tipo de cliente ");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cliente");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setShowGrid(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Buscar:");

        jTextField2.setText("Ingrese ID");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButtonAgregar.setBackground(new java.awt.Color(51, 102, 0));
        jButtonAgregar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jButtonAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        jButtonModificar.setBackground(new java.awt.Color(51, 102, 0));
        jButtonModificar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar.setText("Modificar");
        jButtonModificar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jButtonModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jButtonEliminar.setBackground(new java.awt.Color(51, 102, 0));
        jButtonEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jButtonEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 109, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(228, 228, 228))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
         // Lógica de Búsqueda por ID
        if (!Validaciones.validarNumeroMayorCero(txtBuscarId, "ID de Búsqueda")) return;
        
        try {
            int idBuscado = Integer.parseInt(txtBuscarId.getText().trim());
            Cliente cliente = clienteDAO.buscarPorId(idBuscado);
            
            if (cliente != null) {
                tblClientes.setModel(new ClienteTableModel(List.of(cliente)));

                txtNombreCompleto.setText(cliente.getNombreCompleto());
                txtDni.setText(cliente.getDni());
                txtDomicilio.setText(cliente.getDomicilio());
                txtTelefono.setText(cliente.getTelefono());
                if (cliente.getTipoCliente() != null) cmbTipoCliente.setSelectedItem(cliente.getTipoCliente());
                
                clienteSeleccionado = cliente;
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el cliente con ID: " + idBuscado, "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
                cargarTabla();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
               if (!validarCamposCliente()) return;
        
        TipoCliente tipoSeleccionado = (TipoCliente) cmbTipoCliente.getSelectedItem();
        
        Cliente nuevoCliente = new Cliente(
            txtNombreCompleto.getText().trim(),
            txtDni.getText().trim(),
            txtDomicilio.getText().trim(),
            txtTelefono.getText().trim(),
            tipoSeleccionado
        );
        
        if (clienteDAO.agregar(nuevoCliente)) {
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Cliente agregado con éxito.");
            
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo agregar. (DNI duplicado o error de BD)", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
//                if (clienteSeleccionado == null) {
//            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        if (!validarCamposCliente()) return;

        // Aplicamos cambios al objeto seleccionado
        clienteSeleccionado.setNombreCompleto(txtNombreCompleto.getText().trim());
        clienteSeleccionado.setDni(txtDni.getText().trim());
        clienteSeleccionado.setDomicilio(txtDomicilio.getText().trim());
        clienteSeleccionado.setTelefono(txtTelefono.getText().trim());
        clienteSeleccionado.setTipoCliente((TipoCliente) cmbTipoCliente.getSelectedItem());
        
        if (clienteDAO.modificar(clienteSeleccionado)) {
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Cliente modificado con éxito.");

        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo modificar el cliente.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
//                if (clienteSeleccionado == null) {
//            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar a " + clienteSeleccionado.getNombreCompleto() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (clienteDAO.eliminar(clienteSeleccionado.getIdCliente())) {
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Cliente eliminado con éxito.");
                
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar. El cliente tiene facturas asociadas.", "Error de Integridad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
