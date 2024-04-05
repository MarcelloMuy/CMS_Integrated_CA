/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;

/**
 * This class provides functionality for managing users in the CMS (Content Management System).
 * It includes methods for adding, updating, deleting, and viewing user information.
 * 
 * The class also includes a method for updating user credentials, primarily designed for the admin user.
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
    
    /**
     * Displays the user management menu and handles user inputs.
     */
    public static void DisplayMenu() {
        displayMenu();
    }
    
    /**
     * Updates the credentials (username and password) for a specified user.
     * 
     * @param username The username of the user whose credentials need to be updated.
     */
    public static void UpdateCredentials(String username){
        updateCredentials(username);
    }
    
    /**
     * Displays the user management menu and handles user inputs.
     * The menu options include adding, updating, deleting, and viewing users, as well as exiting the menu.
     */
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
    
    /**
     * Prompts the user to enter an integer input and returns it.
     * Keeps prompting until a valid integer input is provided.
     * 
     * @return The integer input provided by the user.
     */
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
    
    /**
     * Adds a new user to the database.
     * Prompts the user to enter a username, password, and role for the new user.
     * 
     * If successful, the user is added to the database and a success message is displayed.
     * If an error occurs, an error message is displayed.
     */
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
    
    /**
     * Updates the information of an existing user in the database.
     * Prompts the user to enter the username of the user to be updated,
     * as well as the new username, password, and role for the user.
     * 
     * If successful, the user's information is updated in the database and a success message is displayed.
     * If no user is found with the provided username, an appropriate message is displayed.
     * If an error occurs, an error message is displayed.
     */
    private static void updateUser() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Set auto commit to false
            conn.setAutoCommit(false);
            String sql = "UPDATE Users SET username = ?, password = ?, role = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gather user input for username to be updated
                System.out.print("Enter username of the user to update: ");
                String oldUsername = scanner.nextLine().trim();

                // Gather new user details
                System.out.print("Enter new username: ");
                String newUsername = scanner.nextLine().trim();
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine().trim();
                System.out.print("Enter new role (admin/office/lecturer): ");
                String newRole = scanner.nextLine().trim();

                // Set parameters and execute the query
                pstmt.setString(1, newUsername);
                pstmt.setString(2, newPassword);
                pstmt.setString(3, newRole);
                pstmt.setString(4, oldUsername);
                
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
    
    /**
     * Deletes a user from the database based on the provided username.
     * Prompts the user to enter the username of the user to be deleted.
     * 
     * If successful, the user is deleted from the database and a success message is displayed.
     * If no user is found with the provided username, an appropriate message is displayed.
     * If an error occurs, an error message is displayed.
     */
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
    
    /**
     * Displays the list of users currently stored in the database along with their roles.
     * Retrieves user information from the database and prints it to the console.
     * 
     * If an error occurs during the database operation, an error message is displayed.
     */
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
    
        /**
     * Updates the credentials (username and password) for the specified user.
     * Prompts the user to enter a new username and password.
     * 
     * @param username The username of the user whose credentials are to be updated.
     */
    private static void updateCredentials(String username) {
       System.out.println("Update Credentials:");
       System.out.print("Enter new username: ");
       String newUsername = scanner.nextLine().trim();
       System.out.print("Enter new password: ");
       String newPassword = scanner.nextLine().trim();

       // Update admin's credentials in the database
       try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
           String sql = "UPDATE Users SET username = ?, password = ? WHERE username = ?";
           try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setString(1, newUsername);
               pstmt.setString(2, newPassword);
               pstmt.setString(3, username);
               int rowsUpdated = pstmt.executeUpdate();
               if (rowsUpdated > 0) {
                   System.out.println("Credentials updated successfully.");
               } else {
                   System.out.println("Failed to update credentials.");
               }
           }
       } catch (SQLException e) {
           System.out.println("Error updating credentials: " + e.getMessage());
       }
   }
}

