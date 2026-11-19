/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import datos.JuegoDAO;
import modelo.Juego;

/**
 *
 * @author 2DAM
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    /**
     * Creates new form VentanaPrincipal
     */
    // Añadir ANTES de "public VentanaPrincipal() {" 
    private final JuegoDAO dao = new JuegoDAO();
    private JuegosTableModel modeloTabla;

    public VentanaPrincipal() {
        initComponents(); // generado por NetBeans — no borrar 
        configurarTabla(); // conecta el modelo y el renderer 
        configurarPopup(); // asocia el popup a la tabla 
        cargarDatos(); // carga los juegos de la BD 
        setLocationRelativeTo(null);// centra la ventana en pantalla 
    }
    
    public VentanaPrincipal() {
        initComponents();
    }

    private void configurarTabla() {
        // Asignamos nuestro modelo personalizado a la tabla 
        modeloTabla = new JuegosTableModel(new java.util.ArrayList<>());
        tablaJuegos.setModel(modeloTabla);
        // Aplicamos el renderer de colores a TODAS las columnas 
        ConsolaColorRenderer renderer = new ConsolaColorRenderer();
        for (int i = 0; i < tablaJuegos.getColumnCount(); i++) {
            tablaJuegos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        // Anchuras preferidas de cada columna 
        tablaJuegos.getColumnModel().getColumn(0).setPreferredWidth(35);
        tablaJuegos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaJuegos.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaJuegos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaJuegos.getColumnModel().getColumn(4).setPreferredWidth(50);
        tablaJuegos.getColumnModel().getColumn(5).setPreferredWidth(110);
        tablaJuegos.getColumnModel().getColumn(6).setPreferredWidth(100);
        // Doble clic en una fila abre el diálogo de edición 
        tablaJuegos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarSeleccionado();
                }
            }
        });
    }

    private void configurarPopup() {
        // Asociar el popup al JTable 
        tablaJuegos.setComponentPopupMenu(popupTabla);
        // Al hacer clic derecho, seleccionar la fila bajo el cursor 
        tablaJuegos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int fila = tablaJuegos.rowAtPoint(e.getPoint());
                if (fila >= 0) {
                    tablaJuegos.setRowSelectionInterval(fila, fila);
                }
            }
        });
    }
// Recarga todos los juegos de la BD y actualiza estadísticas 

    private void cargarDatos() {
        modeloTabla.setJuegos(dao.obtenerTodos());
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        int total = dao.contarTotal();
        int comp = dao.contarCompletados();
        double med = dao.puntuacionMedia();
        lblTotal.setText(" Total: " + total);
        lblCompletados.setText(" | Completados: " + comp);
        lblMedia.setText(" | Media: "
                + (total == 0 ? "–" : String.format("%.1f ", med)));
    

    ★ 
} 
private void editarSeleccionado() {
        int fila = tablaJuegos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Juego j = modeloTabla.getJuego(fila);
        DialogoJuego dlg = new DialogoJuego(this, j);
        dlg.setVisible(true);
        if (dlg.isGuardado()) {
            dao.actualizar(dlg.getJuego());
            cargarDatos();
        }
    }

    private void eliminarSeleccionado() {
        int fila = tablaJuegos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Juego j = modeloTabla.getJuego(fila);
        int r = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + j.getTitulo() + "\" de tu biblioteca?",
                "Confirmar eliminación", javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        if (r == javax.swing.JOptionPane.YES_OPTION) {
            dao.eliminar(j.getId());
            cargarDatos();
        }
    }

    // "+ Añadir" · mniAnyadir · popAnyadir 
    private void btnAnyadirActionPerformed(java.awt.event.ActionEvent evt) {
        DialogoJuego dlg = new DialogoJuego(this, null); // null = nuevo 
        dlg.setVisible(true);
        if (dlg.isGuardado()) {
            dao.insertar(dlg.getJuego());
            cargarDatos();
        }
    }
// mniEditar · popEditar 

    private void mniEditarActionPerformed(java.awt.event.ActionEvent evt) {
        editarSeleccionado();
    } // mniEliminar · popEliminar 

    private void mniEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        eliminarSeleccionado();
    }
// btnFiltrar 

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {
        String cons = (String) cmbConsola.getSelectedItem();
        String est = (String) cmbEstado.getSelectedItem();
        java.util.List<Juego> lista;
        if (!"Todas".equals(cons)) {
            lista = dao.obtenerPorConsola(cons);
        } else if (!"Todos".equals(est)) {
            lista = dao.obtenerPorEstado(est);
        } else {
            lista = dao.obtenerTodos();
        }
        modeloTabla.setJuegos(lista);
    }
// btnLimpiar 

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        cmbConsola.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        cargarDatos();
    }
