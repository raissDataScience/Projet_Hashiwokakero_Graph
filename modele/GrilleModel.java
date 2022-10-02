package modele;

import controller.GrilleStatut;
import modele.Position.Direction;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * This class models a Hashiwokakero puzzle and its current state.
 */
public class GrilleModel implements GrilleInterface {
    private int noOfIles;

    /**
     * 
     * Model of field including all current field elements, i.e. iles and
     * ponts. The <code>field.length</code> equals <code>height</code> of field,
     * for 0 &lt= i &lt <code>width</code> the <code>field[i].length</code> equals
     * <code>width</code>. If there is no ile or pont at
     * <code>field[y][x]</code>, <code>field[y][x]</code> = <code>null</code>.
     * 
     */
    private final FieldElement[][] field;

    private GrilleStatut.Statut puzzleState = GrilleStatut.Statut.INITIAL;
    private Pont lastInsertedPont;
    public static int pontCounter; // needed to determine lastInsertedPont

    /**
     * 
     * Constructs an instance of an empty <code>width x height</code> Hashiwokakeru
     * puzzle, i.e. a Hashiwokakeru puzzle with <code>width</code> columns and
     * <code>height</code> rows without any iles or ponts. Iles must be
     * added after construction.
     * 
     * @param width
     *            of the Hashiwokakeru puzzle
     * @param height
     *            of the Hashiwokakeru puzzle
     */
    public GrilleModel(int width, int height) {
        field = new FieldElement[height][width];
    }

    @Override
    public int getWidth() {
            return field[0].length;
    }

    @Override
    public int getHeight() {
        return field.length;
    }

    @Override
    public int getNbIles() {
        return noOfIles;
    }

    @Override
    public GrilleStatut.Statut getStatut() {
        return puzzleState;
    }

    @Override
    public boolean estResolu() {
        return puzzleState == GrilleStatut.Statut.RESOLU;
    }

    @Override
    public boolean nonResolu() {
        return puzzleState == GrilleStatut.Statut.INITIAL;
    }

    @Override
    public boolean estImpossible() {
        return puzzleState == GrilleStatut.Statut.NON_RESOLU;
    }

    @Override
    public boolean estErrone() {
        return puzzleState == GrilleStatut.Statut.ERRONE;
    }

    @Override
    public void setStatut(GrilleStatut.Statut state) {
        this.puzzleState = state;
    }

    @Override
    public FieldElement getFieldElementAt(int x, int y) throws IllegalArgumentException {
        if (!estValidePosition(x, y)) { // (x,y) on field
            int maxX = getWidth() - 1;
            int maxY = getHeight() - 1;
            throw new IllegalArgumentException("(" + x + ", " + y + ") are not valid coordinates. x needs to be between 0 and " + maxX + ", y betweeen 0 and " + maxY + ".");
        }
        return field[y][x];
    }

    @Override
    public Ile getIleAt(int x, int y) throws IllegalArgumentException {
        if (!ileAt(x, y)) throw new IllegalArgumentException("Il n'y a pas d'île au (" + x + ", " + y + ").");
        return (Ile) getFieldElementAt(x, y);
    }

    @Override
    public Pont getPontAt(int x, int y) throws IllegalArgumentException {
        if (!pontAt(x, y)) throw new IllegalArgumentException("Il n'y a pas de pont à (" + x + ", " + y + ").");
        return (Pont) getFieldElementAt(x, y);
    }

    @Override
    public boolean ileAt(int x, int y) throws IllegalArgumentException {
        return getFieldElementAt(x, y) instanceof Ile;
    }

    @Override
    public boolean pontAt(int x, int y) throws IllegalArgumentException {
        return getFieldElementAt(x, y) instanceof Pont;
    }

    @Override
    public boolean estVide(int x, int y) throws IllegalArgumentException {
        return getFieldElementAt(x, y) == null;
    }

