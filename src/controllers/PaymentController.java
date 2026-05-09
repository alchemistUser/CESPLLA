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
import dao.StudentDAO;
import models.Enrollment;
import models.Payment;
import models.Student;
import services.PaymentService;
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

    private void handleSearchStudentBilling() {
        String keyword = view.getSearchKeyword();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter a student name or ID.");
            return;
        }

        try {
            List<Student> students = studentDAO.searchStudents(keyword);

            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(view, "No student found.");
                resetDisplay();
                return;
            }

            selectedStudent = students.get(0);
            selectedEnrollment = enrollmentDAO.findByStudentId(selectedStudent.getStudentId());

            if (selectedEnrollment == null) {
                JOptionPane.showMessageDialog(view, "Student has no enrollment record.");
                resetDisplay();
                return;
            }

            loadBillingInformation();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Database error while searching billing record.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void loadBillingInformation() throws SQLException {
        int studentId = selectedStudent.getStudentId();
        String gradeLevel = String.valueOf(selectedEnrollment.getGradeLevel());
        String paymentPlan = view.getSelectedPaymentPlan();

        BigDecimal totalFee = paymentService.getTotalFee(gradeLevel, paymentPlan);
        BigDecimal totalPaid = paymentService.getTotalPaidByStudentId(studentId);
        BigDecimal balance = totalFee.subtract(totalPaid);

        view.getLblStudentIdValue().setText(String.valueOf(studentId));
        view.getLblStudentNameValue().setText(buildStudentName(selectedStudent));
        view.getLblGradeLevelValue().setText(formatGradeLevel(gradeLevel));
        view.getLblEnrollmentIdValue().setText(String.valueOf(selectedEnrollment.getEnrollmentId()));
        view.getLblEnrollmentStatusValue().setText(resolveEnrollmentStatus(selectedEnrollment));

        view.getLblTotalFeeValue().setText(formatCurrency(totalFee));
        view.getLblTotalPaidValue().setText(formatCurrency(totalPaid));
        view.getLblBalanceValue().setText(formatCurrency(balance));

        refreshBillingBreakdownTable(gradeLevel, paymentPlan);
        refreshPaymentTable(studentId);

        view.getBtnRecordPayment().setEnabled(true);
        view.getBtnGenerateSOA().setEnabled(true);
    }

    private void refreshBillingBreakdownTable(String gradeLevel, String paymentPlan) {

        List<BillingBreakdownItem> breakdownItems
                = paymentService.getBillingBreakdown(gradeLevel, paymentPlan);

        DefaultTableModel model
                = (DefaultTableModel) view.getTblBillingBreakdown().getModel();

        model.setRowCount(0);

        for (BillingBreakdownItem item : breakdownItems) {
            model.addRow(new Object[]{
                item.getDescription(),
                formatCurrency(item.getAmount())
            });
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
        if (selectedStudent == null || selectedEnrollment == null) {
            return;
        }

        try {
            loadBillingInformation();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Database error while updating billing summary.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void handleRefresh() {
        try {
            if (selectedStudent != null && selectedEnrollment != null) {
                loadBillingInformation();
            } else {
                resetDisplay();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Database error while refreshing payment data.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void handleRecordPayment() {
        if (selectedStudent == null || selectedEnrollment == null) {
            JOptionPane.showMessageDialog(view, "Please search and select a student billing record first.");
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

                JOptionPane.showMessageDialog(dialog, "Payment recorded successfully.");

                dialog.dispose();
                loadBillingInformation();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        ex.getMessage(),
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        dialog,
                        "Database error while recording payment.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        dialog,
                        "Unexpected error occurred while recording payment.",
                        "System Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        dialog.setVisible(true);
    }

    private Payment buildPayment(PaymentFormDialog dialog) {

        String amountText = dialog.getAmountText();

        if (amountText.isEmpty()) {
            throw new IllegalArgumentException("Payment amount is required.");
        }

        BigDecimal amount;

        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }

        String gradeLevel = String.valueOf(selectedEnrollment.getGradeLevel());

        String paymentPlan = view.getSelectedPaymentPlan();

        BigDecimal remainingBalance;

        try {
            remainingBalance = paymentService.calculateBalance(
                    selectedStudent.getStudentId(),
                    gradeLevel,
                    paymentPlan
            );
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (amount.compareTo(remainingBalance) > 0) {
            throw new IllegalArgumentException(
                    "Payment exceeds remaining balance."
            );
        }

        if (dialog.getPaymentDate() == null) {
            throw new IllegalArgumentException("Payment date is required.");
        }

        LocalDate paymentDate = dialog.getPaymentDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

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
        String referenceNumber = dialog.getReferenceNumber();

        if ("Cash".equals(method)) {
            if (referenceNumber == null || referenceNumber.trim().isEmpty()) {
                return "N/A";
            }

            return referenceNumber.trim();
        }

        if (referenceNumber == null || referenceNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Reference number is required for " + method + " payments."
            );
        }

        return referenceNumber.trim();
    }

    private void handleGenerateSOA() {
        if (selectedStudent == null || selectedEnrollment == null) {
            JOptionPane.showMessageDialog(view, "Please search and select a student billing record first.");
            return;
        }

        JOptionPane.showMessageDialog(view, "Statement of Account generation will be added in the Reports phase.");
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

        DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
        model.setRowCount(0);

        DefaultTableModel breakdownModel
                = (DefaultTableModel) view.getTblBillingBreakdown().getModel();

        breakdownModel.setRowCount(0);

        view.getBtnRecordPayment().setEnabled(false);
        view.getBtnGenerateSOA().setEnabled(false);
    }

    private String buildStudentName(Student student) {
        String middleName = student.getMiddleName() == null ? "" : " " + student.getMiddleName();
        return student.getFirstName() + middleName + " " + student.getLastName();
    }

    private String resolveEnrollmentStatus(Enrollment enrollment) {
        if (enrollment == null) {
            return "-";
        }

        Object status = enrollment.getEnrollmentStatus();

        if (status == null) {
            return "-";
        }

        return String.valueOf(status);
    }

    private String formatGradeLevel(String dbValue) {
        if (dbValue == null) {
            return "";
        }

        switch (dbValue) {
            case "N":
                return "Nursery";
            case "K1":
                return "Junior Kindergarten";
            case "K2":
                return "Senior Kindergarten";
            default:
                return dbValue;
        }
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        return "₱" + amount.setScale(2);
    }
}
