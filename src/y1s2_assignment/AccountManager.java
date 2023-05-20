/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.*;

/**
 *
 * @author Asus
 */
public class AccountManager {

    private Scanner sc = new Scanner(System.in);
    private Validation validate = new Validation();
    private DatabaseSQL databaseSQL = new DatabaseSQL();

    public void UserRegister() {
        System.out.println("==============================================\nSIGN UP now !");
        String accountID = databaseSQL.generateAccountID();
        String Username = validate.validateUsername();
        String EmailAddress = validate.validateEmail();
        String ContactNumber = validate.validatePhoneNo();
        String Password = validate.validatePassword();
        User registeredUser = new User.Builder(accountID, Username, EmailAddress, ContactNumber, Password).build();
        databaseSQL.registerUser(registeredUser);
        System.out.print("Register Successfully\n\n");
    }

    public User userLogin() {
        System.out.println("==============================================\nLOGIN now! ");
        boolean isLoggedIn = false;
        String loginId;
        while (!isLoggedIn) {
            System.out.print("Email Address or Phone Number: ");
            loginId = sc.nextLine();
            User loggedInUser = databaseSQL.getUserLogin(loginId);
            if (loggedInUser != null) {
                System.out.print("Password: ");
                String loginPw = sc.nextLine();
                if (validate.validatePassword(loginPw, loggedInUser.getPassword())) {
                    System.out.println("Login Successfully!\n");
                    System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
                    if (loggedInUser.getName() == null) {
                        userSetup(loggedInUser);
                    }
                    return loggedInUser;
                } else {
                    System.out.println("Wrong Password. Please try again.");
                }
            } else {
                System.out.println("Invalid Email Address/Phone Number. Please try again.");
            }
        }
        return null;
    }

    public void userSetup(User loggedInUser) {
        System.out.println("==============================================\nUSER SETUP");
        String accountID = loggedInUser.getAccountID();
        String username = loggedInUser.getUsername();
        String email = loggedInUser.getEmailAddress();
        String contactNumber = loggedInUser.getContactNumber();
        String password = loggedInUser.getPassword();

        System.out.println("Please set up your profile.");
        String name = validate.validateName();
        String birthday = validate.validateBirthday();
        int age = validate.calculateAge(birthday);
        String address = validate.validateAddress();
        String gender = validate.validateGender();
        String relationshipStatus = validate.validateRelationshipStatus();
        int numberOfFriends = 0;
        sc.nextLine();
        ArrayList<String> hobbies = validate.validateHobby();
        System.out.println("Hobbies: " + hobbies);
        String job = validate.validateJobs();
        Stack<String> jobs = new Stack<>();
        jobs.push(job);
        System.out.println("Account Set Up successfully. Start your journey now!");

        User updateUser = new User.Builder(accountID, username, email, contactNumber, password)
                .setName(name)
                .setBirthday(birthday)
                .setAge(age)
                .setAddress(address)
                .setGender(gender)
                .setRelationshipStatus(relationshipStatus)
                .setNumberOfFriends(numberOfFriends)
                .setHobbies(hobbies)
                .setJobs(jobs)
                .build();
        
        databaseSQL.updateUserDetail(updateUser);
    }
}
