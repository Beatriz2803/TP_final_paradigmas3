/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ar.edu.unlar.paradigmas3.gui;

import ar.edu.unlar.paradigmas3.dao.impl.CategoriaDAO;
import ar.edu.unlar.paradigmas3.modelo.Categoria;
import ar.edu.unlar.paradigmas3.modeloTablas.CategoriaTableModel;
import ar.edu.unlar.paradigmas3.utilidades.Validaciones;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author hp
 */
public class FormCategoria extends javax.swing.JPanel {

        // 1. Dependencias y Estado
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private Categoria categoriaSeleccionada = null;
    
    // Nombres más descriptivos (actualizados manualmente o en el IDE)
    private final javax.swing.JTextField txtNombreCategoria; // Antes jTextField1
    private final javax.swing.JTable tblCategorias;          // Antes jTable1
    private final javax.swing.JTextField txtBuscarId;        // Antes jTextField2
    
    // Asignamos los nombres que usaste en el diseñador a las variables de la clase
    private final javax.swing.JButton btnAgregar;        // Antes jButtonAgregar
    private final javax.swing.JButton btnModificar;      // Antes jButtonModificar
    private final javax.swing.JButton btnEliminar;       // Antes jButtonEliminar
    
    public FormCategoria() {
        initComponents();
        
        // Asignamos las variables internas a las generadas por el IDE para la lógica
        this.txtNombreCategoria = jTextField1; 
        this.tblCategorias = jTable1;
        this.txtBuscarId = jTextField2;
        this.btnAgregar = jButtonAgregar;
        this.btnModificar = jButtonModificar;
        this.btnEliminar = jButtonEliminar; 
        
        cargarTabla(); // Cargar datos al iniciar
        
        // Agregar listener para manejar la selección de filas
        tblCategorias.getSelectionModel().addListSelectionListener(this::tblCategoriasSelectionChanged);
        
        // --- 3. ENLACE MANUAL DE BOTONES FALTANTES ---
        // El IDE no enlazó todos los botones. Los enlazamos manualmente aquí.
        // Esto soluciona la advertencia "is never used".
        btnAgregar.addActionListener(this::jButtonAgregarActionPerformed);
        btnEliminar.addActionListener(this::jButtonEliminarActionPerformed);
        // El botón Modificar ya estaba enlazado en initComponents, pero lo aseguramos:
        btnModificar.addActionListener(this::jButtonModificarActionPerformed);
        
    }

    
    // --- 2. Lógica de JTable ---
    
    private void cargarTabla() {
        List<Categoria> listaCategorias = categoriaDAO.listar();
        CategoriaTableModel modelo = new CategoriaTableModel(listaCategorias);
        tblCategorias.setModel(modelo);
        
        // Ocultar la columna ID si se desea, ya que es la columna 0
        // tblCategorias.getColumnModel().getColumn(0).setMaxWidth(0);
        // tblCategorias.getColumnModel().getColumn(0).setMinWidth(0);
        // tblCategorias.getColumnModel().getColumn(0).setPreferredWidth(0);

        limpiarCampos();
    }
    
