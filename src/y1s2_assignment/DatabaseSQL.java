/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import com.mysql.cj.Messages;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Asus
 */
public class DatabaseSQL {

    private String url = "jdbc:mysql://localhost:3306/users";
    private String username = "root";
    private String password = "Facebook123!";

    public void connectAndFetchData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String generateAccountID(String role) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            Statement stmt = con.createStatement();
            String roleCondition = (role.equals("User")) ? "NOT LIKE 'TFBAM%'" : "LIKE 'TFBAM%'";
            ResultSet rs = stmt.executeQuery("SELECT MAX(Account_ID) FROM usersdata WHERE Account_ID " + roleCondition);
            if (rs.next()) {
                String lastAccountID = rs.getString(1);
                String accountID;

                if (role.equals("User")) {
                    if (lastAccountID == null) {
                        return "00001";
                    } else {
                        int lastUserID = Integer.parseInt(lastAccountID.substring(1));
                        accountID = String.format("%05d", lastUserID + 1);
                    }
                } else {
                    if (lastAccountID == null) {
                        return "TFBAM1";
                    } else {
                        int lastAdminID = Integer.parseInt(lastAccountID.substring(5));
                        accountID = "TFBAM" + (lastAdminID + 1);
                    }
                }

                return accountID;
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExist(String check) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usersdata WHERE UserName = ? OR EmailAddress = ? OR ContactNumber = ? OR Account_ID = ?");
            ps.setString(1, check);
            ps.setString(2, check);
            ps.setString(3, check);
            ps.setString(4, check);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rs.close();
                ps.close();
                con.close();
                return true;
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void registerUser(User user) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("INSERT INTO usersdata (Account_ID, UserName, EmailAddress, ContactNumber, Password, Role) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getAccountID());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmailAddress());
            ps.setString(4, user.getContactNumber());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserLogin(String loginId) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usersdata WHERE  EmailAddress = ? OR ContactNumber = ?");
            ps.setString(1, loginId);
            ps.setString(2, loginId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User.Builder builder = new User.Builder()
                        .setAccountID(rs.getString("Account_ID"))
                        .setUsername(rs.getString("UserName"))
                        .setEmailAddress(rs.getString("EmailAddress"))
                        .setContactNumber(rs.getString("ContactNumber"))
                        .setPassword(rs.getString("Password"))
                        .setRole(rs.getString("Role"))
                        .setName(rs.getString("Name"))
                        .setBirthday(rs.getString("Birthday"))
                        .setAge(rs.getInt("Age"))
                        .setAddress(rs.getString("Address"))
                        .setGender(rs.getString("Gender"))
                        .setRelationshipStatus(rs.getString("Relationship_Status"))
                        .setNumberOfFriends(rs.getInt("NumberOfFriends"));

                String hobbiesString = rs.getString("Hobbies");
                ArrayList<String> hobbies = new ArrayList<>();
                if (hobbiesString != null) {
                    hobbies = new ArrayList<>(Arrays.asList(hobbiesString.split(",")));
                }
                builder.setHobbies(hobbies);

                String jobsString = rs.getString("Jobs");
                List<String> jobsList = new ArrayList<>();
                if (jobsString != null) {
                    jobsList = Arrays.asList(jobsString.split(","));
                }
                Stack<String> jobsStack = new Stack<>();
                jobsStack.addAll(jobsList);
                builder.setJobs(jobsStack);

                String friendsString = rs.getString("Friends");
                ArrayList<String> friends = new ArrayList<>();
                if (friendsString != null) {
                    friends = new ArrayList<>(Arrays.asList(friendsString.split(",")));
                }
                builder.setFriends(friends);

                String sentRequestsString = rs.getString("SentRequests");
                ArrayList<String> sentRequests = new ArrayList<>();
                if (sentRequestsString != null) {
                    sentRequests = new ArrayList<>(Arrays.asList(sentRequestsString.split(",")));
                }
                builder.setSentRequests(sentRequests);

                String receivedRequestsString = rs.getString("ReceivedRequests");
                ArrayList<String> receivedRequests = new ArrayList<>();
                if (receivedRequestsString != null) {
                    receivedRequests = new ArrayList<>(Arrays.asList(receivedRequestsString.split(",")));
                }
                builder.setReceivedRequests(receivedRequests);

                rs.close();
                ps.close();
                con.close();

                return builder.build();
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateUserDetail(User user) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET UserName=?, EmailAddress=?, ContactNumber=?, Password=?, Role=?, Name=?, Birthday=?, Age=?, Address=?, Gender=?, Relationship_Status=?, Hobbies=?, Jobs=?, NumberOfFriends=?, Friends=?, SentRequests=?, ReceivedRequests=? WHERE Account_ID=?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmailAddress());
            ps.setString(3, user.getContactNumber());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getName());
            ps.setString(7, user.getBirthday());
            ps.setInt(8, user.getAge());
            ps.setString(9, user.getAddress());
            ps.setString(10, user.getGender());
            ps.setString(11, user.getRelationshipStatus());
            ps.setString(12, String.join(",", user.getHobbies()));
            ps.setString(13, String.join(",", user.getJobs()));
            ps.setInt(14, user.getNumberOfFriends());
            ps.setString(15, String.join(",", user.getFriends()));
            ps.setString(16, String.join(",", user.getSentRequests()));
            ps.setString(17, String.join(",", user.getReceivedRequests()));
            ps.setString(18, user.getAccountID());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditUserDetail(User user, String columnName) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET " + columnName + " = ? WHERE Account_ID = ?");

            switch (columnName) {
                case "UserName":
                    ps.setString(1, user.getUsername());
                    break;
                case "EmailAddress":
                    ps.setString(1, user.getEmailAddress());
                    break;
                case "ContactNumber":
                    ps.setString(1, user.getContactNumber());
                    break;
                case "Password":
                    ps.setString(1, user.getPassword());
                    break;
                case "Name":
                    ps.setString(1, user.getName());
                    break;
                case "Birthday":
                    ps.setString(1, user.getBirthday());
                    break;
                case "Address":
                    ps.setString(1, user.getAddress());
                    break;
                case "Gender":
                    ps.setString(1, user.getGender());
                    break;
                case "Relationship_Status":
                    ps.setString(1, user.getRelationshipStatus());
                    break;
                case "Hobbies":
                    ps.setString(1, String.join(",", user.getHobbies()));
                    break;
                case "Jobs":
                    ps.setString(1, String.join(",", user.getJobs()));
                    break;
                default:
                    System.out.println("Invalid column name: " + columnName);
                    return;
            }

            ps.setString(2, user.getAccountID());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readFromTable(ArrayList<String> accountIDList, ArrayList<String> usernameList, ArrayList<String> emailList, ArrayList<String> contactNumberList, ArrayList<String> nameList) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM usersdata WHERE Role <> 'Admin'");

            while (rs.next()) {
                String accountID = rs.getString("Account_ID");
                String username = rs.getString("UserName");
                String email = rs.getString("EmailAddress");
                String contactNumber = rs.getString("ContactNumber");
                String name = rs.getString("Name");

                accountIDList.add(accountID);
                usernameList.add(username);
                emailList.add(email);
                contactNumberList.add(contactNumber);
                nameList.add(name);
            }

            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String attribute, String value) {
        User user = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String query = "SELECT * FROM usersdata WHERE " + attribute + "=?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, value);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                User.Builder builder = new User.Builder();
                builder.setAccountID(rs.getString("Account_ID"))
                        .setUsername(rs.getString("UserName"))
                        .setEmailAddress(rs.getString("EmailAddress"))
                        .setContactNumber(rs.getString("ContactNumber"))
                        .setPassword(rs.getString("Password"))
                        .setName(rs.getString("Name"))
                        .setBirthday(rs.getString("Birthday"))
                        .setAge(rs.getInt("Age"))
                        .setAddress(rs.getString("Address"))
                        .setGender(rs.getString("Gender"))
                        .setRelationshipStatus(rs.getString("Relationship_Status"))
                        .setNumberOfFriends(rs.getInt("NumberOfFriends"))
                        .setHobbies(new ArrayList<String>(Arrays.asList(rs.getString("Hobbies").split(","))))
                        .setFriends(new ArrayList<String>(Arrays.asList(rs.getString("Friends").split(","))))
                        .setSentRequests(new ArrayList<String>(Arrays.asList(rs.getString("SentRequests").split(","))))
                        .setReceivedRequests(new ArrayList<String>(Arrays.asList(rs.getString("ReceivedRequests").split(","))));

                List<String> jobsList = Arrays.asList(rs.getString("Jobs").split(","));
                Stack<String> jobsStack = new Stack<>();
                jobsStack.addAll(jobsList);
                builder.setJobs(jobsStack);

                user = new User(builder);
            }

            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUserSQL(User user) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String deleteQuery = "DELETE FROM usersdata WHERE Account_ID = ?";
            try ( PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setString(1, user.getAccountID());
                int rowsAffected = statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while deleting the user from the database: " + e.getMessage());
        }
    }

    public void createVerticesFromDatabase() {
        ConnectionGraph graph = new ConnectionGraph();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT UserName FROM usersdata");

            while (rs.next()) {
                String username = rs.getString("UserName");
                graph.addVertex(username);
            }

            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSentRequests(User loggedInUser) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET SentRequests = ? WHERE Account_ID = ?");
            ps.setString(1, String.join(",", loggedInUser.getSentRequests()));
            ps.setString(2, loggedInUser.getAccountID());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReceivedRequests(User user) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET ReceivedRequests = ? WHERE Account_ID = ?");
            ps.setString(1, String.join(",", user.getReceivedRequests()));
            ps.setString(2, user.getAccountID());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNumFriendsAndFriends(User user) {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("UPDATE usersdata SET NumberOfFriends=?, Friends=? WHERE Account_ID=?");
            ps.setInt(1, user.getNumberOfFriends());
            ps.setString(2, String.join(",", user.getFriends()));
            ps.setString(3, user.getAccountID());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getFriendList(String name) {
    try {
        Connection con = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = con.prepareStatement("SELECT Friends FROM usersdata WHERE Username = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String friendList = rs.getString("Friends");
            ps.close();
            con.close();
            return friendList;
        }

        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}
    
     public String getSentRequests(String name) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT SentRequests FROM usersdata WHERE Username = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            String SentRequests = rs.getString("SentRequests");
            ps.close();
            con.close();
            return SentRequests;
        }

        ps.close();
        con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
     
     public String getReceivedRequests(String name) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT SentRequests FROM usersdata WHERE Username = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            String ReceivedRequests = rs.getString("ReceivedRequests");
            ps.close();
            con.close();
            return ReceivedRequests;
        }

        ps.close();
        con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
     
      public int getNumberOfFriends(String name) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT NumberOfFriends FROM usersdata WHERE UserName = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int numberOfFriends = rs.getInt("NumberOfFriends");
                ps.close();
                con.close();
                return numberOfFriends;
            }

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
