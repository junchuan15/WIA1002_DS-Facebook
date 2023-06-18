/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class PostManager {

    private User loggedinUser;
    private DatabaseSQL database;
    Scanner sc = new Scanner(System.in);

    public PostManager() {
    }

    public PostManager(User loggedinUser) {
        this.loggedinUser = loggedinUser;
        database = new DatabaseSQL();
    }

    public void uploadPost() {
        System.out.println("What's on your mind, " + loggedinUser.getUsername() + "?");
        String content = sc.nextLine();
        boolean containsBadWords = BadWordsChecker(content);
        while (containsBadWords) {
            System.out.println("Your post contains profane words. Please rephrase.");
            content = sc.nextLine();
            containsBadWords = BadWordsChecker(content);
        }
        String choice;
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Do you want to include media (Y/N)? ");
            choice = sc.nextLine();
            if (choice.equalsIgnoreCase("Y")) {
                System.out.print("Enter media path: ");
                String mediaPath = sc.nextLine();
                if (mediaPath != null && !mediaPath.isEmpty()) {
                    Path sourcePath = Paths.get(mediaPath);
                    if (Files.exists(sourcePath)) {
                        try {
                            Path destinationDirectory = Paths.get("media", String.valueOf(loggedinUser.getAccountID()), sourcePath.getFileName().toString());
                            Files.createDirectories(destinationDirectory);

                            Path destinationPath = destinationDirectory.resolve(sourcePath.getFileName());
                            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                            String mediaFilePath = destinationPath.toString();
                            createPost(content, mediaFilePath);
                            validChoice = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Media file not found.");
                    }
                }
            } else if (choice.equalsIgnoreCase("N")) {
                createPost(content, null);
                validChoice = true;
            } else {
                System.out.println("Invalid choice. Please enter 'Y' or 'N'.");
            }
        }
    }

    private void createPost(String content, String mediaPath) {
        Scanner scanner = new Scanner(System.in);

        boolean validChoice = false;
        Post.Status status = null;

        while (!validChoice) {
            System.out.println("Choose post status:");
            System.out.println("1. Public");
            System.out.println("2. Private");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    status = Post.Status.PUBLIC;
                    validChoice = true;
                    break;
                case 2:
                    status = Post.Status.PRIVATE;
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        Post post;
        if (mediaPath != null && !mediaPath.isEmpty()) {
            post = new Post.PostBuilder(LocalDateTime.now(), database.getAutoIncrementID(), loggedinUser.getAccountID(), content, mediaPath, status)
                    .setLikes(0)
                    .setComments(0)
                    .build();
        } else {
            post = new Post.PostBuilder(LocalDateTime.now(), database.getAutoIncrementID(), loggedinUser.getAccountID(), content, status)
                    .setLikes(0)
                    .setComments(0)
                    .build();
        }

        loggedinUser.addPost(post);
        database.uploadPost(post);
        performAction("PostID:" + String.valueOf(post.getPostID()) + " Created A Post");
        System.out.println("Post created successfully.");
    }

    public boolean BadWordsChecker(String content) {
        try ( BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Asus\\Documents\\NetBeansProjects\\Y1S2_Assignment\\profane_words.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String profaneWord = line.trim();
                if (content.toLowerCase().contains(profaneWord.toLowerCase())) {
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Menu in user interface
    public void PostMenu() throws SQLException {
        boolean quit = false;
        while (!quit) {
            System.out.println("==============================================");
            System.out.println("                 POST MENU                    ");
            System.out.println("==============================================");
            System.out.println("             1. Create Post");
            System.out.println("             2. View My Posts");
            System.out.println("             3. View History");
            System.out.println("             4. Explore Public Posts");
            System.out.println("             5. Back to Main Menu");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");
            int select = sc.nextInt();
            System.out.println("==============================================");
            sc.nextLine();

            switch (select) {
                case 1:
                    uploadPost();
                    break;
                case 2:
                    LinkedList<Post> posts = database.getPosts(loggedinUser.getAccountID());
                    if (posts.isEmpty()) {
                        System.out.println("You have no posts.");
                    } else {
                        viewUserPosts(posts);
                    }
                    break;
                case 3:
                    traceBack();
                    break;
                case 4:
                    exploreFeed();
                    break;
                case 5:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void viewUserPosts(LinkedList<Post> posts) throws SQLException {
        int currentIndex = posts.size() - 1;
        boolean exit = false;

        while (!exit) {
            Post currentPost = posts.get(currentIndex);
            printPost(currentPost);
            if (currentPost.getMediaPath() != null && !currentPost.getMediaPath().isEmpty()) {
                viewMedia(currentPost.getMediaPath());
            }
            System.out.println("     -----------------------------------");
            System.out.println("==============================================");
            System.out.println("Action : ");
            System.out.println("1. Next Post");
            System.out.println("2. Previous Post");
            System.out.println("3. Delete Post");
            System.out.println("4. Like Post");
            System.out.println("5. Unlike Post");
            System.out.println("6. Comment on Post");
            System.out.println("7. View Likes");
            System.out.println("8. View Comments");
            System.out.println("9. Back to POST MENU");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println("==============================================");
            switch (choice) {
                case 1:
                    currentIndex--;
                    if (currentIndex < 0) {
                        System.out.println("End of posts.");
                        exit = true;
                    }
                    break;
                case 2:
                    currentIndex++;
                    if (currentIndex >= posts.size()) {
                        System.out.println("Start of posts.");
                        exit = true;
                    }
                    break;
                case 3:
                    deletePost(currentPost);
                    break;
                case 4:
                    likePost(currentPost, loggedinUser);
                    break;
                case 5:
                    unlikePost(currentPost, loggedinUser);
                    break;
                case 6:
                    commentPost(currentPost, loggedinUser);
                    break;
                case 7:
                    viewLikes(currentPost);
                    break;
                case 8:
                    viewComments(currentPost);
                    break;
                case 9:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void deletePost(Post post) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Are you sure you want to delete this post? [Enter Y to delete/N to go back]: ");
        String choice = sc.nextLine();

        while (true) {
            if (choice.equalsIgnoreCase("Y")) {
                database.deletePost(post);
                System.out.println("Post deleted successfully.");
                loggedinUser.getPosts().remove(post);
                break;
            } else if (choice.equalsIgnoreCase("N")) {
                System.out.println("Returning to post.");
                break;
            } else {
                System.out.print("Invalid input. Please enter Y to delete or N to go back: ");
                choice = sc.nextLine();
            }
        }
    }

    public void printPost(Post post) {
        System.out.println("     -----------------------------------");
        User user = database.getUser("Account_ID", post.getAccountID());
        System.out.println("     " + post.getTimeStamp());
        System.out.println("     " + "\u001B[1m" + user.getUsername() + "\u001B[0m");
        System.out.println("     <" + post.getStatusAsString() + ">");
        System.out.println("     ===================================");
        System.out.println("     " + post.getContent());
        System.out.println("     -----------------------------------");
        System.out.println("     👍 " + post.getLikes() + " likes\t\t💬 " + post.getComments() + " comments");
        System.out.println("     ***********************************");
    }

    // View post for friends
    public void viewPost(User user) throws SQLException {
        LinkedList<Post> posts = new LinkedList<>();
        posts = database.getPosts(user.getAccountID());

        if (posts.isEmpty()) {
            System.out.println(user.getUsername() + " has no posts.");
        } else {
            int currentIndex = posts.size() - 1;
            boolean exit = false;

            while (!exit) {
                Post currentPost = posts.get(currentIndex);

                if (currentPost.getStatusAsString().equalsIgnoreCase("PRIVATE") && !user.getFriends().contains(loggedinUser.getUsername())) {
                    currentIndex--;

                    if (currentIndex < 0) {
                        System.out.println("End of posts.");
                        exit = true;
                    }

                    continue;
                }
                printPost(currentPost);
                if (currentPost.getMediaPath() != null && !currentPost.getMediaPath().isEmpty()) {
                    viewMedia(currentPost.getMediaPath());
                }
                performAction("PostID:" + String.valueOf(currentPost.getPostID()) + " Viewed Post by " + database.getUser("Account_ID", currentPost.getAccountID()).getUsername());
                System.out.println("     -----------------------------------");
                System.out.println("==============================================");
                System.out.println("Action : ");
                System.out.println("1. Next Post");
                System.out.println("2. Previous Post");
                System.out.println("3. Like Post");
                System.out.println("4. Unlike Post");
                System.out.println("5. Comment on Post");
                System.out.println("6. View Likes");
                System.out.println("7. View Comments");
                System.out.println("8. Report Post");
                System.out.println("9. Back");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();
                System.out.println("==============================================");
                switch (choice) {
                    case 1:
                        currentIndex--;
                        if (currentIndex < 0) {
                            System.out.println("End of posts.");
                            exit = true;
                        }
                        break;
                    case 2:
                        currentIndex++;
                        if (currentIndex >= posts.size()) {
                            System.out.println("Start of posts.");
                            exit = true;
                        }
                        break;
                    case 3:
                        likePost(currentPost, user);
                        break;
                    case 4:
                        unlikePost(currentPost, user);
                        break;
                    case 5:
                        commentPost(currentPost, user);
                        break;
                    case 6:
                        viewLikes(currentPost);
                        break;
                    case 7:
                        viewComments(currentPost);
                        break;
                    case 8:
                        reportPost(currentPost, user);
                        break;
                    case 9:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }

    public void viewMedia(String mediaPath) {
        if (Desktop.isDesktopSupported()) {
            File mediaFile = new File(mediaPath);

            if (mediaFile.exists() && mediaFile.isFile()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(mediaFile);
                } catch (IOException e) {
                    System.err.println("Failed to open the media file: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid media file path or file does not exist.");
            }
        } else {
            System.out.println("Desktop is not supported.");
        }
    }

    public void likePost(Post post, User user) {
        ArrayList<String> likeList = database.getList(post, "LikeList");
        if (!likeList.contains(loggedinUser.getUsername())) {
            post.setLikes(post.getLikes() + 1);
            likeList.add(loggedinUser.getUsername());
            database.updatePost(post, "LikeList", likeList);
            database.updatePost(post, "Num_Likes", post.getLikes());
            System.out.println("Post liked successfully.");
            performAction("PostID:" + String.valueOf(post.getPostID()) + " Liked Post by " + user.getUsername());
        } else {
            System.out.println("You have already liked this post.");
        }
    }

    public void unlikePost(Post post, User user) {
        ArrayList<String> likeList = database.getList(post, "LikeList");
        if (likeList.contains(loggedinUser.getUsername())) {
            post.setLikes(post.getLikes() - 1);
            likeList.remove(loggedinUser);
            database.updatePost(post, "LikeList", likeList);
            database.updatePost(post, "Num_Likes", post.getLikes());
            System.out.println("Post unliked successfully.");
            performAction("PostID:" + String.valueOf(post.getPostID()) + " Unliked Post by " + user.getUsername());
        } else {
            System.out.println("You haven't liked this post.");
        }
    }

    public void commentPost(Post post, User user) {
        System.out.println("Write a comment.........");
        String comment = "";
        try {
            comment = sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid comment.");
        }
        post.setComments(post.getComments() + 1);
        String commentString = loggedinUser.getUsername() + ":" + comment;
        ArrayList<String> commentList = database.getList(post, "CommentList");
        commentList.add(commentString);
        performAction("PostID:" + String.valueOf(post.getPostID()) + " Comment '" + comment + "' on Post by " + user.getUsername());
        database.updatePost(post, "CommentList", commentList);
        database.updatePost(post, "Num_Comments", post.getComments());
    }

    public void reportPost(Post post, User user) {
        System.out.print("Enter the reason for reporting this post: ");
        String reason = "";
        while (reason.isEmpty()) {
            reason = sc.nextLine().trim();

            if (reason.isEmpty()) {
                System.out.println("Reason cannot be empty. Please try again.");
            }
        }
        String reportString = loggedinUser.getUsername() + ":" + reason;
        ArrayList<String> reportList = database.getList(post, "Report");
        reportList.add(reportString);
        performAction("PostID:" + String.valueOf(post.getPostID()) + " Report '" + reason + "' Post by " + user.getUsername());
        database.updatePost(post, "Report", reportList);
        System.out.println("Post reported successfully.");
    }

    public void viewLikes(Post post) {
        ArrayList<String> likeList = database.getList(post, "LikeList");
        System.out.println("< 👍 " + post.getLikes() + " likes>");
        System.out.println("==============================================");
        System.out.println("Liked by");

        if (likeList.isEmpty()) {
            System.out.println("No likes found.");
        } else {
            int count = 1;
            for (String username : likeList) {
                System.out.println(count + ". " + username);
                count++;
            }
            performAction("PostID:" + String.valueOf(post.getPostID()) + " Viewed Likes On Post by " + database.getUser("Account_ID", post.getAccountID()).getUsername());
        }
    }

    public void viewComments(Post post) {
        ArrayList<String> commentList = database.getList(post, "CommentList");
        System.out.println("< 💬 " + post.getComments() + " comments>");
        System.out.println("==============================================");
        if (commentList.isEmpty()) {
            System.out.println("No comments found.");
        } else {
            int count = 1;
            for (String username : commentList) {
                String[] commentInfo = username.split(":");
                if (commentInfo.length >= 2) {
                    System.out.println(count + ". " + commentInfo[0] + ": " + commentInfo[1]);
                } else {
                    System.out.println(count + ". Invalid comment format");
                }
                count++;
                performAction("PostID:" + String.valueOf(post.getPostID()) + " Viewed comments On Post by " + database.getUser("Account_ID", post.getAccountID()).getUsername());
            }
        }

    }

    public void exploreFeed() throws SQLException {
        List<Post> posts = database.getAllPosts();
        List<Post> filteredPosts = new ArrayList<>();

        for (Post post : posts) {
            if (post.getAccountID() != loggedinUser.getAccountID() || post.getStatusAsString() != "PRIVATE") {
                filteredPosts.add(post);
            }
        }

        if (filteredPosts.isEmpty()) {
            System.out.println("No posts available in the explore feed.");
        } else {
            int randomIndex = new Random().nextInt(filteredPosts.size());
            Post currentPost = filteredPosts.get(randomIndex);
            User postOwner = database.getUser("Account_ID", currentPost.getAccountID());
            viewPost(postOwner);
        }
    }

    public void performAction(String action) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        String str = timestamp + " - " + action;
        loggedinUser.addHistory(str);
    }

    public void traceBack() throws SQLException {
        if (loggedinUser.getHistory() == null) {
            System.out.println("Action history is not available.");
            return;
        }

        System.out.println("Action History:");
        if (loggedinUser.getHistory().isEmpty()) {
            System.out.println("No actions found.");
            return;
        }

        for (int i = loggedinUser.getHistory().size() - 1; i >= 0; i--) {
            String action = loggedinUser.getHistory().get(i);
            System.out.println((loggedinUser.getHistory().size() - i) + ". " + action);
        }

        System.out.print("Enter the number to trace back the corresponding action: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > loggedinUser.getHistory().size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String action = loggedinUser.getHistory().get(loggedinUser.getHistory().size() - choice);
        String[] parts = action.split(" - ");
        if (parts.length < 2) {
            System.out.println("Invalid action format.");
            return;
        }

        String actionString = parts[1];
        String[] actionParts = actionString.split(" ");
        String username = actionParts[actionParts.length - 1];
        if (actionParts.length < 2) {
            System.out.println("Invalid action format.");
            return;
        }

        int postID;
        String[] part = actionParts[0].split(":");
        String postIDString = part[1].trim();
        try {
            postID = Integer.parseInt(postIDString);
        } catch (NumberFormatException e) {
            System.out.println("Invalid post ID.");
            return;
        }

        User user = database.getUser("UserName", username);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        LinkedList<Post> posts = database.getPosts(user.getAccountID());
        if (posts == null) {
            System.out.println("Failed to retrieve user posts.");
            return;
        }

        for (Post post : posts) {
            if (post.getPostID() == postID) {
                try {
                    viewUserPosts(posts);
                    return;
                } catch (SQLException e) {
                    System.out.println("Failed to view user posts.");
                    return;
                }
            }
        }

        System.out.println("Post not found in the user's posts.");
    }
}
