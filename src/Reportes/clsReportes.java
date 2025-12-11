package Reportes;

import Capa_Datos.clsConexion;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.swing.JRViewer;

public class clsReportes {

    // Carpeta base de reportes
    public static final String RUTA_REPORTES = "src/Reportes/";

    /**
     * 📊 Método para mostrar el reporte en una ventana independiente
     * (JasperViewer)
     *
     * @param nombreReporte
     * @param parametros
     * @param conexion
     */
    public void mostrarReporte(String nombreReporte, Map<String, Object> parametros, Connection conexion) {
        try {
            String rutaReporte = RUTA_REPORTES + nombreReporte + ".jasper";

            // Cargar el archivo .jasper compilado
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);

            // Llenar el reporte con parámetros y conexión
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexion);

            // Mostrar el reporte en una ventana
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Vista de Reporte: " + nombreReporte);
            viewer.setVisible(true);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
        }
    }

    /**
     * 🧩 Método para usar el reporte dentro de un panel o JFrame (interno)
     * Devuelve un JRViewer que puedes agregar a tu interfaz.
     *
     * @param nombreReporte
     * @param parametros
     * @param conexion
     * @return
     */
    public JRViewer obtenerVisorInterno(String nombreReporte, Map<String, Object> parametros) throws Exception {
        try {
            clsConexion objC = new clsConexion();
            objC.conectar();
            Connection con = objC.getCon();
            String rutaReporte = RUTA_REPORTES + nombreReporte + ".jasper";
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, con);
            return new JRViewer(print);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error al crear visor interno: " + e.getMessage());
            return null;
        }
    }

    /**
     * 📄 Método para generar un JasperPrint (para exportar o manipular)
     *
     * @param nombreReporte
     * @param parametros
     * @param conexion
     * @return
     */
    public JasperPrint generarReporte(String nombreReporte, Map<String, Object> parametros, Connection conexion) {
        try {
            String rutaReporte = RUTA_REPORTES + nombreReporte + ".jasper";
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);
            return JasperFillManager.fillReport(reporte, parametros, conexion);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error al generar JasperPrint: " + e.getMessage());
            return null;
        }
    }

    /**
     * 💾 Método auxiliar: exportar directamente a PDF
     *
     * @param nombreReporte
     * @param parametros
     * @param conexion
     * @param rutaSalida
     */
    public void exportarAPDF(String nombreReporte, Map<String, Object> parametros, Connection conexion, String rutaSalida) {
        try {
            JasperPrint print = generarReporte(nombreReporte, parametros, conexion);
            if (print != null) {
                JasperExportManager.exportReportToPdfFile(print, rutaSalida);
                JOptionPane.showMessageDialog(null, "Reporte exportado correctamente a: " + rutaSalida);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar a PDF: " + e.getMessage());
        }
    }

    /**
     * Compila un solo archivo .jrxml a .jasper
     *
     * @param nombreReporte Nombre del reporte sin extensión (ejemplo:
     * "GananciasA1")
     */
    public static void compilarReporte(String nombreReporte) throws Exception {
        try {
            String jrxmlPath = RUTA_REPORTES + nombreReporte + ".jrxml";
            String jasperPath = RUTA_REPORTES + nombreReporte + ".jasper";

            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            System.out.println("✅ Reporte compilado correctamente: " + jasperPath);
        } catch (JRException e) {
            System.err.println("❌ Error al compilar el reporte: " + e.getMessage());
            throw new Exception("Error: " + e.getMessage());
        }
    }

    public JRViewer reporteInterno(String archivoReporte, Map<String, Object> parametros) throws Exception {
        try {
            clsConexion objConexion = new clsConexion();
            objConexion.conectar();
            JasperPrint reporte
                    = JasperFillManager.fillReport(this.RUTA_REPORTES + archivoReporte, parametros,
                            objConexion.getCon()
                    );
            JRViewer visor = new JRViewer(reporte);
            return visor;
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());

        }
        return null;
    }

    public JasperPrint reporteExterno(String archivoReporte, Map<String, Object> parametros) throws Exception {
        try {
            clsConexion objConexion = new clsConexion();
            objConexion.conectar();
            JasperPrint reporte
                    = JasperFillManager.fillReport(
                            this.RUTA_REPORTES + archivoReporte,
                            parametros,
                            objConexion.getCon()
                    );

            return reporte;
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

}
