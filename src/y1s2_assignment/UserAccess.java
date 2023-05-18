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
    User loggedInUser;
    DatabaseSQL database;

    public UserAccess(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.database = new DatabaseSQL();
    }

    public void EditProfile() {
        Validation validate = new Validation();
        Scanner sc = new Scanner(System.in);
        boolean isDoneEdit = false;

        while (!isDoneEdit) {
            System.out.println("Edit Your Profile:");
            String[] userDetail = {"Username", "Email Address", "Contact Number", "Password", "Name", "Birthday (DD-MM-YYYY)", "Address", "Gender", "Relationship Status"};
            for (int i = 0; i < userDetail.length; i++) {
                System.out.println((i + 1) + ". " + userDetail[i]);
            }
            System.out.print("Enter what you want to edit (type -1 to finish edit): ");
            int choiceEdit = sc.nextInt();
            sc.nextLine();
            
            switch (choiceEdit) {
                case 1:
                    System.out.println("Current UserName: "+loggedInUser.getUsername());
                    String username = validate.validateUsername();
                    loggedInUser.setUsername(username);
                    database.EditUserDetail(loggedInUser,"UserName");
                    break;
                case 2:
                    System.out.println("Current Email Address: "+loggedInUser.getEmailAddress());
                    String email = validate.validateEmail();
                    loggedInUser.setEmailAddress(email);
                     database.EditUserDetail(loggedInUser,"EmailAdress");
                    break;
                case 3:
                    System.out.println("Current Contact Number: "+loggedInUser.getContactNumber());
                    String contactnumber = validate.validatePhoneNo();
                    loggedInUser.setContactNumber(contactnumber);
                    database.EditUserDetail(loggedInUser,"ContactNumber");
                    break;
                case 4:
                    String password = validate.validatePassword();
                    loggedInUser.setPassword(password);
                    database.EditUserDetail(loggedInUser,"Password");
                    break;
                case 5:
                    String name = validate.validateName();
                    loggedInUser.setName(name);
                    database.EditUserDetail(loggedInUser,"UserName");
                    break;
                case 6:
                    String birthday = validate.validateBirthday();
                    loggedInUser.setBirthday(birthday);
                   database.EditUserDetail(loggedInUser,"UserName");
                    break;
                case 7:
                    String address = validate.validateAddress();
                    loggedInUser.setAddress(address);
                    database.EditUserDetail(loggedInUser,"UserName");
                    break;
                case 8:
                    String gender = validate.validateGender();
                    loggedInUser.setGender(gender);
                    database.EditUserDetail(loggedInUser,"UserName");
                    break;
                case 9:
                    String relationshipStatus = validate.validateRelationshipStatus();
                    loggedInUser.setRelationshipStatus(relationshipStatus);
                    database.EditUserDetail(loggedInUser,"UserName");
                    break;
                case -1:
                    System.out.println("User Profile Updated.");
                    isDoneEdit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
