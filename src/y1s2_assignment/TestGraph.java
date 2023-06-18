/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

/**
 *
 * @author Asus
 */
public class TestGraph {
    public static void main(String[] args) {
        DatabaseSQL database = new DatabaseSQL();       
        ConnectionGraph graph = database.createGraph();
        System.out.println(graph.showAdjacency());
        
    }
}
