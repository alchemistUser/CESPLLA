/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import dao.EnrollmentDAO;
import dao.GuardianDAO;
import dao.PaymentDAO;
import dao.StudentDAO;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import models.BillingBreakdownItem;
import models.Enrollment;
import models.Guardian;
import models.Payment;
import models.Student;

import reports.RegistrationFormReport;

import reports.StatementOfAccountReport;

public class ReportService {

    private final StudentDAO studentDAO;
    private final GuardianDAO guardianDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final PaymentDAO paymentDAO;

    private final PaymentService paymentService;

    private final RegistrationFormReport registrationFormReport;
    private final StatementOfAccountReport statementOfAccountReport;

    public ReportService() {

        studentDAO = new StudentDAO();
        guardianDAO = new GuardianDAO();
        enrollmentDAO = new EnrollmentDAO();
        paymentDAO = new PaymentDAO();

        paymentService = new PaymentService();

        registrationFormReport = new RegistrationFormReport();
        statementOfAccountReport = new StatementOfAccountReport();
    }

    // =====================================================
    // REGISTRATION FORM
    // =====================================================
    public String generateRegistrationForm(int studentId)
            throws SQLException, FileNotFoundException {

        Student student = studentDAO.findById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student not found."
            );
        }

        Guardian guardian
                = guardianDAO.findGuardianByStudentId(studentId);

        if (guardian == null) {
            throw new IllegalArgumentException(
                    "Guardian not found."
            );
        }

        Enrollment enrollment
                = enrollmentDAO.findByStudentId(studentId);

        if (enrollment == null) {
            throw new IllegalArgumentException(
                    "Enrollment not found."
            );
        }

        return registrationFormReport.generate(
                student,
                guardian,
                enrollment
        );
    }

    // =====================================================
    // STATEMENT OF ACCOUNT
    // =====================================================
    public String generateStatementOfAccount(int studentId)
            throws SQLException, FileNotFoundException {

        Student student = studentDAO.findById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student not found."
            );
        }

        /*
         * TEMPORARY REPORT DEFAULT:
         *
         * Current student/enrollment model does not store payment_plan.
         * PaymentService requires gradeLevel + paymentPlan.
         *
         * Later, we can retrieve the latest payment_plan from payment table
         * or add payment_plan to a dedicated billing/enrollment preference table.
         */
        String paymentPlan = "ANNUAL";

        List<BillingBreakdownItem> billingItems
                = paymentService.getBillingBreakdown(
                        student.getGradeLevel(),
                        paymentPlan
                );

        List<Payment> payments
                = paymentDAO.findPaymentsByStudentId(studentId);

        BigDecimal totalCharges
                = paymentService.getTotalFee(
                        student.getGradeLevel(),
                        paymentPlan
                );

        BigDecimal totalPaid
                = paymentService.getTotalPaidByStudentId(studentId);

        BigDecimal remainingBalance
                = totalCharges.subtract(totalPaid);

        return statementOfAccountReport.generate(
                student,
                billingItems,
                payments,
                totalCharges,
                totalPaid,
                remainingBalance
        );
    }
}
