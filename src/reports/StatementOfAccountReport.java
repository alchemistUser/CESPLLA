/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

/**
 *
 * @author Pololoers
 */

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import models.Student;
import models.Payment;

import models.BillingBreakdownItem;

public class StatementOfAccountReport {

    public String generate(
            Student student,
            List<BillingBreakdownItem> billingItems,
            List<Payment> payments,
            BigDecimal totalCharges,
            BigDecimal totalPaid,
            BigDecimal remainingBalance
    ) throws FileNotFoundException {

        String fileName = "Statement_Of_Account_Student_"
                + student.getStudentId()
                + "_"
                + getTimestamp()
                + ".pdf";

        Document document = PdfGeneratorUtil.createDocument(fileName);

        document.add(PdfGeneratorUtil.createTitle(
                "PRECIOUS LITTLE LIGHTS ACADEMY"
        ));

        document.add(PdfGeneratorUtil.createParagraph(
                "Official Statement of Account"
        ));

        // =====================================================
        // STUDENT INFORMATION
        // =====================================================
        document.add(PdfGeneratorUtil.createSectionHeader(
                "Student Information"
        ));

        Table studentTable = PdfGeneratorUtil.createTable(
                new float[]{30, 70}
        );

        studentTable.addCell(PdfGeneratorUtil.createHeaderCell("Field"));
        studentTable.addCell(PdfGeneratorUtil.createHeaderCell("Details"));

        studentTable.addCell(PdfGeneratorUtil.createCell("Student ID"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                String.valueOf(student.getStudentId())
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Student Name"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                buildFullName(
                        student.getFirstName(),
                        student.getMiddleName(),
                        student.getLastName()
                )
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Grade Level"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(student.getGradeLevel())
        ));

        document.add(studentTable);

        // =====================================================
        // BILLING BREAKDOWN
        // =====================================================
        document.add(PdfGeneratorUtil.createSectionHeader(
                "Billing Breakdown"
        ));

        Table billingTable = PdfGeneratorUtil.createTable(
                new float[]{70, 30}
        );

        billingTable.addCell(PdfGeneratorUtil.createHeaderCell("Description"));
        billingTable.addCell(PdfGeneratorUtil.createHeaderCell("Amount"));

        if (billingItems == null || billingItems.isEmpty()) {

            billingTable.addCell(PdfGeneratorUtil.createCell("No billing records found."));
            billingTable.addCell(PdfGeneratorUtil.createCell("N/A"));

        } else {

            for (BillingBreakdownItem item : billingItems) {

                billingTable.addCell(PdfGeneratorUtil.createCell(
                        safeText(item.getDescription())
                ));

                billingTable.addCell(PdfGeneratorUtil.createCell(
                        formatMoney(item.getAmount())
                ));
            }
        }

        document.add(billingTable);

        // =====================================================
        // PAYMENT HISTORY
        // =====================================================
        document.add(PdfGeneratorUtil.createSectionHeader(
                "Payment History"
        ));

        Table paymentTable = PdfGeneratorUtil.createTable(
                new float[]{25, 25, 25, 25}
        );

        paymentTable.addCell(PdfGeneratorUtil.createHeaderCell("Date"));
        paymentTable.addCell(PdfGeneratorUtil.createHeaderCell("Amount"));
        paymentTable.addCell(PdfGeneratorUtil.createHeaderCell("Method"));
        paymentTable.addCell(PdfGeneratorUtil.createHeaderCell("Reference No."));

        if (payments == null || payments.isEmpty()) {

            paymentTable.addCell(PdfGeneratorUtil.createCell("No payments found."));
            paymentTable.addCell(PdfGeneratorUtil.createCell("N/A"));
            paymentTable.addCell(PdfGeneratorUtil.createCell("N/A"));
            paymentTable.addCell(PdfGeneratorUtil.createCell("N/A"));

        } else {

            for (Payment payment : payments) {

                paymentTable.addCell(PdfGeneratorUtil.createCell(
                        safeText(String.valueOf(payment.getPaymentDate()))
                ));

                paymentTable.addCell(PdfGeneratorUtil.createCell(
                        formatMoney(payment.getAmount())
                ));

                paymentTable.addCell(PdfGeneratorUtil.createCell(
                        safeText(payment.getPaymentMethod())
                ));

                paymentTable.addCell(PdfGeneratorUtil.createCell(
                        safeText(payment.getReferenceNumber())
                ));
            }
        }

        document.add(paymentTable);

        // =====================================================
        // ACCOUNT SUMMARY
        // =====================================================
        document.add(PdfGeneratorUtil.createSectionHeader(
                "Account Summary"
        ));

        Table summaryTable = PdfGeneratorUtil.createTable(
                new float[]{50, 50}
        );

        summaryTable.addCell(PdfGeneratorUtil.createCell("Total Charges"));
        summaryTable.addCell(PdfGeneratorUtil.createCell(
                formatMoney(totalCharges)
        ));

        summaryTable.addCell(PdfGeneratorUtil.createCell("Total Paid"));
        summaryTable.addCell(PdfGeneratorUtil.createCell(
                formatMoney(totalPaid)
        ));

        summaryTable.addCell(PdfGeneratorUtil.createCell("Remaining Balance"));
        summaryTable.addCell(PdfGeneratorUtil.createCell(
                formatMoney(remainingBalance)
        ));

        document.add(summaryTable);

        document.add(PdfGeneratorUtil.createParagraph(
                "\nGenerated by PLL Enrollment System"
        ));

        document.close();

        return fileName;
    }

    private String getTimestamp() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        return LocalDateTime.now().format(formatter);
    }

    private String buildFullName(String firstName, String middleName, String lastName) {
        StringBuilder builder = new StringBuilder();

        if (firstName != null && !firstName.trim().isEmpty()) {
            builder.append(firstName.trim());
        }

        if (middleName != null && !middleName.trim().isEmpty()) {
            builder.append(" ").append(middleName.trim());
        }

        if (lastName != null && !lastName.trim().isEmpty()) {
            builder.append(" ").append(lastName.trim());
        }

        return builder.toString().trim();
    }

    private String safeText(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return "N/A";
        }

        return value;
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "PHP 0.00";
        }

        return "PHP " + amount.setScale(2).toPlainString();
    }
}