    @Override
    public boolean estValidePosition(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    @Override
    public boolean estValIlePosition(int x, int y) {
        return (estValidePosition(x, y) && !(getFieldElementAt(x, y) instanceof Ile) && (!estValidePosition(x - 1, y) || !(getFieldElementAt(x - 1, y) instanceof Ile)) && (!estValidePosition(x + 1, y) || !(getFieldElementAt(x + 1, y) instanceof Ile)) && (!estValidePosition(x, y - 1) || !(getFieldElementAt(x, y - 1) instanceof Ile)) && (!estValidePosition(x, y + 1) || !(getFieldElementAt(x, y + 1) instanceof Ile)));
    }

    @Override
    public void addIleAt(int x, int y) throws IllegalArgumentException {
        ajouterIleAt(x, y, 0);
    }

    @Override
    public void ajouterIleAt(int x, int y, int noOfPonts) throws IllegalArgumentException {
        if (!estValidePosition(x, y)) {
            int maxX = getWidth() - 1;
            int maxY = getHeight() - 1;
            throw new IllegalArgumentException("(" + x + ", " + y + ") ne sont pas des coordonnées valides. x doit être compris entre 0 et " + maxX + ". y doit être compris entre 0 et " + maxY + ".");
        }
        if (!estValIlePosition(x, y)) 
            throw new IllegalArgumentException("Ile ne peut être ajouté au (" + x + ", " + y + ") car la distance entre îles devraient être supérieur à 1.");

        if (getFieldElementAt(x, y) instanceof Pont) 
            throw new IllegalArgumentException("L'île ne peut être ajouté au (" + x + ", " + y + ") car il exite un point à cette coordonnées.");

        field[y][x] = new Ile(x, y, this);
        noOfIles++;
        getIleAt(x, y).setNoOfPontsRequired(noOfPonts);
    }



    @Override
    public List<Ile> getIles() {
        List<Ile> iles = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (ileAt(x, y)) iles.add((Ile) getFieldElementAt(x, y));
            }
        }
        return iles;
    }

    @Override
    public Ile getIleVoisine(Ile ile, Direction direction) throws IllegalArgumentException {
        if (ile == null || direction == null)
            throw new IllegalArgumentException("Ile et la direction ne devraient pas vide.");
        Pont pont = getPont(ile, direction);
        return pont != null ? pont.getOtherEnd(ile) : getNeighbourIleNotConnected(ile, direction);
    }

    @Override
    public List<Ile> getIlesVoisines(Ile ile) throws IllegalArgumentException {
        List<Ile> neighbourIles = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Ile neighbor = getIleVoisine(ile, direction);
            if (neighbor != null) 
                neighbourIles.add(neighbor);
        }
        return neighbourIles;
    }

    private Ile getNeighbourIleNotConnected(Ile ile, Direction direction) {
        Position coords = ile.getCoords().getNextCoordsIn(direction);
        while (estValidePosition(coords.x, coords.y) && getFieldElementAt(coords.x, coords.y) == null) 
            // move one more step in direction
            coords = coords.getNextCoordsIn(direction);

        if (estValidePosition(coords.x, coords.y) && ileAt(coords.x, coords.y)) 
            // there is an ile at the (coords.x, coords.y)
            return (Ile) getFieldElementAt(coords.x, coords.y);

        return null;
    }

    @Override
    public Pont getPont(Ile ile, Direction direction) throws IllegalArgumentException {
        if (ile == null || direction == null) throw new IllegalArgumentException("Ile et la direction ne devraient pas vide.");
        // check coords right next to ile in specified direction
        Position coordsBetweenIles = ile.getCoords().getNextCoordsIn(direction);
        if (estValidePosition(coordsBetweenIles.x, coordsBetweenIles.y) && pontAt(coordsBetweenIles.x, coordsBetweenIles.y)) {
            Pont pont = (Pont) getFieldElementAt(coordsBetweenIles.x, coordsBetweenIles.y);
            // check that pont is not orthogonal to direction, i.e. does connect ile
            if (ile.equals(pont.getStart()) || ile.equals(pont.getEnd())) return pont;
        }
        return null;
    }

    @Override
    public Pont getPontEntre(Ile ile, Ile otherIle) throws IllegalArgumentException {
            if (ile == null || otherIle == null)
                    throw new IllegalArgumentException("L'un des îles est nulle.");
            // check that iles are neighbors
            Direction directionOfOtherIle = ile.getCoords().getDirectionOfCoord(otherIle.getCoords());
            if (!otherIle.equals(getIleVoisine(ile, directionOfOtherIle)))
                    throw new IllegalArgumentException("Les îles ne sont pas voisines les unes des autres.");
            // get pont if existing
            Position coordsBetweenIles = ile.getCoords().getNextCoordsIn(directionOfOtherIle);
            return pontAt(coordsBetweenIles.x, coordsBetweenIles.y) ? (Pont) getFieldElementAt(coordsBetweenIles.x, coordsBetweenIles.y) : null;
    }

    @Override
    public Pont getDernierPontAjouter() {
        return lastInsertedPont;
    }

    @Override
    public boolean ajouterPont(Ile ile, Direction direction) throws IllegalArgumentException {
        Ile neighbor = getIleVoisine(ile, direction);
        if (neighbor == null)
                throw new IllegalArgumentException(
                                "Il n'y pas de voisin à " + direction + " de " + ile + " pour y ajouter un pont.");
        return ajouterPontEntre(ile, neighbor);
    }

    @Override
    public boolean ajouterPontEntre(Ile ile, Ile otherIle) throws IllegalArgumentException {
        Pont pont = getPontEntre(ile, otherIle);
        if (pont == null) 
            return ajouterPontEntre(ile, otherIle, false);
        else if (!pont.isDouble()) { // single pont already existing
            pont.setDouble(true);
            lastInsertedPont = pont; // update pont last inserted // TODO: incapsulate in setDouble method
            return true;
        } else return false;

    }

    @Override
    public boolean ajouterPontEntre(Ile ile, Ile otherIle, boolean doublePont) throws IllegalArgumentException {
        if (ile == null || otherIle == null)
            throw new IllegalArgumentException("Les îles ne devraient pas nuls.");
        // check if iles are neighbors
        Direction directionOfOtherIle = ile.getCoords().getDirectionOfCoord(otherIle.getCoords());
        if (!otherIle.equals(getIleVoisine(ile, directionOfOtherIle)))
            throw new IllegalArgumentException("Aucun pont ne peut être ajouté entre " + ile + " et " + otherIle + " car il y a un ile ou un pont entre.");
        // check if pont already exists
        if (getPont(ile, directionOfOtherIle) != null) 
            throw new IllegalArgumentException("Un pont entre " + ile + " and " + otherIle + " existe déjà.");
        // create pont
        Pont pont = new Pont(ile, otherIle, doublePont, this);
        Ile start = pont.getStart();
        Ile end = pont.getEnd();
        for (int x = start.getCoords().x + 1; x < end.getCoords().x; x++) 
            field[start.getCoords().y][x] = pont;

        for (int y = start.getCoords().y + 1; y < end.getCoords().y; y++) 
            field[y][start.getCoords().x] = pont;

        lastInsertedPont = pont; // update last inserted pont // TODO: incapsulate in pont creation
        return true;
    }

    @Override
    public void ajouterPontEntreIles(Ile existingIle, Ile newIle, boolean isDouble) throws IllegalArgumentException {
        ajouterPontEntre(existingIle, newIle, isDouble);
        int noOfPontsAdded = isDouble ? 2 : 1;
        existingIle.setNoOfPontsRequired(existingIle.getNoOfPontsRequired() + noOfPontsAdded);
        newIle.setNoOfPontsRequired(newIle.getNoOfPontsRequired() + noOfPontsAdded);
    }

    @Override
    public boolean supprimerPont(Ile ile, Direction direction) throws IllegalArgumentException {
        Ile otherIle = getIleVoisine(ile, direction);
        return supprimerPontEntre(ile, otherIle);
    }

    @Override
    public boolean supprimerPontEntre(Ile ile, Ile otherIle) {
        return supprimerPontEntre(ile, otherIle, false);
    }

    @Override
    public boolean supprimerPontEntre(Ile ile, Ile otherIle, boolean doublePont) {
        Pont pont = getPontEntre(ile, otherIle);
        // check if pont exists and what kind of pont it is
        if (pont != null) {
            if (pont.isDouble() && !doublePont) 
                pont.setDouble(false); // only a single pont of a double pont is to be removed
            else { // remove whole pont from field
                Ile start = pont.getStart();
                Ile end = pont.getEnd();
                for (int x = start.getCoords().x + 1; x < end.getCoords().x; x++) 
                    field[start.getCoords().y][x] = null;
                for (int y = start.getCoords().y + 1; y < end.getCoords().y; y++) 
                    field[y][start.getCoords().x] = null;
            }
            if (pont.equals(lastInsertedPont)) 
                updateLastInsertedPont();
            return true;
        }
        return false;
    }

    @Override
    public boolean supprimerPont(Pont pont, boolean doublePont) throws IllegalArgumentException {
        return supprimerPontEntre(pont.getStart(), pont.getEnd(), doublePont);
    }

    @Override
    public boolean supprimerPontEntreIles(Pont oldPont, boolean doublePont) throws IllegalArgumentException {
        int noOfPontsRemoved = oldPont.isDouble() && doublePont ? 2 : 1;
        boolean pontWasRemoved = supprimerPont(oldPont, doublePont);
        if (pontWasRemoved) {
            Ile start = oldPont.getStart();
            Ile end = oldPont.getEnd();
            start.setNoOfPontsRequired(start.getNoOfPontsRequired() - noOfPontsRemoved);
            end.setNoOfPontsRequired(end.getNoOfPontsRequired() - noOfPontsRemoved);
        }
        return pontWasRemoved;
    }

    private void updateLastInsertedPont() {
        lastInsertedPont = null;
        // determine pont with highest number (= pont last inserted)
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (pontAt(x, y) && (lastInsertedPont == null || lastInsertedPont.getPontNo() < ((Pont) getFieldElementAt(x, y)).getPontNo()))
                    lastInsertedPont = (Pont) getFieldElementAt(x, y);
            }
        }
    }

    @Override
    public void supprimerPonts() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                // check if pontt at (x, y)
                if (pontAt(x, y)) { // remove pont
                    Pont pont = (Pont) getFieldElementAt(x, y);
                    supprimerPontEntre(pont.getStart(), pont.getEnd(), true);
                }
            }
        }
    }

    /**
     * Returns a <code>String</code> representing the puzzle.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getFieldElementAt(x, y) == null) 
                    stringBuilder.append("*");
                else if (ileAt(x, y)) {
                    Ile ile = (Ile) getFieldElementAt(x, y);
                    stringBuilder.append(ile.getNoOfPontsRequired());
                } else { // pont at (x, y)
                    Pont pont = (Pont) getFieldElementAt(x, y);
                    // check kind of pont
                    if (pont.isVertical()) {
                        if (pont.isDouble()) stringBuilder.append("||");
                        else stringBuilder.append("|");
                    } else {
                        if (pont.isDouble()) stringBuilder.append("=");
                        else stringBuilder.append("-");
                    }
                }
                stringBuilder.append("\t"); // new row
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
