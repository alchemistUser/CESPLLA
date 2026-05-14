/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Pololoers
 */
public class Classroom {

    private int classroomId;
    private String roomName; // e.g., "Room 101"
    private int capacity;

    public Classroom() {
    }

    public Classroom(int classroomId, String roomName, int capacity) {
        this.classroomId = classroomId;
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return roomName + " (Cap: " + capacity + ")"; // Display in ComboBoxes
    }
}
