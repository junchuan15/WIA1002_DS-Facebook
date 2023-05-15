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
public class UserAccess {
    
    public void EditProfile(){
        Scanner scanner = new Scanner(System.in);
            System.out.println("Edit Your Profile:");
            System.out.println("1. Name");
            System.out.println("2. Gender");
            System.out.println("3. Email");
            System.out.println("4. Phone");
            System.out.println("5. Birthday (DD-MM-YYYY)");
            System.out.println("6. Address");
            System.out.println("7. Job");
            System.out.println("8. Hobbies (comma-separated");
            System.out.println("9. Relationship Status");
            System.out.print("Enter your choice: ");
            int choiceEdit = scanner.nextInt();
            scanner.nextLine();

            switch (choiceEdit) {
                        case 1:
                          
                        case 2:
                        
                        case 3:
                        
                        case 4:
                          
                        case 5:
                          
                        case 6:
                          
                            
                        case 7:
                            
                        case 8:
                           
                        case 9:
                           
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
            }
        }
}
    }
}
