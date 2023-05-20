/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*package y1s2_assignment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus
 */

/*public class UserDatabase {

    private static String FILENAME = "users.csv";
    private List<User> users;

    public UserDatabase() {
        this.users = new ArrayList<>();
    }

    public void addUsers(User user) {
        users.add(user);
    }

    public void registeredUser(String accountID,String username, String email, String contactNumber, String password) {
        User newUser = new User.Builder(accountID,username, email, contactNumber, password).build();
        String[] userData = {newUser.getAccountID(),newUser.getUsername(), newUser.getEmailAddress(), newUser.getContactNumber(), newUser.getPassword()};

        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true))) {
            bw.write(String.join(",", userData));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return this.users;
    }

    public String getUserData(String loginId) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row[1].equals(loginId) || row[2].equals(loginId)) {
                    br.close();
                    return line;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return null;
    }

    public boolean checkUserLogin(String loginId) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            String line;
            while ((line = br.readLine()) != null) {
                String[] column = line.split(",");
                if (column[1].equals(loginId) || column[2].equals(loginId)) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return false;
    }

    public boolean checkPassword(String userData, String password) {
        String[] userDataArray = userData.split(",");
        String encryptedPassword = userDataArray[3];
        String[] encryptedPasswordArray = encryptedPassword.split("\\|");
        String hexSalt = encryptedPasswordArray[1];
        byte[] salt = new byte[16];
        for (int i = 0; i < 16; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexSalt.substring(index, index + 2), 16);
            salt[i] = (byte) j;
        }
        String hexPassword = encryptedPasswordArray[0];
        byte[] hash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error encrypting password.");
            return false;
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        String hexHash = hexString.toString();
        return hexHash.equals(hexPassword);
    }

    public String getUserName(String userData) {
        String[] userDataArray = userData.split(",");
        return userDataArray[0];
    }

    public boolean CheckingExist(String check) {
        File file = new File("users.csv");
        if (!file.exists() || file.length() == 0) {
            return false;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length >= 3) {
                    if (row[0].equals(check) || row[1].equals(check) || row[2].equals(check)) {
                        br.close();
                        return true;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return false;
    }

    public void updateUserDetail(User user) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                String[] userDataArray = line.split(",");
                if (userDataArray[1].equals(user.getEmailAddress())) {
                    String hobbiesString = String.join(",", user.getHobbies());
                    String jobsString = String.join(",", user.getJobs());
                    String updatedUserData = String.format("%s,%s,%s,%s,%s,%s,\"%s\",%s,%s,\"%s\",\"%s\"", user.getUsername(), user.getEmailAddress(), user.getContactNumber(), user.getPassword(),
                            user.getName(), user.getBirthday(), user.getAddress(), user.getGender(), user.getRelationshipStatus(), hobbiesString, jobsString);
                    fileContent.append(updatedUserData).append("\n");
                } else {
                    fileContent.append(line).append("\n");
                }
            }
            br.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            writer.write(fileContent.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/