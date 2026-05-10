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
import dao.EnrollmentDAO;
import dao.StudentDAO;
import models.Enrollment;
import models.Payment;
import models.Student;
import services.PaymentService;
import utils.DialogUtil;
import utils.ValidationUtil;
import views.dialogs.payments.PaymentFormDialog;
import views.panels.payments.PaymentBillingPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.BillingBreakdownItem;

public class PaymentController {

    private final PaymentBillingPanel view;
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final PaymentService paymentService;

    private Student selectedStudent;
    private Enrollment selectedEnrollment;

    public PaymentController(PaymentBillingPanel view) {
        this.view = view;
        this.studentDAO = new StudentDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.paymentService = new PaymentService();

        attachEvents();
        resetDisplay();
    }

    private void attachEvents() {
        view.getBtnSearch().addActionListener(e -> handleSearchStudentBilling());
        view.getBtnRefresh().addActionListener(e -> handleRefresh());
        view.getBtnRecordPayment().addActionListener(e -> handleRecordPayment());
        view.getBtnGenerateSOA().addActionListener(e -> handleGenerateSOA());

        view.getCmbPaymentPlan().addActionListener(e -> handlePaymentPlanChanged());
    }

    // =====================================================
    // SEARCH STUDENT BY NAME (FIXED)
    // =====================================================
    private void handleSearchStudentBilling() {
        String keyword = view.getTxtSearchKeyword().getText().trim();

        if (ValidationUtil.isEmpty(keyword)) {
            DialogUtil.showWarning(view, "Please enter a student name.");
            return;
        }

        try {
            // 1. Search by Name
            List<Student> results = studentDAO.searchByName(keyword);

            if (results.isEmpty()) {
                DialogUtil.showError(view, "No students found matching: " + keyword);
                return;
            }

            // 2. Select first result (You can expand this to show a selection dialog later)
            selectedStudent = results.get(0);

            // 3. Find current enrollment for this student
            selectedEnrollment = enrollmentDAO.findByStudentId(selectedStudent.getStudentId());

            if (selectedEnrollment == null) {
                DialogUtil.showWarning(view, "Student found but no enrollment record exists.");
                return;
            }

            // 4. Update UI
            updateStudentDisplay(selectedStudent, selectedEnrollment);

            // 5. Load Data
            loadBillingInformation();
            loadPaymentHistory(selectedStudent.getStudentId());

        } catch (Exception ex) {
            DialogUtil.showError(view, "Search failed: " + ex.getMessage());
        }
    }

    // =====================================================
    // UPDATE DISPLAY HELPERS
    // =====================================================
    private void updateStudentDisplay(Student student, Enrollment enrollment) {
        view.getLblStudentIdValue().setText(String.valueOf(student.getStudentId()));
        view.getLblStudentNameValue().setText(buildStudentName(student));
        view.getLblGradeLevelValue().setText(formatGradeLevel(student.getGradeLevel()));
        view.getLblEnrollmentIdValue().setText(String.valueOf(enrollment.getEnrollmentId()));
        view.getLblEnrollmentStatusValue().setText(resolveEnrollmentStatus(enrollment));

        // Sync payment plan combo box with enrollment data
        view.getCmbPaymentPlan().setSelectedItem(enrollment.getPaymentScheme());
    }

