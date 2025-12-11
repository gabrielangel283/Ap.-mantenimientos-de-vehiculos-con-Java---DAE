/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Logica;

import java.sql.ResultSet;
import Capa_Datos.clsConexion;
import java.sql.Statement;
import java.sql.Connection;
import java.time.LocalDate;

/**
 * @author DIEGO SANDOVAL PAREDES
 */
public class Repuesto {
    
    clsConexion objConectar = new clsConexion();
    String strSQL;
    ResultSet rs = null;
    
    // Listar marcas para el ComboBox
    public ResultSet listarMarcas() throws Exception {
        strSQL = "SELECT * FROM MARCA WHERE estado = TRUE ORDER BY nombre_marca";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar marcas: " + e.getMessage());
        }
    }

    // Búsqueda flexible para el formulario de mantenimiento (Nombre y/o Marca)
    public ResultSet buscarRepuestos(String nombre, String marca) throws Exception {
        strSQL = "SELECT r.*, m.nombre_marca "
               + "FROM REPUESTO r "
               + "INNER JOIN MARCA m ON r.marca_id = m.marca_id "
               + "WHERE 1=1 ";

        if (nombre != null && !nombre.isEmpty()) {
            strSQL += " AND r.nombre_repuesto ILIKE '%" + nombre + "%'";
        }
        
        if (marca != null && !marca.isEmpty() && !marca.equals("Todas")) {
             strSQL += " AND m.nombre_marca = '" + marca + "'";
        }
        
        strSQL += " ORDER BY r.repuesto_id";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar repuestos: " + e.getMessage());
        }
    }

    public void modificarRepuesto(int id, String nombre, double precio, int stock, String descripcion, String estado, int marcaId) throws Exception {
        strSQL = "UPDATE REPUESTO SET nombre_repuesto='" + nombre + "', precio=" + precio + ", stock=" + stock 
               + ", descripcion='" + descripcion + "', estado='" + estado + "', marca_id=" + marcaId 
               + " WHERE repuesto_id=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar repuesto: " + e.getMessage());
        }
    }

    public void darBajaAlta(int id, String nuevoEstado) throws Exception {
        strSQL = "UPDATE REPUESTO SET estado='" + nuevoEstado + "' WHERE repuesto_id=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al cambiar estado del repuesto: " + e.getMessage());
        }
    }

    public void eliminarRepuesto(int id) throws Exception {
        strSQL = "DELETE FROM REPUESTO WHERE repuesto_id=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("No se puede eliminar el repuesto porque tiene registros asociados.");
        }
    }

    // ========================================================================
    // METODOS ORIGINALES (Mantenidos para compatibilidad con otros Frms)
    // ========================================================================

    public ResultSet listarRepuestos() throws Exception {
        strSQL = "SELECT r.*, m.nombre_marca "
                + "FROM Repuesto r "
                + "INNER JOIN Marca m ON r.marca_id = m.marca_id "
                + "ORDER BY r.repuesto_id";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar repuestos! "+ e.getMessage());
        }
    }
    
    public ResultSet listarRepuestosListos() throws Exception {
        strSQL = "SELECT r.*, m.nombre_marca "
                + "FROM Repuesto r "
                + "INNER JOIN Marca m ON r.marca_id = m.marca_id "
                + "WHERE r.estado='disponible' "
                + "ORDER BY r.repuesto_id";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar repuestos! "+ e.getMessage());
        }
    }
    
    public ResultSet buscarRepuestoEstricto(String nombre) throws Exception{
        strSQL = "SELECT r.*, m.nombre_marca "
                + "FROM Repuesto r "
                + "INNER JOIN Marca m ON r.marca_id = m.marca_id "
                + "WHERE r.nombre_repuesto = '" + nombre + "'"
                + "ORDER BY r.repuesto_id ";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar repuesto! "+ e.getMessage());
        }
    }
    
    public ResultSet buscarRepuesto_Nombre(String nombre) throws Exception{
        strSQL = "SELECT r.*, m.nombre_marca "
                + "FROM Repuesto r "
                + "INNER JOIN Marca m ON r.marca_id = m.marca_id "
                + "WHERE r.nombre_repuesto ILIKE '%" + nombre + "%' and r.estado='disponible' "
                + "ORDER BY r.repuesto_id ";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar repuesto! "+ e.getMessage());
        }
    }
    
    public ResultSet buscarRepuesto_Marca(String marca) throws Exception{
        strSQL = "SELECT r.*, m.nombre_marca "
                + "FROM Repuesto r "
                + "INNER JOIN Marca m ON r.marca_id = m.marca_id "
                + "WHERE m.nombre_marca ILIKE '%" + marca + "%' and r.estado='disponible'"
                + "ORDER BY r.repuesto_id ";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar repuesto! "+ e.getMessage());
        }
    }
    
    public ResultSet buscarRepuesto_Codigo(int cod) throws Exception{
        strSQL = "SELECT r.*, m.nombre_marca "
                + "FROM Repuesto r "
                + "INNER JOIN Marca m ON r.marca_id = m.marca_id "
                + "WHERE r.repuesto_id = " + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar repuesto! "+ e.getMessage());
        }
    }
    
    // Método para obtener el id de la MARCA mediante su nombre
    public int obtenerIdMarca(String nomMarca) throws Exception{
        int idMarca = 0; 
        strSQL = " select marca_id from marca where nombre_marca= '"+nomMarca+"'";
        
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {                
                idMarca = rs.getInt("marca_id"); 
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener el ID de la marca ingresada! --> "+e.getMessage());
        }
        return idMarca;
    }
    
    // Método original de registrar (ligeramente adaptado para usar double en precio)
    public void registrarRepuesto(String nombre, double precio, Integer stock, LocalDate fecha, String estado, String descripcion, String nomMarca) throws Exception{
        
        int marca_id = obtenerIdMarca(nomMarca);
        
        if (marca_id == 0) {
            throw new Exception("Error la marca '"+nomMarca+"' no existe");
        }
        
        strSQL = "INSERT INTO Repuesto(nombre_repuesto, precio, stock, fecha_registro, estado, descripcion, marca_id) " +
                 "VALUES('" + nombre + "', " + precio + ", " + stock + ", '" + fecha + "', '" + estado + "', '" + descripcion + "', " + marca_id + ");";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al regitrar repuesto! "+e.getMessage());
        }
    }
    
    // Sobrecarga para compatibilidad con el nuevo formulario (pide ID de marca directo)
    public void registrarRepuesto(String nombre, double precio, int stock, String descripcion, String estado, int marcaId) throws Exception {
        // Usamos CURRENT_DATE de la base de datos para la fecha
         strSQL = "INSERT INTO Repuesto(nombre_repuesto, precio, stock, fecha_registro, estado, descripcion, marca_id) " +
                 "VALUES('" + nombre + "', " + precio + ", " + stock + ", CURRENT_DATE, '" + estado + "', '" + descripcion + "', " + marcaId + ");";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar repuesto: " + e.getMessage());
        }
    }
    
}