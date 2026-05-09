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
import models.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();

        String sql = "SELECT role_id, role_name FROM role ORDER BY role_id";

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Role role = new Role();
                role.setRoleId(resultSet.getInt("role_id"));
                role.setRoleName(resultSet.getString("role_name"));
                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

}