    // =====================================================
    // LOAD BILLING INFO (FEES & BALANCE)
    // =====================================================
    private void loadBillingInformation() {
        if (selectedStudent == null || selectedEnrollment == null) {
            return;
        }

        try {
            int studentId = selectedStudent.getStudentId();
            String gradeLevel = selectedEnrollment.getGradeLevel();
            String paymentPlan = view.getSelectedPaymentPlan();

            // Calculate Totals
            BigDecimal totalFee = paymentService.getTotalFee(gradeLevel, paymentPlan);
            BigDecimal totalPaid = paymentService.getTotalPaidByStudentId(studentId);
            BigDecimal balance = totalFee.subtract(totalPaid);

            // Update Labels
            view.getLblTotalFeeValue().setText(formatCurrency(totalFee));
            view.getLblTotalPaidValue().setText(formatCurrency(totalPaid));
            view.getLblBalanceValue().setText(formatCurrency(balance));

            // Color code balance
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                view.getLblBalanceValue().setForeground(new java.awt.Color(255, 0, 0)); // Red
            } else {
                view.getLblBalanceValue().setForeground(new java.awt.Color(0, 128, 0)); // Green
            }

            // Update Breakdown Table
            refreshBillingBreakdownTable(gradeLevel, paymentPlan);

            // Enable Action Buttons
            view.getBtnRecordPayment().setEnabled(true);
            view.getBtnGenerateSOA().setEnabled(true);

        } catch (Exception ex) {
            DialogUtil.showError(view, "Failed to load billing info: " + ex.getMessage());
        }
    }

    // =====================================================
    // LOAD PAYMENT HISTORY (MISSING METHOD ADDED)
    // =====================================================
    private void loadPaymentHistory(int studentId) {
        try {
            List<Payment> payments = paymentService.getPaymentHistoryByStudentId(studentId);

            DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
            model.setRowCount(0); // Clear existing rows

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Payment payment : payments) {
                model.addRow(new Object[]{
                    payment.getPaymentId(),
                    payment.getPaymentDate() != null ? payment.getPaymentDate().format(formatter) : "N/A",
                    formatCurrency(payment.getAmount()),
                    payment.getPaymentMethod(),
                    payment.getPaymentPlan(),
                    payment.getReferenceNumber()
                });
            }
        } catch (SQLException ex) {
            DialogUtil.showError(view, "Failed to load payment history: " + ex.getMessage());
        }
    }

    private void refreshBillingBreakdownTable(String gradeLevel, String paymentPlan) {
        List<BillingBreakdownItem> items = paymentService.getBillingBreakdown(gradeLevel, paymentPlan);
        DefaultTableModel model = (DefaultTableModel) view.getTblBillingBreakdown().getModel();
        model.setRowCount(0);

        for (BillingBreakdownItem item : items) {
            model.addRow(new Object[]{
                item.getDescription(),
                formatCurrency(item.getAmount())
            });
        }
    }

    // =====================================================
    // EVENT HANDLERS
    // =====================================================
    private void handlePaymentPlanChanged() {
        if (selectedEnrollment != null) {
            loadBillingInformation();
        }
    }

    private void handleRefresh() {
        if (selectedStudent != null) {
            loadBillingInformation();
            loadPaymentHistory(selectedStudent.getStudentId());
        } else {
            resetDisplay();
        }
    }

    private void handleRecordPayment() {
        if (selectedStudent == null || selectedEnrollment == null) {
            DialogUtil.showWarning(view, "Please search and select a student first.");
            return;
        }

        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(view);
        PaymentFormDialog dialog = new PaymentFormDialog(owner, true);
        dialog.setLocationRelativeTo(view);

        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());

        dialog.getBtnSavePayment().addActionListener(e -> {
            try {
                // Use PaymentService to validate and build the payment object
                Payment payment = paymentService.validateAndBuildPayment(
                        dialog.getAmountText(),
                        dialog.getSelectedPaymentMethod(),
                        dialog.getReferenceNumber(),
                        view.getSelectedPaymentPlan(),
                        dialog.getPaymentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        selectedStudent.getStudentId(),
                        selectedEnrollment.getGradeLevel()
                );

                // Record Payment
                paymentService.recordPayment(payment);

                DialogUtil.showSuccess(dialog, "Payment recorded successfully.");
                dialog.dispose();

                // Refresh Data
                loadBillingInformation();
                loadPaymentHistory(selectedStudent.getStudentId());

            } catch (IllegalArgumentException ex) {
                DialogUtil.showWarning(dialog, ex.getMessage());
            } catch (Exception ex) {
                DialogUtil.showError(dialog, "Database error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void handleGenerateSOA() {
        javax.swing.JOptionPane.showMessageDialog(
                view,
                "Statement of Account generation will be added in the Reports phase.",
                "Information",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void resetDisplay() {
        selectedStudent = null;
        selectedEnrollment = null;

        view.getLblStudentIdValue().setText("-");
        view.getLblStudentNameValue().setText("-");
        view.getLblGradeLevelValue().setText("-");
        view.getLblEnrollmentIdValue().setText("-");
        view.getLblEnrollmentStatusValue().setText("-");

        view.getLblTotalFeeValue().setText("₱0.00");
        view.getLblTotalPaidValue().setText("₱0.00");
        view.getLblBalanceValue().setText("₱0.00");
        view.getLblBalanceValue().setForeground(new java.awt.Color(255, 0, 0));

        ((DefaultTableModel) view.getTblPayments().getModel()).setRowCount(0);
        ((DefaultTableModel) view.getTblBillingBreakdown().getModel()).setRowCount(0);

        view.getBtnRecordPayment().setEnabled(false);
        view.getBtnGenerateSOA().setEnabled(false);
    }

    // =====================================================
    // FORMATTERS
    // =====================================================
    private String buildStudentName(Student s) {
        return s.getFirstName() + " " + (s.getMiddleName() != null ? s.getMiddleName() + " " : "") + s.getLastName();
    }

    private String resolveEnrollmentStatus(Enrollment e) {
        if (e == null) {
            return "-";
        }
        return switch (e.getEnrollmentStatus()) {
            case 0 ->
                "Pending";
            case 1 ->
                "Enrolled";
            case 2 ->
                "Rejected";
            default ->
                "Unknown";
        };
    }

    private String formatGradeLevel(String db) {
        return switch (db != null ? db.trim() : "") {
            case "N" ->
                "Nursery";
            case "K1" ->
                "Junior Kindergarten";
            case "K2" ->
                "Senior Kindergarten";
            default ->
                db;
        };
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "₱0.00";
        }
        return "₱" + amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

}