    // Listener para cuando se selecciona una fila
    private void tblCategoriasSelectionChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && tblCategorias.getSelectedRow() != -1) {
            int fila = tblCategorias.getSelectedRow();
            CategoriaTableModel modelo = (CategoriaTableModel) tblCategorias.getModel();
            
            // Obtenemos el objeto Categoria completo de la fila seleccionada
            categoriaSeleccionada = modelo.getCategoria(fila);
            
            // Cargamos el nombre en el campo para posible modificación
            txtNombreCategoria.setText(categoriaSeleccionada.getNombre());
            
            // Deshabilitamos el botón Agregar y habilitamos Modificar/Eliminar
            btnAgregar.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }
    
        // --- 3. Métodos Auxiliares ---

    private void limpiarCampos() {
        txtNombreCategoria.setText("");
        txtBuscarId.setText("");
        categoriaSeleccionada = null; // Volver al modo "Agregar"
        
        // Habilitamos el botón Agregar y deshabilitamos Modificar/Eliminar (estado inicial)
        btnAgregar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        
        txtNombreCategoria.requestFocus();
        tblCategorias.clearSelection();
    }
    

        // --- 4. Eventos de Botones (Lógica CRUD y Validación) ---
    
    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // --- VALIDACIÓN DE LA CONSIGNA (Mínimo 3 caracteres) ---
        if (!Validaciones.validarTextoMinimo(txtNombreCategoria, "Nombre de Categoría")) {
            return; // Detiene la ejecución si la validación falla
        }
        
        // Crear el objeto
        Categoria nuevaCategoria = new Categoria(txtNombreCategoria.getText().trim());
        
        if (categoriaDAO.agregar(nuevaCategoria)) {
            JOptionPane.showMessageDialog(this, "Categoría agregada con éxito.");
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo agregar la categoría. (Verifique si ya existe)", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }                                            


    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar '" + categoriaSeleccionada.getNombre() + "'?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (categoriaDAO.eliminar(categoriaSeleccionada.getIdCategoria())) {
                JOptionPane.showMessageDialog(this, "Categoría eliminada con éxito.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar. Está asociada a uno o más productos.", "Error de Integridad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Nota: El método jTextField2ActionPerformed (Buscar) quedó pendiente, 
    // pero la lógica principal del CRUD y la tabla ya está lista. 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
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
        jLabel2.setText("Ingrese categoria");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Categoria");

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

        jTextField2.setText("Ingrese ID");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButtonAgregar.setText("Agregar");
        jButtonAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButtonModificar.setText("Modificar");
        jButtonModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(228, 228, 228))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(109, Short.MAX_VALUE)
                .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90)
                .addComponent(jButtonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
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
                .addContainerGap(131, Short.MAX_VALUE))
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
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
// 1. VALIDACIÓN: Asegurar que se ingresó un ID numérico válido (> 0)
    if (!Validaciones.validarNumeroMayorCero(txtBuscarId, "ID de Búsqueda")) {
        return;
    }
    
    try {
        int idBuscado = Integer.parseInt(txtBuscarId.getText().trim());
        
        // 2. LLAMADA AL DAO
        Categoria categoria = categoriaDAO.buscarPorId(idBuscado);
        
        if (categoria != null) {
            // 3. SI SE ENCUENTRA: Mostrar el resultado y cargarlo para modificación
            
            // Creamos un nuevo modelo con solo la categoría encontrada
            List<Categoria> resultado = List.of(categoria);
            CategoriaTableModel modelo = new CategoriaTableModel(resultado);
            tblCategorias.setModel(modelo);
            
            // Cargamos los datos en los campos de edición
            categoriaSeleccionada = categoria;
            txtNombreCategoria.setText(categoria.getNombre());
            
            JOptionPane.showMessageDialog(this, "Categoría encontrada.");
            
        } else {
            // 4. SI NO SE ENCUENTRA: Informar y recargar la tabla completa
            JOptionPane.showMessageDialog(this, "No se encontró ninguna categoría con el ID: " + idBuscado, "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
            cargarTabla(); // Vuelve a cargar todos los elementos
        }
    } catch (NumberFormatException e) {
        // Esta excepción ya está cubierta por ValidadorUtil, pero es un seguro
        JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // --- VALIDACIÓN DE LA CONSIGNA ---
        if (!Validaciones.validarTextoMinimo(txtNombreCategoria, "Nombre de Categoría")) {
            return; 
        }

        // Aplicar el cambio al objeto seleccionado
        categoriaSeleccionada.setNombre(txtNombreCategoria.getText().trim());
        
        if (categoriaDAO.modificar(categoriaSeleccionada)) {
            JOptionPane.showMessageDialog(this, "Categoría modificada con éxito.");
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo modificar la categoría.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonModificarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
