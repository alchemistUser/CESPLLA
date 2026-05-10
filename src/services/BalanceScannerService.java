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
import java.math.BigDecimal;

import java.time.LocalDate;

public class BalanceScannerService {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    /**
     * Calculates total payments made by a student within a specific school year
     * range.
     */
    public BigDecimal getTotalPaidForYear(int studentId, String schoolYear) {
        try {
            int startYear = Integer.parseInt(schoolYear.split("-")[0]);
            LocalDate startDate = LocalDate.of(startYear, 6, 1);  // June 1
            LocalDate endDate = LocalDate.of(startYear + 1, 3, 31); // March 31 next year
            return paymentDAO.getTotalPaidByDateRange(studentId, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    /**
     * Returns base tuition fee for a grade level. NOTE: Replace with
     * FeeScheduleDAO when Phase 9 is implemented.
     */
    public BigDecimal getFeeForGradeLevel(String gradeLevel) {
        return switch (gradeLevel != null ? gradeLevel.trim() : "") {
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
     * Calculates outstanding balance for a specific school year.
     */
    public BigDecimal calculateBalanceForYear(int studentId, String schoolYear, String gradeLevel) {
        BigDecimal fee = getFeeForGradeLevel(gradeLevel);
        BigDecimal paid = getTotalPaidForYear(studentId, schoolYear);
        return fee.subtract(paid);
    }
}
