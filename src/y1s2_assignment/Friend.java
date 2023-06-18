/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import y1s2_assignment.ConnectionGraph.Vertex;

/**
 *
 * @author Asus
 */
public class Friend {

    private User loggedInUser;
    DatabaseSQL database = new DatabaseSQL();
    ConnectionGraph graph = database.createGraph();
    Scanner scanner = new Scanner(System.in);

    public Friend(User loggedInUser) {
        this.loggedInUser = loggedInUser;
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

    public void numFriendIncrement(User user) {
        int numberOfFriends = user.getNumberOfFriends();
        user.setNumberOfFriends(numberOfFriends + 1);
    }

    public void deleteFriend(User user) {
        ArrayList<String> friends = loggedInUser.getFriends();
        if (friends.isEmpty()) {
            System.out.println("You have no friends.");
            return;
        }

        String username = user.getUsername();

        if (friends.contains(username)) {
            System.out.print("Are you sure you want to delete " + username + " (Y/N)?: ");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("Y")) {
                removeFriend(loggedInUser, user);
                System.out.println(username + " has been deleted from your friends list.");
            } else {
                System.out.println("Deletion canceled.");
            }
        } else if (loggedInUser.getReceivedRequests().contains(username)) {
            System.out.println(username + " has sent you a friend request. Reject the friend request instead.");
        } else if (loggedInUser.getSentRequests().contains(username)) {
            System.out.println("You have sent a friend request to " + username + ". Cancel the friend request instead.");
        } else {
            System.out.println(username + " is not in your friends list.");
        }
    }

    private void removeFriend(User loggedInUser, User friend) {
        ArrayList<String> friends = loggedInUser.getFriends();
        friends.remove(friend.getUsername());
        numFriendDecrement(loggedInUser);
        database.updateUserDetail(loggedInUser);

        ArrayList<String> userFriends = friend.getFriends();
        userFriends.remove(loggedInUser.getUsername());
        numFriendDecrement(friend);
        database.updateUserDetail(friend);
        graph.removeEdge(loggedInUser, friend);
    }

    private void numFriendDecrement(User user) {
        int numFriends = user.getNumberOfFriends();
        user.setNumberOfFriends(numFriends - 1);
    }

    public void friendMenu() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("==============================================");
            System.out.println("                  FRIEND MENU                  ");
            System.out.println("==============================================");
            System.out.println("          1. Show Friend List");
            System.out.println("          2. Show Pending Friend Requests");
            System.out.println("          3. Show Sent Friend Requests");
            System.out.println("          4. Show Friend Recommendation");
            System.out.println("          5. Back to Main Menu.");
            System.out.println("==============================================");
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
                        System.out.println("==============================================");
                        System.out.println("          DISCOVER RECOMMENDATION:");
                        System.out.println("==============================================");
                        System.out.println("          1. By Degree of Friend");
                        System.out.println("          2. By Hobbies and Jobs");
                        System.out.println("          3. Back to Friend Menu");
                        System.out.println("==============================================");
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

    public void showFriendList() throws SQLException {
        if (loggedInUser == null) {
            System.out.println("No user logged in.");
            return;
        }

        ArrayList<String> friends = loggedInUser.getFriends();
        if (friends.isEmpty()) {
            System.out.println("You have no friends.");
            return;
        }

        System.out.println("Friend List:");
        for (int i = 0; i < friends.size(); i++) {
            String friend = friends.get(i);
            System.out.println((i + 1) + ". " + friend);

        }

        System.out.print("Enter the index of the user you want to view [-1 to back]:");
        int selectedIndex = scanner.nextInt();
        if (selectedIndex == -1) {
            return; // Go back
        }
        System.out.println("==============================================");

        if (selectedIndex < 1 || selectedIndex > friends.size()) {
            System.out.println("Invalid index. Please try again.");
            return;
        }

        String selectedUserName = friends.get(selectedIndex - 1);
        User selectedUser = database.getUser("UserName", selectedUserName);
        if (selectedUser == null) {
            System.out.println("User not found. Please try again.");
            return;
        }

        action(selectedUser);
    }

    public void showSentRequests() throws SQLException {

        ArrayList<String> sentRequests = loggedInUser.getSentRequests();
        if (sentRequests.isEmpty()) {
            System.out.println("You have not sent any friend requests.");
            return;
        }
        System.out.println("Sent Friend Requests:");

        for (int i = 0; i < sentRequests.size(); i++) {
            String sentRequest = sentRequests.get(i);
            System.out.println((i + 1) + ". " + sentRequest);
        }

        System.out.print("Enter the index of the user you want to view [-1 to back]: ");
        int selectedIndex = scanner.nextInt();
        if (selectedIndex == -1) {
            return; // Go back
        }
        System.out.println("==============================================");
        if (selectedIndex < 1 || selectedIndex > sentRequests.size()) {
            System.out.println("Invalid index. Please try again.");
            return;
        }

        String selectedUserName = sentRequests.get(selectedIndex - 1);
        User selectedUser = database.getUser("UserName", selectedUserName);
        if (selectedUser == null) {
            System.out.println("User not found. Please try again.");
            return;
        }
        action(selectedUser);
    }

