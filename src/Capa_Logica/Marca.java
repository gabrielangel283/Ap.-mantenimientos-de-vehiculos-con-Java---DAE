package Capa_Logica;

import Capa_Datos.clsConexion;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author USER
 */
public class Marca {
    private static clsConexion objConex = new clsConexion();
    private static Statement stm;
    
    private int marca_id;
    private String nombre_marca;
    private LocalDate fecha_registro;
    private boolean estado;
    private String descipcion;

    public Marca() {
    }

    public void setMarca_id(int marca_id) {
        this.marca_id = marca_id;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getMarca_id() {
        return marca_id;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }

    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public boolean isEstado() {
        return estado;
    }
    
    public static ResultSet listarMarcas() throws Exception {
        String sql = "SELECT nombre_marca AS \"Identificador\", "
           + "nombre_marca AS \"Nombre\", "
           + "fecha_registro AS \"Fecha registro\", "
           + "CASE WHEN estado THEN 'Vigente' ELSE 'No vigente' END AS \"Estado actual\" "
           + "FROM marca "
           + "ORDER BY 1;";
        try {
            return objConex.consultarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al listar las marcas: " + e.getMessage());
        }
    }
    
    public static void insertarMarca(String nombreMarca, String descrip) throws Exception {
        String sql = "insert into marca(nombre_marca,descripcion) values('"+nombreMarca+"','"+descrip+"')";
        try {
            objConex.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al insertar la marca: " + e.getMessage());
        }
    }
    
    public static int eliminarMarca(int idMarca) throws Exception {
        String sql = "{ ? = call eliminar_marca(?) }";
        try {
            int resultado = objConex.ejecutarFuncionEntero(sql, idMarca);
            return resultado;
        } catch (Exception e) {
            throw new Exception("Error al eliminar la marca: " + e.getMessage());
        }
    }
    
    public static void modificarMarca(Marca objNuevo, int marca_id) throws Exception {
        String sql  = "update marca set nombre_marca='"+objNuevo.getNombre_marca()+"',descripcion='"+
                        objNuevo.getDescipcion()+"' where marca_id="+marca_id;
        try {
            objConex.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al modificar la marca: " + e.getMessage());
        }
    }
    
    public static void darBajaAltaMarca(int marca_id) throws Exception {
        String sql = "update marca set estado= not estado where marca_id="+marca_id;
        try {
            objConex.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al cambiar el estado de marca: " + e.getMessage());
        }
    }
    
    public static int cantidadMarcas() throws Exception {
        String sql = "select count(*) from marca";
        try {
            ResultSet rs = objConex.consultarBD(sql);
            while (rs.next()) {                
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener la cantidad de marcas: " + e.getMessage());
        }
        return 0;
    }
    
    public static ResultSet buscarMarcaPorNombre(String nomMarca) throws Exception {
        String sql = "select * from marca where nombre_marca ilike '%"+nomMarca+"%'";
        try {
            ResultSet rs = objConex.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al obtener una marca por su nombre: " + e.getMessage());
        }
    }
    
    public static ResultSet obtenerMarca(int marca_id) throws Exception {
        String sql = "select * from marca where marca_id="+marca_id;
        try {
            ResultSet rs = objConex.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al obtener una marca por su id: " + e.getMessage());
        }
    }
    
    
}
