/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.EnrollmentDAO;
import dao.StudentDAO;
import models.Enrollment;
import models.Payment;
import models.Student;
import services.BillingService;
import services.PaymentService;
import utils.DialogUtil;
import utils.ValidationUtil;
import views.dialogs.payments.PaymentFormDialog;
import views.panels.payments.PaymentBillingPanel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.BillingBreakdownItem;

/**
 *
 * @author Pololoers
 */
public class PaymentController {

    private final PaymentBillingPanel view;
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final PaymentService paymentService;

    private Student selectedStudent;
    private Enrollment selectedEnrollment;

    private final BillingService billingService = new BillingService();

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

    private void handleSearchStudentBilling() {
        String keyword = view.getTxtSearchKeyword().getText().trim();

        if (ValidationUtil.isEmpty(keyword)) {
            DialogUtil.showWarning(view, "Please enter student name.");
            return;
        }

        try {
            // Search by name instead of LRN
            List<Student> students = studentDAO.searchByName(keyword);

            if (students.isEmpty()) {
                DialogUtil.showError(view, "No students found matching: " + keyword);
                return;
            }

            selectedStudent = students.get(0);
            selectedEnrollment = enrollmentDAO.findByStudentId(selectedStudent.getStudentId());

            // Display student info - Use correct getter names
            view.getLblStudentNameValue().setText(
                    selectedStudent.getFirstName() + " " + selectedStudent.getLastName()
            );
            view.getLblGradeLevelValue().setText(selectedStudent.getGradeLevel());
            view.getLblStudentIdValue().setText(String.valueOf(selectedStudent.getStudentId()));

            if (selectedEnrollment != null) {
                view.getLblEnrollmentIdValue().setText(String.valueOf(selectedEnrollment.getEnrollmentId()));
                view.getLblEnrollmentStatusValue().setText(resolveEnrollmentStatus(selectedEnrollment));

                // Sync Payment Plan
                view.getCmbPaymentPlan().setSelectedItem(selectedEnrollment.getPaymentScheme());
            }

            updateBalanceLabels(selectedStudent.getStudentId());
            loadPaymentHistory(selectedStudent.getStudentId());

            view.getBtnRecordPayment().setEnabled(true);
            view.getBtnGenerateSOA().setEnabled(true);

        } catch (Exception ex) {
            DialogUtil.showError(view, "Search failed: " + ex.getMessage());
        }
    }

