/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
/**
 *
 * @author jaimes
 */
public class InicioFrame extends JFrame {
    
    public InicioFrame() {
        setTitle("Bienvenidos al To-Do List");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---- Panel Superior ----
        JLabel titulo = new JLabel("¡Bienvenidos al Sistema To-Do List!", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JLabel icono = new JLabel();
        ImageIcon logo = new ImageIcon(getClass().getResource("/recursos/icons8-tasks-48.png"));
        Image img = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        icono.setIcon(new ImageIcon(img));
        icono.setHorizontalAlignment(SwingConstants.CENTER);
        add(icono, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Opciones principales");
        
        ImageIcon iconLogin = new ImageIcon(getClass().getResource("/recursos/icons8-login-48.png"));
        Image imgLogin = iconLogin.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon iconAcerca = new ImageIcon(getClass().getResource("/recursos/icons8-about-48.png"));
        Image imgAcerca = iconAcerca.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);   
        
        JMenuItem itemLogin = new JMenuItem("Iniciar Sesión", new ImageIcon(imgLogin));
        itemLogin.addActionListener((ActionEvent e) -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        menuOpciones.add(itemLogin);

        JMenuItem itemAcerca = new JMenuItem("Acerca de...", new ImageIcon(imgAcerca));
        itemAcerca.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this,
                    "Sistema de Gestión de Tareas\nVersión 1.0\nDesarrollado por Silvia Jaimes, Karen Velásquez y Camilo Páez",
                    "Acerca de", JOptionPane.INFORMATION_MESSAGE);
        });
        menuOpciones.add(itemAcerca);

        menuBar.add(menuOpciones);
        setJMenuBar(menuBar);

        JLabel pie = new JLabel("© 2025 To-Do List | Todos los derechos reservados", SwingConstants.CENTER);
        pie.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        pie.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(pie, BorderLayout.SOUTH);
    }
    
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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new InicioFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
