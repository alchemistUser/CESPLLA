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
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Student;
import services.ReportService;

import views.panels.reports.ReportsView;

public class ReportsController {

    private final ReportsView view;
    private final ReportService reportService;
    private final StudentDAO studentDAO;

    private int selectedStudentId = -1;

    public ReportsController(ReportsView view) {
        this.view = view;
        this.reportService = new ReportService();
        this.studentDAO = new StudentDAO();

        attachEvents();
        loadStudentsToTable();
    }

    private void attachEvents() {
        view.getBtnSearch().addActionListener(e -> searchStudents());

        view.getBtnRefresh().addActionListener(e -> {
            view.getTxtSearchStudent().setText("");
            selectedStudentId = -1;
            view.getLblSelectedStudent().setText("Selected Student: None");
            loadStudentsToTable();
        });

        view.getTblStudents().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleStudentSelection();
            }
        });

        view.getBtnGenerateRegistrationForm().addActionListener(e
                -> generateRegistrationForm()
        );

        view.getBtnGenerateStatementOfAccount().addActionListener(e
                -> generateStatementOfAccount()
        );
    }

    private void loadStudentsToTable() {
        try {
            List<Student> students = studentDAO.findAll();
            populateStudentTable(students);
        } catch (Exception ex) {
            showErrorMessage("Failed to load students.\n" + ex.getMessage());
        }
    }

    private void searchStudents() {
        try {
            String keyword = view.getTxtSearchStudent().getText().trim();

            List<Student> students;

            if (keyword.isEmpty()) {
                students = studentDAO.findAll();
            } else {
                students = studentDAO.searchStudents(keyword);
            }

            selectedStudentId = -1;
            view.getLblSelectedStudent().setText("Selected Student: None");

            populateStudentTable(students);

        } catch (Exception ex) {
            showErrorMessage("Search failed.\n" + ex.getMessage());
        }
    }

    private void populateStudentTable(List<Student> students) {
        DefaultTableModel model
                = (DefaultTableModel) view.getTblStudents().getModel();

        model.setRowCount(0);

        for (Student student : students) {
            model.addRow(new Object[]{
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getGradeLevel()
            });
        }
    }

    private void handleStudentSelection() {
        JTable table = view.getTblStudents();

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedStudentId = Integer.parseInt(
                table.getValueAt(selectedRow, 0).toString()
        );

        String firstName = table.getValueAt(selectedRow, 1).toString();
        String lastName = table.getValueAt(selectedRow, 2).toString();

        view.getLblSelectedStudent().setText(
                "Selected Student: " + firstName + " " + lastName
        );
    }

    private void generateRegistrationForm() {
        if (!validateStudentSelection()) {
            return;
        }

        try {
            String fileName
                    = reportService.generateRegistrationForm(selectedStudentId);

            showSuccessMessage("Registration form generated successfully.");
            openGeneratedFile(fileName);

        } catch (Exception ex) {
            showErrorMessage(
                    "Failed to generate registration form.\n" + ex.getMessage()
            );
        }
    }

    private void generateStatementOfAccount() {
        if (!validateStudentSelection()) {
            return;
        }

        try {
            String fileName
                    = reportService.generateStatementOfAccount(selectedStudentId);

            showSuccessMessage("Statement of account generated successfully.");
            openGeneratedFile(fileName);

        } catch (Exception ex) {
            showErrorMessage(
                    "Failed to generate statement of account.\n" + ex.getMessage()
            );
        }
    }

    private boolean validateStudentSelection() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(
                    view,
                    "Please select a student first.",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    private void openGeneratedFile(String fileName) {
        try {
            File file = new File("generated_reports/" + fileName);

            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (IOException ex) {
            showErrorMessage(
                    "Unable to open generated PDF.\n" + ex.getMessage()
            );
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
                view,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                view,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
