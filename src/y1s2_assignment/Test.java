/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();
        String loginUser = "";
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
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        accountManager.UserRegister();
                    case 2:
                        loginUser = accountManager.UserLogin();
                        login = true;
                        break;
                    case 3:
                        exit = true;
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } 
                UserAccess userlogin = new UserAccess(loginUser);
                System.out.println("1. Edit Account");
                System.out.println("2. Display Account");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        userlogin.EditProfile();
                        exit = true;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            
            exit = true;
        }
    }
}
