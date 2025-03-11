package com.project.examresult.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;
    private String studentName;
    private String department;
    private int yearOfStudy;

    // Constructor for students
    public User(int userId, String username, String password, String role, String email, String phone,
                String studentName, String department, int yearOfStudy) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.studentName = studentName;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
    }

    // Constructor for admins (without student details)
    public User(int userId, String username, String password, String role, String email, String phone) {
        this(userId, username, password, role, email, phone, null, null, 0);
    }

    // ✅ Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {  // Added this getter
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {  // Added this getter
        return email;
    }

    public String getPhone() {  // Added this getter
        return phone;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDepartment() {
        return department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    @Override
    public String toString() {
        if ("student".equalsIgnoreCase(role)) {
            return "User" +
                    "\nuserId=" + userId + ", \nusername='" + username + '\'' +
                    "\nrole='" + role + '\'' +
                    ", \nemail='" + email + '\'' +
                    ", \nphone='" + phone + '\'' +
                    ", \nstudentName='" + studentName + '\'' +
                    ", \ndepartment='" + department + '\'' +
                    ", \nyearOfStudy=" + yearOfStudy;
        } else {
            return "User" +
                    "\nuserId=" + userId +
                    ", \nusername='" + username + '\'' +
                    ", \nrole='" + role + '\'' +
                    ", \nemail='" + email + '\'' +
                    ", \nphone='" + phone + '\'';
        }
    }
}
