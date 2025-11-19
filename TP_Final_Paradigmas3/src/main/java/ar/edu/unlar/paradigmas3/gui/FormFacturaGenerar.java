package ar.edu.unlar.paradigmas3.gui;

import ar.edu.unlar.paradigmas3.dao.impl.*; // Importamos todos los DAOs necesarios
import ar.edu.unlar.paradigmas3.modelo.*;
import ar.edu.unlar.paradigmas3.modeloTablas.*;
import ar.edu.unlar.paradigmas3.utilidades.Validaciones;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FormFacturaGenerar extends javax.swing.JPanel {

    // 1. Dependencias y Estado
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final FormaPagoDAO formaPagoDAO = new FormaPagoDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    
    // ESTADO CLAVE: El "carrito" que contendrá los detalles a guardar
    private final List<DetalleFactura> detallesVenta = new ArrayList<>();
    
    // Mapeo de Componentes del Diseñador (Declaración de variables final)
    private final JComboBox<Producto> cmbProducto;
    private final JTextField txtCantidad;
    private final JComboBox<Cliente> cmbCliente;
    private final JComboBox<FormaPago> cmbFormaPago;
    private final JTextField txtObservaciones;
    private final javax.swing.JTable tblDetalles;
    private final javax.swing.JButton btnAgregarItem;
    private final javax.swing.JButton btnGenerarFactura;
    
    // 2. Variables para mostrar el Total (Lo agregamos manualmente a la UI o lo usamos internamente)
    private double totalFactura = 0.0;
    
    public FormFacturaGenerar() {
        initComponents();
                // --- ASIGNACIÓN DE VARIABLES INTERNAS ---
        this.cmbProducto = (JComboBox<Producto>) (JComboBox<?>) jcbProducto; 
        this.txtCantidad = jtfCantidad;
        this.cmbCliente = (JComboBox<Cliente>) (JComboBox<?>) jcbCliente;
        this.cmbFormaPago = (JComboBox<FormaPago>) (JComboBox<?>) jcbFormaPago;
        this.txtObservaciones = jtfObservaciones;
        this.tblDetalles = jTable1;
        this.btnAgregarItem = bntAgregarItem;
        this.btnGenerarFactura = btn_GenerarFactura;
        
        cargarCombos();
        actualizarDetalleTabla(); // Inicializa la tabla vacía
        
        // Enlaces manuales 
        btnAgregarItem.addActionListener(this::bntAgregarItemActionPerformed);
        btnGenerarFactura.addActionListener(this::btn_GenerarFacturaActionPerformed);
        
        // Listener: Actualizar precio unitario al cambiar el producto
        jcbProducto.addActionListener(this::jcbProductoChanged);
        
    }
    // --- Lógica de ComboBox ---
    private void cargarCombos() {
        try {
            // Cargar Productos
            List<Producto> productos = productoDAO.listar();
            cmbProducto.setModel(new ProductoComboBoxModel(productos));

            // Cargar Clientes
            List<Cliente> clientes = clienteDAO.listar();
            cmbCliente.setModel(new ClienteComboBoxModel(clientes));
            
            // Cargar Formas de Pago
            List<FormaPago> formas = formaPagoDAO.listar();
            cmbFormaPago.setModel(new FormaPagoComboBoxModel(formas));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos iniciales: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- Lógica de JTable Detalle (Carrito) ---
    private void actualizarDetalleTabla() {
        DetalleFacturaTableModel modelo = new DetalleFacturaTableModel(detallesVenta);
        tblDetalles.setModel(modelo);
        calcularTotal();
    }
    
    private void calcularTotal() {
        totalFactura = 0.0;
        for (DetalleFactura detalle : detallesVenta) {
            totalFactura += detalle.calcularSubtotal();
        }
        // Aquí podrías actualizar un Jlabel o JTextField deshabilitado con el total
        // Ejemplo: lblTotal.setText(String.format("TOTAL: $%.2f", totalFactura));
    }
    
// --- 3. Eventos del Detalle (Agregar Ítem) ---
    
    private void jcbProductoChanged(java.awt.event.ActionEvent evt) {
        // Lógica opcional para actualizar un campo de precio unitario en la interfaz
        // No es estrictamente necesario, pero mejora la UX.
    }
    
    private void bntAgregarItemActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // 1. VALIDACIÓN de Cantidad (Numérico > 0)
        if (!Validaciones.validarNumeroMayorCero(txtCantidad, "Cantidad")) {
            return;
        }
        
        // 2. Obtener datos
        Producto productoSeleccionado = (Producto) cmbProducto.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        if (productoSeleccionado == null) {
             JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.", "Validación", JOptionPane.ERROR_MESSAGE);
             return;
        }

        // 3. VALIDACIÓN DE STOCK (Consigna implícita)
        if (!productoSeleccionado.stockDisponible(cantidad)) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + productoSeleccionado.getStock(), "Error de Stock", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Crear el Detalle y añadir al carrito
        DetalleFactura nuevoDetalle = new DetalleFactura(productoSeleccionado, cantidad);
        // Note: DetalleFactura ya calcula el subtotal y fija el precio unitario del producto.
        
        detallesVenta.add(nuevoDetalle);
        
        // 5. Actualizar la tabla y limpiar campos
        actualizarDetalleTabla(); 
        txtCantidad.setText("");
        txtCantidad.requestFocus();
    }                                            

    // --- 4. Evento Principal (Generar Factura - Transacción) ---

    private void btn_GenerarFacturaActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        if (detallesVenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto al detalle.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cmbCliente.getSelectedItem() == null || cmbFormaPago.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar Cliente y Forma de Pago.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Recopilar datos de Cabecera
        Factura nuevaFactura = new Factura();
        nuevaFactura.setFechaGeneracion(LocalDate.now()); // Fecha automática
        nuevaFactura.setCliente((Cliente) cmbCliente.getSelectedItem());
        nuevaFactura.setFormaPago((FormaPago) cmbFormaPago.getSelectedItem());
        nuevaFactura.setObservaciones(txtObservaciones.getText().trim());
        nuevaFactura.setDetalleFacturas(detallesVenta);
        
        // El total se calcula en el objeto Factura.calcularTotal() y se inserta en la DB.
        
        // 2. Ejecutar la Transacción
        if (facturaDAO.generar(nuevaFactura)) {
            JOptionPane.showMessageDialog(this, "FACTURA GENERADA Y GUARDADA CON ÉXITO.\nNº: " + nuevaFactura.getNumeroFactura(), "Transacción Exitosa", JOptionPane.INFORMATION_MESSAGE);
            
            // 3. Resetear el formulario para una nueva factura
            detallesVenta.clear();
            cargarCombos(); // Recarga combos si es necesario, limpia estado
            actualizarDetalleTabla();
            txtObservaciones.setText("");
            
        } else {
            JOptionPane.showMessageDialog(this, "ERROR FATAL: La transacción falló. Los cambios fueron revertidos (Rollback).", "Error de Transacción", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtfObservaciones = new javax.swing.JTextField();
        jcbCliente = new javax.swing.JComboBox<>();
        jcbFormaPago = new javax.swing.JComboBox<>();
        btn_GenerarFactura = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jcbProducto = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jtfCantidad = new javax.swing.JTextField();
        bntAgregarItem = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Seleccione cliente");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Seleccione forma de pago");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Ingrese observaciones");

        jtfObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfObservacionesActionPerformed(evt);
            }
        });

        jcbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbClienteActionPerformed(evt);
            }
        });

        jcbFormaPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbFormaPagoActionPerformed(evt);
            }
        });

        btn_GenerarFactura.setBackground(new java.awt.Color(51, 153, 0));
        btn_GenerarFactura.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        btn_GenerarFactura.setForeground(new java.awt.Color(255, 255, 255));
        btn_GenerarFactura.setText("Generar Factura");
        btn_GenerarFactura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        btn_GenerarFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Datos de la Factura");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Producto");

        jcbProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbProductoActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Cantidad");

        jtfCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCantidadActionPerformed(evt);
            }
        });

        bntAgregarItem.setBackground(new java.awt.Color(51, 102, 0));
        bntAgregarItem.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        bntAgregarItem.setForeground(new java.awt.Color(255, 255, 255));
        bntAgregarItem.setText("Agregar al carrito");
        bntAgregarItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel7)
                        .addComponent(jtfObservaciones)
                        .addComponent(jcbCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jcbFormaPago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_GenerarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel8)
                        .addComponent(jtfCantidad))
                    .addComponent(bntAgregarItem, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bntAgregarItem, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_GenerarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nueva Factura");

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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jtfObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfObservacionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfObservacionesActionPerformed

    private void jcbProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbProductoActionPerformed

    private void jtfCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfCantidadActionPerformed

    private void jcbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbClienteActionPerformed

    private void jcbFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbFormaPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbFormaPagoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntAgregarItem;
    private javax.swing.JButton btn_GenerarFactura;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbCliente;
    private javax.swing.JComboBox<String> jcbFormaPago;
    private javax.swing.JComboBox<String> jcbProducto;
    private javax.swing.JTextField jtfCantidad;
    private javax.swing.JTextField jtfObservaciones;
    // End of variables declaration//GEN-END:variables
}
