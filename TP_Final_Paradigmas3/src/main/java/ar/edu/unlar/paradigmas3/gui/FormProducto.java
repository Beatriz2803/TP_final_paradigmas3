package ar.edu.unlar.paradigmas3.gui;

import ar.edu.unlar.paradigmas3.dao.impl.CategoriaDAO;
import ar.edu.unlar.paradigmas3.dao.impl.ProductoDAO;
import ar.edu.unlar.paradigmas3.modelo.Categoria;
import ar.edu.unlar.paradigmas3.modelo.Producto;
import ar.edu.unlar.paradigmas3.models.CategoriaComboBoxModel;
import ar.edu.unlar.paradigmas3.models.ProductoTableModel;
import ar.edu.unlar.paradigmas3.utilidades.Validaciones; // Asumo que cambiaste el nombre a ValidadorUtil

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

public class FormProducto extends javax.swing.JPanel {

        // Dependencias y Estado
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private Producto productoSeleccionado = null;
    

    private List<Categoria> listaCategorias; 
    
    // Mapeo de Componentes del Diseñador
    private final JTextField txtNombre;
    private final JTextField txtDescripcion;
    private final JTextField txtPrecio;
    private final JTextField txtStock;
    private final JComboBox<Categoria> cmbCategoria; // Usaremos la variable cmbCategoria para el JComboBox
    private final JTextField txtBuscarId;
    private final javax.swing.JTable tblProductos;
    private final javax.swing.JButton btnAgregar;
    private final javax.swing.JButton btnModificar;
    private final javax.swing.JButton btnEliminar;

    public FormProducto() {
        initComponents();
    // ASIGNACIÓN DE VARIABLES INTERNAS
        this.txtNombre = jtfNombre;
        this.txtDescripcion = jtfDescripcion;
        this.txtPrecio = jtfPrecio;
        this.txtStock = jtfStock;
        this.txtBuscarId = jtfBuscarId;
        this.tblProductos = jTable1;
        this.btnAgregar = jButtonAgregar;
        this.btnModificar = jButtonModificar;
        this.btnEliminar = jButtonEliminar;
        
        // Solución al error de tipos: Forzar el cast del JComboBox
        this.cmbCategoria = (JComboBox<Categoria>) (JComboBox<?>) jcbCategoria; 
        
        cargarCategorias();
        cargarTabla();
        

        tblProductos.getSelectionModel().addListSelectionListener(this::tblProductosSelectionChanged);
        
        // Enlaces manuales (para evitar advertencias de unused)
        btnAgregar.addActionListener(this::jButtonAgregarActionPerformed);
        btnModificar.addActionListener(this::jButtonModificarActionPerformed);
        btnEliminar.addActionListener(this::jButtonEliminarActionPerformed);
        txtBuscarId.addActionListener(this::jtfBuscarIdActionPerformed); 
    }
    

    private void cargarCategorias() {
        try {
            listaCategorias = categoriaDAO.listar();
            CategoriaComboBoxModel modelo = new CategoriaComboBoxModel(listaCategorias);
            cmbCategoria.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        List<Producto> listaProductos = productoDAO.listar();
        tblProductos.setModel(new ProductoTableModel(listaProductos));
        limpiarCampos();
    }
    
    private void tblProductosSelectionChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && tblProductos.getSelectedRow() != -1) {
            int fila = tblProductos.getSelectedRow();
            ProductoTableModel modelo = (ProductoTableModel) tblProductos.getModel();
            
            productoSeleccionado = modelo.getProducto(fila);
            
            txtNombre.setText(productoSeleccionado.getNombre());
            txtDescripcion.setText(productoSeleccionado.getDescripcion());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
            txtStock.setText(String.valueOf(productoSeleccionado.getStock()));
            

            if (productoSeleccionado.getCategoria() != null) {
                 cmbCategoria.setSelectedItem(productoSeleccionado.getCategoria());
            }

            btnAgregar.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtBuscarId.setText("");
        if (cmbCategoria.getItemCount() > 0) {
             cmbCategoria.setSelectedIndex(0); 
        }
        productoSeleccionado = null; 
        
        btnAgregar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        tblProductos.clearSelection();
    }



    private boolean validarCamposProducto() {
        if (!Validaciones.validarTextoMinimo(txtNombre, "Nombre")) return false;
        if (!Validaciones.validarTextoMinimo(txtDescripcion, "Descripción")) return false;

        if (!Validaciones.validarNumeroMayorCero(txtPrecio, "Precio")) return false;
        if (!Validaciones.validarNumeroMayorCero(txtStock, "Stock")) return false; 
        
        if (cmbCategoria.getSelectedItem() == null) {
             JOptionPane.showMessageDialog(this, "Debe seleccionar una Categoría.", "Validación", JOptionPane.ERROR_MESSAGE);
             return false;
        }
        return true;
    }


    
    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if (!validarCamposProducto()) return;
        
