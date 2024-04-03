/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;
/**
 *
 * @author Marce
 */


public class Generate_Reports {
    
   public static void selectReport(String reportType) {
    switch (reportType) {
        case "course":
            generateCourseReport();
            break;
        case "student":
            generateStudentReport();
            break;
        case "lecturer":
            generateLecturerReport();
            break;
        default:
            System.out.println("Invalid report type.");
            break;
    }
}

    private static void generateCourseReport() {
        System.out.println("CourseReport"); 
    }

    private static void generateStudentReport() {
        System.out.println("Student");     
    }

    private static void generateLecturerReport() {
        System.out.println("Lecturer");   
    }
    
}