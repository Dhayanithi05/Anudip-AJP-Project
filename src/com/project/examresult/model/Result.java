package com.project.examresult.model;

public class Result {
    private int examId;
    private String subject;
    private int marks;
    private String grade;

    // Constructor
    public Result(int examId, String subject, int marks, String grade) {
        this.examId = examId;
        this.subject = subject;
        this.marks = marks;
        this.grade = grade;
    }

    // Getters and Setters
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // ✅ Override toString() for proper output display
    @Override
    public String toString() {
        return "Exam ID: " + examId +
               ", Subject: " + subject +
               ", Marks: " + marks +
               ", Grade: " + grade;
    }
}
