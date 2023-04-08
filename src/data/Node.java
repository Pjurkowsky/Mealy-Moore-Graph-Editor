/*
 *  Program: Operacje na obiektach klasy Node
 *     Plik: Node.java
 *           -definicja publicznej klasy Node
 *
 *    Autor: Lukasz Wdowiak 264026
 *     Data: Grudzien 2022 r.
 */
/**
 * Klasa reprezentuje wezel automatu: <br>
 *
 * @author Lukasz Wdowiak
 * @version Grudzien 2022 r.
 */
package data;

import java.awt.*;
import java.io.Serializable;

public class Node implements Serializable {
    /**
     * Zmienna przechowujaca pozycje iksowa wezla
     */
    private int x;
    /**
     * Zmienna przechowujaca pozycje igrekowa wezla
     */
    private int y;
    /**
     * Zmienna przechowujaca promien wezla
     */
    private int r;
    /**
     * Zmienna przechowujaca nazwe wezla
     */
    private String name;
    /**
     * Zmienna przechowujaca kolor wezla
     */
    private Color color;

    /**
     * Konstruktor wezla przyjmujacy 3 argumenty
     *
     * @param x    iksowa wspolrzedna
     * @param y    igrekowa wspolrzedna
     * @param name nazwa wezla
     */
    public Node(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.r = 20;
        color = Color.white;
        this.name = name;
    }

    /**
     * Konstruktor wezla przyjmujacy 2 argumenty
     *
     * @param point wspolrzedne wezla
     * @param name  nazwa wezla
     */
    public Node(Point point, String name) {
        this(point.x, point.y, name);
    }

    /**
     * Konstruktor wezla przyjmujacy 3 argumenty
     *
     * @param point wspolrzedne wezla
     * @param name  nazwa wezla
     * @param color kolor wezla
     */
    public Node(Point point, String name, Color color) {
        this(point.x, point.y, name);
        this.color = color;
    }

    /**
     * Nadpisana metoda porównywania dwoch objektow typu Node
     *
     * @return Prawde gdy Nody sa takie same i falsz jak nie
     */
    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        return this.x == ((Node) obj).x && this.y == ((Node) obj).y;
    }

    /**
     * Metoda rysujaca wezly na ekranie
     *
     * @param g grafika
     */
    void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
        g.setColor(Color.BLACK);
        g.drawOval(x - r, y - r, 2 * r, 2 * r);
        g.setColor(Color.BLACK);
        g.drawString(name, x - r + 5, y);

    }

    /**
     * Metoda zwracajaca prawde gdy najedziemy myszka na wezel
     *
     * @param nx wspolrzedna myszki
     * @param ny wspolrzedna myszki
     */
    public Boolean isMouseOver(int nx, int ny) {
        return (x - nx) * (x - nx) + (y - ny) * (y - ny) <= r * r;
    }

    /**
     * Metoda zwracajaca prawde gdy najedziemy myszka na wezel
     *
     * @param point wspolrzedne myszki
     */
    public Boolean isMouseOver(Point point) {
        return isMouseOver(point.x, point.y);
    }

    /**
     * Metoda poruszjaca wezlem o dx,dy
     *
     * @param dx o ile ma sie zmienic x
     * @param dy o ile ma sie zmienic y
     */
    public void moveNode(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * @return zwraca x
     */
    public int getX() {
        return x;
    }

    /**
     * @return zwraca y
     */
    public int getY() {
        return y;
    }

    /**
     * @return zwraca  punkt(x,y)
     */
    public Point getPoint() {
        return new Point(x, y);
    }

    /**
     * @return zwraca   promien wezla
     */
    public int getR() {
        return r;
    }

    /**
     * @return zwraca kolor wezla
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return zwraca nazwe wezla
     */
    public String getName() {
        return name;
    }

    /**
     * @param x ustawia wspolrzedna x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y ustawia wspolrzedna y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param r ustawia promien wezla
     */
    public void setR(int r) {
        this.r = r;
    }

    /**
     * @param name ustawia nazwe wezla
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param color ustawia kolor kolor
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return zwraca tekstowa reprezentacje wezla
     */
    @Override
    public String toString() {
        return name + ": (" + x + "," + y + ")";
    }

}
