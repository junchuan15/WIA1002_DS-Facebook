/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class PostManager {

    private User loggedinUser;
    private DatabaseSQL database;
    Scanner sc = new Scanner(System.in);

    public PostManager(User loggedinUser) {
        this.loggedinUser = loggedinUser;
        database = new DatabaseSQL();
    }

    public void uploadPost() {
        System.out.println("==============================================\nWhat's on your mind, " + loggedinUser.getUsername() + "?");
        String content = sc.nextLine();

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
        System.out.println("Post created successfully.");
    }

    // Menu in user interface
    public void PostMenu() throws SQLException {
        boolean quit = false;
        while (!quit) {
            System.out.println("==============================================\nPOST MENU");
            System.out.println("1. Upload A Post");
            System.out.println("2. View My Post");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int select = sc.nextInt();
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
                        int currentIndex = 0;
                        boolean exit = false;

                        while (!exit) {
                            Post currentPost = posts.get(currentIndex);
                            printPost(currentPost);
                            if (currentPost.getMediaPath() != null && !currentPost.getMediaPath().isEmpty()) {
                                viewMedia(currentPost.getMediaPath());
                            }
                            System.out.println("-----------------------------------");
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

                            switch (choice) {
                                case 1:
                                    currentIndex++;
                                    if (currentIndex >= posts.size()) {
                                        System.out.println("End of posts.");
                                        exit = true;
                                    }
                                    break;
                                case 2:
                                    currentIndex--;
                                    if (currentIndex < 0) {
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
                    break;
                case 3:
                    quit = true;
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
        User user = database.getUser("Account_ID", post.getAccountID());
        System.out.println(post.getTimeStamp());
        System.out.println("\u001B[1m" + user.getUsername() + "\u001B[0m");
        System.out.println("<" + post.getStatusAsString() + ">");
        System.out.println("===================================\n");
        System.out.println(post.getContent());
        System.out.println();
        System.out.println("-----------------------------------");
        System.out.println("👍 " + post.getLikes() + " likes\t\t💬 " + post.getComments() + " comments");
        System.out.println("***********************************");
    }

    // View post for friends
    public void viewPost(User user) throws SQLException {
        LinkedList<Post> posts = new LinkedList<>();
        posts = database.getPosts(user.getAccountID());

        if (posts.isEmpty()) {
            System.out.println(user.getUsername() + " has no post.");
        } else {
            int currentIndex = 0;
            boolean exit = false;

            while (!exit) {
                Post currentPost = posts.get(currentIndex);
                printPost(currentPost);
                if (currentPost.getMediaPath() != null && !currentPost.getMediaPath().isEmpty()) {
                    viewMedia(currentPost.getMediaPath());
                }
                System.out.println("-----------------------------------");
                System.out.println("1. Next Post");
                System.out.println("2. Previous Post");
                System.out.println("3. Like Post");
                System.out.println("4. Unlike Post");
                System.out.println("5. Comment on Post");
                System.out.println("6. View Likes");
                System.out.println("7. View Comments");
                System.out.println("8. Back");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        currentIndex++;
                        if (currentIndex >= posts.size()) {
                            System.out.println("End of posts.");
                            exit = true;
                        }
                        break;
                    case 2:
                        currentIndex--;
                        if (currentIndex < 0) {
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
        post.setLikes(post.getLikes() + 1);
        ArrayList<String> likeList = database.getList(post, "LikeList");
        likeList.add(user.getUsername());
        database.updatePost(post, "LikeList", likeList);
        database.updatePost(post, "Num_Likes", post.getLikes());
    }

    public void unlikePost(Post post, User user) {
        post.setLikes(post.getLikes() - 1);
        ArrayList<String> likeList = database.getList(post, "LikeList");
        likeList.remove(user.getUsername());
        database.updatePost(post, "LikeList", likeList);
        database.updatePost(post, "Num_Likes", post.getLikes());
    }

    public void commentPost(Post post, User user) {
        StringBuilder sb = new StringBuilder();
        System.out.println("-------------------------");
        System.out.println("Write a comment.........");
        String comment = sc.nextLine();
        post.setComments(post.getComments() + 1);
        String commentString = user.getUsername() + ":" + comment;
        ArrayList<String> commentList = database.getList(post, "CommentList");
        commentList.add(commentString);
        database.updatePost(post, "CommentList", commentList);
        database.updatePost(post, "Num_Comments", post.getComments());
    }

    public void viewLikes(Post post) {
        ArrayList<String> likeList = database.getList(post, "LikeList");
        System.out.println("< 👍 " + post.getLikes() + " likes>");
        System.out.println("-------------------------");
        System.out.println("Liked by");
        int count = 1;
        for (String username : likeList) {
            System.out.println(count + ". " + username);
            count++;
        }
        System.out.println("-------------------------");
    }

    public void viewComments(Post post) {
        ArrayList<String> commentList = database.getList(post, "CommentList");
        System.out.println("< 💬 " + post.getComments() + " comments>");
        System.out.println("-------------------------");
        int count = 1;
        for (String username : commentList) {
            String[] commentInfo = username.split(":");
            System.out.println(count + ". " + commentInfo[0] + ": " + commentInfo[1]);
            count++;
        }
        System.out.println("-------------------------");
    }
}
