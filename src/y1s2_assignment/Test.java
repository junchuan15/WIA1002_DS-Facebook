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
      
        boolean exit = false;
        while (!exit) {
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
                    break;
                case 2:
                    exit = true;
                    accountManager.UserLogin();
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
       
    }
}

