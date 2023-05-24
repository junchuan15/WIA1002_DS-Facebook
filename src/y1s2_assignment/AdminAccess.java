/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

/**
 *
 * @author Asus
 */
public class AdminAccess extends UserAccess {
        
    private DatabaseSQL database;
    
        public AdminAccess(User loggedInUser) {
        super(loggedInUser);
        database = new DatabaseSQL();
    }
        
        
        
}
