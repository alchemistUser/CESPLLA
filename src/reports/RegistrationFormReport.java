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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import models.Student;
import models.Guardian;

import models.Enrollment;

public class RegistrationFormReport {

    public String generate(Student student, Guardian guardian, Enrollment enrollment)
            throws FileNotFoundException {

        String fileName = "Registration_Form_Student_"
                + student.getStudentId()
                + "_"
                + getTimestamp()
                + ".pdf";

        Document document = PdfGeneratorUtil.createDocument(fileName);

        document.add(PdfGeneratorUtil.createTitle(
                "PRECIOUS LITTLE LIGHTS ACADEMY"
        ));

        document.add(PdfGeneratorUtil.createParagraph(
                "Official Student Registration Form"
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

        studentTable.addCell(PdfGeneratorUtil.createCell("Full Name"));
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

        studentTable.addCell(PdfGeneratorUtil.createCell("Age"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                String.valueOf(student.getAge())
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Birthdate"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(String.valueOf(student.getBirthdate()))
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Address"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(student.getAddress())
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Previous School"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(student.getPrevSchool())
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Has Allergies"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                student.isHasAllergies() ? "Yes" : "No"
        ));

        studentTable.addCell(PdfGeneratorUtil.createCell("Medical Details"));
        studentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(student.getMedicalDetails())
        ));

        document.add(studentTable);

        // =====================================================
        // GUARDIAN INFORMATION
        // =====================================================
        document.add(PdfGeneratorUtil.createSectionHeader(
                "Guardian Information"
        ));

        Table guardianTable = PdfGeneratorUtil.createTable(
                new float[]{30, 70}
        );

        guardianTable.addCell(PdfGeneratorUtil.createHeaderCell("Field"));
        guardianTable.addCell(PdfGeneratorUtil.createHeaderCell("Details"));

        guardianTable.addCell(PdfGeneratorUtil.createCell("Guardian ID"));
        guardianTable.addCell(PdfGeneratorUtil.createCell(
                String.valueOf(guardian.getGuardianId())
        ));

        guardianTable.addCell(PdfGeneratorUtil.createCell("Guardian Name"));
        guardianTable.addCell(PdfGeneratorUtil.createCell(
                buildFullName(
                        guardian.getFirstName(),
                        "",
                        guardian.getLastName()
                )
        ));

        guardianTable.addCell(PdfGeneratorUtil.createCell("Relationship"));
        guardianTable.addCell(PdfGeneratorUtil.createCell(
                safeText(guardian.getRelationship())
        ));

        guardianTable.addCell(PdfGeneratorUtil.createCell("Contact Number"));
        guardianTable.addCell(PdfGeneratorUtil.createCell(
                safeText(guardian.getContactNumber())
        ));

        guardianTable.addCell(PdfGeneratorUtil.createCell("Address"));
        guardianTable.addCell(PdfGeneratorUtil.createCell(
                safeText(guardian.getAddress())
        ));

        guardianTable.addCell(PdfGeneratorUtil.createCell("Occupation"));
        guardianTable.addCell(PdfGeneratorUtil.createCell(
                safeText(guardian.getOccupation())
        ));

        document.add(guardianTable);

        // =====================================================
        // ENROLLMENT INFORMATION
        // =====================================================
        document.add(PdfGeneratorUtil.createSectionHeader(
                "Enrollment Information"
        ));

        Table enrollmentTable = PdfGeneratorUtil.createTable(
                new float[]{30, 70}
        );

        enrollmentTable.addCell(PdfGeneratorUtil.createHeaderCell("Field"));
        enrollmentTable.addCell(PdfGeneratorUtil.createHeaderCell("Details"));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Enrollment ID"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                String.valueOf(enrollment.getEnrollmentId())
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Student ID"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                String.valueOf(enrollment.getStudentId())
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Grade Level"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(enrollment.getGradeLevel())
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Diagnostic Test Schedule"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                safeText(String.valueOf(enrollment.getDiagnosticTestSchedule()))
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Diagnostic Test Status"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                enrollment.isDiagnosticTestStatus() ? "Passed" : "Pending / Failed"
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("PSA Submitted"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                enrollment.isPsaStatus() ? "Yes" : "No"
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("SF10 Submitted"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                enrollment.isSf10Status() ? "Yes" : "No"
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Good Moral Submitted"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                enrollment.isGoodMoralStatus() ? "Yes" : "No"
        ));

        enrollmentTable.addCell(PdfGeneratorUtil.createCell("Enrollment Status"));
        enrollmentTable.addCell(PdfGeneratorUtil.createCell(
                formatEnrollmentStatus(enrollment.getEnrollmentStatus())
        ));

        document.add(enrollmentTable);

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

    private String formatEnrollmentStatus(int status) {
        switch (status) {
            case 1:
                return "Pending";
            case 2:
                return "Enrolled";
            case 3:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }
}
