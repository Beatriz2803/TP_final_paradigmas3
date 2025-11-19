
package ar.edu.unlar.paradigmas3.gui;
import ar.edu.unlar.paradigmas3.dao.impl.FacturaDAO;
import ar.edu.unlar.paradigmas3.modelo.Factura;
import ar.edu.unlar.paradigmas3.modeloTablas.FacturaTableModel;
import ar.edu.unlar.paradigmas3.utilidades.Validaciones;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class FormVerFacturas extends javax.swing.JPanel {

    // 1. Dependencias y Estado
    private final FacturaDAO facturaDAO = new FacturaDAO();

    // Mapeo de Componentes del Diseñador
    private final javax.swing.JTable tblFacturas; // jTable1
    private final javax.swing.JTextField txtBuscarId; // jtfBuscarId
    public FormVerFacturas() {
        initComponents();
        // Asignación de variables internas
        this.tblFacturas = jTable1;
        this.txtBuscarId = jtfBuscarId;

        cargarTablaCompleta(); // Cargar datos al iniciar

        // Enlace del evento de búsqueda (Enter en el campo de texto)
        jtfBuscarId.addActionListener(this::jtfBuscarIdActionPerformed);
    }

    // --- Lógica de JTable ---

    /**
     * Carga el JTable con todas las facturas.
     */
    private void cargarTablaCompleta() {
        try {
            // FacturaDAO.listar() carga la cabecera y los detalles
            List<Factura> listaFacturas = facturaDAO.listar();
            tblFacturas.setModel(new FacturaTableModel(listaFacturas));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la lista de facturas: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
        txtBuscarId.setText("");
    }

    /**
     * Carga el JTable con una lista específica de facturas (usado para la búsqueda).
     */
    private void cargarTabla(List<Factura> lista) {
        tblFacturas.setModel(new FacturaTableModel(lista));
    }


    // --- Búsqueda por ID ---

    private void jtfBuscarIdActionPerformed(java.awt.event.ActionEvent evt) {
        // 1. VALIDACIÓN
        if (!Validaciones.validarNumeroMayorCero(txtBuscarId, "ID de Búsqueda")) {
            return;
        }

        try {
            int idBuscado = Integer.parseInt(txtBuscarId.getText().trim());

            // 2. LLAMADA AL DAO: Usamos buscarPorNumero que trae la factura completa
            Factura factura = facturaDAO.buscarPorNumero(idBuscado);

            if (factura != null) {
                // SI SE ENCUENTRA: Mostrar solo esa factura
                List<Factura> resultado = new ArrayList<>();
                resultado.add(factura);
                cargarTabla(resultado);

                JOptionPane.showMessageDialog(this, "Factura Nº " + idBuscado + " encontrada.");

            } else {
                // SI NO SE ENCUENTRA: Informar y recargar la tabla completa
                JOptionPane.showMessageDialog(this, "No se encontró la factura con el Nº: " + idBuscado, "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
                cargarTablaCompleta();
            }

        } catch (NumberFormatException e) {
            // cubierto por el validador
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jtfBuscarId = new javax.swing.JTextField();

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FACTURAS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addContainerGap(27, Short.MAX_VALUE))
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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Buscar por id:");

        jtfBuscarId.setText("Ingrese ID");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jtfBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtfBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jtfBuscarId;
    // End of variables declaration//GEN-END:variables
}
