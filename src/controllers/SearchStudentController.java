/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import dao.StudentDAO;
import models.Student;
import views.panels.students.SearchStudentPanel;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
import views.dialogs.students.StudentEnrollmentDetailsDialog;

public class SearchStudentController {

    private final SearchStudentPanel panel;
    private final StudentDAO studentDAO;

    public SearchStudentController(SearchStudentPanel panel) {
        this.panel = panel;
        this.studentDAO = new StudentDAO();

        setupTable();
        attachEvents();
        loadAllStudents();
    }

    private void setupTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Grade Level",
                    "First Name",
                    "Middle Name",
                    "Last Name",
                    "Age",
                    "Birthdate",
                    "Previous School",
                    "Has Allergies"
                },
                0
        );

        panel.getTblStudents().setModel(model);
    }

    private void attachEvents() {
        panel.getBtnSearch().addActionListener(e -> searchStudents());
        panel.getBtnRefresh().addActionListener(e -> loadAllStudents());
        panel.getBtnViewDetails().addActionListener(e -> openStudentDetails());
    }

    private void searchStudents() {
        String keyword = panel.getSearchKeyword();

        if (keyword.isEmpty()) {
            panel.setMessage("Please enter a student name.");
            return;
        }

        try {
            List<Student> students = studentDAO.searchStudents(keyword);
            displayStudents(students);

        } catch (SQLException ex) {
            System.err.println("SEARCH STUDENT DATABASE ERROR:");
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    panel,
                    "Database error while searching students:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadAllStudents() {
        panel.setMessage("");

        try {
            displayStudents(studentDAO.findAll());

        } catch (Exception ex) {
            System.err.println("LOAD STUDENTS ERROR:");
            System.err.println("Message: " + ex.getMessage());
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    panel,
                    "Error while loading student records:\n" + ex.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openStudentDetails() {
        int selectedRow = panel.getTblStudents().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    panel,
                    "Please select a student record first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int studentId = (int) panel.getTblStudents().getValueAt(selectedRow, 0);

        StudentEnrollmentDetailsDialog dialog
                = new StudentEnrollmentDetailsDialog(null, true);

        new StudentEnrollmentDetailsController(dialog, studentId);

        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);
    }

    private void displayStudents(List<Student> students) {
        DefaultTableModel model
                = (DefaultTableModel) panel.getTblStudents().getModel();

        model.setRowCount(0);

        for (Student student : students) {
            model.addRow(new Object[]{
                student.getStudentId(),
                formatGradeLevel(student.getGradeLevel()), 
                student.getFirstName(),
                student.getMiddleName(),
                student.getLastName(),
                student.getAge(),
                student.getBirthdate(),
                student.getPrevSchool(),
                student.isHasAllergies() ? "Yes" : "No"
            });
        }

        if (students.isEmpty()) {
            panel.setMessage("No student records found.");
        } else {
            panel.setMessage(students.size() + " record(s) found.");
        }
    }

    private String formatGradeLevel(String gradeLevel) {
        if (gradeLevel == null) {
            return "-";
        }

        return switch (gradeLevel) {
            case "N" ->
                "Nursery";
            case "K1" ->
                "Junior Kindergarten";
            case "K2" ->
                "Senior Kindergarten";
            default ->
                gradeLevel;
        };
    }
}
