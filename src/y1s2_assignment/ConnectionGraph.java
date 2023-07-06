/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ConnectionGraph {

    private LinkedList<Vertex> vertices;
    DatabaseSQL database = new DatabaseSQL();
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
        if (user.equals(friend)) {
            return;
        }

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
            for (Vertex neighbor : vertex.getNeighbours()) {
                neighbor.removeNeighbour(vertex);
            }
        }
    }

    public void removeEdge(User user1, User user2) {
        if (user1.equals(user2)) {
            return;
        }

        Vertex vertex1 = getVertex(user1);
        Vertex vertex2 = getVertex(user2);

        if (vertex1 != null && vertex2 != null) {
            vertex1.removeNeighbour(vertex2);
            vertex2.removeNeighbour(vertex1);
        }
    }

    public List<User> getRecommendedConnections(User loggedInUser) {
        List<User> recommendedConnections = new ArrayList<>();
        Vertex loggedInUserVertex = getVertex(loggedInUser);

        if (loggedInUserVertex != null) {
            List<Vertex> level2Connections = bfs(loggedInUser, 2);
            List<Vertex> level3Connections = bfs(loggedInUser, 3);

            level2Connections.sort((v1, v2) -> {
                int mutuals1 = countMutualFriends(loggedInUser, v1.getUser());
                int mutuals2 = countMutualFriends(loggedInUser, v2.getUser());
                if (mutuals1 != mutuals2) {
                    return Integer.compare(mutuals2, mutuals1);
                } else {
                    return Integer.compare(countMutualConnections(loggedInUser, v2.getUser()), countMutualConnections(loggedInUser, v1.getUser()));
                }
            });

            level3Connections.sort((v1, v2) -> {
                int mutuals1 = countMutualFriends(loggedInUser, v1.getUser());
                int mutuals2 = countMutualFriends(loggedInUser, v2.getUser());
                if (mutuals1 != mutuals2) {
                    return Integer.compare(mutuals2, mutuals1);
                } else {
                    return Integer.compare(countMutualConnections(loggedInUser, v2.getUser()), countMutualConnections(loggedInUser, v1.getUser()));
                }
            });

            Set<Vertex> firstDegreeConnections = new HashSet<>();
            for (Vertex vertex : level2Connections) {
                if (!vertex.getUser().equals(loggedInUser)) {
                    recommendedConnections.add(vertex.getUser());
                    firstDegreeConnections.add(vertex);
                }
            }

            for (Vertex vertex : level3Connections) {
                if (!vertex.getUser().equals(loggedInUser) && !firstDegreeConnections.contains(vertex)) {
                    recommendedConnections.add(vertex.getUser());
                }
            }
        }

        return recommendedConnections;
    }

    private int countMutualConnections(User user1, User user2) {
        List<String> user1Connections = user1.getFriends();
        List<String> user2Connections = user2.getFriends();

        int count = 0;
        for (String connection : user1Connections) {
            if (user2Connections.contains(connection)) {
                count++;
            }
        }

        return count;
    }

    private List<Vertex> bfs(User loggedInUser, int targetDegree) {
        List<Vertex> connections = new ArrayList<>();
        Vertex loggedInUserVertex = getVertex(loggedInUser);

        if (loggedInUserVertex != null) {
            Set<Vertex> visited = new HashSet<>();
            Queue<Vertex> queue = new LinkedList<>();
            visited.add(loggedInUserVertex);
            queue.offer(loggedInUserVertex);
            loggedInUserVertex.setDegree(0);

            while (!queue.isEmpty()) {
                Vertex current = queue.poll();
                int currentDegree = current.getDegree();

                if (currentDegree == targetDegree) {
                    connections.add(current);
                } else if (currentDegree > targetDegree) {
                    return connections;
                }

                for (Vertex neighbour : current.getNeighbours()) {
                    if (!visited.contains(neighbour)) {
                        visited.add(neighbour);
                        queue.offer(neighbour);
                        neighbour.setDegree(currentDegree + 1);
                    }
                }
            }
        }

        return connections;
    }

    public int countConnectionsAtDegree(User loggedInUser, int degree) {
        Vertex loggedInUserVertex = getVertex(loggedInUser);

        if (loggedInUserVertex != null) {
            Set<Vertex> visited = new HashSet<>();
            Queue<Vertex> queue = new LinkedList<>();
            visited.add(loggedInUserVertex);
            queue.offer(loggedInUserVertex);
            loggedInUserVertex.setDegree(0);

            int connectionCount = 0;

            while (!queue.isEmpty()) {
                int size = queue.size();

                for (int i = 0; i < size; i++) {
                    Vertex current = queue.poll();
                    int currentDegree = current.getDegree();

                    if (currentDegree == degree) {
                        connectionCount++;
                    } else if (currentDegree > degree) {
                        return connectionCount;
                    }

                    for (Vertex neighbour : current.getNeighbours()) {
                        if (!visited.contains(neighbour)) {
                            visited.add(neighbour);
                            queue.offer(neighbour);
                            neighbour.setDegree(currentDegree + 1);
                        }
                    }
                }
            }

            return connectionCount;
        }

        return 0;
    }

    public Vertex getVertex(User user) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().equals(user)) {
                return vertex;
            }
        }
        return null;

    }

    public User getUserG(String name) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().getUsername().equals(name)) {
                return vertex.getUser();
            }
        }
        return null;
    }

    public int countMutualFriends(User loggedInUser, User friend) {
        Vertex loggedInUserVertex = getVertex(loggedInUser);
        Vertex userVertex = getVertex(friend);

        if (loggedInUserVertex != null && userVertex != null) {
            int count = 0;

            for (Vertex neighbour : loggedInUserVertex.getNeighbours()) {
                if (userVertex.getNeighbours().contains(neighbour)) {
                    count++;
                }
            }

            return count;
        }

        return 0;
    }

    // Check whether the graph works
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

    public class Vertex {

        private User user;
        private int index;
        private LinkedList<Vertex> neighbours;
        private int degree;

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
            neighbours.add(neighbour);

        }

        public void removeNeighbour(Vertex neighbour) {
            neighbours.remove(neighbour);
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

    }
}
