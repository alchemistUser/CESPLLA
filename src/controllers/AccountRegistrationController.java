package controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Pololoers
 */
import services.UserService;
import views.panels.users.AccountRegistrationPanel;

import javax.swing.JOptionPane;

public class AccountRegistrationController {

    private final AccountRegistrationPanel panel;
    private final UserService userService;

    public AccountRegistrationController(AccountRegistrationPanel panel) {
        this.panel = panel;
        this.userService = new UserService();

        attachEvents();
    }

    private void attachEvents() {

        panel.getBtnRegister().addActionListener(e -> handleRegister());

        panel.getBtnClear().addActionListener(e -> panel.clearForm());
    }

    private void handleRegister() {

        String result = userService.registerUser(
                panel.getNameInput(),
                panel.getEmailInput(),
                panel.getPasswordInput(),
                panel.getConfirmPasswordInput(),
                panel.getSelectedRole()
        );

        if (result.equals("SUCCESS")) {

            JOptionPane.showMessageDialog(
                    panel,
                    "Account created successfully."
            );

            panel.clearForm();

        } else {

            panel.setMessage(result);
        }
    }

}
