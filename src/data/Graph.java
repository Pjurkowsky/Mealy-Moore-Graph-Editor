    /*
     *  Program: Operacje na obiektach klasy Graph
     *     Plik: Graph.java
     *           -definicja publicznej klasy Graph
     *
     *    Autor: Lukasz Wdowiak 264026
     *     Data: Grudzien 2022 r.
     */


/**
 * Klasa reprezentuje graf automatu: <br>
 *
 * @author Lukasz Wdowiak
 * @version Grudzien 2022 r.
 */
package data;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Graph implements Serializable {
    /**
     * Wezly grafu
     */
    ArrayList<Node> nodes = new ArrayList<>();
    /**
     * krawedzie grafu
     */
    ArrayList<Edge> edges = new ArrayList<>();

    public Graph() {

    }

    /**
     * Metoda rysujaca krawedzie i wezly grafu
     */
    public void draw(Graphics g) {
        for (Edge edge : edges) {
            edge.draw(g);
        }
        for (Node node : nodes) {
            node.draw(g);
        }
    }

    /**
     * Metoda dodajaca wezel do grafu
     */
    public void addNode(Node newNode) throws GraphException {
        for(Node node: nodes)
            if(node.getName().equals(newNode.getName()))
                throw new GraphException("Nie mozna stworzyc dwoch wezlow o tej samej nazwie");

        nodes.add(newNode);
    }

    /**
     * Metoda dodajaca krawedz do grafu
     * wyrzuca wyjatek gdy sprobujemy polaczyc ponownie wezly w tym samym kierunku
     */
    public void addEdge(Edge newEdge) throws GraphException {
        for (Edge edge : edges) {
            if (newEdge.equals(edge)) {
                throw new GraphException("Nie mozna polaczyc dwoch tych samych wezlow w ta sama strone");
            }
        }
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            if (newEdge.getNode1().equals(edge.getNode2()) && newEdge.getNode2().equals(edge.getNode1())) {
                newEdge.setSecondEdge(true);
                edges.add(newEdge);
                return;
            }
        }
        edges.add(newEdge);
    }

    /**
     * Metoda przesuwajaca caly graf o dx i dy
     */
    public void moveAllNodes(int dx, int dy) {
        for (Node node : nodes)
            node.moveNode(dx, dy);
    }

    /**
     * Metoda usuwajaca wezel
     */
    public void deleteNode(Node node) {
        for (int j = 0; j < edges.size(); j++)
            if (edges.get(j).getNode1().equals(node) || edges.get(j).getNode2().equals(node)) {
                deleteEdge(edges.get(j));
                j--;
            }
        nodes.remove(node);
    }

    /**
     * Metoda usuwajaca krawedz
     */
    public void deleteEdge(Edge edge) {
        edges.remove(edge);
    }

    /**
     * Metoda zwracajaca wezly
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Metoda zwracajaca krawedzie
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Metoda zapisujaca graf to pliku binarnego
     */
    public static void printToFile(String file_name, Graph graph) throws GraphException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file_name))) {
            outputStream.writeObject(graph);
        } catch (IOException e) {
            throw new GraphException(e.getMessage());
        }
    }

    /**
     * Metoda odczytujaca graf z pliku binarnego
     */
    public static Graph readFromFile(String file_name) throws GraphException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file_name))) {
            return (Graph) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GraphException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }

    /**
     * Statyczna metoda posiadajaca przykladowy graf moore'a (detektor sekwencji 0110)
     */
    public static Graph graphExample() throws GraphException {
        Graph graph = new Graph();

        Node q0 = new Node(new Point(104, 150), "q0/y0", Color.white);
        Node q1 = new Node(new Point(194, 274), "q1/y0", Color.lightGray);
        Node q2 = new Node(new Point(296, 226), "q2/y0", Color.CYAN);
        Node q3 = new Node(new Point(364, 133), "q3/y0", Color.yellow);
        Node q4 = new Node(new Point(369, 61), "q4/y1", Color.GREEN);

        Edge e0 = new Edge(q0, q0, "z1", Color.RED);
        Edge e1 = new Edge(q0, q1, "z0", Color.BLUE);
        Edge e2 = new Edge(q1, q1, "z0", Color.BLUE);
        Edge e3 = new Edge(q1, q2, "z1", Color.RED);
        Edge e4 = new Edge(q2, q1, "z0", Color.BLUE);
        Edge e5 = new Edge(q2, q3, "z1", Color.RED);
        Edge e6 = new Edge(q3, q4, "z0", Color.BLUE);
        Edge e7 = new Edge(q4, q0, "z0 z1", Color.MAGENTA);

        graph.addNode(q0);
        graph.addNode(q1);
        graph.addNode(q2);
        graph.addNode(q3);
        graph.addNode(q4);

        graph.addEdge(e0);
        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);
        graph.addEdge(e4);
        graph.addEdge(e5);
        graph.addEdge(e6);
        graph.addEdge(e7);

        System.out.println(e3.getNode1().equals(q1));
        return graph;
    }
}


