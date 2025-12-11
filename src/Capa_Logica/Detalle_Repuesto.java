
package Capa_Logica;

/**
 *
 * @author USER
 */
public class Detalle_Repuesto {
    private int repuesto_id;
    private int orden_id;
    private int cantidad;
    private double precio;

    public Detalle_Repuesto() {
    }

    public void setRepuesto_id(int repuesto_id) {
        this.repuesto_id = repuesto_id;
    }

    public void setOrden_id(int orden_id) {
        this.orden_id = orden_id;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getRepuesto_id() {
        return repuesto_id;
    }

    public int getOrden_id() {
        return orden_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }
    
    
    
}
