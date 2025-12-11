package Capa_Logica;

import Capa_Datos.clsConexion;
import java.sql.*;
import java.text.Normalizer;

/**
 *
 * @author Garaycochea Mendoza Txiki Xavier
 */
public class Comprobante {

    clsConexion objConectar = new clsConexion();
    String strSQL;
    ResultSet rs = null;
    Connection con;
    Statement st;
    PreparedStatement ps;

    public void registrar(){
        
    }
    
    public ResultSet listarClientes() throws Exception {
        strSQL = "select * from Cliente order by cliente_id";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar clientes: " + e.getMessage());
        }
    }

    public ResultSet obtenerClienteID(int cod) throws Exception {
        strSQL = "select * from Cliente where cliente_id=" + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente: " + e.getMessage());
        }
    }

    public static String unaccent(String str) {
        if (str == null) {
            return null;
        }
        str = str.toLowerCase();
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }

    public ResultSet buscarClientesPorFiltro(String numDoc, String filtroNombreApellido) throws Exception {
        String NumDoc = (numDoc != null) ? numDoc.trim() : "";
        String FiltroNombreApellido = (filtroNombreApellido != null) ? filtroNombreApellido.trim() : "";

        String caso = "1=1";
        ResultSet rs = null; 

        if (!NumDoc.isEmpty()) {
            caso += " AND num_doc ILIKE '" + NumDoc + "%'";
        }

        if (!FiltroNombreApellido.isEmpty()) {
            String filtroSinAcentos = unaccent(FiltroNombreApellido);
            caso += " AND (LOWER(unaccent(nombres)) LIKE '%" + filtroSinAcentos + "%' OR LOWER(unaccent(apellidos)) LIKE '%" + filtroSinAcentos + "%')";
        }
        strSQL = "SELECT * FROM CLIENTE WHERE " + caso;

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar clientes por filtro: " + e.getMessage());
        }
    }

    public void registrar(String nom, String ap, String fono, String numDoc, String tipoCliente) throws Exception {

        strSQL = "INSERT INTO Cliente(nombres, apellidos, num_doc, telefono, tipo) VALUES (?, ?, ?, ?, ?)";

        try {
            objConectar.conectar();
            con = objConectar.getCon();
            ps = con.prepareStatement(strSQL);

            ps.setString(1, nom);
            ps.setString(2, ap);
            ps.setString(3, numDoc);
            ps.setString(4, fono);
            ps.setString(5, tipoCliente);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Error al intentar registrar el usuario: " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            objConectar.desconectar();
        }
    }

    public void modificar(Integer cod, String nom, String ap, String fono, String numDoc, String tipoCliente) throws Exception {

        strSQL = "UPDATE Cliente SET nombres=?, apellidos=?, telefono=?, num_doc=?, tipo=? WHERE cliente_id=?";

        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);

            ps = con.prepareStatement(strSQL);

            ps.setString(1, nom);
            ps.setString(2, ap);
            ps.setString(3, fono);
            ps.setString(4, numDoc);
            ps.setString(5, tipoCliente);
            ps.setInt(6, cod);

            ps.executeUpdate();
            con.commit();

        } catch (Exception e) {
            con.rollback();
            throw new Exception("Error al modificar el cliente: " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            objConectar.desconectar();
        }
    }

    public void eliminar(Integer cod) throws Exception {
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);

            strSQL = "select estado from Cliente where cliente_id=" + cod;
            st = con.createStatement();
            rs = st.executeQuery(strSQL);

            if (rs.next()) {
                boolean estado = rs.getBoolean("estado");

                if (estado) {
                    strSQL = "update Cliente set estado=false where cliente_id=" + cod;
                    st.executeUpdate(strSQL);
                } else {
                    strSQL = "delete from Cliente where cliente_id=" + cod;
                    st.executeUpdate(strSQL);
                }
            } else {
                throw new Exception("El cliente con id " + cod + " no existe.");
            }

            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new Exception("Error al intentar eliminar cliente: " + e.getMessage());
        } finally {
            if (st != null) {
                st.close();
            }
            objConectar.desconectar();
        }
    }

    public void darBajaAlta(Integer cod) throws Exception {
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);
            strSQL = "update Cliente set estado=not estado where cliente_id=" + cod;
            objConectar.ejecutarBD(strSQL);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new Exception("Error al dar de baja/alta al cliente: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }

    public int totalRegistros() throws Exception {
        int cant = 0;
        strSQL = "Select count(*) as total from Cliente";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                cant = rs.getInt("total");
            }
        } catch (Exception e) {
            throw new Exception("Error al contar registros: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return cant;
    }
}
