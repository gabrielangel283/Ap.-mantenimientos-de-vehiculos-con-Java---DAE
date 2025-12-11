package Capa_Presentacion;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        try {
            new Capa_Logica.Orden().actualizarOrdenesFinalizadas();
        } catch (Exception e) {
            System.out.println("Error actualizando ordenes: " + e.getMessage());
        }

        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("Table.selectionBackground", new Color(51, 153, 255)); // color de fondo al seleccionar
            UIManager.put("Table.selectionForeground", Color.WHITE);
            frmMenuPrincipal frm = new frmMenuPrincipal();
            frm.setVisible(true);
            frm.setExtendedState(JFrame.MAXIMIZED_BOTH);

            LocalDate prueba = LocalDate.now();
            String hola = String.valueOf(prueba);

            System.out.println(hola);
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println(e.getMessage());
        }

    }

}
