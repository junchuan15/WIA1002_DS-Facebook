/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.SQLException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();
        Validation validate = new Validation();
        User loggedInUser = null;

        while (true) {
            if (loggedInUser == null) {
                System.out.println("Welcome to TheFacebook!");
                System.out.println("==============================================\nMAIN MENU");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                String choiceStr = sc.nextLine();

                if (choiceStr.matches("\\d+")) {
                    int choice = Integer.parseInt(choiceStr);

                    switch (choice) {
                        case 1:
                            accountManager.UserRegister();
                            break;
                        case 2:
                            loggedInUser = accountManager.userLogin();
                            if (loggedInUser != null) {
                                System.out.println("Login successful!");
                            } else {
                                System.out.println("Invalid username or password. Please try again.");
                            }
                            break;
                        case 3:
                            System.out.println("Exiting the program...");
                            return; // End the program
                        default:
                            System.out.println("Invalid choice!");
                            break;
                    }
                } else {
                    System.out.println("Invalid input! Please enter a number.");
                }
            } else {
                if (validate.isAdmin(loggedInUser)) {
                    AdminAccess adminAccess = new AdminAccess(loggedInUser);
                    adminAccess.adminMenu();
                    loggedInUser = null;
                    continue; 
                } else {
                    UserAccess userAccess = new UserAccess(loggedInUser);
                    userAccess.userMenu();
                    loggedInUser = null;
                    continue; 
                }
            }
        }
    }
}
