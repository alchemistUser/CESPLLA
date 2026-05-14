/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Pololoers
 */
import database.DatabaseConnection;
import models.Section;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO {

    public List<Section> findAll() {
        List<Section> list = new ArrayList<>();
        String sql = "SELECT section_id, grade_level, section_name FROM section ORDER BY grade_level, section_name";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Section section) {
        String sql = "INSERT INTO section (grade_level, section_name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, section.getGradeLevel());
            stmt.setString(2, section.getSectionName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Section section) {
        String sql = "UPDATE section SET grade_level = ?, section_name = ? WHERE section_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, section.getGradeLevel());
            stmt.setString(2, section.getSectionName());
            stmt.setInt(3, section.getSectionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int sectionId) {
        String sql = "DELETE FROM section WHERE section_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sectionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Section mapResultSet(ResultSet rs) throws SQLException {
        Section s = new Section();
        s.setSectionId(rs.getInt("section_id"));
        s.setGradeLevel(rs.getString("grade_level"));
        s.setSectionName(rs.getString("section_name"));
        return s;
    }
}
