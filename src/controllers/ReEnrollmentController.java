/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import dao.StudentDAO;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import models.Enrollment;
import models.Student;

import services.ReEnrollmentService;

import views.panels.enrollment.OldStudentEnrollmentPanel;
import views.panels.enrollment.ReEnrollmentPanel;

public class ReEnrollmentController {

    private final ReEnrollmentPanel view;

    private final JPanel dashboardPanel;

    private final StudentDAO studentDAO;

    private final ReEnrollmentService reEnrollmentService;

    private final int studentId;

    private Student student;

    public ReEnrollmentController(
            ReEnrollmentPanel view,
            JPanel dashboardPanel,
            int studentId
    ) {

        this.view = view;
        this.dashboardPanel = dashboardPanel;
        this.studentId = studentId;

        studentDAO = new StudentDAO();

        reEnrollmentService
                = new ReEnrollmentService();

        initializeData();

        attachEvents();
    }

    // =====================================================
    // INITIALIZE DATA
    // =====================================================
    private void initializeData() {

        try {

            student = studentDAO.findById(studentId);

            if (student == null) {

                JOptionPane.showMessageDialog(
                        view,
                        "Student not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            loadStudentInformation();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    view,
                    "Failed to load student information.\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // LOAD STUDENT INFO
    // =====================================================
    private void loadStudentInformation() {

        String fullName
                = student.getFirstName()
                + " "
                + student.getLastName();

        view.getLblStudentName().setText(fullName);

        view.getLblCurrentGradeLevel().setText(
                student.getGradeLevel()
        );
    }

    // =====================================================
    // ATTACH EVENTS
    // =====================================================
    private void attachEvents() {

        initializeSaveButton();

        initializeBackButton();
    }

    // =====================================================
    // SAVE BUTTON
    // =====================================================
    private void initializeSaveButton() {

        view.getBtnSaveEnrollment()
                .addActionListener(e -> {

                    saveEnrollment();
                });
    }

    // =====================================================
    // BACK BUTTON
    // =====================================================
    private void initializeBackButton() {

        view.getBtnBack().addActionListener(e -> {

            OldStudentEnrollmentPanel panel
                    = new OldStudentEnrollmentPanel();

            new OldStudentEnrollmentController(
                    panel,
                    dashboardPanel
            );

            loadPanel(panel);
        });
    }

    // =====================================================
    // SAVE ENROLLMENT
    // =====================================================
    private void saveEnrollment() {

        try {

            Enrollment enrollment
                    = new Enrollment();

            enrollment.setStudentId(studentId);

            enrollment.setGradeLevel(
                    view.getCmbNewGradeLevel()
                            .getSelectedItem()
                            .toString()
            );

            // =========================================
            // DIAGNOSTIC TEST SCHEDULE
            // =========================================
            if (view.getDateDiagnosticSchedule()
                    .getDate() != null) {

                enrollment.setDiagnosticTestSchedule(
                        view.getDateDiagnosticSchedule()
                                .getDate()
                                .toInstant()
                                .atZone(
                                        java.time.ZoneId.systemDefault()
                                )
                                .toLocalDate()
                );
            }

            // =========================================
            // REQUIREMENT STATUS
            // =========================================
            enrollment.setDiagnosticTestStatus(
                    view.getChkDiagnosticPassed()
                            .isSelected()
            );

            enrollment.setPsaStatus(
                    view.getChkPsaSubmitted()
                            .isSelected()
            );

            enrollment.setSf10Status(
                    view.getChkSf10Submitted()
                            .isSelected()
            );

            enrollment.setGoodMoralStatus(
                    view.getChkGoodMoralSubmitted()
                            .isSelected()
            );

            // =========================================
            // ENROLLMENT STATUS
            // =========================================
            String status
                    = view.getCmbEnrollmentStatus()
                            .getSelectedItem()
                            .toString();

            int statusValue = 0;

            switch (status) {

                case "Pending":
                    statusValue = 0;
                    break;

                case "Enrolled":
                    statusValue = 1;
                    break;

                case "Rejected":
                    statusValue = 2;
                    break;
            }

            enrollment.setEnrollmentStatus(
                    statusValue
            );

            // =========================================
            // SAVE
            // =========================================
            reEnrollmentService
                    .createReEnrollment(enrollment);

            JOptionPane.showMessageDialog(
                    view,
                    "Student re-enrollment successful.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    view,
                    "Failed to save re-enrollment.\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // LOAD PANEL
    // =====================================================
    private void loadPanel(JPanel panel) {

        dashboardPanel.removeAll();

        dashboardPanel.setLayout(
                new java.awt.BorderLayout()
        );

        dashboardPanel.add(
                panel,
                java.awt.BorderLayout.CENTER
        );

        dashboardPanel.revalidate();

        dashboardPanel.repaint();
    }
}
