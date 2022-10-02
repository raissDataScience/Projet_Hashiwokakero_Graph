package controller;

import java.util.ArrayList;	

import java.util.Iterator;
import java.util.List;

import modele.Position.Direction;
import modele.Pont;
import modele.Ile;
import modele.GrilleInterface;

//TODO: Get sure moves, but do not make them here. Instead return move to main controller und make it in the main controller.

/**
 * An instance of the class <code>PontAdder</code> can add a pont to a
 * Hashiwokakeru puzzle if this pont must certainly be added to the puzzle
 * based on the puzzle's current state (see {@link #makeSureMove() makeSureMove}
 * method).
 */
public class PontHandler {

    private final GrilleInterface modele;
    private final GrilleStatut stateChecker;
    private List<Ile> neighbors;
    private List<Ile> neighborsToBuildPont;
    private List<Ile> neighborsToBuildDoublePont;
    private List<Ile> neighborsRequiringMoreThanOnePont;
    private List<Ile> neighborsRequiringMoreThanTwoPonts;

    /**
     * Constructs an instance of a <code>PontAdder</code>.
     * 
     * @param modele
     *            which holds the puzzle to which a pont is to be added as well as
     *            the puzzles state
     * @param stateChecker
     *            to update the state of the puzzle after a pont has been added
     */
    public PontHandler(GrilleInterface modele, GrilleStatut stateChecker) {
        this.modele = modele;
        this.stateChecker = stateChecker;
    }

    /**
     * Adds a pont to the puzzle if a pont can be found that must certainly be
     * built based <strong>on the current state</strong> of the puzzle.
     * 
     * <p>
     * <strong>Important:</strong> If the user has already added ponts by himself
     * or herself that render the puzzle unsolvable without the program recognizing
     * it, the pont that is added by the method may not lead to a solution of the
     * puzzle and may need to be removed in order to solve the puzzle.
     * </p>
     * 
     * <p>
     * <strong>Important:</strong> No pont is added if
     * <ul>
     * <li>no pont can be found,</li>
     * <li>the puzzle is unsolvable and this was recognized by the application,</li>
     * <li>the puzzle is invalid or already solved.
     * </ul>
     * </p>
     * 
     * @return true if a pont was added, false otherwise
     */
    public boolean makeSureMove() {
        return modele.nonResolu() ? makeSureMoveByUsingRules() : false; // TODO: || trialAndError(); // here an extension could be added to solve puzzle														// differently
    }

    /*
     * Goal: Solving every puzzle, i.e., adding a pont in every case that a pont
     * can be added to get to the solution of the puzzle.
     * 
     * Solution idea 1: If no sure move can be made, make an unsure move (here I can
     * use some heuristics to choose the pont to add), solve the puzzle with the
     * solver without refreshing the view until a solution is found or the puzzle
     * becomes unsolvable. If solution is found, add first pont added to the
     * puzzle (unsure pont). If puzzle becomes unsolvable, remove last unsure
     * pont added and make another unsure move that has not been made before. Stop
     * if a certain time period has passed (all ponts added must be removed).
     * 
     * 
     */

    private static int NO_OF_TRIALS = 0;

