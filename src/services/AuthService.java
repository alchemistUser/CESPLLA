/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import dao.UserDAO;
import models.User;
import utils.PasswordUtil;
import utils.SessionManager;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String email, String plainPassword) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim());

        if (user == null) {
            return null;
        }

        boolean passwordMatches = PasswordUtil.verifyPassword(
                plainPassword,
                user.getPassword()
        );

        if (!passwordMatches) {
            return null;
        }

        SessionManager.login(user);
        return user;
    }

}
