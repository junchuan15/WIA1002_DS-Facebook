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

    public Friend(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.database = new DatabaseSQL();
    }

    public void sendFriendRequest(User user) {
        String username = user.getUsername();

        if (!loggedInUser.getFriends().contains(username) && !loggedInUser.getSentRequests().contains(username)) {
            receiveFriendRequest(user);
            loggedInUser.getSentRequests().add(username);
            database.updateSentRequests(loggedInUser);
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
            database.updateReceivedRequests(user);
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
            String receiveListString = database.getReceivedRequests(loggedInUser.getUsername());
            String[] receive = receiveListString.split(",");
            pending.addAll(Arrays.asList(receive));
            loggedInUser.getReceivedRequests().remove(username);
            database.updateReceivedRequests(loggedInUser);
            ArrayList<String> friends1 = loggedInUser.getFriends();
            String friendListString1 = database.getFriendList(loggedInUser.getUsername());
            String[] friendNames1 = friendListString1.split(",");
            friends1.addAll(Arrays.asList(friendNames1));
            friends1.add(user.getUsername());
            loggedInUser.getFriends().add(username);
            numFriendIncrement(loggedInUser);
            database.updateNumFriendsAndFriends(loggedInUser);

            ArrayList<String> sentRequest = user.getSentRequests();
            String sentListString = database.getSentRequests(username);
            String[] request = sentListString.split(",");
            sentRequest.addAll(Arrays.asList(request));
            sentRequest.remove(loggedInUser.getUsername());
            database.updateSentRequests(user);
            ArrayList<String> friends2 = user.getFriends();
            String friendListString2 = database.getFriendList(username);
            String[] friendNames2 = friendListString2.split(",");
            friends2.addAll(Arrays.asList(friendNames2));
            friends2.add(loggedInUser.getUsername());
            numFriendIncrement(user);
            database.updateNumFriendsAndFriends(user);
            System.out.println("Friend request from " + username + " is accepted. " + username + " is now your friend.");
        } else {
            ArrayList<String> pending = loggedInUser.getReceivedRequests();
            String receiveListString = database.getReceivedRequests(loggedInUser.getUsername());
            String[] receive = receiveListString.split(",");
            pending.addAll(Arrays.asList(receive));
            loggedInUser.getReceivedRequests().remove(username);
            database.updateReceivedRequests(loggedInUser);

            ArrayList<String> sentRequest = user.getSentRequests();
            String sentListString = database.getSentRequests(username);
            String[] request = sentListString.split(",");
            sentRequest.addAll(Arrays.asList(request));
            user.getSentRequests().remove(loggedInUser.getUsername());
            database.updateSentRequests(user);
            System.out.println("Friend request from " + username + " is rejected. ");
        }
    }

    public void numFriendIncrement(User user) {
        String username = user.getUsername();
        int numberOfFriend = database.getNumberOfFriends(username);
        numberOfFriend++;
        user.setNumberOfFriends(numberOfFriend);
    }

    public void numFriendDecrement(User user) {
        String username = user.getUsername();
        int numberOfFriend = database.getNumberOfFriends(username);
        numberOfFriend--;
        user.setNumberOfFriends(numberOfFriend);
    }

   public void deleteFriend(User user) {
    Scanner sc = new Scanner(System.in);
    ArrayList<String> friends = loggedInUser.getFriends();
    String friendListString = database.getFriendList(loggedInUser.getUsername());
    String[] friendNames = friendListString.split(",");
    friends.addAll(Arrays.asList(friendNames));
    if (friends.isEmpty()) {
        System.out.println("You have no friends.");
        return;
    }

    System.out.print("Are you sure you want to delete " + user.getUsername() + " (Y/N)?: ");
    String choice = sc.next();
    if (choice.equalsIgnoreCase("Y")) {
        friends.remove(user.getUsername());
        numFriendDecrement(loggedInUser);
        database.updateNumFriendsAndFriends(loggedInUser);

        ArrayList<String> userFriends = user.getFriends();
        String userFriendListString = database.getFriendList(user.getUsername());
        String[] userFriendNames = userFriendListString.split(",");
        userFriends.addAll(Arrays.asList(userFriendNames));
        userFriends.remove(loggedInUser.getUsername());
        numFriendDecrement(user);
        database.updateNumFriendsAndFriends(user);
        
        System.out.println(user.getUsername() + " has been deleted from your friends list.");
    } else {
        System.out.println("Deletion canceled.");
    }
}

    public void friendMenu() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
     while (!exit) {
     System.out.println("Friend Menu:");
        System.out.println("1. Show Friend List");
        System.out.println("2. Show Pending Friend Requests");
        System.out.println("3. Show Sent Friend Requests");
        System.out.println("4. Send Friend Request");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
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
                    break;
                case 0:
                    exit=true;
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
        String friendListString = database.getFriendList(loggedInUser.getUsername());
        String[] friendNames = friendListString.split(",");
        friends.addAll(Arrays.asList(friendNames));
        if (friends.isEmpty()) {
            System.out.println("You have no friends.");
        }
        for (int i = 0; i < friends.size(); i++) {
            String friend = friends.get(i);
            System.out.println((i + 1) + ". " + friend);
        }
    }

    public void showSentRequests() {
        System.out.println("Sent Friend Requests:");
        ArrayList<String> sentRequest = loggedInUser.getSentRequests();
        String SentListString = database.getSentRequests(loggedInUser.getUsername());
        String[] request = SentListString.split(",");
        sentRequest.addAll(Arrays.asList(request));
        if (sentRequest.isEmpty()) {
            System.out.println("No Sent Request.");
        }
        for (int i = 0; i < sentRequest.size(); i++) {
            String sent = sentRequest.get(i);
            System.out.println((i + 1) + ". " + sent);
        }
    }

    public void handleFriendRequests() {
        Scanner input = new Scanner(System.in);
        ArrayList<String> pending = loggedInUser.getReceivedRequests();
        String ReceiveListString = database.getReceivedRequests(loggedInUser.getUsername());
        String[] request = ReceiveListString.split(",");
        pending.addAll(Arrays.asList(request));

        System.out.println("Recived Friend Requests: ");
        for (int i = 0; i < pending.size(); i++) {
            String friend = pending.get(i);
            System.out.println((i + 1) + ". " + friend);
        }

        if (pending.isEmpty()) {
            System.out.println("No pending friend request");
            return;
        }

        System.out.print("Enter the action that you wanted to make: [1 to Accept, 2 to Reject]");
        int choice = input.nextInt();

        if (choice < 1 || choice > 2) {
            System.out.println("Invalid choice. Request not processed.");
            return;
        }

        if (choice == 1) {
            System.out.println("Please choose who you want to accept: ");
            for (int i = 0; i < pending.size(); i++) {
                String friend = pending.get(i);
                System.out.println((i + 1) + ". " + friend);
            }
            int accept = input.nextInt();
            if (accept < 1 || accept > pending.size()) {
                System.out.println("Invalid choice. Request not processed.");
                return;
            }
            String friendToAccept = pending.get(accept - 1);
            pendingFriendRequest(friendToAccept, "1");
        } else {
            System.out.println("Please choose the friend request to reject: ");
            for (int i = 0; i < pending.size(); i++) {
                String friend = pending.get(i);
                System.out.println((i + 1) + ". " + friend);
            }

            int reject = input.nextInt();
            String friendToReject = pending.get(reject - 1);
            pendingFriendRequest(friendToReject, "2");
        }
    }
    /*
    public List<String> getMutualFriends(User otherUser) {
        List<String> mutualFriendNames = new ArrayList<>();
        for (User friend : this.friends) {
            if (otherUser.friends.contains(friend)) {
                mutualFriendNames.add(friend.getName());
            }
        }
        return mutualFriendNames;
    }
     */
}
