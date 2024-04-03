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
 *
 * @author Marce
 */
public class Menu_System {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CMS";
    private static final String USER = "pooa2024";
    private static final String PASSWORD = "pooa2024";
    
    private static String loggedInUsername; // Variable to store the username of the logged-in user
       
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
    
    private static void displayOfficeMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\nOffice Menu:");
            System.out.println("Please select an option:");
            System.out.println("1. Generate Reports");
            System.out.println("2. Update Credentials");
            System.out.println("3. Logout");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
//                    Generate reports
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
    
    private static void displayLecturerMenu() {
        boolean logout = false;

        while (!logout) {
            System.out.println("\nOffice Menu:");
            System.out.println("Please select an option:");
            System.out.println("1. Generate Report");
            System.out.println("2. Update Credentials");
            System.out.println("3. Logout");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
//                    Generate reports
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
}
