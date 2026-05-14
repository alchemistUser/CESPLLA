/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

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
import java.util.List;

public class ClassScheduleService {

    private final ClassDAO classDAO;
    private final ScheduleDAO scheduleDAO;
    private final SectionDAO sectionDAO;
    private final SubjectDAO subjectDAO;
    private final TeacherDAO teacherDAO;

    public ClassScheduleService() {
        this.classDAO = new ClassDAO();
        this.scheduleDAO = new ScheduleDAO();
        this.sectionDAO = new SectionDAO();
        this.subjectDAO = new SubjectDAO();
        this.teacherDAO = new TeacherDAO();
    }

    public List<ClassModel> getAllClassesWithDetails() throws SQLException {
        return classDAO.findAllWithDetails();
    }

    public List<Section> getAllSections() {
        return sectionDAO.findAll();
    }

    public List<Subject> getAllSubjects() {
        return subjectDAO.findAll();
    }

    public List<Teacher> getAllTeachers() {
        return teacherDAO.findAll();
    }

    public void addClass(ClassModel cls) throws SQLException {
        validateClass(cls);
        int scheduleId = findOrCreateSchedule(cls.getDay(), cls.getStartTime(), cls.getEndTime());
        cls.setScheduleId(scheduleId);
        classDAO.insert(cls);
    }

    public void updateClass(ClassModel cls) throws SQLException {
        validateClass(cls);
        int scheduleId = findOrCreateSchedule(cls.getDay(), cls.getStartTime(), cls.getEndTime());
        cls.setScheduleId(scheduleId);
        classDAO.update(cls);
    }

    public void deleteClass(int classId) throws SQLException {
        classDAO.delete(classId);
    }

    private void validateClass(ClassModel cls) {
        if (cls.getSectionId() <= 0 || cls.getSubjectId() <= 0 || cls.getTeacherId() <= 0) {
            throw new IllegalArgumentException("Section, Subject, and Teacher are required.");
        }
        if (cls.getDay() == null || cls.getDay().trim().isEmpty()) {
            throw new IllegalArgumentException("Day is required.");
        }
        if (cls.getStartTime() < 0 || cls.getEndTime() <= cls.getStartTime()) {
            throw new IllegalArgumentException("End time must be greater than start time.");
        }
    }

    private int findOrCreateSchedule(String day, int startTime, int endTime) {
        List<ScheduleModel> schedules = scheduleDAO.findAll();
        for (ScheduleModel s : schedules) {
            if (s.getDay().equals(day) && s.getStartTime() == startTime && s.getEndTime() == endTime) {
                return s.getScheduleId();
            }
        }

        ScheduleModel newSch = new ScheduleModel();
        newSch.setDay(day);
        newSch.setStartTime(startTime);
        newSch.setEndTime(endTime);
        scheduleDAO.insert(newSch);

        schedules = scheduleDAO.findAll();
        for (ScheduleModel s : schedules) {
            if (s.getDay().equals(day) && s.getStartTime() == startTime && s.getEndTime() == endTime) {
                return s.getScheduleId();
            }
        }
        throw new RuntimeException("Failed to create schedule record.");
    }
}
