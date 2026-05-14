/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Pololoers
 */
import services.ClassScheduleService;
import utils.DialogUtil;
import views.dialogs.classschedule.ClassFormDialog;
import views.maintenance.ClassScheduleManagementPanel;

import javax.swing.table.DefaultTableModel;
import java.awt.Frame;

import java.util.List;
import javax.swing.JOptionPane;
import models.ScheduleGenerationService;

import dao.ClassDAO;
import dao.ScheduleDAO;
import dao.SectionDAO;
import dao.SubjectDAO;
import dao.TeacherDAO;
import models.ClassModel;
import models.ScheduleModel;
import models.Section;
import models.Subject;
import models.Teacher;

public class ClassScheduleController {

    private final ClassScheduleManagementPanel view;
    private final ClassScheduleService service;

    private List<ClassModel> allClasses;
    private List<Section> sections;          // ← ADD THIS
    private List<Subject> subjects;          // ← ADD THIS  
    private List<Teacher> teachers;          // ← ADD THIS
    private List<ScheduleModel> schedules;   // ← ADD THIS

    private final ClassDAO classDAO;
    private final SectionDAO sectionDAO;
    private final SubjectDAO subjectDAO;
    private final TeacherDAO teacherDAO;
    private final ScheduleDAO scheduleDAO;

    public ClassScheduleController(ClassScheduleManagementPanel view) {
        this.view = view;
        this.service = new ClassScheduleService();

        this.classDAO = new ClassDAO();
        this.sectionDAO = new SectionDAO();
        this.subjectDAO = new SubjectDAO();
        this.teacherDAO = new TeacherDAO();
        this.scheduleDAO = new ScheduleDAO();

        initializeComponents();
        loadData();
        attachEvents();
    }

    private void initializeComponents() {
        view.initializeTable();
        view.getCmbGradeFilter().setSelectedIndex(0);
    }

