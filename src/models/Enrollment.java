/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Pololoers
 */
import java.time.LocalDate;

public class Enrollment {

    private int enrollmentId;
    private int studentId;
    private String gradeLevel;
    private LocalDate diagnosticTestSchedule;
    private boolean diagnosticTestStatus;
    private boolean psaStatus;
    private boolean sf10Status;
    private boolean goodMoralStatus;
    private int enrollmentStatus;
    private String paymentScheme;  // ← ADD THIS FIELD

    public Enrollment() {
        this.paymentScheme = "QUARTERLY"; // Default value
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public LocalDate getDiagnosticTestSchedule() {
        return diagnosticTestSchedule;
    }

    public void setDiagnosticTestSchedule(LocalDate diagnosticTestSchedule) {
        this.diagnosticTestSchedule = diagnosticTestSchedule;
    }

    public boolean isDiagnosticTestStatus() {
        return diagnosticTestStatus;
    }

    public void setDiagnosticTestStatus(boolean diagnosticTestStatus) {
        this.diagnosticTestStatus = diagnosticTestStatus;
    }

    public boolean isPsaStatus() {
        return psaStatus;
    }

    public void setPsaStatus(boolean psaStatus) {
        this.psaStatus = psaStatus;
    }

    public boolean isSf10Status() {
        return sf10Status;
    }

    public void setSf10Status(boolean sf10Status) {
        this.sf10Status = sf10Status;
    }

    public boolean isGoodMoralStatus() {
        return goodMoralStatus;
    }

    public void setGoodMoralStatus(boolean goodMoralStatus) {
        this.goodMoralStatus = goodMoralStatus;
    }

    public int getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(int enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    // ADD THESE METHODS
    public String getPaymentScheme() {
        return paymentScheme;
    }

    public void setPaymentScheme(String paymentScheme) {
        this.paymentScheme = paymentScheme;
    }
}
