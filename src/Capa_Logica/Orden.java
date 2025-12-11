package Capa_Logica;

import Capa_Datos.clsConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Txiki Xavier Garaycochea Mendoza
 */
public class Orden {

    private int orden_id;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String estado;
    private int usuario_id;
    private int vehiculo_id;
    private List<Detalle_Repuesto> repuestos;   // muchos repuestos
    private List<Detalle_Servicio> servicios;   // muchos servicios

    public Orden() {
    }

    public void setOrden_id(int orden_id) {
        this.orden_id = orden_id;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public void setVehiculo_id(int vehiculo_id) {
        this.vehiculo_id = vehiculo_id;
    }

    public void setRepuestos(List<Detalle_Repuesto> repuestos) {
        this.repuestos = repuestos;
    }

    public void setServicios(List<Detalle_Servicio> servicios) {
        this.servicios = servicios;
    }

    public int getOrden_id() {
        return orden_id;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public String getEstado() {
        return estado;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public int getVehiculo_id() {
        return vehiculo_id;
    }

    public List<Detalle_Repuesto> getRepuestos() {
        return repuestos;
    }

    public List<Detalle_Servicio> getServicios() {
        return servicios;
    }

    @Override
    public String toString() {
        return "Orden{" + "orden_id=" + orden_id + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", estado=" + estado + ", usuario_id=" + usuario_id + ", vehiculo_id=" + vehiculo_id + '}';
    }

    private static clsConexion objCon = new clsConexion();

    public ResultSet buscarDetalleOrden(int id) throws Exception {
        String strSQL = "SELECT * FROM DETALLE_REPUESTO WHERE orden_id = " + id;
        try {
            ResultSet rs = objCon.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar detalle: " + e.getMessage());
        }
    }

    public void anularOrden(int id, String motivo) throws Exception {
        try {
            objCon.iniciarTransaccion();

            ResultSet rsR = buscarDetalleOrden(id);
            while (rsR.next()) {
                int cantidad = rsR.getInt("cantidad");
                int idR = rsR.getInt("repuesto_id");
                String strSQL1 = "UPDATE repuesto SET stock=stock + " + cantidad + ", estado='disponible' WHERE repuesto_id=" + idR;
                objCon.ejecutarBD(strSQL1);
            }
            String strSQL2 = "UPDATE orden SET estado='anulado' WHERE orden_id=" + id;
            objCon.ejecutarBD(strSQL2);
            String strSQL3 = "INSERT INTO DETALLE_ANULADO(orden_id,motivo) VALUES (" + id + ", '" + motivo + "')";
            objCon.ejecutarBD(strSQL3);

            objCon.confirmarTransaccion();
        } catch (Exception e) {
            objCon.cancelarTransaccion();
            throw new Exception("Error al anular orden: " + e.getMessage());
        } finally {
            objCon.desconectar();
        }
    }

    public static void registrarOrden2(DefaultTableModel repuestos, DefaultTableModel servicios, String placa,
            LocalDate fechaI) throws Exception {
        try {
            objCon.iniciarTransaccion();  // ⬅️ Inicia la transacción
            //Sacar idVehiculos
            ResultSet rsVeh = new Vehiculo().obtenerVehiculoPorNroPlaca(placa);
            int idV = 0;
            while (rsVeh.next()) {
                idV = rsVeh.getInt("vehiculo_id");
            }
            // 1️⃣ Insertar la orden principal
            String sqlOrden = "INSERT INTO orden(fecha_fin, estado, usuario_id, vehiculo_id, fecha_inicio) "
                    + "VALUES (null, 'pendiente', null, " + idV + ",'" + java.sql.Date.valueOf(fechaI) + "')";
            objCon.ejecutarBD(sqlOrden);

            // Obtener el último ID generado (para PostgreSQL)
            String sqlId = "SELECT currval(pg_get_serial_sequence('orden', 'orden_id')) AS id";
            ResultSet rs = objCon.getCon().createStatement().executeQuery(sqlId);
            int idOrdenGenerado = 0;
            if (rs.next()) {
                idOrdenGenerado = rs.getInt("id");
            }
            rs.close();

            // 2️⃣ Insertar repuestos relacionados
            for (int i = 0; i < repuestos.getRowCount(); i++) {
                ResultSet rsRep = new Repuesto().buscarRepuestoEstricto(repuestos.getValueAt(i, 0).toString());
                int idR = 0;
                while (rsRep.next()) {
                    idR = rsRep.getInt("repuesto_id");
                }
                String sqlRep = "INSERT INTO detalle_repuesto(orden_id, repuesto_id, cantidad, precio) "
                        + "VALUES (" + idOrdenGenerado + ", " + idR + ", "
                        + Integer.parseInt(repuestos.getValueAt(i, 2).toString()) + ", " + Double.parseDouble(repuestos.getValueAt(i, 1).toString()) + ")";
                objCon.ejecutarBD(sqlRep);
            }

            // 4️⃣ Insertar servicios relacionados
            for (int i = 0; i < servicios.getRowCount(); i++) {
                ResultSet rsSer = new Servicio().buscarServicio(servicios.getValueAt(i, 1).toString());
                int idS = 0;
                while (rsSer.next()) {
                    idS = rsSer.getInt("servicio_id");
                }
                String sqlServ = "INSERT INTO detalle_servicio(servicio_id, orden_id, precio) "
                        + "VALUES (" + idS + ", " + idOrdenGenerado + ", " + Double.parseDouble(servicios.getValueAt(i, 2).toString()) + ")";
                objCon.ejecutarBD(sqlServ);
            }

            //Actuaalizar stock de repuestos
            for (int i = 0; i < repuestos.getRowCount(); i++) {
                ResultSet rsRep = new Repuesto().buscarRepuestoEstricto(repuestos.getValueAt(i, 0).toString());
                int idR = 0;
                while (rsRep.next()) {
                    idR = rsRep.getInt("repuesto_id");
                }
                int cantidad = Integer.parseInt(repuestos.getValueAt(i, 2).toString());
                String sqlRep = "UPDATE repuesto SET stock=stock-" + cantidad + " WHERE repuesto_id=" + idR;
                objCon.ejecutarBD(sqlRep);
            }

            // 5️⃣ Confirmar todo
            objCon.confirmarTransaccion();

        } catch (Exception e) {
            objCon.cancelarTransaccion();
            throw new Exception("Error en la transacción de orden: " + e.getMessage());
        } finally {
            objCon.desconectar();
        }
    }

    public static ResultSet listarOrdenesPorEstado(String estado) throws Exception {
        String sql = "";
        if (estado.equalsIgnoreCase("pendiente")) {
            sql = "select o.orden_id as \"Num. Orden\",o.fecha_inicio as \"Fecha inicio\",o.estado as \"Estado\",v.num_placa as \"Num. Placa\""
                    + "from orden o inner join vehiculo v on v.vehiculo_id = o.vehiculo_id "
                    + "where o.estado='" + estado + "' order by 2";
        } else if (estado.equalsIgnoreCase("en proceso")) { // en proceso - saldra el trbajador actual de la orden
            sql = "select o.orden_id as \"Num. Orden\",o.fecha_inicio as \"Fecha inicio\",o.estado as \"Estado\",v.num_placa \"Num. Placa\",u.nombres||' '||u.apellidos "
                    + "as \"Mecanico\",u.dni as \"DNI\" from orden o inner join usuario u on u.usuario_id = o.usuario_id "
                    + "inner join vehiculo v on v.vehiculo_id = o.vehiculo_id "
                    + "where o.estado='" + estado + "' order by 2";
        } else {
            sql = "select o.orden_id as \"Num. Orden\",o.fecha_inicio as \"Fecha inicio\",o.estado "
                    + "as \"Estado\",v.num_placa \"Num. Placa\",u.nombres||' '||u.apellidos as "
                    + "\"Mecanico\",u.dni as \"DNI\" from orden o inner join usuario u on u.usuario_id = o.usuario_id "
                    + "inner join vehiculo v on v.vehiculo_id = o.vehiculo_id where o.estado=lower('" + estado + "') "
                    + "order by 1";
        }
        ResultSet rs = null;
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar ordenes por estado: " + e.getMessage());
        }
    }

    public static ResultSet listarOrdenesPorMayorAFecha_Estado(String estado, LocalDate fecha_ini) throws Exception {
        String sql = "";
        if (estado.equalsIgnoreCase("pendiente")) {
            sql = "select o.orden_id,o.fecha_inicio,o.estado,v.num_placa "
                    + "from orden o inner join vehiculo v on v.vehiculo_id = o.vehiculo_id "
                    + "where o.estado='" + estado + "' and fecha_inicio >= '"
                    + java.sql.Date.valueOf(fecha_ini) + "' order by 2";
        } else if (estado.equalsIgnoreCase("en proceso")) { // en proceso - saldra el trbajador actual de la orden
            sql = "select o.orden_id,o.fecha_inicio,o.estado,v.num_placa,u.nombres||' '||u.apellidos "
                    + "as mecanico,u.dni from orden o inner join usuario u on u.usuario_id = o.usuario_id "
                    + "inner join vehiculo v on v.vehiculo_id = o.vehiculo_id "
                    + "where o.estado='" + estado + "' and fecha_inicio >= '"
                    + java.sql.Date.valueOf(fecha_ini) + "' order by 2";
        }else{
            sql = "select o.orden_id,o.fecha_inicio,o.estado,v.num_placa,u.nombres||' '||u.apellidos "
                    + "as mecanico,u.dni from orden o inner join usuario u on u.usuario_id = o.usuario_id "
                    + "inner join vehiculo v on v.vehiculo_id = o.vehiculo_id "
                    + "where o.estado='" + estado + "' and fecha_inicio >= '"
                    + java.sql.Date.valueOf(fecha_ini) + "' order by 2";
        }
        ResultSet rs = null;
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar las ordenes por fecha mayor y estado: " + e.getMessage());
        }
    }

    public void actualizarOrdenesFinalizadas() {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            objCon.conectar();
            con = objCon.getCon();

            String sql = "UPDATE ORDEN "
                    + "SET estado = 'completado' "
                    + "WHERE fecha_fin <= NOW() "
                    + "AND estado <> 'completado' "
                    + "AND estado <> 'pagado'";

            ps = con.prepareStatement(sql);

            int filasActualizadas = ps.executeUpdate();

            System.out.println("Órdenes actualizadas automáticamente: " + filasActualizadas);

        } catch (Exception e) {
            System.out.println("Error actualizando órdenes finalizadas: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
    }
    
    public void pagarOrden(int orden_id, String tipo, float monto, String metPago, int cliente_id) throws Exception {
        try {
            objCon.iniciarTransaccion();
            
            // cambiar el estado de la orden a 'pagado' y la fecha_pago en la fecha actual de pago
            String sqlOrden = "UPDATE orden SET estado='pagado',fecha_pago=current_Date WHERE orden_id=" + orden_id;
            objCon.ejecutarBD(sqlOrden);
            
            // generar el comprobante de pago
            String sqlComprobante = "insert into comprobante(monto_total,metodo_pago,"
                    + "tipo_comprobante,orden_id,cliente_id) values("+monto+",'"+metPago
                    + "','"+tipo+"',"+orden_id+","+cliente_id+")";
            
            objCon.confirmarTransaccion();
        } catch (Exception e) {
            objCon.cancelarTransaccion();
            throw new Exception("Error al pagar orden: " + e.getMessage());
        }
    }
    
    public static float obtenerMonto(int orden_id) throws Exception {
        String sql = "SELECT sum(d_s.precio)+sum(d_r.precio*d_r.cantidad) as monto " +
                    "FROM ORDEN o inner join detalle_servicio d_s on d_s.orden_id = o.orden_id " +
                    "inner join detalle_repuesto d_r on d_r.orden_id = o.orden_id where o.orden_id=1";
        try {
            ResultSet rs = objCon.consultarBD(sql);
            while (rs.next()) {                
                return rs.getFloat("monto");
            }
        } catch (Exception e) {
            throw new Exception("Error al pagar orden: " + e.getMessage());
        }
        return 0;
    }

}
