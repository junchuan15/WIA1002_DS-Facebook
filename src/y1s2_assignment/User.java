/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;

/**
 *
 * @author Asus
 */
public class User {
    String Name;
    String Username;
    String EmailAddress;
    String ContactNumber;
    String Birthday;
    int Age;
    String Address;
    String Gender;
    int NumberOfFriends;
    ArrayList <String> Hobbies=new ArrayList<>();
    ArrayList <String> Jobs=new ArrayList<>();
    
    public User( String Username, String EmailAddress,String ContactNumber){
    }

    public User(String Name, String Username, String EmailAddress, String ContactNumber, String Birthday, int Age, String Address, String Gender, int NumberOfFriends) {
        this.Name = Name;
        this.Username = Username;
        this.EmailAddress = EmailAddress;
        this.ContactNumber = ContactNumber;
        this.Birthday = Birthday;
        this.Age = Age;
        this.Address = Address;
        this.Gender = Gender;
        this.NumberOfFriends = NumberOfFriends;
    }
    
    
    
    
    
}
