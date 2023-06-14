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

            StringBuilder queryBuilder = new StringBuilder("UPDATE usersdata SET ");
            ArrayList<Object> queryParams = new ArrayList<>();

            if (user.getUsername() != null) {
                queryBuilder.append("UserName=?, ");
                queryParams.add(user.getUsername());
            }

            if (user.getEmailAddress() != null) {
                queryBuilder.append("EmailAddress=?, ");
                queryParams.add(user.getEmailAddress());
            }

            if (user.getContactNumber() != null) {
                queryBuilder.append("ContactNumber=?, ");
                queryParams.add(user.getContactNumber());
            }

            if (user.getPassword() != null) {
                queryBuilder.append("Password=?, ");
                queryParams.add(user.getPassword());
            }

            if (user.getRole() != null) {
                queryBuilder.append("Role=?, ");
                queryParams.add(user.getRole());
            }

            if (user.getName() != null) {
                queryBuilder.append("Name=?, ");
                queryParams.add(user.getName());
            }

            if (user.getBirthday() != null) {
                queryBuilder.append("Birthday=?, ");
                queryParams.add(user.getBirthday());
            }

            if (user.getAge() != 0) {
                queryBuilder.append("Age=?, ");
                queryParams.add(user.getAge());
            }

            if (user.getAddress() != null) {
                queryBuilder.append("Address=?, ");
                queryParams.add(user.getAddress());
            }

            if (user.getGender() != null) {
                queryBuilder.append("Gender=?, ");
                queryParams.add(user.getGender());
            }

            if (user.getRelationshipStatus() != null) {
                queryBuilder.append("Relationship_Status=?, ");
                queryParams.add(user.getRelationshipStatus());
            }

            if (user.getHobbies() != null) {
                queryBuilder.append("Hobbies=?, ");
                queryParams.add(joinWithComma(user.getHobbies()));
            }

            if (user.getJobs() != null) {
                queryBuilder.append("Jobs=?, ");
                queryParams.add(joinWithComma(user.getJobs()));
            }

            if (user.getNumberOfFriends() != 0) {
                queryBuilder.append("NumberOfFriends=?, ");
                queryParams.add(user.getNumberOfFriends());
            }

            if (user.getFriends() != null) {
                queryBuilder.append("Friends=?, ");
                queryParams.add(joinWithComma(user.getFriends()));
            }

            if (user.getSentRequests() != null) {
                queryBuilder.append("SentRequests=?, ");
                queryParams.add(joinWithComma(user.getSentRequests()));
            }

            if (user.getReceivedRequests() != null) {
                queryBuilder.append("ReceivedRequests=?, ");
                queryParams.add(joinWithComma(user.getReceivedRequests()));
            }

            queryBuilder.setLength(queryBuilder.length() - 2);

            queryBuilder.append(" WHERE Account_ID=?");
            queryParams.add(user.getAccountID());

            PreparedStatement ps = con.prepareStatement(queryBuilder.toString());
            int parameterIndex = 1;
            for (Object param : queryParams) {
                if (param instanceof Integer) {
                    ps.setInt(parameterIndex++, (Integer) param);
                } else {
                    ps.setString(parameterIndex++, String.valueOf(param));
                }
            }
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String joinWithComma(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < list.size(); i++) {
            String element = list.get(i);
            if (element != null) {
                sb.append(element);
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
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
                        .setNumberOfFriends(rs.getInt("NumberOfFriends"));

                String hobbiesString = rs.getString("Hobbies");
                List<String> hobbies = new ArrayList<>();
                if (hobbiesString != null) {
                    String[] hobbiesArray = hobbiesString.split(",");
                    for (String hobby : hobbiesArray) {
                        if (hobby != null) {
                            hobbies.add(hobby);
                        }
                    }
                }
                builder.setHobbies((ArrayList<String>) hobbies);

                String friendsString = rs.getString("Friends");
                List<String> friends = new ArrayList<>();
                if (friendsString != null) {
                    String[] friendsArray = friendsString.split(",");
                    for (String friend : friendsArray) {
                        if (friend != null) {
                            friends.add(friend);
                        }
                    }
                }
                builder.setFriends((ArrayList<String>) friends);

                String sentRequestsString = rs.getString("SentRequests");
                List<String> sentRequests = new ArrayList<>();
                if (sentRequestsString != null) {
                    String[] sentRequestsArray = sentRequestsString.split(",");
                    for (String request : sentRequestsArray) {
                        if (request != null) {
                            sentRequests.add(request);
                        }
                    }
                }
                builder.setSentRequests((ArrayList<String>) sentRequests);

                String receivedRequestsString = rs.getString("ReceivedRequests");
                List<String> receivedRequests = new ArrayList<>();
                if (receivedRequestsString != null) {
                    String[] receivedRequestsArray = receivedRequestsString.split(",");
                    for (String request : receivedRequestsArray) {
                        if (request != null) {
                            receivedRequests.add(request);
                        }
                    }
                }
                builder.setReceivedRequests((ArrayList<String>) receivedRequests);

                List<String> jobsList = Arrays.asList(rs.getString("Jobs").split(","));
                Stack<String> jobsStack = new Stack<>();
                for (String job : jobsList) {
                    if (job != null) {
                        jobsStack.push(job);
                    }
                }
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

    public ArrayList<User> loadUsers() {
        ArrayList<User> userList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM usersdata");

            while (resultSet.next()) {
               String username = resultSet.getString("UserName");
                User user = getUser("UserName", username);
                userList.add(user);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public ConnectionGraph createGraph() {
        ConnectionGraph graph = new ConnectionGraph();
        ArrayList<User> users = loadUsers();

        for (User user : users) {
            graph.addVertex(user);
            ArrayList<String> friendUsernames = user.getFriends();
            if (friendUsernames != null) {
                for (String friendUsername : friendUsernames) {
                    User friend = null;
                    for (User u : users) {
                        if (u.getUsername().equalsIgnoreCase(friendUsername)) {
                            friend = u;
                            graph.addEdge(user, friend);
                            break;
                        }
                    }
                }
            }
        }
        return graph;
    }
    
     public String getRandomUsername(String loggedInUsername) {
        String randomUsername = null;

        try {
         Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            Statement statement = connection.createStatement();
            String countQuery = "SELECT COUNT(*) AS total FROM usersdata WHERE username != ?";
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            countStatement.setString(1, loggedInUsername);
            ResultSet countResult = countStatement.executeQuery();

            int totalUsernames = 0;
            if (countResult.next()) {
                totalUsernames = countResult.getInt("total");
            }

            // Generate a random index within the range of the total number of usernames
            int randomIndex = (int) (Math.random() * totalUsernames) + 1;

            // Query the SQL database to retrieve the username at the random index, excluding the logged-in user
            String usernameQuery = "SELECT username FROM usersdata WHERE username != ? LIMIT ?, 1";
            PreparedStatement usernameStatement = connection.prepareStatement(usernameQuery);
            usernameStatement.setString(1, loggedInUsername);
            usernameStatement.setInt(2, randomIndex - 1);
            ResultSet usernameResult = usernameStatement.executeQuery();

            if (usernameResult.next()) {
                randomUsername = usernameResult.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return randomUsername;
    }
     
}