    // ✅ ADDED THIS MISSING METHOD
    private void loadPaymentHistory(int studentId) {
        try {
            List<Payment> payments = paymentService.getPaymentHistoryByStudentId(studentId);

            DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
            model.setRowCount(0);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Payment payment : payments) {
                model.addRow(new Object[]{
                    payment.getPaymentId(),
                    payment.getPaymentDate() != null ? payment.getPaymentDate().format(formatter) : "",
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

    private void loadBillingInformation() throws SQLException {
        int studentId = selectedStudent.getStudentId();
        String gradeLevel = String.valueOf(selectedEnrollment.getGradeLevel());
        String paymentPlan = view.getSelectedPaymentPlan();

        BigDecimal totalFee = paymentService.getTotalFee(gradeLevel, paymentPlan);
        BigDecimal totalPaid = paymentService.getTotalPaidByStudentId(studentId);
        BigDecimal balance = totalFee.subtract(totalPaid);

        view.getLblTotalFeeValue().setText(formatCurrency(totalFee));
        view.getLblTotalPaidValue().setText(formatCurrency(totalPaid));
        view.getLblBalanceValue().setText(formatCurrency(balance));

        refreshBillingBreakdownTable(gradeLevel, paymentPlan);
        refreshPaymentTable(studentId);
    }

    private void refreshBillingBreakdownTable(String gradeLevel, String paymentPlan) {
        List<BillingBreakdownItem> items = paymentService.getBillingBreakdown(gradeLevel, paymentPlan);
        DefaultTableModel model = (DefaultTableModel) view.getTblBillingBreakdown().getModel();
        model.setRowCount(0);

        for (BillingBreakdownItem item : items) {
            model.addRow(new Object[]{item.getDescription(), formatCurrency(item.getAmount())});
        }
    }

    private void refreshPaymentTable(int studentId) throws SQLException {
        List<Payment> payments = paymentService.getPaymentHistoryByStudentId(studentId);
        DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
        model.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Payment payment : payments) {
            model.addRow(new Object[]{
                payment.getPaymentId(),
                payment.getPaymentDate() != null ? payment.getPaymentDate().format(formatter) : "",
                formatCurrency(payment.getAmount()),
                payment.getPaymentMethod(),
                payment.getPaymentPlan(),
                payment.getReferenceNumber()
            });
        }
    }

    private void handlePaymentPlanChanged() {
        if (selectedStudent != null && selectedEnrollment != null) {
            try {
                loadBillingInformation();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleRefresh() {
        if (selectedStudent != null) {
            try {
                loadBillingInformation();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            resetDisplay();
        }
    }

    private void handleRecordPayment() {
        if (selectedStudent == null) {
            DialogUtil.showWarning(view, "Please search and select a student first.");
            return;
        }

        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(view);
        PaymentFormDialog dialog = new PaymentFormDialog(owner, true);
        dialog.setLocationRelativeTo(view);

        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());

        dialog.getBtnSavePayment().addActionListener(e -> {
            try {
                Payment payment = buildPayment(dialog);
                paymentService.recordPayment(payment);

                DialogUtil.showSuccess(dialog, "Payment recorded successfully.");
                dialog.dispose();
                refreshPaymentTable(selectedStudent.getStudentId());
                updateBalanceLabels(selectedStudent.getStudentId());

            } catch (IllegalArgumentException ex) {
                DialogUtil.showWarning(dialog, ex.getMessage());
            } catch (Exception ex) {
                DialogUtil.showError(dialog, "Database error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private Payment buildPayment(PaymentFormDialog dialog) {
        String amountText = dialog.getAmountText();
        if (amountText.isEmpty()) {
            throw new IllegalArgumentException("Amount required.");
        }

        BigDecimal amount = new BigDecimal(amountText);
        String gradeLevel = selectedEnrollment.getGradeLevel();
        String paymentPlan = view.getSelectedPaymentPlan();

        // ✅ FIXED: Wrap in try-catch for SQLException
        BigDecimal balance;
        try {
            balance = paymentService.calculateBalance(selectedStudent.getStudentId(), gradeLevel, paymentPlan);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to calculate balance: " + ex.getMessage());
        }

        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Payment exceeds balance.");
        }

        LocalDate paymentDate = dialog.getPaymentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Payment payment = new Payment();
        payment.setStudentId(selectedStudent.getStudentId());
        payment.setAmount(amount);
        payment.setPaymentMethod(dialog.getSelectedPaymentMethod());
        payment.setPaymentPlan(paymentPlan);
        payment.setReferenceNumber(resolveReferenceNumber(dialog));
        payment.setPaymentDate(paymentDate);

        return payment;
    }

    private String resolveReferenceNumber(PaymentFormDialog dialog) {
        String method = dialog.getSelectedPaymentMethod();
        String ref = dialog.getReferenceNumber();

        if ("Cash".equals(method)) {
            return (ref == null || ref.trim().isEmpty()) ? "N/A" : ref.trim();
        }
        if (ref == null || ref.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference number required for " + method);
        }
        return ref.trim();
    }

    private void handleGenerateSOA() {
        if (selectedStudent == null) {
            DialogUtil.showWarning(view, "Please select a student first.");
            return;
        }
        JOptionPane.showMessageDialog(view, "SOA generation will be added in Reports phase.", "Info", JOptionPane.INFORMATION_MESSAGE);
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
        ((DefaultTableModel) view.getTblPayments().getModel()).setRowCount(0);
        view.getBtnRecordPayment().setEnabled(false);
        view.getBtnGenerateSOA().setEnabled(false);
    }

    private void updateBalanceLabels(int studentId) {
        try {
            BigDecimal totalFee = billingService.getTotalFee(studentId);
            BigDecimal totalPaid = billingService.getTotalPaid(studentId);
            BigDecimal balance = billingService.calculateBalance(studentId);

            // Use correct getter names
            view.getLblTotalFeeValue().setText(billingService.formatAmount(totalFee));
            view.getLblTotalPaidValue().setText(billingService.formatAmount(totalPaid));
            view.getLblBalanceValue().setText(billingService.formatAmount(balance));

            view.getLblBalanceValue().setForeground(
                    balance.compareTo(BigDecimal.ZERO) > 0 ? new java.awt.Color(255, 0, 0) : new java.awt.Color(0, 128, 0)
            );
        } catch (Exception ex) {
            DialogUtil.showError(view, "Failed to calculate balance: " + ex.getMessage());
        }
    }

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

    private String formatCurrency(BigDecimal amount) {
        return amount == null ? "₱0.00" : "₱" + amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
