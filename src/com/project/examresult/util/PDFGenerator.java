package com.project.examresult.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.examresult.dao.ResultDAO;
import com.project.examresult.dao.UserDAO;
import com.project.examresult.model.Result;
import com.project.examresult.model.User;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGenerator {
    public static void generateStudentResultPDF(int userId, String filePath) {
        try {
            Connection conn = DBConnection.getConnection();
            UserDAO userDAO = new UserDAO(conn);
            ResultDAO resultDAO = new ResultDAO(conn);

            // Fetch user details (Fix)
            User user = userDAO.getUserDetailsById(userId);  // Changed to correct method
            if (user == null) {
                System.out.println("⚠ User not found.");
                return;
            }

            // Fetch student results (Fix)
            List<Result> results = resultDAO.getResultsListByStudent(userId);  // Changed to correct method
            if (results.isEmpty()) {
                System.out.println("⚠ No results found for this student.");
                return;
            }

            // Create PDF Document
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Student Exam Results", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Add Student Profile Details
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("Student ID: " + user.getUserId(), infoFont));
            document.add(new Paragraph("Name: " + user.getStudentName(), infoFont));
            document.add(new Paragraph("Email: " + user.getEmail(), infoFont));
            document.add(new Paragraph("Department: " + user.getDepartment(), infoFont));
            document.add(new Paragraph("Year of Study: " + user.getYearOfStudy(), infoFont));
            document.add(new Paragraph("\n"));

            // Create Table for Exam Results
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add Table Headers
            table.addCell("Exam ID");
            table.addCell("Subject");
            table.addCell("Marks");
            table.addCell("Grade");

            // Add Exam Results
            for (Result res : results) {
                table.addCell(String.valueOf(res.getExamId()));
                table.addCell(res.getSubject());
                table.addCell(String.valueOf(res.getMarks()));
                table.addCell(res.getGrade());
            }

            document.add(table);

            // Add Download Timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Font timestampFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph timestampParagraph = new Paragraph("Downloaded on: " + timestamp, timestampFont);
            timestampParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(new Paragraph("\n"));
            document.add(timestampParagraph);

            document.close();
            System.out.println("✅ PDF generated successfully: " + filePath);
        } catch (Exception e) {
            System.out.println("❌ Error generating PDF: " + e.getMessage());
        }
    }
}
