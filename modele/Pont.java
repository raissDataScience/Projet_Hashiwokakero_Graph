/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * This inner class models a pont on the field of the Hashiwokakeru puzzle.
 *
 *
 */
public class Pont implements FieldElement {
    Ile start;
    Ile end;
    boolean isDouble;
    private boolean isVertical;
    private int singlePontNo;
    private int pontNo;
    final GrilleModel outer;

    Pont(Ile islandA, Ile islandB, boolean isDouble, final GrilleModel outer) throws IllegalArgumentException {
        this.outer = outer;
        // check orientation of pont
        if (islandA.getCoords().x != islandB.getCoords().x && islandA.getCoords().y == islandB.getCoords().y) {
            isVertical = false;
        } else if (islandA.getCoords().x == islandB.getCoords().x && islandA.getCoords().y != islandB.getCoords().y) {
            isVertical = true;
        } else throw new IllegalArgumentException("Aucun pont ne peut être construit entre " + islandA + " et " + islandB + " car le pont ne serait ni horizontal ni vertical.");
        
        // set start and end of pont
        boolean islandAisStart = islandA.compareTo(islandB) < 0;
        this.start = islandAisStart ? islandA : islandB;
        this.end = islandAisStart ? islandB : islandA;
        this.isDouble = isDouble;
        pontNo = GrilleModel.pontCounter++; // to determine pont last inserted
    }

    int getPontNo() {
        return pontNo;
    }

    /**
     * Returns true if this <code>Pont
     *
     * @return true if this <code>Pont
     */
    public boolean isVertical() {
        return isVertical;
    }

    /**
     * Returns true if this <code>Pont
     *
     * @return true if this <code>Pont
     */
    public boolean isDouble() {
        return isDouble;
    }

    void setDouble(boolean isDouble) {
        if (isDouble) {
            if (!this.isDouble) {
                singlePontNo = pontNo; // lazy instantiation
                pontNo = GrilleModel.pontCounter++;
            }
        } else pontNo = singlePontNo;
        this.isDouble = isDouble;
    }

    /**
     * Gets the <code>Ile<code>this<code>this<code>this<code>this
     *
     * @return island connected by this pont north or west of the island it is
     *         connected to
     */
    public Ile getStart() {
        return start;
    }

    /**
     * Gets the <code>Ile<code>this<code>this<code>this<code>this
     *
     * @return island connected by this pont south or east of the island it is
     *         connected to
     */
    public Ile getEnd() {
        return end;
    }

    /**
     *
     * Gets the <code>Ile<code>this pont. If <code>island<code>this<code>island<code>this
     *
     * @param island
     *            representing one end of this pont
     * @return other end of this pont
     * @throws IllegalArgumentException
     *             if <code>island</code> is neither one nor the other end of
     *             <code>this</code> pont
     */
    public Ile getOtherEnd(Ile island) throws IllegalArgumentException {
        if (island.equals(start)) return end;
        if (island.equals(end)) return start;
        throw new IllegalArgumentException("L'île " + island + " n'est ni le début ni la fin du pont " + this + ".");
    }

    /**
     * Returns a <code>String<code>Pont
     */
    @Override
    public String toString() {
        return "( " + getStart() + ", " + getEnd() + " | " + isDouble + " )";
    }
    
}
