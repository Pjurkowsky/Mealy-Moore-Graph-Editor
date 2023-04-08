/*
 *  Program: Klasa wyjątkowa(hehe)
 *     Plik: GraphException.java
 *           -definicja publicznej klasy GraphException
 *
 *    Autor: Lukasz Wdowiak 264026
 *     Data: Grudzien 2022 r.
 */
package data;
/**
 * Klasa dziedziczaca po Exception do przechwytywania wyjatkow klasy Graph
 */
public class GraphException extends Exception {
    /**
     * Konstruktor klasy GraphException
     * @param message wiadomosc do wyswietlenia dla uzytkownika
     */
    public GraphException(String message) {
        super(message);
    }
}

