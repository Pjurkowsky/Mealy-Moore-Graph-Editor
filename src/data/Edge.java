/*
 *  Program: Operacje na obiektach klasy Edge
 *     Plik: Edge.java
 *           -definicja publicznej klasy Edge
 *
 *    Autor: Lukasz Wdowiak 264026
 *     Data: Grudzien 2022 r.
 */


/**
 * Klasa reprezentuje krawedzi grafu automatu: <br>
 *
 *
 *
 *
 * @author Lukasz Wdowiak
 * @version Grudzien 2022 r.
 */


package data;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;

public class Edge implements Serializable {
    /**
     * Pierwszy wezel
     */
    private Node node1;
    /**
     * Drugi wezel
     */
    private Node node2;

    /**
     * Nazwa krawedzi
     */
    private String name;
    /**
     * Kolor krawedzi
     */
    private Color color = Color.black;
    /**
     * Jezeli secondEdge = true, to nazwa wezla jest troszke wyzej, aby poprawic czytelnosc grafu
     */
    boolean secondEdge = false;
    /**
     * Korekta konca lini rysujacej krawedz
     */
    Point correctEndOfLine;
    /**
     * Konstruktor klasy Edge przyjmujacy 3 argumenty
     */
    public Edge(Node node1, Node node2, String name) {
        this.node1 = node1;
        this.node2 = node2;
        this.name = name;
        correctEndOfLine = node2.getPoint();

    }
    /**
     * Konstruktor klasy Edge przyjmujacy 4 argumenty
     */
    public Edge(Node node1, Node node2, String name, Color color) {
       this(node1,node2, name);
       this.color = color;

    }
    /**
     * Metoda draw rysuje krawedzie zawarte w grafie
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        if (!node1.equals(node2)) {
            float add = 0;
            float xD = Math.signum(node1.getY() - node2.getY());

            if (xD < 1)
                add = 0f;
            else add = 3.14f;
            double angle = Math.atan((double) (node2.getY() - node1.getY()) / (node2.getX() - node1.getX()));
            if (angle >= 0 && angle <= 1.571f)
                add -= 3.14f;
            add -= 0.5;

            int xPosy = Math.round((float) (node2.getX() + Math.cos(add + angle) * 20));
            int yPosy = Math.round((float) (node2.getY() + Math.sin(add + angle) * 20));
            correctEndOfLine = new Point(xPosy, yPosy);
            drawArrow(g, node1.getX(), node1.getY(), xPosy, yPosy, 10, 40, color);

            int x = (node2.getX() + node1.getX()) / 2;
            int y = (node2.getY() + node1.getY()) / 2;

            g2d.rotate(angle, x, y);
            int yx = y + 20;
            if (secondEdge)
                yx -= 30;

            g2d.setColor(color);
            g2d.drawString(name, x, yx);
        } else {
            drawConnectionToItself(g, node1.getX(), node1.getY(), 10, 40, color);
            g2d.setColor(color);
            g2d.drawString(name, node1.getX() - 30, node2.getY() - 30);
        }
    }
    /**
     * Metoda ta sprawdza rownosc dwoch krawedzi - krawedzie sa rowne wtedy gdy pierwszy wezel bedzie rowny pierwszemu wezlowi
     * i drugi wezel bedzie rowny drugiegmu wezlowi
     */
    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        return this.node1.equals(((Edge) obj).getNode1()) && this.node2.equals(((Edge) obj).getNode2());
    }
    /**
     * Statyczna metoda rysujaca strzalke
     * <ul>
     * <li>x0, y0 - punkt rozpoczecia strzalki</li>
     * <li>x1 ,y1 - punkt konca strzalki</li>
     * <li>headAngle - kat konca strzalki</li>
     * <li>color - color strzalki</li>
     * </ul>
     */

    static public void drawArrow(Graphics g, int x0, int y0, int x1,
                                 int y1, int headLength, int headAngle, Color color) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y0 - y1, x0 - x1);
        int[] xs = {x1 + (int) (headLength * Math.cos(angle + offs)), x1,
                x1 + (int) (headLength * Math.cos(angle - offs))};
        int[] ys = {y1 + (int) (headLength * Math.sin(angle + offs)), y1,
                y1 + (int) (headLength * Math.sin(angle - offs))};
        g.setColor(color);
        g.drawLine(x0, y0, x1, y1);

        g.drawPolyline(xs, ys, 3);
    }
    /**
     * Statyczna metoda rysujaca polaczenie wezla do samego siebie
     * <ul>
     * <li>x, y - punkt srodka wezla</li>
     * <li>headAngle - kat konca strzalki</li>
     * <li>color - color strzalki</li>
     * </ul>
     */
    static public void drawConnectionToItself(Graphics g, int x, int y, int headLength, int headAngle, Color color) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = 3.70;
        int[] xs = {x - 20 + (int) (headLength * Math.cos(angle + offs)), x - 20,
                x - 20 + (int) (headLength * Math.cos(angle - offs))};
        int[] ys = {y + 8 + (int) (headLength * Math.sin(angle + offs)), y + 8,
                y + 8 + (int) (headLength * Math.sin(angle - offs))};
        g.setColor(color);
        g.drawPolyline(xs, ys, 3);
        g.drawOval(x - 30, y - 30, 40, 40);
    }

    /**
     * Metoda zwraca prawde gdy najedziemy kursorem myszki na krawedz
     * jak argument przyjmuje wspolrzedne
     */
    public Boolean isMouseOver(int nx, int ny) {
        if (node1.equals(node2)) {
            return (node1.getX() - 20 - nx) * (node1.getX() - 20 - nx) + (node1.getY() - 20 - ny) * (node2.getY() - 20 - ny) <= 200;
        } else {
            int dx = (correctEndOfLine.x - node1.getX());
            int dy = (correctEndOfLine.y - node1.getY());
            double d = ((Math.abs(dx * (node1.getY() - ny) - dy * (node1.getX() - nx))) / (Math.sqrt(dx * dx + dy * dy)));
            if ((nx > this.node1.getX() && nx > this.node2.getX()) || (nx < this.node1.getX() && nx < this.node2.getX())) {
                return false;
            }
            if ((ny > this.node1.getY() && ny > this.node2.getY()) || (ny < this.node1.getY() && ny < this.node2.getY())) {
                return false;
            }
            return  d<=3 ;
        }
    }

    /**
     * Metoda ta rusza wezlami polaczynymi krawedzia
     */
    public void moveNodes(int dx, int dy) {
        if(node1.equals(node2)){
            node1.setX(node1.getX() + dx);
            node1.setY(node1.getY() + dy);
        }
        else{
            node1.setX(node1.getX() + dx);
            node1.setY(node1.getY() + dy);
            node2.setX(node2.getX() + dx);
            node2.setY(node2.getY() + dy);
        }
    }

    /**
     * Metoda ta zwraca pierwszy wezel
     */
    public Node getNode1() {
        return node1;
    }
    /**
     * Metoda ta zwraca drugi wezel
     */
    public Node getNode2() {
        return node2;
    }
    /**
     * Metoda ta zwraca nazwe krawedzi
     */
    public String getName() {
        return name;
    }
    /**
     * Metoda ta zwraca kolor krawedzi
     */
    public Color getColor() {
        return color;
    }
    /**
     * Metoda ta ustawia nazwe krawedzi
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Metoda zwraca prawde gdy najedziemy kursorem myszki na krawedz
     * Przeładowana metoda przyjmujaca punkt jako argument, zamiast wspolrzednych
     */
    public Boolean isMouseOver(Point point) {
        return isMouseOver(point.x, point.y);
    }
    /**
     * Metoda ta  ustawia kolor krawedzi
     */
    public void setColor(Color color) {
        this.color = color;
    }
    /**
     * Metoda ta ustawia czy jest to druga krawedz pomiedzy dwoma wezlami
     */
    public void setSecondEdge(boolean secondEdge) {
        this.secondEdge = secondEdge;
    }
    /**
     * Metoda ta zwraca tekstowa reprezentacje krawedzi
     */
    @Override
    public String toString() {
        return name + ": " + node1.toString() + " --> " + node2.toString();
    }
}
