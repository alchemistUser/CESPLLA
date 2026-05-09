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
import models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean createStudent(Student student) {
        String sql = """
                INSERT INTO student
                (grade_level, first_name, middle_name, last_name, address, age,
                 birthdate, prev_school, has_allergies, medical_details)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getGradeLevel());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getMiddleName());
            statement.setString(4, student.getLastName());
            statement.setString(5, student.getAddress());
            statement.setInt(6, student.getAge());
            statement.setDate(7, Date.valueOf(student.getBirthdate()));
            statement.setString(8, student.getPrevSchool());
            statement.setBoolean(9, student.isHasAllergies());
            statement.setString(10, student.getMedicalDetails());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createStudent(Connection connection, Student student) throws SQLException {
        String sql = """
            INSERT INTO student
            (grade_level, first_name, middle_name, last_name, address, age,
             birthdate, prev_school, has_allergies, medical_details)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING student_id
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getGradeLevel());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getMiddleName());
            statement.setString(4, student.getLastName());
            statement.setString(5, student.getAddress());
            statement.setInt(6, student.getAge());
            statement.setDate(7, Date.valueOf(student.getBirthdate()));
            statement.setString(8, student.getPrevSchool());
            statement.setBoolean(9, student.isHasAllergies());
            statement.setString(10, student.getMedicalDetails());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("student_id");
                }
            }
        }

        throw new SQLException("Failed to create student.");
    }

    public Student findById(int studentId) {
        String sql = """
                SELECT student_id, grade_level, first_name, middle_name, last_name,
                       address, age, birthdate, prev_school, has_allergies, medical_details
                FROM student
                WHERE student_id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToStudent(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Student> searchByName(String keyword) {
        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, grade_level, first_name, middle_name, last_name,
                       address, age, birthdate, prev_school, has_allergies, medical_details
                FROM student
                WHERE LOWER(first_name) LIKE LOWER(?)
                   OR LOWER(middle_name) LIKE LOWER(?)
                   OR LOWER(last_name) LIKE LOWER(?)
                ORDER BY last_name, first_name
                """;

        String searchKeyword = "%" + keyword + "%";

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(mapResultSetToStudent(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, grade_level, first_name, middle_name, last_name,
                       address, age, birthdate, prev_school, has_allergies, medical_details
                FROM student
                ORDER BY last_name, first_name
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();

        student.setStudentId(resultSet.getInt("student_id"));
        student.setGradeLevel(resultSet.getString("grade_level"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setMiddleName(resultSet.getString("middle_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setAddress(resultSet.getString("address"));
        student.setAge(resultSet.getInt("age"));
        student.setBirthdate(resultSet.getDate("birthdate").toLocalDate());
        student.setPrevSchool(resultSet.getString("prev_school"));
        student.setHasAllergies(resultSet.getBoolean("has_allergies"));
        student.setMedicalDetails(resultSet.getString("medical_details"));

        return student;
    }

    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> students = new ArrayList<>();

        String sql = """
        SELECT
            student_id,
            grade_level,
            first_name,
            middle_name,
            last_name,
            address,
            age,
            birthdate,
            prev_school,
            has_allergies,
            medical_details
        FROM student
        WHERE
            LOWER(first_name) LIKE LOWER(?)
            OR LOWER(last_name) LIKE LOWER(?)
            OR LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER(?)
        ORDER BY last_name ASC, first_name ASC
    """;

        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            stmt.setString(1, searchValue);
            stmt.setString(2, searchValue);
            stmt.setString(3, searchValue);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Student student = mapResultSetToStudent(rs);
                    students.add(student);
                }
            }
        }

        return students;
    }
}
