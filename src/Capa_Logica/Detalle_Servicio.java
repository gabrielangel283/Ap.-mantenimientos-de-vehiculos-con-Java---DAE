
package Capa_Logica;

/**
 *
 * @author USER
 */
public class Detalle_Servicio {
    private int servicio_id;
    private int orden_id;
    private double precio;

    public Detalle_Servicio() {
    }

    public void setServicio_id(int servicio_id) {
        this.servicio_id = servicio_id;
    }

    public void setOrden_id(int orden_id) {
        this.orden_id = orden_id;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getServicio_id() {
        return servicio_id;
    }

    public int getOrden_id() {
        return orden_id;
    }

    public double getPrecio() {
        return precio;
    }
    
    
}
