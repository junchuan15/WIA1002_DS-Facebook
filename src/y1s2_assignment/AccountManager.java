/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.security.auth.login.AccountExpiredException;

/**
 *
 * @author Asus
 */
public class AccountManager {

    private Scanner sc = new Scanner(System.in);
    private UserDatabase database = new UserDatabase();
    private Validation validate=new Validation();

    public void UserRegister() {
        System.out.println("==============================================\nSIGN UP now !");
        int ID = 1;
        String accountID = validate.accountIDgenerator();
        String Username = validate.validateUsername();
        String EmailAddress = validate.validateEmail();
        String ContactNumber = validate.validatePhoneNo();
        String Password = validate.validatePassword() ;
        database.registeredUser(accountID, Username, EmailAddress, ContactNumber, Password);
        System.out.print("Register Successfully\n\n");
    }

    public String UserLogin() {
        System.out.println("==============================================\nLOGIN now! ");
        boolean isLoggedIn = false;
        String loginId;
        while (!isLoggedIn) {
            System.out.print("Email Address or Phone Number: ");
            loginId = sc.nextLine();
            if (database.checkUserLogin(loginId)) {
                isLoggedIn = true;
                String userData = database.getUserData(loginId);
                if (userData != null) {
                    String[] userDataArray = userData.split("'");
                    String accountID = userDataArray[0];
                    String username = userDataArray[1];
                    String email = userDataArray[2];
                    String contactNumber = userDataArray[3];
                    String password = userDataArray[4];
                    boolean isPasswordCorrect = false;
                    while (!isPasswordCorrect) {
                        System.out.print("Password: ");
                        String loginPw = sc.nextLine();
                        if (database.checkPassword(userData, loginPw)) {
                            isPasswordCorrect = true;
                            System.out.println("Login Successfully!\n");
                            User loggedInUser = new User.Builder(accountID,username, email, contactNumber, password).build();
                            System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
                            String loginUserData = database.getUserData(loggedInUser.getEmailAddress());
                            String[] loginUserRow = loginUserData.split(",");
                            if (loginUserRow.length < 6) {
                                UserSetup(loggedInUser.getEmailAddress());
                            } 
                            return loggedInUser.getUsername();
                        } else {
                            System.out.println("Wrong Password. Please try again.");
                        }
                    }
                } else {
                    System.out.println("Invalid Email Address/Phone Number. Please try again.");
                }
            } else {
                System.out.println("Invalid Email Address/Phone Number. Please try again.");
            }
        }
        return null;
    }

    public void UserSetup(String loginUser) {
        System.out.println("==============================================\nUSER SETUP");
        String[] userData = database.getUserData(loginUser).split(",");
        String username = userData[0];
        String email = userData[1];
        String contactNumber = userData[2];
        String password = userData[3];

        System.out.println("Please set up your profile.");
        String Name = validate.validateName();
        String Birthday = validate.validateBirthday();
        String Address = validate.validateAddress();
        String gender = validate.validateGender();
        String relationshipStatus = validate.validateRelationshipStatus();
        List<String> hobbies = validate.validateHobby();
        System.out.println("Hobbies: " + hobbies);
        Stack<String> jobs = validate.validateJobs();
        System.out.println("Job history:");
        for (String job : jobs) {
            System.out.println(job);
        }
        System.out.println("Account Set Up successfully. Start your journey now!");
        User updateUser = new User.Builder(username, email, contactNumber, password)
                .setName(Name)
                .setBirthday(Birthday)
                .setAddress(Address)
                .setGender(gender)
                .setRelationshipStatus(relationshipStatus)
                .setHobbies(hobbies)
                .setJobs(jobs)
                .build();
        database.updateUserDetail(updateUser);
    }
}
