/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
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
    private Scanner sc;

    public SearchEngine(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.accountIDList = new ArrayList<>();
        this.usernameList = new ArrayList<>();
        this.emailList = new ArrayList<>();
        this.contactNumberList = new ArrayList<>();
        this.nameList = new ArrayList<>();
        this.friendManager = new Friend(loggedInUser);
        this.sc = new Scanner(System.in);
        this.database.readFromTable(accountIDList, usernameList, emailList, contactNumberList, nameList, loggedInUser);
    }

    public void searchUsers() {
        boolean exit = false;

        while (!exit) {
            System.out.println("==============================================");
            System.out.println("                 SEARCH                        ");
            System.out.println("==============================================");
            System.out.println("          1. By account ID");
            System.out.println("          2. By username");
            System.out.println("          3. By email");
            System.out.println("          4. By contact number");
            System.out.println("          5. By name");
            System.out.println("          6. Back to Main Menu");
            System.out.println("==============================================");
            System.out.print("        Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();
                System.out.println("==============================================");
                switch (choice) {
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
                        exit = true;
                        System.out.println("Returning to Main Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public void searchField(ArrayList<String> inputList, String attribute) {
        String search;

        do {
            System.out.print("Search " + attribute + ": ");
            search = sc.nextLine();
            System.out.println("==============================================");
            if (search.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (search.isEmpty());

        ArrayList<String> sortedList = display(inputList, search);

        for (int i = 0; i < sortedList.size(); i++) {
            System.out.println((i + 1) + ". " + sortedList.get(i));
        }

        int selectedIndex = 0;

        do {
            System.out.println("==============================================");
            System.out.print("Enter the index of the " + attribute + " you want to find: ");
            String input = sc.nextLine();

            try {
                selectedIndex = Integer.parseInt(input);
                if (selectedIndex >= 1 && selectedIndex <= sortedList.size()) {
                    break;
                } else {
                    System.out.println("Invalid index! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid index.");
            }
        } while (true);

        String selectedValue = sortedList.get(selectedIndex - 1);

        try {
            User searchedUser = database.getUser(attribute, selectedValue);

            if (searchedUser != null) {
                friendManager.action(searchedUser);
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving the user from the database.");
        }
    }

    public ArrayList<String> display(ArrayList<String> inputList, String searchKey) {
        ArrayList<ArrayList<String>> similarity = new ArrayList<>();
        ArrayList<String> row1 = new ArrayList<>(inputList);
        ArrayList<String> row2 = new ArrayList<>();

        for (int i = 0; i < row1.size(); i++) {
            String temp = Double.toString(DamerauLevenshteinDistance(searchKey, row1.get(i)));
            row2.add(temp);
        }

        for (int i = 0; i < row1.size(); i++) {
            ArrayList<String> row = new ArrayList<>();
            row.add(row1.get(i));
            row.add(row2.get(i));
            similarity.add(row);
        }

        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> row1, ArrayList<String> row2) {
                Double distance1 = Double.parseDouble(row1.get(1));
                Double distance2 = Double.parseDouble(row2.get(1));
                return distance1.compareTo(distance2);
            }
        };

        Collections.sort(similarity, comparator);

        ArrayList<String> sortedList = new ArrayList<>();

        if (similarity.size() >= 10) {
            for (int i = 0; i < 10; i++) {
                String username = similarity.get(i).get(0);
                if (!username.equals(loggedInUser.getUsername())) {
                    sortedList.add(username);
                }
            }
        } else if (similarity.size() > 0 && similarity.size() < 10) {
            for (int i = 0; i < similarity.size(); i++) {
                String username = similarity.get(i).get(0);
                if (!username.equals(loggedInUser.getUsername())) {
                    sortedList.add(username);
                }
            }
        }

        return sortedList;
    }

    public double DamerauLevenshteinDistance(String searchKey, String element) {
        if (element.equals(searchKey)) {
            return 0;
        } else if (element.startsWith(searchKey) || element.endsWith(searchKey)) {
            return 0.1;
        } else if (match(searchKey, element)) {
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
            if (length >= middle) {
                if (length <= middle + middle / 2) {
                    return 0.3;
                } else if (length <= searchKey.length()) {
                    return 0.2;
                }
            }

            return formula(searchKey, element);
        }

        return formula(searchKey, element);
    }

    public int formula(String searchKey, String element) {
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

    public boolean match(String searchKey, String element) {
        for (int i = 0; i < searchKey.length(); i++) {
            char ch = searchKey.charAt(i);
            if (element.contains(Character.toString(ch))) {
                return true;
            }
        }
        return false;
    }
}
