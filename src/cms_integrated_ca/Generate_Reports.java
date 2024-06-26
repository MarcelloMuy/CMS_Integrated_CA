/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The Generate_Reports class provides methods for generating reports from the CMS database.
 * It supports generating reports for courses, students, and lecturers in various output formats
 * such as TXT, CSV, and console.
 * 
 * This class contains methods to:
 * - Generate course reports
 * - Generate student reports 
 * - Generate lecturer reports
 * 
 * Each report generation method retrieves data from the database based on predefined SQL queries
 * and formats the retrieved data into the specified output format.
 * 
 *
 * @author Marce
 */
public class Generate_Reports {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CMS";
    private static final String DB_USER = "pooa2024";
    private static final String DB_PASSWORD = "pooa2024";

    public static void selectReport(String reportType, String outputFormat) {
        switch (reportType) {
            case "course":
                generateCourseReport(outputFormat);
                break;
            case "student":
                generateStudentReport(outputFormat);
                break;
            case "lecturer":
                generateLecturerReport(outputFormat);
                break;
            default:
                System.out.println("Invalid report type.");
                break;
        }
    }
    
     /**
     * Generates a course report based on the specified output format.
     * 
     * @param outputFormat The format in which the report should be generated ("txt", "csv", or "console").
     * 
     * The method executes an SQL query to retrieve data from the CMS database
     */
    public static void generateCourseReport(String outputFormat) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT " +
                "Modules.module_name, " +
                "Courses.course_name, " +
                "COUNT(Enrollments.student_id) AS enrolled_students, " +
                "Lecturers.lecturer_name, " +
                "Rooms.room_name " +
                "FROM " +
                "Modules " +
                "INNER JOIN Modules_Courses ON Modules.module_id = Modules_Courses.module_id " +
                "INNER JOIN Courses ON Modules_Courses.course_id = Courses.course_id " +
                "LEFT JOIN Enrollments ON Modules.module_id = Enrollments.module_id " +
                "LEFT JOIN Lecturers ON Modules.lecturer_id = Lecturers.lecturer_id " +
                "LEFT JOIN Rooms ON Modules.room_id = Rooms.room_id " +
                "GROUP BY " +
                "Modules.module_id, " +
                "Courses.course_name";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                switch (outputFormat) {
                    case "txt":
                        generateTxtCourseReport(rs);
                        break;
                    case "csv":
                        generateCsvCourseReport(rs);
                        break;
                    case "console":
                        generateConsoleCourseReport(rs);
                        break;
                    default:
                        System.out.println("Invalid output format.");
                }
            }
        } catch (SQLException | IOException e) {
        }
    }
     /**
     * Generates a course report in TXT format.
     */
    private static void generateTxtCourseReport(ResultSet rs) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter("course_report.txt")) {
            while (rs.next()) {
                String line = String.format("Module: %s | Course: %s | Enrolled Students: %d | Lecturer: %s | Room: %s%n",
                        rs.getString("module_name"),
                        rs.getString("course_name"),
                        rs.getInt("enrolled_students"),
                        rs.getString("lecturer_name"),
                        rs.getString("room_name"));
                writer.write(line);
            }
        }
        System.out.println("TXT report generated successfully.");
    }
     /**
     * Generates a course report in CSV format.
     */
    private static void generateCsvCourseReport(ResultSet rs) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter("course_report.csv")) {
            writer.write("Module,Course,Enrolled Students,Lecturer,Room\n");
            while (rs.next()) {
                String line = String.format("%s,%s,%d,%s,%s%n",
                        rs.getString("module_name"),
                        rs.getString("course_name"),
                        rs.getInt("enrolled_students"),
                        rs.getString("lecturer_name"),
                        rs.getString("room_name"));
                writer.write(line);
            }
        }
        System.out.println("CSV report generated successfully.");
    }
     /**
     * Generates a course report in the console.
     */
    private static void generateConsoleCourseReport(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("Module: %s | Course: %s | Enrolled Students: %d | Lecturer: %s | Room: %s%n",
                    rs.getString("module_name"),
                    rs.getString("course_name"),
                    rs.getInt("enrolled_students"),
                    rs.getString("lecturer_name"),
                    rs.getString("room_name"));
        }
        System.out.println("Console report generated successfully.");
    }
    
     /**
     * Generates a student report based on the specified output format.
     * 
     * @param outputFormat The format in which the report should be generated ("txt", "csv", or "console").
     * 
     * The method executes an SQL query to retrieve data from the CMS database
     */
    public static void generateStudentReport(String outputFormat) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT " +
                    "CONCAT(s.first_name, ' ', s.last_name) AS full_name, " +
                    "s.student_id, " +
                    "c.course_name, " +
                    "GROUP_CONCAT(DISTINCT m.module_name SEPARATOR ', ') AS enrolled_modules, " +
                    "IF(COUNT(DISTINCT cm.module_name) > 0, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(cm.module_name, ' (Grade: ', sg.grade, ')') SEPARATOR ', '), " +
                    "'No completed modules') AS completed_modules, " +
                    "IF(COUNT(DISTINCT mr.module_name) > 0, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(mr.module_name, ' (Grade: ', sg2.grade, ')') SEPARATOR ', '), " +
                    "'No modules to repeat') AS modules_to_repeat " +
                    "FROM " +
                    "Students s " +
                    "JOIN " +
                    "Courses c ON s.course_type = c.course_id " +
                    "JOIN " +
                    "Enrollments e ON s.student_id = e.student_id " +
                    "JOIN " +
                    "Modules m ON e.module_id = m.module_id " +
                    "LEFT JOIN " +
                    "Completed_Modules comp ON s.student_id = comp.student_id " +
                    "LEFT JOIN " +
                    "Modules cm ON comp.module_id = cm.module_id " +
                    "LEFT JOIN " +
                    "Student_Grades sg ON comp.enrollment_id = sg.enrollment_id " +
                    "LEFT JOIN " +
                    "Modules_To_Repeat mt ON s.student_id = mt.student_id " +
                    "LEFT JOIN " +
                    "Modules mr ON mt.module_id = mr.module_id " +
                    "LEFT JOIN " +
                    "Enrollments er ON mt.student_id = er.student_id AND mt.module_id = er.module_id " +
                    "LEFT JOIN " +
                    "Student_Grades sg2 ON er.enrollment_id = sg2.enrollment_id " +
                    "GROUP BY " +
                    "s.student_id";
            
          
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                switch (outputFormat) {
                    case "txt":
                        generateTxtStudentReport(rs);
                        break;
                    case "csv":
                        generateCsvStudentReport(rs);
                        break;
                    case "console":   
                        generateConsoleStudentReport(rs);
                        break;
                    default:
                        System.out.println("Invalid output format.");
                }
            }
        } catch (SQLException | IOException e) {
        }
    }
    
     /**
     * Generates a student report in TXT format.
     */
    private static void generateTxtStudentReport(ResultSet rs) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter("student_report.txt")) {
            while (rs.next()) {
                String line = String.format("Full Name: %s | Student ID: %d | Course Name: %s | Enrolled Modules: %s | Completed Modules: %s | Modules to Repeat: %s%n",
                        rs.getString("full_name"),
                        rs.getInt("student_id"),
                        rs.getString("course_name"),
                        rs.getString("enrolled_modules"),
                        rs.getString("completed_modules"),
                        rs.getString("modules_to_repeat"));
                writer.write(line);
            }
        }
        System.out.println("TXT report generated successfully.");
    }
    
     /**
     * Generates a student report in CSV format.
     */
    private static void generateCsvStudentReport(ResultSet rs) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter("student_report.csv")) {
            writer.write("Full Name,Student ID,Course Name,Enrolled Modules,Completed Modules,Modules to Repeat\n");
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                int studentId = rs.getInt("student_id");
                String courseName = rs.getString("course_name");
                String enrolledModules = "\"" + rs.getString("enrolled_modules").replaceAll(",", ", ") + "\"";
                String completedModules = "\"" + rs.getString("completed_modules").replaceAll(",", ", ") + "\"";
                String modulesToRepeat = "\"" + rs.getString("modules_to_repeat").replaceAll(",", ", ") + "\"";
                String line = String.format("%s,%d,%s,%s,%s,%s%n",
                        fullName,
                        studentId,
                        courseName,
                        enrolledModules,
                        completedModules,
                        modulesToRepeat);
                writer.write(line);
            }
        }
        System.out.println("CSV report generated successfully.");
    }
    
     /**
     * Generates a student report in the console.
     */
    private static void generateConsoleStudentReport(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("Full Name: %s | Student ID: %d | Course Name: %s | Enrolled Modules: %s | Completed Modules: %s | Modules to Repeat: %s%n",
                    rs.getString("full_name"),
                    rs.getInt("student_id"),
                    rs.getString("course_name"),
                    rs.getString("enrolled_modules"),
                    rs.getString("completed_modules"),
                    rs.getString("modules_to_repeat"));
        }
        System.out.println("Console report generated successfully.");
    }
    
     /**
     * Generates a lecturer report based on the specified output format.
     * 
     * @param outputFormat The format in which the report should be generated ("txt", "csv", or "console").
     * 
     * The method executes an SQL query to retrieve data from the CMS database
     */    
    public static void generateLecturerReport(String outputFormat) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT " +
                    "L.lecturer_name, " +
                    "L.role, " +
                    "GROUP_CONCAT(DISTINCT CT.class_type_name ORDER BY CT.class_type_name) AS class_types, " +
                    "GROUP_CONCAT(DISTINCT M.module_name ORDER BY M.module_name) AS teaching_modules, " +
                    "COUNT(DISTINCT E.student_id) AS num_students_enrolled " +
                    "FROM " +
                    "Lecturers L " +
                    "JOIN " +
                    "Lecturer_Class_Types LCT ON L.lecturer_id = LCT.lecturer_id " +
                    "JOIN " +
                    "Class_Types CT ON LCT.class_type_id = CT.class_type_id " +
                    "JOIN " +
                    "Modules M ON L.lecturer_id = M.lecturer_id " +
                    "LEFT JOIN " +
                    "Enrollments E ON M.module_id = E.module_id " +
                    "GROUP BY " +
                    "L.lecturer_id, L.lecturer_name, L.role";
          
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                switch (outputFormat) {
                    case "txt":
                        generateTxtLecturerReport(rs);
                        break;
                    case "csv":
                        generateCsvLecturerReport(rs);
                        break;
                    case "console":   
                        generateConsoleLecturerReport(rs);
                        break;
                    default:
                        System.out.println("Invalid output format.");
                }
            }
        } catch (SQLException | IOException e) {
        }
    }
    
     /**
     * Generates a lecturer report in TXT format.
     */
    private static void generateTxtLecturerReport(ResultSet rs) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter("lecturer_report.txt")) {
            while (rs.next()) {
                String line = String.format("Lecturer Name: %s | Role: %s | Class Types: %s | Teaching Modules: %s | Number of Students Enrolled: %d%n",
                        rs.getString("lecturer_name"),
                        rs.getString("role"),
                        rs.getString("class_types"),
                        rs.getString("teaching_modules"),
                        rs.getInt("num_students_enrolled"));
                writer.write(line);
            }
        }
        System.out.println("TXT report generated successfully.");
    }
    
     /**
     * Generates a lecturer report in CSV format.
     */
    private static void generateCsvLecturerReport(ResultSet rs) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter("lecturer_report.csv")) {
            writer.write("Lecturer Name,Role,Class Types,Teaching Modules,Number of Students Enrolled\n");
            while (rs.next()) {
                String lecturerName = rs.getString("lecturer_name");
                String role = rs.getString("role");
                String classTypes = "\"" + rs.getString("class_types").replaceAll(",", ", ") + "\"";
                String moduleName = rs.getString("teaching_modules");
                int numStudentsEnrolled = rs.getInt("num_students_enrolled");
                String line = String.format("%s,%s,%s,%s,%d%n",
                        lecturerName,
                        role,
                        classTypes,
                        moduleName,
                        numStudentsEnrolled);
                writer.write(line);
            }
        }
        System.out.println("CSV report generated successfully.");
    }
    
     /**
     * Generates a lecturer report in the console.
     */
    private static void generateConsoleLecturerReport(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("Lecturer Name: %s | Role: %s | Class Types: %s | Teaching Modules: %s | Number of Students Enrolled: %d%n",
                    rs.getString("lecturer_name"),
                    rs.getString("role"),
                    rs.getString("class_types"),
                    rs.getString("teaching_modules"),
                    rs.getInt("num_students_enrolled"));
        }
        System.out.println("Console report generated successfully.");
    }
}
