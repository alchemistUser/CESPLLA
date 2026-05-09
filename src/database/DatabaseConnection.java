/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author Pololoers
 */
import config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                AppConfig.DB_URL,
                AppConfig.DB_USERNAME,
                AppConfig.DB_PASSWORD
        );
    }

    public static boolean testConnection() {

        try (Connection connection = getConnection()) {

            return connection != null && !connection.isClosed();

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

}
