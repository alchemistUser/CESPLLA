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
import models.BillingBreakdownItem;
import models.Payment;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    /**
     * Records a payment after validation
     */
    public int recordPayment(Payment payment) throws SQLException {
        validatePayment(payment);
        return paymentDAO.recordPayment(payment);
    }

    /**
     * Gets payment history for a student
     */
    public List<Payment> getPaymentHistoryByStudentId(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID.");
        }
        return paymentDAO.findPaymentsByStudentId(studentId);
    }

    /**
     * Gets total amount paid by student
     */
    public BigDecimal getTotalPaidByStudentId(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID.");
        }
        BigDecimal totalPaid = paymentDAO.getTotalPaidByStudentId(studentId);
        return totalPaid == null ? BigDecimal.ZERO : totalPaid;
    }

    /**
     * Calculates total fee based on grade level and payment plan
     */
    public BigDecimal getTotalFee(String gradeLevel, String paymentPlan) {
        List<BillingBreakdownItem> items = getBillingBreakdown(gradeLevel, paymentPlan);
        BigDecimal total = BigDecimal.ZERO;

        for (BillingBreakdownItem item : items) {
            total = total.add(item.getAmount());
        }

        return total;
    }

    /**
     * Calculates remaining balance
     */
    public BigDecimal calculateBalance(int studentId, String gradeLevel, String paymentPlan) throws SQLException {
        BigDecimal totalFee = getTotalFee(gradeLevel, paymentPlan);
        BigDecimal totalPaid = getTotalPaidByStudentId(studentId);
        return totalFee.subtract(totalPaid);
    }

    /**
     * Gets billing breakdown items
     */
    public List<BillingBreakdownItem> getBillingBreakdown(String gradeLevel, String paymentPlan) {
        List<BillingBreakdownItem> items = new ArrayList<>();

        items.add(new BillingBreakdownItem(
                "Upon Enrollment",
                getUponEnrollmentFee(gradeLevel)
        ));

        switch (paymentPlan) {
            case "QUARTERLY":
                items.add(new BillingBreakdownItem("1st Quarter", getInstallmentFee(gradeLevel, paymentPlan)));
                items.add(new BillingBreakdownItem("2nd Quarter", getInstallmentFee(gradeLevel, paymentPlan)));
                items.add(new BillingBreakdownItem("3rd Quarter", getInstallmentFee(gradeLevel, paymentPlan)));
                items.add(new BillingBreakdownItem("4th Quarter", getInstallmentFee(gradeLevel, paymentPlan)));
                break;

            case "SEMI_ANNUAL":
                items.add(new BillingBreakdownItem("1st Semi-Annual", getInstallmentFee(gradeLevel, paymentPlan)));
                items.add(new BillingBreakdownItem("2nd Semi-Annual", getInstallmentFee(gradeLevel, paymentPlan)));
                break;

            case "ANNUAL":
                items.add(new BillingBreakdownItem("Annual Payment", getInstallmentFee(gradeLevel, paymentPlan)));
                break;

            default:
                throw new IllegalArgumentException("Invalid payment plan.");
        }

        addLearningWorktextBreakdown(items, gradeLevel);
        return items;
    }

    /**
     * Validates and builds payment from dialog data
     */
    public Payment validateAndBuildPayment(
            String amountText,
            String paymentMethod,
            String referenceNumber,
            String paymentPlan,
            LocalDate paymentDate,
            int studentId,
            String gradeLevel
    ) {
        // Validate amount
        if (amountText == null || amountText.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment amount is required.");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid payment amount format.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }

        // Validate payment method
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        String method = paymentMethod.trim();
        if (!method.equals("Cash") && !method.equals("GCash")
                && !method.equals("Bank Transfer") && !method.equals("Check")) {
            throw new IllegalArgumentException("Invalid payment method.");
        }

        // Validate payment plan
        if (paymentPlan == null || paymentPlan.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment plan is required.");
        }

        String plan = paymentPlan.trim();
        if (!plan.equals("QUARTERLY") && !plan.equals("SEMI_ANNUAL") && !plan.equals("ANNUAL")) {
            throw new IllegalArgumentException("Invalid payment plan.");
        }

        // Validate reference number based on payment method
        String resolvedRef = resolveReferenceNumber(method, referenceNumber);

        // Validate payment date
        if (paymentDate == null) {
            throw new IllegalArgumentException("Payment date is required.");
        }

        if (paymentDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Payment date cannot be in the future.");
        }

        // Check balance
        try {
            BigDecimal remainingBalance = calculateBalance(studentId, gradeLevel, plan);
            if (amount.compareTo(remainingBalance) > 0) {
                throw new IllegalArgumentException("Payment exceeds remaining balance of ₱"
                        + remainingBalance.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to calculate balance: " + ex.getMessage());
        }

        // Build payment object
        Payment payment = new Payment();
        payment.setStudentId(studentId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setPaymentPlan(plan);
        payment.setReferenceNumber(resolvedRef);
        payment.setPaymentDate(paymentDate);

        return payment;
    }

    /**
     * Validates if payment can be accepted
     */
    public boolean canAcceptPayment(BigDecimal amount, BigDecimal balance) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return amount.compareTo(balance) <= 0;
    }

    /**
     * Validates payment method requirements
     */
    public boolean validatePaymentMethod(String paymentMethod, String referenceNumber) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return false;
        }

        String method = paymentMethod.trim();
        if (method.equalsIgnoreCase("GCash") || method.equalsIgnoreCase("Bank Transfer")) {
            return referenceNumber != null && !referenceNumber.trim().isEmpty();
        }

        return true;
    }

    // ==================== PRIVATE HELPER METHODS ====================
    private void addLearningWorktextBreakdown(List<BillingBreakdownItem> items, String gradeLevel) {
        BigDecimal quarterlyWorktextFee = getQuarterlyLearningWorktextFee(gradeLevel);

        items.add(new BillingBreakdownItem("Learning Worktext - Quarter 1", quarterlyWorktextFee));
        items.add(new BillingBreakdownItem("Learning Worktext - Quarter 2", quarterlyWorktextFee));
        items.add(new BillingBreakdownItem("Learning Worktext - Quarter 3", quarterlyWorktextFee));
        items.add(new BillingBreakdownItem("Learning Worktext - Quarter 4", quarterlyWorktextFee));
    }

    private BigDecimal getUponEnrollmentFee(String gradeLevel) {
        if (isPreschool(gradeLevel)) {
            return new BigDecimal("3250");
        }
        if (isElem13(gradeLevel)) {
            return new BigDecimal("4750");
        }
        if (isElem46(gradeLevel)) {
            return new BigDecimal("5250");
        }
        throw new IllegalArgumentException("Invalid grade level: " + gradeLevel);
    }

    private BigDecimal getInstallmentFee(String gradeLevel, String paymentPlan) {
        switch (paymentPlan) {
            case "QUARTERLY":
                if (isPreschool(gradeLevel)) {
                    return new BigDecimal("3000");
                }
                if (isElem13(gradeLevel)) {
                    return new BigDecimal("5000");
                }
                if (isElem46(gradeLevel)) {
                    return new BigDecimal("5000");
                }
                break;
            case "SEMI_ANNUAL":
                if (isPreschool(gradeLevel)) {
                    return new BigDecimal("5500");
                }
                if (isElem13(gradeLevel)) {
                    return new BigDecimal("9500");
                }
                if (isElem46(gradeLevel)) {
                    return new BigDecimal("9500");
                }
                break;
            case "ANNUAL":
                if (isPreschool(gradeLevel)) {
                    return new BigDecimal("10300");
                }
                if (isElem13(gradeLevel)) {
                    return new BigDecimal("18300");
                }
                if (isElem46(gradeLevel)) {
                    return new BigDecimal("18300");
                }
                break;
        }
        throw new IllegalArgumentException("Invalid payment plan or grade level.");
    }

    private BigDecimal getQuarterlyLearningWorktextFee(String gradeLevel) {
        if (isPreschool(gradeLevel)) {
            return new BigDecimal("1250");
        }
        if ("1".equals(gradeLevel)) {
            return new BigDecimal("1800");
        }
        if ("2".equals(gradeLevel) || "3".equals(gradeLevel)) {
            return new BigDecimal("2000");
        }
        if (isElem46(gradeLevel)) {
            return new BigDecimal("2100");
        }
        throw new IllegalArgumentException("Invalid grade level: " + gradeLevel);
    }

    private String resolveReferenceNumber(String method, String referenceNumber) {
        if ("Cash".equals(method)) {
            if (referenceNumber == null || referenceNumber.trim().isEmpty()) {
                return "N/A";
            }
            return referenceNumber.trim();
        }

        if (referenceNumber == null || referenceNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference number is required for " + method + " payments.");
        }

        return referenceNumber.trim();
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment data is required.");
        }
        if (payment.getStudentId() <= 0) {
            throw new IllegalArgumentException("Student ID is required.");
        }
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required.");
        }
        if (payment.getPaymentPlan() == null || payment.getPaymentPlan().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment plan is required.");
        }
        if (payment.getPaymentDate() == null) {
            throw new IllegalArgumentException("Payment date is required.");
        }
    }

    private boolean isPreschool(String gradeLevel) {
        return "N".equals(gradeLevel) || "K1".equals(gradeLevel) || "K2".equals(gradeLevel);
    }

    private boolean isElem13(String gradeLevel) {
        return "1".equals(gradeLevel) || "2".equals(gradeLevel) || "3".equals(gradeLevel);
    }

    private boolean isElem46(String gradeLevel) {
        return "4".equals(gradeLevel) || "5".equals(gradeLevel) || "6".equals(gradeLevel);
    }
}
