package controller;


import java.util.Arrays;
import java.util.LinkedList;

import modele.Position.Direction;
import modele.Pont;
import modele.Ile;
import modele.GrilleInterface;

/**
 * 
 * Instances of the <code>GrilleStatut<code>Statut
 */
public class GrilleStatut {

    private final GrilleInterface modele;
    private final Ile[] allIles;
    private final boolean[] checkedForIsolation;
    // checkedForIsolation[i] true if it has been checked that ile iles[i]
    // belongs to isolated component, i. e. component to which no pont can be
    // built (iles of component have all ponts)

    /**
     * Constructs a <code>PuzzleStateChecker</code> for determining and setting the
     * state of the <code>modele</code>.
     * 
     * @param modele
     *            whose state is to be determined and set
     */
    public GrilleStatut(GrilleInterface modele) {
        this.modele = modele;
        this.allIles = new Ile[modele.getNbIles()];
        modele.getIles().toArray(allIles);
        this.checkedForIsolation = new boolean[allIles.length];
    }

    /**
     * Sets the state of the model of which <code>this</code> instance holds a
     * reference to.
     */
    public void setPuzzleState() {
        modele.setStatut(getPuzzleState());
    }

    private Statut getPuzzleState() {
        resetIsolationCheck();
        for (int ileIndex = 0; ileIndex < allIles.length; ileIndex++) {
            if (allIles[ileIndex].getNoOfPontsMissing() < 0) 
                return Statut.ERRONE;
            if (allIles[ileIndex].getNoOfPontsMissing() == 0) {
                int noOfIlesOfIsolatedGraph = checkForIsolationAndReturnNoOfIlesOfIsolatedComponent(ileIndex);
                if (noOfIlesOfIsolatedGraph > 0) 
                    return (noOfIlesOfIsolatedGraph == modele.getNbIles()) ? Statut.RESOLU : Statut.NON_RESOLU; // not all iles can be connected since there is isolation
            } else if (!reqiredPontsCanBeBuilt(allIles[ileIndex])) return Statut.NON_RESOLU;
        }
        return Statut.INITIAL;
    }

    private void resetIsolationCheck() {
        for (int i = 0; i < checkedForIsolation.length; i++) 
            checkedForIsolation[i] = false;
    }

    /**
     * Checks if it was already checked if ile in iles array at ileIndex
     * belongs to isolated component and returns the number of iles belonging to
     * this isolated component. If the ile at ileIndex does not belong to an
     * isolated component, 0 is returned, but nevertheless all iles of component
     * are marked as checked for isolation so that method returns quicker in the
     * future.
     * 
     * 
     * @param ileIndex
     *            of ile in iles array to be checked
     * @return number of iles belonging to isolated component
     */
    private int checkForIsolationAndReturnNoOfIlesOfIsolatedComponent(int ileIndex) {
        if (checkedForIsolation[ileIndex]) return 0; // else breadthFirstSearch (BFS) starting from ile iles[ileIndex]

        LinkedList<Ile> queue = new LinkedList<>();
        queue.add(allIles[ileIndex]);
        boolean ileMissingPontsFound = false;
        int noOfIlesOfIsolatedComponent = 0;
        while (!queue.isEmpty()) {
            Ile front = queue.poll();
            int frontIndex = Arrays.binarySearch(allIles, front);
            if (!checkedForIsolation[frontIndex]) { // termination condition
                if (front.getNoOfPontsMissing() == 0) 
                    noOfIlesOfIsolatedComponent++;
                else ileMissingPontsFound = true; // component, that ile at ileIndex belongs to, is not isolated
                checkedForIsolation[frontIndex] = true; // necessary for termination and quick returns in future
                addConnectedNeighborsToQueue(queue, front); // get rest of component
            }
        }
        return ileMissingPontsFound ? 0 : noOfIlesOfIsolatedComponent;
    }

    private void addConnectedNeighborsToQueue(LinkedList<Ile> queue, Ile ile) {
        for (Direction direction : Direction.values()) {
            Pont pont = modele.getPont(ile, direction);
            if (pont != null) queue.add(pont.getOtherEnd(ile));
        }
    }

    /**
     * Returns true if it is (still) possible to build all the ponts the
     * <code>ile</code> requires, otherwise false. The <code>ile</code> is
     * missing at least one pont.
     * 
     * @param ile
     *            that is to be checked.
     * @return true if it is (still) possible to build all the ponts the
     *         <code>ile</code> requires, otherwise false.
     */
    private boolean reqiredPontsCanBeBuilt(Ile ile) {
        int noOfBuildablePonts = 0;
        for (Direction direction : Direction.values()) {
            Ile neighbor = modele.getIleVoisine(ile, direction);
            if (neighbor != null && neighbor.getNoOfPontsMissing() > 0) {
                Pont pont = modele.getPont(ile, direction);
                if (neighbor.getNoOfPontsMissing() > 1 && pont == null) 
                    noOfBuildablePonts += 2;
                else if (pont == null || !pont.isDouble())
                    noOfBuildablePonts++; // on pont can still be built in direction
            }
        }
        return ile.getNoOfPontsMissing() <= noOfBuildablePonts;
    }
    
    public enum Statut {

        INITIAL,

        ERRONE,

        NON_RESOLU,

        RESOLU;
    }
}
