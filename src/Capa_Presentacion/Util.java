package Capa_Presentacion;

import java.lang.reflect.Field;
import javax.swing.JTable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class Util {
    
    public static void llenarTabla(JTable tabla, ResultSet rs) throws SQLException {
        // Obtener metadatos de las columnas
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Crear un modelo de tabla
        DefaultTableModel model = new DefaultTableModel();

        // Añadir nombres de columnas
        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(metaData.getColumnLabel(i));
        }

        // Añadir filas con datos
        while (rs.next()) {
            Object[] fila = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                fila[i - 1] = rs.getObject(i);
            }
            model.addRow(fila);
        }

        // Asignar el modelo a la JTable
        tabla.setModel(model);
    }

    public static void llenarTablaConObjeto(JTable tabla, Object obj) {
        // Obtener la clase del objeto
        Class<?> clazz = obj.getClass();

        // Obtener todos los atributos declarados
        Field[] fields = clazz.getDeclaredFields();

        // Crear modelo de tabla con 2 columnas
        String[] columnas = {"Atributo", "Valor"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Recorrer atributos
        for (Field field : fields) {
            field.setAccessible(true); // permitir acceso aunque sean privados
            try {
                Object valor = field.get(obj);
                modelo.addRow(new Object[]{field.getName(), valor});
            } catch (IllegalAccessException e) {
                modelo.addRow(new Object[]{field.getName(), "No accesible"});
            }
        }

        // Asignar modelo a la tabla
        tabla.setModel(modelo);
    }
}
