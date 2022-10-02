package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import modele.Position;
import modele.Position.Direction;
import modele.GrilleModel;
import modele.Pont;
import modele.Ile;
import modele.GrilleInterface;

/**
 * Class providing methods for generating a relatively random Hashiwokakero
 * puzzle that can be solved. Randomness is confined by constraints such as the
 * number of iles that the puzzle should have or the width and height of the
 * puzzle.
 */
public class GrilleGenerator {

    private static final int 
            MIN_WIDTH = 4, 
            MIN_HEIGHT = 4, 
            MAX_WIDTH = 15, 
            MAX_HEIGHT = 15, 
            MIN_NO_OF_ISLANDS = 2;
    private GrilleInterface hashiModel; // model created

    private static final Random random = new Random();
    // to generate width, height, noOfIles, coords of iles and type of ponts

    private List<Ile> pontableIles;
    // iles from or to which a pont can still be built, from these iles
    // iles are picked to build a pont to a new ile

    /**
     * Génère les dimensions de la grille dont dépend le nombre d'îles générées aléatoirement
     * @return 
     */
    public GrilleInterface getPuzzleSituationModel() {
        int width = random.nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
        int height = random.nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
        int noOfIles = getRandNoOfIles(width, height);
        return getPuzzleSituationModel(width, height, noOfIles);
    }

    private static int getRandNoOfIles(int width, int height) {
        int minNoOfIles = width < height ? width : height;
        int maxNoOfIles = getMaxNoOfIles(width, height);
        minNoOfIles = minNoOfIles > maxNoOfIles ? maxNoOfIles : minNoOfIles;
        return random.nextInt(maxNoOfIles - minNoOfIles + 1) + minNoOfIles;
    };

    private static int getMaxNoOfIles(int width, int height) {
        return width * height / 5;
    }

    /**
     * Generates a random, solvable <code>width</code> x <code>height</code>
     * Hashiwokakero puzzle with random number of iles in interval [min(width,
     * height), max(4, width*height / 5)].
     * 
     * @param width  of puzzle to be generated
     * @param height of puzzle to be generated
     * @return A Hashiwokakero puzzle that can be solved
     * @throws IllegalArgumentException if <code>width</code> or <code>height</code>
     *                                  is not in [4, 25]
     */
    public GrilleInterface getPuzzleSituationModel(int width, int height) throws IllegalArgumentException {
        int noOfIles = getRandNoOfIles(width, height);
        return getPuzzleSituationModel(width, height, noOfIles);
    };

    private static boolean isPuzzleConfigurationValid(int width, int height) {
        return MIN_WIDTH <= width && width <= MAX_WIDTH && MIN_HEIGHT <= height && height <= MAX_HEIGHT;
    };

    /**
     * Generates a random, solvable <code>width</code> x <code>height</code>
     * Hashiwokakero puzzle with <code>noOfIles</code> number of iles.
     */
    public GrilleInterface getPuzzleSituationModel(int width, int height, int noOfIles) throws IllegalArgumentException {
        // check if width and height are valid
        if (!isPuzzleConfigurationValid(width, height))
                throw new IllegalArgumentException(
                                "La configuration de la carte n'est pas valide pour générer un puzzle. La largeur doit être comprise entre " + MIN_WIDTH
                                                + " et " + MAX_WIDTH + ". La hauteur doit être comprise entre " + MIN_HEIGHT + " et " + MAX_HEIGHT + ".");
        // check if noOfIles is valid
        if (MIN_NO_OF_ISLANDS > noOfIles || noOfIles > getMaxNoOfIles(width, height))
                throw new IllegalArgumentException(
                                "La configuration de la carte n'est pas valide pour générer un puzzle. Le nombre de fichiers doit être compris entre "
                                                + MIN_NO_OF_ISLANDS + " et " + getMaxNoOfIles(width, height) + ".");
        // create model and populate it with iles and ponts until requirements are met
        while (hashiModel == null || hashiModel.getNbIles() != noOfIles) {
            hashiModel = new GrilleModel(width, height);
            addSolvedHashiPuzzleToModel(noOfIles);
        }
        hashiModel.supprimerPonts();
        return hashiModel;
    }

    // populate model with iles and ponts until requirements are met
    private void addSolvedHashiPuzzleToModel(int noOfIles) {
        pontableIles = new ArrayList<>();
        // put first ile on field
        addIleAtRandomPositionToModel();
        // rajouter les iles manquant afin que la grille soit solvable
        while (hashiModel.getNbIles() < noOfIles && !pontableIles.isEmpty()) 
            addIleWithPontToExistingIle();
    }

    private void addIleAtRandomPositionToModel() {
        int x = random.nextInt(hashiModel.getWidth());
        int y = random.nextInt(hashiModel.getHeight());
        addIleToModelAndPontableIles(x, y);
    }

    // rajoute d'ile au coordonnées x,y du modèle puis met à jour le tableau pontableIles
    private void addIleToModelAndPontableIles(int x, int y) {
        hashiModel.addIleAt(x, y);
        pontableIles.add(hashiModel.getIleAt(x, y));
    }

