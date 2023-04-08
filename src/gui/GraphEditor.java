/*
 *  Program: Operacje na obiektach klasy GraphEditor
 *     Plik: GraphEditor.java
 *           -definicja publicznej klasy GraphEditor
 *           -definicja klasy GraphMenuBar
 *
 *    Opis:
 *          Automaty moore'a i mealy'ego:
 *          - wezly reprezentuja stany logincze
 *          - krawedzie reprezentuja przejscia ze stanu do stanu
 *          - edytor umozliwa rysowanie kierunkowych krawedzi jak i petli
 *          - edytor sprawdza i umozliwa tylko jedno polaczenie w danym kierunku
 *          - edytor sprawdza i umozliwa stworzenie tylko jednego stanu o danej nazwie
 *          - edytor umozliwa dowolne kolorowanie krawedzi i wezlow grafu
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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GraphEditor extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 450;

    GraphPanel graphPanel = new GraphPanel();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GraphEditor();
                } catch (GraphException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public GraphEditor() throws GraphException {
        setTitle("Edytor grafow");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setJMenuBar(new GraphMenuBar(this, graphPanel));


        setContentPane(graphPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

class GraphMenuBar extends JMenuBar implements ActionListener {
    JMenu fileMenu = new JMenu("Plik");
    JMenuItem readMenuItem = new JMenuItem("Wczytaj");
    JMenuItem writeMenuItem = new JMenuItem("Zapisz");
    JMenuItem readExample = new JMenuItem("Wczytaj przykład");
    JMenuItem exitMenuItem = new JMenuItem("Wyjdz");

    JMenu graph = new JMenu("Graf");
    JMenuItem listOfNodes = new JMenuItem("Lista wezlow");
    JMenuItem listOfEdges = new JMenuItem("Lista krawedzi");

    JMenu help = new JMenu("Pomoc");
    JMenuItem instruction = new JMenuItem("Instrukcja");
    JMenuItem author = new JMenuItem("Autor");


    Window parent;

    GraphPanel graphPanel;

    GraphMenuBar(Window parent, GraphPanel graphPanel) {
        this.parent = parent;
        this.graphPanel = graphPanel;


        fileMenu.add(readMenuItem);
        fileMenu.add(writeMenuItem);
        fileMenu.add(readExample);
        fileMenu.add(exitMenuItem);

        graph.add(listOfNodes);
        graph.add(listOfEdges);

        help.add(instruction);
        help.add(author);

        readMenuItem.addActionListener(this);
        writeMenuItem.addActionListener(this);
        readExample.addActionListener(this);
        exitMenuItem.addActionListener(this);
        listOfNodes.addActionListener(this);
        listOfEdges.addActionListener(this);
        instruction.addActionListener(this);
        author.addActionListener(this);

        add(fileMenu);
        add(graph);
        add(help);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == readMenuItem) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "bin, ser", "bin", "ser");
                chooser.setFileFilter(filter);
                chooser.setCurrentDirectory(new File("."));
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                    graphPanel.setGraph(Graph.readFromFile(chooser.getSelectedFile().getAbsolutePath()));
                graphPanel.repaint();
            }
            if (e.getSource() == writeMenuItem) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "bin, ser", "bin", "ser");
                chooser.setFileFilter(filter);
                chooser.setCurrentDirectory(new File("."));
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    if (fileName.equals("")) return;
                    Graph.printToFile(fileName, graphPanel.getGraph());
                }
            }
            if(e.getSource() == readExample) {
                graphPanel.setGraph(Graph.graphExample());
                graphPanel.repaint();
            }
            if (e.getSource() == exitMenuItem) {
                parent.dispose();
            }
            if (e.getSource() == listOfEdges) {
                StringBuilder sb = new StringBuilder();
                for(Edge edge: graphPanel.getGraph().getEdges()){
                    sb.append(edge.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(parent,
                        sb.toString());
            }
            if (e.getSource() == listOfNodes) {
                StringBuilder sb = new StringBuilder();
                for(Node node: graphPanel.getGraph().getNodes()){
                    sb.append(node.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(parent,
                        sb.toString());
            }

            if (e.getSource() == instruction) {
                JOptionPane.showMessageDialog(parent, "Instrukcja obslugi\n" +
                        "Działanie programu opiera sie o kilka trybow (na gorze ekranu wyswietla aktualnie wybrany tryb) \n" +
                        "Po wcisnieciu odpowiedniego przycisku zalaczy sie dany tryb\n" +
                        "Przyciski:\n" +
                        "Q - tryb rysowania (po nacisnieciu lpm na plansze stawiamy w tym miejscu wezel, po nacisnieciu w wezel rysujemy krawedz)\n" +
                        "W - tryb poruszania (po nacisnieciu lpm na planszy ruszamy plansza, gdy nacisniemy wezel ruszamy wezlem, gdy krawedz - krawedzia) \n" +
                        "E - tryb edycji (po nacisnieciu lpm na krawedz lub wezel mozemy zmienic jej kolor)\n" +
                        "D - tryb usuwania (po nacisnieciu lpm na krawedz badz wezel usuwamy go):\n" +
                        "\n" +
                        "Dodatkowo istnieje mozliwosc edycji wezlow i krawedzi za pomoca klawisza ppm \n"

                );
            }
            if (e.getSource() == author) {
                JOptionPane.showMessageDialog(parent, "Program do rysowania automatow\n" +
                        "Autor: Lukasz Wdowiak 264026 \n" +
                        "Data:  grudzien 2022 r.\n");
            }
        } catch (GraphException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);

        }
    }

}