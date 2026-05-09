/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import models.Enrollment;
import models.Guardian;
import models.Student;
import services.EnrollmentService;
import views.panels.enrollment.NewStudentEnrollmentPanel;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EnrollmentController {

    private final NewStudentEnrollmentPanel view;
    private final EnrollmentService enrollmentService;

    public EnrollmentController(NewStudentEnrollmentPanel view) {
        this.view = view;
        this.enrollmentService = new EnrollmentService();

        attachEvents();
        handleAllergyToggle();
    }

    private void attachEvents() {
        view.getBtnSaveEnrollment().addActionListener(e -> handleSaveEnrollment());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getChkHasAllergies().addActionListener(e -> handleAllergyToggle());
    }

    private void handleAllergyToggle() {
        boolean hasAllergies = view.getChkHasAllergies().isSelected();

        view.getTxtMedicalDetails().setEnabled(hasAllergies);

        if (!hasAllergies) {
            view.getTxtMedicalDetails().setText("");
        }
    }

    private void handleSaveEnrollment() {
        try {
            Student student = buildStudent();
            Guardian guardian = buildGuardian();
            Enrollment enrollment = buildEnrollment();

            enrollmentService.validateNewStudentEnrollment(student, guardian, enrollment);

            String summary = buildEnrollmentSummary();

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    summary,
                    "Confirm Enrollment Details",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int enrollmentId = enrollmentService.enrollNewStudent(student, guardian, enrollment);

            JOptionPane.showMessageDialog(
                    view,
                    "Enrollment saved successfully.\nEnrollment ID: " + enrollmentId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();

        } catch (IllegalArgumentException ex) {
            System.err.println("VALIDATION ERROR: " + ex.getMessage());

            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (SQLException ex) {
            System.err.println("DATABASE ERROR:");
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    view,
                    "Database error while saving enrollment:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            System.err.println("UNEXPECTED ERROR:");
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    view,
                    "Unexpected error:\n" + ex.getMessage(),
                    "Unexpected Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String buildEnrollmentSummary() {
        return """
                Please confirm enrollment details:

                Student:
                Name: %s %s %s
                Grade Level: %s
                Age: %s
                Birthdate: %s

                Guardian:
                Name: %s %s
                Contact Number: %s
                Relationship: %s

                Enrollment:
                Diagnostic Schedule: %s
                Diagnostic Passed: %s
                PSA Submitted: %s
                SF10 Submitted: %s
                Good Moral Submitted: %s
                Enrollment Status: %s
                """.formatted(
                view.getTxtStudentFirstName().getText().trim(),
                view.getTxtStudentMiddleName().getText().trim(),
                view.getTxtStudentLastName().getText().trim(),
                view.getCmbGradeLevel().getSelectedItem(),
                view.getSpnAge().getValue(),
                toLocalDate(view.getDateBirthdate().getDate()),
                view.getTxtGuardianFirstName().getText().trim(),
                view.getTxtGuardianLastName().getText().trim(),
                view.getTxtGuardianContactNumber().getText().trim(),
                view.getCmbRelationship().getSelectedItem(),
                toLocalDate(view.getDateDiagnosticSchedule().getDate()),
                view.getChkDiagnosticPassed().isSelected() ? "Yes" : "No",
                view.getChkPsaSubmitted().isSelected() ? "Yes" : "No",
                view.getChkSf10Submitted().isSelected() ? "Yes" : "No",
                view.getChkGoodMoralSubmitted().isSelected() ? "Yes" : "No",
                view.getCmbEnrollmentStatus().getSelectedItem()
        );
    }

    private Student buildStudent() {
        Student student = new Student();

        student.setGradeLevel(mapGradeLevel());
        student.setFirstName(view.getTxtStudentFirstName().getText().trim());
        student.setMiddleName(view.getTxtStudentMiddleName().getText().trim());
        student.setLastName(view.getTxtStudentLastName().getText().trim());
        student.setAddress(view.getTxtStudentAddress().getText().trim());
        student.setAge((int) view.getSpnAge().getValue());
        student.setBirthdate(toLocalDate(view.getDateBirthdate().getDate()));
        student.setPrevSchool(view.getTxtPreviousSchool().getText().trim());
        student.setHasAllergies(view.getChkHasAllergies().isSelected());
        student.setMedicalDetails(view.getTxtMedicalDetails().getText().trim());

        return student;
    }

    private Guardian buildGuardian() {
        Guardian guardian = new Guardian();

        guardian.setFirstName(view.getTxtGuardianFirstName().getText().trim());
        guardian.setLastName(view.getTxtGuardianLastName().getText().trim());
        guardian.setContactNumber(view.getTxtGuardianContactNumber().getText().trim());
        guardian.setAddress(view.getTxtGuardianAddress().getText().trim());
        guardian.setOccupation(view.getTxtGuardianOccupation().getText().trim());
        guardian.setRelationship(view.getCmbRelationship().getSelectedItem().toString());

        return guardian;
    }

    private Enrollment buildEnrollment() {
        Enrollment enrollment = new Enrollment();

        enrollment.setGradeLevel(mapGradeLevel());
        enrollment.setDiagnosticTestSchedule(toLocalDate(view.getDateDiagnosticSchedule().getDate()));
        enrollment.setDiagnosticTestStatus(view.getChkDiagnosticPassed().isSelected());
        enrollment.setPsaStatus(view.getChkPsaSubmitted().isSelected());
        enrollment.setSf10Status(view.getChkSf10Submitted().isSelected());
        enrollment.setGoodMoralStatus(view.getChkGoodMoralSubmitted().isSelected());
        enrollment.setEnrollmentStatus(mapEnrollmentStatus());

        return enrollment;
    }

    private String mapGradeLevel() {
        String selected = view.getCmbGradeLevel().getSelectedItem().toString();

        return switch (selected) {
            case "Nursery" ->
                "N";
            case "Junior Kindergarten" ->
                "K1";
            case "Senior Kindergarten" ->
                "K2";
            default ->
                selected;
        };
    }

    private int mapEnrollmentStatus() {
        String status = view.getCmbEnrollmentStatus().getSelectedItem().toString();

        return switch (status) {
            case "Pending" ->
                1;
            case "Enrolled" ->
                2;
            case "Rejected" ->
                3;
            default ->
                1;
        };
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void clearForm() {
        view.getCmbGradeLevel().setSelectedIndex(0);

        view.getTxtStudentFirstName().setText("");
        view.getTxtStudentMiddleName().setText("");
        view.getTxtStudentLastName().setText("");
        view.getTxtStudentAddress().setText("");
        view.getSpnAge().setValue(1);
        view.getDateBirthdate().setDate(null);
        view.getTxtPreviousSchool().setText("");
        view.getChkHasAllergies().setSelected(false);
        view.getTxtMedicalDetails().setText("");
        view.getTxtMedicalDetails().setEnabled(false);

        view.getTxtGuardianFirstName().setText("");
        view.getTxtGuardianLastName().setText("");
        view.getTxtGuardianContactNumber().setText("");
        view.getTxtGuardianAddress().setText("");
        view.getTxtGuardianOccupation().setText("");
        view.getCmbRelationship().setSelectedIndex(0);

        view.getDateDiagnosticSchedule().setDate(null);
        view.getChkDiagnosticPassed().setSelected(false);
        view.getChkPsaSubmitted().setSelected(false);
        view.getChkSf10Submitted().setSelected(false);
        view.getChkGoodMoralSubmitted().setSelected(false);
        view.getCmbEnrollmentStatus().setSelectedIndex(0);
    }
}