    private void loadData() {
        try {
            sections = sectionDAO.findAll();           // ← ADD THIS
            subjects = subjectDAO.findAll();           // ← ADD THIS
            teachers = teacherDAO.findAll();           // ← ADD THIS
            schedules = scheduleDAO.findAll();         // ← ADD THIS
            allClasses = classDAO.findAllWithDetails();

            refreshTable();
        } catch (Exception ex) {
            DialogUtil.showError(view, "Failed to load class schedule data: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        view.clearTable();
        DefaultTableModel model = (DefaultTableModel) view.getTblSchedule().getModel();
        String selectedGrade = view.getCmbGradeFilter().getSelectedItem().toString();

        List<ClassModel> filtered = allClasses;
        if (!"All Grades".equals(selectedGrade)) {
            filtered = allClasses.stream()
                    .filter(c -> c.getSectionName().contains(selectedGrade))
                    .toList();
        }

        for (ClassModel c : filtered) {
            model.addRow(new Object[]{
                c.getClassId(), c.getSectionName(), c.getSubjectName(),
                c.getTeacherName(), c.getDay(), c.getStartTime(), c.getEndTime()
            });
        }
    }

    private void attachEvents() {
        view.getBtnAdd().addActionListener(e -> openAddDialog());
        view.getBtnEdit().addActionListener(e -> openEditDialog());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnRefresh().addActionListener(e -> {
            loadData();
            DialogUtil.showSuccess(view, "Schedule refreshed.");
        });
        view.getCmbGradeFilter().addActionListener(e -> refreshTable());
    }

    private void openAddDialog() {
        Frame parent = (Frame) javax.swing.SwingUtilities.getWindowAncestor(view);
        ClassFormDialog dialog = new ClassFormDialog(parent, true);
        dialog.setMode("ADD");
        populateDialogDropdowns(dialog);
        dialog.getBtnSave().addActionListener(e -> handleSave(dialog));
        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }

    private void openEditDialog() {
        int row = view.getSelectedRowIndex();
        if (row == -1) {
            DialogUtil.showWarning(view, "Select a class to edit.");
            return;
        }
        int classId = (int) view.getTblSchedule().getValueAt(row, 0);
        ClassModel selected = allClasses.stream().filter(c -> c.getClassId() == classId).findFirst().orElse(null);
        if (selected == null) {
            return;
        }

        Frame parent = (Frame) javax.swing.SwingUtilities.getWindowAncestor(view);
        ClassFormDialog dialog = new ClassFormDialog(parent, true);
        dialog.setMode("EDIT");
        populateDialogDropdowns(dialog);

        Section sec = service.getAllSections().stream().filter(s -> s.getSectionId() == selected.getSectionId()).findFirst().orElse(null);
        Subject sub = service.getAllSubjects().stream().filter(s -> s.getSubjectId() == selected.getSubjectId()).findFirst().orElse(null);
        Teacher tch = service.getAllTeachers().stream().filter(t -> t.getTeacherId() == selected.getTeacherId()).findFirst().orElse(null);

        dialog.populateForm(selected, sec, sub, tch);
        dialog.getBtnSave().addActionListener(e -> handleUpdate(dialog, selected));
        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }

    private void populateDialogDropdowns(ClassFormDialog dialog) {
        dialog.getCmbSection().removeAllItems();
        for (Section s : sections) {                    // ← Now works
            dialog.getCmbSection().addItem(s);
        }

        dialog.getCmbSubject().removeAllItems();
        for (Subject s : subjects) {                    // ← Now works
            dialog.getCmbSubject().addItem(s);
        }

        dialog.getCmbTeacher().removeAllItems();
        for (Teacher t : teachers) {                    // ← Now works
            dialog.getCmbTeacher().addItem(t);
        }

        dialog.getCmbDay().removeAllItems();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String d : days) {
            dialog.getCmbDay().addItem(d);
        }
    }

    private void handleSave(ClassFormDialog dialog) {
        try {
            ClassModel cls = buildClassFromDialog(dialog);
            service.addClass(cls);
            DialogUtil.showSuccess(dialog, "Added successfully.");
            dialog.dispose();
            loadData();
        } catch (Exception ex) {
            DialogUtil.showError(dialog, "Failed: " + ex.getMessage());
        }
    }

    private void handleUpdate(ClassFormDialog dialog, ClassModel original) {
        try {
            ClassModel cls = buildClassFromDialog(dialog);
            cls.setClassId(original.getClassId());
            service.updateClass(cls);
            DialogUtil.showSuccess(dialog, "Updated successfully.");
            dialog.dispose();
            loadData();
        } catch (Exception ex) {
            DialogUtil.showError(dialog, "Failed: " + ex.getMessage());
        }
    }

    private ClassModel buildClassFromDialog(ClassFormDialog dialog) {
        Section sec = (Section) dialog.getCmbSection().getSelectedItem();
        Subject sub = (Subject) dialog.getCmbSubject().getSelectedItem();
        Teacher tch = (Teacher) dialog.getCmbTeacher().getSelectedItem();

        if (sec == null || sub == null || tch == null) {
            throw new IllegalArgumentException("Please select Section, Subject, and Teacher.");
        }

        ClassModel cls = new ClassModel();
        cls.setSectionId(sec.getSectionId());
        cls.setSubjectId(sub.getSubjectId());
        cls.setTeacherId(tch.getTeacherId());
        cls.setDay(dialog.getCmbDay().getSelectedItem().toString());

        try {
            cls.setStartTime(Integer.parseInt(dialog.getTxtStartTime().getText().trim()));
            cls.setEndTime(Integer.parseInt(dialog.getTxtEndTime().getText().trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Times must be valid integers.");
        }
        return cls;
    }

    private void handleDelete() {
        int row = view.getSelectedRowIndex();
        if (row == -1) {
            DialogUtil.showWarning(view, "Select a class to delete.");
            return;
        }
        int classId = (int) view.getTblSchedule().getValueAt(row, 0);
        if (DialogUtil.showConfirm(view, "Delete this schedule?")) {
            try {
                service.deleteClass(classId);
                DialogUtil.showSuccess(view, "Deleted.");
                loadData();
            } catch (Exception ex) {
                DialogUtil.showError(view, "Failed: " + ex.getMessage());
            }
        }
    }

    private void handleAutoGenerate() {
        // 1. Check if sections are available
        if (sections == null || sections.isEmpty()) {
            DialogUtil.showError(view, "No sections available. Please create a section first.");
            return;
        }

        // 2. Prompt user to select a section
        Section selectedSection = (Section) JOptionPane.showInputDialog(
                view,
                "Select a section to auto-generate schedule:",
                "Auto-Generate Schedule",
                JOptionPane.QUESTION_MESSAGE,
                null,
                sections.toArray(),
                sections.get(0)
        );

        // 3. Handle user cancellation
        if (selectedSection == null) {
            return; // User clicked Cancel
        }

        try {
            // 4. Call Service to generate schedule
            ScheduleGenerationService genService = new ScheduleGenerationService();
            List<ClassModel> created = genService.generateSchedule(selectedSection.getSectionId());

            // 5. Provide feedback
            if (created.isEmpty()) {
                DialogUtil.showWarning(view, "Could not generate schedule. Check if subjects and teachers are available.");
            } else {
                DialogUtil.showSuccess(view, "Successfully auto-generated " + created.size() + " classes!");
                loadData(); // Refresh table
            }

        } catch (Exception ex) {
            DialogUtil.showError(view, "Auto-generation failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
