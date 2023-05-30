/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class ConnectionGraph {

    private LinkedList<Vertex> vertices;
    private int index;

    public ConnectionGraph() {
        vertices = new LinkedList<>();
        index = 0;
    }

    public void addVertex(String username) {
        if (!vertexExists(username)) {
            vertices.add(new Vertex(username, index++));
        }
    }

    public void addEdge(User loggedInUser, String username2) {
        Vertex vertex1 = getVertex(loggedInUser.getName());
        Vertex vertex2 = getVertex(username2);

        if (vertex1 != null && vertex2 != null) {
            vertex1.addNeighbor(vertex2);
            vertex2.addNeighbor(vertex1);
        }
    }

    public void removeVertex(String username) {
        Vertex vertex = getVertex(username);
        if (vertex != null) {
            vertices.remove(vertex);
            // Remove connections to the vertex
            for (Vertex v : vertices) {
                v.removeNeighbor(vertex);
            }
        }
    }

    public void removeEdge(String username1, String username2) {
        Vertex vertex1 = getVertex(username1);
        Vertex vertex2 = getVertex(username2);

        if (vertex1 != null && vertex2 != null) {
            vertex1.removeNeighbor(vertex2);
            vertex2.removeNeighbor(vertex1);
        }
    }

    public String showFirstDegreeConnections(String username) {
        StringBuilder sb = new StringBuilder();
        Vertex vertex = getVertex(username);
        if (vertex != null) {
            LinkedList<Vertex> neighbors = vertex.getNeighbors();
            for (Vertex neighbor : neighbors) {
                sb.append(neighbor.getUsername()).append(",");
            }

        } else {
            sb.append("Vertex ").append(username).append(" not found.");
        }
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public String showSecondDegreeConnections(String username) {
        StringBuilder sb = new StringBuilder();
        Vertex vertex = getVertex(username);
        if (vertex != null) {
            LinkedList<Vertex> firstDegreeNeighbors = vertex.getNeighbors();

            for (Vertex firstDegreeNeighbor : firstDegreeNeighbors) {
                LinkedList<Vertex> secondDegreeNeighbors = firstDegreeNeighbor.getNeighbors();
                for (Vertex secondDegreeNeighbor : secondDegreeNeighbors) {
                    if (!secondDegreeNeighbor.equals(vertex) && !firstDegreeNeighbors.contains(secondDegreeNeighbor)) {
                        sb.append(secondDegreeNeighbor.getUsername()).append(",");
                    }
                }
            }
        } else {
            sb.append("Vertex ").append(username).append(" not found.");
        }
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public String showFirstAndSecondDegreeConnections(String username) {
        StringBuilder sb = new StringBuilder();
        Vertex vertex = getVertex(username);
        if (vertex != null) {
            LinkedList<Vertex> firstDegreeNeighbors = vertex.getNeighbors();
            for (Vertex firstDegreeNeighbor : firstDegreeNeighbors) {
                sb.append(firstDegreeNeighbor.getUsername()).append(",");
                LinkedList<Vertex> secondDegreeNeighbors = firstDegreeNeighbor.getNeighbors();
                for (Vertex secondDegreeNeighbor : secondDegreeNeighbors) {
                    if (!secondDegreeNeighbor.equals(vertex) && !firstDegreeNeighbors.contains(secondDegreeNeighbor)) {
                        sb.append(secondDegreeNeighbor.getUsername()).append(",");
                    }
                }
            }
        } else {
            sb.append("Vertex ").append(username).append(" not found.");
        }
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

   public int findDegreeOfConnection(String username1, String username2) {
    Vertex vertex1 = getVertex(username1);
    Vertex vertex2 = getVertex(username2);

    if (vertex1 != null && vertex2 != null) {
        LinkedList<Vertex> queue = new LinkedList<>();
        boolean[] visited = new boolean[vertices.size()];
        int[] degree = new int[vertices.size()];

        queue.add(vertex1);
        visited[vertex1.getIndex()] = true;

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            if (current.equals(vertex2)) {
                return degree[current.getIndex()];
            }

            LinkedList<Vertex> neighbors = current.getNeighbors();
            int currentDegree = degree[current.getIndex()];
            int nextDegree = currentDegree + 1;

            for (Vertex neighbor : neighbors) {
                int neighborIndex = neighbor.getIndex();
                if (!visited[neighborIndex]) {
                    queue.add(neighbor);
                    visited[neighborIndex] = true;
                    degree[neighborIndex] = nextDegree;
                }
            }
        }
    }

    return -1; // Return -1 if the connection is not found
}
    public String showAdjacency() {
        StringBuilder sb = new StringBuilder();

        for (Vertex vertex : vertices) {
            sb.append(vertex.getUsername()).append(":");
            LinkedList<Vertex> neighbors = vertex.getNeighbors();
            for (Vertex neighbor : neighbors) {
                sb.append(neighbor.getUsername()).append(",");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }
        return sb.toString();
    }

   /* public void storeAdjacency() {
        try {
            FileWriter writer = new FileWriter("connection.txt");
            writer.write(showAdjacency());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void loadAdjacency() {
        try ( BufferedReader reader = new BufferedReader(new FileReader("connection.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String vertex = parts[0];
                    String[] neighbors = parts[1].split(",");
                    addVertex(vertex);
                    for (String neighbor : neighbors) {
                        addVertex(neighbor);
                        addEdge(vertex, neighbor);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
*/
    private boolean vertexExists(User loggedInUser) {
        for (Vertex vertex : vertices) {
            if (vertex.getUsername().equals(loggedInUser.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private Vertex getVertex(String username) {
        for (Vertex vertex : vertices) {
            if (vertex.getUsername().equals(username)) {
                return vertex;
            }
        }
        return null;
    }

    private class Vertex {

        private String username;
        private int index;
        private LinkedList<Vertex> neighbours;

        public Vertex(String username, int index) {
            this.username = username;
            this.index = index;
            this.neighbours = new LinkedList<>();
        }

        public String getUsername() {
            return username;
        }

        public int getIndex() {
            return index;
        }

        public LinkedList<Vertex> getNeighbors() {
            return neighbours;
        }

        public void addNeighbor(Vertex neighbour) {
            if (!neighbours.contains(neighbour)) {
                neighbours.add(neighbour);
            }
        }

        public void removeNeighbor(Vertex neighbour) {
            neighbours.remove(neighbour);
        }
    
}
}
