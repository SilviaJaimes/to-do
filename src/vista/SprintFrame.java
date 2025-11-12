package vista;

import controlador.SprintController;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import modelo.Sprint;
import modelo.SprintTarea;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import modelo.Usuarios;
import modelo.Tareas;

public class SprintFrame extends JFrame {
    private SprintController sprintController = new SprintController();
    private JComboBox<String> cbSprints;
    private JButton btnAgregarTarea;
    private JButton btnCrearSprint;
    private JTable tablaTareas;
    private List<Sprint> listaSprints;
    private JLabel lblTituloSprint;
    private JComboBox<String> cbUsuarios;

    public SprintFrame() {
        setTitle("Gestión de Sprints");
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarEventosYDatos();
    }

    private void inicializarEventosYDatos() {
        JPanel panelBotones = new JPanel();
        cbSprints = new JComboBox<>();
        btnCrearSprint = new JButton("Crear Sprint");
        btnAgregarTarea = new JButton("Agregar Tarea al Sprint");
        JButton btnEliminarTarea = new JButton("Eliminar Tarea del Sprint");

        panelBotones.add(btnCrearSprint);
        panelBotones.add(btnAgregarTarea);
        panelBotones.add(btnEliminarTarea); 
        panelBotones.add(new JLabel("Selecciona un Sprint:"));
        panelBotones.add(cbSprints);
        cbUsuarios = new JComboBox<>();
        panelBotones.add(new JLabel("Filtrar por Usuario:"));
        panelBotones.add(cbUsuarios);
        
        btnEliminarTarea.addActionListener(e -> {
            int fila = tablaTareas.getSelectedRow();
            if(fila >= 0){
                int idTarea = (int) tablaTareas.getValueAt(fila, 0); // ID de la tarea
                int indexSprint = cbSprints.getSelectedIndex();
                if(indexSprint >= 0){
                    int idSprint = listaSprints.get(indexSprint).getId();
                    int confirmar = JOptionPane.showConfirmDialog(this,
                            "¿Desea eliminar esta tarea del sprint?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);
                    if(confirmar == JOptionPane.YES_OPTION){
                        boolean exito = sprintController.eliminarTareaDelSprint(idSprint, idTarea);
                        if(exito){
                            JOptionPane.showMessageDialog(this, "Tarea eliminada del sprint");
                            cargarTablaTareas(idSprint);
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al eliminar la tarea");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una tarea de la tabla");
            }
        });

        lblTituloSprint = new JLabel("", SwingConstants.CENTER);
        lblTituloSprint.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        lblTituloSprint.setAlignmentX(CENTER_ALIGNMENT); 

        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.add(panelBotones);
        panelNorte.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panelNorte.add(lblTituloSprint);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelNorte, BorderLayout.NORTH);

        tablaTareas = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        btnCrearSprint.addActionListener(e -> abrirDialogCrearSprint());
        btnAgregarTarea.addActionListener(e -> abrirDialogAgregarTarea());

        cargarSprints();

        cbSprints.addActionListener(e -> {
            int index = cbSprints.getSelectedIndex();
            if(index >= 0) {
                Integer idSprint = listaSprints.get(index).getId();
                cargarTablaTareas(idSprint);
                cargarUsuariosPorSprint(idSprint);
            }
        });
        cbUsuarios.addActionListener(e -> {
            int indexSprint = cbSprints.getSelectedIndex();
            if(indexSprint >= 0){
                int idSprint = listaSprints.get(indexSprint).getId();
                cargarTablaTareas(idSprint);
                cargarUsuariosPorSprint(idSprint);
            }
        });
    }

    private void cargarSprints() {
        cbSprints.removeAllItems();
        listaSprints = sprintController.listarSprints();
        for (Sprint s : listaSprints) {
            cbSprints.addItem(s.getNombre());
        }
        if(!listaSprints.isEmpty()) {
            cargarTablaTareas(listaSprints.get(0).getId());
        }
    }

    private void cargarTablaTareas(Integer idSprint) {
        if(idSprint == null) return;

        Sprint sprintSeleccionado = listaSprints.stream()
                .filter(s -> s.getId() == idSprint)
                .findFirst().orElse(null);
        if(sprintSeleccionado != null){
            lblTituloSprint.setText(sprintSeleccionado.getNombre());
        }

        List<Tareas> tareas = sprintController.listarTareasPorSprint(idSprint);

        // Filtrar por usuario
        String usuarioFiltro = (String) cbUsuarios.getSelectedItem();
        if(usuarioFiltro != null && !usuarioFiltro.equals("Todos")){
            tareas = tareas.stream()
                           .filter(t -> usuarioFiltro.equals(t.getNombreUsuario()))
                           .toList();
        }

        String[] columnas = {"ID", "Título", "Descripción", "Usuario"};
        Object[][] datos = new Object[tareas.size()][4];

        for (int i = 0; i < tareas.size(); i++) {
            Tareas t = tareas.get(i);
            datos[i][0] = t.getId();
            datos[i][1] = t.getTitulo();
            datos[i][2] = t.getDescripcion();
            datos[i][3] = t.getNombreUsuario();
        }

        tablaTareas.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));
    }
    
    private void cargarUsuariosPorSprint(Integer idSprint) {
        String usuarioSeleccionado = (String) cbUsuarios.getSelectedItem();

        cbUsuarios.removeAllItems();
        cbUsuarios.addItem("Todos"); 
        List<String> usuarios = sprintController.listarUsuariosPorSprint(idSprint);
        for(String u : usuarios){
            cbUsuarios.addItem(u);
        }

        if(usuarioSeleccionado != null && usuarios.contains(usuarioSeleccionado)){
            cbUsuarios.setSelectedItem(usuarioSeleccionado);
        } else {
            cbUsuarios.setSelectedIndex(0);
        }
    }

