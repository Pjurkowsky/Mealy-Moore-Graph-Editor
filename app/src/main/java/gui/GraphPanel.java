/*
 *  Program: Operacje na obiektach klasy GraphPanel
 *     Plik: GraphPanel.java
 *           -definicja publicznej klasy GraphPanel
 *           -definicja typu wyliczeniowego Mode
 *
 *    Autor: Lukasz Wdowiak 264026
 *     Data: Grudzien 2022 r.
 */
package gui;

import data.Edge;
import data.Graph;
import data.GraphException;
import data.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.event.KeyEvent.*;

public class GraphPanel extends JPanel implements MouseListener, KeyListener, MouseWheelListener, MouseMotionListener {
    Graph graph;
    Mode mode = Mode.DRAWING_NODES;

    Node selectedNode;

    Point mousePointer = new Point(0, 0);
    boolean mouseButtonLeft = false;
    Node nodeUnderCursor = null;
    Edge edgeUnderCursor = null;

    JPanel toolBar = new JPanel();
    JLabel modeLabel = new JLabel("Tryb: ");
    JRadioButton Q = new JRadioButton("Q");
    JRadioButton W = new JRadioButton("W");
    JRadioButton E = new JRadioButton("E");
    JRadioButton D = new JRadioButton("D");

    GraphPanel() throws GraphException {


        Q.setEnabled(false);
        W.setEnabled(false);
        E.setEnabled(false);
        D.setEnabled(false);

        Q.setSelected(true);
        toolBar.add(modeLabel);
        toolBar.add(Q);
        toolBar.add(W);
        toolBar.add(E);
        toolBar.add(D);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(Q);
        buttonGroup.add(W);
        buttonGroup.add(E);
        buttonGroup.add(D);

        add(toolBar);
        graph = Graph.graphExample();
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);

