/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Asus
 */
public class Validation {

    private DatabaseSQL database = new DatabaseSQL();
    private Encryption encrypt = new Encryption();
    Scanner sc = new Scanner(System.in);

    public String validateUsername() {
        String UsernameInput = "";
        boolean isUniqueUsername = false;
        while (!isUniqueUsername) {
            System.out.print("Username: ");
            UsernameInput = sc.nextLine();
            String regex_username = "^[a-zA-Z0-9._]*$";
            if (Checkinglength(UsernameInput, 4, 20)) {
                System.out.println("The length of Username must be between 4-20.");
            } else {
                if (database.isExist(UsernameInput)) {
                    System.out.println("The username '" + UsernameInput + "' already exists.");
                } else {
                    if (!Validification(UsernameInput, regex_username)) {
                        System.out.println("Username is invalid. Please try again.");

                    } else {
                        isUniqueUsername = true;
                    }
                }
            }
        }
        return UsernameInput;
    }

    public String validateEmail() {
        String EmailAddressInput = "";
        boolean isValidEmail = false;
        while (!isValidEmail) {
            System.out.print("Email Address: ");
            EmailAddressInput = sc.nextLine();
            String regex_email = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
            if (database.isExist(EmailAddressInput)) {
                System.out.println("The email '" + EmailAddressInput + "' had already been registered.");
            } else {
                if (!Validification(EmailAddressInput, regex_email)) {
                    System.out.println("Email Address is invalid. Please try again.");
                } else {
                    isValidEmail = true;
                }
            }
        }
        return EmailAddressInput;
    }

    public String validatePhoneNo() {
        String ContactNumberInput = "";
        boolean isValidPhoneNumber = false;
        while (!isValidPhoneNumber) {
            System.out.print("Phone Number: ");
            ContactNumberInput = sc.nextLine();
            if (Checkinglength(ContactNumberInput, 7, 15)) {
                System.out.println("The length of Contact Number must be between 7-15.");
            } else {
                String regex_phone = "^[0-9-]+$";
                if (database.isExist(ContactNumberInput)) {
                    System.out.println("The contact number '" + ContactNumberInput + "' had already been registered.");
                } else {
                    if (!Validification(ContactNumberInput, regex_phone)) {
                        System.out.println("Contact Number is invalid. Please try again.");

                    } else {
                        isValidPhoneNumber = true;
                    }
                }
            }
        }
        return ContactNumberInput;
    }

