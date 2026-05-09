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

    public int recordPayment(Payment payment) throws SQLException {
        validatePayment(payment);
        return paymentDAO.recordPayment(payment);
    }

    public List<Payment> getPaymentHistoryByStudentId(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID.");
        }

        return paymentDAO.findPaymentsByStudentId(studentId);
    }

    public BigDecimal getTotalPaidByStudentId(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID.");
        }

        BigDecimal totalPaid = paymentDAO.getTotalPaidByStudentId(studentId);
        return totalPaid == null ? BigDecimal.ZERO : totalPaid;
    }

    public BigDecimal getTotalFee(String gradeLevel, String paymentPlan) {
        List<BillingBreakdownItem> items = getBillingBreakdown(gradeLevel, paymentPlan);

        BigDecimal total = BigDecimal.ZERO;

        for (BillingBreakdownItem item : items) {
            total = total.add(item.getAmount());
        }

        return total;
    }

    public BigDecimal calculateBalance(int studentId, String gradeLevel, String paymentPlan) throws SQLException {
        BigDecimal totalFee = getTotalFee(gradeLevel, paymentPlan);
        BigDecimal totalPaid = getTotalPaidByStudentId(studentId);

        return totalFee.subtract(totalPaid);
    }

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

        throw new IllegalArgumentException("Invalid grade level.");
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

        throw new IllegalArgumentException("Invalid grade level.");
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment data is required.");
        }

        if (payment.getStudentId() <= 0) {
            throw new IllegalArgumentException("Student ID is required.");
        }

        if (payment.getAmount() == null) {
            throw new IllegalArgumentException("Payment amount is required.");
        }

        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }

        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        String method = payment.getPaymentMethod().trim();

        if (!method.equals("Cash")
                && !method.equals("GCash")
                && !method.equals("Bank Transfer")
                && !method.equals("Check")) {
            throw new IllegalArgumentException("Invalid payment method.");
        }

        if (payment.getPaymentPlan() == null || payment.getPaymentPlan().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment plan is required.");
        }

        String plan = payment.getPaymentPlan().trim();

        if (!plan.equals("QUARTERLY")
                && !plan.equals("SEMI_ANNUAL")
                && !plan.equals("ANNUAL")) {
            throw new IllegalArgumentException("Invalid payment plan.");
        }

        if (!method.equals("Cash")
                && (payment.getReferenceNumber() == null
                || payment.getReferenceNumber().trim().isEmpty()
                || payment.getReferenceNumber().trim().equalsIgnoreCase("N/A"))) {

            throw new IllegalArgumentException(
                    "Reference number is required for " + method + " payments."
            );
        }

        if (method.equals("Cash")
                && (payment.getReferenceNumber() == null
                || payment.getReferenceNumber().trim().isEmpty())) {

            payment.setReferenceNumber("N/A");
        }
        
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        if (payment.getPaymentDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Payment date cannot be in the future.");
        }
    }

    private boolean isPreschool(String gradeLevel) {
        return "N".equals(gradeLevel)
                || "K1".equals(gradeLevel)
                || "K2".equals(gradeLevel);
    }

    private boolean isElem13(String gradeLevel) {
        return "1".equals(gradeLevel)
                || "2".equals(gradeLevel)
                || "3".equals(gradeLevel);
    }

    private boolean isElem46(String gradeLevel) {
        return "4".equals(gradeLevel)
                || "5".equals(gradeLevel)
                || "6".equals(gradeLevel);
    }
}