    private void abrirDialogCrearSprint() {
        JDialog dialog = new JDialog(this, "Crear Nuevo Sprint", true);
        dialog.setSize(330, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtNombre = new JTextField();
        txtNombre.setMaximumSize(new java.awt.Dimension(300, 25));

        JSpinner spFechaInicio = new JSpinner(new SpinnerDateModel());
        spFechaInicio.setMaximumSize(new java.awt.Dimension(300, 25));

        JSpinner spFechaFin = new JSpinner(new SpinnerDateModel());
        spFechaFin.setMaximumSize(new java.awt.Dimension(300, 25));

        JTextArea txtDescripcion = new JTextArea(4, 25);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setMaximumSize(new java.awt.Dimension(300, 80));

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNombre);

        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtNombre);
        panel.add(Box.createRigidArea(new Dimension(0,5)));

        JLabel lblFechaInicio = new JLabel("Fecha Inicio:");
        lblFechaInicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblFechaInicio);

        spFechaInicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(spFechaInicio);
        panel.add(Box.createRigidArea(new Dimension(0,5)));

        JLabel lblFechaFin = new JLabel("Fecha Fin:");
        lblFechaFin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblFechaFin);

        spFechaFin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(spFechaFin);
        panel.add(Box.createRigidArea(new Dimension(0,5)));

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDescripcion);
        
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scrollDesc);
        panel.add(Box.createRigidArea(new Dimension(0,10)));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            Date inicio = (Date) spFechaInicio.getValue();
            Date fin = (Date) spFechaFin.getValue();
            String desc = txtDescripcion.getText().trim();

            if(nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio");
                return;
            }

            Sprint sprint = new Sprint();
            sprint.setNombre(nombre);
            sprint.setFechaInicio(inicio);
            sprint.setFechaFin(fin);
            sprint.setDescripcion(desc);

            boolean exito = sprintController.crearSprint(sprint);
            if(exito){
                JOptionPane.showMessageDialog(dialog, "Tarea agregada al sprint");

                int indexSprint = cbSprints.getSelectedIndex();
                if(indexSprint >= 0){
                    int idSprint = listaSprints.get(indexSprint).getId();
                    cargarTablaTareas(idSprint);
                }

                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al agregar la tarea");
            }
        });

        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btnGuardar);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void abrirDialogAgregarTarea() {
        JDialog dialog = new JDialog(this, "Agregar Tarea al Sprint", true);
        dialog.setSize(250, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Sprint> cbSprintDialog = new JComboBox<>();
        for(Sprint s : listaSprints){
            cbSprintDialog.addItem(s);
        }
        cbSprintDialog.setMaximumSize(new java.awt.Dimension(300, 25));

        cbSprintDialog.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Sprint s) setText(s.getNombre());
                return this;
            }
        });

        JComboBox<Tareas> cbTareaDialog = new JComboBox<>();
        cbTareaDialog.setMaximumSize(new java.awt.Dimension(300, 25));

        if(cbSprintDialog.getItemCount() > 0){
            Sprint primerSprint = (Sprint) cbSprintDialog.getItemAt(0);
            List<Tareas> tareasDisponibles = sprintController.listarTareasNoAsignadasConUsuario(primerSprint.getId());
            for(Tareas t : tareasDisponibles) cbTareaDialog.addItem(t);

            cbTareaDialog.setRenderer(new DefaultListCellRenderer(){
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if(value instanceof Tareas t) setText(t.getTitulo() + " - " + t.getDescripcion());
                    return this;
                }
            });
        }

        cbSprintDialog.addActionListener(e -> {
            Sprint sprintSeleccionado = (Sprint) cbSprintDialog.getSelectedItem();
            cbTareaDialog.removeAllItems();
            if(sprintSeleccionado != null){
                List<Tareas> tareasDisponibles = sprintController.listarTareasNoAsignadasConUsuario(sprintSeleccionado.getId());
                for(Tareas t : tareasDisponibles) cbTareaDialog.addItem(t);
            }
        });

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregar.addActionListener(e -> {
            Sprint sprintSeleccionado = (Sprint) cbSprintDialog.getSelectedItem();
            Tareas tareaSeleccionada = (Tareas) cbTareaDialog.getSelectedItem();
            if(sprintSeleccionado != null && tareaSeleccionada != null){
                SprintTarea st = new SprintTarea(sprintSeleccionado.getId(), tareaSeleccionada.getId());
                boolean exito = sprintController.agregarTareaASprint(st);
                if(exito){
                    JOptionPane.showMessageDialog(dialog, "Tarea agregada al sprint");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al agregar la tarea");
                }
            }
        });

        JLabel lblSeleccionarSprint = new JLabel("Selecciona Sprint:");
        lblSeleccionarSprint.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSeleccionarSprint);

        cbSprintDialog.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cbSprintDialog);
        panel.add(Box.createRigidArea(new Dimension(0,5)));

        JLabel lblSeleccionarTarea = new JLabel("Selecciona Tarea:");
        lblSeleccionarTarea.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSeleccionarTarea);

        cbTareaDialog.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cbTareaDialog);
        panel.add(Box.createRigidArea(new Dimension(0,10)));

        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btnAgregar);

        dialog.add(panel);
        dialog.setVisible(true);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
