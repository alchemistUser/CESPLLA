/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import dao.EnrollmentDAO;
import dao.PaymentDAO;
import dao.StudentDAO;
import database.DatabaseConnection;
import models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArchiveService {

    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final PaymentDAO paymentDAO;

    public ArchiveService() {
        this.studentDAO = new StudentDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.paymentDAO = new PaymentDAO();
    }

    /**
     * Finds students who haven't enrolled in the current school year and are
     * considered inactive
     */
    public List<Student> findInactiveStudents(LocalDate cutoffDate) throws SQLException {
        List<Student> inactiveStudents = new ArrayList<>();

        String sql = """
            SELECT s.*
            FROM student s
            LEFT JOIN enrollment e ON s.student_id = e.student_id
            WHERE e.enrollment_id IS NULL
            OR e.diagnostic_test_schedule < ?
            ORDER BY s.last_name, s.first_name
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(cutoffDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inactiveStudents.add(mapResultSetToStudent(rs));
                }
            }
        }

        return inactiveStudents;
    }

    /**
     * Archives a student record by moving it to an archive table and deleting
     * from active tables
     */
    public boolean archiveStudent(int studentId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Create archive table if not exists
            createArchiveTable(conn);

            // Copy student to archive
            String archiveSql = """
                INSERT INTO student_archive
                SELECT *, CURRENT_TIMESTAMP as archived_date
                FROM student
                WHERE student_id = ?
                ON CONFLICT (student_id) DO NOTHING
            """;

            try (PreparedStatement stmt = conn.prepareStatement(archiveSql)) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            // Delete from enrollment (cascades to payment)
            String deleteEnrollmentSql = "DELETE FROM enrollment WHERE student_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteEnrollmentSql)) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            // Delete student
            String deleteStudentSql = "DELETE FROM student WHERE student_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteStudentSql)) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
     * Archives multiple students
     */
    public int archiveMultipleStudents(List<Integer> studentIds) throws SQLException {
        int archivedCount = 0;
        for (int studentId : studentIds) {
            try {
                if (archiveStudent(studentId)) {
                    archivedCount++;
                }
            } catch (SQLException e) {
                System.err.println("Failed to archive student " + studentId + ": " + e.getMessage());
            }
        }
        return archivedCount;
    }

    private void createArchiveTable(Connection conn) throws SQLException {
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS student_archive (
                student_id INTEGER PRIMARY KEY,
                grade_level VARCHAR(2),
                first_name VARCHAR(255),
                middle_name VARCHAR(255),
                last_name VARCHAR(255),
                address VARCHAR(255),
                age INTEGER,
                birthdate DATE,
                prev_school VARCHAR(255),
                has_allergies BOOLEAN,
                medical_details VARCHAR(255),
                archived_date TIMESTAMP
            )
        """;

        try (PreparedStatement stmt = conn.prepareStatement(createTableSql)) {
            stmt.executeUpdate();
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setGradeLevel(rs.getString("grade_level"));
        student.setFirstName(rs.getString("first_name"));
        student.setMiddleName(rs.getString("middle_name"));
        student.setLastName(rs.getString("last_name"));
        student.setAddress(rs.getString("address"));
        student.setAge(rs.getInt("age"));
        student.setBirthdate(rs.getDate("birthdate").toLocalDate());
        student.setPrevSchool(rs.getString("prev_school"));
        student.setHasAllergies(rs.getBoolean("has_allergies"));
        student.setMedicalDetails(rs.getString("medical_details"));
        return student;
    }
}
