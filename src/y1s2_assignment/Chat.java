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
                String formattedTimestamp = entry.getTimestamp().format(formatter);
                System.out.println(formattedTimestamp + " " + entry.getMessage());
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
                try {
                    chatEntries.add(parseChatEntry(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid chat entry encountered: " + line);
                }
            }
            reader.close();
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
            if (chatEntries.isEmpty()) {
                System.out.println("No messages found in the chat history.");
                return;
            }

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
            if (chatEntries.isEmpty()) {
                System.out.println("No messages found in the chat history.");
                return;
            }

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
            System.out.println("You are now chatting with " + friend.getUsername());
            loadChatHistory(friend);
            printChat(1);

            Scanner scanner = new Scanner(System.in);
            String message;
            boolean isChatting = true;

            System.out.println("[Type exit to Exit; edit to Edit Message; delete to Delete Message]");

            while (isChatting) {
                System.out.print("You: ");
                message = scanner.nextLine();

                switch (message.toLowerCase()) {
                    case "exit":
                        isChatting = false;
                        break;
                    case "delete":
                        System.out.print("Enter the index[0 as first line of chat] of the message to remove: ");
                        int index = scanner.nextInt();
                        scanner.nextLine();
                        removeMessage(index);
                        break;
                    case "edit":
                        System.out.print("Enter the index[0 as first line of chat] of the message to edit: ");
                        index = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter the new message: ");
                        String newMessage = scanner.nextLine();
                        editMessage(index, newMessage);
                        break;
                    default:
                        addMessage(loggedInUser.getUsername() + ": " + message);
                        break;
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

        ArrayList<LocalDateTime> latestChatTimestamps = new ArrayList<>();
        ArrayList<String> lastMessages = new ArrayList<>();

        for (String friend : friends) {
            User friendObj = database.getUser("UserName", friend);
            if (friendObj != null) {
                friendObjects.add(friendObj);
                loadChatHistory(friendObj);
                // Get the latest chat timestamp and last message for each friend
                if (!chatEntries.isEmpty()) {
                    ChatEntry lastChatEntry = chatEntries.get(chatEntries.size() - 1);
                    latestChatTimestamps.add(lastChatEntry.getTimestamp());
                    lastMessages.add(lastChatEntry.getMessage());
                } else {
                    // If no chat history, assume default values
                    latestChatTimestamps.add(LocalDateTime.MIN);
                    lastMessages.add("");
                }
            }
        }

        // Sort the friendObjects list based on the latest chat timestamp in descending order
        friendObjects.sort(Comparator.comparing(friend -> latestChatTimestamps.get(friendObjects.indexOf(friend))).reversed());

        if (friendObjects.isEmpty()) {
            System.out.println("You have no friends to chat with.");
            return;
        }

        int choice = 1;
        for (User friend : friendObjects) {
            LocalDateTime latestTimestamp = latestChatTimestamps.get(friendObjects.indexOf(friend));
            String lastMessage = lastMessages.get(friendObjects.indexOf(friend));

            if (latestTimestamp.equals(LocalDateTime.MIN)) {
                System.out.println(choice + ". " + friend.getUsername() + " (No chat history)");
            } else {
                System.out.println(choice + ". " + friend.getUsername() + " (Last Message: " + latestTimestamp + ")");
            }
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
                    try {
                        System.out.println("You are now chatting with " + friend.getUsername());
                        printChat(1);
                        String message;
                        boolean isChatting = true;
                        System.out.println("[Type exit to Exit; edit to Edit Message; delete to Delete Message]");
                        while (isChatting) {
                            System.out.print("You: ");
                            message = scanner.nextLine();

                            switch (message.toLowerCase()) {
                                case "exit":
                                    isChatting = false;
                                    break;
                                case "delete":
                                    System.out.print("Enter the index[0 as first line of chat] of the message to remove: ");
                                    int index = scanner.nextInt();
                                    scanner.nextLine();
                                    removeMessage(index);
                                    break;
                                case "edit":
                                    System.out.print("Enter the index[0 as first line of chat] of the message to edit: ");
                                    index = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Enter the new message: ");
                                    String newMessage = scanner.nextLine();
                                    editMessage(index, newMessage);
                                    break;
                                default:
                                    addMessage(loggedInUser.getUsername() + ": " + message);
                                    break;
                            }
                        }
                        saveChatHistory(friend);
                    } catch (Exception e) {
                        System.out.println("Error occurred during the chat: " + e.getMessage());
                    }
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
