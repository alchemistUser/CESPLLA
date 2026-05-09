/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Pololoers
 */
public class User {

    private int userId;
    private int roleId;
    private String name;
    private String password;
    private String email;
    private String roleName;

    public User() {
    }

    public User(int userId, int roleId, String name, String password, String email, String roleName) {
        this.userId = userId;
        this.roleId = roleId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.roleName = roleName;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
