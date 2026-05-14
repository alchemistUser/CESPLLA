/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import utils.SessionManager;
import views.auth.LoginView;
import views.dashboards.AdminDashboardView;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import views.maintenance.ClassScheduleManagementPanel;
import views.maintenance.SchoolYearManagementViewPanel;

import views.panels.users.AccountRegistrationPanel;
import views.panels.students.SearchStudentPanel;
import views.panels.enrollment.NewStudentEnrollmentPanel;

import views.panels.payments.PaymentBillingPanel;
import views.panels.reports.ReportsView;
import views.panels.enrollment.EnrollmentMenuPanel;
import views.maintenance.SchoolYearManagementViewPanel;

public class AdminDashboardController {

    private final AdminDashboardView adminDashboardView;

    public AdminDashboardController(AdminDashboardView adminDashboardView) {
        this.adminDashboardView = adminDashboardView;
        attachEvents();
    }

    private void attachEvents() {

        adminDashboardView.getBtnAccountRegistration().addActionListener(e -> {
            AccountRegistrationPanel panel = new AccountRegistrationPanel();
            new AccountRegistrationController(panel);
            loadPanel(panel);
        });

        adminDashboardView.getBtnEnrollment().addActionListener(e -> {
            EnrollmentMenuPanel panel = new EnrollmentMenuPanel();
            new EnrollmentMenuController(
                    panel,
                    adminDashboardView.getPanelMain()
            );
            loadPanel(panel);
        });

        adminDashboardView.getBtnSearchStudent().addActionListener(e -> {
            SearchStudentPanel panel = new SearchStudentPanel();
            new SearchStudentController(panel);
            loadPanel(panel);
        });

        adminDashboardView.getBtnPaymentBilling().addActionListener(e -> {
            PaymentBillingPanel panel = new PaymentBillingPanel();
            new PaymentController(panel);
            loadPanel(panel);
        });

        adminDashboardView.getBtnClassSchedule().addActionListener(e -> {
            ClassScheduleManagementPanel panel = new ClassScheduleManagementPanel();
            new ClassScheduleController(panel);
            loadPanel(panel);
        });
        
        adminDashboardView.getBtnReports().addActionListener(e -> {
            ReportsView panel = new ReportsView();
            new ReportsController(panel);
            loadPanel(panel);
        });

        adminDashboardView.getBtnMaintenance().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        adminDashboardView,
                        "Maintenance module will be added later."
                )
        );

        adminDashboardView.getBtnHelp().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        adminDashboardView,
                        "Help module will be added later."
                )
        );

        adminDashboardView.getBtnAbout().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        adminDashboardView,
                        "About module will be added later."
                )
        );

        adminDashboardView.getBtnLogout().addActionListener(e -> handleLogout());

        adminDashboardView.getBtnSchoolYearManagement().addActionListener(e -> {
            SchoolYearManagementViewPanel panel = new SchoolYearManagementViewPanel();
            new SchoolYearManagementController(panel);
            loadPanel(panel);
        });
    }

    private void handleLogout() {

        int choice = JOptionPane.showConfirmDialog(
                adminDashboardView,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            SessionManager.logout();

            LoginView loginView = new LoginView();
            new LoginController(loginView);

            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);

            adminDashboardView.dispose();
        }
    }

    private void loadPanel(JPanel panel) {

        adminDashboardView.getPanelMain().removeAll();

        adminDashboardView.getPanelMain().setLayout(
                new java.awt.BorderLayout()
        );

        adminDashboardView.getPanelMain().add(
                panel,
                java.awt.BorderLayout.CENTER
        );

        adminDashboardView.getPanelMain().revalidate();
        adminDashboardView.getPanelMain().repaint();
    }
}
