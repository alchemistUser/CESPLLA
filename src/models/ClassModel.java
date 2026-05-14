package models;

public class ClassModel {

    private int classId;
    private int subjectId;
    private int sectionId;
    private int scheduleId;
    private int teacherId;

    // Helper fields for UI display
    private String subjectName;
    private String sectionName;
    private String day;
    private int startTime;
    private int endTime;
    private String teacherName;

    public ClassModel() {
    }

    public ClassModel(int classId, int subjectId, int sectionId, int scheduleId, int teacherId) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.sectionId = sectionId;
        this.scheduleId = scheduleId;
        this.teacherId = teacherId;
    }

    // Getters and Setters for IDs
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    // Getters and Setters for Display
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
