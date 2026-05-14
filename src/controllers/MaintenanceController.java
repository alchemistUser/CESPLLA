/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */

import services.ArchiveService;
import services.BackupService;
import utils.DialogUtil;
import views.maintenance.MaintenanceView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import models.Student; // <-- IMPORTANT: Import Student model

public class MaintenanceController {

    private final MaintenanceView view;
    private final BackupService backupService;
    private final ArchiveService archiveService;

    public MaintenanceController(MaintenanceView view) {
        this.view = view;
        this.backupService = new BackupService();
        this.archiveService = new ArchiveService();

        initializeView();
        attachEvents();
    }

    private void initializeView() {
        view.setStatus("Ready");
        updateLastBackupLabel();
    }

    private void attachEvents() {
        view.getBtnCreateBackup().addActionListener(e -> handleCreateBackup());
        view.getBtnRestoreBackup().addActionListener(e -> handleRestoreBackup());
        view.getBtnArchiveRecords().addActionListener(e -> handleArchiveRecords());
    }

    private void handleCreateBackup() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Create a new database backup now?",
                "Confirm Backup",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        view.setButtonsEnabled(false);
        view.setStatus("Creating backup...");

        SwingWorker<Boolean, Integer> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    boolean success = backupService.createBackup();
                    setProgress(100);
                    return success;
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        DialogUtil.showSuccess(view, "Backup created successfully!");
                        updateLastBackupLabel();
                        view.setStatus("Backup completed: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } else {
                        DialogUtil.showError(view, "Backup failed. Check console for details.");
                        view.setStatus("Backup failed");
                    }
                } catch (Exception e) {
                    DialogUtil.showError(view, "Backup error: " + e.getMessage());
                    view.setStatus("Backup error");
                } finally {
                    view.setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void handleRestoreBackup() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Backup File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("SQL Files", "sql"));
        fileChooser.setCurrentDirectory(new File("C:/PLL_Backups"));

        int userSelection = fileChooser.showOpenDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "WARNING: This will overwrite the current database!\n"
                    + "All current data will be lost.\n\n"
                    + "Are you sure you want to restore from:\n" + selectedFile.getName() + "?",
                    "Confirm Restore",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            view.setButtonsEnabled(false);
            view.setStatus("Restoring database...");

            SwingWorker<Boolean, Integer> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() {
                    try {
                        boolean success = backupService.restoreBackup(selectedFile.getAbsolutePath());
                        setProgress(100);
                        return success;
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            DialogUtil.showSuccess(view, "Database restored successfully!\nPlease restart the application.");
                            view.setStatus("Restore completed");

                            int exitConfirm = JOptionPane.showConfirmDialog(
                                    view,
                                    "Database restored. Restart application now?",
                                    "Restart Required",
                                    JOptionPane.YES_NO_OPTION
                            );

                            if (exitConfirm == JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                        } else {
                            DialogUtil.showError(view, "Restore failed. Check console for details.");
                            view.setStatus("Restore failed");
                        }
                    } catch (Exception e) {
                        DialogUtil.showError(view, "Restore error: " + e.getMessage());
                        view.setStatus("Restore error");
                    } finally {
                        view.setButtonsEnabled(true);
                    }
                }
            };

            worker.execute();
        }
    }

    private void handleArchiveRecords() {
        String input = JOptionPane.showInputDialog(
                view,
                "Enter cutoff date (YYYY-MM-DD):\n"
                + "Students who haven't enrolled since this date will be archived.",
                "Archive Records",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            LocalDate cutoffDate = LocalDate.parse(input.trim());

            // FIX: Use explicit List type
            List<Student> inactiveStudents = archiveService.findInactiveStudents(cutoffDate);

            if (inactiveStudents.isEmpty()) {
                // FIX: Use showWarning instead of showInfo (which doesn't exist in your DialogUtil)
                DialogUtil.showWarning(view, "No inactive students found.");
                return;
            }

            StringBuilder studentList = new StringBuilder();
            for (int i = 0; i < Math.min(10, inactiveStudents.size()); i++) {
                Student s = inactiveStudents.get(i);
                studentList.append(s.getLastName()).append(", ").append(s.getFirstName()).append("\n");
            }

            if (inactiveStudents.size() > 10) {
                studentList.append("... and ").append(inactiveStudents.size() - 10).append(" more");
            }

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Found " + inactiveStudents.size() + " inactive student(s):\n\n" + studentList.toString() + "\n\n"
                    + "Archive these records?\n"
                    + "This action cannot be undone.",
                    "Confirm Archive",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            view.setButtonsEnabled(false);
            view.setStatus("Archiving records...");

            SwingWorker<Integer, Void> worker = new SwingWorker<>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    // FIX: Use standard ArrayList loop
                    List<Integer> studentIds = new ArrayList<>();
                    for (Student s : inactiveStudents) {
                        studentIds.add(s.getStudentId());
                    }
                    return archiveService.archiveMultipleStudents(studentIds);
                }

                @Override
                protected void done() {
                    try {
                        int archivedCount = get();
                        DialogUtil.showSuccess(view, "Successfully archived " + archivedCount + " student record(s).");
                        view.setStatus("Archived " + archivedCount + " records");
                    } catch (Exception e) {
                        DialogUtil.showError(view, "Archive error: " + e.getMessage());
                        view.setStatus("Archive failed");
                    } finally {
                        view.setButtonsEnabled(true);
                    }
                }
            };

            worker.execute();

        } catch (Exception e) {
            DialogUtil.showError(view, "Invalid date format. Please use YYYY-MM-DD");
        }
    }

    private void updateLastBackupLabel() {
        String latestBackup = backupService.getLatestBackupPath();
        if (latestBackup != null) {
            File backupFile = new File(latestBackup);
            long millis = backupFile.lastModified();
            String dateStr = LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(millis),
                    java.time.ZoneId.systemDefault()
            ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            view.setLastBackup(dateStr);
        } else {
            view.setLastBackup("No backups found");
        }
    }
}
