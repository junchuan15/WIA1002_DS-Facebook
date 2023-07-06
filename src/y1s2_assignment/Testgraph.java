/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.List;

/**
 *
 * @author Asus
 */
public class Testgraph {
    public static void main(String[] args) {
       DatabaseSQL database = new DatabaseSQL();
       ConnectionGraph graph = database.createGraph();
        System.out.println(graph.showAdjacency());
        User u1 = graph.getUserG("hudson");
        User u2 = graph.getUserG("Wong");
        System.out.println(graph.countConnectionsAtDegree(u1, 3));
       
    }
}
