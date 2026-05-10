/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import dao.PaymentDAO;
import dao.EnrollmentDAO;
import models.Enrollment;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class BillingService {

    private final PaymentDAO paymentDAO;
    private final EnrollmentDAO enrollmentDAO;

    public BillingService() {
        this.paymentDAO = new PaymentDAO();
        this.enrollmentDAO = new EnrollmentDAO();
    }

    /**
     * Gets total fee for a student based on grade level. NOTE: Replace with
     * FeeScheduleDAO when Phase 9 is implemented.
     */
    public BigDecimal getTotalFee(int studentId) throws SQLException {
        // Get student's current enrollment to determine grade level
        Enrollment enrollment = enrollmentDAO.findByStudentId(studentId);

        if (enrollment == null) {
            throw new SQLException("No enrollment found for student ID: " + studentId);
        }

        String gradeLevel = enrollment.getGradeLevel();
        return getFeeForGradeLevel(gradeLevel);
    }

    /**
     * Gets total amount paid by student.
     */
    public BigDecimal getTotalPaid(int studentId) throws SQLException {
        return paymentDAO.getTotalPaidByStudentId(studentId);
    }

    /**
     * Calculates remaining balance for a student.
     */
    public BigDecimal calculateBalance(int studentId) throws SQLException {
        BigDecimal totalFee = getTotalFee(studentId);
        BigDecimal totalPaid = getTotalPaid(studentId);

        return totalFee.subtract(totalPaid);
    }

    /**
     * Validates if payment amount is acceptable.
     */
    public boolean canAcceptPayment(BigDecimal amount, BigDecimal balance) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return amount.compareTo(balance) <= 0;
    }

    /**
     * Validates payment method requirements.
     */
    public boolean validatePaymentMethod(String paymentMethod, String referenceNumber) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return false;
        }

        if (paymentMethod.equalsIgnoreCase("GCash")
                || paymentMethod.equalsIgnoreCase("Bank Transfer")) {
            return referenceNumber != null && !referenceNumber.trim().isEmpty();
        }

        return true;
    }

    /**
     * Helper method to get fee based on grade level. TODO: Replace with
     * FeeScheduleDAO lookup
     */
    private BigDecimal getFeeForGradeLevel(String gradeLevel) {
        if (gradeLevel == null) {
            return BigDecimal.ZERO;
        }

        return switch (gradeLevel.trim()) {
            case "N" ->
                new BigDecimal("15000.00");
            case "K1", "K2" ->
                new BigDecimal("18000.00");
            case "1", "2", "3", "4", "5", "6" ->
                new BigDecimal("22000.00");
            default ->
                new BigDecimal("20000.00");
        };
    }

    /**
     * Formats amount for display.
     */
    public String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "₱ 0.00";
        }
        return "₱ " + amount.setScale(2, RoundingMode.HALF_UP).toString();
    }

}
