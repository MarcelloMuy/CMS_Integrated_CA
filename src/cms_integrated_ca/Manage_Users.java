/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;

/**
 *
 * @author Marce
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Manage_Users {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CMS";
    private static final String DB_USER = "pooa2024";
    private static final String DB_PASSWORD = "pooa2024";

    public static void DisplayMenu() {
        displayMenu();
    }
    
    private static void displayMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nUser Management Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. View Users");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    viewUsers();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static void addUser() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Set auto commit to false
            conn.setAutoCommit(false); 
            String sql = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gather user input for username, password, and role
                System.out.print("Enter username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Enter password: ");
                String password = scanner.nextLine().trim();
                System.out.print("Enter role (admin/office/lecturer): ");
                String role = scanner.nextLine().trim();
                
                // Set parameters and execute the query
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, role);
                pstmt.executeUpdate();
                System.out.println("User added successfully.");
                // saving changes
                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void updateUser() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Set auto commit to false
            conn.setAutoCommit(false);
            String sql = "UPDATE Users SET role = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gather user input for username and new role
                System.out.print("Enter username of the user to update: ");
                String username = scanner.nextLine().trim();
                System.out.print("Enter new role (admin/office/lecturer): ");
                String newRole = scanner.nextLine().trim();
                
                // Set parameters and execute the query
                pstmt.setString(1, newRole);
                pstmt.setString(2, username);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("User updated successfully.");
                    // saving changes
                    conn.commit();
                } else {
                    System.out.println("No user found with the provided username.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            //          Set auto commit to false
            conn.setAutoCommit(false);
            String sql = "DELETE FROM Users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gather user input for username
                System.out.print("Enter username of the user to delete: ");
                String username = scanner.nextLine().trim();
                
                // Set parameters and execute the query
                pstmt.setString(1, username);
                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("User deleted successfully.");
                    // saving changes
                    conn.commit();
                } else {
                    System.out.println("No user found with the provided username.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void viewUsers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Users";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Print user information
                    System.out.println("Users:");
                    System.out.println("Username\tRole");
                    while (rs.next()) {
                        String username = rs.getString("username");
                        String role = rs.getString("role");
                        System.out.println(username + "\t\t" + role);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error viewing users: " + e.getMessage());
        }
    }
}