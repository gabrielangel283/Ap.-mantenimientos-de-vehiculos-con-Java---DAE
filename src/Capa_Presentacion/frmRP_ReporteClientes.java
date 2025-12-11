package Capa_Presentacion;

/**
 *
 * @author Lope
 */

import Reportes.clsReportes;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.swing.JRViewer;
import javax.swing.JOptionPane;


public class frmRP_ReporteClientes extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmRP_ReporteClientes.class.getName());

    public frmRP_ReporteClientes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    private void mostrarReporteClientes() {
        try {
            clsReportes objR = new clsReportes();
            
            // 1. Crear un mapa de parámetros vacío (el reporte no necesita filtros de entrada)
            Map<String, Object> parametros = new HashMap<>();
            
            // Si el reporte usa subreportes, a veces necesita esta ruta:
            parametros.put("REPORT_DIR", clsReportes.RUTA_REPORTES);
            
            // 2. Obtener el visor interno del reporte
            // Nombre del archivo: "ReportComprobantes" (sin la extensión .jasper)
            JRViewer visor = objR.obtenerVisorInterno("ReportComprobantes", parametros);

            if (visor != null && cont != null) {
                // 3. Limpia el panel y agrega el visor
                cont.setLayout(new BorderLayout());
                cont.removeAll();
                cont.add(visor, BorderLayout.CENTER);
                cont.revalidate();
                cont.repaint();
            } else if (visor == null) {
                // Si el visor es null, objR.obtenerVisorInterno ya mostró un JOptionPane con el error.
                JOptionPane.showMessageDialog(this, "No se pudo obtener el visor del reporte.", "Error de Reporte", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error general al intentar generar el reporte: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cont = new javax.swing.JDesktopPane();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout contLayout = new javax.swing.GroupLayout(cont);
        cont.setLayout(contLayout);
        contLayout.setHorizontalGroup(
            contLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        contLayout.setVerticalGroup(
            contLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Generar Reporte");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cont)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(492, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(463, 463, 463))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(cont)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mostrarReporteClientes();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane cont;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
