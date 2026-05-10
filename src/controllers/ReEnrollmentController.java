/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import dao.StudentDAO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.Enrollment;
import models.Student;
import services.ReEnrollmentService;
import views.panels.enrollment.OldStudentEnrollmentPanel;
import views.panels.enrollment.ReEnrollmentPanel;
import views.dialogs.students.BalanceScannerDialog;
import utils.SchoolYearUtil;
import utils.DialogUtil;
import javax.swing.JFrame;

import java.awt.Window;

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
        reEnrollmentService = new ReEnrollmentService();

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
                    "Failed to load student information.\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // LOAD STUDENT INFO
    // =====================================================
    private void loadStudentInformation() {
        String fullName = student.getFirstName() + " " + student.getLastName();
        view.getLblStudentName().setText(fullName);
        view.getLblCurrentGradeLevel().setText(student.getGradeLevel());
    }

    // =====================================================
    // ATTACH EVENTS
    // =====================================================
    private void attachEvents() {
        initializeSaveButton();
        initializeBackButton();
        initializeCheckBalanceButton(); // ← NEW: Attach Check Balance listener
    }

    // =====================================================
    // SAVE BUTTON
    // =====================================================
    private void initializeSaveButton() {
        view.getBtnSaveEnrollment().addActionListener(e -> saveEnrollment());
    }

    // =====================================================
    // BACK BUTTON
    // =====================================================
    private void initializeBackButton() {
        view.getBtnBack().addActionListener(e -> {
            OldStudentEnrollmentPanel panel = new OldStudentEnrollmentPanel();
            new OldStudentEnrollmentController(panel, dashboardPanel);
            loadPanel(panel);
        });
    }

    // =====================================================
    // CHECK BALANCE BUTTON ← NEW METHOD
    // =====================================================
    private void initializeCheckBalanceButton() {
        view.getBtnCheckBalance().addActionListener(e -> handleCheckBalance());
    }

    private void handleCheckBalance() {
        try {
            // 1. Determine School Year to check (use current SY for re-enrollment)
            String schoolYear = SchoolYearUtil.getCurrentSchoolYear();

            // 2. Get parent window for dialog positioning
            Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(view);
            JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;

            // 3. Create and configure dialog
            BalanceScannerDialog dialog = new BalanceScannerDialog(parentFrame, true);
            dialog.getLblStudentName().setText(
                    student.getFirstName() + " " + student.getLastName()
            );
            dialog.getLblSchoolYear().setText(schoolYear);

            // 4. Initialize controller and load data
            BalanceScannerController dialogController = new BalanceScannerController(dialog);
            dialogController.loadData(studentId, schoolYear);

            // 5. Show dialog
            dialog.setLocationRelativeTo(view);
            dialog.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtil.showError(view, "Failed to check balance: " + ex.getMessage());
        }
    }

    // =====================================================
    // SAVE ENROLLMENT
    // =====================================================
    private void saveEnrollment() {
        try {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setGradeLevel(
                    view.getCmbNewGradeLevel().getSelectedItem().toString()
            );

            // DIAGNOSTIC TEST SCHEDULE
            if (view.getDateDiagnosticSchedule().getDate() != null) {
                enrollment.setDiagnosticTestSchedule(
                        view.getDateDiagnosticSchedule()
                                .getDate()
                                .toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                );
            }

            // REQUIREMENT STATUS
            enrollment.setDiagnosticTestStatus(view.getChkDiagnosticPassed().isSelected());
            enrollment.setPsaStatus(view.getChkPsaSubmitted().isSelected());
            enrollment.setSf10Status(view.getChkSf10Submitted().isSelected());
            enrollment.setGoodMoralStatus(view.getChkGoodMoralSubmitted().isSelected());

            // ENROLLMENT STATUS
            String status = view.getCmbEnrollmentStatus().getSelectedItem().toString();
            int statusValue = switch (status) {
                case "Pending" ->
                    0;
                case "Enrolled" ->
                    1;
                case "Rejected" ->
                    2;
                default ->
                    0;
            };
            enrollment.setEnrollmentStatus(statusValue);

            // SAVE
            reEnrollmentService.createReEnrollment(enrollment);
            JOptionPane.showMessageDialog(
                    view,
                    "Student re-enrollment successful.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Failed to save re-enrollment.\n" + ex.getMessage(),
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
        dashboardPanel.setLayout(new java.awt.BorderLayout());
        dashboardPanel.add(panel, java.awt.BorderLayout.CENTER);
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }
}
