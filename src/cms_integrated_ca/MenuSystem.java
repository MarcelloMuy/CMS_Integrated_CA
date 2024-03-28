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
public class MenuSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CMS";
    private static final String USER = "pooa2024";
    private static final String PASSWORD = "pooa2024";
    
    
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
                            if (role.equals("admin")) {
                                displayAdminMenu();
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
//                 call method
                    break;
                case 2:
                    // Call method
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
