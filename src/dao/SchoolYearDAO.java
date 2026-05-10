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
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class SchoolYearDAO {

    /**
     * Fetches student records and payment totals for a specific date range.
     * Uses LEFT JOIN to include students who haven't made payments yet.
     */
    public List<Map<String, Object>> findArchiveRecords(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> records = new ArrayList<>();

        // We join enrollment to find students enrolled in this period (using diagnostic_test_schedule as proxy)
        // We LEFT JOIN payment to calculate total paid, even if 0.
        String sql = """
            SELECT 
                s.student_id,
                s.first_name,
                s.last_name,
                s.grade_level,
                COALESCE(SUM(p.amount), 0) as total_paid
            FROM enrollment e
            JOIN student s ON e.student_id = s.student_id
            LEFT JOIN payment p ON s.student_id = p.student_id 
                AND p.payment_date >= ? AND p.payment_date <= ?
            WHERE e.diagnostic_test_schedule >= ? AND e.diagnostic_test_schedule <= ?
            GROUP BY s.student_id, s.first_name, s.last_name, s.grade_level
            ORDER BY s.last_name ASC
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // FIX: Use java.sql.Date explicitly to resolve ambiguity
            java.sql.Date sqlStart = java.sql.Date.valueOf(startDate);
            java.sql.Date sqlEnd = java.sql.Date.valueOf(endDate);

            // Parameters for Payment Date filter
            pstmt.setDate(1, sqlStart);
            pstmt.setDate(2, sqlEnd);

            // Parameters for Enrollment Date filter
            pstmt.setDate(3, sqlStart);
            pstmt.setDate(4, sqlEnd);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("student_id", rs.getInt("student_id"));
                    row.put("student_name", rs.getString("first_name") + " " + rs.getString("last_name"));
                    row.put("grade_level", rs.getString("grade_level"));
                    row.put("total_paid", rs.getDouble("total_paid"));

                    // Placeholders for data not in ERD or hard to calculate without Fee table
                    row.put("lrn", "ID-" + rs.getInt("student_id"));
                    row.put("enrollment_date", "N/A");
                    row.put("payment_status", "Pending");
                    row.put("balance", 0.00);

                    records.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}
