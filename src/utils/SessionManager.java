/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Pololoers
 */
import models.User;

public class SessionManager {
    private static User currentUser;

    private SessionManager() {
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "Admin".equalsIgnoreCase(currentUser.getRoleName());
    }

    public static boolean isRegistrar() {
        return currentUser != null && "Registrar".equalsIgnoreCase(currentUser.getRoleName());
    }

    public static boolean isTeacher() {
        return currentUser != null && "Teacher".equalsIgnoreCase(currentUser.getRoleName());
    }

}