    public String validatePassword() throws SQLException {
        String PasswordInput = "";
        boolean isValidPassword = false;
        String regex_pw = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+*=!?.~_\\|{}\\[\\]();:,<>/\\\\])[ -~]{8,}$";
        while (!isValidPassword) {
            System.out.print("Password: ");
            PasswordInput = sc.nextLine();
            if (!Validification(PasswordInput, regex_pw)) {
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
                System.out.print("Retype Password: ");
                String retypePassword = sc.nextLine();
                while (!retypePassword.equals(PasswordInput)) {
                    System.out.println("Password does not match! Please try again.");
                    System.out.print("Retype Password: ");
                    retypePassword = sc.nextLine();
                }
                
            }
        }
        return PasswordInput;
    }

    public boolean isAdmin(User registeredUser) {
        String email_admin = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+\\.tfbAdmin@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        return Validification(registeredUser.getEmailAddress(), email_admin);
    }

    public String validateName() {
        String NameInput = "";
        boolean isValidName = false;
        while (!isValidName) {
            System.out.print("Name: ");
            NameInput = sc.nextLine();
            String regex_name = "^^[a-zA-Z]+([ '-/][a-zA-Z]+)*$";
            if (Checkinglength(NameInput, 4, 50)) {
                System.out.println("The length of Name must be between 4-50.");
            } else {
                if (!Validification(NameInput, regex_name)) {
                    System.out.println("Name is invalid. Please try again.");
                } else {
                    isValidName = true;
                }

            }
        }
        return NameInput;
    }

    public String validateBirthday() {
        String birthdayInput = "";
        boolean isValidBirthday = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!isValidBirthday) {
            System.out.print("Birthday (yyyy-MM-dd): ");
            birthdayInput = sc.nextLine();
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
                    birthdayInput = formatter.format(date);
                    System.out.println("Age: " + age);
                    isValidBirthday = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid birthday format or date. Please try again.");
            }
        }
        return birthdayInput;
    }

    public int calculateAge(String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(birthday, formatter);
        return Period.between(date, LocalDate.now()).getYears();
    }

    public String validateAddress() {
        String AddressInput = "";
        boolean isValidAddress = false;
        while (!isValidAddress) {
            System.out.print("Address: ");
            AddressInput = sc.nextLine();
            String regex_address = "^[a-zA-Z0-9\\s,'/.-]+$";
            if (!Validification(AddressInput, regex_address)) {
                System.out.println("Address is invalid. Please try again.");
            } else {
                isValidAddress = true;
            }
        }
        return AddressInput;
    }

    public String validateGender() {
        String gender = "";
        boolean isValidGender = false;
        while (!isValidGender) {
            System.out.println("Gender: ");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.println("3. Other");
            System.out.print("Select your gender: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    gender = "Male";
                    isValidGender = true;
                    break;
                case "2":
                    gender = "Female";
                    isValidGender = true;
                    break;
                case "3":
                    gender = "Other";
                    isValidGender = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        return gender;
    }

    public String validateRelationshipStatus() {
        String relationshipStatus = "";
        boolean isValidrelationshipStatus = false;
        while (!isValidrelationshipStatus) {
            System.out.println("Relationship Status: ");
            System.out.println("1. Single");
            System.out.println("2. In a relationship");
            System.out.println("3. Married");
            System.out.println("4. It's complicated");
            System.out.println("5. Hide");
            System.out.print("Select your relationship status: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    relationshipStatus = "Single";
                    isValidrelationshipStatus = true;
                    break;
                case "2":
                    relationshipStatus = "In a relationship";
                    isValidrelationshipStatus = true;
                    break;
                case "3":
                    relationshipStatus = "Married";
                    isValidrelationshipStatus = true;
                    break;
                case "4":
                    relationshipStatus = "It's Complicated";
                    isValidrelationshipStatus = true;
                    break;
                case "5":
                    relationshipStatus = "Hide";
                    isValidrelationshipStatus = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        return relationshipStatus;
    }

    public ArrayList<String> validateHobby() {
        ArrayList<String> hobbies = new ArrayList<>();
        boolean isAddingHobby = false;

        while (!isAddingHobby) {
            System.out.print("Enter hobby (type 'done' to finish input): ");
            String hobbyInput = sc.nextLine();
            if (hobbyInput.equalsIgnoreCase("done")) {
                isAddingHobby = true;
                break;
            }
            String regexHobby = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
            if (!Validification(hobbyInput, regexHobby)) {
                System.out.println("Invalid hobby input. Please try again.");
            } else {
                hobbies.add(hobbyInput.trim());
            }
        }
        return hobbies;
    }

    public String validateJobs() {
        String JobInput = "";
        boolean isValidJob = false;
        while (!isValidJob) {
            System.out.print("Job: ");
            JobInput = sc.nextLine();
            String regex_job = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
            if (!Validification(JobInput, regex_job)) {
                System.out.println("Address is invalid. Please try again.");
            } else {
                isValidJob = true;
            }
        }
        return JobInput;

    }

    public boolean Checkinglength(String str, int a, int b) {
        if (str.length() < a || str.length() > b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Validification(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
/*
    public boolean validateCurrentPassword(String currentPasswordInput, String encryptedPassword) {
        return validatePassword(currentPasswordInput, encryptedPassword);
    }

    public String encryptPassword(String password) {
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

    public boolean validatePassword(String inputPassword, String encryptedPassword) {
        String[] parts = encryptedPassword.split("\\|");
        String hashPart = parts[0];
        String saltPart = parts[1];

        try {
            byte[] salt = new byte[saltPart.length() / 2];
            for (int i = 0; i < salt.length; i++) {
                int index = i * 2;
                int value = Integer.parseInt(saltPart.substring(index, index + 2), 16);
                salt[i] = (byte) value;
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(inputPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString().equals(hashPart);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error decrypting password.");
            return false;
        }
    }
*/
}