        requestFocus();
    }

    private Node findNode(Point point) {
        for (Node node : graph.getNodes()) {
            if (node.isMouseOver(point.x, point.y)) {
                return node;
            }
        }
        return null;
    }

    private Edge findEdge(Point point) {
        for (Edge edge : graph.getEdges()) {
            if (edge.isMouseOver(point.x, point.y)) {
                return edge;
            }
        }
        return null;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    private void setMouseCursor() {
        int mouseCursor;
        if (nodeUnderCursor != null || edgeUnderCursor != null) {
            mouseCursor = Cursor.HAND_CURSOR;
        } else if (mode.equals(Mode.MOVING_BOARD)) {
            mouseCursor = Cursor.MOVE_CURSOR;
        } else {
            mouseCursor = Cursor.DEFAULT_CURSOR;
        }
        setCursor(Cursor.getPredefinedCursor(mouseCursor));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point mosPos = getMousePosition();
        if (mode.equals(Mode.DRAWING_EDGES) && mosPos != null) {
            if (selectedNode.isMouseOver(mosPos)) {
                Edge.drawConnectionToItself(g, selectedNode.getX(), selectedNode.getY(), 10, 40, Color.BLACK);
            } else {
                double rads = Math.atan((double) (selectedNode.getY() - mosPos.y) / (selectedNode.getX() - mosPos.x));
                int xPosy = Math.round((float) (selectedNode.getX() + Math.cos(-rads) * 20));
                int yPosy = Math.round((float) (selectedNode.getY() + Math.sin(rads) * 20));
                Edge.drawArrow(g, xPosy, yPosy, mosPos.x, mosPos.y, 10, 40, Color.BLACK);
            }
        }
        if (graph == null) return;
        graph.draw(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == VK_Q) {
            mode = Mode.DRAWING_NODES;
            Q.setSelected(true);
        }
        if (e.getKeyCode() == VK_W) {
            mode = Mode.MOVING_BOARD;
            W.setSelected(true);
        }
        if (e.getKeyCode() == VK_E) {
            mode = Mode.EDITING;
            E.setSelected(true);
        }
        if (e.getKeyCode() == VK_D) {
            mode = Mode.DELETING;
            D.setSelected(true);
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            if (e.getButton() == 1) {
                mousePointer.setLocation(getMousePosition());
                mouseButtonLeft = true;
            }

            if (e.getButton() == 3) {
                if (nodeUnderCursor != null)
                    createPopupMenu(e, nodeUnderCursor);
                else if (edgeUnderCursor != null)
                    createPopupMenu(e, edgeUnderCursor);
                else {
                    mousePointer.setLocation(getMousePosition());
                    createPopupMenu(e);
                }

            }

            if (e.getButton() == 1 && mode.equals(Mode.DRAWING_EDGES)) {
                if (nodeUnderCursor != null) {
                    String name = JOptionPane.showInputDialog(this, "Nazwa krawedzi", "z");
                    if (name != null) {
                        graph.addEdge(new Edge(selectedNode, nodeUnderCursor, name));
                        mode = Mode.SELECTING_NODES;
                    }
                }
            }

            if (e.getButton() == 1 && mode.equals(Mode.DRAWING_NODES)) {
                boolean x = false;
                Point pos = getMousePosition();
                if (nodeUnderCursor != null) {
                    x = true;
                    mode = Mode.SELECTING_NODES;
                }
                if (!x) {
                    String name = JOptionPane.showInputDialog(this, "Nazwa węzła", "q");
                    if (name != null)
                        graph.addNode(new Node(pos, name));
                }
            }

            if (e.getButton() == 1 && mode.equals(Mode.SELECTING_NODES)) {
                Node underCursor = findNode(getMousePosition());
                if (underCursor != null) {
                    selectedNode = underCursor;
                    mode = Mode.DRAWING_EDGES;
                }
            }
            if (e.getButton() == 1 && mode.equals(Mode.EDITING)) {
                if (nodeUnderCursor != null) {
                    Color newColor = JColorChooser.showDialog(
                            this,
                            "Choose Background Color",
                            nodeUnderCursor.getColor());
                    if (newColor != null)
                        nodeUnderCursor.setColor(newColor);
                }
                if (edgeUnderCursor != null) {
                    Color newColor = JColorChooser.showDialog(
                            this,
                            "Choose Background Color",
                            edgeUnderCursor.getColor());
                    if (newColor != null)
                        edgeUnderCursor.setColor(newColor);
                }
            }
            if (e.getButton() == 1 && mode.equals(Mode.DELETING)) {
                if (nodeUnderCursor != null)
                    graph.deleteNode(nodeUnderCursor);
                else if (edgeUnderCursor != null)
                    graph.deleteEdge(edgeUnderCursor);
                nodeUnderCursor = null;
            }
            repaint();
        } catch (GraphException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point mosPos = getMousePosition();
        if (e.getButton() == 1 && mosPos != null) {
            mousePointer.setLocation(mosPos);
            mouseButtonLeft = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point mosPos = getMousePosition();
        if (mosPos != null) {
            if (mouseButtonLeft && mode.equals(Mode.MOVING_BOARD)) {
                if (nodeUnderCursor != null)
                    nodeUnderCursor.moveNode(mosPos.x - mousePointer.x, mosPos.y - mousePointer.y);
                else if (edgeUnderCursor != null)
                    edgeUnderCursor.moveNodes(mosPos.x - mousePointer.x, mosPos.y - mousePointer.y);
                else
                    graph.moveAllNodes(mosPos.x - mousePointer.x, mosPos.y - mousePointer.y);
            }
            mousePointer = mosPos;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mode.equals(Mode.DRAWING_EDGES)) {
            repaint();
        }
        Point mosPos = getMousePosition();
        if (mosPos != null) {
            nodeUnderCursor = findNode(mosPos);
            edgeUnderCursor = findEdge(mosPos);
        }
        setMouseCursor();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }


    private void createPopupMenu(MouseEvent event, Node node) {
        JMenuItem menuItem;

        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Zmien kolor");

        menuItem.addActionListener((a) -> {
            Color newColor = JColorChooser.showDialog(
                    this,
                    "Zmien kolor",
                    node.getColor());
            if (newColor != null) {
                node.setColor(newColor);
            }
            repaint();
        });

        popup.add(menuItem);

        menuItem = new JMenuItem("Usun ten wezel");
        menuItem.addActionListener((action) -> {
            graph.deleteNode(node);
            repaint();
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Zmien nazwe");
        menuItem.addActionListener((action) -> {
            String s = JOptionPane.showInputDialog("Podaj nazwe wezla:", node.getName());
            node.setName(s != null ? s : node.getName());
            repaint();
        });
        popup.add(menuItem);


        menuItem = new JMenuItem("Dodaj nowa krawedz");
        menuItem.addActionListener((action) -> {
            String s = JOptionPane.showInputDialog("Podaj nazwe krawedzi:", "z");
            String s1 = JOptionPane.showInputDialog("Podaj nazwe drugiego wezla:", "q");
            boolean found = false;
            for (Node node2 : graph.getNodes()) {
                if (node2.getName().equals(s1)) {
                    try {
                        graph.addEdge(new Edge(node, node2, s));
                        found = true;
                        break;
                    } catch (GraphException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if (!found)
                JOptionPane.showMessageDialog(this, "Nie znaleziono takiego wezla", "info", JOptionPane.INFORMATION_MESSAGE);
            repaint();
        });
        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }

    private void createPopupMenu(MouseEvent event, Edge edge) {
        JMenuItem menuItem;

        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Zmien kolor");

        menuItem.addActionListener((a) -> {
            Color newColor = JColorChooser.showDialog(
                    this,
                    "Zmien kolor",
                    edge.getColor());
            if (newColor != null) {
                edge.setColor(newColor);
            }
            repaint();
        });

        popup.add(menuItem);

        menuItem = new JMenuItem("Usun ta krawedz");
        menuItem.addActionListener((action) -> {
            graph.deleteEdge(edge);
            repaint();
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Zmien nazwe");
        menuItem.addActionListener((action) -> {
            String s = JOptionPane.showInputDialog("Podaj nazwe krawedzi:", edge.getName());
            edge.setName(s != null ? s : edge.getName());
            repaint();
        });
        popup.add(menuItem);

        popup.show(event.getComponent(), event.getX(), event.getY());
    }

    private void createPopupMenu(MouseEvent event) {
        JMenuItem menuItem;

        JPopupMenu popup = new JPopupMenu();

        menuItem = new JMenuItem("Stworz wezel");
        menuItem.addActionListener((action) -> {
            String s = JOptionPane.showInputDialog("Podaj nazwe wezla:", "q");
            if (s != null) {
                try {
                    graph.addNode(new Node(mousePointer, s));
                } catch (GraphException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
                }
            }
            repaint();
        });
        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }
}


enum Mode {
    SELECTING_NODES("Selecting Nodes"),
    DRAWING_NODES("Drawing Nodes"),
    DRAWING_EDGES("Drawing Edges"),
    MOVING_BOARD("Moving Board"),
    EDITING("Editing"),
    DELETING("Deleting");

    String type;

    private Mode(String type_name) {
        this.type = type_name;
    }
}