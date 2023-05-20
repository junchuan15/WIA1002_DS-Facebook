/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Asus
 */
public class User {

    private String accountID;
    private String Username;
    private String EmailAddress;
    private String ContactNumber;
    private String Password;
    private String Name;
    private String Birthday;
    private int Age;
    private String Address;
    private String Gender;
    private String RelationshipStatus;
    private int NumberOfFriends;
    private ArrayList<String> Hobbies;
    private Stack<String> Jobs;

    public User(Builder builder) {
        this.accountID = builder.accountID;
        this.Username = builder.Username;
        this.EmailAddress = builder.EmailAddress;
        this.ContactNumber = builder.ContactNumber;
        this.Password = builder.Password;
        this.Name = builder.Name;
        this.Birthday = builder.Birthday;
        this.Age = builder.Age;
        this.Address = builder.Address;
        this.Gender = builder.Gender;
        this.RelationshipStatus = builder.RelationshipStatus;
        this.NumberOfFriends = builder.NumberOfFriends;
        this.Hobbies = builder.Hobbies;
        this.Jobs = builder.Jobs;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return Username;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public String getPassword() {
        return Password;
    }

    public String getName() {
        return Name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public int getAge() {
        return Age;
    }

    public String getAddress() {
        return Address;
    }

    public String getGender() {
        return Gender;
    }

    public String getRelationshipStatus() {
        return RelationshipStatus;
    }

    public int getNumberOfFriends() {
        return NumberOfFriends;
    }

    public ArrayList<String> getHobbies() {
        return Hobbies;
    }

    public Stack<String> getJobs() {
        if (Jobs == null) {
            Jobs = new Stack<>();
        }
        return Jobs;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setEmailAddress(String EmailAddress) {
        this.EmailAddress = EmailAddress;
    }

    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setRelationshipStatus(String RelationshipStatus) {
        this.RelationshipStatus = RelationshipStatus;
    }

    public void setHobbies(ArrayList<String> Hobbies) {
        this.Hobbies = Hobbies;
    }

    public void setJobs(Stack<String> Jobs) {
        if (this.Jobs == null) {
            this.Jobs = new Stack<>();
        }
        this.Jobs = Jobs;
    }

    public static class Builder {

        private String accountID;
        private String Username;
        private String EmailAddress;
        private String ContactNumber;
        private String Password;
        private String Name;
        private String Birthday;
        private int Age;
        private String Address;
        private String Gender;
        private String RelationshipStatus;
        private int NumberOfFriends;
        private ArrayList<String> Hobbies;
        private Stack<String> Jobs;

        public Builder(String accountID, String Username, String EmailAddress, String ContactNumber, String Password) {
            this.accountID = accountID;
            this.Username = Username;
            this.EmailAddress = EmailAddress;
            this.ContactNumber = ContactNumber;
            this.Password = Password;
        }

        public Builder() {
        }

        public Builder setAccountID(String accountID) {
            this.accountID = accountID;
            return (this);
        }

        public Builder setUsername(String Username) {
            this.Username = Username;
            return (this);
        }

        public Builder setPassword(String Password) {
            this.Password = Password;
            return (this);
        }

        public Builder setEmailAddress(String EmailAddress) {
            this.EmailAddress = EmailAddress;
            return this;
        }

        public Builder setContactNumber(String ContactNumber) {
            this.ContactNumber = ContactNumber;
            return this;
        }

        public Builder setName(String Name) {
            this.Name = Name;
            return this;
        }

        public Builder setBirthday(String Birthday) {
            this.Birthday = Birthday;
            return this;
        }

        public Builder setAge(int Age) {
            this.Age = Age;
            return this;
        }

        public Builder setAddress(String Address) {
            this.Address = Address;
            return this;
        }

        public Builder setGender(String Gender) {
            this.Gender = Gender;
            return this;
        }

        public Builder setRelationshipStatus(String RelationshipStatus) {
            this.RelationshipStatus = RelationshipStatus;
            return this;
        }

        public Builder setNumberOfFriends(int NumberOfFriends) {
            this.NumberOfFriends = NumberOfFriends;
            return this;
        }

        public Builder setHobbies(ArrayList<String> Hobbies) {
            this.Hobbies = Hobbies;
            return this;
        }

        public Builder setJobs(Stack<String> jobs) {
            if (this.Jobs == null) {
                this.Jobs = new Stack<>();
            }
            this.Jobs=jobs;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
