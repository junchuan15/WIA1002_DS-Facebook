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
        boolean login = false;
        boolean exit = false;

        while (!exit) {
            if (!login) {
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
                                login = true;
                            } else {
                                System.out.println("Invalid username or password. Please try again.");
                            }
                            break;
                        case 3:
                            exit = true;
                            System.out.println("Exiting the program...");
                            break;
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
                } else {
                    UserAccess userAccess = new UserAccess(loggedInUser);
                    boolean backToMainMenu = false;

                    while (!backToMainMenu) {
                        System.out.println("==============================================\nUSER MENU");
                        System.out.println("1. Edit Account");
                        System.out.println("2. Display Account");
                        System.out.println("3. Search User");
                        System.out.println("4. Friend Menu");
                        System.out.println("5. Messenger");
                        System.out.println("6. Posting");
                        System.out.println("7. Logout");
                        System.out.print("Enter your choice: ");
                         String choiceStr = sc.nextLine();

                        if (choiceStr.matches("\\d+")) {
                            int choice = Integer.parseInt(choiceStr);

                            switch (choice) {
                                case 1:
                                    userAccess.EditProfile();
                                    break;
                                case 2:
                                    userAccess.viewAccount(loggedInUser);
                                    break;
                                case 3:
                                    userAccess.Search();
                                    break;
                                case 4:
                                    userAccess.Friend();
                                    break;
                                case 5:
                                    userAccess.Chat();
                                    break;
                                case 6:
                                    userAccess.Post();
                                case 7:
                                    System.out.println("Log out successfully. Bye~\n");
                                    loggedInUser = null;
                                    login = false;
                                    backToMainMenu = true;
                                    break;
                                default:
                                    System.out.println("Invalid choice!");
                                    break;
                            }
                        } else {
                            System.out.println("Invalid input! Please enter a number.");
                        }
                    }
                }
            }
        }
    }
}