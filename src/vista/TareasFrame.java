/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import controlador.TareasController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;
import modelo.Tareas;
import modelo.Usuarios;
import controlador.UsuariosController;
import java.util.Vector;

/**
 *
 * @author jaimes
 */
public class TareasFrame extends javax.swing.JFrame {
    private JTable table;
    private DefaultTableModel model;
    private TareasController controller = new TareasController();
    private Usuarios usuario;
    private Usuarios tareaSeleccionadaUsuario;

    public TareasFrame(Usuarios usuario) {
        this.usuario = usuario;
        setTitle("Gestión de Tareas");
        setSize(700, 400);
        setLayout(null);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Título", "Descripción", "Fecha Creación", "Fecha Vencimiento", "ID Categoría", "ID Estado", "ID Prioridad"});
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 650, 200);
        add(scroll);

        JButton btnCrear = new JButton("Crear");
        btnCrear.setBounds(20, 240, 100, 30);
        add(btnCrear);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(140, 240, 100, 30);
        add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(260, 240, 100, 30);
        add(btnEliminar);

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBounds(380, 240, 100, 30);
        add(btnRefrescar);

        btnRefrescar.addActionListener(e -> cargarTareas());

        btnCrear.addActionListener(e -> {
            Tareas t = mostrarDialogo(null);
            if (t != null) {
                int idTarea = controller.crearTarea(t);
                if (idTarea > 0 && tareaSeleccionadaUsuario != null) {
                    controller.asignarTareaAUsuario(idTarea, tareaSeleccionadaUsuario.getId());
                }
                cargarTareas();
            }
        });

        btnEditar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila >= 0) {
                Tareas t = new Tareas();
                t.setId((int) model.getValueAt(fila, 0));
                t.setTitulo((String) model.getValueAt(fila, 1));
                t.setDescripcion((String) model.getValueAt(fila, 2));
                t.setFechaCreacion((java.sql.Date) model.getValueAt(fila, 3));
                t.setFechaVencimiento((java.sql.Date) model.getValueAt(fila, 4));
                t.setIdCategoria((int) model.getValueAt(fila, 5));
                t.setIdEstado((int) model.getValueAt(fila, 6));
                t.setIdPrioridad((int) model.getValueAt(fila, 7));
                t = mostrarDialogo(t);
                if (t != null) controller.actualizarTarea(t);
                cargarTareas();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una tarea");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila >= 0) {
                int id = (int) model.getValueAt(fila, 0);
                controller.eliminarTarea(id);
                cargarTareas();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una tarea");
            }
        });

        cargarTareas();
    }

    private TareasFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void cargarTareas() {
        model.setRowCount(0);
        List<Tareas> lista = controller.listarTareasPorUsuario(usuario.getId(), usuario.getRol());
        for (Tareas t : lista) {
            model.addRow(new Object[]{
                t.getId(), t.getTitulo(), t.getDescripcion(),
                t.getFechaCreacion(), t.getFechaVencimiento(),
                t.getIdCategoria(), t.getIdEstado(), t.getIdPrioridad()
            });
        }
    }

    private Tareas mostrarDialogo(Tareas t) {
        UsuariosController usuariosController = new UsuariosController();
        List<Usuarios> listaUsuarios = usuariosController.listarUsuarios();

        JComboBox<Usuarios> cbUsuarios = new JComboBox<>(new Vector<>(listaUsuarios));

        // Personalizar cómo se muestran los nombres
        cbUsuarios.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Usuarios) {
                    setText(((Usuarios) value).getNombre());
                }
                return this;
            }
        });

        JTextField titulo = new JTextField();
        JTextField descripcion = new JTextField();
        JTextField fechaCreacion = new JTextField();
        JTextField fechaVencimiento = new JTextField();
        JTextField idCategoria = new JTextField();
        JTextField idEstado = new JTextField();
        JTextField idPrioridad = new JTextField();

        if (t != null) {
            titulo.setText(t.getTitulo());
            descripcion.setText(t.getDescripcion());
            fechaCreacion.setText(t.getFechaCreacion().toString());
            fechaVencimiento.setText(t.getFechaVencimiento().toString());
            idCategoria.setText(String.valueOf(t.getIdCategoria()));
            idEstado.setText(String.valueOf(t.getIdEstado()));
            idPrioridad.setText(String.valueOf(t.getIdPrioridad()));
        }

        Object[] campos = {
            "Usuario asignado:", cbUsuarios,
            "Título:", titulo,
            "Descripción:", descripcion,
            "Fecha Creación (yyyy-mm-dd):", fechaCreacion,
            "Fecha Vencimiento (yyyy-mm-dd):", fechaVencimiento,
            "ID Categoría:", idCategoria,
            "ID Estado:", idEstado,
            "ID Prioridad:", idPrioridad
        };

        int opcion = JOptionPane.showConfirmDialog(null, campos, "Tarea", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            Tareas tarea = new Tareas();
            if (t != null) tarea.setId(t.getId());
            tarea.setTitulo(titulo.getText());
            tarea.setDescripcion(descripcion.getText());
            tarea.setFechaCreacion(java.sql.Date.valueOf(fechaCreacion.getText()));
            tarea.setFechaVencimiento(java.sql.Date.valueOf(fechaVencimiento.getText()));
            tarea.setIdCategoria(Integer.parseInt(idCategoria.getText()));
            tarea.setIdEstado(Integer.parseInt(idEstado.getText()));
            tarea.setIdPrioridad(Integer.parseInt(idPrioridad.getText()));

            // Guardamos el usuario seleccionado en el campo "putUser" del objeto
            tarea.setIdCategoria(Integer.parseInt(idCategoria.getText())); // ya estaba
            tarea.setIdEstado(Integer.parseInt(idEstado.getText())); // ya estaba

            tareaSeleccionadaUsuario = (Usuarios) cbUsuarios.getSelectedItem(); // <<< guarda el usuario elegido

            return tarea;
        }
        return null;
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TareasFrame.class.getName());

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
        java.awt.EventQueue.invokeLater(() -> new TareasFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
