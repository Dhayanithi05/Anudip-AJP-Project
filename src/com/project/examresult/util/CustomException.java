package com.project.examresult.util;

@SuppressWarnings("serial")
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }

    // Exception for invalid selection
    public static class InvalidSelectionException extends CustomException {
        public InvalidSelectionException() {
            super("Invalid selection. Please enter a valid number.");
        }
    }

    // Exception for invalid data type
    public static class InvalidDataTypeException extends CustomException {
        public InvalidDataTypeException() {
            super("Invalid input. Please enter data in the correct format.");
        }
    }

    // Exception for database errors
    public static class DatabaseException extends CustomException {
        public DatabaseException(String message) {
            super("Database error: " + message);
        }
    }

    // Exception when user is not found
    public static class UserNotFoundException extends CustomException {
        public UserNotFoundException() {
            super("User not found. Please check your credentials.");
        }
    }

    // Exception for duplicate user registration
    public static class DuplicateUserException extends CustomException {
        public DuplicateUserException() {
            super("User already exists. Please use a different username.");
        }
    }

    // Exception when exam ID is not found
    public static class ExamNotFoundException extends CustomException {
        public ExamNotFoundException() {
            super("Exam not found. Please enter a valid Exam ID.");
        }
    }

    // Exception when result ID is not found
    public static class ResultNotFoundException extends CustomException {
        public ResultNotFoundException() {
            super("Result not found. Please enter a valid Result ID.");
        }
    }

    // Exception for invalid date format
    public static class InvalidDateFormatException extends CustomException {
        public InvalidDateFormatException() {
            super("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    // Exception for invalid marks
    public static class InvalidMarksException extends CustomException {
        public InvalidMarksException() {
            super("Invalid marks. Marks should be between 0 and 100.");
        }
    }
}
