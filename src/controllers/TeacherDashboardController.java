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
import views.dashboards.TeacherDashboardView;

import javax.swing.JOptionPane;

public class TeacherDashboardController {

    private final TeacherDashboardView teacherDashboardView;

    public TeacherDashboardController(TeacherDashboardView teacherDashboardView) {
        this.teacherDashboardView = teacherDashboardView;
        attachEvents();
    }

    private void attachEvents() {
        teacherDashboardView.getBtnStudentQualification().addActionListener(e
                -> JOptionPane.showMessageDialog(teacherDashboardView, "Student Qualification module will be added later.")
        );

        teacherDashboardView.getBtnViewSchedule().addActionListener(e
                -> JOptionPane.showMessageDialog(teacherDashboardView, "View Schedule module will be added later.")
        );

        teacherDashboardView.getBtnHelp().addActionListener(e
                -> JOptionPane.showMessageDialog(teacherDashboardView, "Help module will be added later.")
        );

        teacherDashboardView.getBtnAbout().addActionListener(e
                -> JOptionPane.showMessageDialog(teacherDashboardView, "About module will be added later.")
        );

        teacherDashboardView.getBtnLogout().addActionListener(e -> handleLogout());
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                teacherDashboardView,
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

            teacherDashboardView.dispose();
        }
    }

}
