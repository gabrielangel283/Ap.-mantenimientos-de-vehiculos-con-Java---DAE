package Capa_Presentacion;

import Capa_Logica.Cliente;
import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lope
 */
public class frmMantCliente extends javax.swing.JInternalFrame {

    Cliente objCliente = new Cliente();

    public frmMantCliente() {
        initComponents();
        listarClientes();
        listarTotal();
        txtID.setEnabled(false);
    }

    private void listarClientes() {
        ResultSet rs;
        String estado;
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("cliente_ID");
        model.addColumn("nombres");
        model.addColumn("apellidos");
        model.addColumn("num_doc");
        model.addColumn("telefono");
        model.addColumn("f_registro");
        model.addColumn("tipo");
        model.addColumn("estado");

        try {
            rs = objCliente.listarClientes();
            while (rs.next()) {
                if (rs.getBoolean("estado")) {
                    estado = "Activo";
                } else {
                    estado = "Inactivo";
                }
                model.addRow(new Object[]{
                    rs.getInt("cliente_id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("num_doc"),
                    rs.getString("telefono"),
                    rs.getString("fecha_registro"),
                    rs.getString("tipo"),
                    estado
                });
            }
            jTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al llenar la tabla:" + e.getMessage());
        }
    }

    private int llenarTabla(ResultSet rs) {
        int contadorFilas = 0; 
        String estado;
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("cliente_ID");
        model.addColumn("nombres");
        model.addColumn("apellidos");
        model.addColumn("num_doc");
        model.addColumn("telefono");
        model.addColumn("f_registro");
        model.addColumn("tipo");
        model.addColumn("estado");

        try {
            while (rs.next()) {
                estado = rs.getBoolean("estado") ? "Activo" : "Inactivo";
                model.addRow(new Object[]{
                    rs.getInt("cliente_id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("num_doc"),
                    rs.getString("telefono"),
                    rs.getString("fecha_registro"),
                    rs.getString("tipo"),
                    estado
                });
                contadorFilas++; 
            }
            jTable.setModel(model);

            return contadorFilas;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al llenar la tabla: " + e.getMessage());
            return 0; 
        }
    }

    private void cargarClienteEnCampos(int clienteID) {
        ResultSet rs = null;
        try {
            rs = objCliente.obtenerClienteID(clienteID);
            if (rs.next()) {
                txtID.setText(String.valueOf(clienteID));
                txtNombres.setText(rs.getString("nombres"));
                txtApellidos.setText(rs.getString("apellidos"));
                txtTelefono.setText(rs.getString("telefono"));
                txtDNI.setText(rs.getString("num_doc"));

                String tipoBD = rs.getString("tipo");
                cmbTipo.setSelectedItem(tipoBD.substring(0, 1).toUpperCase() + tipoBD.substring(1));

                boolean estadoActivo = rs.getBoolean("estado");
                actualizarEstadoUI(estadoActivo);

            } else {
                JOptionPane.showMessageDialog(this, "El cliente con ID " + clienteID + " no existe.");
                lblEstado.setText("");
                btnDarbaja.setText("DAR BAJA/ALTA");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cliente: " + e.getMessage());
        }
    }

    public void limpiarControles() {

        txtDNI.setText("");
        txtApellidos.setText("");
        txtNombres.setText("");
        txtTelefono.setText("");
        txtID.setText("");

        txtID.setEnabled(false);
        txtNombres.setEnabled(true);
        txtApellidos.setEnabled(true);
        txtTelefono.setEnabled(true);

        cmbTipo.setSelectedIndex(0);
        cmbTipo.setEnabled(true);

        txtID.requestFocus();

        if (txtBuscarDNI != null) {
            txtBuscarDNI.setText("");
        }
        if (txtBuscarApellidos != null) {
            txtBuscarApellidos.setText("");
        }
    }

    private void listarTotal() {
        try {
            int tCLientes = objCliente.totalRegistros();
            lbl_total.setText(String.valueOf(tCLientes));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar total de clientes: " + e.getMessage());
        }
    }

    private boolean validarCampos() {

        if (cmbTipo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de cliente (Natural o Juridico).");
            cmbTipo.requestFocus();
            return false;
        }
        String tipoSeleccionado = cmbTipo.getSelectedItem().toString().toLowerCase();

        // 2. NOMBRES/RAZÓN SOCIAL
        final int MAX_NOMBRES = 30;

        if (txtNombres.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Nombres/Razón Social no puede estar vacío");
            txtNombres.requestFocus();
            return false;
        }

        if (tipoSeleccionado.equals("juridico")) {
            if (!txtNombres.getText().matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,\\-&()]+$")) {
                JOptionPane.showMessageDialog(this, "La Razón Social puede contener letras, números y caracteres básicos (., - &).");
                txtNombres.requestFocus();
                return false;
            }
        } else {
            // Nombres Naturales
            if (!txtNombres.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                JOptionPane.showMessageDialog(this, "El campo Nombres solo debe contener letras.");
                txtNombres.requestFocus();
                return false;
            }
        }

        if (txtNombres.getText().length() < 2 || txtNombres.getText().length() > MAX_NOMBRES) {
            JOptionPane.showMessageDialog(this, "El campo Nombres/Razón Social debe tener entre 2 y " + MAX_NOMBRES + " caracteres.");
            txtNombres.requestFocus();
            return false;
        }

        // 3. APELLIDOS
        final int MAX_APELLIDOS = 30;

        if (tipoSeleccionado.equals("natural")) {
            // Obligatorio para cliente Natural
            if (txtApellidos.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo Apellidos es obligatorio para un cliente Natural.");
                txtApellidos.requestFocus();
                return false;
            }

            // Restricciones para Apellidos
            if (!txtApellidos.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                JOptionPane.showMessageDialog(this, "El campo Apellidos solo debe contener letras.");
                txtApellidos.requestFocus();
                return false;
            }
            if (txtApellidos.getText().length() < 2 || txtApellidos.getText().length() > MAX_APELLIDOS) {
                JOptionPane.showMessageDialog(this, "El campo Apellidos debe tener entre 2 y " + MAX_APELLIDOS + " caracteres.");
                txtApellidos.requestFocus();
                return false;
            }
        }

        // 4. DOCUMENTO

        if (txtDNI.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Documento no puede estar vacío");
            txtDNI.requestFocus();
            return false;
        }

        // Restricción General
        if (!txtDNI.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El campo Documento solo debe contener dígitos numéricos.");
            txtDNI.requestFocus();
            return false;
        }

        if (tipoSeleccionado.equals("natural")) {
            // DNI: Exactamente 8 dígitos
            if (txtDNI.getText().length() != 8) {
                JOptionPane.showMessageDialog(this, "El Documento (DNI) para clientes Naturales debe tener exactamente 8 dígitos.");
                txtDNI.requestFocus();
                return false;
            }
            if (txtDNI.getText().matches("(.)\\1{7}")) {
                JOptionPane.showMessageDialog(this, "El Documento no puede tener todos los dígitos iguales.");
                txtDNI.requestFocus();
                return false;
            }
        } else if (tipoSeleccionado.equals("juridico")) {
            // RUC: Exactamente 11 dígitos
            if (txtDNI.getText().length() != 11) {
                JOptionPane.showMessageDialog(this, "El Documento (RUC) para clientes Jurídicos debe tener exactamente 11 dígitos.");
                txtDNI.requestFocus();
                return false;
            }
            // Validación de RUC (debe empezar con 10, 20, 15, etc.)
            if (!txtDNI.getText().startsWith("10") && !txtDNI.getText().startsWith("20")) {
                JOptionPane.showMessageDialog(this, "El RUC debe comenzar con 10 o 20.");
                txtDNI.requestFocus();
                return false;
            }
        }

        // 5. TELEFONO
        final int TEL_LENGTH = 9;

        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Teléfono no puede estar vacío");
            txtTelefono.requestFocus();
            return false;
        }
        if (txtTelefono.getText().length() != TEL_LENGTH) {
            JOptionPane.showMessageDialog(this, "El Teléfono debe tener exactamente " + TEL_LENGTH + " dígitos.");
            txtTelefono.requestFocus();
            return false;
        }
        if (!txtTelefono.getText().matches("\\d{" + TEL_LENGTH + "}")) {
            JOptionPane.showMessageDialog(this, "El Teléfono debe contener solo dígitos numéricos.");
            txtTelefono.requestFocus();
            return false;
        }
        if (txtTelefono.getText().matches("(.)\\1{" + (TEL_LENGTH - 1) + "}")) {
            JOptionPane.showMessageDialog(this, "El Teléfono no puede tener todos los dígitos iguales.");
            txtTelefono.requestFocus();
            return false;
        }

        return true;
    }

    private void actualizarEstadoUI(boolean activo) {
        if (activo) {
            lblEstado.setText("ACTIVO");
            lblEstado.setForeground(new Color(0, 153, 51));
            btnDarbaja.setText("Dar Baja");
        } else {
            lblEstado.setText("INACTIVO");
            lblEstado.setForeground(new Color(204, 0, 0)); 
            btnDarbaja.setText("Dar Alta");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        lbl_total = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtBuscarApellidos = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtBuscarDNI = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        cmbTipo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnDarbaja = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jPanel5.setBackground(new java.awt.Color(255, 61, 61));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Mantenimiento de Clientes");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("total registros: ");

        lbl_total.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_total.setText("------");

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 61, 61));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Lista de Clientes");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Nombre o Apellido:");

