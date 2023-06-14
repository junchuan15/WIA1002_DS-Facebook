/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConnectionGraph {

    private LinkedList<Vertex> vertices;
    DatabaseSQL database = new DatabaseSQL();
    ;
    private int index;

    public ConnectionGraph() {
        vertices = new LinkedList<>();
        index = 0;
    }

    public void addVertex(User users) {
        if (!vertexExists(users)) {
            vertices.add(new Vertex(users, index++));

        }
    }

    public void addEdge(User user, User friend) {
        Vertex vertex1 = getVertex(user);
        Vertex vertex2 = getVertex(friend);

        if (vertex1 != null && vertex2 != null) {
            vertex1.addNeighbour(vertex2);
            vertex2.addNeighbour(vertex1);
        }
    }

    public void removeVertex(User user) {
        Vertex vertex = getVertex(user);
        if (vertex != null) {
            vertices.remove(vertex);
            for (Vertex v : vertices) {
                v.removeNeighbour(vertex);
            }
        }
    }

    public void removeEdge(User user1, User user2) {
        Vertex vertex1 = getVertex(user1);
        Vertex vertex2 = getVertex(user2);

        if (vertex1 != null && vertex2 != null) {
            vertex1.removeNeighbour(vertex2);
            vertex2.removeNeighbour(vertex1);
        }
    }

    public List<String> showFirstDegreeConnections(User user) {
        List<String> firstDegree = new ArrayList<>();
        Vertex vertex = getVertex(user);

        if (vertex != null) {
            LinkedList<Vertex> neighbours = vertex.getNeighbours();
            for (Vertex neighbour : neighbours) {
                firstDegree.add(neighbour.getUser().getUsername());
            }
        } else {
            firstDegree.add("Vertex " + user.getUsername() + " not found.");
        }

        return firstDegree;
    }

    public List< String> showSecondDegreeConnections(User loggedInUser) {
        List< String> secondDegree = new ArrayList<>();
        Vertex loggedInUserVertex = getVertex(loggedInUser);

        if (loggedInUserVertex != null) {
            LinkedList<Vertex> firstDegreeNeighbours = loggedInUserVertex.getNeighbours();

            for (Vertex firstDegreeNeighbor : firstDegreeNeighbours) {
                LinkedList<Vertex> secondDegreeNeighbours = firstDegreeNeighbor.getNeighbours();
                for (Vertex secondDegreeNeighbour : secondDegreeNeighbours) {
                    if (!secondDegreeNeighbour.equals(loggedInUserVertex) && !firstDegreeNeighbours.contains(secondDegreeNeighbour)) {
                        secondDegree.add(secondDegreeNeighbour.getUser().getUsername());
                    }
                }
            }
        }

        return secondDegree;
    }

    public List< String> showFirstAndSecondDegreeConnections(User user) {
        List< String> connections = new ArrayList<>();
        Vertex vertex = getVertex(user);

        if (vertex != null) {
            LinkedList<Vertex> firstDegreeNeighbours = vertex.getNeighbours();
            for (Vertex firstDegreeNeighbour : firstDegreeNeighbours) {
                connections.add(firstDegreeNeighbour.getUser().getUsername());

                LinkedList<Vertex> secondDegreeNeighbours = firstDegreeNeighbour.getNeighbours();
                for (Vertex secondDegreeNeighbour : secondDegreeNeighbours) {
                    if (!secondDegreeNeighbour.equals(vertex) && !firstDegreeNeighbours.contains(secondDegreeNeighbour)) {
                        connections.add(secondDegreeNeighbour.getUser().getUsername());
                    }
                }
            }
        } else {
            connections.add("Vertex " + user.getUsername() + " not found.");
        }

        return connections;
    }

    public void findDegreeOfConnection(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                User user1 = users.get(i);
                User user2 = users.get(j);
                int degree = findDegreeOfConnection(user1, user2);
                System.out.println("Degree of connection between " + user1.getUsername() + " and " + user2.getUsername() + ": " + degree);
            }
        }
    }

    private int findDegreeOfConnection(User user1, User user2) {
        Vertex vertex1 = getVertex(user1);
        Vertex vertex2 = getVertex(user2);

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

                LinkedList<Vertex> neighbours = current.getNeighbours();
                int currentDegree = degree[current.getIndex()];
                int nextDegree = currentDegree + 1;

                for (Vertex neighbour : neighbours) {
                    int neighbourIndex = neighbour.getIndex();
                    if (!visited[neighbourIndex]) {
                        queue.add(neighbour);
                        visited[neighbourIndex] = true;
                        degree[neighbourIndex] = nextDegree;
                    }
                }
            }
        }

        return -1; // Return -1 if the connection is not found
    }

    public String showAdjacency() {
        StringBuilder sb = new StringBuilder();

        for (Vertex vertex : vertices) {
            User user = vertex.getUser();
            sb.append(user.getUsername()).append(":");
            List<Vertex> neighbours = vertex.getNeighbours();
            for (Vertex neighbour : neighbours) {
                User neighbourUser = neighbour.getUser();
                sb.append(neighbourUser.getUsername()).append(",");
            }

            if (!neighbours.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("\n");
        }

        sb.append("Adjacency list generated successfully.");
        return sb.toString();
    }

    private boolean vertexExists(User user) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    private Vertex getVertex(User user) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().equals(user)) {
                return vertex;
            }
        }
        return null;
    }

    private class Vertex {

        private User user;
        private int index;
        private LinkedList<Vertex> neighbours;

        public Vertex(User user, int index) {
            this.user = user;
            this.index = index;
            this.neighbours = new LinkedList<>();
        }

        public User getUser() {
            return user;
        }

        public int getIndex() {
            return index;
        }

        public LinkedList<Vertex> getNeighbours() {
            return neighbours;
        }

        public void addNeighbour(Vertex neighbour) {
            if (!neighbours.contains(neighbour)) {
                neighbours.add(neighbour);
            }
        }

        public void removeNeighbour(Vertex neighbour) {
            neighbours.remove(neighbour);
        }

    }
}
