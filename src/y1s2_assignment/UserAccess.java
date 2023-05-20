/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.*;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class UserAccess {

    private User loggedInUser;
    private DatabaseSQL database;

    public UserAccess(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.database = new DatabaseSQL();
    }

    public void EditProfile() {
        Validation validate = new Validation();
        Scanner sc = new Scanner(System.in);
        boolean isDoneEdit = false;

        while (!isDoneEdit) {
            System.out.println("==============================================\nEdit Your Profile:");
            String[] userDetail = {"Username", "Email Address", "Contact Number", "Password", "Name", "Birthday (DD-MM-YYYY)", "Address", "Gender", "Relationship Status", "Hobbies", "Jobs"};
            for (int i = 0; i < userDetail.length; i++) {
                System.out.println((i + 1) + ". " + userDetail[i]);
            }
            System.out.print("Enter what you want to edit (type -1 to finish edit): ");
            int choiceEdit = sc.nextInt();
            sc.nextLine();

            switch (choiceEdit) {
                case 1:
                    System.out.println("Current UserName: " + loggedInUser.getUsername());
                    System.out.print("New ");
                    String username = validate.validateUsername();
                    loggedInUser.setUsername(username);
                    database.EditUserDetail(loggedInUser, "UserName");
                    break;
                case 2:
                    System.out.println("Current Email Address: " + loggedInUser.getEmailAddress());
                    System.out.print("New ");
                    String email = validate.validateEmail();
                    loggedInUser.setEmailAddress(email);
                    database.EditUserDetail(loggedInUser, "EmailAddress");
                    break;
                case 3:
                    System.out.println("Current Contact Number: " + loggedInUser.getContactNumber());
                    System.out.print("New ");
                    String contactnumber = validate.validatePhoneNo();
                    loggedInUser.setContactNumber(contactnumber);
                    database.EditUserDetail(loggedInUser, "ContactNumber");
                    break;
                case 4:
                    System.out.print("New ");
                    String password = validate.validatePassword();
                    loggedInUser.setPassword(password);
                    database.EditUserDetail(loggedInUser, "Password");
                    break;
                case 5:
                    System.out.print("New ");
                    String name = validate.validateName();
                    loggedInUser.setName(name);
                    database.EditUserDetail(loggedInUser, "Name");
                    break;
                case 6:
                    System.out.println("Current Birthday: " + loggedInUser.getBirthday());
                    System.out.print("New ");
                    String birthday = validate.validateBirthday();
                    loggedInUser.setBirthday(birthday);
                    loggedInUser.setAge(validate.calculateAge(birthday));
                    database.EditUserDetail(loggedInUser, "Birthday");
                    database.EditUserDetail(loggedInUser, "Age");
                    break;
                case 7:
                    System.out.println("Current Address: " + loggedInUser.getAddress());
                    System.out.print("New ");
                    String address = validate.validateAddress();
                    loggedInUser.setAddress(address);
                    database.EditUserDetail(loggedInUser, "Address");
                    break;
                case 8:
                    System.out.println("Current Gender: " + loggedInUser.getGender());
                    System.out.print("New ");
                    String gender = validate.validateGender();
                    loggedInUser.setGender(gender);
                    database.EditUserDetail(loggedInUser, "Gender");
                    break;
                case 9:
                    System.out.println("Current Relationship Status: " + loggedInUser.getRelationshipStatus());
                    System.out.print("New ");
                    String relationshipStatus = validate.validateRelationshipStatus();
                    loggedInUser.setRelationshipStatus(relationshipStatus);
                    database.EditUserDetail(loggedInUser, "Relationship_Status");
                    break;
                case 10:
                    ArrayList<String> hobbies = loggedInUser.getHobbies();
                    System.out.println("Current Hobbies: " + hobbies);
                    System.out.println("1. Add Hobby");
                    System.out.println("2. Remove Hobby");
                    System.out.print("Enter your choice: ");
                    int hobbyChoice = sc.nextInt();
                    sc.nextLine();
                    switch (hobbyChoice) {
                        case 1:
                            ArrayList<String> addHobby = new ArrayList<>();
                            addHobby = validate.validateHobby();
                            hobbies.addAll(addHobby);
                            loggedInUser.setHobbies(hobbies);
                            database.EditUserDetail(loggedInUser, "Hobbies");
                            break;

                        case 2:
                            if (hobbies.isEmpty()) {
                                System.out.println("No hobbies to remove.");
                            } else {
                                System.out.println("List of Hobby:");
                                for (int i = 0; i < hobbies.size(); i++) {
                                    System.out.println(i+1+". "+hobbies.get(i));
                                }
                                System.out.print("Enter the index of the hobby to remove (1-" + (hobbies.size()) + "): ");
                                int removeIndex = sc.nextInt()-1;
                                sc.nextLine();

                                if (removeIndex >= 0 && removeIndex < hobbies.size()) {
                                    String hobbyToRemove = hobbies.get(removeIndex);
                                    hobbies.remove(removeIndex);
                                    loggedInUser.setHobbies(hobbies);
                                    database.EditUserDetail(loggedInUser, "Hobbies");
                                    System.out.println("Hobby '" + hobbyToRemove + "' removed.");
                                } else {
                                    System.out.println("Invalid index!");
                                }
                            }
                            break;
                        default:
                            System.out.println("Invalid choice!");
                            break;
                    }
                    break;
                case 11:
                    Stack<String> jobs = loggedInUser.getJobs();
                    System.out.println("Current Job:" + jobs.peek());
                    System.out.print("New ");
                    String newJob = validate.validateJobs();
                    jobs.push(newJob);
                    loggedInUser.setJobs(jobs);
                    database.EditUserDetail(loggedInUser, "Jobs");
                    break;
                case -1:
                    System.out.println("User Profile Updated.\n");
                    isDoneEdit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void viewAccount(User selectedUser) {
        User user = database.getUser(selectedUser.getAccountID());
        System.out.println("==============================================\nDisplaying user profile:");
        System.out.println("Name: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email Address: " + user.getEmailAddress());
        System.out.println("Contact Number: " + user.getContactNumber());
        System.out.println("Birthday: " + user.getBirthday());
        System.out.println("Age: " + user.getAge());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Gender: " + user.getGender());
        System.out.println("Relationship Status: " + user.getRelationshipStatus());
        System.out.println("Number of Friends: " + user.getNumberOfFriends());
        System.out.println("Hobbies: " + user.getHobbies());
        Stack<String> jobs = user.getJobs();
        String currentJob = jobs.pop();
        System.out.println("Current Job: " + currentJob);
        System.out.println("Previous Jobs History: " + jobs);
        System.out.println("");
        jobs.push(currentJob);
    }
}
