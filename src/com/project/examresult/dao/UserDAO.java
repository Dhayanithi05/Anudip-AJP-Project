package com.project.examresult.dao;

import com.project.examresult.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ Authenticate User and Fetch Student Details If Applicable
    public User authenticateUser(String username, String password) {
        String sql = "SELECT user_id, username, role, email, phone, student_name, department, year_of_study " +
                     "FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String role = rs.getString("role");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");

                    if ("student".equalsIgnoreCase(role)) {
                        String studentName = rs.getString("student_name");
                        String department = rs.getString("department");
                        int yearOfStudy = rs.getInt("year_of_study");

                        return new User(userId, username, "", role, email, phone, studentName, department, yearOfStudy);
                    } else {
                        return new User(userId, username, "", role, email, phone);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error during authentication: " + e.getMessage());
        }
        return null;
    }

    // ✅ Fetch All Users (Admins and Students)
    public List<String> getAllUsers() {
        List<String> usersTable = new ArrayList<>();
        String sql = "SELECT user_id, username, role, email, phone, student_name, department, year_of_study FROM users";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                usersTable.add("No users found.");
                return usersTable;
            }

            // Table Header
            usersTable.add("+---------+-----------------+----------+----------------------+------------+-----------------+--------------------------------+--------------+");
            usersTable.add("| User ID | Username        | Role     | Email                | Phone      | Student Name    | Department                         | Year of Study|");
            usersTable.add("+---------+-----------------+----------+----------------------+------------+-----------------+--------------------------------+--------------+");

            // Table Rows
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                if ("student".equalsIgnoreCase(role)) {
                    String studentName = rs.getString("student_name");
                    String department = rs.getString("department");
                    int yearOfStudy = rs.getInt("year_of_study");

                    usersTable.add(String.format("| %-7d | %-15s | %-8s | %-20s | %-10s | %-15s | %-30s | %-12d |",
                            userId, username, role, email, phone, studentName, department, yearOfStudy));
                } else {
                    usersTable.add(String.format("| %-7d | %-15s | %-8s | %-20s | %-10s | %-15s | %-30s | %-12s |",
                            userId, username, role, email, phone, "N/A", "N/A", "N/A"));
                }
            }

            // Table Footer
            usersTable.add("+---------+-----------------+----------+----------------------+------------+-----------------+-------------+-----------------------------------+");

        } catch (SQLException e) {
            usersTable.add("❌ Error fetching users: " + e.getMessage());
        }
        return usersTable;
    }
 // ✅ Add New Student (Now Accepts a User Object)
    public boolean addStudent(User user) {
        String sql = "INSERT INTO users (username, password, role, email, phone, student_name, department, year_of_study) " +
                     "VALUES (?, ?, 'student', ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getStudentName());
            pstmt.setString(6, user.getDepartment());
            pstmt.setInt(7, user.getYearOfStudy());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error adding student: " + e.getMessage());
        }
        return false;
    }
    public List<String> getUserById(int userId) {
        List<String> userTable = new ArrayList<>();
        String sql = "SELECT user_id, username, role, email, phone, student_name, department, year_of_study " +
                     "FROM users WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    userTable.add("No user found with ID: " + userId);
                    return userTable;
                }

                // Table Header
                userTable.add("+---------+-----------------+----------+----------------------+------------+-----------------+-------------+--------------+");
                userTable.add("| User ID | Username        | Role     | Email                | Phone      | Student Name    | Department  | Year of Study |");
                userTable.add("+---------+-----------------+----------+----------------------+------------+-----------------+-------------+--------------+");

                // Table Row
                String formattedRow = String.format("| %-7d | %-15s | %-8s | %-20s | %-10s | %-15s | %-11s | %-12s |",
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("student_name") != null ? rs.getString("student_name") : "N/A",
                    rs.getString("department") != null ? rs.getString("department") : "N/A",
                    rs.getObject("year_of_study") != null ? rs.getInt("year_of_study") : "N/A"
                );
                userTable.add(formattedRow);

                // Table Footer
                userTable.add("+---------+-----------------+----------+----------------------+------------+-----------------+-------------+--------------+");
            }
        } catch (SQLException e) {
            userTable.add("❌ Error fetching user: " + e.getMessage());
        }
        return userTable;
    }



    // ✅ Add New Student (Fixed Signature)
    public boolean addStudent(String username, String password, String email, String phone, String studentName, String department, int yearOfStudy) {
        String sql = "INSERT INTO users (username, password, role, email, phone, student_name, department, year_of_study) " +
                     "VALUES (?, ?, 'student', ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Consider hashing before storing
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, studentName);
            pstmt.setString(6, department);
            pstmt.setInt(7, yearOfStudy);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error adding student: " + e.getMessage());
        }
        return false;
    }
    public User getUserDetailsById(int userId) {
        String sql = "SELECT user_id, username, role, email, phone, student_name, department, year_of_study FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        "",
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("student_name"),
                        rs.getString("department"),
                        rs.getInt("year_of_study")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user: " + e.getMessage());
        }
        return null;
    }

}