        txtBuscarApellidos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Documento:");

        txtBuscarDNI.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_total)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtBuscarDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(txtBuscarApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)))
                .addGap(12, 12, 12))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbl_total))
                .addGap(22, 22, 22))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("ID:");

        txtDNI.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Nombres:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Apellidos:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Documento:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Telefono:");

        txtNombres.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtApellidos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });

        txtID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        cmbTipo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Escoger", "Natural", "Juridico" }));
        cmbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoItemStateChanged(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Tipo:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Estado:");

        lblEstado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEstado.setText("----");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTelefono)
                            .addComponent(txtNombres)
                            .addComponent(txtApellidos)
                            .addComponent(txtDNI)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(22, 22, 22)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))))
                        .addContainerGap(27, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel12)
                    .addComponent(jLabel1))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnNuevo.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/registrarUsuario.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminarUsuario.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnModificar.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/modificarUsuario.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnDarbaja.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnDarbaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBajaUsuario.png"))); // NOI18N
        btnDarbaja.setText("Dar Baja/ Alta");
        btnDarbaja.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnDarbaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDarbajaActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/bote-de-basura.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/salir.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDarbaja, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDarbaja, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        try {
            if (btnNuevo.getText().equals("Nuevo")) {
                btnNuevo.setText("Guardar");
                limpiarControles();
                txtNombres.setEnabled(false);
                txtApellidos.setEnabled(false);
                cmbTipo.requestFocus();

            } else {
                if (!validarCampos()) {
                    return;
                }

                int opcion = JOptionPane.showConfirmDialog(this, "Esta seguro de registrar este cliente?", "Confirmar Registro", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {

                    String tipoCliente = cmbTipo.getSelectedItem().toString().toLowerCase();

                    objCliente.registrar(txtNombres.getText(),
                            txtApellidos.getText(),
                            txtTelefono.getText(),
                            txtDNI.getText(),
                            tipoCliente);

                    limpiarControles();
                    listarClientes();
                    listarTotal();
                    JOptionPane.showMessageDialog(this, "Cliente registrado correctamente");
                }
                
                btnNuevo.setText("Nuevo");   
                txtNombres.setEnabled(true);   
                txtApellidos.setEnabled(true);  
            }

        } catch (Exception e) {
            if (btnNuevo.getText().equals("Guardar")) {
                btnNuevo.setText("Nuevo");
                txtNombres.setEnabled(true);
                txtApellidos.setEnabled(true);
            }
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente para eliminar.");
            return;
        }

        try {
            int clienteID = Integer.parseInt(txtID.getText());
            boolean estaActivo = false;

            try (java.sql.ResultSet rs = objCliente.obtenerClienteID(clienteID)) {
                if (rs.next()) {
                    estaActivo = rs.getBoolean("estado");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: No se encontró el cliente con ID " + clienteID);
                    return;
                }
            }

            String mensajeConfirmacion;
            String tituloConfirmacion;

            if (estaActivo) {
                mensajeConfirmacion = "El cliente está ACTIVO. Al confirmar, pasará a estado INACTIVO por seguridad. ¿Desea continuar?";
                tituloConfirmacion = "Confirmar Desactivación";
            } else {
                mensajeConfirmacion = "¡El cliente ya está INACTIVO! Al confirmar, se ELIMINARÁ DEFINITIVAMENTE de la base de datos. ¿Desea continuar?";
                tituloConfirmacion = "Confirmar Eliminación Definitiva";
            }

            int opcion = JOptionPane.showConfirmDialog(this, mensajeConfirmacion, tituloConfirmacion, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                objCliente.eliminar(clienteID);

                limpiarControles();
                listarClientes();
                listarTotal();

                String mensajeExito = estaActivo ? "Cliente desactivado correctamente." : "Cliente eliminado definitivamente de la base de datos.";
                JOptionPane.showMessageDialog(this, mensajeExito);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error durante la operación de eliminación/baja: " + e.getMessage());
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        btnNuevo.setText("Nuevo");
        listarClientes();
        limpiarControles();
        listarTotal();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente para modificar.");
            return;
        }

        try {
            if (!validarCampos()) {
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de modificar este cliente?", "Confirmar Modificación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                
                String tipoCliente = cmbTipo.getSelectedItem().toString().toLowerCase();
                objCliente.modificar(
                        Integer.parseInt(txtID.getText()),
                        txtNombres.getText(),
                        txtApellidos.getText(),
                        txtTelefono.getText(),
                        txtDNI.getText(), 
                        tipoCliente
                );

                limpiarControles();
                listarClientes();

                JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDarbajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarbajaActionPerformed
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.");
            return;
        }

        try {
            int clienteID = Integer.parseInt(txtID.getText());
            objCliente.darBajaAlta(clienteID);
            cargarClienteEnCampos(clienteID);
            listarClientes();
            JOptionPane.showMessageDialog(this, "Estado del cliente modificado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cambiar estado: " + e.getMessage());
        }
    }//GEN-LAST:event_btnDarbajaActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        int id = (int) jTable.getValueAt(jTable.getSelectedRow(), 0);
        txtID.setText(String.valueOf(id));
        cargarClienteEnCampos(id);
    }//GEN-LAST:event_jTableMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String numDoc = txtBuscarDNI.getText();
        String filtroNombreApellido = txtBuscarApellidos.getText();
        ResultSet rs;
        int filasEncontradas = 0;

        if (numDoc.trim().isEmpty() && filtroNombreApellido.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un documento o un nombre/apellido para buscar");
            return;
        }

        try {
            rs = objCliente.buscarClientesPorFiltro(numDoc, filtroNombreApellido);

            filasEncontradas = llenarTabla(rs);

            if (filasEncontradas == 0) {
                JOptionPane.showMessageDialog(this, "No se encontraron clientes con el criterio de búsqueda.");
            } 

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de busqueda: " + e.getMessage());
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

            String tipoSeleccionado = cmbTipo.getSelectedItem().toString().toLowerCase();

            if (tipoSeleccionado.equals("escoger") || cmbTipo.getSelectedIndex() == 0) {
                txtNombres.setText("");
                txtNombres.setEnabled(false);
                txtApellidos.setText("");
                txtApellidos.setEnabled(false);
                return; 
            }
            txtNombres.setEnabled(true);
            txtNombres.requestFocus(); 

            if (tipoSeleccionado.equals("juridico")) {
                txtApellidos.setText("");      
                txtApellidos.setEnabled(false);
            } else if (tipoSeleccionado.equals("natural")) {
                txtApellidos.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
    }//GEN-LAST:event_txtTelefonoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnDarbaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtBuscarApellidos;
    private javax.swing.JTextField txtBuscarDNI;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
