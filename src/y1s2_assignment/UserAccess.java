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

    String userName;
    UserDatabase database = new UserDatabase();

    public UserAccess(String userName) {
        this.userName = userName;
    }

    public void EditProfile() {
        Validation validate = new Validation();
        Scanner sc = new Scanner(System.in);
        boolean isDoneEdit = false;
        while (!isDoneEdit){
        System.out.println("Edit Your Profile:");
        String[] userDetail = {"Username", "Email Address", "Contact Number", "Password", "Name", "Birthday (DD-MM-YYYY)", "Address", "Gender", "Relationship Status", "Hobbies", "Jobs"};
        for (int i = 0; i < userDetail.length; i++) {
            System.out.println((i + 1) + ". " + userDetail[i]);
        }
        System.out.print("Enter what you want to edit (type -1 to finish edit): ");
        int choiceEdit = sc.nextInt();
        sc.nextLine();
        switch (choiceEdit) {
            case 1:
                String username = validate.validateUsername();
                User userEditUserName = database.getUser(userName);
                userEditUserName.setUsername(username);
                database.updateUserDetail(userEditUserName);
                break;
            case 2:
                String email = validate.validateEmail();
                User userEditEmail = database.getUser(userName);
                userEditEmail.setEmailAddress(email);
                database.updateUserDetail(userEditEmail);
                break;
            case 3:
                String contactnumber = validate.validatePhoneNo();
                User userEditContactNo = database.getUser(userName);
                userEditContactNo.setContactNumber(contactnumber);
                database.updateUserDetail(userEditContactNo);
                break;
            case 4:
                String password = validate.validatePassword();
                User userEditPassword = database.getUser(userName);
                userEditPassword.setPassword(password);
                database.updateUserDetail(userEditPassword);
                break;
            case 5:
                String name = validate.validateName();
                User userEditName = database.getUser(userName);
                userEditName.setName(name);
                database.updateUserDetail(userEditName);
                break;
            case 6:
                String birthday = validate.validateBirthday();
                User userEditBirthday = database.getUser(userName);
                userEditBirthday.setUsername(birthday);
                database.updateUserDetail(userEditBirthday);
                break;
            case 7:
                String address = validate.validateAddress();
                User userEditAddress = database.getUser(userName);
                userEditAddress.setAddress(address);
                database.updateUserDetail(userEditAddress);
                break;
            case 8:
                String gender = validate.validateGender();
                User userEditGender = database.getUser(userName);
                userEditGender.setGender(gender);
                database.updateUserDetail(userEditGender);
                break;
            case 9:
                String relationshipStatus = validate.validateRelationshipStatus();
                User userEditRS = database.getUser(userName);
                userEditRS.setRelationshipStatus(relationshipStatus);
                database.updateUserDetail(userEditRS);
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
