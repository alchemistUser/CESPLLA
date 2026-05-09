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

public class Student {

    private int studentId;
    private String gradeLevel;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private int age;
    private LocalDate birthdate;
    private String prevSchool;
    private boolean hasAllergies;
    private String medicalDetails;

    public Student() {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPrevSchool() {
        return prevSchool;
    }

    public void setPrevSchool(String prevSchool) {
        this.prevSchool = prevSchool;
    }

    public boolean isHasAllergies() {
        return hasAllergies;
    }

    public void setHasAllergies(boolean hasAllergies) {
        this.hasAllergies = hasAllergies;
    }

    public String getMedicalDetails() {
        return medicalDetails;
    }

    public void setMedicalDetails(String medicalDetails) {
        this.medicalDetails = medicalDetails;
    }
}
