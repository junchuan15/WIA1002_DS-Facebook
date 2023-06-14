/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Asus
 */
public class Chat {

    private User loggedInUser;
    private List<ChatEntry> chatEntries;
    private final int pageSize = 3;
    private DatabaseSQL database = new DatabaseSQL();

    public Chat(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.chatEntries = new ArrayList<>();
    }

    public void addMessage(String message) {
        chatEntries.add(new ChatEntry(message, LocalDateTime.now()));
    }

    public void printChat(int page) {
        System.out.println("Chat History:");
        if (chatEntries.isEmpty()) {
            System.out.println("No messages found in the chat history.");
        } else {
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, chatEntries.size());

            if (startIndex >= endIndex) {
                System.out.println("Invalid page number.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = startIndex; i < endIndex; i++) {
                ChatEntry entry = chatEntries.get(i);
                String senderName = entry.getMessage().substring(0, entry.getMessage().indexOf(":") + 1);
                String formattedTimestamp = entry.getTimestamp().format(formatter);
                System.out.println(formattedTimestamp + " " + senderName + " " + entry.getMessage());
            }
        }
    }

    public void saveChatHistory(User friend) {
        try {
            String fileName = generateChatHistoryFileName(friend);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (ChatEntry entry : chatEntries) {
                String formattedTimestamp = entry.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                writer.write(formattedTimestamp + " " + entry.getMessage());
                writer.newLine();
            }
            writer.close();
            System.out.println("Chat history with " + friend.getUsername() + " saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving chat history: " + e.getMessage());
        }
    }

