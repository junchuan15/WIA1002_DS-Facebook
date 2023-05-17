/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.sql.*;

/**
 *
 * @author Asus
 */
public class DatabaseSQL {

    String url = "jdbc:mysql://localhost:3306/users";
    String username = "root";
    String password = "Jcquah2003!@";

    public void connectAndFetchData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
       
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
public void registeredUser(String accountID, String username, String email, String contactNumber, String password) {
    try (Connection con = DriverManager.getConnection(url, username, password);
         PreparedStatement ps = con.prepareStatement("INSERT INTO users (Account_ID, UserName, EmailAddress, ContactNumber, Password) VALUES (?, ?, ?, ?, ?)")) {
        ps.setString(1, accountID);
        ps.setString(2, username);
        ps.setString(3, email);
        ps.setString(4, contactNumber);
        ps.setString(5, password);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}

