/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * This class represents the main menu system of the CMS (Content Management System).
 * It allows users to log in, displays appropriate menus based on their roles, and provides options for managing users and reports.
 * 
 * The class uses JDBC for database interaction and includes methods for handling user input and displaying menus.
 * 
 * @author Marce
 */
public class Menu_System {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CMS";
    private static final String USER = "pooa2024";
    private static final String PASSWORD = "pooa2024";
    
    private static String loggedInUsername; // Variable to store the username of the logged-in user
    
     /**
     * The main method of the CMS menu system.
     * It displays the welcome message and presents options for logging in or exiting the program.
     * 
     * @param args The command line arguments.
     */   
    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Welcome to the CMS!");
            System.out.println("Please select an option:");
            System.out.println("1. Login");
            System.out.println("2. Exit");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    exit = true;
                    System.out.println("You exit the CMS program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        // Close resources
        scanner.close();
    }
    
     /**
     * Prompts the user with a given message to enter an integer input and returns it.
     * Keeps prompting until a valid integer input is provided.
     * 
     * @param prompt The message prompt to display to the user.
     * @return The integer input provided by the user.
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
    
     /**
     * Handles the login process for users.
     * Prompts the user to enter their username and password.
     * Retrieves user information from the database and verifies credentials.
     * Displays appropriate menus based on the user's role after successful login.
     */
    private static void login() {
        System.out.println("Login:");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (storedPassword.equals(password)) {
                            String role = rs.getString("role");
                            System.out.println(role + " login successful!");
                            switch (role) {
                            case "admin":
                                loggedInUsername = username;
                                displayAdminMenu();
                                break;
                            case "office":
                                loggedInUsername = username;
                                displayOfficeMenu();
                                break;
                            case "lecturer":
                                loggedInUsername = username;
                                displayLecturerMenu();
                                break;
                            default:
                                System.out.println("Invalid role.");
                                break;
                        }
                            return;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        
        System.out.println("Invalid username or password. Please try again.");
    }
    
      /**
     * Displays the menu options available to administrators.
     * Allows administrators to manage users or update their own credentials.
     * Provides an option to logout.
     */
    private static void displayAdminMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\nAdmin Menu:");
            System.out.println("Please select an option:");
            System.out.println("1. Manage Users");
            System.out.println("2. Update Credentials");
            System.out.println("3. Logout");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    Manage_Users.DisplayMenu();
                    break;
                case 2:                    
                    Manage_Users.UpdateCredentials(loggedInUsername);                    
                    break;
                case 3:
                    logout = true;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
     /**
     * Displays the menu options available to office staff.
     * Allows office staff to generate reports or update their own credentials.
     * Provides an option to logout.
     */
    private static void displayOfficeMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\nOffice Menu:");
            System.out.println("Please select an option:");
            System.out.println("1. Generate Course Report");
            System.out.println("2. Generate Student Report");
            System.out.println("3. Generate Lecturer Report");
            System.out.println("4. Update Credentials");
            System.out.println("5. Logout");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    generateReportMenu("course");
                    break;
                case 2:
                    generateReportMenu("student");
                    break;
                case 3:
                    generateReportMenu("lecturer");
                    break;
                case 4:                    
                    Manage_Users.UpdateCredentials(loggedInUsername);                    
                    break;
                case 5:
                    logout = true;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
     /**
     * Displays the menu options available to lecturers.
     * Allows lecturers to generate reports or update their own credentials.
     * Provides an option to logout.
     */
    private static void displayLecturerMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\nLecturer Menu:");
            System.out.println("Please select an option:");
            System.out.println("1. Generate Lecturer Report");
            System.out.println("2. Update Credentials");
            System.out.println("3. Logout");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    generateReportMenu("lecturer");
                    break;
                case 2:                    
                    Manage_Users.UpdateCredentials(loggedInUsername);                    
                    break;
                case 3:
                    logout = true;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
     /**
     * Displays the menu options for generating reports.
     * Allows users to select the format of the report: TXT file, CSV file, or Console.
     * 
     * @param reportType The type of report to generate.
     */
    private static void generateReportMenu(String reportType) {
        System.out.println("Select the report format:");
        System.out.println("1. TXT file");
        System.out.println("2. CSV file");
        System.out.println("3. Console");

        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                Generate_Reports.selectReport(reportType, "txt");
                break;
            case 2:
                Generate_Reports.selectReport(reportType, "csv");
                break;
            case 3:
                Generate_Reports.selectReport(reportType, "console");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }    
}