        Categoria categoriaSeleccionada = (Categoria) cmbCategoria.getSelectedItem();
        
        Producto nuevoProducto = new Producto(
            txtNombre.getText().trim(),
            txtDescripcion.getText().trim(),
            Double.parseDouble(txtPrecio.getText().trim()),
            Integer.parseInt(txtStock.getText().trim()),
            categoriaSeleccionada
        );
        
        if (productoDAO.agregar(nuevoProducto)) {
            JOptionPane.showMessageDialog(this, "Producto agregado con éxito.");
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo agregar el producto.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }                                            

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {                                               
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCamposProducto()) return;

        productoSeleccionado.setNombre(txtNombre.getText().trim());
        productoSeleccionado.setDescripcion(txtDescripcion.getText().trim());
        productoSeleccionado.setPrecioUnitario(Double.parseDouble(txtPrecio.getText().trim()));
        productoSeleccionado.setStock(Integer.parseInt(txtStock.getText().trim()));
        productoSeleccionado.setCategoria((Categoria) cmbCategoria.getSelectedItem());
        
        if (productoDAO.modificar(productoSeleccionado)) {
            JOptionPane.showMessageDialog(this, "Producto modificado con éxito.");
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo modificar el producto.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }                                              

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar a " + productoSeleccionado.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(productoSeleccionado.getIdProducto())) {
                JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar. El producto está en facturas.", "Error de Integridad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtfNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfPrecio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtfStock = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtfDescripcion = new javax.swing.JTextField();
        jcbCategoria = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jtfBuscarId = new javax.swing.JTextField();
        jButtonModificar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonAgregar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(985, 460));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Ingrese nombre");

        jtfNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNombreActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Ingrese precio");

        jtfPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfPrecioActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Ingrese stock");

        jtfStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfStockActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Ingrese categoria");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Ingrese descripcion");

        jtfDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescripcionActionPerformed(evt);
            }
        });

        jcbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCategoriaActionPerformed(evt);
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
                    .addComponent(jLabel4)
                    .addComponent(jtfPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(jtfStock)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jtfDescripcion)
                    .addComponent(jtfNombre)
                    .addComponent(jcbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jtfDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfStock, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(160, 160, 160))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Producto");

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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Buscar:");

        jtfBuscarId.setText("Ingrese ID");
        jtfBuscarId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfBuscarIdActionPerformed(evt);
            }
        });

        jButtonModificar.setBackground(new java.awt.Color(51, 102, 0));
        jButtonModificar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonModificar.setText("Modificar");
        jButtonModificar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jButtonModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButtonEliminar.setBackground(new java.awt.Color(51, 102, 0));
        jButtonEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jButtonEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButtonAgregar.setBackground(new java.awt.Color(51, 102, 0));
        jButtonAgregar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jButtonAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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
                        .addGap(0, 101, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jtfBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(228, 228, 228))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(89, 89, 89)
                                .addComponent(jButtonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(129, 129, 129))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 533, Short.MAX_VALUE)))
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

    private void jtfBuscarIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfBuscarIdActionPerformed
        if (!Validaciones.validarNumeroMayorCero(txtBuscarId, "ID de Búsqueda")) return;
        
        try {
            int idBuscado = Integer.parseInt(txtBuscarId.getText().trim());
            Producto producto = productoDAO.buscarPorId(idBuscado);
            
            if (producto != null) {
                tblProductos.setModel(new ProductoTableModel(List.of(producto)));
                

                txtNombre.setText(producto.getNombre());
                txtDescripcion.setText(producto.getDescripcion());
                txtPrecio.setText(String.valueOf(producto.getPrecioUnitario()));
                txtStock.setText(String.valueOf(producto.getStock()));
                if (producto.getCategoria() != null) cmbCategoria.setSelectedItem(producto.getCategoria());
                
                productoSeleccionado = producto;
                
                // Habilita Modificar/Eliminar
                btnAgregar.setEnabled(false);
                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
                
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto con ID: " + idBuscado, "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
                cargarTabla();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jtfBuscarIdActionPerformed

    private void jtfDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfDescripcionActionPerformed

    private void jtfNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNombreActionPerformed

    private void jtfPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfPrecioActionPerformed

    private void jtfStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfStockActionPerformed

    private void jcbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbCategoriaActionPerformed


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
    private javax.swing.JComboBox<String> jcbCategoria;
    private javax.swing.JTextField jtfBuscarId;
    private javax.swing.JTextField jtfDescripcion;
    private javax.swing.JTextField jtfNombre;
    private javax.swing.JTextField jtfPrecio;
    private javax.swing.JTextField jtfStock;
    // End of variables declaration//GEN-END:variables
}
