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
public class TestGraph {
     public static void main(User[] args) {
        
        DatabaseSQL database = new DatabaseSQL();
        ConnectionGraph graph = database.createGraph();
    
        System.out.println("Adjacency:");
        System.out.println(graph.showAdjacency());
/*
    User user = users.get(15);
    System.out.println("First-degree connections for " + user.getUsername() + ":");
    System.out.println(graph.showFirstDegreeConnections(user));

    System.out.println("Second-degree connections for " + user.getUsername() + ":");
    System.out.println(graph.showSecondDegreeConnections(user));

    System.out.println("First and second-degree connections for " + user.getUsername() + ":");
    System.out.println(graph.showFirstAndSecondDegreeConnections(user));

    graph.findDegreeOfConnection(users);*/
    }
}
