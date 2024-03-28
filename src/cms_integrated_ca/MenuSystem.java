/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cms_integrated_ca;

import java.util.Scanner;

/**
 *
 * @author Marce
 */
public class MenuSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static String currentUserRole = ""; // To store the role of the current user
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
        
        if (username.equals("admin") && password.equals("java")) {
            currentUserRole = "admin";
            System.out.println(currentUserRole+" login successful!");
            
        } else if (username.equals("office") && password.equals("java")) {
            currentUserRole = "office";
            System.out.println(currentUserRole+" login successful!");
            
        } else if (username.equals("lecturer") && password.equals("java")) {
            currentUserRole = "lecturer";
            System.out.println(currentUserRole+" login successful!");
            
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
    
}
