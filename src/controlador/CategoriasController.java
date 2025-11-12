/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Categorias;
/**
 *
 * @author jaime
 */
public class CategoriasController {
    public void crearCategoria(Categorias c) {
        String sql = "INSERT INTO categoria(nombre) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizarCategoria(Categorias c) {
        String sql = "UPDATE categoria SET nombre=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarCategoria(int id) {
        String sql = "DELETE FROM categoria WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Categorias> listarCategorias() {
        List<Categorias> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categorias c = new Categorias();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