// mniActualizar (F5) 

    private void mniActualizarActionPerformed(java.awt.event.ActionEvent evt) {
        cargarDatos();
    } // mniSalir 

    private void mniSalirActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupTabla = new javax.swing.JPopupMenu();
        popAnayadir = new javax.swing.JMenuItem();
        popEditar = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popEliminar = new javax.swing.JMenuItem();
        panelFiltros = new javax.swing.JPanel();
        JLabel1 = new javax.swing.JLabel();
        cmbConsola = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        btnFiltrar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnAnyadir = new javax.swing.JButton();
        tablaJuegos = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelEstadisticas = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        lblCompletados = new javax.swing.JLabel();
        lblMedia = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mniSalir = new javax.swing.JMenuItem();
        mnJuegos = new javax.swing.JMenu();
        mniAnyadir = new javax.swing.JMenuItem();
        mniEditar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mniEliminar = new javax.swing.JMenuItem();
        mnVista = new javax.swing.JMenu();
        mniActualizar = new javax.swing.JMenuItem();

        popAnayadir.setText("Añadir juego...");
        popupTabla.add(popAnayadir);

        popEditar.setText("Editar juego...");
        popupTabla.add(popEditar);
        popupTabla.add(jSeparator2);

        popEliminar.setText("Eliminar juego");
        popupTabla.add(popEliminar);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RetroVault-Biblioteca de Videojuegos Retro");
        setMinimumSize(new java.awt.Dimension(900, 600));

        panelFiltros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        JLabel1.setText("Consola");
        panelFiltros.add(JLabel1);

        cmbConsola.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "NES", "SNES", "Mega Drive", "Game Boy", "PlayStation", "Nintendo 64", "Game Gear", "Arcade" }));
        cmbConsola.addActionListener(this::cmbConsolaActionPerformed);
        panelFiltros.add(cmbConsola);

        jLabel1.setText("Estado");
        panelFiltros.add(jLabel1);

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pendiente", "Jugando", "Completado", "Abandonado" }));
        panelFiltros.add(cmbEstado);

        btnFiltrar.setText("Filtrar");
        panelFiltros.add(btnFiltrar);

        btnLimpiar.setText("Limpiar");
        panelFiltros.add(btnLimpiar);

        btnAnyadir.setText("Añadir");
        panelFiltros.add(btnAnyadir);

        getContentPane().add(panelFiltros, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(60, 60, 60));
        jTable1.setRowHeight(26);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaJuegos.setViewportView(jTable1);

        getContentPane().add(tablaJuegos, java.awt.BorderLayout.CENTER);

        panelEstadisticas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblTotal.setText("Total:0");
        panelEstadisticas.add(lblTotal);

        lblCompletados.setText("Completados:0");
        panelEstadisticas.add(lblCompletados);

        lblMedia.setText("Puntuación media:-");
        panelEstadisticas.add(lblMedia);

        getContentPane().add(panelEstadisticas, java.awt.BorderLayout.PAGE_END);

        jMenu1.setText("Archivo");

        mniSalir.setText("Salir");
        jMenu1.add(mniSalir);

        jMenuBar1.add(jMenu1);

        mnJuegos.setText("Juegos");

        mniAnyadir.setText("Añadir Juego");
        mnJuegos.add(mniAnyadir);

        mniEditar.setText("Editar juego");
        mniEditar.addActionListener(this::mniEditarActionPerformed);
        mnJuegos.add(mniEditar);
        mnJuegos.add(jSeparator1);

        mniEliminar.setText("Eliminar juego");
        mnJuegos.add(mniEliminar);

        jMenuBar1.add(mnJuegos);

        mnVista.setText("Vista");

        mniActualizar.setText("Actualizar");
        mnVista.add(mniActualizar);

        jMenuBar1.add(mnVista);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbConsolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbConsolaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbConsolaActionPerformed

    private void mniEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEditarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mniEditarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel1;
    private javax.swing.JButton btnAnyadir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cmbConsola;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCompletados;
    private javax.swing.JLabel lblMedia;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JMenu mnJuegos;
    private javax.swing.JMenu mnVista;
    private javax.swing.JMenuItem mniActualizar;
    private javax.swing.JMenuItem mniAnyadir;
    private javax.swing.JMenuItem mniEditar;
    private javax.swing.JMenuItem mniEliminar;
    private javax.swing.JMenuItem mniSalir;
    private javax.swing.JPanel panelEstadisticas;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JMenuItem popAnayadir;
    private javax.swing.JMenuItem popEditar;
    private javax.swing.JMenuItem popEliminar;
    private javax.swing.JPopupMenu popupTabla;
    private javax.swing.JScrollPane tablaJuegos;
    // End of variables declaration//GEN-END:variables
}
