/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author Pololoers
 */
public class AppConfig {

    private AppConfig() {
    }

    public static final String DB_URL
            = "jdbc:postgresql://localhost:5432/plla_enrollment";

    public static final String DB_USERNAME = "postgres";

    public static final String DB_PASSWORD = "admin";

    public static final String REPORT_FOLDER = "reports/";

    public static final String BACKUP_FOLDER = "database/backups/";
}
