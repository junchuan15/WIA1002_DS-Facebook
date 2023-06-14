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
        String Username = validate.validateUsername();
        String EmailAddress = validate.validateEmail();
        String ContactNumber = validate.validatePhoneNo();
        String Password = validate.validatePassword();
        User registeredUser = new User.Builder(Username, EmailAddress, ContactNumber, Password).build();
        if (validate.isAdmin(registeredUser)) {
            registeredUser.setRole("Admin");
            registeredUser.setAccountID(databaseSQL.generateAccountID(registeredUser.getRole()));

        } else {
            registeredUser.setRole("User");
            registeredUser.setAccountID(databaseSQL.generateAccountID(registeredUser.getRole()));
        }

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
                    if (validate.isAdmin(loggedInUser)) {
                        System.out.println("You are logged in as an admin.");
                        int attempts = 0;
                        boolean isSecretCode = false;
                        while (attempts < 3 && !isSecretCode) {
                            System.out.print("Enter the secret code: ");
                            String secretcode = sc.nextLine();

                            if (secretcode.equals("Ilovethefacebook<3")) {
                                isSecretCode = true;
                                System.out.println("Secret code authentication successful!");

                                if (loggedInUser.getName() == null) {
                                    userSetup(loggedInUser);
                                }
                                System.out.println("Login Successfully!\n");
                                System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
                                return loggedInUser;

                            } else {
                                attempts++;
                                System.out.println("Invalid key password. Attempts left: " + (3 - attempts));
                            }
                        }

                        System.out.println("Key password authentication failed. Returning to login screen.");
                    } else {
                        if (loggedInUser.getName() == null) {
                            userSetup(loggedInUser);
                        }
                        System.out.println("Login Successfully!\n");
                        System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
                        System.out.println("You are logged in as a regular user.");
                        return loggedInUser;
                    }
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

        System.out.println("Please set up your profile.");
        String name = validate.validateName();
        String birthday = validate.validateBirthday();
        int age = validate.calculateAge(birthday);
        String address = validate.validateAddress();
        String gender = validate.validateGender();
        String relationshipStatus = validate.validateRelationshipStatus();
        ArrayList<String> hobbies = validate.validateHobby();
        String job = validate.validateJobs();
        Stack<String> jobs = new Stack<>();
        jobs.push(job);
        System.out.println("Account Set Up successfully. Start your journey now!");

        User updateUser = new User.Builder(loggedInUser.getUsername(), loggedInUser.getEmailAddress(),
                loggedInUser.getContactNumber(), loggedInUser.getPassword())
                .setAccountID(loggedInUser.getAccountID())
                .setRole(loggedInUser.getRole())
                .setName(name)
                .setBirthday(birthday)
                .setAge(age)
                .setAddress(address)
                .setGender(gender)
                .setRelationshipStatus(relationshipStatus)
                .setNumberOfFriends(0)
                .setHobbies(hobbies)
                .setJobs(jobs)
                .setFriends(new ArrayList<>()) 
                .setSentRequests(new ArrayList<>()) 
                .setReceivedRequests(new ArrayList<>())
                .build();

        databaseSQL.updateUserDetail(updateUser);
    }
}