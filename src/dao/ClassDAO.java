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
import models.ClassModel;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class ClassDAO {

    /**
     * Fetches all classes with JOINed details for UI display
     */
    public List<ClassModel> findAllWithDetails() {
        List<ClassModel> list = new ArrayList<>();
        String sql = """
            SELECT c.class_id, 
                   c.subject_id, sub.subject_name,
                   c.section_id, sec.section_name,
                   c.schedule_id, sch.day, sch.start_time, sch.end_time,
                   c.teacher_id, t.name as teacher_name
            FROM class c
            LEFT JOIN subjects sub ON c.subject_id = sub.subject_id
            LEFT JOIN section sec ON c.section_id = sec.section_id
            LEFT JOIN schedule sch ON c.schedule_id = sch.schedule_id
            LEFT JOIN teacher t ON c.teacher_id = t.teacher_id
            ORDER BY sec.grade_level, sec.section_name, sch.day, sch.start_time
        """;

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(ClassModel cls) {
        String sql = "INSERT INTO class (subject_id, section_id, schedule_id, teacher_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cls.getSubjectId());
            stmt.setInt(2, cls.getSectionId());
            stmt.setInt(3, cls.getScheduleId());
            stmt.setInt(4, cls.getTeacherId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ClassModel cls) {
        String sql = "UPDATE class SET subject_id = ?, section_id = ?, schedule_id = ?, teacher_id = ? WHERE class_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cls.getSubjectId());
            stmt.setInt(2, cls.getSectionId());
            stmt.setInt(3, cls.getScheduleId());
            stmt.setInt(4, cls.getTeacherId());
            stmt.setInt(5, cls.getClassId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int classId) {
        String sql = "DELETE FROM class WHERE class_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ClassModel mapResultSet(ResultSet rs) throws SQLException {
        ClassModel c = new ClassModel();
        c.setClassId(rs.getInt("class_id"));
        c.setSubjectId(rs.getInt("subject_id"));
        c.setSubjectName(rs.getString("subject_name"));
        c.setSectionId(rs.getInt("section_id"));
        c.setSectionName(rs.getString("section_name"));
        c.setScheduleId(rs.getInt("schedule_id"));
        c.setDay(rs.getString("day"));
        c.setStartTime(rs.getInt("start_time"));
        c.setEndTime(rs.getInt("end_time"));
        c.setTeacherId(rs.getInt("teacher_id"));
        c.setTeacherName(rs.getString("teacher_name"));
        return c;
    }
}