    /**
     * 
     * Finds a sure move by trial and error and makes it.
     * 
     * @return true if sure move could be made.
     */
    private boolean trialAndError() {
        NO_OF_TRIALS++;
        if (NO_OF_TRIALS <= 100) {
            Iterator<Ile> iles = modele.getIles().iterator();
            while (iles.hasNext()) { // TODO: Time limit
                Ile ile = iles.next();
                if (ile.getNoOfPontsMissing() > 0) {
                    Iterator<Ile> voisins = modele.getIlesVoisines(ile).iterator();
                    while (voisins.hasNext()) {
                        Ile neighbor = voisins.next();
                        if (neighbor.getNoOfPontsMissing() > 0) {
                            if (isNextMove(ile, neighbor)) {
                                modele.ajouterPontEntre(ile, neighbor);
                                stateChecker.setPuzzleState();
                                NO_OF_TRIALS--;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        NO_OF_TRIALS--;
        return false;
    }

    private boolean isNextMove(Ile ile, Ile neighbor) {
        List<Pont> unsurePonts = new ArrayList<>();
        addUnsurePontAndResetPuzzleState(ile, neighbor, unsurePonts);
        while (makeSureMove()) 
            unsurePonts.add(modele.getDernierPontAjouter());
        boolean isNextMove = modele.estResolu();
        removePontsFromModel(unsurePonts);
        return isNextMove;
    }

    private void removePontsFromModel(List<Pont> unsurePonts) {
        Iterator<Pont> unsurePontsIterator = unsurePonts.iterator();
        while (unsurePontsIterator.hasNext()) 
            modele.supprimerPont(unsurePontsIterator.next(), false);
    }

    private void addUnsurePontAndResetPuzzleState(Ile ile, Ile neighbor, List<Pont> unsurePonts) {
        modele.ajouterPontEntre(ile, neighbor);
        stateChecker.setPuzzleState();
        unsurePonts.add(modele.getDernierPontAjouter());
    }

    /**
     * Goes through all iles of <code>modele</code> iteratively trying to
     * find a pont that must be added to an ile (and its neighbor ile).
     * 
     * @return true if sure move could be made, otherwise false.
     */
    private boolean makeSureMoveByUsingRules() {
        boolean sureMoveWasMade = false;
        Iterator<Ile> ileIter = modele.getIles().iterator();
        while (ileIter.hasNext() && !sureMoveWasMade) 
            sureMoveWasMade = addSurePontToIle(ileIter.next());
        return sureMoveWasMade;
    }

    /**
     * @param ile
     *            to be checked if (sure) pont can be added.
     * @return true if pont was added, otherwise false.
     */
    private boolean addSurePontToIle(Ile ile) {
        if (ile.getNoOfPontsMissing() != 0) {
            initListsOfNeighbors(ile);
            Ile neighbor = getNeighborToWhichPontMustBeBuilt(ile);
            if (neighbor != null) {
                modele.ajouterPontEntre(ile, neighbor);
                stateChecker.setPuzzleState();
                return true;
            }
        }
        return false;
    }

    /**
     * Initialize lists of neighbor iles of <code>ile</code> that are needed
     * in algorithms to determine if a sure pont can be built from
     * <code>ile</code>.
     * 
     * @param ile
     */
    private void initListsOfNeighbors(Ile ile) {
        neighbors = new ArrayList<>();
        neighborsToBuildPont = new ArrayList<>();
        neighborsToBuildDoublePont = new ArrayList<>();
        neighborsRequiringMoreThanOnePont = new ArrayList<>();
        neighborsRequiringMoreThanTwoPonts = new ArrayList<>();
        addNeighborsToLists(ile);
    }

    /**
     * Initialize lists of neighbor iles of <code>ile</code> that are needed
     * in algorithms to determine if a sure pont can be built from
     * <code>ile</code>.
     * 
     * @param ile
     */
    private void addNeighborsToLists(Ile ile) {
        for (Direction direction : Direction.values()) {
            Ile neighbor = modele.getIleVoisine(ile, direction);
            if (neighbor != null) {
                neighbors.add(neighbor);
                int noOfBuildablePonts = getNoOfBuildablePonts(ile, neighbor);
                if (noOfBuildablePonts > 0) {
                    neighborsToBuildPont.add(neighbor);
                    if (noOfBuildablePonts > 1) 
                        neighborsToBuildDoublePont.add(neighbor);
                    if (neighbor.getNoOfPontsRequired() > 1) 
                        neighborsRequiringMoreThanOnePont.add(neighbor);
                    if (neighbor.getNoOfPontsRequired() > 2) 
                        neighborsRequiringMoreThanTwoPonts.add(neighbor);
                }
            }
        }
    }

    private int getNoOfBuildablePonts(Ile ile, Ile neighbor) {
        Pont pontToNeighbor = modele.getPontEntre(ile, neighbor);
        if (pontToNeighbor == null && neighbor.getNoOfPontsMissing() > 1)
            return 2;
        if ((pontToNeighbor != null && !pontToNeighbor.isDouble() || pontToNeighbor == null) && neighbor.getNoOfPontsMissing() > 0)
            return 1;
        return 0;
    }

    /**
     * Checks if there is a neighbor of <code>ile</code> to which a pont must
     * be built from <code>ile</code> by using rules described in assignment.
     * <strong>Important:</strong> Returns null if no such neighbor can be found.
     * 
     * @param ile
     *            to be checked for a neighbor to which a pont must be built.
     * @return neighbor (ile) to which pont must be built if existing,
     *         otherwise null.
     */
    private Ile getNeighborToWhichPontMustBeBuilt(Ile ile) {
        int noOfBuildablePonts = neighborsToBuildPont.size() + neighborsToBuildDoublePont.size();
        if (2 * neighbors.size() <= ile.getNoOfPontsRequired() || ile.getNoOfPontsMissing() == noOfBuildablePonts) 
            return neighborsToBuildPont.get(0);
        if ((2 * neighbors.size() - 1 <= ile.getNoOfPontsRequired() || ile.getNoOfPontsMissing() == noOfBuildablePonts - 1) && !neighborsToBuildDoublePont.isEmpty())
            return neighborsToBuildDoublePont.get(0);
        if (neighborsRequiringMoreThanOnePont.size() == 1 && (ile.getNoOfPontsRequired() == 1 || ile.getNoOfPontsRequired() == 2 && ile.getNoOfPontsMissing() == 2)) 
            return neighborsRequiringMoreThanOnePont.get(0);  
        if (ile.getNoOfPontsRequired() == 2 && ile.getNoOfPontsMissing() == 2 && neighborsToBuildPont.size() == 2 && neighborsRequiringMoreThanTwoPonts.size() == 1) 
            return neighborsRequiringMoreThanTwoPonts.get(0);
        return null;
    }

}
