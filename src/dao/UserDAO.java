/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Pololoers
 */
import database.DatabaseConnection;
import models.User;

import java.sql.*;

public class UserDAO {

    public User findByEmail(String email) {
        String sql = """
                SELECT 
                    u.user_id,
                    u.role_id,
                    u.name,
                    u.password,
                    u.email,
                    r.role_name
                FROM users u
                INNER JOIN role r ON u.role_id = r.role_id
                WHERE u.email = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();

                    user.setUserId(resultSet.getInt("user_id"));
                    user.setRoleId(resultSet.getInt("role_id"));
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    user.setEmail(resultSet.getString("email"));
                    user.setRoleName(resultSet.getString("role_name"));

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createUser(User user) {
        String sql = """
                INSERT INTO users (role_id, name, password, email)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getRoleId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
