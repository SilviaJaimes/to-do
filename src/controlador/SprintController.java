package controlador;

import db.DBConnection;
import modelo.Sprint;
import modelo.SprintTarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Tareas;

public class SprintController {

    public boolean crearSprint(Sprint sprint) {
        String sql = "INSERT INTO sprint (nombre, fecha_inicio, fecha_fin, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sprint.getNombre());
            ps.setDate(2, new java.sql.Date(sprint.getFechaInicio().getTime()));
            ps.setDate(3, new java.sql.Date(sprint.getFechaFin().getTime()));
            ps.setString(4, sprint.getDescripcion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Sprint> listarSprints() {
        List<Sprint> lista = new ArrayList<>();
        String sql = "SELECT * FROM sprint ORDER BY fecha_inicio DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Sprint s = new Sprint(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_inicio"),
                    rs.getDate("fecha_fin"),
                    rs.getString("descripcion")
                );
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean agregarTareaASprint(SprintTarea st) {
        String sql = "INSERT INTO sprint_tarea (idsprint, idtarea) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, st.getIdSprint());
            ps.setInt(2, st.getIdTarea());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Tareas> listarTareasPorFiltro(Integer idUsuario, Integer idSprint) {
        List<Tareas> lista = new ArrayList<>();
        String sql = """
            SELECT t.id, t.titulo, t.descripcion
            FROM tarea t
            LEFT JOIN sprint_tarea st ON st.idtarea = t.id
            WHERE 1=1
        """;
        if(idSprint != null) {
            sql += " AND st.idsprint = ? ";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if(idSprint != null) {
                ps.setInt(index++, idSprint);
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Tareas t = new Tareas();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));
                lista.add(t);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Tareas> listarTareasNoAsignadas(Integer idSprint) {
        List<Tareas> lista = new ArrayList<>();
        String sql = """
            SELECT t.id, t.titulo, t.descripcion
            FROM tarea t
            WHERE t.id NOT IN (SELECT idtarea FROM sprint_tarea WHERE idsprint = ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSprint);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Tareas t = new Tareas();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));
                lista.add(t);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Tareas> listarTareasNoAsignadasConUsuario(Integer idSprint) {
        List<Tareas> lista = new ArrayList<>();
        String sql = """
            SELECT t.id, t.titulo, u.nombre AS nombre_usuario
            FROM tarea t
            LEFT JOIN usuario_tarea ut ON t.id = ut.idtarea
            LEFT JOIN usuario u ON ut.idusuario = u.id
            WHERE t.id NOT IN (SELECT idtarea FROM sprint_tarea WHERE idsprint = ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSprint);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Tareas t = new Tareas();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                String nombreUsuario = rs.getString("nombre_usuario");
                if(nombreUsuario != null && !nombreUsuario.isEmpty()){
                    t.setDescripcion(nombreUsuario); 
                } else {
                    t.setDescripcion("Sin asignar");
                }
                lista.add(t);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    public List<Tareas> listarTareasPorSprint(Integer idSprint) {
        List<Tareas> lista = new ArrayList<>();
        String sql = """
            SELECT t.id, t.titulo, t.descripcion, u.nombre AS nombre_usuario
            FROM tarea t
            LEFT JOIN usuario_tarea ut ON t.id = ut.idtarea
            LEFT JOIN usuario u ON ut.idusuario = u.id
            LEFT JOIN sprint_tarea st ON t.id = st.idtarea
            WHERE st.idsprint = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSprint);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Tareas t = new Tareas();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion")); 
                String usuario = rs.getString("nombre_usuario");
                t.setNombreUsuario(usuario != null ? usuario : "Sin asignar"); 
                lista.add(t);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean eliminarTareaDelSprint(int idSprint, int idTarea) {
        String sql = "DELETE FROM sprint_tarea WHERE idsprint = ? AND idtarea = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSprint);
            ps.setInt(2, idTarea);
            int afectadas = ps.executeUpdate();
            return afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Tareas> listarTareasPorSprintYUsuario(int idSprint, String usuario) {
        if(usuario == null || usuario.isEmpty()) {
            return listarTareasPorSprint(idSprint);
        }
        
        return listarTareasPorSprint(idSprint).stream()
            .filter(t -> t.getNombreUsuario().equals(usuario))
            .collect(Collectors.toList());
    }
    
    public List<String> listarUsuariosPorSprint(int idSprint) {
        List<Tareas> tareas = listarTareasPorSprint(idSprint); 
        if(tareas == null || tareas.isEmpty()) return List.of(); 
        return tareas.stream()
                     .map(Tareas::getNombreUsuario)
                     .filter(nombre -> nombre != null && !nombre.isEmpty())
                     .distinct()
                     .sorted()
                     .collect(Collectors.toList());
    }
}