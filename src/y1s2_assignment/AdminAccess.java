/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class AdminAccess extends UserAccess {

    private DatabaseSQL database;

    public AdminAccess(User loggedInUser) {
        super(loggedInUser);
        database = new DatabaseSQL();
    }

    public void adminMenu() {
        Scanner sc = new Scanner(System.in);
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            System.out.println("==============================================\nADMIN MENU");
            System.out.println("1. Edit Account");
            System.out.println("2. Display Account");
            System.out.println("3. Search User");
            System.out.println("4. Delete User");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            String choiceStr = sc.nextLine();

            if (choiceStr.matches("\\d+")) {
                int choice = Integer.parseInt(choiceStr);

                switch (choice) {
                    case 1:
                        EditProfile();
                        break;
                    case 2:
                        viewAccount(loggedInUser);
                        break;
                    case 3:
                        Search();
                        break;
                    case 4:
                        deleteUser();
                        break;
                    case 5:
                        System.out.println("Log out successfully. Bye~\n");
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

    public void deleteUser() {
        System.out.println("==============================================\nDELETE USER");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the account ID of the user to delete: ");
        String accountID = sc.nextLine();
        User user = database.getUser("Account_ID", accountID);
        if (user == null) {
            System.out.println("User does not exist.");
            return;
        }
        System.out.println("User Details:");
        System.out.println("Account ID: " + user.getAccountID());
        System.out.println("Name: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email Address: " + user.getEmailAddress());
        System.out.println("Contact Number: " + user.getContactNumber());
        System.out.print("Are you sure you want to delete this user? (Y/N):");
        String confirmation = sc.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            database.deleteUserSQL(user);
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }
}