    public void loadChatHistory(User friend) {
        String fileName = generateChatHistoryFileName(friend);
        File file = new File(fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                chatEntries.add(parseChatEntry(line));
            }
            reader.close();
            System.out.println("Chat history with " + friend.getUsername() + " loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading chat history: " + e.getMessage());
        }
    }

    private ChatEntry parseChatEntry(String line) throws DateTimeParseException {
        String[] parts = line.split(" ", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid chat entry format: " + line);
        }

        String timestampStr = parts[0] + " " + parts[1];
        String message = parts[2];

        try {
            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new ChatEntry(message, timestamp);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Error parsing timestamp: " + timestampStr, timestampStr, e.getErrorIndex());
        }
    }

    private String generateChatHistoryFileName(User friend) {
        String loggedUsername = loggedInUser.getUsername();
        String friendUsername = friend.getUsername();

        String[] names = {loggedUsername, friendUsername};
        Comparator<String> nameComparator = (name1, name2) -> {
            String[] parts1 = name1.split("[._]");
            String[] parts2 = name2.split("[._]");
            int minLength = Math.min(parts1.length, parts2.length);
            for (int i = 0; i < minLength; i++) {
                int comparison = parts1[i].compareTo(parts2[i]);
                if (comparison != 0) {
                    return comparison;
                }
            }
            return Integer.compare(parts1.length, parts2.length);
        };

        Arrays.sort(names, nameComparator);

        String fileName = names[0] + "_" + names[1] + "_chat_history.txt";
        try {
            // Validate the generated file name
            if (!isValidFileName(fileName)) {
                throw new IllegalArgumentException("Invalid file name.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while generating chat history file name: " + e.getMessage());
        }

        return fileName;
    }

    private boolean isValidFileName(String fileName) {
        String regex = "^[a-zA-Z0-9._-]+$";
        return fileName.matches(regex);
    }

    public void removeMessage(int index) {
        try {
            if (index >= 0 && index < chatEntries.size()) {
                chatEntries.remove(index);
                System.out.println("Message removed successfully.");
            } else {
                System.out.println("Invalid message index.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while removing message: " + e.getMessage());
        }
    }

    public void editMessage(int index, String newMessage) {
        try {
            if (index >= 0 && index < chatEntries.size()) {
                ChatEntry entry = chatEntries.get(index);
                String senderName = entry.getMessage().substring(0, entry.getMessage().indexOf(":") + 1);
                entry.setMessage(senderName + " " + newMessage);
                System.out.println("Message edited successfully.");
            } else {
                System.out.println("Invalid message index.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while editing message: " + e.getMessage());
        }
    }

    public void startChatting(User friend) {
        try {
            loadChatHistory(friend);
            printChat(1);

            Scanner scanner = new Scanner(System.in);
            String message;

            while (true) {
                System.out.print("You: ");
                message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    break;
                } else if (message.equalsIgnoreCase("remove")) {
                    System.out.print("Enter the index of the message to remove: ");
                    int index = scanner.nextInt();
                    scanner.nextLine();
                    removeMessage(index);
                } else if (message.equalsIgnoreCase("edit")) {
                    System.out.print("Enter the index of the message to edit: ");
                    int index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter the new message: ");
                    String newMessage = scanner.nextLine();
                    editMessage(index, newMessage);
                } else {
                    addMessage(loggedInUser.getUsername() + ": " + message);
                }
            }
            saveChatHistory(friend);
        } catch (Exception e) {
            System.out.println("Error occurred during the chat: " + e.getMessage());
        }
    }

    public void chatManager() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You have entered the chat manager. Choose a friend to chat with:");

        ArrayList<String> friends = loggedInUser.getFriends();
        ArrayList<User> friendObjects = new ArrayList<>();

        for (String friend : friends) {
            User friendObj = database.getUser("UserName", friend);
            friendObjects.add(friendObj);
        }

        if (friendObjects.isEmpty()) {
            System.out.println("You have no friends to chat with.");
            return; // Return to the main menu
        }

        // Compare and display the latest time on top
        /* friendObjects.sort(Comparator.comparing((User friend) -> {
        String fileName = generateChatHistoryFileName(friend);
        if (fileName != null && Files.exists(Paths.get(fileName))) {
            return getLatestChatTimestamp(friend);
        } else {
            System.out.println("Chat history file not found for " + friend.getUsername());
        }
        return null; // or handle the error case accordingly
    }, Comparator.nullsLast(Comparator.reverseOrder()))); */
        int choice = 1;
        for (User friend : friendObjects) {
            System.out.println(choice + ". " + friend.getUsername());
            choice++;
        }

        int friendChoice;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                friendChoice = scanner.nextInt();
                scanner.nextLine();

                if (friendChoice >= 1 && friendChoice <= friendObjects.size()) {
                    User friend = friendObjects.get(friendChoice - 1);
                    System.out.println("You are now chatting with " + friend.getUsername());
                    System.out.println("[Type exit to Exit; edit to Edit Message; delete to Delete Message]");
                    startChatting(friend);
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                String input = scanner.nextLine();
                System.out.println("Invalid input. Please enter a valid choice.");
            }
        }
        }

    private LocalDateTime getLatestChatTimestamp(User friend) {
        if (friend == null) {
            System.out.println("Invalid friend.");
            return null;
        }

        String fileName = generateChatHistoryFileName(friend);
        if (fileName == null) {
            System.out.println("Invalid file name.");
            return null;
        }

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            LocalDateTime latestTimestamp = null;
            while ((line = reader.readLine()) != null) {
                try {
                    ChatEntry entry = parseChatEntry(line);
                    LocalDateTime timestamp = entry.getTimestamp();
                    if (latestTimestamp == null || timestamp.isAfter(latestTimestamp)) {
                        latestTimestamp = timestamp;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Error occurred while parsing chat entry: " + e.getMessage());
                }
            }
            return latestTimestamp;
        } catch (FileNotFoundException e) {
            System.out.println("Chat history file not found.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading chat history: " + e.getMessage());
        }
        return null;
    }

    private class ChatEntry {

        private String message;
        private LocalDateTime timestamp;

        public ChatEntry(String message, LocalDateTime timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}
