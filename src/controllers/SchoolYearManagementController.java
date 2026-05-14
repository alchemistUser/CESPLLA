/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import views.maintenance.SchoolYearManagementViewPanel;
import services.SchoolYearService; // Requires Step 4
import utils.DialogUtil;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class SchoolYearManagementController {

    private final SchoolYearManagementViewPanel view;
    private final SchoolYearService service;

    public SchoolYearManagementController(SchoolYearManagementViewPanel view) {
        this.view = view;
        this.service = new SchoolYearService(); // Service created in next step
        attachEvents();
    }

    private void attachEvents() {
        view.getBtnLoadArchive().addActionListener(e -> handleLoadArchive());
    }

    private void handleLoadArchive() {
        String selectedYear = (String) view.getCmbSchoolYearFilter().getSelectedItem();

        if (selectedYear == null) {
            DialogUtil.showWarning(view, "Please select a school year.");
            return;
        }

        // UI Feedback
        view.getBtnLoadArchive().setEnabled(false);
        view.getBtnLoadArchive().setText("Loading...");

        try {
            // Call Service to fetch data based on date inference
            List<Map<String, Object>> records = service.getArchiveRecordsForYear(selectedYear);

            // Update Table
            DefaultTableModel model = (DefaultTableModel) view.getTblArchiveData().getModel();
            model.setRowCount(0); // Clear existing data

            for (Map<String, Object> record : records) {
                model.addRow(new Object[]{
                    record.get("lrn"),
                    record.get("student_name"),
                    record.get("grade_level"),
                    record.get("enrollment_date"),
                    record.get("payment_status"),
                    record.get("balance")
                });
            }

            view.setTotalRecords(records.size());

            if (records.isEmpty()) {
                DialogUtil.showWarning(view, "No records found for " + selectedYear);
            } else {
                DialogUtil.showSuccess(view, "Archive loaded for " + selectedYear);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtil.showError(view, "Error loading archive: " + ex.getMessage());
        } finally {
            // Reset UI
            view.getBtnLoadArchive().setEnabled(true);
            view.getBtnLoadArchive().setText("Load Archive");
        }
    }
}
