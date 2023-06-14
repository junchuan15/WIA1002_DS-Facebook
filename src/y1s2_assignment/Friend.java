/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void friendMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Friend Menu:");
            System.out.println("1. Show Friend List");
            System.out.println("2. Show Pending Friend Requests");
            System.out.println("3. Show Sent Friend Requests");
            System.out.println("4. Send Friend Request");
            System.out.println("0. Exit");
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
                    sendFriendRequest();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting Friend Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void sendFriendRequest() {
        System.out.print("Enter the username of the user you want to send a friend request to: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();

        User user = database.getUser("Username", username);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        sendFriendRequest(user);
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
     
    public List< String> getMutualFriends(User user) {
        List< String> mutualFriendNames = new ArrayList<>();
        ArrayList< String> friends = loggedInUser.getFriends();
        ArrayList< String> otherUserFriends = user.getFriends();

        for (String friend : friends) {
            if (otherUserFriends.contains(friend)) {
                mutualFriendNames.add(friend);
            }
        }

        return mutualFriendNames;
    }

    public List< String> friendRecommendation(User user, String option) {
        List<String> friendRecommendations = new ArrayList<>();

        if (option.equals("mutualFriend")) {
            List<String> mutualFriends = graph.showSecondDegreeConnections(user);
            for (String currentUsername : mutualFriends) {
                int score = 0;
                int numHobbies = 0;
                int numJobs = 0;

                User currentUser = database.getUser("Username", currentUsername);

                if (currentUser != null) {
                    // Check number of same hobbies
                    List<String> currentUserHobbies = currentUser.getHobbies();
                    for (String hobby : user.getHobbies()) {
                        if (currentUserHobbies.contains(hobby)) {
                            numHobbies++;
                        }
                    }
                    score += numHobbies * 2;

                    // Check number of same jobs
                    List<String> currentUserJobs = currentUser.getJobs();
                    for (String job : user.getJobs()) {
                        if (currentUserJobs.contains(job)) {
                            numJobs++;
                        }
                    }
                    score += numJobs;

                    // Add the username to the friendRecommendations list
                    friendRecommendations.add(currentUser.getUsername());
                }
            }
        } else if (option.equals("public")) {
            int[] scoreList = new int[20];
            String[] friendRecommendationList = new User[20];
            List<Integer> randomNumbers = generateUniqueNumbers(database.getUserCount());

            int n = Math.min(randomNumbers.size(), friendRecommendationList.length); // Corrected declaration and initialization of 'n'
            int index = 0;
            for (int i = 0; i < n; i++) {
                if (randomNumbers.contains(i)) {
                    int score = 0;
                    int numMutualFriends = 0;
                    int numHobbies = 0;
                    int numJobs = 0;

                    User currentUser = database.getUserByIndex(i);

                    if (currentUser != null && !currentUser.getUsername().equals(user.getUsername())) {
                        // Check number of mutual friends
                        List<String> mutualFriends = getMutualFriends(currentUser);
                        numMutualFriends = mutualFriends.size();
                        score += numMutualFriends * 3;

                        // Check number of same hobbies
                        List<String> currentUserHobbies = currentUser.getHobbies();
                        for (String hobby : user.getHobbies()) {
                            if (currentUserHobbies.contains(hobby)) {
                                numHobbies++;
                            }
                        }
                        score += numHobbies * 2;

                        // Check number of same jobs
                        List<String> currentUserJobs = currentUser.getJobs();
                        for (String job : user.getJobs()) {
                            if (currentUserJobs.contains(job)) {
                                numJobs++;
                            }
                        }
                        score += numJobs;

                        scoreList[index] = score;
                        friendRecommendationList[index] = currentUser.getUsername();
                        index++;
                    }
                }
            }
            // Selection sort - sort recommendation list based on score
            for (int i = 0; i < n - 1; i++) {
                int maxIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (scoreList[j] > scoreList[maxIdx]) {
                        maxIdx = j;
                    }
                }
                // Swap the elements
                int tempScore = scoreList[maxIdx];
                scoreList[maxIdx] = scoreList[i];
                scoreList[i] = tempScore;

                String tempRecommendation = friendRecommendationList[maxIdx];
                friendRecommendationList[maxIdx] = friendRecommendationList[i];
                friendRecommendationList[i] = tempRecommendation;
            }

            // Add recommended users to the friendRecommendations list
            for (int i = 0; i < friendRecommendationList.length; i++) {
                String username = friendRecommendationList[i];
                User recommendedUser = database.getUser("Username", username);
                if (recommendedUser != null) {
                    // Create a new User object with the recommended user's data and score
                    User recommendedUserWithScore = new User(recommendedUser.getUsername(), recommendedUser.getFullName(), recommendedUser.getAge());
                    recommendedUserWithScore.setScore(scoreList[i]);
                    friendRecommendations.add(recommendedUserWithScore);
                }
            }
        }

        return friendRecommendations;
    }
}