    private void addIleWithPontToExistingIle() {
        // pick ile from pontableIles
        int randIleIndex = random.nextInt(pontableIles.size());
        Ile existingIle = pontableIles.get(randIleIndex);
        List<Position> validNeighborIleCoords = getValidNeighborIleCoords(existingIle);
        if (validNeighborIleCoords.isEmpty()) 
            pontableIles.remove(randIleIndex);
        else { // new ile including pont to existing one can be added
            Position coords = validNeighborIleCoords.get(random.nextInt(validNeighborIleCoords.size()));
            // check if pont must be split to add ile
            if (hashiModel.pontAt(coords.x, coords.y)) 
                addIleBySplittingPontWithPontToExIles(existingIle, coords);
            else addIleWithPontToExIle(existingIle, coords);
        }
    }

    // rajouter d'Ile avec pont à une ile existant
    private void addIleWithPontToExIle(Ile existingIle, Position coords) {
        addIleToModelAndPontableIles(coords.x, coords.y);
        Ile newIle = hashiModel.getIleAt(coords.x, coords.y);
        boolean isDouble = random.nextBoolean(); 
        hashiModel.ajouterPontEntreIles(existingIle, newIle, isDouble);
    }

    private void addIleBySplittingPontWithPontToExIles(Ile existingIle, Position coords) {
        Pont oldPont = hashiModel.getPontAt(coords.x, coords.y);
        hashiModel.supprimerPontEntreIles(oldPont, true);
        addIleWithPontToExIle(existingIle, coords);
        Ile newIle = hashiModel.getIleAt(coords.x, coords.y);
        hashiModel.ajouterPontEntreIles(oldPont.getStart(), newIle, oldPont.isDouble());
        hashiModel.ajouterPontEntreIles(oldPont.getEnd(), newIle, oldPont.isDouble());
    }

    /**
     * Generates a list of coordinates in a random direction from the
     * <code>existingIle</code> where a neighbor ile to
     * <code>existingIle</code> can be placed.
     * 
     * <p>
     * Direction is chosen randomly out of all possible directions but when no
     * suitable coordinates can be found, direction is removed from possible
     * directions and another direction is chosen out of all possible directions.
     * This continues until either suitable coordinates are found or no possible
     * direction remains in which case an empty list of coordinates is returned.
     * </p>
     * 
     * <p>
     * A neighbor ile to the <code>existingIle</code> can be placed at a pair
     * of coordinates (x, y) if there would be no pont crossing between the
     * neighbor ile and the <code>existingIle</code> and if the (vertical and
     * horizontal) distance of the neighbor ile to other iles is greater 1.
     * <b>Note that there could be a pont crossing at (x, y) since the "between"
     * excludes the coordinates of the iles themselves.</b>
     * </p>
     * 
     * @param existingIle that is used for generating coordinates
     * @return A list of coordinates on which a neighbor ile to the existing
     *         ile can be placed
     */
    private List<Position> getValidNeighborIleCoords(Ile existingIle) {
        List<Position> validNeighborIleCoords = Collections.EMPTY_LIST;
        Iterator<Direction> neighborDirectionsIter = getDirectsWithoutPontsRandOrd(existingIle).iterator();
        // check directions from existingIle to find coords in which pont to new ile can be built until no direction left or possible direction found
        while (neighborDirectionsIter.hasNext() && validNeighborIleCoords.isEmpty()) 
            validNeighborIleCoords = getValidNeighborIleCoords(existingIle, neighborDirectionsIter.next());
        return validNeighborIleCoords;
    }

    // returns a shuffled list of directions in which no pont has been built from
    // the ile
    private List<Direction> getDirectsWithoutPontsRandOrd(Ile ile) {
        List<Direction> directionsWithoutPonts = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (hashiModel.getPont(ile, direction) == null) 
                directionsWithoutPonts.add(direction);
        }
        Collections.shuffle(directionsWithoutPonts);
        return directionsWithoutPonts;
    }

    /**
     * Generates a list of coordinates in the <code>direction</code> from the
     * <code>existingIle</code> where a neighbor ile to
     * <code>existingIle</code> can be placed.
     * 
     * A neighbor ile to the <code>existingIle</code> can be placed at a pair
     * of coordinates (x, y) if there would be no pont crossing between the
     * neighbor ile and the <code>existingIle</code> and if the (vertical and
     * horizontal) distance of the neighbor ile to other iles is greater 1.
     * <b>Note that there could be a pont crossing at (x, y) since the "between"
     * excludes the coordinates of the iles themselves.</b>
     * 
     * @param existingIle that is used for generating coordinates
     * @param direction      in which coordinates are searched for
     * @return A list of coordinates in <code>direction</code> on which a neighbor
     *         ile to the existing ile can be placed
     */
    private List<Position> getValidNeighborIleCoords(Ile existingIle, Direction direction) {
        List<Position> validNeighborIleCoords = new ArrayList<>();
        // add all coordinates that a neighbor ile could be built on to list
        Position coords = existingIle.getCoords().getNextCoordsIn(direction);
        while (hashiModel.estValidePosition(coords.x, coords.y) && !hashiModel.ileAt(coords.x, coords.y)) {
            if (hashiModel.estValIlePosition(coords.x, coords.y)) 
                validNeighborIleCoords.add(coords);
            if (hashiModel.pontAt(coords.x, coords.y)) 
                break;
            coords = coords.getNextCoordsIn(direction);
        }
        return validNeighborIleCoords;
    }

}
