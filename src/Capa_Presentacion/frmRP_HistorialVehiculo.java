package Capa_Presentacion;

import Reportes.clsReportes;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.swing.JRViewer;
/**
 *
 * @author Lope
 */

public class frmRP_HistorialVehiculo extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmRP_HistorialVehiculo.class.getName());

    public frmRP_HistorialVehiculo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void mostrarReporte(String p_Placa) {
        try {
            clsReportes objR = new clsReportes();
            
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Placa_Param", p_Placa); 
            parametros.put("REPORT_DIR", "src/Reportes/");

            JRViewer visor = objR.obtenerVisorInterno("Historial_Vehiculo", parametros);

            if (visor != null) {
                cont.setLayout(new BorderLayout());
                cont.removeAll();
                cont.add(visor, BorderLayout.CENTER);
                cont.revalidate();
                cont.repaint();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cont = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        btnReporte = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout contLayout = new javax.swing.GroupLayout(cont);
        cont.setLayout(contLayout);
        contLayout.setHorizontalGroup(
            contLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1117, Short.MAX_VALUE)
        );
        contLayout.setVerticalGroup(
            contLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 579, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Placa del vehiculo:");

        txtPlaca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnReporte.setText("Generar Reporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cont)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btnReporte)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReporte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cont)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        String p_placa = txtPlaca.getText().trim(); 

    if(p_placa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debes ingresar el número de placa para el reporte.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Llama al método para mostrar el reporte con la placa ingresada
    mostrarReporte(p_placa);
    }//GEN-LAST:event_btnReporteActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReporte;
    private javax.swing.JDesktopPane cont;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
