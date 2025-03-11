package com.project.examresult.main;

import com.project.examresult.dao.*;
import com.project.examresult.model.User;
import com.project.examresult.util.DBConnection;
import com.project.examresult.util.PDFGenerator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;


public class MainApp {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            if (conn == null) {
                System.out.println("Error: Unable to connect to the database.");
                return;
            }

            UserDAO userDAO = new UserDAO(conn);
            ExamDAO examDAO = new ExamDAO(conn);
            ResultDAO resultDAO = new ResultDAO(conn);

            while (true) {
                System.out.println("--------------------------------------------------------");
                System.out.println("************ Welcome to ONLINE EXAM SYSTEM ************ ");
                System.out.println("--------------------------------------------------------");
                System.out.print("\n👤 Enter username (or type 'exit' to quit): ");
                String username = scanner.nextLine().trim();

                if (username.equalsIgnoreCase("exit")) {
                    System.out.println("👋 Exiting the system. Goodbye!");
                    break;
                }

                System.out.print("🔑 Enter password: ");
                String password = scanner.nextLine().trim();
                User user = userDAO.authenticateUser(username, password);

                if (user != null) {
                    boolean running = true;
                    while (running) {
                        try {
                            if ("admin".equals(user.getRole())) {
                                running = handleAdminMenu(scanner, userDAO, examDAO, resultDAO);
                            } else {
                                running = handleStudentMenu(scanner, examDAO, resultDAO, user);
                            }
                        } catch (Exception e) {
                            System.out.println("❌ An error occurred: " + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("❌ Invalid credentials. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Database connection error: " + e.getMessage());
        }
    }

    private static boolean handleAdminMenu(Scanner scanner, UserDAO userDAO, ExamDAO examDAO, ResultDAO resultDAO) {
        while (true) {
            System.out.println("\n===== 🔹 Admin Menu =====");
            System.out.println("1️. View All Users");
            System.out.println("2️. View All Exams");
            System.out.println("3️. Edit Exam Schedule");
            System.out.println("4️. View All Results");
            System.out.println("5️. Edit Assessment Results");
            System.out.println("6️. Add New Student");
            System.out.println("7️. Add New Exam");
            System.out.println("8. Delete an Exam"); // Add new option
            System.out.println("9. Logout");
            System.out.println("=========================");

            int choice = getValidIntegerInput(scanner, "➡ Enter your choice: ");

            switch (choice) {
                case 1 -> userDAO.getAllUsers().forEach(System.out::println);
                case 2 -> examDAO.getAllExams().forEach(System.out::println);
                case 3 -> editExamSchedule(scanner, examDAO);
                case 4 -> resultDAO.getAllResults().forEach(System.out::println);
                case 5 -> editAssessmentResults(scanner, resultDAO);
                case 6 -> addNewStudent(scanner, userDAO);
                case 7 -> addNewExam(scanner, examDAO);
                case 8 -> deleteExam(scanner, examDAO);
                case 9 -> {
                    System.out.println("🚪 Logging out... Goodbye, Admin!");
                    return false;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }



    private static boolean handleStudentMenu(Scanner scanner, ExamDAO examDAO, ResultDAO resultDAO, User user) {
        System.out.println("\n===== 🎓 Student Menu =====");
        System.out.println("1️. View Profile");
        System.out.println("2️. View Exam Schedule");
        System.out.println("3️. View Exam Results");
        System.out.println("4️. Download Exam Results as PDF");
        System.out.println("5️. Logout");
        System.out.println("===========================");

        int choice = getValidIntegerInput(scanner, "➡ Enter your choice: ");

        switch (choice) {
            case 1 -> System.out.println("\n📄 Profile:\n" + user);
            case 2 -> examDAO.getStudentExams(user.getUserId()).forEach(System.out::println);
            case 3 -> resultDAO.getResultsByStudent(user.getUserId()).forEach(System.out::println);
            case 4 -> downloadResultPDF(scanner, user.getUserId());
            case 5 -> {
                System.out.println("🚪 Logging out... Goodbye, Student!");
                return false;
            }
            default -> System.out.println("❌ Invalid choice. Try again.");
        }
        return true;
    }

    private static void editExamSchedule(Scanner scanner, ExamDAO examDAO) {
        System.out.print("✏ Enter Exam ID to edit: ");
        int examId = getValidIntegerInput(scanner, "");
        System.out.print("📅 Enter new date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine().trim();
        if (examDAO.updateExam(examId, newDate)) {
            System.out.println("✅ Exam schedule updated successfully.");
        } else {
            System.out.println("❌ Failed to update exam schedule.");
        }
    }

    private static void editAssessmentResults(Scanner scanner, ResultDAO resultDAO) {
        System.out.print("✏ Enter Result ID: ");  // Fix: Use Result ID instead of Student ID & Exam ID
        int resultId = getValidIntegerInput(scanner, "");
        System.out.print("📖 Enter new marks: ");  // Fix: Ask for new marks
        int newMarks = getValidIntegerInput(scanner, "");
        if (resultDAO.updateResult(resultId, newMarks)) {
            System.out.println("✅ Result updated successfully.");
        } else {
            System.out.println("❌ Failed to update result.");
        }
    }


    private static void addNewStudent(Scanner scanner, UserDAO userDAO) {
        System.out.print("👤 Enter student username: ");
        String username = scanner.nextLine().trim();
        System.out.print("📧 Enter student email: ");
        String email = scanner.nextLine().trim();
        System.out.print("📱 Enter phone number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("🏫 Enter student name: ");
        String studentName = scanner.nextLine().trim();
        System.out.print("🎓 Enter department: ");
        String department = scanner.nextLine().trim();
        System.out.print("📅 Enter year of study: ");
        int yearOfStudy = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("🔑 Enter password: ");
        String password = scanner.nextLine().trim();

        // Create a User object
        User user = new User(yearOfStudy, username, password, email, phone, studentName, department, password, yearOfStudy);

        // Pass the User object to the DAO method
        if (userDAO.addStudent(user)) {
            System.out.println("✅ Student added successfully.");
        } else {
            System.out.println("❌ Failed to add student.");
        }
    }

    private static void deleteExam(Scanner scanner, ExamDAO examDAO) {
        System.out.print("🗑 Enter Exam ID to delete: ");
        int examId = getValidIntegerInput(scanner, "");

        System.out.print("⚠ Are you sure you want to delete this exam? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes")) {
            if (examDAO.deleteExam(examId)) {
                System.out.println("✅ Exam deleted successfully.");
            } else {
                System.out.println("❌ Failed to delete the exam. Check the Exam ID.");
            }
        } else {
            System.out.println("❌ Deletion canceled.");
        }
    }

    private static void addNewExam(Scanner scanner, ExamDAO examDAO) {
        System.out.print("📚 Enter Subject Name: ");
        String subjectName = scanner.nextLine().trim();

        // Get or Insert Subject ID
        int subjectId = examDAO.getOrInsertSubject(subjectName);
        if (subjectId == -1) {
            System.out.println("❌ Failed to add subject. Please try again.");
            return;
        }

        System.out.print("📅 Enter Exam Date (YYYY-MM-DD): ");
        String examDateStr = scanner.nextLine().trim();

        try {
            Date examDate = Date.valueOf(examDateStr); // Convert String to java.sql.Date
            examDAO.addExam(subjectId, examDate);
            System.out.println("✅ Exam added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid date format! Use YYYY-MM-DD.");
        }
    }

    private static void downloadResultPDF(Scanner scanner, int userId) {
        String defaultPath = System.getProperty("user.home") + "/Documents/student_results.pdf";
        System.out.println("📂 Saving PDF to: " + defaultPath);
        PDFGenerator.generateStudentResultPDF(userId, defaultPath);
    }

    private static int getValidIntegerInput(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } else {
                System.out.println("❌ Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }
}
