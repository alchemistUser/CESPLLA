/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import utils.DialogUtil;
import utils.SchoolYearUtil;
import views.dialogs.students.BalanceScannerDialog;
import services.BalanceScannerService;
import dao.PaymentDAO;
import models.Payment;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

public class BalanceScannerController {

    private final BalanceScannerDialog dialog;
    private final BalanceScannerService service;
    private final PaymentDAO paymentDAO;

    public BalanceScannerController(BalanceScannerDialog dialog) {
        this.dialog = dialog;
        this.service = new BalanceScannerService();
        this.paymentDAO = new PaymentDAO();
        attachEvents();
    }

    private void attachEvents() {
        dialog.getBtnClose().addActionListener(e -> dialog.dispose());
    }

    /**
     * Loads balance data for the specified student and school year. Must be
     * called after setting dialog.setStudentId() and dialog.setSchoolYear()
     */
    public void loadData(int studentId, String schoolYear) {
        // 1. Get date range for the school year
        int startYear = Integer.parseInt(schoolYear.split("-")[0]);
        LocalDate startDate = LocalDate.of(startYear, 6, 1);
        LocalDate endDate = LocalDate.of(startYear + 1, 3, 31);

        // 2. Get total paid for this year
        BigDecimal totalPaid = paymentDAO.getTotalPaidByDateRange(studentId, startDate, endDate);

        // 3. Get fee for grade level (you'll need to pass gradeLevel or fetch it)
        // For now, using a placeholder. Replace with actual gradeLevel from student/enrollment
        BigDecimal totalFee = service.getFeeForGradeLevel("1"); // Example: Grade 1

        // 4. Calculate balance
        BigDecimal balance = totalFee.subtract(totalPaid);

        // 5. Update UI
        dialog.getLblTotalFee().setText("₱ " + totalFee.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dialog.getLblTotalPaid().setText("₱ " + totalPaid.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            dialog.getLblBalance().setText("₱ " + balance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            dialog.getLblBalance().setForeground(new java.awt.Color(255, 0, 0)); // Red
        } else {
            dialog.getLblBalance().setText("CLEARED");
            dialog.getLblBalance().setForeground(new java.awt.Color(0, 128, 0)); // Green
        }

        // 6. Load payment history table
        List<Payment> payments = paymentDAO.findPaymentsByStudentAndDateRange(studentId, startDate, endDate);
        DefaultTableModel model = (DefaultTableModel) dialog.getTblPaymentHistory().getModel();
        model.setRowCount(0);

        for (Payment payment : payments) {
            model.addRow(new Object[]{
                payment.getPaymentDate(),
                "₱ " + payment.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                payment.getPaymentMethod(),
                payment.getReferenceNumber()
            });
        }

        if (payments.isEmpty()) {
            DialogUtil.showWarning(dialog, "No payments found for " + schoolYear);
        }
    }
}
