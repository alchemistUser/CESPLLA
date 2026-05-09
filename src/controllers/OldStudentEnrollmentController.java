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

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import models.Student;

import views.panels.enrollment.OldStudentEnrollmentPanel;
import views.panels.enrollment.ReEnrollmentPanel;

public class OldStudentEnrollmentController {

    private final OldStudentEnrollmentPanel view;

    private final JPanel dashboardPanel;

    private final StudentDAO studentDAO;

    private int selectedStudentId = -1;

    public OldStudentEnrollmentController(
            OldStudentEnrollmentPanel view,
            JPanel dashboardPanel
    ) {

        this.view = view;

        this.dashboardPanel = dashboardPanel;

        studentDAO = new StudentDAO();

        attachEvents();

        loadStudentsToTable();
    }

    // =====================================================
    // ATTACH EVENTS
    // =====================================================
    private void attachEvents() {

        initializeSearchButton();

        initializeRefreshButton();

        initializeTableSelection();

        initializeProceedEnrollmentButton();
    }

    // =====================================================
    // SEARCH BUTTON
    // =====================================================
    private void initializeSearchButton() {

        view.getBtnSearch().addActionListener(e -> {

            searchStudents();
        });
    }

    // =====================================================
    // REFRESH BUTTON
    // =====================================================
    private void initializeRefreshButton() {

        view.getBtnRefresh().addActionListener(e -> {

            view.getTxtSearch().setText("");

            selectedStudentId = -1;

            view.getLblSelectedStudent().setText(
                    "Selected Student: None"
            );

            loadStudentsToTable();
        });
    }

    // =====================================================
    // TABLE SELECTION
    // =====================================================
    private void initializeTableSelection() {

        view.getTblStudents()
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        handleStudentSelection();
                    }
                });
    }

    // =====================================================
    // PROCEED ENROLLMENT BUTTON
    // =====================================================
    private void initializeProceedEnrollmentButton() {

        view.getBtnProceedEnrollment()
                .addActionListener(e -> {

                    proceedEnrollment();
                });
    }

    // =====================================================
    // LOAD STUDENTS
    // =====================================================
    private void loadStudentsToTable() {

        try {

            List<Student> students
                    = studentDAO.findAll();

            populateStudentTable(students);

        } catch (Exception ex) {

            showErrorMessage(
                    "Failed to load students.\n"
                    + ex.getMessage()
            );
        }
    }

    // =====================================================
    // SEARCH STUDENTS
    // =====================================================
    private void searchStudents() {

        try {

            String keyword
                    = view.getTxtSearch()
                            .getText()
                            .trim();

            List<Student> students;

            if (keyword.isEmpty()) {

                students = studentDAO.findAll();

            } else {

                students
                        = studentDAO.searchStudents(keyword);
            }

            populateStudentTable(students);

        } catch (Exception ex) {

            showErrorMessage(
                    "Search failed.\n"
                    + ex.getMessage()
            );
        }
    }

    // =====================================================
    // POPULATE TABLE
    // =====================================================
    private void populateStudentTable(
            List<Student> students
    ) {

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

    // =====================================================
    // HANDLE STUDENT SELECTION
    // =====================================================
    private void handleStudentSelection() {

        JTable table = view.getTblStudents();

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedStudentId = Integer.parseInt(
                table.getValueAt(selectedRow, 0).toString()
        );

        String firstName
                = table.getValueAt(selectedRow, 1).toString();

        String lastName
                = table.getValueAt(selectedRow, 2).toString();

        JLabel label
                = view.getLblSelectedStudent();

        label.setText(
                "Selected Student: "
                + firstName
                + " "
                + lastName
        );
    }

    // =====================================================
    // PROCEED ENROLLMENT
    // =====================================================
    private void proceedEnrollment() {

        if (selectedStudentId == -1) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please select a student first.",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        ReEnrollmentPanel panel
                = new ReEnrollmentPanel();

        new ReEnrollmentController(
                panel,
                dashboardPanel,
                selectedStudentId
        );

        loadPanel(panel);
    }

    // =====================================================
    // LOAD PANEL
    // =====================================================
    private void loadPanel(JPanel panel) {

        dashboardPanel.removeAll();

        dashboardPanel.setLayout(
                new java.awt.BorderLayout()
        );

        dashboardPanel.add(
                panel,
                java.awt.BorderLayout.CENTER
        );

        dashboardPanel.revalidate();

        dashboardPanel.repaint();
    }

    // =====================================================
    // ERROR MESSAGE
    // =====================================================
    private void showErrorMessage(String message) {

        JOptionPane.showMessageDialog(
                view,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