    public void handleFriendRequests() {
        ArrayList<String> pendingRequests = loggedInUser.getReceivedRequests();
        if (pendingRequests.isEmpty()) {
            System.out.println("You have no pending friend requests.");
            return;
        }

        System.out.println("Received Friend Requests:");
        for (int i = 0; i < pendingRequests.size(); i++) {
            String friendRequest = pendingRequests.get(i);
            System.out.println((i + 1) + ". " + friendRequest);
        }

        System.out.print("Enter the index of the friend request to process [-1 to back]: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == -1) {
            return; // Go back
        }

        if (choice < 1 || choice > pendingRequests.size()) {
            System.out.println("Invalid choice. Request not processed.");
            return;
        }

        String friendToProcess = pendingRequests.get(choice - 1);
        processFriendRequest(friendToProcess);
    }

    private void processFriendRequest(String friendUsername) {
        while (true) {
            System.out.print("Enter the action that you want to take [1 to Accept, 2 to Reject, -1 to Back]: ");
            int choice = scanner.nextInt();

            if (choice == -1) {
                return;
            } else if (choice == 1) {
                acceptFriendRequest(friendUsername);
                break;
            } else if (choice == 2) {
                rejectFriendRequest(friendUsername);
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void acceptFriendRequest(String friendUsername) {
        User friend = database.getUser("Username", friendUsername);
        if (friend == null) {
            System.out.println("User not found.");
            return;
        }

        loggedInUser.getReceivedRequests().remove(friendUsername);
        loggedInUser.getFriends().add(friendUsername);
        numFriendIncrement(loggedInUser);

        friend.getSentRequests().remove(loggedInUser.getUsername());
        friend.getFriends().add(loggedInUser.getUsername());
        numFriendIncrement(friend);
        graph.addEdge(loggedInUser, friend);

        database.updateUserDetail(loggedInUser);
        database.updateUserDetail(friend);

        System.out.println("Friend request from " + friendUsername + " is accepted. " + friendUsername + " is now your friend.");
    }

    private void rejectFriendRequest(String friendUsername) {
        loggedInUser.getReceivedRequests().remove(friendUsername);
        User friend = database.getUser("Username", friendUsername);
        if (friend != null) {
            friend.getSentRequests().remove(loggedInUser.getUsername());
            database.updateUserDetail(friend);
        }

        database.updateUserDetail(loggedInUser);

        System.out.println("Friend request from " + friendUsername + " is rejected.");
    }

    public void viewAccount(User loggedInUser) {
        User user = database.getUser("Account_ID", loggedInUser.getAccountID());
        if (user != null) {
            System.out.println("==============================================");
            System.out.println("             Displaying user profile           ");
            System.out.println("==============================================");
            System.out.println("Account ID:            " + user.getAccountID());
            System.out.println("Name:                  " + user.getName());
            System.out.println("Username:              " + user.getUsername());
            System.out.println("Email Address:         " + user.getEmailAddress());
            System.out.println("Contact Number:        " + user.getContactNumber());
            System.out.println("Birthday:              " + user.getBirthday());
            System.out.println("Age:                   " + user.getAge());
            System.out.println("Address:               " + user.getAddress());
            System.out.println("Gender:                " + user.getGender());
            System.out.println("Relationship Status:   " + user.getRelationshipStatus());
            int numberOfMutualFriends = countMutualFriends(user);
            System.out.println("Number of Friends:     " + user.getNumberOfFriends() + " (" + numberOfMutualFriends + " Mutual Friend(s))");
            System.out.println("Friends:               " + user.getFriends());
            System.out.println("Hobbies:               " + user.getHobbies());
            Stack<String> jobs = user.getJobs();
            String currentJob = jobs.pop();
            System.out.println("Current Job:           " + currentJob);
            System.out.println("Previous Jobs History: " + jobs);
            jobs.push(currentJob);
        } else {
            System.out.println("User not found.");
        }
    }

    public void action(User user) throws SQLException {
        boolean exit2 = false;
        viewAccount(user);

        while (!exit2) {
            System.out.println("==============================================");
            System.out.println("                 ACTION                        ");
            System.out.println("==============================================");
            System.out.println("          1. Add friend");
            System.out.println("          2. Remove friend");
            System.out.println("          3. View Post");
            System.out.println("          4. Chat");
            System.out.println("          5. Back");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");
            int choice2 = scanner.nextInt();
            scanner.nextLine();

            switch (choice2) {
                case 1:
                    sendFriendRequest(user);
                    break;
                case 2:
                    deleteFriend(user);
                    break;
                case 3:
                    PostManager post = new PostManager(loggedInUser);
                    post.viewPost(user);
                    break;
                case 4:
                    Chat chat = new Chat(loggedInUser);
                    chat.startChatting(user);
                    break;
                case 5:
                    exit2 = true;
                    System.out.println("Loading Back...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    public void publicRecommend() throws SQLException {
        List<User> users = new ArrayList<>(database.loadUsers());
        // Remove logged-in user and their friends from the recommendation list
        List<String> loggedInUserFriendNames = loggedInUser.getFriends();
        users = users.stream()
                .filter(user -> !loggedInUserFriendNames.contains(user.getUsername()))
                .filter(user -> !user.getAccountID().equals(loggedInUser.getAccountID()))
                .collect(Collectors.toList());

        // Sort the users based on the calculated score
        users.sort(Comparator.comparingInt(this::calculateScore).reversed());
        if (users.isEmpty()) {
            System.out.println("No friend recommendations available.");
            return;
        }

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

        int friendChoice = -1;
        while (friendChoice != -1) {
            System.out.print("Choose a friend from the recommendation list (enter the number, or -1 to go back): ");
            try {
                friendChoice = scanner.nextInt();
                scanner.nextLine();

                if (friendChoice == -1) {
                    return; // Go back
                } else if (friendChoice < 1 || friendChoice > users.size()) {
                    System.out.println("Invalid choice! Please try again.");
                } else {
                    User selectedFriend = users.get(friendChoice - 1);
                    action(selectedFriend);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
                friendChoice = -1;
            }
        }
    }

    private int countMutualFriends(User user) {
        Vertex loggedInUserVertex = graph.findVertex(loggedInUser);
        Vertex userVertex = graph.findVertex(user);

        if (loggedInUserVertex != null && userVertex != null) {
            return graph.countMutualConnections(loggedInUserVertex, userVertex);
        }

        return 0;
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

    public void degreeRecommend() throws SQLException {
        List<User> recommendedFriends = graph.getRecommendedConnections(loggedInUser);

        // Sort the recommended friends based on the number of mutuals in descending order
        recommendedFriends.sort((u1, u2) -> {
            int mutuals1 = countMutualConnections(loggedInUser, u1);
            int mutuals2 = countMutualConnections(loggedInUser, u2);
            return Integer.compare(mutuals2, mutuals1);
        });

        // Split the recommended friends into 2nd-degree and 3rd-degree connections
        List<User> secondDegree = new ArrayList<>();
        List<User> thirdDegree = new ArrayList<>();

        for (User friend : recommendedFriends) {
            int mutuals = countMutualConnections(loggedInUser, friend);
            if (mutuals > 0) {
                if (graph.getShortestDistance(loggedInUser, friend) == 2) {
                    secondDegree.add(friend);
                } else if (graph.getShortestDistance(loggedInUser, friend) == 3) {
                    thirdDegree.add(friend);
                }
            }
        }

        // Sort the 2nd-degree connections based on the number of mutuals in descending order
        secondDegree.sort((u1, u2) -> Integer.compare(countMutualConnections(loggedInUser, u2), countMutualConnections(loggedInUser, u1)));

        // Sort the 3rd-degree connections alphabetically
        thirdDegree.sort(Comparator.comparing(User::getUsername));

        List<User> friendRecommendations = new ArrayList<>();

        // Add the 2nd-degree connections with higher priority
        friendRecommendations.addAll(secondDegree);

        // Add the 3rd-degree connections with lower priority
        friendRecommendations.addAll(thirdDegree);

        if (friendRecommendations.isEmpty()) {
            System.out.println("No friend recommendations available.");
        } else {
            System.out.println("Friend Recommendations:");
            int count = 1;
            for (User recommendedUser : friendRecommendations) {
                System.out.println(count + ". " + recommendedUser.getUsername());
                count++;
            }

            Scanner sc = new Scanner(System.in);
            int friendChoice = 0;

            while (friendChoice < 1 || friendChoice > friendRecommendations.size()) {
                System.out.print("Choose a friend from the recommendation list (enter the number): ");
                friendChoice = sc.nextInt();
                sc.nextLine();

                if (friendChoice < 1 || friendChoice > friendRecommendations.size()) {
                    System.out.println("Invalid choice! Please try again.");
                }
            }

            User selectedFriend = friendRecommendations.get(friendChoice - 1);
            action(selectedFriend);
        }
    }

    public int countMutualConnections(User user1, User user2) {
        List<String> friends1 = user1.getFriends();
        List<String> friends2 = user2.getFriends();

        int count = 0;
        for (String friend1 : friends1) {
            if (friends2.contains(friend1)) {
                count++;
            }
        }

        return count;
    }

}
