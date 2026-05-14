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
import models.Subject;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class SubjectDAO {

    public List<Subject> findAll() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT subject_id, grade_level, subject_name, time_alloted FROM subjects ORDER BY grade_level, subject_name";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Subject subject) {
        String sql = "INSERT INTO subjects (grade_level, subject_name, time_alloted) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subject.getGradeLevel());
            stmt.setString(2, subject.getSubjectName());
            stmt.setInt(3, subject.getTimeAlloted());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Subject subject) {
        String sql = "UPDATE subjects SET grade_level = ?, subject_name = ?, time_alloted = ? WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subject.getGradeLevel());
            stmt.setString(2, subject.getSubjectName());
            stmt.setInt(3, subject.getTimeAlloted());
            stmt.setInt(4, subject.getSubjectId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Subject mapResultSet(ResultSet rs) throws SQLException {
        Subject s = new Subject();
        s.setSubjectId(rs.getInt("subject_id"));
        s.setGradeLevel(rs.getString("grade_level"));
        s.setSubjectName(rs.getString("subject_name"));
        s.setTimeAlloted(rs.getInt("time_alloted"));
        return s;
    }
}
