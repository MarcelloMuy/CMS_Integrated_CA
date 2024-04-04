/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
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
//                generateStudentReport();
                break;
            case "lecturer":
//                generateLecturerReport();
                break;
            default:
                System.out.println("Invalid report type.");
                break;
        }
    }

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
                        generateTxtReport(rs);
                        break;
                    case "csv":
                        generateCsvReport(rs);
                        break;
                    case "console":
                        generateConsoleReport(rs);
                        break;
                    default:
                        System.out.println("Invalid output format.");
                }
            }
        } catch (SQLException | IOException e) {
        }
    }

    private static void generateTxtReport(ResultSet rs) throws SQLException, IOException {
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

    private static void generateCsvReport(ResultSet rs) throws SQLException, IOException {
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

    private static void generateConsoleReport(ResultSet rs) throws SQLException {
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
}