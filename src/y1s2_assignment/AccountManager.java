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

/**
 *
 * @author Asus
 */
public class AccountManager {

    private static Scanner sc = new Scanner(System.in);
    private static UserDatabase database = new UserDatabase();

    public static void UserRegister() {
        System.out.println("==============================================\nSIGN UP now !");
        String Username = null;
        boolean isUniqueUsername = false;
        while (!isUniqueUsername) {
            System.out.print("Username: ");
            String UsernameInput = sc.nextLine();
            String regex_username = "^[a-zA-Z0-9._]*$";
            if (Checkinglength(UsernameInput, 4, 20)) {
                System.out.println("The length of Username must be between 4-20.");
            } else {
                if (database.CheckingExist(UsernameInput)) {
                    System.out.println("The username '" + UsernameInput + "' already exists.");
                } else {
                    if (!Validation(UsernameInput, regex_username)) {
                        System.out.println("Contact Number is invalid. Please try again.");

                    } else {
                        isUniqueUsername = true;
                        Username = UsernameInput;
                    }
                }
            }
        }
        String EmailAddress = null;
        boolean isValidEmail = false;
        while (!isValidEmail) {
            System.out.print("Email Address: ");
            String EmailAddressInput = sc.nextLine();
            String regex_email = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
            if (database.CheckingExist(EmailAddressInput)) {
                System.out.println("The email '" + EmailAddressInput + "' had already been registered.");
            } else {
                if (!Validation(EmailAddressInput, regex_email)) {
                    System.out.println("Email Address is invalid. Please try again.");

                } else {
                    isValidEmail = true;
                    EmailAddress = EmailAddressInput;
                }
            }
        }
        String ContactNumber = null;
        boolean isValidPhoneNumber = false;
        while (!isValidPhoneNumber) {
            System.out.print("Phone Number: ");
            String ContactNumberInput = sc.nextLine();
            if (Checkinglength(ContactNumberInput, 7, 15)) {
                System.out.println("The length of Contact Number must be between 7-15.");
            } else {
                String regex_phone = "^[0-9-]+$";
                if (database.CheckingExist(ContactNumberInput)) {
                    System.out.println("The contact number '" + ContactNumberInput + "' had already been registered.");
                } else {
                    if (!Validation(ContactNumberInput, regex_phone)) {
                        System.out.println("Contact Number is invalid. Please try again.");

                    } else {
                        isValidPhoneNumber = true;
                        ContactNumber = ContactNumberInput;
                    }
                }
            }
        }

        String Password = null;
        boolean isValidPassword = false;
        String regex_pw = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+*=!?.~_\\|{}\\[\\]();:,<>/\\\\])[ -~]{8,}$";
        while (!isValidPassword) {
            System.out.print("Password: ");
            String PasswordInput = sc.nextLine();

            if (!Validation(PasswordInput, regex_pw)) {
                System.out.println("Password Invalid.");
                if (PasswordInput.length() < 8) {
                    System.out.println("Password must be at least 8 characters long.");
                }
                if (!PasswordInput.matches(".*[A-Z].*")) {
                    System.out.println("Password must contain at least one uppercase letter.");
                }
                if (!PasswordInput.matches(".*[a-z].*")) {
                    System.out.println("Password must contain at least one lowercase letter.");
                }
                if (!PasswordInput.matches(".*[0-9].*")) {
                    System.out.println("Password must contain at least one digit.");
                }
                if (!PasswordInput.matches(".*[@#$%^&+*=!?.~_\\|{}\\[\\]();:,<>/\\\\].*")) {
                    System.out.println("Password must contain at least one special character.");
                }
            } else {
                isValidPassword = true;
                Password = PasswordInput;
            }
        }
        System.out.print("Retype Password: ");
        String retypePassword = sc.nextLine();
        while (!retypePassword.equals(Password)) {
            System.out.println("Password does not match! Please try again.");
            System.out.print("Retype Password: ");
            retypePassword = sc.nextLine();
        }
        String hashedPassword = encryptPassword(Password);
        database.registeredUser(Username, EmailAddress, ContactNumber, hashedPassword);
        System.out.print("Register Successfully\n\n");

    }

    public static boolean Checkinglength(String str, int a, int b) {
        if (str.length() < a || str.length() > b) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean Validation(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static String encryptPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            hexString.append("|");
            for (byte b : salt) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error encrypting password.");
            return null;
        }
    }

    public static void UserLogin() {
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
                    String[] userDataArray = userData.split(",");
                    String username = userDataArray[0];
                    String email = userDataArray[1];
                    String contactNumber = userDataArray[2];
                    String password = userDataArray[3];
                    boolean isPasswordCorrect = false;
                    while (!isPasswordCorrect) {
                        System.out.print("Password: ");
                        String loginPw = sc.nextLine();
                        if (database.checkPassword(userData, loginPw)) {
                            isPasswordCorrect = true;
                            System.out.println("Login Successfully!\n");
                            User loggedInUser = new User.Builder(username, email, contactNumber, password).build();
                            System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
                            String loginUserData = database.getUserData(loggedInUser.getEmailAddress());
                            String[] loginUserRow = loginUserData.split(",");
                            if (loginUserRow.length < 5) {
                                UserSetup(loggedInUser.getEmailAddress());
                            } else {
                                continue;
                            }
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
    }

    public static void UserSetup(String loginUser) {
        System.out.println("==============================================\nUSER SETUP");
        String[] userData = database.getUserData(loginUser).split(",");
        String username = userData[0];
        String email = userData[1];
        String contactNumber = userData[2];
        String password = userData[3];

        System.out.println("Please set up your profile.");
        String Name = null;
        boolean isValidName = false;
        while (!isValidName) {
            System.out.print("Name: ");
            String NameInput = sc.nextLine();
            String regex_name = "^^[a-zA-Z]+([ '-/][a-zA-Z]+)*$";
            if (Checkinglength(NameInput, 4, 50)) {
                System.out.println("The length of Name must be between 4-50.");
            } else {
                if (!Validation(NameInput, regex_name)) {
                    System.out.println("Name is invalid. Please try again.");
                } else {
                    isValidName = true;
                    Name = NameInput;
                }

            }
        }
        String birthday = null;
        boolean isValidBirthday = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!isValidBirthday) {
            System.out.print("Birthday (yyyy-MM-dd): ");
            String birthdayInput = sc.nextLine();
            try {
                LocalDate date = LocalDate.parse(birthdayInput, formatter);
                int age = Period.between(date, LocalDate.now()).getYears();
                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();
                boolean isValidDate = true;
                if (day < 1 || day > YearMonth.of(year, month).lengthOfMonth()) {
                    isValidDate = false;
                    System.out.println("Invalid day of the month. Please try again.");
                }

                if (year < 1900 || year > LocalDate.now().getYear()) {
                    isValidDate = false;
                    System.out.println("Invalid year. Please try again.");
                }

                if (isValidDate) {
                    birthday = formatter.format(date);
                    System.out.println("Age: " + age);
                    isValidBirthday = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid birthday format or date. Please try again.");
            }
        }
        String Address = null;
        boolean isValidAddress = false;
        while (!isValidAddress) {
            System.out.print("Address: ");
            String AddressInput = sc.nextLine();
            String regex_address = "^[a-zA-Z0-9\\s,'/.-]+$";
            if (!Validation(AddressInput, regex_address)) {
                System.out.println("Address is invalid. Please try again.");
            } else {
                isValidAddress = true;
                Address = AddressInput;
            }

        }

        String gender = null;
        boolean isValidGender = false;
        while (!isValidGender) {
            System.out.println("Gender: ");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.println("3. Other");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    gender = "Male";
                    isValidGender = true;
                    break;
                case 2:
                    gender = "Female";
                    isValidGender = true;
                    break;
                case 3:
                    gender = "Other";
                    isValidGender = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        String relationshipStatus = null;
        boolean isValidrelationshipStatus = false;
        while (!isValidrelationshipStatus) {
            System.out.println("Relationship Status: ");
            System.out.println("1. Single");
            System.out.println("2. In a relationship");
            System.out.println("3. Married");
            System.out.println("4. It's complicated");
            System.out.println("5. Hide");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    relationshipStatus = "Single";
                    isValidrelationshipStatus = true;
                    break;
                case 2:
                    relationshipStatus = "In a relationship";
                    isValidrelationshipStatus = true;
                    break;
                case 3:
                    relationshipStatus = "Married";
                    isValidrelationshipStatus = true;
                    break;
                case 4:
                    relationshipStatus = "It's Complicated";
                    isValidrelationshipStatus = true;
                    break;
                case 5:
                    relationshipStatus = "Hide";
                    isValidrelationshipStatus = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        sc.nextLine();
        List<String> hobbies = new ArrayList<>();
        boolean isAddingHobby = false;
        while (!isAddingHobby) {
            System.out.print("Enter hobby (type 'done' to finish input): ");
            String hobbyInput = sc.nextLine();
            if (hobbyInput.equalsIgnoreCase("done")) {
                isAddingHobby = true;
                break;
            }
            String regexHobby = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
            if (!Validation(hobbyInput, regexHobby)) {
                System.out.println("Invalid hobby input. Please try again.");
            } else {
                hobbies.add(hobbyInput.trim());
            }
        }
        System.out.println("Hobbies: " + hobbies);

        Stack<String> jobs = new Stack<>();
        boolean isAddingJobs = false;
        while (!isAddingJobs) {
            System.out.print("Current/Previous Job(type 'done' to finish input):");
            String jobInput = sc.nextLine();
            if (jobInput.equalsIgnoreCase("done")) {
                isAddingHobby = true;
                break;
            }
            String regexJob = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
            if (!Validation(jobInput, regexJob)) {
                System.out.println("Invalid job input. Please try again.");
            } else {
                jobs.push(jobInput.trim());
            }
        }
        System.out.println("Job history:");
        for (String job : jobs) {
            System.out.println(job);
        }
        System.out.println("Account Set Up successfully. Start your journey now!");
        User updateUser = new User.Builder(username, email, contactNumber, password)
                .setName(Name)
                .setBirthday(birthday)
                .setAddress(Address)
                .setGender(gender)
                .setRelationshipStatus(relationshipStatus)
                .setHobbies(hobbies)
                .setJobs(jobs)
                .build();
        database.updateUserDetail(updateUser);
    }
}
