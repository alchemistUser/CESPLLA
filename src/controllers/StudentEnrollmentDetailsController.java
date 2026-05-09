/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import dao.EnrollmentDAO;
import dao.GuardianDAO;
import dao.StudentDAO;
import models.Enrollment;
import models.Guardian;
import models.Student;
import views.dialogs.students.StudentEnrollmentDetailsDialog;

import javax.swing.JOptionPane;
import java.sql.SQLException;

public class StudentEnrollmentDetailsController {

    private final StudentEnrollmentDetailsDialog dialog;
    private final StudentDAO studentDAO;
    private final GuardianDAO guardianDAO;
    private final EnrollmentDAO enrollmentDAO;

    public StudentEnrollmentDetailsController(
            StudentEnrollmentDetailsDialog dialog,
            int studentId
    ) {
        this.dialog = dialog;
        this.studentDAO = new StudentDAO();
        this.guardianDAO = new GuardianDAO();
        this.enrollmentDAO = new EnrollmentDAO();

        attachEvents();
        loadDetails(studentId);
    }

    private void attachEvents() {
        dialog.getBtnClose().addActionListener(e -> dialog.dispose());
    }

    private void loadDetails(int studentId) {
        try {
            Student student = studentDAO.findById(studentId);
            Guardian guardian = guardianDAO.findGuardianByStudentId(studentId);
            Enrollment enrollment = enrollmentDAO.findByStudentId(studentId);

            if (student == null) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Student record not found.",
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
                dialog.dispose();
                return;
            }

            populateStudent(student);
            populateGuardian(guardian);
            populateEnrollment(enrollment);

        } catch (SQLException ex) {
            System.err.println("LOAD STUDENT ENROLLMENT DETAILS ERROR:");
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    dialog,
                    "Database error while loading student details:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            dialog.dispose();
        }
    }

    private void populateStudent(Student student) {
        dialog.getLblStudentIdValue().setText(String.valueOf(student.getStudentId()));
        dialog.getLblGradeLevelValue().setText(formatGradeLevel(student.getGradeLevel()));
        dialog.getLblStudentNameValue().setText(
                student.getFirstName() + " "
                + safe(student.getMiddleName()) + " "
                + student.getLastName()
        );
        dialog.getLblAgeValue().setText(String.valueOf(student.getAge()));
        dialog.getLblBirthdateValue().setText(String.valueOf(student.getBirthdate()));
        dialog.getLblAddressValue().setText(student.getAddress());
        dialog.getLblPreviousSchoolValue().setText(student.getPrevSchool());
        dialog.getLblHasAllergiesValue().setText(student.isHasAllergies() ? "Yes" : "No");
        dialog.getLblMedicalDetailsValue().setText(safe(student.getMedicalDetails()));
    }

    private void populateGuardian(Guardian guardian) {
        if (guardian == null) {
            dialog.getLblGuardianNameValue().setText("No guardian record found.");
            dialog.getLblGuardianContactValue().setText("-");
            dialog.getLblGuardianAddressValue().setText("-");
            dialog.getLblGuardianOccupationValue().setText("-");
            dialog.getLblRelationshipValue().setText("-");
            return;
        }

        dialog.getLblGuardianNameValue().setText(
                guardian.getFirstName() + " " + guardian.getLastName()
        );
        dialog.getLblGuardianContactValue().setText(guardian.getContactNumber());
        dialog.getLblGuardianAddressValue().setText(guardian.getAddress());
        dialog.getLblGuardianOccupationValue().setText(guardian.getOccupation());
        dialog.getLblRelationshipValue().setText(guardian.getRelationship());
    }

    private void populateEnrollment(Enrollment enrollment) {
        if (enrollment == null) {
            dialog.getLblEnrollmentIdValue().setText("No enrollment record found.");
            dialog.getLblDiagnosticScheduleValue().setText("-");
            dialog.getLblDiagnosticStatusValue().setText("-");
            dialog.getLblPsaStatusValue().setText("-");
            dialog.getLblSf10StatusValue().setText("-");
            dialog.getLblGoodMoralStatusValue().setText("-");
            dialog.getLblEnrollmentStatusValue().setText("-");
            return;
        }

        dialog.getLblEnrollmentIdValue().setText(String.valueOf(enrollment.getEnrollmentId()));
        dialog.getLblDiagnosticScheduleValue().setText(String.valueOf(enrollment.getDiagnosticTestSchedule()));
        dialog.getLblDiagnosticStatusValue().setText(enrollment.isDiagnosticTestStatus() ? "Passed" : "Not Passed");
        dialog.getLblPsaStatusValue().setText(enrollment.isPsaStatus() ? "Submitted" : "Not Submitted");
        dialog.getLblSf10StatusValue().setText(enrollment.isSf10Status() ? "Submitted" : "Not Submitted");
        dialog.getLblGoodMoralStatusValue().setText(enrollment.isGoodMoralStatus() ? "Submitted" : "Not Submitted");
        dialog.getLblEnrollmentStatusValue().setText(mapEnrollmentStatus(enrollment.getEnrollmentStatus()));
    }

    private String mapEnrollmentStatus(int status) {
        return switch (status) {
            case 1 ->
                "Pending";
            case 2 ->
                "Enrolled";
            case 3 ->
                "Rejected";
            default ->
                "Unknown";
        };
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value;
    }

    private String formatGradeLevel(String gradeLevel) {
        if (gradeLevel == null) {
            return "-";
        }

        return switch (gradeLevel) {
            case "N" ->
                "Nursery";
            case "K1" ->
                "Junior Kindergarten";
            case "K2" ->
                "Senior Kindergarten";
            default ->
                gradeLevel;
        };
    }
}
