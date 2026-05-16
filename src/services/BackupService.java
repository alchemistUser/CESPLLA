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

    private static final String DB_NAME = "plla_enrollment";
    private static final String DB_USER = "postgres";
    private static final String BACKUP_DIR = "C:/PLL_Backups";

    // UPDATE THIS PATH to match your PostgreSQL installation
    private static final String PG_DUMP_PATH = "C:/Program Files/PostgreSQL/18/bin/pg_dump.exe";
    private static final String PSQL_PATH = "C:/Program Files/PostgreSQL/18/bin/psql.exe";

    /**
     * Creates a database backup using pg_dump
     */
    public boolean createBackup() throws IOException, InterruptedException {
        // Verify pg_dump exists
        File pgDump = new File(PG_DUMP_PATH);
        if (!pgDump.exists()) {
            throw new IOException("pg_dump not found at: " + PG_DUMP_PATH
                    + "\nPlease update PG_DUMP_PATH in BackupService.java");
        }

        // Create backup directory if not exists
        new File(BACKUP_DIR).mkdirs();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = BACKUP_DIR + "/backup_" + timestamp + ".sql";

        // Build pg_dump command with FULL PATH
        ProcessBuilder processBuilder = new ProcessBuilder(
                PG_DUMP_PATH,
                "-U", DB_USER,
                "-d", DB_NAME,
                "-F", "p", // Plain SQL format
                "-f", backupFile
        );

        // Set environment variable for password (optional - you may need to use .pgpass file)
        processBuilder.environment().put("PGPASSWORD", "admin"); // ⚠️ UPDATE THIS

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Backup created successfully: " + backupFile);
            return true;
        } else {
            System.err.println("Backup failed with exit code: " + exitCode);
            return false;
        }
    }

    /**
     * Restores database from backup file using psql
     */
    public boolean restoreBackup(String backupFilePath) throws IOException, InterruptedException {
        // Verify psql exists
        File psql = new File(PSQL_PATH);
        if (!psql.exists()) {
            throw new IOException("psql not found at: " + PSQL_PATH
                    + "\nPlease update PSQL_PATH in BackupService.java");
        }

        if (!Files.exists(Paths.get(backupFilePath))) {
            throw new IOException("Backup file not found: " + backupFilePath);
        }

        // Build psql command with FULL PATH
        ProcessBuilder processBuilder = new ProcessBuilder(
                PSQL_PATH,
                "-U", DB_USER,
                "-d", DB_NAME,
                "-f", backupFilePath
        );

        // Set environment variable for password
        processBuilder.environment().put("PGPASSWORD", "your_password_here"); // ⚠️ UPDATE THIS

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Restore completed successfully");
            return true;
        } else {
            System.err.println("Restore failed with exit code: " + exitCode);
            return false;
        }
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
