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

import views.panels.users.AccountRegistrationPanel;
import views.panels.students.SearchStudentPanel;
import views.panels.enrollment.NewStudentEnrollmentPanel;

import views.panels.payments.PaymentBillingPanel;

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
            NewStudentEnrollmentPanel panel = new NewStudentEnrollmentPanel();
            new EnrollmentController(panel);
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

        adminDashboardView.getBtnClassSchedule().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        adminDashboardView,
                        "Class Schedule module will be added later."
                )
        );

        adminDashboardView.getBtnReports().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        adminDashboardView,
                        "Reports module will be added later."
                )
        );

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
