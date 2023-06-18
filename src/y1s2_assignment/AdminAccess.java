/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class AdminAccess extends UserAccess {

    private DatabaseSQL database = new DatabaseSQL();
    private ArrayList<User> users;
    private ArrayList<Post> posts;
    private Post post;
    private PostManager pm = new PostManager();
    Scanner sc = new Scanner(System.in);

    public AdminAccess(User loggedInUser) {
        super(loggedInUser);
        users = database.loadUsers();
        posts = database.getAllPosts();

    }

    public void adminMenu() throws SQLException {
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            System.out.println("==============================================");
            System.out.println("                 ADMIN MENU                    ");
            System.out.println("==============================================");
            System.out.println("             1. Admin Control");
            System.out.println("             2. Display Profile");
            System.out.println("             3. Search User");
            System.out.println("             4. Manage Friends");
            System.out.println("             5. Messenger");
            System.out.println("             6. Post");
            System.out.println("             7. Logout");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");
            String choiceStr = sc.nextLine();

            if (choiceStr.matches("\\d+")) {
                int choice = Integer.parseInt(choiceStr);

                switch (choice) {
                    case 1:
                        adminControl();
                        break;
                    case 2:
                        displayProfile();
                        break;
                    case 3:
                        Search();
                        break;
                    case 4:
                        Friend();
                        break;
                    case 5:
                        Chat();
                        break;
                    case 6:
                        Post();
                        break;
                    case 7:
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

    public void adminControl() throws SQLException {
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            System.out.println("==============================================");
            System.out.println("               ADMIN CONTROL                   ");
            System.out.println("==============================================");
            System.out.println("          1. Delete User");
            System.out.println("          2. Delete Post");
            System.out.println("          3. Ban User");
            System.out.println("          4. Unban User");
            System.out.println("          5. Add Bad Words");
            System.out.println("          6. View User Reports");
            System.out.println("          7. Back to Admin Menu");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");
            String choiceStr = sc.nextLine();

            if (choiceStr.matches("\\d+")) {
                int choice = Integer.parseInt(choiceStr);

                switch (choice) {
                    case 1:
                        deleteUser();
                        break;
                    case 2:
                        deletePost();
                        break;
                    case 3:
                        banUser();
                        break;
                    case 4:
                        unbanUser();
                        break;
                    case 5:
                        addBadWords();
                        break;
                    case 6:
                        viewUserReports();
                        break;
                    case 7:
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

    public void deleteUser() {
        System.out.println("==============================================\nDELETE USER");

        if (users.isEmpty() || users.size() == 1) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Users:\n------------------------");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
           
                System.out.println("Index: " + i);
                System.out.println("Account ID: " + user.getAccountID());
                System.out.println("Username: " + user.getUsername());
                System.out.println("------------------------");
            
        }

        System.out.print("Enter the index of the user to delete [-1 to back]: ");
        if (sc.hasNextInt()) {
            int index = sc.nextInt();
            sc.nextLine();

            if (index == -1) {
                return;
            }

            if (index >= 0 && index < users.size()) {
                User user = users.get(index);
                System.out.println("==============================================");
                System.out.println("User Details:\n------------------------");
                System.out.println("Account ID: " + user.getAccountID());
                System.out.println("Name: " + user.getName());
                System.out.println("Username: " + user.getUsername());
                System.out.println("Email Address: " + user.getEmailAddress());
                System.out.println("Contact Number: " + user.getContactNumber());
                System.out.println("------------------------");
                System.out.print("Are you sure you want to delete this user? (Y/N): ");
                String confirmation = sc.nextLine();

                if (confirmation.equalsIgnoreCase("Y")) {
                    database.deleteUserSQL(user);
                    users.remove(index);
                    System.out.println("User deleted successfully.");
                } else {
                    System.out.println("Deletion canceled.");
                }
            } else {
                System.out.println("Invalid index!");
            }
        } else {
            System.out.println("Invalid input! Please enter an integer index.");
        }
    }

    public void deletePost() {
        System.out.println("==============================================\nDELETE POST");

        if (posts.isEmpty()) {
            System.out.println("No posts found.");
            return;
        }

        System.out.println("Posts:\n------------------------");
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            System.out.println("Index: " + i);
            System.out.println("Post ID: " + post.getPostID());
            System.out.println("Content: " + post.getContent());
            System.out.println("------------------------");
        }

        System.out.print("Enter the index of the post to delete [-1 to back]: ");
        if (sc.hasNextInt()) {
            int index = sc.nextInt();
            sc.nextLine();

            if (index == -1) {
                return;
            }

            if (index >= 0 && index < posts.size()) {
                Post post = posts.get(index);
                System.out.println("Post Details:");
                System.out.println("Post ID: " + post.getPostID());
                System.out.println("Content: " + post.getContent());

                if (post.getMediaPath() != null && !post.getMediaPath().isEmpty()) {
                    System.out.println("Media: " + post.getMediaPath());
                    pm.viewMedia(post.getMediaPath());
                }
                System.out.println("------------------------");
                System.out.print("Are you sure you want to delete this post? (Y/N): ");
                String confirmation = sc.nextLine();

                if (confirmation.equalsIgnoreCase("Y")) {
                    database.deletePost(post);
                    posts.remove(index);
                    System.out.println("Post deleted successfully.");
                } else {
                    System.out.println("Deletion canceled.");
                }
            } else {
                System.out.println("Invalid index!");
            }
        } else {
            System.out.println("Invalid input! Please enter an integer index.");
        }
    }

    public void banUser() {
        System.out.println("==============================================\nBAN USER");

        if (users.isEmpty() || users.size() == 1) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Users:\n------------------------");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
       
            System.out.println("Index: " + i);
            System.out.println("Account ID: " + user.getAccountID());
            System.out.println("Name: " + user.getName());
            System.out.println("Username: " + user.getUsername());
            System.out.println("------------------------");
        }

        System.out.print("Enter the index of the user to ban [-1 to back]: ");
        if (sc.hasNextInt()) {
            int index = sc.nextInt();
            sc.nextLine();

            if (index == -1) {
                return;
            }

            if (index >= 0 && index < users.size()) {
                User user = users.get(index);
               
                System.out.println("User Details:\n------------------------");
                System.out.println("Account ID: " + user.getAccountID());
                System.out.println("Name: " + user.getName());
                System.out.println("Username: " + user.getUsername());
                System.out.println("------------------------");
                System.out.print("Enter the duration of the ban (in hours): ");
                if (sc.hasNextInt()) {
                    int durationHours = sc.nextInt();
                    sc.nextLine();
                    LocalDateTime banEndTime = LocalDateTime.now().plusHours(durationHours);
                    user.setBanEndTime(banEndTime);
                    user.setBanned(true);
                    System.out.println("User " + user.getUsername() + " banned successfully for " + durationHours + " hours.");
                } else {
                    System.out.println("Invalid input! Please enter an integer duration.");
                }
            } else {
                System.out.println("Invalid index!");
            }
        } else {
            System.out.println("Invalid input! Please enter an integer index.");
        }
    }

    public void unbanUser() {
        System.out.println("==============================================\nUNBAN USER");
        System.out.println("Banned Users:\n------------------------");
        List<User> bannedUsers = getBannedUsers();
        if (bannedUsers.isEmpty()) {
            System.out.println("No users are currently banned.");
            return;
        }

        for (int i = 0; i < bannedUsers.size(); i++) {
            User user = bannedUsers.get(i);
            System.out.println("Index: " + i);
            System.out.println("Account ID: " + user.getAccountID());
            System.out.println("Name: " + user.getName());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Ban End Time: " + user.getBanEndTime());
            System.out.println("------------------------");
        }

        System.out.print("Enter the index of the user to unban [-1 to back]: ");
        if (sc.hasNextInt()) {
            int index = sc.nextInt();
            sc.nextLine();
            if (index == -1) {
                return;
            }

            if (index >= 0 && index < bannedUsers.size()) {
                User user = bannedUsers.get(index);
                user.setBanned(false);
                user.setBanEndTime(null);
                System.out.println("User unbanned successfully.");
            } else {
                System.out.println("Invalid index!");
            }
        } else {
            System.out.println("Invalid input! Please enter an integer index.");
        }
    }

    private List<User> getBannedUsers() {
        List<User> bannedUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isBanned()) {
                bannedUsers.add(user);
            }
        }
        return bannedUsers;
    }

    public void addBadWords() {
        System.out.println("==============================================\nADD BAD WORDS");
        String newBadWords;
        do {
            System.out.print("Enter the bad word to add [Enter 'Quit' to back]: ");
            newBadWords = sc.nextLine().trim();
            if (newBadWords.equalsIgnoreCase("quit")) {
                return;
            }
            if (newBadWords.isEmpty()) {
                System.out.println("Invalid input. Please enter a non-empty bad word.");
            }
        } while (newBadWords.isEmpty());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Asus\\Documents\\NetBeansProjects\\Y1S2_Assignment\\profane_words.txt", true));
            writer.write(newBadWords);
            writer.newLine();
            writer.close();
            System.out.println("Bad word added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while adding the profanity word.");
            e.printStackTrace();
        }
    }

    private ArrayList<String> getReport(ArrayList<Post> posts) {
        ArrayList<String> reports = new ArrayList<>();

        for (Post post : posts) {
            ArrayList<String> postReports = database.getList(post, "Report");
            reports.addAll(postReports);
        }

        return reports;
    }

    public void viewUserReports() {
        ArrayList<String> reports = getReport(posts);
        if (reports.isEmpty()) {
            System.out.println("No user reports found.");
        } else {
            System.out.println("User Reports:");
            System.out.println("-------------------------");

            int count = 1;
            for (String report : reports) {
                String[] reportInfo = report.split(":");
                if (reportInfo.length >= 3) {
                    int postIndex = Integer.parseInt(reportInfo[2]);
                    if (postIndex >= 0 && postIndex < posts.size()) {
                        Post reportedPost = posts.get(postIndex);
                        System.out.println(count + ". " + reportInfo[0] + ": " + reportInfo[1]);
                        System.out.println("Reported Post:");
                        pm.printPost(reportedPost);
                        if (reportedPost.getMediaPath() != null && !reportedPost.getMediaPath().isEmpty()) {
                            pm.viewMedia(reportedPost.getMediaPath());
                        }
                        System.out.println("-------------------------");
                    } else {
                        System.out.println("Invalid post index.");
                    }
                } else {
                    System.out.println(count + ". Invalid report format.");
                }
                count++;
            }

            System.out.println("Options:");
            System.out.println("1. Delete Report");
            System.out.println("2. Delete Reported Post");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            boolean validChoice = false;

            while (!validChoice) {
                switch (choice) {
                    case 1:
                        deleteReport();
                        validChoice = true;
                        break;
                    case 2:
                        deleteReportedPost(reports, posts);
                        validChoice = true;
                        break;
                    case 3:
                        validChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }

    private void deleteReport() {
        ArrayList<String> reports = database.getList(post, "Report");

        if (!reports.isEmpty()) {
            reports.clear();
            database.updatePost(post, "Report", reports);
            System.out.println("All reports deleted successfully.");
        } else {
            System.out.println("No reports found for this post.");
        }
    }

    private void deleteReportedPost(ArrayList<String> reports, ArrayList<Post> posts) {
        System.out.print("Enter the index of reported post to delete [-1 to back]: ");
        if (sc.hasNextInt()) {
            int index = sc.nextInt();
            sc.nextLine();

            if (index == -1) {
                return;
            }

            if (index >= 0 && index < reports.size()) {
                String report = reports.get(index);
                String[] reportInfo = report.split(":");
                if (reportInfo.length >= 3) {
                    int postIndex = Integer.parseInt(reportInfo[2]);

                    if (postIndex >= 0 && postIndex < posts.size()) {
                        Post reportedPost = posts.get(postIndex);
                        database.deletePost(reportedPost);
                        reports.remove(index);
                        database.updatePost(post, "Report", reports);

                        System.out.println("Reported post deleted successfully.");
                    } else {
                        System.out.println("Invalid post index.");
                    }
                } else {
                    System.out.println("Invalid report format.");
                }
            } else {
                System.out.println("Invalid index!");
            }
        } else {
            System.out.println("Invalid input! Please enter an integer index.");
        }
    }

}
