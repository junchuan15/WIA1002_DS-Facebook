/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.SQLException;
import java.util.*;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class UserAccess {

    protected User loggedInUser;
    private DatabaseSQL database;

    public UserAccess(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.database = new DatabaseSQL();
    }

    public UserAccess() {
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
                    break;
                case 2:
                    System.out.println("Current Email Address: " + loggedInUser.getEmailAddress());
                    System.out.print("New ");
                    String email = validate.validateEmail();
                    loggedInUser.setEmailAddress(email);
                    break;
                case 3:
                    System.out.println("Current Contact Number: " + loggedInUser.getContactNumber());
                    System.out.print("New ");
                    String contactnumber = validate.validatePhoneNo();
                    loggedInUser.setContactNumber(contactnumber);
                    break;
                case 4:
                    System.out.println("Current Password: " + loggedInUser.getContactNumber());
                    System.out.print("New ");
                    String password = validate.validatePassword();
                    loggedInUser.setPassword(password);
                    break;
                case 5:
                    System.out.println("Current Name: " + loggedInUser.getContactNumber());
                    System.out.print("New ");
                    String name = validate.validateName();
                    loggedInUser.setName(name);
                    break;
                case 6:
                    System.out.println("Current Birthday: " + loggedInUser.getBirthday());
                    System.out.print("New ");
                    String birthday = validate.validateBirthday();
                    loggedInUser.setBirthday(birthday);
                    loggedInUser.setAge(validate.calculateAge(birthday));
                    break;
                case 7:
                    System.out.println("Current Address: " + loggedInUser.getAddress());
                    System.out.print("New ");
                    String address = validate.validateAddress();
                    loggedInUser.setAddress(address);
                    break;
                case 8:
                    System.out.println("Current Gender: " + loggedInUser.getGender());
                    System.out.print("New ");
                    String gender = validate.validateGender();
                    loggedInUser.setGender(gender);
                    break;
                case 9:
                    System.out.println("Current Relationship Status: " + loggedInUser.getRelationshipStatus());
                    System.out.print("New ");
                    String relationshipStatus = validate.validateRelationshipStatus();
                    loggedInUser.setRelationshipStatus(relationshipStatus);
                    break;
                case 10:
                    ArrayList< String> hobbies = loggedInUser.getHobbies();
                    System.out.println("Current Hobbies: " + hobbies);
                    System.out.println("1. Add Hobby");
                    System.out.println("2. Remove Hobby");
                    System.out.print("Enter your choice: ");
                    int hobbyChoice = sc.nextInt();
                    sc.nextLine();
                    switch (hobbyChoice) {
                        case 1:
                            ArrayList< String> addHobby = new ArrayList<>();
                            addHobby = validate.validateHobby();
                            hobbies.addAll(addHobby);
                            loggedInUser.setHobbies(hobbies);
                            break;

                        case 2:
                            if (hobbies.isEmpty()) {
                                System.out.println("No hobbies to remove.");
                            } else {
                                System.out.println("List of Hobby:");
                                for (int i = 0; i < hobbies.size(); i++) {
                                    System.out.println(i + 1 + ". " + hobbies.get(i));
                                }
                                System.out.print("Enter the index of the hobby to remove (1-" + (hobbies.size()) + "): ");
                                int removeIndex = sc.nextInt() - 1;
                                sc.nextLine();

                                if (removeIndex >= 0 && removeIndex < hobbies.size()) {
                                    String hobbyToRemove = hobbies.get(removeIndex);
                                    hobbies.remove(removeIndex);
                                    loggedInUser.setHobbies(hobbies);
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
                    Stack< String> jobs = loggedInUser.getJobs();
                    System.out.println("Current Job:" + jobs.peek());
                    System.out.print("New ");
                    String newJob = validate.validateJobs();
                    jobs.push(newJob);
                    loggedInUser.setJobs(jobs);
                    break;
                case -1:
                    System.out.println("User Profile Updated.\n");
                    database.updateUserDetail(loggedInUser);
                    isDoneEdit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void viewAccount(User loggedInUser) {
        User user = database.getUser("Account_ID", loggedInUser.getAccountID());
        if (user != null) {
            System.out.println("==============================================\nDisplaying user profile:");
            System.out.println("Account ID: " + user.getAccountID());
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
            Stack< String> jobs = user.getJobs();
            String currentJob = jobs.pop();
            System.out.println("Current Job: " + currentJob);
            System.out.println("Previous Jobs History: " + jobs);
            System.out.println("");
            jobs.push(currentJob);
        } else {
            System.out.println("User not found.");
        }
    }

    public void Search() throws SQLException {
        SearchEngine search = new SearchEngine(loggedInUser);
        search.searchUsers();
    }

    public void Friend() throws SQLException {
        Friend friend = new Friend(loggedInUser);
        friend.friendMenu();
    }

    public void Chat() {
        Chat chat = new Chat(loggedInUser);
        chat.chatManager();
    }
    
    public void Post() throws SQLException{
        PostManager post = new PostManager(loggedInUser);
        post.PostMenu();
    }
}