package Capa_Presentacion;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 *
 * @author Flores Coronel Gabriel Angel
 */
public class DesktopConFondo extends JDesktopPane {
    private Image imagen;

    public DesktopConFondo(String rutaImagen) {
        imagen = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

