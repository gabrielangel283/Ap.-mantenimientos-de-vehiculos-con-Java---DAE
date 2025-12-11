package Capa_Presentacion;

/**
 *
 * @author Lope
 */
import Reportes.clsReportes;
import net.sf.jasperreports.swing.JRViewer;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class frmRP_ListadoVehiculos extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmRP_ListadoVehiculos.class.getName());

    public frmRP_ListadoVehiculos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void mostrarReporteClientesVehiculos(String tipoCliente) throws Exception {
        try {
            clsReportes objR = new clsReportes();
            Map<String, Object> parametros = new HashMap<>();

            parametros.put("Tipo_Cliente", tipoCliente);

            parametros.put("TITULO_TIPO", tipoCliente.toUpperCase());
            parametros.put("REPORT_DIR", "src/Reportes/");


            JRViewer visor = objR.obtenerVisorInterno("ListadoVehiculos", parametros);

            if (visor != null) {
                cont.setLayout(new BorderLayout());
                cont.removeAll();
                cont.add(visor, BorderLayout.CENTER);
                cont.revalidate();
                cont.repaint();
            }

        } catch (Exception ex) {
            throw new Exception("Error al mostrar el reporte de listado: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cont = new javax.swing.JDesktopPane();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbTipoCliente = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout contLayout = new javax.swing.GroupLayout(cont);
        cont.setLayout(contLayout);
        contLayout.setHorizontalGroup(
            contLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1161, Short.MAX_VALUE)
        );
        contLayout.setVerticalGroup(
            contLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Generar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tipo Cliente:");

        cmbTipoCliente.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbTipoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Natural", "Juridico" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cont)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbTipoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(jButton1)
                .addGap(394, 394, 394))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(cmbTipoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cont)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String tipoSeleccionado = cmbTipoCliente.getSelectedItem().toString();

            if (tipoSeleccionado.trim().isEmpty() || tipoSeleccionado.equals("Seleccionar Tipo")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de cliente (Natural o Jurídico).", "AVISO", JOptionPane.WARNING_MESSAGE);
                return;
            }

            mostrarReporteClientesVehiculos(tipoSeleccionado.toLowerCase());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbTipoCliente;
    private javax.swing.JDesktopPane cont;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
