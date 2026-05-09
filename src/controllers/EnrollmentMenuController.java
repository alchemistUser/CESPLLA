/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import views.panels.enrollment.EnrollmentMenuPanel;

import views.panels.enrollment.NewStudentEnrollmentPanel;
import views.panels.enrollment.OldStudentEnrollmentPanel;

public class EnrollmentMenuController {

    private final EnrollmentMenuPanel view;

    private final JPanel dashboardPanel;

    public EnrollmentMenuController(
            EnrollmentMenuPanel view,
            JPanel dashboardPanel
    ) {

        this.view = view;
        this.dashboardPanel = dashboardPanel;

        attachEvents();
    }

    // =====================================================
    // ATTACH EVENTS
    // =====================================================
    private void attachEvents() {

        initializeNewStudentEnrollmentButton();

        initializeOldStudentEnrollmentButton();

        initializeEditStudentInformationButton();
    }

    // =====================================================
    // NEW STUDENT ENROLLMENT
    // =====================================================
    private void initializeNewStudentEnrollmentButton() {

        view.getBtnNewStudentEnrollment()
                .addActionListener(e -> {

                    NewStudentEnrollmentPanel panel
                            = new NewStudentEnrollmentPanel();

                    new EnrollmentController(panel);

                    loadPanel(panel);
                });
    }

    // =====================================================
    // OLD STUDENT ENROLLMENT
    // =====================================================
    private void initializeOldStudentEnrollmentButton() {

        view.getBtnOldStudentEnrollment()
                .addActionListener(e -> {

                    OldStudentEnrollmentPanel panel
                            = new OldStudentEnrollmentPanel();

                    new OldStudentEnrollmentController(
                            panel,
                            dashboardPanel
                    );

                    loadPanel(panel);
                });
    }

    // =====================================================
    // EDIT STUDENT INFORMATION
    // =====================================================
    private void initializeEditStudentInformationButton() {

        view.getBtnEditStudentInformation()
                .addActionListener(e -> {

                    JOptionPane.showMessageDialog(
                            view,
                            "Edit Student Information module "
                            + "will be implemented later."
                    );
                });
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
}
