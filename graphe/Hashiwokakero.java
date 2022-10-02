/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

import controller.GrilleGenerator;
import controller.GrilleThread;
import controller.GrilleStatut;
import controller.PontHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import modele.Position.Direction;
import modele.GrilleInterface;
import modele.Ile;

/**
 *
 * @author mac
 */
public class Hashiwokakero implements IInputListener {
    private JFrame mainFrame;
    private GrilleInterface modele;
    private GrilleComponent graphic;
    private JLabel statutLabel;

    private GrilleStatut checker;
    private PontHandler handler;
    private GrilleThread manager;

    public Hashiwokakero() {
        createModel();
        createView();
        placeComponents();
    }
    
    /**
     * Rend l'application visible au centre de l'écran.
     */
    public void display() {
        refresh();
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    private void createModel() {
        modele = new GrilleGenerator().getPuzzleSituationModel(); // singleton design pattern could be used
        checker = new GrilleStatut(modele);
        checker.setPuzzleState();
        handler = new PontHandler(modele, checker);
        manager = new GrilleThread(handler, this);
    }
    
    private void createView() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Fichier");
        menu.setMnemonic('F');
        menuBar.add(menu);
        
        menu.addSeparator();
        
        JMenuItem item1 = new JMenuItem("Redémarrez");
        item1.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK) );
        item1.addActionListener((ActionEvent e) -> {
            restartPuzzle();
        });
        
        menu.add(item1);
        JMenuItem item2 = new JMenuItem("Quitter");
        item2.setMnemonic('X');
        item2.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK) );
        item2.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        menu.add(item2);
        
        mainFrame = new JFrame("Hashiwokakero");
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
        graphic = new GrilleComponent(modele, this);
        statutLabel = new JLabel();
        statutLabel.setHorizontalAlignment(JLabel.CENTER);
        statutLabel.setVerticalAlignment(JLabel.CENTER);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setPuzzleStateLabel() {
        String status, title, message; Color color;
        switch (modele.getStatut()) {
        case RESOLU:
            color = Color.GREEN;
            status = "L'énigme est résolu";
            title = "L'énigme est résolue!";
            message = "Félicitations, l'énigme est résolu.";//! Aucun autre pont ne peut être ajouté
            break;
        case INITIAL:
            color = Color.BLACK;
            status = "Pas encore résolu";
            title = "Aucun pont sans conflit trouvé!";
            message = "Aucun pont n'a pu être trouvé qui puisse être ajouté sans provoquer ni même risquer une violation des règles. C'est maintenant à vous de trouver un tel pont.";
            break;
        case NON_RESOLU: 
            color = Color.DARK_GRAY;
            status = "Peut plus être résolu";
            title = "Aucun pont sans conflit trouvé!";
            message = "Aucun pont n'a pu être trouvé qui puisse être ajouté sans provoquer ni même risquer une violation des règles. C'est maintenant à vous de trouver un tel pont.";
            break;
        case ERRONE:
            color = Color.RED;
            status = "Contient une erreur";
            title = "L'énigme contient une erreur!";
            message = "Au moins une île (rouge) a trop de ponts. Un pont ne peut pas être ajouté tant qu'une île a trop de ponts. Retirez le (s) pont (s) ou redémarrez le puzzle pour pouvoir résoudre le puzzle.";
            break;
        default: 
            color = Color.BLACK;
            status = "Le statut inconnu";
            title = "L'énigme ne peut plus être résolue!";
            message = "L'énigme ne peut plus être résolue. Aucun pont ne peut être ajouté sans provoquer ni même risquer une violation des règles. Retirez les ponts ou redémarrez le puzzle pour pouvoir résoudre le puzzle.";
            break;
        }
        statutLabel.setText(status);
        statutLabel.setForeground(color);
        
        if (modele.getStatut() == GrilleStatut.Statut.RESOLU) 
            JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    private void placeComponents() {
        Border COMPONENT_PADDING = BorderFactory.createEmptyBorder(5, 5, 5, 5),
                outsideBorder = BorderFactory.createCompoundBorder(COMPONENT_PADDING, BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        
        JPanel p = new JPanel(new BorderLayout()); {
            p.add(graphic);
        }
        p.setBorder(BorderFactory.createCompoundBorder(outsideBorder, COMPONENT_PADDING));
        mainFrame.add(p, BorderLayout.CENTER);
        
        p = new JPanel(new FlowLayout());{
            p.add(statutLabel);
        }
        mainFrame.add(p);
    }

    @Override
    public void refresh() {
        setPuzzleStateLabel();
        SwingUtilities.invokeLater(() -> 
                graphic.repaint() // not always edt
        );
    }

    @Override
    public void makeMove(Ile ile, Direction directionOfClick, boolean addPont) {
        if (!manager.isAlive()) { // state design pattern could be used
            Ile otherIle = modele.getIleVoisine(ile, directionOfClick); // design by contract: ile != null
            if (ile != null && otherIle != null) { 
                if (addPont) modele.ajouterPontEntre(ile, otherIle);
                else modele.supprimerPontEntre(ile, otherIle);
            }
            checker.setPuzzleState();
            refresh();
        }
    }

    @Override
    public boolean addNextPont() {
        if (!manager.isAlive()) {
            boolean moveCouldBeMade = handler.makeSureMove();
            checker.setPuzzleState();
            refresh();
            return moveCouldBeMade;
        }
        return false; 
    }

    @Override
    public void startAndStopSolving() {
        if (!manager.isAlive()) {
            manager = new GrilleThread(handler, this);
            manager.solvePuzzleWithPauses();
        } else manager.interrupt();
    }

    @Override
    public void restartPuzzle() {
        manager.interrupt();
        modele.supprimerPonts();
        checker.setPuzzleState();
        refresh();
    }

    private void updateViewAndControllerComponents() {
        createModel();
        graphic.setPuzzleSituationModel(modele);
        refresh();
    }

    @Override
    public void generatePuzzle() {
        manager.interrupt();
        modele = new GrilleGenerator().getPuzzleSituationModel();
        updateViewAndControllerComponents();
    }

    @Override
    public void generatePuzzle(int width, int height) {
        manager.interrupt();
        modele = new GrilleGenerator().getPuzzleSituationModel(width, height);
        updateViewAndControllerComponents();
    }

    @Override
    public void generatePuzzle(int width, int height, int noOfIles) {
        manager.interrupt();
        modele = new GrilleGenerator().getPuzzleSituationModel(width, height, noOfIles);
        updateViewAndControllerComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Hashiwokakero().display();
    }
    
}
