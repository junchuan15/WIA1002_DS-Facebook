/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class SearchEngine {

    private ArrayList<String> accountIDList;
    private ArrayList<String> usernameList;
    private ArrayList<String> emailList;
    private ArrayList<String> contactNumberList;
    private ArrayList<String> nameList;
    private DatabaseSQL database = new DatabaseSQL();
    private UserAccess userAccess = new UserAccess();
    private User loggedInUser;
    private Friend friendManager;

    public SearchEngine(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.friendManager = new Friend(loggedInUser);
        accountIDList = new ArrayList<>();
        usernameList = new ArrayList<>();
        emailList = new ArrayList<>();
        contactNumberList = new ArrayList<>();
        nameList = new ArrayList<>();
        database.readFromTable(accountIDList, usernameList, emailList, contactNumberList, nameList);
    }

    public void searchUsers() {
        Scanner sc = new Scanner(System.in);
        boolean exit1 = false;

        while (!exit1) {
            System.out.println("==============================================\nSearch for Users: ");
            System.out.println("1. By account ID");
            System.out.println("2. By username");
            System.out.println("3. By email");
            System.out.println("4. By contact number");
            System.out.println("5. By name");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice1 = sc.nextInt();
            sc.nextLine();

            switch (choice1) {
                case 1:
                    searchField(accountIDList, "Account_ID");
                    break;
                case 2:
                    searchField(usernameList, "UserName");
                    break;
                case 3:
                    searchField(emailList, "EmailAddress");
                    break;
                case 4:
                    searchField(contactNumberList, "ContactNumber");
                    break;
                case 5:
                    searchField(nameList, "Name");
                    break;
                case 6:
                    exit1 = true;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    public void searchField(ArrayList<String> inputList, String attribute) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Type a " + attribute + ": ");
        String search = sc.nextLine();
        display(inputList, search);

        System.out.print("Select the " + attribute + " you want to find: ");
        String correct = sc.nextLine();
        User searchedUser = database.getUser(attribute, correct);
        action(inputList, searchedUser);
    }

    public static void display(ArrayList<String> inputList, String searchKey) {
        ArrayList<ArrayList<String>> similarity = new ArrayList<>();

        for (String value : inputList) {
            String distance = Double.toString(DamerauLevenshteinDistance(searchKey, value));
            ArrayList<String> row = new ArrayList<>();
            row.add(value);
            row.add(distance);
            similarity.add(row);
        }
        Comparator<ArrayList<String>> comparator = Comparator.comparingDouble(row -> Double.parseDouble(row.get(1)));
        similarity.sort(comparator);
        similarity.sort((row1, row2) -> {
            int compareResult = row1.get(1).compareTo(row2.get(1));
            if (compareResult == 0) {
                return row1.get(0).compareTo(row2.get(0));
            }
            return compareResult;
        });

        for (int i = 0; i < Math.min(10, similarity.size()); i++) {
            System.out.println(similarity.get(i).get(0));
        }
    }

    public static double DamerauLevenshteinDistance(String searchKey, String element) {
        if (element.equals(searchKey)) {
            return 0;
        } else if (element.startsWith(searchKey) || element.endsWith(searchKey)) {
            return 0.1;
        } else if (match(searchKey, element)) { //finding longest common substring
            int[][] commonSubstring = new int[searchKey.length() + 1][element.length() + 1];
            int length = 0;
            for (int i = 1; i <= searchKey.length(); i++) {
                for (int j = 1; j <= element.length(); j++) {
                    if (searchKey.charAt(i - 1) == element.charAt(j - 1)) {
                        commonSubstring[i][j] = commonSubstring[i - 1][j - 1] + 1;
                        length = Math.max(length, commonSubstring[i][j]);
                    } else {
                        commonSubstring[i][j] = 0;
                    }
                }
            }
            int middle = searchKey.length() / 2;
            int quarter = middle / 2;
            if (length >= middle) {
                if (length <= middle + quarter) {
                    return 0.3;
                } else if (length <= searchKey.length()) {
                    return 0.2;
                }
            } else {
                return formula(searchKey, element);
            }
        } else {
            return formula(searchKey, element);
        }
        return -1;
    }

    public static int formula(String searchKey, String element) {
        int[][] distance = new int[searchKey.length() + 1][element.length() + 1];
        int cost = 0;
        for (int i = 0; i <= searchKey.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= element.length(); j++) {
            distance[0][j] = j;
        }
        for (int i = 1; i <= searchKey.length(); i++) {
            for (int j = 1; j <= element.length(); j++) {
                if (searchKey.charAt(i - 1) == element.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                //first term is for deletion, second term is for insertion
                distance[i][j] = Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1);
                //first term is the min from above, second term is for substitution
                distance[i][j] = Math.min(distance[i][j], distance[i - 1][j - 1] + cost);
                if (i > 1 && j > 1 && searchKey.charAt(i - 1) == element.charAt(j - 2) && searchKey.charAt(i - 2) == element.charAt(j - 1)) {
                    //first term is from above comparison, second term is for transposition
                    distance[i][j] = Math.min(distance[i][j], distance[i - 2][j - 2] + cost);
                }
            }
        }
        return distance[searchKey.length()][element.length()];
    }

    public static boolean match(String searchKey, String element) {
        for (int i = 0; i < searchKey.length(); i++) {
            char ch = searchKey.charAt(i);
            if (element.contains(Character.toString(ch))) {
                return true;
            }
        }
        return false;
    }

    public void action(ArrayList<String> inputList, User searchedUser) {
        Scanner sc = new Scanner(System.in);
        boolean exit2 = false;

        while (!exit2) {
            System.out.println("Action: ");
            System.out.println("1.View profile");
            System.out.println("2.Add friend");
            System.out.println("3.Remove friend");
            System.out.println("4.Chat");
            System.out.println("5.Exit");
            System.out.print("Enter your choice: ");
            int choice2 = sc.nextInt();
            sc.nextLine();
            switch (choice2) {
                case 1:
                    userAccess.viewAccount(searchedUser);
                    break;
                case 2:
                    friendManager.sendFriendRequest(searchedUser);
                    break;
                case 3:
                    friendManager.deleteFriend(searchedUser);
                    break;
                case 4:
                    //chat
                    break;
                case 5:
                    exit2 = true;
                    System.out.println("Exit successfully");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }

    }
}
