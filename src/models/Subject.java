package models;

public class Subject {

    private int subjectId;
    private String gradeLevel;
    private String subjectName;
    private int timeAlloted; // Mapped from 'time_alloted' (Integer)

    public Subject() {
    }

    public Subject(int subjectId, String gradeLevel, String subjectName, int timeAlloted) {
        this.subjectId = subjectId;
        this.gradeLevel = gradeLevel;
        this.subjectName = subjectName;
        this.timeAlloted = timeAlloted;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTimeAlloted() {
        return timeAlloted;
    }

    public void setTimeAlloted(int timeAlloted) {
        this.timeAlloted = timeAlloted;
    }

    @Override
    public String toString() {
        return subjectName;
    }
}
