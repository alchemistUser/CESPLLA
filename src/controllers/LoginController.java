/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import models.User;
import services.AuthService;
import utils.SessionManager;
import views.auth.LoginView;

import views.dashboards.AdminDashboardView;
import views.dashboards.RegistrarDashboardView;
import views.dashboards.TeacherDashboardView;

import javax.swing.JOptionPane;

public class LoginController {

    private final LoginView loginView;
    private final AuthService authService;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.authService = new AuthService();

        attachEvents();
    }

    private void attachEvents() {
        loginView.getBtnLogin().addActionListener(e -> handleLogin());

        loginView.getBtnForgotPassword().addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    loginView,
                    "Forgot password feature will be added later."
            );
        });
    }

    private void handleLogin() {
        String email = loginView.getEmail();
        String password = loginView.getPassword();

        User user = authService.login(email, password);

        if (user == null) {
            loginView.setMessage("Invalid email or password.");
            return;
        }

        loginView.setMessage("");

        JOptionPane.showMessageDialog(
                loginView,
                "Login successful. Welcome, " + user.getName() + "!"
        );

        openDashboard(user);
    }

    private void openDashboard(User user) {
        if (SessionManager.isAdmin()) {

            AdminDashboardView adminDashboardView = new AdminDashboardView();
            adminDashboardView.setWelcomeText(user.getName());
            new AdminDashboardController(adminDashboardView);
            adminDashboardView.setLocationRelativeTo(null);
            adminDashboardView.setVisible(true);

        } else if (SessionManager.isRegistrar()) {

            RegistrarDashboardView registrarDashboardView = new RegistrarDashboardView();
            registrarDashboardView.setWelcomeText(user.getName());
            new RegistrarDashboardController(registrarDashboardView);
            registrarDashboardView.setLocationRelativeTo(null);
            registrarDashboardView.setVisible(true);

        } else if (SessionManager.isTeacher()) {

            TeacherDashboardView teacherDashboardView = new TeacherDashboardView();
            teacherDashboardView.setWelcomeText(user.getName());
            new TeacherDashboardController(teacherDashboardView);
            teacherDashboardView.setLocationRelativeTo(null);
            teacherDashboardView.setVisible(true);
        }

        loginView.dispose();
    }

}
