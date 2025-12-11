package Capa_Presentacion;

import Capa_Logica.Cliente;
import Capa_Logica.Orden;
import Capa_Logica.Repuesto;
import Capa_Logica.Vehiculo;
import java.awt.Frame;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Txiki Xavier Garaycochea Mendoza
 */
public class frmGenerarOrden extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmGenerarOrden.class.getName());

    int correlativo = 0;
    public DefaultTableModel modeloBack;

    /**
     * Creates new form frmGenerarOrden
     */
    public frmGenerarOrden(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicial();
    }

    private String nroPlaca = "";

    private void inicial() {
        txtModelo.setEnabled(false);
        txtColor.setEnabled(false);
        txtMarca.setEnabled(false);
        txtCostoRepuestos.setEnabled(false);
        txtCostoServicios.setEnabled(false);
        txtCostoTotalOrden.setEnabled(false);
        txtDueñoVeh.setEnabled(false);
        txtNumRepuestos.setEnabled(false);
        txtNumeroPlaca.setEnabled(false);
        txtNumServicios.setEnabled(false);
        btnAgregarServicio.setEnabled(false);
        btnBorrarServicio.setEnabled(false);
        btnRegistrarOrden.setEnabled(false);
        txtNroPlaca.setText("");
        txtModelo.setText("");
        txtMarca.setText("");
        txtColor.setText("");
        txtCostoRepuestos.setText("");
        txtCostoServicios.setText("");
        txtCostoTotalOrden.setText("");
        txtNumRepuestos.setText("");
        txtNumServicios.setText("");
        txtDueñoVeh.setText("");
        txtNumeroPlaca.setText("");
        jdInicio.setDate(null);
        jdInicio.setEnabled(false);
        iniciarTablaRepuestos();
        iniciarTablaServicios();
        //Tabla de backend
        //Nro de servicio
        //ModeloBack de los repuestos con dicho nroCorre
        //Por cada repuesto, verificar si:
        //Si el nro del repuesto back es IGUAL al mostrado en el front, se eliminará el repuesto.
        //Si el nro del repuesto back es MENOR al mostrado en el front, se actualizará la cantidad.
        modeloBack = new DefaultTableModel();
        modeloBack.addColumn("Nombre repuesto");
        modeloBack.addColumn("Cantidad");
        modeloBack.addColumn("N°");
    }

    private void iniciarTablaServicios() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("N°");
        modelo.addColumn("Nombre Serv");
        modelo.addColumn("Precio");
        tblServiciosOrden.setModel(modelo);
    }

    private void iniciarTablaRepuestos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre repuesto");
        modelo.addColumn("Precio");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Marca");
        tblRepuestosOrden.setModel(modelo);
    }

    private int repuestoRepetido(String nombre_repuesto) {
        DefaultTableModel modelo = (DefaultTableModel) tblRepuestosOrden.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombre = tblRepuestosOrden.getValueAt(i, 0).toString();
            if (nombre.equals(nombre_repuesto)) {
                return i;
            }
        }
        return -1;
    }

    private boolean servicioRepetido(String nombre_servicio) {
        DefaultTableModel modelo = (DefaultTableModel) tblServiciosOrden.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombre = tblServiciosOrden.getValueAt(i, 1).toString();
            if (nombre.equals(nombre_servicio)) {
                return true;
            }
        }
        return false;
    }

    private Object[] obtenerFila(DefaultTableModel modelo, int fila) {
        int columnas = modelo.getColumnCount();
        Object[] datos = new Object[columnas];

        for (int i = 0; i < columnas; i++) {
            datos[i] = modelo.getValueAt(fila, i);
        }
        return datos;
    }

    private void actualizarBarra(DefaultTableModel modeloS, DefaultTableModel modeloR) {
        int cantS = 0;
        int cantR = 0;
        double sumS = 0;
        double sumR = 0;
        for (int i = 0; i < modeloS.getRowCount(); i++) {
            double precio = Double.parseDouble(modeloS.getValueAt(i, 2).toString());
            sumS = sumS + precio;
            cantS++;
        }
        for (int i = 0; i < modeloR.getRowCount(); i++) {
            double precio = Double.parseDouble(modeloR.getValueAt(i, 1).toString());
            int cantidad = Integer.parseInt(modeloR.getValueAt(i, 2).toString());
            sumR = sumR + (precio * cantidad);
            cantR = cantR + cantidad;
        }
        txtNumServicios.setText(String.valueOf(cantS));
        txtNumRepuestos.setText(String.valueOf(cantR));
        txtCostoServicios.setText(String.valueOf(sumS));
        txtCostoRepuestos.setText(String.valueOf(sumR));
        txtCostoTotalOrden.setText(String.valueOf(sumR + sumS));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNroPlaca = new javax.swing.JTextField();
        btnBuscarV = new javax.swing.JButton();
        btnVehiculoPorCliente = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        btnSiguiente_veh = new javax.swing.JButton();
        btnCambiarVehi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAgregarServicio = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblServiciosOrden = new javax.swing.JTable();
        btnBorrarServicio = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnRegistrarOrden = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCostoServicios = new javax.swing.JTextField();
        txtCostoRepuestos = new javax.swing.JTextField();
        txtCostoTotalOrden = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        txtNumServicios = new javax.swing.JTextField();
        txtNumRepuestos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNumeroPlaca = new javax.swing.JTextField();
        txtDueñoVeh = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jdInicio = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRepuestosOrden = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REGISTRAR ORDEN");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Nro. Placa:");

        txtNroPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNroPlacaActionPerformed(evt);
            }
        });

        btnBuscarV.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnBuscarV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
        btnBuscarV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarVActionPerformed(evt);
            }
        });

        btnVehiculoPorCliente.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnVehiculoPorCliente.setText("VEHÍCULOS POR CLIENTE");
        btnVehiculoPorCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehiculoPorClienteActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Modelo:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Color:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Marca:");

        btnSiguiente_veh.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnSiguiente_veh.setText("SIGUIENTE");
        btnSiguiente_veh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguiente_vehActionPerformed(evt);
            }
        });

        btnCambiarVehi.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnCambiarVehi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cambiar1.png"))); // NOI18N
        btnCambiarVehi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarVehiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(jLabel4)
                            .addGap(12, 12, 12)
                            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(58, 58, 58)
                                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel2))
                            .addGap(30, 30, 30)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNroPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarV, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnVehiculoPorCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCambiarVehi, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSiguiente_veh, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtNroPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnBuscarV)))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnVehiculoPorCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSiguiente_veh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCambiarVehi))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnAgregarServicio.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnAgregarServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar2.png"))); // NOI18N
        btnAgregarServicio.setText("AGREGAR SERVICIO");
        btnAgregarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarServicioActionPerformed(evt);
            }
        });

        tblServiciosOrden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblServiciosOrden);

        btnBorrarServicio.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnBorrarServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delete23.png"))); // NOI18N
        btnBorrarServicio.setText("BORRAR SERVICIO");
        btnBorrarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarServicioActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(255, 61, 61));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Servicios");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(btnAgregarServicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBorrarServicio)
                .addGap(72, 72, 72))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBorrarServicio)
                    .addComponent(btnAgregarServicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrarOrden.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnRegistrarOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/generar.png"))); // NOI18N
        btnRegistrarOrden.setText("REGISTRAR ORDEN");
        btnRegistrarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarOrdenActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 61, 61));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Resumen de Orden");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 51, 51), null));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Costo Total de Servicios:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Costo Total de Repuestos:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("COSTO TOTAL DE ORDEN:");

        txtCostoServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoServiciosActionPerformed(evt);
            }
        });

        txtCostoTotalOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoTotalOrdenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCostoTotalOrden, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCostoServicios, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(txtCostoRepuestos))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCostoServicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCostoRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCostoTotalOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 51, 51), null));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Número de Servicios:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Número de Repuestos:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNumRepuestos))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtNumServicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtNumRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 51, 51), null));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Nro Placa:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Dueño:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNumeroPlaca, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(txtDueñoVeh))
                .addGap(40, 40, 40))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtNumeroPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtDueñoVeh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Fecha Inicio:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(127, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRegistrarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(224, 224, 224))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(15, 15, 15)
                .addComponent(btnRegistrarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 61, 61));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Registrar Orden");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        tblRepuestosOrden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblRepuestosOrden);

        jPanel11.setBackground(new java.awt.Color(255, 61, 61));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Repuestos Generales");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNroPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroPlacaActionPerformed

    private void txtCostoTotalOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoTotalOrdenActionPerformed

    }//GEN-LAST:event_txtCostoTotalOrdenActionPerformed

    private void btnAgregarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarServicioActionPerformed
        try {
            frmDetalleServicio frm = new frmDetalleServicio((Frame) SwingUtilities.getWindowAncestor(this), true);
            frm.setVisible(true);
            frm.setLocationRelativeTo(null);
            String nombre_serv = frm.getNombre_serv();
            float precio_serv = frm.getPrecio_serv();
            JTable tablaRep_det = frm.getRepuestos();
            if (nombre_serv.equals("") || precio_serv == -1 || tablaRep_det == null) {
                return;
            }
            if (servicioRepetido(nombre_serv)) {
                JOptionPane.showMessageDialog(this, "Ya tienes el servicio en la orden", "REPETIDO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Servicio agregado correctamente", "AGREGADO", JOptionPane.INFORMATION_MESSAGE);
                int co = correlativo + 1;
                correlativo = correlativo + 1;

                DefaultTableModel modeloServ_orden = (DefaultTableModel) tblServiciosOrden.getModel();
                modeloServ_orden.addRow(new Object[]{co, nombre_serv, precio_serv});
                DefaultTableModel modeloRep_orden = (DefaultTableModel) tblRepuestosOrden.getModel();
                DefaultTableModel modelo_detalle_rep = (DefaultTableModel) tablaRep_det.getModel();
                if (modeloRep_orden.getRowCount() == 0) { //agregar si no hay nada
                    for (int i = 0; i < modelo_detalle_rep.getRowCount(); i++) {
                        Object[] data = obtenerFila(modelo_detalle_rep, i);
                        modeloRep_orden.addRow(data);
                        String nom = modelo_detalle_rep.getValueAt(i, 0).toString();
                        int cant = Integer.parseInt(modelo_detalle_rep.getValueAt(i, 2).toString());
                        modeloBack.addRow(new Object[]{nom, cant, co});
                    }
                    actualizarBarra(modeloServ_orden, modeloRep_orden);

                } else { // agregar

                    for (int i = 0; i < modelo_detalle_rep.getRowCount(); i++) {
                        String nom_rep_detalle = (String) modelo_detalle_rep.getValueAt(i, 0);
                        int filaRepetido_repuesto = repuestoRepetido(nom_rep_detalle);
                        int mas_cant_rep = Integer.parseInt(modelo_detalle_rep.getValueAt(i, 2).toString());
                        if (filaRepetido_repuesto != -1) { // sumar repuesto
                            int nueva_cant = Integer.parseInt(modeloRep_orden.getValueAt(filaRepetido_repuesto, 2).toString()) + mas_cant_rep;
                            tblRepuestosOrden.setValueAt((nueva_cant), filaRepetido_repuesto, 2);
                        } else { // agregar en la tabla repuesto
                            modeloRep_orden.addRow(obtenerFila(modelo_detalle_rep, i));
                        }
                        modeloBack.addRow(new Object[]{nom_rep_detalle, mas_cant_rep, co});
                    }
                    actualizarBarra(modeloServ_orden, modeloRep_orden);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarServicioActionPerformed

    private void btnVehiculoPorClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehiculoPorClienteActionPerformed
        try {
            frmVehiculiPorCliente f = new frmVehiculiPorCliente(null, true);
            f.setVisible(true);
            f.setLocationRelativeTo(null);
            nroPlaca = f.getNroPlaca();
            txtNroPlaca.setText(nroPlaca);
            txtColor.setText(f.getColor());
            txtMarca.setText(f.getMarca());
            txtModelo.setText(f.getModelo());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnVehiculoPorClienteActionPerformed

    private void btnBuscarVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVActionPerformed
        try {
            if (txtNroPlaca.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el numero de placa del vehiculo para buscarlo", "BÚSQUEDA", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String nroPlaca = txtNroPlaca.getText().trim();
            ResultSet rs = new Vehiculo().obtenerVehiculoPorNroPlaca(nroPlaca);
            while (rs.next()) {
                txtColor.setText(rs.getString("color"));
                txtModelo.setText(rs.getString("modelo"));
                txtMarca.setText(rs.getString("nombre_marca"));
            }
            if (txtColor.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "El vehiculo de N°placa: " + nroPlaca + " no esta registrado", "BÚSQUEDA", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarVActionPerformed

    private void btnSiguiente_vehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguiente_vehActionPerformed
        try {
            if (txtNroPlaca.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Debe escribir la placa para identificarla", "PARAMETRO FALTANTE", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ResultSet rs = new Vehiculo().obtenerVehiculoPorNroPlaca(txtNroPlaca.getText());
            ResultSet rsC = null;
            while (rs.next()) {
                rsC = new Cliente().obtenerClienteID(Integer.parseInt(rs.getString("cliente_id")));
            }
            while (rsC.next()) {
                txtDueñoVeh.setText(rsC.getString("nombres"));
            }
            nroPlaca = txtNroPlaca.getText();
            txtNroPlaca.setEnabled(false);
            btnSiguiente_veh.setEnabled(false);
            btnBuscarV.setEnabled(false);
            btnVehiculoPorCliente.setEnabled(false);
            txtNumeroPlaca.setText(nroPlaca);
            btnAgregarServicio.setEnabled(true);
            btnBorrarServicio.setEnabled(true);
            btnRegistrarOrden.setEnabled(true);
            jdInicio.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSiguiente_vehActionPerformed

    private void btnCambiarVehiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarVehiActionPerformed
        nroPlaca = "";
        txtColor.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtNroPlaca.setEnabled(true);
        btnSiguiente_veh.setEnabled(true);
        btnBuscarV.setEnabled(true);
        btnVehiculoPorCliente.setEnabled(true);
        txtNumeroPlaca.setText("");
    }//GEN-LAST:event_btnCambiarVehiActionPerformed

    private void btnBorrarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarServicioActionPerformed
        // 1. Validar que haya una fila seleccionada
        int fila = tblServiciosOrden.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un servicio para borrar",
                    "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DefaultTableModel modeloS = (DefaultTableModel) tblServiciosOrden.getModel();
            int corr_S = Integer.parseInt(tblServiciosOrden.getValueAt(fila, 0).toString());
            DefaultTableModel modeloR = (DefaultTableModel) tblRepuestosOrden.getModel();

            // Recorrer modeloBack de atrás hacia adelante para evitar problemas al eliminar
            for (int i = modeloBack.getRowCount() - 1; i >= 0; i--) {
                int corr_R = Integer.parseInt(modeloBack.getValueAt(i, 2).toString());

                if (corr_R == corr_S) {
                    String nom_RB = modeloBack.getValueAt(i, 0).toString();
                    int cantidad_B = Integer.parseInt(modeloBack.getValueAt(i, 1).toString());

                    // Buscar y actualizar en la tabla de repuestos del front
                    for (int j = modeloR.getRowCount() - 1; j >= 0; j--) {
                        String nom_RF = modeloR.getValueAt(j, 0).toString();

                        if (nom_RB.equals(nom_RF)) {
                            int cantidad_F = Integer.parseInt(modeloR.getValueAt(j, 2).toString());

                            if (cantidad_B == cantidad_F) {
                                // Eliminar completamente el repuesto
                                modeloR.removeRow(j);
                            } else {
                                // Restar cantidad
                                modeloR.setValueAt(cantidad_F - cantidad_B, j, 2);
                            }
                            break; // Salir del bucle interno una vez encontrado
                        }
                    }

                    // Eliminar de modeloBack
                    modeloBack.removeRow(i);
                }
            }

            // 3. Eliminar el servicio
            modeloS.removeRow(fila);

            // 4. Actualizar totales
            actualizarBarra(modeloS, modeloR);

            JOptionPane.showMessageDialog(this, "Servicio borrado correctamente",
                    "BORRADO", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al borrar servicio: " + e.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBorrarServicioActionPerformed

    private void txtCostoServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoServiciosActionPerformed

    }//GEN-LAST:event_txtCostoServiciosActionPerformed

    private void btnRegistrarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarOrdenActionPerformed
        if (jdInicio.getDate() != null) {
            try {
                DefaultTableModel modeloS = (DefaultTableModel) tblServiciosOrden.getModel();
                if(modeloS.getRowCount()==0){
                    JOptionPane.showMessageDialog(rootPane, "Debe agregar como mínimo un servicio", "AGREGAR SERVICIO", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                DefaultTableModel modeloR = (DefaultTableModel) tblRepuestosOrden.getModel();
                for (int i = 0; i < modeloR.getRowCount(); i++) {
                    String nombreR = modeloR.getValueAt(i, 0).toString();
                    int cantidad = Integer.parseInt(modeloR.getValueAt(i, 2).toString());
                    ResultSet rsR = new Repuesto().buscarRepuestoEstricto(nombreR);
                    int stock = 0;
                    while (rsR.next()) {
                        stock = rsR.getInt("stock");
                    }
                    if (cantidad > stock) {
                        JOptionPane.showMessageDialog(rootPane, "El stock de " + nombreR + " no es suficiente para la orden", "STOCK INSUFICIENTE", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                LocalDate fechaI = jdInicio.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                new Orden().registrarOrden2(modeloR, modeloS, txtNumeroPlaca.getText(), fechaI);
                tblRepuestosOrden.removeAll();
                tblServiciosOrden.removeAll();
                modeloBack.setRowCount(0);
                inicial();
                JOptionPane.showMessageDialog(rootPane, "Registro Exitoso", "REGISTRO", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Error al registrar orden: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Debe colocar las fechas necesarias", "FECHAS FALTANTES", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarOrdenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarServicio;
    private javax.swing.JButton btnBorrarServicio;
    private javax.swing.JButton btnBuscarV;
    private javax.swing.JButton btnCambiarVehi;
    private javax.swing.JButton btnRegistrarOrden;
    private javax.swing.JButton btnSiguiente_veh;
    private javax.swing.JButton btnVehiculoPorCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdInicio;
    private javax.swing.JTable tblRepuestosOrden;
    private javax.swing.JTable tblServiciosOrden;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtCostoRepuestos;
    private javax.swing.JTextField txtCostoServicios;
    private javax.swing.JTextField txtCostoTotalOrden;
    private javax.swing.JTextField txtDueñoVeh;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtNroPlaca;
    private javax.swing.JTextField txtNumRepuestos;
    private javax.swing.JTextField txtNumServicios;
    private javax.swing.JTextField txtNumeroPlaca;
    // End of variables declaration//GEN-END:variables
}
