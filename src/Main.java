/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Pololoers
 */
import controllers.LoginController;
import views.auth.LoginView;

public class Main {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            LoginView loginView = new LoginView();

            new LoginController(loginView);

            loginView.setVisible(true);
        });
    }
}