package Capa_Logica;

import Capa_Datos.clsConexion;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 *
 * @author Flores Coronel Gabriel Angel
 */

public class Usuario {
    
    //Atributos de la clase Usuario
    private int usuario_id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String clave;
    private LocalDate fecha_registro;
    private String telefono;
    private String estado;
    private String rol;

    public Usuario() {
    }

    public Usuario(String nombres, String apellidos, String dni, String clave, String telefono, String rol) throws Exception {
        setNombres(nombres);
        setApellidos(apellidos);
        setDni(dni);
        setClave(clave);
        setTelefono(telefono);
        setRol(rol);
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) throws Exception {
        if(dni.length()!=8 || !dni.matches("\\d+")){ // solo numeros y longuitud 8
            throw new Exception("El DNI debe tener 8 dígitos");
        }else{
            this.dni = dni;
        }
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) throws Exception {
        if(telefono.length()!=9 || !telefono.matches("\\d+")){
            throw new Exception("El telefono debe tener 9 dígitos");
        }else{
            this.telefono = telefono;
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) throws Exception {
        if(rol.trim().equals("")) {
            throw new Exception("Debe elegir un rol");
        } else {
            this.rol = rol;
        }
    }
    
    //Crear instancia de la clase clsJDBC
    private static clsConexion objConectar = new clsConexion();

    /**
     * Función que me permitirá loguearme con mi usuario
     *
     * @param dni
     * @param con : Contraseña de usuario ingresado.
     * @return : retorna el nombre del usuario si es que el loguin es correcto.
     * @throws Exception
     */
    public static String[] login(String dni, String con) throws Exception {
        try {
            String sql = "select * from usuario where dni='" + dni + 
                "' and clave=crypt('" + con + "',clave)";
            String[] info_usu = new String[4];
            ResultSet rs = objConectar.consultarBD(sql);
            if (rs.next()) {
                info_usu[0] = rs.getString("nombres");
                info_usu[1] = rs.getString("apellidos");
                info_usu[2] = rs.getString("rol");
                info_usu[3] = rs.getString("estado");
            }
            return info_usu;
        } catch (Exception e) {
            throw new Exception("Error al iniciar sesión. --> " + e.getMessage());
        }
    }

    public static ResultSet listarUsuarios() throws Exception {
        String sql = "SELECT usuario_id AS \"Identificador\", "
           + "nombres AS \"Nombres\", "
           + "apellidos AS \"Apellidos\", "
           + "dni AS \"Num. Identidad\", "
           + "fecha_registro AS \"Fecha registro\", "
           + "telefono AS \"Telefono\", "
           + "estado AS \"Estado actual\", "
           + "rol AS \"Rol\" "
           + "FROM usuario "
           + "ORDER BY 1;";
        try {
            ResultSet rs = objConectar.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios.-->" + e.getMessage());
        }
    }
    
    // AQUI ES DONDE SE GUARDA
    public static void registrar(Usuario objUsuario) throws Exception {
        String sql = "insert into usuario(nombres,apellidos,dni,clave,telefono,rol) "
                + "values('"+objUsuario.getNombres()+"','"+objUsuario.getApellidos()+"','"
                +objUsuario.getDni()+"',crypt('"+objUsuario.getClave()+"',gen_salt('bf')),'"+objUsuario.getTelefono()
                +"','"+objUsuario.getRol()+"')";
        try {
            objConectar.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al insertar el usuario.-->" + e.getMessage());
        }
    }
    
    public static void modificar(int idUsu, Usuario objNuevo) throws Exception {
        String sql = "update usuario set nombres='"+objNuevo.getNombres()+"',apellidos='"+objNuevo.getApellidos()+
                "',dni='"+objNuevo.getDni()+"',telefono='"+objNuevo.getTelefono()+
                "',rol='"+objNuevo.getRol()+"' where usuario_id="+idUsu;
        try {
            objConectar.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al modificar el usuario: " + e.getMessage());
        }
    }
    
    public static int eliminar(int usu_id) throws Exception {
        String sql = "{ ? = call eliminar_usuario(?) }";
        try {
            int resultado = objConectar.ejecutarFuncionEntero(sql, usu_id);
            return resultado;
        } catch (Exception e) {
            throw new Exception("Error al aliminar el usuario: " + e.getMessage());
        }
    }
    
    public static void darBaja(int usu_id) throws Exception {
        String sql = "update usuario set estado='inactivo' where usuario_id="+usu_id;
        try {
            objConectar.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja al usuario: " + e.getMessage());
        }
    }
    
    public static void darAlta(int usu_id) throws Exception {
        String sql = "update usuario set estado='disponible' where usuario_id="+usu_id;
        try {
            objConectar.ejecutarBD(sql);
        } catch (Exception e) {
            throw new Exception("Error al dar de alta al usuario: " + e.getMessage());
        }
    }
    
    public static ResultSet buscarUsuarioPorNombreRol(String nombre, String rol) throws Exception {
        ResultSet rs = null;
        String sql = "";
        if(nombre.trim().equals("") && !rol.trim().equals("")) {
            sql = "select nombres AS \"Nombres\",apellidos AS \"Apellidos\",dni AS \"Num. Identidad\",fecha_registro AS "
                    + "\"Fecha registro\",telefono AS \"Telefono\",estado AS \"Estado actual\",rol AS \"Rol\" "
                    + "from usuario where rol ilike '"+rol+"'";
        } else if(!nombre.trim().equals("") && rol.trim().equals("")) {
            sql = "select nombres AS \"Nombres\",apellidos AS \"Apellidos\",dni AS \"Num. Identidad\",fecha_registro AS "
                    + "\"Fecha registro\",telefono AS \"Telefono\",estado AS \"Estado actual\",rol AS \"Rol\" "
                    + "from usuario where nombres ilike '%"+nombre+"%'";
        } else {
            sql = "select nombres AS \"Nombres\",apellidos AS \"Apellidos\",dni AS \"Num. Identidad\",fecha_registro AS "
                    + "\"Fecha registro\",telefono AS \"Telefono\",estado AS \"Estado actual\",rol AS \"Rol\" "
                    + "from usuario where nombres ilike '%"+nombre+"%' and rol ilike '"+rol+"'";
        }
        try {
            rs = objConectar.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario: " + e.getMessage());
        }
    }
    
    public static int contarUsuarios() throws Exception {
        int contador = 0;
        try {
            ResultSet rs = listarUsuarios();
            while(rs.next()) {
                contador++;
            }
            return contador;
        } catch (Exception e) {
            throw new Exception("Error al contar la cantidad de usuarios: " + e.getMessage());
        }
    }
    
    public static ResultSet obtenerTrabDeOrden(int orden_id) throws Exception {
        String sql = "select * from usuario u inner join orden o on o.usuario_id=u.usuario_id"
                + " where u.estado='ocupado' and u.rol='mecanico'";
        ResultSet rs = null;
        try {
            rs = objConectar.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al obtener el trabajador de una orden: " + e.getMessage());
        }
    }
    
    public static ResultSet obtenerMecanicosPorEstado(String estado) throws Exception {
        String sql = "select dni as DNI,nombres||' '||apellidos as Mecanico,estado, rol"
                + " from usuario where rol='mecanico' and estado='"+estado+"'";
        ResultSet rs = null;
        try {
            rs = objConectar.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al obtener mecanicos por estado: " + e.getMessage());
        }
    }
    
    public static void asignarNvoMecanicoAOrden(int orden_id, int usu_nuev_id, LocalDate nva_fecha_fin) throws Exception {
        try {
            objConectar.iniciarTransaccion();
            
            //cambiar el estado del antiguo usu a disponible
            ResultSet rs = obtenerTrabDeOrden(orden_id);
            int usu_ant_id = 0;
            while (rs.next()) {                
                usu_ant_id = rs.getInt("usuario_id");
            }
            String sqlAntig = "update usuario set estado='disponible' where usuario_id="+usu_ant_id;
            objConectar.ejecutarBD(sqlAntig);
            
            //cambiar el estado del nuevo usu a ocupado
            String sqlNuevo = "update usuario set estado='ocupado' where usuario_id="+usu_ant_id;
            objConectar.ejecutarBD(sqlNuevo);
            
            //cambiar el usuario_id de la orden
            String sqlOrden = "update orden set usuario_id="+usu_nuev_id+",fecha_fin='"+java.sql.Date.valueOf(nva_fecha_fin)+"' where orden_id="+orden_id;
            objConectar.ejecutarBD(sqlOrden);
            
            objConectar.confirmarTransaccion();
        } catch (Exception e) {
            objConectar.cancelarTransaccion();
            throw new Exception("Error al cambiar el mecanico de orden: " + e.getMessage());
        }
    }
    
    public static void asignarMecanicoAOrden(int orden_id, int usu_id, LocalDate fecha_fin) throws Exception {
        try {
            objConectar.iniciarTransaccion();
            
            //cambiar el estado de usuario a 'opupado'
            String sqlMec = "update usuario set estado='ocupado' where usuario_id="+usu_id;
            objConectar.ejecutarBD(sqlMec);
            
            //cambiar el usuario_id, fecha_fin y estado de la orden
            String sqlOrden = "update orden set usuario_id="+usu_id+",fecha_fin='"+
                    java.sql.Date.valueOf(fecha_fin)+"', estado='en proceso' where orden_id="+orden_id;
            objConectar.ejecutarBD(sqlOrden);
            
            
            objConectar.confirmarTransaccion();
        } catch (Exception e) {
            objConectar.cancelarTransaccion();
            throw new Exception("Error al asignar mecanico a orden: " + e.getMessage());
        }
    }
    
    public static int obtenerIdUsuPorDni(String dni) throws Exception {
        ResultSet rs = null;
        String sql = "select usuario_id,dni,nombres||' '||apellidos as mecanico,estado, rol"
                + " from usuario where dni = '"+dni+"'";
        try {
            rs = objConectar.consultarBD(sql);
            while (rs.next()) {                
                return rs.getInt("usuario_id");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener id de usuario por dni: " + e.getMessage());
        }
        return 0;
    }
    
    public static ResultSet listarUsuariosDispDni(String dni) throws Exception {
        ResultSet rs = null;
        String sql = "select dni,nombres||' '||apellidos as mecanico,estado, rol"
                + " from usuario where estado='disponible' and dni ilike '%"+dni+"%' "
                + "and rol='mecanico'";
        try {
            rs = objConectar.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios disponible por dni: " + e.getMessage());
        }
    }
    
    public static ResultSet listarUsuariosDispNombre(String nombre) throws Exception {
        ResultSet rs = null;
        String sql = "select dni \"DNI\",nombres||' '||apellidos as \"Mecanico\",estado as \"Estado\", rol as \"Rol\" from"
                + " usuario where estado='disponible' and nombres||' '||apellidos ilike '%"
                + nombre+"%' and rol='mecanico' order by 1";
        try {
            rs = objConectar.consultarBD(sql);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios disponibles por nombre: " + e.getMessage());
        }
    }
    
}
