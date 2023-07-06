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
    private DatabaseSQL database = new DatabaseSQL();
    private Encryption encrypt = new Encryption();
    Scanner sc = new Scanner(System.in);

    public UserAccess(User loggedInUser) {
        this.loggedInUser = database.getUser("UserName", loggedInUser.getUsername());
    }

    public UserAccess() {
        this.loggedInUser = loggedInUser;
        this.database = new DatabaseSQL();
    }

    public void userMenu() throws SQLException {
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            System.out.println("==============================================");
            System.out.println("                 USER MENU                     ");
            System.out.println("==============================================");
            System.out.println("          1. Display Profile");
            System.out.println("          2. Search User");
            System.out.println("          3. Manage Friends");
            System.out.println("          4. Messenger");
            System.out.println("          5. Post");
            System.out.println("          6. Logout");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");
            String choiceStr = sc.nextLine();

            if (choiceStr.matches("\\d+")) {
                int choice = Integer.parseInt(choiceStr);

                switch (choice) {
                    case 1:
                        displayProfile();
                        break;
                    case 2:
                        Search();
                        break;
                    case 3:
                        Friend();
                        break;
                    case 4:
                        Chat();
                        break;
                    case 5:
                        Post();
                        break;
                    case 6:
                        System.out.println("Logout successful. Goodbye!\n");
                        backToMainMenu = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public void EditProfile() throws SQLException {
        Validation validate = new Validation();
        boolean isDoneEdit = false;

        while (!isDoneEdit) {
            System.out.println("==============================================");
            System.out.println("              Edit Your Profile                ");
            System.out.println("==============================================");
            String[] userDetail = {"Username", "Email Address", "Contact Number", "Reset Password", "Name", "Birthday (DD-MM-YYYY)", "Address", "Gender", "Relationship Status", "Hobbies", "Jobs"};

            for (int i = 0; i < userDetail.length; i++) {
                System.out.println("          " + (i + 1) + ". " + userDetail[i]);
            }
            System.out.println("==============================================");
            System.out.println("            Finish Editing ?");
            System.out.println("          -1. Save Changes");
            System.out.println("          -2. Discard Changes");
            System.out.println("==============================================");
            boolean valid = false;
            while (!valid) {
                System.out.print("Enter what you want to edit: ");
                int choiceEdit = sc.nextInt();
                sc.nextLine();
                System.out.println("==============================================");
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
                        System.out.println("Current Phone Number: " + loggedInUser.getContactNumber());
                        System.out.print("New ");
                        String contactnumber = validate.validatePhoneNo();
                        loggedInUser.setContactNumber(contactnumber);
                        break;
                    case 4:
                        boolean passwordChanged = false;
                        boolean incorrectPassword = false;
                        while (!passwordChanged && !incorrectPassword) {
                            System.out.print("Enter your current password (Press 'b' to go back): ");
                            String currentPasswordInput = sc.nextLine();
                            if (currentPasswordInput.equalsIgnoreCase("b")) {
                                break;
                            }
                            String encryptedPassword = loggedInUser.getPassword();
                            if (!encrypt.checkPassword(loggedInUser.getAccountID(), currentPasswordInput, encryptedPassword)) {
                                System.out.println("Incorrect current password. Password change failed.");
                                incorrectPassword = true;
                            } else {
                                System.out.print("New ");
                                String newPassword = validate.validatePassword();
                                String encryptedPw = encrypt.encryption(loggedInUser.getAccountID(), newPassword);
                                loggedInUser.setPassword(encryptedPw);
                                System.out.println("Password reset successful.");
                                passwordChanged = true;
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Current Name: " + loggedInUser.getName());
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
                        Stack<String> jobs = loggedInUser.getJobs();
                        System.out.println("Current Job: " + jobs.peek());
                        System.out.print("New ");
                        String newJob = validate.validateJobs();
                        jobs.push(newJob);
                        loggedInUser.setJobs(jobs);
                        break;
                    case -1:
                        System.out.println("Saving changes to user profile...\n");
                        database.updateUserDetail(loggedInUser);
                        valid = true;
                        isDoneEdit = true;
                        break;
                    case -2:
                        System.out.println("Discarding changes to user profile...\n");
                        valid = true;
                        isDoneEdit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                if (isDoneEdit) {
                    break;
                }
            }
        }
    }

    public void displayProfile() throws SQLException {
        boolean back = false;
        boolean edit = false;
        while (!back) {
            System.out.println("==============================================");
            System.out.println("           Displaying user profile           ");
            System.out.println("==============================================");
            System.out.println("Account ID:            " + loggedInUser.getAccountID());
            System.out.println("Name:                  " + loggedInUser.getName());
            System.out.println("Username:              " + loggedInUser.getUsername());
            System.out.println("Email Address:         " + loggedInUser.getEmailAddress());
            System.out.println("Contact Number:        " + loggedInUser.getContactNumber());
            System.out.println("Birthday:              " + loggedInUser.getBirthday());
            System.out.println("Age:                   " + loggedInUser.getAge());
            System.out.println("Address:               " + loggedInUser.getAddress());
            System.out.println("Gender:                " + loggedInUser.getGender());
            System.out.println("Relationship Status:   " + loggedInUser.getRelationshipStatus());
            System.out.println("Number of Friends:     " + loggedInUser.getNumberOfFriends());
            System.out.println("Friends:               " + loggedInUser.getFriends());
            System.out.println("Hobbies:               " + loggedInUser.getHobbies());
            Stack<String> jobs = loggedInUser.getJobs();
            String currentJob = jobs.pop();
            System.out.println("Current Job:           " + currentJob);
            System.out.println("Previous Jobs History: " + jobs);
            jobs.push(currentJob);
            System.out.println("==============================================");
            while (!edit) {
                System.out.print("Do you want to edit your account? (Y/N): ");
                String choice = sc.nextLine();

                switch (choice.toUpperCase()) {
                    case "Y":
                        EditProfile();
                        break;
                    case "N":
                        System.out.println("Loading back to Main Menu...");
                        back = true;
                        edit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 'Y' or 'N'.");
                        break;
                }
            }

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

    public void Post() throws SQLException {
        PostManager post = new PostManager(loggedInUser);
        post.PostMenu();
    }
}
