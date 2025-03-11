package com.project.examresult.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.project.examresult.model.Result;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultDAO {
    private static final Logger LOGGER = Logger.getLogger(ResultDAO.class.getName());
    private Connection conn;

    public ResultDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addResult(int userId, int examId, int marks, String grade) {
        String sql = "INSERT INTO results (user_id, exam_id, marks, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, examId);
            pstmt.setInt(3, marks);
            pstmt.setString(4, grade);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding result", e);
        }
        return false;
    }

    public boolean updateResult(int resultId, int newMarks) {
        String sql = "UPDATE results SET marks = ?, grade = ? WHERE result_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newMarks);
            pstmt.setString(2, calculateGrade(newMarks));  // Fix: Set grade based on marks
            pstmt.setInt(3, resultId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating result", e);
        }
        return false;
    }

    private String calculateGrade(int marks) {
        if (marks >= 90) return "A";
        else if (marks >= 80) return "B";
        else if (marks >= 70) return "C";
        else if (marks >= 60) return "D";
        else return "F";
    }


    public List<String> getAllResults() {
        List<String> resultsTable = new ArrayList<>();
        String sql = "SELECT result_id, user_id, exam_id, marks, grade FROM results";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {  // Check if the ResultSet is empty
                resultsTable.add("No results found.");
                return resultsTable;
            }

            // Table Header
            resultsTable.add("+------------+---------+---------+-------+-------+");
            resultsTable.add("| Result ID  | User ID | Exam ID | Marks | Grade |");
            resultsTable.add("+------------+---------+---------+-------+-------+");

            // Table Rows
            while (rs.next()) {
                String formattedRow = String.format("| %-10d | %-7d | %-7d | %-5d | %-5s |",
                    rs.getInt("result_id"),
                    rs.getInt("user_id"),
                    rs.getInt("exam_id"),
                    rs.getInt("marks"),
                    rs.getString("grade")
                );
                resultsTable.add(formattedRow);
            }

            // Table Footer
            resultsTable.add("+------------+---------+---------+-------+-------+");

        } catch (SQLException e) {
            resultsTable.add("❌ Error retrieving all results: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error retrieving all results", e);
        }
        return resultsTable;
    }


    public List<String> getResultsByStudent(int studentId) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT r.result_id, r.exam_id, sub.subject_name, r.marks, r.grade " +
                     "FROM results r " +
                     "JOIN exams e ON r.exam_id = e.exam_id " +
                     "JOIN subjects sub ON e.subject_id = sub.subject_id " +
                     "WHERE r.user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    results.add("No results found for Student ID: " + studentId);
                    return results;
                }

                // Table header
                results.add("+---------+-----------------+-------+-------+");
                results.add("| Exam ID | Subject         | Marks | Grade |");
                results.add("+---------+-----------------+-------+-------+");

                // Table rows
                while (rs.next()) {
                    results.add(String.format("| %-7d | %-15s | %-5d | %-5s |",
                            rs.getInt("exam_id"),
                            rs.getString("subject_name"),
                            rs.getInt("marks"),
                            rs.getString("grade")));
                }

                // Table footer
                results.add("+---------+-----------------+-------+-------+");

            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving results for Student ID: " + studentId, e);
            results.add("Error retrieving results.");
        }

        return results;
    }

    private String formatResult(ResultSet rs) throws SQLException {
        return "Result ID: " + rs.getInt("result_id") +
               ", User ID: " + rs.getInt("user_id") +
               ", Exam ID: " + rs.getInt("exam_id") +
               ", Marks: " + rs.getInt("marks") +
               ", Grade: " + rs.getString("grade");
    }
    public List<Result> getResultsListByStudent(int studentId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT r.result_id, r.exam_id, sub.subject_name, r.marks, r.grade " +
                     "FROM results r " +
                     "JOIN exams e ON r.exam_id = e.exam_id " +
                     "JOIN subjects sub ON e.subject_id = sub.subject_id " +
                     "WHERE r.user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Result(
                        rs.getInt("result_id"),
                        rs.getString("subject_name"),
                        rs.getInt("marks"),
                        rs.getString("grade")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching results: " + e.getMessage());
        }
        return results;
    }

}
