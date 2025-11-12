/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Usuarios;
/**
 *
 * @author jaimes
 */
public class MenuFrame extends JFrame {
    private Usuarios usuario;

    public MenuFrame(Usuarios usuario) {
        this.usuario = usuario;
        setTitle("Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        String tituloMenu = (usuario.getRol() == 1) ? "Opciones Administrador" : "Opciones Usuario";
        JMenu menuOpciones = new JMenu(tituloMenu);

        ImageIcon iconTareas = new ImageIcon(getClass().getResource("/recursos/icons8-tasks-48.png"));
        Image imgTareas = iconTareas.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconUsuarios = new ImageIcon(getClass().getResource("/recursos/icons8-people-48.png"));
        Image imgUsuarios = iconUsuarios.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconCategorias = new ImageIcon(getClass().getResource("/recursos/icons8-categorize-30.png"));
        Image imgCategorias = iconCategorias.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconPrioridades = new ImageIcon(getClass().getResource("/recursos/icons8-priorities-60.png"));
        Image imgPrioridades = iconPrioridades.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconEstados = new ImageIcon(getClass().getResource("/recursos/icons8-states-48.png"));
        Image imgEstados = iconEstados.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconCambiar = new ImageIcon(getClass().getResource("/recursos/icons8-switch-50.png"));
        Image imgCambiar = iconCambiar.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconCerrar = new ImageIcon(getClass().getResource("/recursos/icons8-close-window-50.png"));
        Image imgCerrar = iconCerrar.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconAcerca = new ImageIcon(getClass().getResource("/recursos/icons8-about-48.png"));
        Image imgAcerca = iconAcerca.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconSprint = new ImageIcon(getClass().getResource("/recursos/icons8-calendar-48.png"));
        Image imgSprint = iconSprint.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        
        JMenuItem itemTareas = new JMenuItem("Tareas", new ImageIcon(imgTareas));
        itemTareas.addActionListener(e -> new TareasFrame(usuario).setVisible(true));
        menuOpciones.add(itemTareas);

        if (usuario.getRol() == 1) {
            JMenuItem itemUsuarios = new JMenuItem("Usuarios", new ImageIcon(imgUsuarios));
            itemUsuarios.addActionListener(e -> new UsuariosFrame().setVisible(true));
            menuOpciones.add(itemUsuarios);

            JMenuItem itemCategorias = new JMenuItem("Categorías", new ImageIcon(imgCategorias));
            itemCategorias.addActionListener(e -> new CategoriasFrame().setVisible(true));
            menuOpciones.add(itemCategorias);

            JMenuItem itemPrioridades = new JMenuItem("Prioridades", new ImageIcon(imgPrioridades));
            itemPrioridades.addActionListener(e -> new PrioridadesFrame().setVisible(true));
            menuOpciones.add(itemPrioridades);

            JMenuItem itemEstados = new JMenuItem("Estados", new ImageIcon(imgEstados));
            itemEstados.addActionListener(e -> new EstadosFrame().setVisible(true));
            menuOpciones.add(itemEstados);
            
            JMenuItem itemSprints = new JMenuItem("Sprints", new ImageIcon(imgSprint));
            itemSprints.addActionListener(e -> new SprintFrame().setVisible(true));
            menuOpciones.add(itemSprints);
        }

        menuOpciones.addSeparator();

        JMenuItem itemCambioUsuario = new JMenuItem("Cambiar de Usuario", new ImageIcon(imgCambiar));
        itemCambioUsuario.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        menuOpciones.add(itemCambioUsuario);

        JMenuItem itemSalir = new JMenuItem("Cerrar Aplicación", new ImageIcon(imgCerrar));
        itemSalir.addActionListener(e -> System.exit(0));
        menuOpciones.add(itemSalir);

        menuBar.add(menuOpciones);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de...", new ImageIcon(imgAcerca));
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistema de Gestión de Tareas\nVersión 1.0\nDesarrollado por Silvia Jaimes, Karen Velasquez y Camilo Paez"));
        menuAyuda.add(itemAcerca);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }

    private MenuFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Usuarios usuarioAdmin = new Usuarios();
        usuarioAdmin.setRol(1);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MenuFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
