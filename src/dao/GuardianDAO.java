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
import models.Guardian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuardianDAO {

    public int createGuardian(Connection conn, Guardian guardian) throws SQLException {
        String sql = """
            INSERT INTO guardian (
                first_name,
                last_name,
                contact_number,
                address,
                occupation
            )
            VALUES (?, ?, ?, ?, ?)
            RETURNING guardian_id
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guardian.getFirstName());
            stmt.setString(2, guardian.getLastName());
            stmt.setString(3, guardian.getContactNumber());
            stmt.setString(4, guardian.getAddress());
            stmt.setString(5, guardian.getOccupation());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("guardian_id");
                }
            }
        }

        throw new SQLException("Failed to create guardian.");
    }

    public void linkGuardianToStudent(
            Connection conn,
            int guardianId,
            int studentId,
            String relationship
    ) throws SQLException {

        String sql = """
            INSERT INTO guardian_of_student (
                guardian_id,
                student_id,
                relationship
            )
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guardianId);
            stmt.setInt(2, studentId);
            stmt.setString(3, relationship);
            stmt.executeUpdate();
        }
    }

    public Guardian findGuardianByStudentId(int studentId) throws SQLException {
        String sql = """
            SELECT 
                g.guardian_id,
                g.first_name,
                g.last_name,
                g.contact_number,
                g.address,
                g.occupation,
                gos.relationship
            FROM guardian g
            INNER JOIN guardian_of_student gos
                ON g.guardian_id = gos.guardian_id
            WHERE gos.student_id = ?
            LIMIT 1
        """;

        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuardian(rs);
                }
            }
        }

        return null;
    }

    private Guardian mapResultSetToGuardian(ResultSet rs) throws SQLException {
        Guardian guardian = new Guardian();

        guardian.setGuardianId(rs.getInt("guardian_id"));
        guardian.setFirstName(rs.getString("first_name"));
        guardian.setLastName(rs.getString("last_name"));
        guardian.setContactNumber(rs.getString("contact_number"));
        guardian.setAddress(rs.getString("address"));
        guardian.setOccupation(rs.getString("occupation"));
        guardian.setRelationship(rs.getString("relationship"));

        return guardian;
    }

}
