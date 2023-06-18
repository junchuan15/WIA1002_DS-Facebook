/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Random;

/**
 *
 * @author Asus
 */
public class Encryption {

    private DatabaseSQL database = new DatabaseSQL();

    public boolean checkPassword(String accountID, String password, String passwordDatabase) throws SQLException {
        String encryptedPassword = encryption(accountID, password);
        if (encryptedPassword != null && encryptedPassword.equals(passwordDatabase)) {
            return true;
        } else {
            return false;
        }
    }

    public String encryption(String accountID, String password) throws SQLException {
        String pepper = "<3ds";
        char[] encrypted = new char[50];
        int index = 0;

        password = cook(accountID, password, pepper);

        for (int i = 0; i < password.length() * 2; i += 2) {
            //encrypt current password
            encrypted[i] = operation(password.charAt(index), '*', 3);
            encrypted[i + 1] = operation(password.charAt(index), '*', 7);
            index++;
        }

        //add padding to make all passwords the same length
        //uses a combination of the original password to create padding
        int padding = 50 - password.length() * 2;
        index = 50 - padding;
        for (int i = 0; i < padding; i++) {
            encrypted[index] = operation(encrypted[i + 9], '*', encrypted[i + 20]);
            index++;
        }

        int sum = 0;

        //make the whole password unique even when only one single character is changed
        for (int i = 0; i < encrypted.length; i++) {
            sum += (int) encrypted[i];
        }

        for (int i = 0; i < encrypted.length; i++) {
            encrypted[i] = operation(encrypted[i], '+', sum);
        }

        //convert char to String
        String encryptedString = "";
        for (int i = 0; i < encrypted.length; i++) {
            encryptedString += encrypted[i];
        }

        return encryptedString;
    }

    public boolean validatePassword(String accountID, String currentPasswordInput, String encryptedPassword) throws SQLException {
        String decryptedPassword = encryption(accountID, currentPasswordInput); // Encrypt the entered password with the account ID
        return encryptedPassword.equals(decryptedPassword);
    }

    public char operation(char character, char operation, int value) {
        int temp = character;

        //execute operation
        switch (operation) {
            case '+':
                temp += value;
                break;
            case '-':
                temp -= value;
                break;
            case '*':
                temp *= value;
                break;
            case '/':
                temp /= value;
                break;
        }

        //ascii value: 33->126
        //make sure value in between 33->126
        if (temp > 126) {
            temp -= (temp / 94) * 94;
        }

        while (temp < 33) {
            temp += 94;
        }

        //skip the character '\'and '"' to avoid words like \n or any other complications
        //ascii value 92 = '\'
        if (temp == 92) {
            temp += 1;
        }

        //ascii value 34 = '"'
        if (temp == 34) {
            temp += 1;
        }

        return (char) temp;
    }

    public String cook(String accountID, String password, String pepper) throws SQLException {
        String salt = database.retrieveUserSalt(accountID); // Retrieve the saved salt for the user

        if (salt == null || salt.isEmpty()) {
            // If salt doesn't exist, generate a new one and save it
            salt = generateSalt();
            database.insertUserSalt(accountID, salt);
        }

        password = salt + password; // Add salt to the password

        return pepper + password; // Add pepper to the salted password
    }

    public String generateSalt() {
        StringBuilder salt = new StringBuilder();
        Random rand = new Random();
        int saltLength = rand.nextInt(4) + 2;

        for (int i = 0; i < saltLength; i++) {
            salt.append((char) (rand.nextInt(92) + 33));
        }

        return salt.toString();
    }
}
