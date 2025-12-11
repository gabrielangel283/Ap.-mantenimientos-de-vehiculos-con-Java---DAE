package Capa_Logica;

import Capa_Datos.clsConexion;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.time.LocalDate;

/**
 *
 * @author Txiki Xavier Garaycochea Mendoza
 */
public class Servicio {
    clsConexion objConectar = new clsConexion();
    String strSQL;
    ResultSet rs;
    Statement st;
    Connection con;
    
    public ResultSet listarServicios() throws Exception{
        strSQL = "select * from servicio";
        try{
            rs = objConectar.consultarBD(strSQL);
            return rs;
        }catch(Exception e){
            throw new Exception("Error al listar servicios: " + e.getMessage());
        }
    }
    
    public ResultSet listarServiciosListos() throws Exception{
        strSQL = "select * from servicio where estado=true";
        try{
            rs = objConectar.consultarBD(strSQL);
            return rs;
        }catch(Exception e){
            throw new Exception("Error al listar servicios: " + e.getMessage());
        }
    }
    
    public ResultSet buscarServicio(String nom) throws Exception{
        strSQL = "select * from servicio where nombre_servicio='" + nom + "'";
        try{
            rs =  objConectar.consultarBD(strSQL);
            return rs;
        }catch(Exception e){
            throw new Exception("Error al buscar servicio: " + e.getMessage());
        }
    }
    
    public ResultSet buscarServicioFlex(String nom) throws Exception{
        strSQL = "select * from servicio where nombre_servicio ILIKE '%" + nom + "%' and estado=true";
        try{
            rs =  objConectar.consultarBD(strSQL);
            return rs;
        }catch(Exception e){
            throw new Exception("Error al buscar servicio: " + e.getMessage());
        }
    }
    
    public void registrar(String nom, double prec, LocalDate fecha, boolean estado, String desc) throws Exception{
        strSQL = "insert into servicio(nombre_servicio,precio_base,fecha_registro,estado,descripcion) values ('" +
                nom + "'," + prec + ",'" + fecha + "'," + estado + ",'"  + desc + "')";
        try{
            objConectar.ejecutarBD(strSQL);
        }catch(Exception e){
            throw new Exception("Error al registrar servicio: " + e.getMessage());
        }
    }
    
    public void eliminar(int cod) throws Exception{
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);

            strSQL = "select estado from servicio where servicio_id=" + cod;
            st = con.createStatement();
            rs = st.executeQuery(strSQL);

            if (rs.next()) {
                boolean estado = rs.getBoolean("estado");

                if (estado) {
                    strSQL = "update servicio set estado=false where servicio_id=" + cod;
                    st.executeUpdate(strSQL);
                } else {

                    strSQL = "delete from servicio where servicio_id=" + cod;
                    st.executeUpdate(strSQL);
                }
            } else {
                throw new Exception("El servicio con id " + cod + " no existe.");
            }

            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new Exception("Error al intentar eliminar servicio: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }
    
    public void darBajaAlta(int cod) throws Exception{
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);
            strSQL = "update servicio set estado=not estado where servicio_id=" + cod;
            objConectar.ejecutarBD(strSQL);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new Exception("Error al dar de baja al servicio: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }
    
    public void modificar(String nom, double prec, LocalDate fecha, boolean estado, String desc, int cod) throws Exception{
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);
            strSQL = "update servicio set nombre_servicio='" + nom + "',precio_base=" + prec + ",fecha_registro='"
                    + java.sql.Date.valueOf(fecha) + "',estado=" + estado + ", descripcion='" + desc + "' where servicio_id=" + cod;
            objConectar.ejecutarBD(strSQL);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new Exception("Error al modificar el servicio: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }
    
    public ResultSet listarNombresServicios() throws Exception {
        strSQL = "select nombre_servicio from servicio where estado=true order by 1";
        ResultSet rs = null;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar nombres de servicios: " + e.getMessage());
        }
    }
    
}
