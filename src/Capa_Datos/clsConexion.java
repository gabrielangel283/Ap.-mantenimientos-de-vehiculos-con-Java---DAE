package Capa_Datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.CallableStatement;  

/**
 *
 * @author todos
 */

public class clsConexion {

    private String driver, url, user, password;
    private Connection con;
    private Statement sent = null;

    public clsConexion() {

        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/bd_DAE";
        this.user = "postgres";
        this.password = "postgres";
        this.con = null;

        // config.properties, no borrar pls, César lo usa
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);

            this.password = props.getProperty("db.password", this.password);

        } catch (IOException e) {
            System.out.println("Ño");
        }
    }

    public void conectar() throws Exception {
        //ESTO ES PARA LAS TRANSACCION
        if (con == null || con.isClosed()) {
            try {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, password);
                con.setAutoCommit(true); // modo normal por defecto
            } catch (ClassNotFoundException | SQLException ex) {
                throw new Exception("Error al conectar a la BD: " + ex.getMessage());
            }
        }
    }

    public void desconectar() throws Exception {
        //ESTO ES PARA LAS TRANSACCIONES
        if (con != null && !con.isClosed()) {
            try {
                con.close();
            } catch (SQLException ex) {
                throw new Exception("Error al desconectar de la BD: " + ex.getMessage());
            }
        }
    }

    public ResultSet consultarBD(String strSQL) throws Exception {
        ResultSet rs = null;
        try {
            conectar();
            sent = con.createStatement();
            rs = sent.executeQuery(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al ejecutar consulta en la BD:  " + e.getMessage());
        } finally {
            // No cierres si estamos en transacción
            if (sent != null && con.getAutoCommit()) {
                sent.close();
            }
            if (con != null && con.getAutoCommit()) {
                desconectar();
            }
        }
    }

    public void ejecutarBD(String strSQL) throws Exception {
        // PARA LAS TRANSACCIONES
        try {
            conectar();
            sent = con.createStatement();
            sent.executeUpdate(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al ejecutar la BD: " + e.getMessage());
        } finally {
            if (sent != null) {
                sent.close();
            }
            if (con.getAutoCommit()) {
                desconectar();
            }
        }
    }

    public Connection getCon() {
        return con;
    }

    public int ejecutarFuncionEntero(String sql, Object... params) throws Exception {
        CallableStatement cs = null;
        try {
            conectar();
            cs = con.prepareCall(sql);

            cs.registerOutParameter(1, java.sql.Types.INTEGER);

            for (int i = 0; i < params.length; i++) {
                cs.setObject(i + 2, params[i]);
            }

            cs.execute();

            return cs.getInt(1);
        } catch (Exception e) {
            throw new Exception("Error al ejecutar función: " + e.getMessage());
        } finally {
            if (cs != null) {
                cs.close();
            }
            if (con != null) {
                desconectar();
            }
        }
    }

    public void iniciarTransaccion() throws Exception {
        conectar();
        con.setAutoCommit(false);
    }

    public void confirmarTransaccion() throws Exception {
        if (con != null) {
            con.commit();
            con.setAutoCommit(true);
        }
    }

    public void cancelarTransaccion() throws Exception {
        if (con != null) {
            con.rollback();
            con.setAutoCommit(true);
        }
    }

}
