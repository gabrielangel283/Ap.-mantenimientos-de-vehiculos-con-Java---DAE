package Capa_Logica;

import Capa_Datos.clsConexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author César
 */
public class Vehiculo {

    clsConexion objCon = new clsConexion();
    String sql;
    ResultSet rs = null;
    Connection con;
    Statement st;

    public int eliminar(int id) {
        try {
            String sqlCheck = "SELECT COUNT(*) AS total FROM Orden WHERE vehiculo_id = " + id;
            ResultSet rsCheck = objCon.consultarBD(sqlCheck);

            if (rsCheck.next()) {
                int total = rsCheck.getInt("total");

                if (total > 0) {
                    String sqlUpdate = "UPDATE Vehiculo SET estado = 0 WHERE vehiculo_id = " + id
                            + " RETURNING 1 as resultado";
                    ResultSet rsUpdate = objCon.consultarBD(sqlUpdate);
                    return rsUpdate.next() ? rsUpdate.getInt("resultado") : 0;
                } else {
                    String sqlDelete = "DELETE FROM Vehiculo WHERE vehiculo_id = " + id + " RETURNING 1 as resultado";
                    ResultSet rsDelete = objCon.consultarBD(sqlDelete);
                    return rsDelete.next() ? rsDelete.getInt("resultado") : 0;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error en eliminar Vehiculo: " + ex.getMessage());
        }
        return 0;
    }

    public ResultSet listarMarcas() {
        sql = "SELECT nombre_marca FROM Marca WHERE estado = TRUE ORDER BY marca_id ASC";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return null;
    }

    public ResultSet buscarCliente(String dni) {
        sql = "SELECT * FROM Cliente WHERE num_doc = " + "'" + dni + "'";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return null;
    }

    public ResultSet listarVehiculo() {
        sql = "SELECT vh.*, mc.marca_id, nombre_marca AS marca, cl.cliente_id, num_doc AS num_documento, (apellidos || ', ' || nombres) AS cliente FROM Vehiculo vh "
                + "INNER JOIN Marca mc ON mc.marca_id = vh.marca_id "
                + "INNER JOIN Cliente cl ON cl.cliente_id = vh.cliente_id "
                + "ORDER BY fecha_registro DESC";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return null;
    }

    public ResultSet listarPorFiltros(String filter, String marca) {
        try {
            filter = (filter == null) ? "" : filter.trim();

            sql = "SELECT vh.*, mc.marca_id, nombre_marca AS marca, cl.cliente_id, num_doc AS num_documento, (apellidos || ', ' || nombres) AS cliente FROM Vehiculo vh "
                    + "INNER JOIN Marca mc ON mc.marca_id = vh.marca_id "
                    + "INNER JOIN Cliente cl ON cl.cliente_id = vh.cliente_id ";

            if (!marca.equalsIgnoreCase("todos") && filter.isEmpty()) {
                sql += "WHERE mc.nombre_marca = '" + marca + "'";
            } else if (!marca.equalsIgnoreCase("todos") && !filter.isEmpty()) {
                sql += "WHERE mc.nombre_marca = '" + marca + "' "
                        + "AND (vh.modelo LIKE '%" + filter + "%' "
                        + "OR vh.color LIKE '%" + filter + "%' "
                        + "OR vh.num_placa LIKE '%" + filter + "%' "
                        + "OR cl.nombres LIKE '%" + filter + "%' "
                        + "OR cl.apellidos LIKE '%" + filter + "%' "
                        + "OR cl.num_doc LIKE '%" + filter + "%')";
            } else if (marca.equalsIgnoreCase("todos") && !filter.isEmpty()) {
                sql += "WHERE vh.modelo LIKE '%" + filter + "%' "
                        + "OR vh.color LIKE '%" + filter + "%' "
                        + "OR vh.num_placa LIKE '%" + filter + "%' "
                        + "OR cl.nombres LIKE '%" + filter + "%' "
                        + "OR cl.apellidos LIKE '%" + filter + "%' "
                        + "OR cl.num_doc LIKE '%" + filter + "%'";
            }
            rs = objCon.consultarBD(sql);
            return rs;

        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return null;
    }

    public int insertarVehiculo(String modelo, String num_placa, String descripcion, String color, int cliente_id,
            int marca_id) {
        int resultado = 0;
        sql = "INSERT INTO Vehiculo(modelo, num_placa, descripcion, color, cliente_id, marca_id) "
                + "VALUES('" + modelo + "', '" + num_placa + "', '" + descripcion + "', '" + color + "', " + cliente_id
                + ", " + marca_id
                + ");";

        try {
            objCon.ejecutarBD(sql);
            return resultado;
        } catch (Exception e) {
            System.getLogger(e.getMessage());
        }

        return resultado;
    }

    public ResultSet buscarIdCliente(String dni) {
        sql = "SELECT cliente_id FROM Cliente WHERE num_doc = '" + dni + "'";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return null;
    }

    public ResultSet buscarIdMarca(String marca) {
        sql = "SELECT marca_id FROM Marca WHERE nombre_marca = '" + marca + "'";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return null;
    }

    public int actualizarVehiculo(int vehiculo_id, String modelo, String num_placa, String descripcion, String color,
            int cliente_id, int marca_id) {
        int resultado = 0;
        sql = "UPDATE Vehiculo SET modelo = '" + modelo + "', num_placa = '" + num_placa + "', descripcion = '"
                + descripcion + "', color = '" + color + "', cliente_id = " + cliente_id + ", marca_id = " + marca_id
                + " WHERE vehiculo_id = " + vehiculo_id + ";";

        try {
            objCon.ejecutarBD(sql);
            return 1;
        } catch (Exception e) {
            System.getLogger(e.getMessage());
        }

        return resultado;
    }

    public ResultSet buscarPorId(int id) {
        sql = "SELECT vh.*, mc.marca_id, nombre_marca AS marca, cl.cliente_id, num_doc AS num_documento, (apellidos || ', ' || nombres) AS cliente FROM Vehiculo vh "
                + "INNER JOIN Marca mc ON mc.marca_id = vh.marca_id "
                + "INNER JOIN Cliente cl ON cl.cliente_id = vh.cliente_id " + "WHERE  vehiculo_id = " + id;
        ResultSet rs = null;
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception ex) {
            System.getLogger(ex.getMessage());
        }
        return rs;
    }

    public int darBajaAlta(int id) {
        int resultado = 0;
        sql = "UPDATE Vehiculo SET estado = NOT estado WHERE vehiculo_id = " + id + " RETURNING 1 as resultado;";
        try {
            ResultSet rs = objCon.consultarBD(sql);
            if (rs.next()) {
                resultado = rs.getInt("resultado");
            }
            return resultado;
        } catch (Exception e) {
            System.getLogger(e.getMessage());
        }
        return resultado;
    }

    public int buscarRankMarca(String marca) {
        int resultado = 0;
        sql = "SELECT * FROM ("
                + "SELECT ROW_NUMBER() OVER(ORDER BY marca_id ASC) AS rank, nombre_marca"
                + " FROM Marca "
                + "WHERE estado = TRUE"
                + ") "
                + "WHERE nombre_marca = '" + marca + "'";

        try {
            ResultSet rs = objCon.consultarBD(sql);
            if (rs.next()) {
                resultado = rs.getInt("rank");
            }
            return resultado;
        } catch (Exception e) {
            System.getLogger(e.getMessage());
        }

        System.out.println(resultado);
        return resultado;
    }

    public ResultSet obtenerVehiculoPorNroPlaca(String nroPlaca) throws Exception {
        ResultSet rs = null;
        sql = "select * from vehiculo inner join marca on marca.marca_id=vehiculo.marca_id where num_placa='" + nroPlaca + "' order by 1 limit 1;";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al obtener vehiculo por numero de placa: " + e.getMessage());
        }
    }

    public ResultSet listarVehiculosClientes() throws Exception {
        ResultSet rs = null;
        sql = "select v.num_placa as \"Num. placa\",v.modelo as \"Modelo\",v.color as Color,m.nombre_marca "
                + "as Marca,c.nombres||' '||c.apellidos as \"Nombre del cliente\""
                + ",c.num_doc as \"Num. documento\" from vehiculo v inner join cliente c on c.cliente_id = v.cliente_id "
                + "inner join marca m on m.marca_id = v.marca_id";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar los vehiculos con sus dueños: " + e.getMessage());
        }
    }

