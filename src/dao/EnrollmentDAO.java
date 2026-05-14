/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import models.Enrollment;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Pololoers
 */
public class EnrollmentDAO {

    public int createEnrollment(Connection conn, Enrollment enrollment) throws SQLException {
        String sql = """
            INSERT INTO enrollment (
                student_id,
                grade_level,
                diagnostic_test_schedule,
                diagnostic_test_status,
                psa_status,
                sf10_status,
                goodmoral_status,
                enrollment_status,
                payment_scheme
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING enrollment_id
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setString(2, enrollment.getGradeLevel());

            if (enrollment.getDiagnosticTestSchedule() != null) {
                stmt.setDate(3, Date.valueOf(enrollment.getDiagnosticTestSchedule()));
            } else {
                stmt.setDate(3, null);
            }

            stmt.setBoolean(4, enrollment.isDiagnosticTestStatus());
            stmt.setBoolean(5, enrollment.isPsaStatus());
            stmt.setBoolean(6, enrollment.isSf10Status());
            stmt.setBoolean(7, enrollment.isGoodMoralStatus());
            stmt.setInt(8, enrollment.getEnrollmentStatus());
            stmt.setString(9, enrollment.getPaymentScheme()); // ← ADDED

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("enrollment_id");
                }
            }
        }

        throw new SQLException("Failed to create enrollment.");
    }

    public Enrollment findByStudentId(int studentId) throws SQLException {
        String sql = """
            SELECT
                enrollment_id,
                student_id,
                grade_level,
                diagnostic_test_schedule,
                diagnostic_test_status,
                psa_status,
                sf10_status,
                goodmoral_status,
                enrollment_status,
                payment_scheme
            FROM enrollment
            WHERE student_id = ?
            ORDER BY enrollment_id DESC
            LIMIT 1
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEnrollment(rs);
                }
            }
        }

        return null;
    }

    public Enrollment findByEnrollmentId(int enrollmentId) throws SQLException {
        String sql = """
            SELECT
                enrollment_id,
                student_id,
                grade_level,
                diagnostic_test_schedule,
                diagnostic_test_status,
                psa_status,
                sf10_status,
                goodmoral_status,
                enrollment_status,
                payment_scheme
            FROM enrollment
            WHERE enrollment_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEnrollment(rs);
                }
            }
        }

        return null;
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();

        enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setGradeLevel(rs.getString("grade_level"));

        Date schedule = rs.getDate("diagnostic_test_schedule");
        if (schedule != null) {
            enrollment.setDiagnosticTestSchedule(schedule.toLocalDate());
        }

        enrollment.setDiagnosticTestStatus(rs.getBoolean("diagnostic_test_status"));
        enrollment.setPsaStatus(rs.getBoolean("psa_status"));
        enrollment.setSf10Status(rs.getBoolean("sf10_status"));
        enrollment.setGoodMoralStatus(rs.getBoolean("goodmoral_status"));
        enrollment.setEnrollmentStatus(rs.getInt("enrollment_status"));
        enrollment.setPaymentScheme(rs.getString("payment_scheme")); // ← ADDED

        return enrollment;
    }
}
