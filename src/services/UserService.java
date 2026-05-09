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

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public String registerUser(
            String name,
            String email,
            String password,
            String confirmPassword,
            String role
    ) {

        if (name == null || name.trim().isEmpty()) {
            return "Name is required.";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }

        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        if (userDAO.emailExists(email)) {
            return "Email already exists.";
        }

        int roleId = switch (role) {
            case "Admin" ->
                1;
            case "Registrar" ->
                2;
            case "Teacher" ->
                3;
            default ->
                0;
        };

        if (roleId == 0) {
            return "Invalid role.";
        }

        User user = new User();

        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setRoleId(roleId);

        boolean created = userDAO.createUser(user);

        if (!created) {
            return "Failed to create account.";
        }

        return "SUCCESS";
    }

}
