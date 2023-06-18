/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import com.mysql.cj.Messages;
import java.sql.*;
import java.util.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
            // createTableIfNotExists(con);
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Need to update as debug
    private void createTableIfNotExists(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS usersdata (" +
                "Account_ID VARCHAR(10) PRIMARY KEY," +
                "UserName VARCHAR(50) NOT NULL," +
                "EmailAddress VARCHAR(50) NOT NULL," +
                "ContactNumber VARCHAR(20) NOT NULL," +
                "Password VARCHAR(255) NOT NULL," +
                "Role VARCHAR(20) NOT NULL," +
                // Add more columns as needed
                ")";
        stmt.executeUpdate(createTableQuery);
        stmt.close();
    }*/
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

            if (user.getHobbies() != null && !user.getHobbies().isEmpty()) {
                queryBuilder.append("Hobbies=?, ");
                queryParams.add(joinWithComma(user.getHobbies()));
            }

            if (user.getJobs() != null && !user.getJobs().isEmpty()) {
                queryBuilder.append("Jobs=?, ");
                queryParams.add(joinWithComma(user.getJobs()));
            }

            if (user.getNumberOfFriends() != 0) {
                queryBuilder.append("NumberOfFriends=?, ");
                queryParams.add(user.getNumberOfFriends());
            }

            if (user.getFriends() != null && !user.getFriends().isEmpty()) {
                queryBuilder.append("Friends=?, ");
                queryParams.add(joinWithComma(user.getFriends()));
            }

            if (user.getSentRequests() != null && !user.getSentRequests().isEmpty()) {
                queryBuilder.append("SentRequests=?, ");
                queryParams.add(joinWithComma(user.getSentRequests()));
            }

            if (user.getReceivedRequests() != null && !user.getReceivedRequests().isEmpty()) {
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
        boolean isFirst = true;
        for (String element : list) {
            if (element != null && !element.isEmpty()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(",");
                }
                sb.append(element);
            }
        }
        return sb.toString();
    }

    public void readFromTable(ArrayList<String> accountIDList, ArrayList<String> usernameList, ArrayList<String> emailList, ArrayList<String> contactNumberList, ArrayList<String> nameList, User user) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
            String query = "SELECT * FROM usersdata WHERE Account_ID <> '" + user.getAccountID() + "'";
            ResultSet rs = statement.executeQuery(query);

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
            String deleteUserDataQuery = "DELETE FROM usersdata WHERE Account_ID = ?";
            try ( PreparedStatement userDataStatement = connection.prepareStatement(deleteUserDataQuery)) {
                userDataStatement.setString(1, user.getAccountID());
                int userDataRowsAffected = userDataStatement.executeUpdate();
            }

            String deleteUserPostsQuery = "DELETE FROM userspost WHERE Account_ID = ?";
            try ( PreparedStatement userPostsStatement = connection.prepareStatement(deleteUserPostsQuery)) {
                userPostsStatement.setString(1, user.getAccountID());
                int userPostsRowsAffected = userPostsStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while deleting the user and posts from the database: " + e.getMessage());
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

    public int getAutoIncrementID() {
        int autoIncrementID = 1;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String query = "SELECT post_ID FROM userspost ORDER BY post_ID DESC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                autoIncrementID = resultSet.getInt("post_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autoIncrementID;
    }

    public void uploadPost(Post post) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String query = "INSERT INTO userspost (PostTime, Account_ID, Content, MediaPath, Status, Num_Likes, Num_Comments) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(post.getTimeStamp()));
            preparedStatement.setString(2, post.getAccountID());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setString(4, post.getMediaPath());
            preparedStatement.setString(5, post.getStatusAsString());
            preparedStatement.setInt(6, post.getLikes());
            preparedStatement.setInt(7, post.getComments());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePost(Post post) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String query = "DELETE FROM userspost WHERE Post_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, post.getPostID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Post> getPosts(String accountID) throws SQLException {
        LinkedList<Post> posts = new LinkedList<>();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String query = "SELECT * FROM userspost WHERE Account_ID=?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, accountID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                LocalDateTime timeStamp = rs.getTimestamp("PostTime").toLocalDateTime();
                int postID = rs.getInt("Post_ID");
                String content = rs.getString("Content");
                String mediaPath = rs.getString("MediaPath");
                String statusString = rs.getString("status");
                Post.Status status = Post.Status.valueOf(statusString);
                int likes = rs.getInt("Num_Likes");
                int comments = rs.getInt("Num_Comments");

                Post.PostBuilder postBuilder;
                if (mediaPath != null && !mediaPath.isEmpty()) {
                    postBuilder = new Post.PostBuilder(timeStamp, postID, accountID, content, mediaPath, status);
                } else {
                    postBuilder = new Post.PostBuilder(timeStamp, postID, accountID, content, status);
                }

                Post post = postBuilder
                        .setLikes(likes)
                        .setComments(comments)
                        .build();

                posts.add(post);
            }

            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public ArrayList<String> getList(Post post, String attribute) {
        ArrayList<String> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            statement = connection.prepareStatement("SELECT * FROM userspost WHERE Post_Id = ?");
            statement.setInt(1, post.getPostID());
            rs = statement.executeQuery();

            if (rs.next()) {
                String listString = rs.getString(attribute);
                if (listString != null) {
                    String[] arrayString = listString.split(",");
                    for (String str : arrayString) {
                        if (str != null && !str.isEmpty()) {
                            list.add(str);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public void updatePost(Post post, String attribute, Object value) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            statement = connection.prepareStatement("UPDATE userspost SET " + attribute + "=? WHERE Post_Id=?");

            if (value instanceof String) {
                statement.setString(1, (String) value);
            } else if (value instanceof Integer) {
                statement.setInt(1, (Integer) value);
            } else if (value instanceof ArrayList<?>) {
                ArrayList<String> list = (ArrayList<String>) value;
                statement.setString(1, String.join(",", list));
            }

            statement.setInt(2, post.getPostID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            String query = "SELECT * FROM userspost";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                LocalDateTime timeStamp = rs.getTimestamp("PostTime").toLocalDateTime();
                int postID = rs.getInt("Post_ID");
                String accountID = rs.getString("Account_ID");
                String content = rs.getString("Content");
                String mediaPath = rs.getString("MediaPath");
                String statusString = rs.getString("status");
                Post.Status status = Post.Status.valueOf(statusString);
                int likes = rs.getInt("Num_Likes");
                int comments = rs.getInt("Num_Comments");

                Post.PostBuilder postBuilder;
                if (mediaPath != null && !mediaPath.isEmpty()) {
                    postBuilder = new Post.PostBuilder(timeStamp, postID, accountID, content, mediaPath, status);
                } else {
                    postBuilder = new Post.PostBuilder(timeStamp, postID, accountID, content, status);
                }

                Post post = postBuilder
                        .setLikes(likes)
                        .setComments(comments)
                        .build();

                posts.add(post);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public void insertUserSalt(String accountID, String salt) throws SQLException {
        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("INSERT INTO userspassword (Account_ID, Salt) VALUES (?, ?)");
            ps.setString(1, accountID);
            ps.setString(2, salt);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String retrieveUserSalt(String accountID) throws SQLException {
        String salt = null;

        try {
            connectAndFetchData();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "Facebook123!");
            PreparedStatement ps = con.prepareStatement("SELECT Salt FROM userspassword WHERE Account_ID = ?");
            ps.setString(1, accountID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                salt = rs.getString("Salt");
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salt;
    }

}
