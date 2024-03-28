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
                    System.out.println("You are logged in");
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
}
