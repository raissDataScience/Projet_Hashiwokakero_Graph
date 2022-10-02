/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * This inner class models an ile on the field of the Hashiwokakeru puzzle.
 *
 *
 */
public class Ile implements FieldElement, Comparable<Ile> {

    final Position coords;
    int noOfPontsRequired;
    final GrilleModel outer;

    Ile(int x, int y, final GrilleModel outer) {
        this.outer = outer;
        this.coords = new Position(x, y);
    }

    /**
     *
     * Iles are compared by their coordinates. The model guarantees that there
     * are never two iles with the same coordinates. <strong>Important:</strong>
     * The equals and hashcode method are not overriden since there are never two
     * iles with the same coordinates and therefore they are still valid.
     *
     * @param otherIle
     *            to compare <code>this</code> ile to
     * @result a negative integer, zero, or a positive integer as this ile is
     *         less than, equal to, or greater than the specified ile
     */
    @Override
    public int compareTo(Ile otherIle) {
        return getCoords().compareTo(otherIle.getCoords());
    }

    /**
     * Returns a <code>String<code>Ile
     */
    @Override
    public String toString() {
        return "( " + coords.x + ", " + coords.y + " | " + noOfPontsRequired + " )";
    }

    /**
     * Gets the number of ponts required by this instance of an
     * <code>Ile</code>.
     * 
     * @return number of ponts required by this instance of an <code>Ile</code>
     */
    public int getNoOfPontsRequired() {
        return noOfPontsRequired;
    }

    /**
     * Sets the number of ponts required by this instance of an
     * <code>Ile
     */
    void setNoOfPontsRequired(int noOfPonts) {
        this.noOfPontsRequired = noOfPonts;
    }

    /**
     * Gets an instance of <code>Position
     * 
     * @return this island's <code>Coordinates</code>
     */
    public Position getCoords() {
        return coords;
    }

    public int getX() {
        return getCoords().x;
    }

    public int getY() {
        return getCoords().y;
    }

    /**
     * Gets the number of ponts missing, i.e. the number of ponts that yet need
     * to be added, by this instance of an <code>Ile
     *
     * @return number of ponts missing
     */
    public int getNoOfPontsMissing() {
        int noOfPontsMissing = noOfPontsRequired;
        for (Position.Direction direction : Position.Direction.values()) {
            Pont pont = outer.getPont(this, direction);
            if (pont != null) 
                noOfPontsMissing = pont.isDouble() ? noOfPontsMissing - 2 : noOfPontsMissing - 1;
        }
        return noOfPontsMissing;
    }
    
}
