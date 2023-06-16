
















/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class Friend {

    private User loggedInUser;
    private DatabaseSQL database;
    private ConnectionGraph graph;
    Scanner scanner = new Scanner(System.in);

    public Friend(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.database = new DatabaseSQL();
        this.graph = database.createGraph();
    }

    public void sendFriendRequest(User user) {
        String username = user.getUsername();

        if (!loggedInUser.getFriends().contains(username) && !loggedInUser.getSentRequests().contains(username)) {
            receiveFriendRequest(user);
            loggedInUser.getSentRequests().add(username);
            database.updateUserDetail(loggedInUser);
            System.out.println("Friend request sent to " + username);
        } else if (loggedInUser.getFriends().contains(username)) {
            System.out.println("The user " + username + " is already your friend.");
        } else {
            System.out.println("The user " + username + " is in your sent friend request list.");
        }
    }

    public void receiveFriendRequest(User user) {
        String username = loggedInUser.getUsername();

        if (!user.getReceivedRequests().contains(username)) {
            user.getReceivedRequests().add(username);
            database.updateUserDetail(user);
        }
    }

    public void pendingFriendRequest(String username, String action) {
        User user = database.getUser("Username", username);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (action.equals("1")) {
            ArrayList<String> pending = loggedInUser.getReceivedRequests();
            loggedInUser.getReceivedRequests().remove(username);
            ArrayList<String> friends1 = loggedInUser.getFriends();
            friends1.add(username);
            numFriendIncrement(loggedInUser);

            ArrayList<String> sentRequest = user.getSentRequests();
            sentRequest.remove(loggedInUser.getUsername());
            ArrayList<String> friends2 = user.getFriends();
            friends2.add(loggedInUser.getUsername());
            numFriendIncrement(user);

            System.out.println("Friend request from " + username + " is accepted. " + username + " is now your friend.");
        } else {
            ArrayList<String> pending = loggedInUser.getReceivedRequests();
            loggedInUser.getReceivedRequests().remove(username);
            ArrayList<String> sentRequest = user.getSentRequests();
            sentRequest.remove(loggedInUser.getUsername());
        }

        database.updateUserDetail(loggedInUser);
        database.updateUserDetail(user);
    }

    public void numFriendIncrement(User user) {
        int numberOfFriends = user.getNumberOfFriends();
        user.setNumberOfFriends(numberOfFriends + 1);
    }

    public void numFriendDecrement(User user) {
        int numberOfFriends = user.getNumberOfFriends();
        user.setNumberOfFriends(numberOfFriends - 1);
    }

    public void deleteFriend(User user) {
        ArrayList<String> friends = loggedInUser.getFriends();
        if (friends.isEmpty()) {
            System.out.println("You have no friends.");
            return;
        }

        System.out.print("Are you sure you want to delete " + user.getUsername() + " (Y/N)?: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("Y")) {
            friends.removeIf(friend -> friend.equals(user.getUsername()));
            numFriendDecrement(loggedInUser);
            database.updateUserDetail(loggedInUser);

            ArrayList<String> userFriends = user.getFriends();
            userFriends.remove(loggedInUser.getUsername());
            numFriendDecrement(user);
            database.updateUserDetail(user);

            System.out.println(user.getUsername() + " has been deleted from your friends list.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    public void friendMenu() throws SQLException {
        boolean exit = false;
        while (!exit) {

            System.out.println("==============================================\nFRIEND MENU:");
            System.out.println("1. Show Friend List");
            System.out.println("2. Show Pending Friend Requests");
            System.out.println("3. Show Sent Friend Requests");
            System.out.println("4. Show Friend Recommendation");
            System.out.println("5. Back to Main Menu.");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    showFriendList();
                    break;
                case 2:
                    handleFriendRequests();
                    break;
                case 3:
                    showSentRequests();
                    break;
                case 4:
                    boolean back = false;
                    while (!back) {
                        System.out.println("==============================================\nDISCOVER RECOMMENDATION:");
                        System.out.println("1. By Degree of Friend");
                        System.out.println("2. By Hobbies and Jobs");
                        System.out.println("3. Back to Friend Menu");
                        System.out.print("Enter your choice: ");
                        int select = scanner.nextInt();
                        scanner.nextLine();
                        switch (select) {
                            case 1:
                                degreeRecommend();
                                break;
                            case 2:
                                publicRecommend();
                                break;
                            case 3:
                                back = true;
                                System.out.println("Exiting Discover Menu");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;

                        }
                    }
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting Friend Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void showFriendList() {
        System.out.println("Friend List:");
        ArrayList<String> friends = loggedInUser.getFriends();
        if (friends.isEmpty()) {
            System.out.println("You have no friends.");
            return;
        }
        for (int i = 0; i < friends.size(); i++) {
            String friend = friends.get(i);
            System.out.println((i + 1) + ". " + friend);
        }
    }

    public void showSentRequests() {
        System.out.println("Sent Friend Requests:");
        ArrayList<String> sentRequest = loggedInUser.getSentRequests();
        if (sentRequest.isEmpty()) {
            System.out.println("No Sent Request.");
            return;
        }
        for (int i = 0; i < sentRequest.size(); i++) {
            String sent = sentRequest.get(i);
            System.out.println((i + 1) + ". " + sent);
        }
    }

    public void handleFriendRequests() {
        ArrayList<String> pending = loggedInUser.getReceivedRequests();
        if (pending.isEmpty()) {
            System.out.println("No pending friend requests.");
            return;
        }

        System.out.println("Received Friend Requests:");
        for (int i = 0; i < pending.size(); i++) {
            String friend = pending.get(i);
            System.out.println((i + 1) + ". " + friend);
        }

        System.out.print("Enter the action that you want to take [1 to Accept, 2 to Reject]: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice < 1 || choice > 2) {
            System.out.println("Invalid choice. Request not processed.");
            return;
        }

        System.out.print("Please choose the request to " + (choice == 1 ? "accept" : "reject") + ": ");
        int requestIndex = scanner.nextInt();

        if (requestIndex < 1 || requestIndex > pending.size()) {
            System.out.println("Invalid choice. Request not processed.");
            return;
        }

        String friendToProcess = pending.get(requestIndex - 1);
        pendingFriendRequest(friendToProcess, String.valueOf(choice));
    }

    public void action(User user) throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean exit2 = false;

        while (!exit2) {
            System.out.println("==============================================\nACTION: ");
            System.out.println("1. View profile");
            System.out.println("2. Add friend");
            System.out.println("3. Remove friend");
            System.out.println("4. Chat");
            System.out.println("5. View Post");
            System.out.println("6. Back");
            System.out.print("Enter your choice: ");
            int choice2 = sc.nextInt();
            sc.nextLine();
            switch (choice2) {
                case 1:
                    UserAccess userAccess = new UserAccess();
                    userAccess.viewAccount(user);
                    break;
                case 2:
                    sendFriendRequest(user);
                    break;
                case 3:
                    deleteFriend(user);
                    break;
                case 4:
                    Chat chat = new Chat(loggedInUser);
                    chat.startChatting(user);
                    break;
                case 5:
                    PostManager post = new PostManager(loggedInUser);
                    post.viewPost(user);
                    break;
                case 6:
                    exit2 = true;
                    System.out.println("Loading Back...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    // follow friend degree
    public void degreeRecommend() throws SQLException {
        List<User> friend = graph.getRecommendedConnections(loggedInUser);

        if (friend.isEmpty()) {
            System.out.println("No friend recommendations available.");
        } else {
            System.out.println("Friend Recommendations:");
            int friendSize = Math.min(10, friend.size());
            for (int i = 0; i < friendSize; i++) {
                System.out.println((i + 1) + ". " + friend.get(i).getUsername());
            }

            int friendChoice = 0;

            while (friendChoice < 1 || friendChoice > friend.size()) {
                System.out.print("Choose a friend from the recommendation list (enter the number): ");
                try {
                    friendChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (friendChoice < 1 || friendChoice > friend.size()) {
                        System.out.println("Invalid choice! Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.nextLine();
                    friendChoice = 0;
                }
            }
            User selectedFriend = friend.get(friendChoice - 1);
            action(selectedFriend);
        }
    }
    
    // I change using comparator instead of bubble sort
    public void publicRecommend() throws SQLException {
        List<User> users = database.loadUsers();
        users.remove(loggedInUser);
        users.removeAll(graph.showFirstDegreeConnections(loggedInUser));

        users.sort(Comparator.comparingInt(this::calculateScore).reversed());

        if (users.isEmpty()) {
            System.out.println("No friend recommendations available.");
        } else {
            System.out.println("Friend Recommendations:");
            for (int i = 0; i < Math.min(10, users.size()); i++) {
                User recommendedUser = users.get(i);
                System.out.print((i + 1) + ". " + recommendedUser.getUsername());
                int sameHobbies = countSameInterests(recommendedUser.getHobbies(), loggedInUser.getHobbies());
                int sameJobs = countSameInterests(recommendedUser.getJobs(), loggedInUser.getJobs());
                int mutualFriends = countMutualFriends(recommendedUser);

                if (sameHobbies > 0 || sameJobs > 0 || mutualFriends > 0) {
                    System.out.print(" (");

                    if (sameHobbies > 0) {
                        System.out.print(sameHobbies + " same hobby(s), ");
                    }

                    if (sameJobs > 0) {
                        System.out.print(sameJobs + " same job(s), ");
                    }

                    if (mutualFriends > 0) {
                        System.out.print(mutualFriends + " mutual friend(s)");
                    }

                    System.out.print(")");
                }

                System.out.println();
            }

            int friendChoice = 0;

            while (friendChoice < 1 || friendChoice > users.size()) {
                System.out.print("Choose a friend from the recommendation list (enter the number): ");
                try {
                    friendChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (friendChoice < 1 || friendChoice > users.size()) {
                        System.out.println("Invalid choice! Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.nextLine();
                    friendChoice = 0;
                }
            }

            User selectedFriend = users.get(friendChoice - 1);
            action(selectedFriend);
        }
    }

    private int calculateScore(User user) {
        int score = 0;

        int sameHobbies = countSameInterests(user.getHobbies(), loggedInUser.getHobbies());
        score += sameHobbies * 2;

        int sameJobs = countSameInterests(user.getJobs(), loggedInUser.getJobs());
        score += sameJobs * 2;

        int mutualFriends = countMutualFriends(user);
        return score + mutualFriends;
    }

    private int countSameInterests(List<String> list1, List<String> list2) {
        List<String> commonInterests = new ArrayList<>(list1);
        commonInterests.retainAll(list2);
        return commonInterests.size();
    }

    private int countMutualFriends(User user) {
        List<String> mutualFriends = graph.showFirstDegreeConnections(user);
        mutualFriends.retainAll(loggedInUser.getFriends());
        return mutualFriends.size();
    }
}
