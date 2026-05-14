/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {

    private static final String DB_NAME = "cesplla";
    private static final String DB_USER = "postgres";
    private static final String BACKUP_DIR = "C:/PLL_Backups";

    /**
     * Creates a database backup using pg_dump
     */
    public boolean createBackup() throws IOException, InterruptedException {
        // Create backup directory if not exists
        new File(BACKUP_DIR).mkdirs();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = BACKUP_DIR + "/backup_" + timestamp + ".sql";

        // Build pg_dump command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_dump",
                "-U", DB_USER,
                "-d", DB_NAME,
                "-F", "p", // Plain SQL format
                "-f", backupFile
        );

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        return exitCode == 0;
    }

    /**
     * Restores database from backup file using pg_restore or psql
     */
    public boolean restoreBackup(String backupFilePath) throws IOException, InterruptedException {
        if (!Files.exists(Paths.get(backupFilePath))) {
            throw new IOException("Backup file not found: " + backupFilePath);
        }

        // Build psql command for restore
        ProcessBuilder processBuilder = new ProcessBuilder(
                "psql",
                "-U", DB_USER,
                "-d", DB_NAME,
                "-f", backupFilePath
        );

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        return exitCode == 0;
    }

    /**
     * Lists all available backup files
     */
    public String[] getBackupFiles() {
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            return new String[0];
        }

        return backupDir.list((dir, name) -> name.endsWith(".sql"));
    }

    /**
     * Gets the latest backup file path
     */
    public String getLatestBackupPath() {
        String[] files = getBackupFiles();
        if (files.length == 0) {
            return null;
        }

        // Sort by name (timestamp is in filename)
        java.util.Arrays.sort(files);
        return BACKUP_DIR + "/" + files[files.length - 1];
    }
}