    public ResultSet filtrarVehiculo_CliNombre(String nombreC) throws Exception {
        ResultSet rs = null;
        sql = "select v.num_placa,v.modelo,v.color,m.nombre_marca,c.nombres||' '||c.apellidos as "
                + "nombre_de_cliente,c.num_doc from vehiculo v inner join cliente c on c.cliente_id = v.cliente_id "
                + "inner join marca m on m.marca_id = v.marca_id where c.nombres||' '||c.apellidos ilike "
                + "'%" + nombreC + "%'";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar vehiculos por el nombre del cliente: " + e.getMessage());
        }
    }

    public ResultSet filtrarVehiculo_CliDni(String dni) throws Exception {
        ResultSet rs = null;
        sql = "select v.num_placa,v.modelo,v.color,m.nombre_marca,c.nombres||' '||c.apellidos as "
                + "nombre_de_cliente,c.num_doc from vehiculo v inner join cliente c on c.cliente_id = v.cliente_id "
                + "inner join marca m on m.marca_id = v.marca_id where c.num_doc ilike "
                + "'%" + dni + "%'";
        try {
            rs = objCon.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar vehiculos por el numero de documento del cliente: " + e.getMessage());
        }
    }

    public boolean placaExiste(String placa) throws Exception {
        sql = "SELECT * FROM vehiculo WHERE num_placa = '" + placa + "'";
        rs = objCon.consultarBD(sql);
        return rs.next();

    }

    public boolean placaExisteParaOtroVehiculo(String placa, int vehiculoId) throws Exception {
        sql = "SELECT * FROM vehiculo WHERE num_placa = '" + placa + "' AND vehiculo_id != " + vehiculoId;
        rs = objCon.consultarBD(sql);
        return rs.next();
    }

}
