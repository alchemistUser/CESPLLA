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
import dao.GuardianDAO;
import dao.StudentDAO;
import database.DatabaseConnection;
import models.Enrollment;
import models.Guardian;
import models.Student;

import java.sql.Connection;
import java.sql.SQLException;

public class EnrollmentService {

    private final StudentDAO studentDAO;
    private final GuardianDAO guardianDAO;
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentService() {
        this.studentDAO = new StudentDAO();
        this.guardianDAO = new GuardianDAO();
        this.enrollmentDAO = new EnrollmentDAO();
    }

    public void validateNewStudentEnrollment(
            Student student,
            Guardian guardian,
            Enrollment enrollment
    ) {
        validateStudent(student);
        validateGuardian(guardian);
        validateEnrollment(enrollment);
    }

    public int enrollNewStudent(
            Student student,
            Guardian guardian,
            Enrollment enrollment
    ) throws SQLException {

        validateNewStudentEnrollment(student, guardian, enrollment);

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            int studentId = studentDAO.createStudent(connection, student);
            int guardianId = guardianDAO.createGuardian(connection, guardian);

            guardianDAO.linkGuardianToStudent(
                    connection,
                    guardianId,
                    studentId,
                    guardian.getRelationship()
            );

            enrollment.setStudentId(studentId);
            enrollment.setGradeLevel(student.getGradeLevel());

            int enrollmentId = enrollmentDAO.createEnrollment(connection, enrollment);

            connection.commit();
            return enrollmentId;

        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }

            throw ex;

        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    private void validateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student information is required.");
        }

        if (isEmpty(student.getGradeLevel())) {
            throw new IllegalArgumentException("Grade level is required.");
        }

        if (isEmpty(student.getFirstName())) {
            throw new IllegalArgumentException("Student first name is required.");
        }

        if (isEmpty(student.getLastName())) {
            throw new IllegalArgumentException("Student last name is required.");
        }

        if (isEmpty(student.getAddress())) {
            throw new IllegalArgumentException("Student address is required.");
        }

        if (student.getAge() <= 0) {
            throw new IllegalArgumentException("Student age must be greater than zero.");
        }

        if (student.getBirthdate() == null) {
            throw new IllegalArgumentException("Student birthdate is required.");
        }

        if (isEmpty(student.getPrevSchool())) {
            throw new IllegalArgumentException("Previous school is required.");
        }

        if (student.isHasAllergies() && isEmpty(student.getMedicalDetails())) {
            throw new IllegalArgumentException("Medical details are required if the student has allergies.");
        }
    }

    private void validateGuardian(Guardian guardian) {
        if (guardian == null) {
            throw new IllegalArgumentException("Guardian information is required.");
        }

        if (isEmpty(guardian.getFirstName())) {
            throw new IllegalArgumentException("Guardian first name is required.");
        }

        if (guardian.getFirstName().length() > 255) {
            throw new IllegalArgumentException("Guardian first name must not exceed 255 characters.");
        }

        if (isEmpty(guardian.getLastName())) {
            throw new IllegalArgumentException("Guardian last name is required.");
        }

        if (guardian.getLastName().length() > 255) {
            throw new IllegalArgumentException("Guardian last name must not exceed 255 characters.");
        }

        if (isEmpty(guardian.getContactNumber())) {
            throw new IllegalArgumentException("Guardian contact number is required.");
        }

        if (guardian.getContactNumber().length() > 15) {
            throw new IllegalArgumentException("Guardian contact number must not exceed 15 characters.");
        }

        if (isEmpty(guardian.getAddress())) {
            throw new IllegalArgumentException("Guardian address is required.");
        }

        if (guardian.getAddress().length() > 255) {
            throw new IllegalArgumentException("Guardian address must not exceed 255 characters.");
        }

        if (isEmpty(guardian.getOccupation())) {
            throw new IllegalArgumentException("Guardian occupation is required.");
        }

        if (guardian.getOccupation().length() > 255) {
            throw new IllegalArgumentException("Guardian occupation must not exceed 255 characters.");
        }

        if (isEmpty(guardian.getRelationship())) {
            throw new IllegalArgumentException("Guardian relationship is required.");
        }

        if (guardian.getRelationship().length() > 255) {
            throw new IllegalArgumentException("Guardian relationship must not exceed 255 characters.");
        }
    }

    private void validateEnrollment(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment information is required.");
        }

        if (enrollment.getDiagnosticTestSchedule() == null) {
            throw new IllegalArgumentException("Diagnostic test schedule is required.");
        }

        if (enrollment.getEnrollmentStatus() <= 0) {
            throw new IllegalArgumentException("Enrollment status is required.");
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
