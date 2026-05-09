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
import views.dashboards.RegistrarDashboardView;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import views.panels.students.SearchStudentPanel;
import views.panels.enrollment.NewStudentEnrollmentPanel;

import views.panels.payments.PaymentBillingPanel;

public class RegistrarDashboardController {

    private final RegistrarDashboardView registrarDashboardView;

    public RegistrarDashboardController(RegistrarDashboardView registrarDashboardView) {
        this.registrarDashboardView = registrarDashboardView;
        attachEvents();
    }

    private void attachEvents() {

        registrarDashboardView.getBtnEnrollment().addActionListener(e -> {
            NewStudentEnrollmentPanel panel = new NewStudentEnrollmentPanel();
            new EnrollmentController(panel);
            loadPanel(panel);
        });

        registrarDashboardView.getBtnSearchStudent().addActionListener(e -> {
            SearchStudentPanel panel = new SearchStudentPanel();
            new SearchStudentController(panel);
            loadPanel(panel);
        });

        registrarDashboardView.getBtnPaymentBilling().addActionListener(e -> {
            PaymentBillingPanel panel = new PaymentBillingPanel();
            new PaymentController(panel);
            loadPanel(panel);
        });

        registrarDashboardView.getBtnReports().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        registrarDashboardView,
                        "Reports module will be added later."
                )
        );

        registrarDashboardView.getBtnHelp().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        registrarDashboardView,
                        "Help module will be added later."
                )
        );

        registrarDashboardView.getBtnAbout().addActionListener(e
                -> JOptionPane.showMessageDialog(
                        registrarDashboardView,
                        "About module will be added later."
                )
        );

        registrarDashboardView.getBtnLogout().addActionListener(e -> handleLogout());
    }

    private void handleLogout() {

        int choice = JOptionPane.showConfirmDialog(
                registrarDashboardView,
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

            registrarDashboardView.dispose();
        }
    }

    private void loadPanel(JPanel panel) {

        registrarDashboardView.getPanelMain().removeAll();

        registrarDashboardView.getPanelMain().setLayout(
                new java.awt.BorderLayout()
        );

        registrarDashboardView.getPanelMain().add(
                panel,
                java.awt.BorderLayout.CENTER
        );

        registrarDashboardView.getPanelMain().revalidate();
        registrarDashboardView.getPanelMain().repaint();
    }
}
