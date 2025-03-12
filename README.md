# Online Exam Result Management System

## 📌 Project Overview
The **Online Exam Result System** is a Java-based application built using **Spring Boot** (backend) and **Java Swing** (frontend). It facilitates secure management of exams, students, and results. The system provides admin functionalities such as managing exams and students, while students can view their results and download them as PDF reports.

## 🛠️ Features
- **User Authentication** (Admin & Students)
#### Admin Panel:
- View All Users
- View All Exams
- Edit Exam Schedule
- View All Results
- Edit Assessment Results
- Add New Student
- Add New Exam
- Logout

#### Student Panel:
- View Profile
- View Exam Schedule
- View Exam Results
- Download Exam Results as PDF
- Logout
- **Database Integration** (MySQL)
- **Exception Handling** with Custom Exceptions
- **Logging & Debugging Support**
- **Modular Codebase for Scalability**

## 🚀 Tech Stack
- **Backend:** Java (JDBC, MySQL, DAO pattern)
- **Database:** MySQL
- **PDF Generation:** iText Library
- **Exception Handling:** Custom Exception Classes

## 📂 Project Structure
```
onlineexamsystem
│── JRE System Library [JavaSE-21]
│── src
│   ├── com.project.examresult.dao
│   │   ├── ExamDAO.java
│   │   ├── ResultDAO.java
│   │   ├── UserDAO.java
│   │
│   ├── com.project.examresult.main
│   │   ├── MainApp.java
│   │
│   ├── com.project.examresult.model
│   │   ├── Result.java
│   │   ├── User.java
│   │
│   ├── com.project.examresult.util
│   │   ├── CustomException.java
│   │   ├── DBConnection.java
│   │   ├── PDFGenerator.java
│
│── Referenced Libraries
```

## 📜 Installation Guide
### 🔹 Prerequisites
- Java JDK (v8 or above)
- MySQL Database
- Eclipse/IntelliJ IDE (Recommended)

### 🔹 Database Setup
1. Create a MySQL database:
   ```sql
   CREATE DATABASE exam_management;
   ```
2. Update database credentials in `DBConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/exam_management";
   private static final String USER = "root";
   private static final String PASSWORD = "your_password";
   ```
3. Import the required SQL tables (Users, Exams, Results, etc.).

### 🔹 Run the Application
1. Clone this repository:
   ```bash
   git clone https://github.com/Dhayanithi05/Anudip-AJP-Project
   ```
2. Navigate to the project directory:
   ```bash
   cd Anudip-AJP-Project
   ```
3. Compile and run the application:
   ```bash
   javac -cp . MainApp.java && java -cp . com.project.examresult.main.MainApp
   ```

## 📜 Contributing
1. Fork the repository.
2. Create a new branch (`feature-branch`).
3. Commit your changes.
4. Push to the branch and submit a PR.

