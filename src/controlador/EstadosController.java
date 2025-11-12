/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Estados;
/**
 *
 * @author jaimes
 */
public class EstadosController {
    public void crearEstado(Estados e) {
        String sql = "INSERT INTO estado(nombre) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public void actualizarEstado(Estados e) {
        String sql = "UPDATE estado SET nombre=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public void eliminarEstado(int id) {
        String sql = "DELETE FROM estado WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<Estados> listarEstados() {
        List<Estados> lista = new ArrayList<>();
        String sql = "SELECT * FROM estado";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Estados e = new Estados();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                lista.add(e);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }
}
