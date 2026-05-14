package models;

public class ScheduleModel {

    private int scheduleId;
    private String day;
    private int startTime; // Stored as Integer in DB
    private int endTime;   // Stored as Integer in DB

    public ScheduleModel() {
    }

    public ScheduleModel(int scheduleId, String day, int startTime, int endTime) {
        this.scheduleId = scheduleId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    @Override
    public String toString() {
        return day;
    }
}
