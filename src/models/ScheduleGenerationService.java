/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Pololoers
 */
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the Greedy Algorithm for Auto-Generating Class Schedules. Ref:
 * Figure 27 (Autogenerate Schedule) & Page 118 of Documentation.
 */
public class ScheduleGenerationService {

    private final ClassDAO classDAO;
    private final ScheduleDAO scheduleDAO;
    private final SubjectDAO subjectDAO;
    private final TeacherDAO teacherDAO;
    private final SectionDAO sectionDAO;

    public ScheduleGenerationService() {
        this.classDAO = new ClassDAO();
        this.scheduleDAO = new ScheduleDAO();
        this.subjectDAO = new SubjectDAO();
        this.teacherDAO = new TeacherDAO();
        this.sectionDAO = new SectionDAO();
    }

    /**
     * Generates a conflict-free schedule for a specific Section.
     *
     * @param sectionId The ID of the section to generate for.
     * @return List of successfully scheduled classes.
     */
    public List<ClassModel> generateSchedule(int sectionId) throws SQLException {
        // 1. Get Section Details
        List<Section> allSections = sectionDAO.findAll();
        Section targetSection = allSections.stream()
                .filter(s -> s.getSectionId() == sectionId)
                .findFirst()
                .orElseThrow(() -> new SQLException("Section not found"));

        String gradeLevel = targetSection.getGradeLevel();

        // 2. Fetch Resources
        List<Subject> allSubjects = subjectDAO.findAll();
        List<Teacher> allTeachers = teacherDAO.findAll();
        List<ScheduleModel> allSlots = scheduleDAO.findAll();
        List<ClassModel> existingClasses = classDAO.findAllWithDetails();

        // Filter Subjects for this Grade Level
        List<Subject> subjectsToSchedule = allSubjects.stream()
                .filter(s -> s.getGradeLevel().equals(gradeLevel))
                // GREEDY HEURISTIC: Sort by time_alloted DESCENDING 
                // (Schedule longest subjects first to avoid fragmentation)
                .sorted(Comparator.comparingInt(Subject::getTimeAlloted).reversed())
                .collect(Collectors.toList());

        List<ClassModel> generatedClasses = new ArrayList<>();

        // 3. Greedy Assignment Loop
        for (Subject subject : subjectsToSchedule) {
            boolean scheduled = false;

            // Find a Teacher for this Subject (Match teacher.subject column to subject.subject_name)
            // Fallback: Pick any teacher if specific match isn't strict
            List<Teacher> candidateTeachers = allTeachers.stream()
                    .filter(t -> t.getSubject() != null && t.getSubject().equalsIgnoreCase(subject.getSubjectName()))
                    .collect(Collectors.toList());

            if (candidateTeachers.isEmpty()) {
                candidateTeachers = allTeachers; // Fallback if no specific teacher found
            }

            // Try to fit the subject into a slot
            for (ScheduleModel slot : allSlots) {
                // Check if slot duration is sufficient
                int slotDuration = slot.getEndTime() - slot.getStartTime();
                if (slotDuration < subject.getTimeAlloted()) {
                    continue; // Slot is too short for this subject
                }

                // Check conflicts for each candidate teacher
                for (Teacher teacher : candidateTeachers) {
                    if (isConflictFree(existingClasses, generatedClasses, sectionId, teacher.getTeacherId(), slot)) {

                        // --- SUCCESS: Create Class Entry ---
                        ClassModel newClass = new ClassModel();
                        newClass.setSectionId(sectionId);
                        newClass.setSubjectId(subject.getSubjectId());
                        newClass.setTeacherId(teacher.getTeacherId());
                        newClass.setScheduleId(slot.getScheduleId());

                        // Save to Database
                        classDAO.insert(newClass);

                        // Add to generated list (so we check against it for subsequent subjects in this run)
                        generatedClasses.add(newClass);
                        scheduled = true;
                        break; // Move to next subject
                    }
                }
                if (scheduled) {
                    break;
                }
            }

            if (!scheduled) {
                System.out.println("Warning: Could not schedule subject: " + subject.getSubjectName());
            }
        }

        return generatedClasses;
    }

    /**
     * Checks if assigning a Teacher to a Slot creates a conflict. Conflict
     * occurs if: 1. The Teacher is already teaching in that Slot. 2. The
     * Section is already occupied in that Slot.
     */
    private boolean isConflictFree(List<ClassModel> existing, List<ClassModel> newClasses,
            int sectionId, int teacherId, ScheduleModel slot) {

        // Combine existing DB records and records generated in this session
        List<ClassModel> allRecords = new ArrayList<>(existing);
        allRecords.addAll(newClasses);

        for (ClassModel c : allRecords) {
            // Check Time Overlap
            // Assuming slot times are integers like 800 (8:00) and 900 (9:00)
            // Overlap exists if: StartA < EndB AND EndA > StartB
            // Note: This assumes your ScheduleModel has start/end times accessible. 
            // You may need to fetch slot details if not cached, but here we pass the slot object.

            // We need the slot details for the EXISTING class to compare
            // Since ClassModel has start/end time fields (populated by DAO join), we use those:
            boolean timeOverlap = (slot.getStartTime() < c.getEndTime()) && (slot.getEndTime() > c.getStartTime());

            if (timeOverlap) {
                // Check Teacher Conflict
                if (c.getTeacherId() == teacherId) {
                    return false;
                }

                // Check Section Conflict
                if (c.getSectionId() == sectionId) {
                    return false;
                }
            }
        }
        return true;
    }
}
