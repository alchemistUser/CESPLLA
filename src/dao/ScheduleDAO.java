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
import models.ScheduleModel;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class ScheduleDAO {

    public List<ScheduleModel> findAll() {
        List<ScheduleModel> list = new ArrayList<>();
        String sql = "SELECT schedule_id, day, start_time, end_time FROM schedule ORDER BY day, start_time";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(ScheduleModel schedule) {
        String sql = "INSERT INTO schedule (day, start_time, end_time) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, schedule.getDay());
            stmt.setInt(2, schedule.getStartTime());
            stmt.setInt(3, schedule.getEndTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ScheduleModel schedule) {
        String sql = "UPDATE schedule SET day = ?, start_time = ?, end_time = ? WHERE schedule_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, schedule.getDay());
            stmt.setInt(2, schedule.getStartTime());
            stmt.setInt(3, schedule.getEndTime());
            stmt.setInt(4, schedule.getScheduleId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int scheduleId) {
        String sql = "DELETE FROM schedule WHERE schedule_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ScheduleModel mapResultSet(ResultSet rs) throws SQLException {
        ScheduleModel s = new ScheduleModel();
        s.setScheduleId(rs.getInt("schedule_id"));
        s.setDay(rs.getString("day"));
        s.setStartTime(rs.getInt("start_time"));
        s.setEndTime(rs.getInt("end_time"));
        return s;
    }
}
