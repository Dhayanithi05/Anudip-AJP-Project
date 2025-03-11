package com.project.examresult.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class ExamDAO {
    private Connection conn;

    public ExamDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllExams() {
        List<String> exams = new ArrayList<>();
        String sql = "SELECT e.exam_id, s.subject_name, e.exam_date FROM exams e JOIN subjects s ON e.subject_id = s.subject_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Add table header
            exams.add("+---------+------------------+------------+");
            exams.add("| Exam ID | Subject          | Exam Date  |");
            exams.add("+---------+------------------+------------+");

            // Add table rows
            while (rs.next()) {
                exams.add(String.format("| %-7d | %-16s | %-10s |",
                        rs.getInt("exam_id"),
                        rs.getString("subject_name"),
                        rs.getDate("exam_date").toString()));
            }
            exams.add("+---------+------------------+------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }


    public boolean updateExam(int examId, String newDate) {
        String sql = "UPDATE exams SET exam_date = ? WHERE exam_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Convert String to java.sql.Date
            Date sqlDate = Date.valueOf(newDate);

            pstmt.setDate(1, sqlDate);
            pstmt.setInt(2, examId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) { 
            System.out.println("❌ Invalid date format! Use YYYY-MM-DD.");
            return false;
        }
    }

    public void addExam(int subjectId, Date examDate) {
        String sql = "INSERT INTO exams (subject_id, exam_date) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            pstmt.setDate(2, examDate);
            pstmt.executeUpdate();
            System.out.println("✅ Exam added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getOrInsertSubject(String subjectName) {
        String selectSql = "SELECT subject_id FROM subjects WHERE subject_name = ?";
        String insertSql = "INSERT INTO subjects (subject_name) VALUES (?)";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, subjectName);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("subject_id"); // Subject exists, return ID
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, subjectName);
                    int affectedRows = insertStmt.executeUpdate();

                    if (affectedRows > 0) {
                        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1); // Return newly created subject ID
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failed to insert
    }
    public boolean deleteExam(int examId) {
        String sql = "DELETE FROM exams WHERE exam_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, examId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getStudentExams(int studentId) {
        List<String> exams = new ArrayList<>();
        String sql = "SELECT e.exam_id, s.subject_name, e.exam_date " +
                     "FROM exams e " +
                     "JOIN subjects s ON e.subject_id = s.subject_id " + 
                     "JOIN results r ON e.exam_id = r.exam_id " +
                     "WHERE r.user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            // Add table header
            exams.add("+---------+---------------------+------------+");
            exams.add("| Exam ID | Subject             | Exam Date  |");
            exams.add("+---------+---------------------+------------+");

            // Add table rows
            while (rs.next()) {
                exams.add(String.format("| %-7d | %-20s | %-10s |",
                        rs.getInt("exam_id"),
                        rs.getString("subject_name"),
                        rs.getDate("exam_date").toString()));
            }
            exams.add("+---------+------------------+---------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
}