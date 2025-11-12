/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Tareas;
/**
 *
 * @author jaimes
 */
public class TareasController {
    public int crearTarea(Tareas t) {
        String sql = "INSERT INTO tarea(titulo,descripcion,fecha_creacion,fecha_vencimiento,idcategoria,idestado,idprioridad) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setDate(3, t.getFechaCreacion());
            ps.setDate(4, t.getFechaVencimiento());
            ps.setInt(5, t.getIdCategoria());
            ps.setInt(6, t.getIdEstado());
            ps.setInt(7, t.getIdPrioridad());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void actualizarTarea(Tareas t) {
        String sql = "UPDATE tarea SET titulo=?, descripcion=?, fecha_creacion=?, fecha_vencimiento=?, idcategoria=?, idestado=?, idprioridad=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setDate(3, t.getFechaCreacion());
            ps.setDate(4, t.getFechaVencimiento());
            ps.setInt(5, t.getIdCategoria());
            ps.setInt(6, t.getIdEstado());
            ps.setInt(7, t.getIdPrioridad());
            ps.setInt(8, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarTarea(int id) {
        String sqlUsuarioTarea = "DELETE FROM usuario_tarea WHERE idtarea = ?";
        String sqlTarea = "DELETE FROM tarea WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlUsuarioTarea);
             PreparedStatement ps2 = conn.prepareStatement(sqlTarea)) {

            ps1.setInt(1, id);
            ps1.executeUpdate();

            ps2.setInt(1, id);
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tareas> listarTareas() {
        List<Tareas> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarea";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tareas t = new Tareas();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFechaCreacion(rs.getDate("fecha_creacion"));
                t.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                t.setIdCategoria(rs.getInt("idcategoria"));
                t.setIdEstado(rs.getInt("idestado"));
                t.setIdPrioridad(rs.getInt("idprioridad"));
                lista.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
    
    public List<Tareas> listarTareasPorUsuario(int idUsuario, int rol) {
        List<Tareas> lista = new ArrayList<>();
        String sql;

        if (rol == 1) {
            sql = "SELECT * FROM tarea";
        } else {
            sql = "SELECT t.* FROM tarea t " +
                  "LEFT JOIN usuario_tarea ut ON t.id = ut.idtarea " +
                  "WHERE ut.idusuario = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (rol != 1) {
                ps.setInt(1, idUsuario);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tareas t = new Tareas();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFechaCreacion(rs.getDate("fecha_creacion"));
                t.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                t.setIdCategoria(rs.getInt("idcategoria"));
                t.setIdEstado(rs.getInt("idestado"));
                t.setIdPrioridad(rs.getInt("idprioridad"));
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public void asignarTareaAUsuario(int idTarea, int idUsuario) {
        String sql = "INSERT INTO usuario_tarea(idtarea, idusuario) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTarea);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
