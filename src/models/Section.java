package models;

public class Section {

    private int sectionId;
    private String gradeLevel;
    private String sectionName;

    public Section() {
    }

    public Section(int sectionId, String gradeLevel, String sectionName) {
        this.sectionId = sectionId;
        this.gradeLevel = gradeLevel;
        this.sectionName = sectionName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @Override
    public String toString() {
        return sectionName + " - " + gradeLevel;
    }
}
