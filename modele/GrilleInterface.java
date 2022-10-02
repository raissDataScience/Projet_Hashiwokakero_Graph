package modele;

import controller.GrilleStatut;
import java.util.List;


/**
 * 
 * The <code>GrilleInterface interface should be implemented by a
 * class modeling (the current state of) a Hashiwokakeru puzzle.
 * 
 * <p>
 * A lot of methods of the <code>GrilleInterface<code> use coordinates (x,
 * y). x represents the column number and y the row number, therefore, x needs
 * to be greater than or equal to 0 and less than width of the (field of the)
 * puzzle and y needs to be greater than or equal to 0 and less than height of
 * the (field of the) puzzle. <strong>Important:
 */
public interface GrilleInterface {

	/**
	 *
	 * Gets the width, i.e. the number of columns, of the puzzle.
	 * 
	 * @return width the puzzle
	 */
	int getWidth();

	/**
	 *
	 * Gets the heigth, i.e. the number of rows, of the puzzle.
	 * 
	 * @return height of the puzzle
	 */
	int getHeight();

	/**
	 * 
	 * Gets the number of iles that the puzzle has.
	 * 
	 * @return number of iles of the puzzle
	 */
	int getNbIles();

	/**
	 * 
	 * Gets the state of the puzzle.
	 * 
	 * @return state of the puzzle
	 */
	GrilleStatut.Statut getStatut();

	/**
	 * Returns <code>true{@link SolutionType#SOLVED SolutionType.SOLVED}, otherwise <code>false
	 * 
	 * @return true if the puzzle is solved, otherwise false
	 */
	boolean estResolu();

	/**
	 * 
	 * Returns <code>true{@link SolutionType#NOT_YET_SOLVED SolutionType.NOT_YET_SOLVED}, otherwise
	 * <code>false
	 * 
	 * @return true if puzzle is yet to be solved, otherwise false
	 */
	boolean nonResolu();

	/**
	 * 
	 * Returns <code>true{@link SolutionType#UNSOLVABLE SolutionType.UNSOLVABLE}, otherwise
	 * <code>false
	 * 
	 * @return true if puzzle is unsolvable, otherwise false
	 */
	boolean estImpossible();

	/**
	 * 
	 * Returns <code>true{@link SolutionType#CONTAINS_ERROR SolutionType.CONTAINS_ERROR}, otherwise
	 * <code>false
	 * 
	 * @return true if puzzle contains error, otherwise false
	 */
	boolean estErrone();

	/**
	 * 
	 * Sets the state of the puzzle.
	 * 
	 * @param state
	 *            of the puzzle
	 */
	void setStatut(GrilleStatut.Statut state);

	/**
	 * 
	 * Gets <code>FieldElement at coordinates (x, y).
	 * <code>FieldElement<code>Ile<code>Pont<code>null<code>FieldElement<p>
	 * <strong>Important:<code>Ile<code>Pont 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return <code>FieldElement<code>Ile<code>Pont<code>null
	 */
	FieldElement getFieldElementAt(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Gets <code>Ile
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return <code>Ile
	 * 
	 * @throws IllegalArgumentException
	 *             if (x, y) are not valid coordinates
	 *             ({@link #estValidePosition(int, int) estValidePosition}
	 *             method returns <code>false<code>Ile
	 */
	Ile getIleAt(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Gets <code>Pont
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return <code>Pont
	 * 
	 * @throws IllegalArgumentException
	 *             if (x, y) are not valid coordinates
	 *             ({@link #estValidePosition(int, int) estValidePosition}
	 *             method returns <code>false<code>Pont
	 */
	Pont getPontAt(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Returns <code>true<code>Ile<code>false
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return true if there is an Ile at coordinates (x, y), otherwise false
	 * @throws IllegalArgumentException
	 *             if (x, y) are not valid coordinates
	 *             ({@link #estValidePosition(int, int) estValidePosition}
	 *             method returns <code>false
	 */
	boolean ileAt(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Returns <code>true<code>Pont<code>false
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return true if there is a Pont at coordinates (x, y), otherwise false
	 * @throws IllegalArgumentException
	 *             if (x, y) are not valid coordinates
	 *             ({@link #estValidePosition(int, int) estValidePosition}
	 *             method returns <code>false
	 */
	boolean pontAt(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Returns <code>true<code>Pont<code>Ile<code>false
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return true if there is nothing (<code>null</code>) at coordinates (x, y),
	 *         otherwise false
	 * @throws IllegalArgumentException
	 *             if (x, y) are not valid coordinates
	 *             ({@link #estValidePosition(int, int) estValidePosition}
	 *             method returns <code>false
	 */
	boolean estVide(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Returns <code>true</code> if coordinates (x, y) are valid, i.e. if x is be
	 * greater than or equal to 0 and less than width of the puzzle and y is greater
	 * than or equal to 0 and less than height of the puzzle.
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return true if coordinates (x, y) are valid, otherwise false
	 */
	boolean estValidePosition(int x, int y);

	/**
	 * 
	 * Returns <code>true<code>Ile can be added to puzzle at
	 * coordinates (x, y), i.e. {@link #estValidePosition(int, int)
	 * estValidePosition} method returns <code>true
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @return true if Ile can be added to puzzle at coordinates (x, y)
	 */
	boolean estValIlePosition(int x, int y);

	/**
	 * 
	 * Adds an <code>Ile<code>Ile{@link #estValIlePosition(int, int)
	 * estValIlePosition} method returns <code>true<code>Pont<code>IllegalArgumentException
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @throws IllegalArgumentException
	 *             if (x, y) is not a valid <code>Ile<code>Pont
	 */
	void addIleAt(int x, int y) throws IllegalArgumentException;

	/**
	 * 
	 * Adds an <code>Ile<code>noOfPont<code>Ile{@link #estValIlePosition(int, int) estValIlePosition} method
	 * returns <code>true<code>Pont<code>IllegalArgumentException
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param noOfPonts
	 *            that ile requires
	 * @throws IllegalArgumentException
	 *             if (x, y) is not a valid <code>Ile<code>Pont
	 */
	void ajouterIleAt(int x, int y, int noOfPonts) throws IllegalArgumentException;

	/**
	 * 
	 * Gets an instance a <code>List&lt=Ile> containing all the iles of
	 * the puzzle (at the point in time the method is used) ordered naturally, i.e.
	 * by column first and row second. If there are no iles, the list is going to
	 * be empty.
	 * 
	 * @return a list of all the iles of the puzzle
	 */
	List<Ile> getIles();

        /**
         * 
         * Gets the neighbor ile of <code>ile</code> in the <code>direction</code>
         * given, i.e. the ile that is first encountered when stepping into the
         * <code>direction</code> given starting at the coordinates right next to the
         * <code>ile</code> in the <code>direction</code> given. The
         * <code>ile</code> is possibly connected to the neighbor ile by a
         * <code>Pont</code> but this is not necessarily the case.
         * 
         * <p>
         * <strong>Important:</strong> If a crossing <code>Pont</code>, i.e. a
         * <code>Pont</code> that does not connect the <code>ile</code> with a
         * neighbor ile, is encountered while stepping into the
         * <code>direction</code> given, the method returns <code>null</code> since the
         * <code>Pont</code> acts as a border hiding a possibly existing neighbor
         * ile. In case a neighbor ile is encountered, it is returned even if it
         * does not miss any ponts and therefore cannot be connected to the
         * <code>ile</code> without removing a pont. <code>null</code> is also
         * returned if no neighbor ile is found, i.e. the coordinates become invalid
         * ({@link #isValidFieldPosition(int, int) isValidFieldPosition} method returns
         * <code>false</code>) by stepping into the <code>direction</code> given.
         * </p>
         * 
         * @param ile
         *            the neighbor ile to this ile is to be found
         * @param direction
         *            the direction in which the neighbor ile should be searched for
         * @return neighbor ile if existing, otherwise null
         * @throws IllegalArgumentException
         *             if ile or direction is null
         */
        Ile getIleVoisine(Ile ile, Position.Direction direction) throws IllegalArgumentException;

        /**
         * 
         * Gets a <code>List&lt=Ile></code> the neighbor iles of the
         * <code>ile</code> in every possible direction (north, east, south and
         * west).
         * 
         * <p>
         * A neighbor ile of the <code>ile</code> is an ile first encountered
         * when stepping into a direction D starting at the coordinates right next to
         * the <code>ile</code> in the direction D. The <code>ile</code> is
         * possibly connected to the neighbor ile by a <code>Pont</code> but this
         * is not necessarily the case.
         * </p>
         * 
         * <p>
         * <strong>Important:</strong> If a crossing <code>Pont</code>, i.e. a
         * <code>Pont</code> that does not connect the <code>ile</code> with a
         * neighbor ile, is encountered while stepping into a direction, no neighbor
         * ile is added to the <code>List&lt=Ile></code> of neighbor iles since
         * the <code>Pont</code> acts as a border hiding the possibly existing
         * neighbor ile. This is also true if no neighbor ile is found, i.e. the
         * coordinates become invalid ({@link #isValidFieldPosition(int, int)
         * isValidFieldPosition} method returns <code>false</code>) by stepping into the
         * direction. In case a neighbor ile is encountered, it is added to the
         * <code>List&lt=Ile></code> of neighbor iles that is returned by the
         * method even if it does not miss any ponts and therefore cannot be connected
         * to the <code>ile</code> without removing a pont. If no neighbor iles
         * are found, an empty <code>List&lt=Ile></code> is returned.
         * </p>
         * 
         * @param ile
         *            the neighbor iles to this ile are to be found
         * @return list of neighbor iles
         * @throws IllegalArgumentException
         *             if ile is null
         */
	List<Ile> getIlesVoisines(Ile ile) throws IllegalArgumentException;
        
	/**
	 * 
	 * Gets the pont of <code>ile<code>direction<strong>Important:<code>null
	 * 
	 * @param ile
	 *            connected by the pont
	 * @param direction
	 *            of the pont
	 * @return pont of ile in the direction if existing, otherwise null
	 * @throws IllegalArgumentException
	 *             if ile or direction is null
	 */
	Pont getPont(Ile ile, Position.Direction direction) throws IllegalArgumentException;

	/**
	 * 
	 * Gets the pont between the <code>ile</code> and the
	 * <code>otherIle</code>. <strong>Important:</strong> Returns
	 * <code>null</code> if there is no such pont.
	 * 
	 * @param ile
	 *            connected by the pont
	 * @param otherIle
	 *            connected by the pont
	 * @return pont between iles if existing, otherwise null
	 * @throws IllegalArgumentException
	 *             if one of the iles is null or the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles)
	 */
	Pont getPontEntre(Ile ile, Ile otherIle) throws IllegalArgumentException;

	/**
	 * 
	 * Gets the pont last inserted into the puzzle. <strong>Important:</strong>
	 * Returns <code>null</code> if there is no such pont.
	 * 
	 * @return pont last inserted, otherwise null
	 */
	Pont getDernierPontAjouter();

	/**
	 * 
	 * Adds a (single) pont to the <code>ile</code> in the
	 * <code>direction</code> if possible, i.e. the <code>ile</code> is missing a
	 * pont, there is a neighbor ile in the <code>direction</code> also missing
	 * a pont and if there is a pont between the two iles, it is not a double
	 * pont.
	 * 
	 * @param ile
	 *            to which the pont should be added
	 * @param direction
	 *            in which the pont should be added
	 * @return true if the pont was added
	 * @throws IllegalArgumentException
	 *             if ile or direction is null or there is no neighbor ile in
	 *             the direction
	 */
	boolean ajouterPont(Ile ile, Position.Direction direction) throws IllegalArgumentException;

	/**
	 * 
	 * Adds a (single) pont between the <code>ile<code>otherIle if possible, i.e. the iles are neighbors, both
	 * are missing a pont and there is no double <code>Pont
	 * 
	 * @param ile
	 *            to connect by the pont
	 * @param otherIle
	 *            to connect by the pont
	 * @return true if the pont was added
	 * @throws IllegalArgumentException
	 *             if one of the iles is null or the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles)
	 */
	boolean ajouterPontEntre(Ile ile, Ile otherIle) throws IllegalArgumentException;

	/**
	 * 
	 * Adds a pont between the <code>ile<code>otherIle if possible, i.e. the iles are neighbors, both
	 * are missing a pont and there is no <code>Pont<code>doublePont<code>Pont<code>Pont<strong>Important:<code>Pont
	 * 
	 * @param ile
	 *            to connect by the pont
	 * @param otherIle
	 *            to connect by the pont
	 * @param doublePont
	 *            true if the pont to be added is a double pont, otherwise a
	 *            single pont is going to be added
	 * @return true if the pont was added
	 * @throws IllegalArgumentException
	 *             if one of the iles is null, the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles) or a pont between
	 *             the two iles already exists
	 */
	boolean ajouterPontEntre(Ile ile, Ile otherIle, boolean doublePont) throws IllegalArgumentException;

	/**
	 * 
	 * Adds a pont between the <code>ile<code>otherIle if possible, i.e. the iles are neighbors, both
	 * are missing a pont and there is no <code>Pont<code>doublePont<code>Pont<code>Pont<strong>Important:<code>Pont
	 * 
	 * @param ile
	 *            to connect by the pont
	 * @param otherIle
	 *            to connect by the pont
	 * @param doublePont
	 *            true if the pont to be added is a double pont, otherwise a
	 *            single pont is going to be added
	 * @return true if the pont was added
	 * @throws IllegalArgumentException
	 *             if one of the iles is null, the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles) or a pont between
	 *             the two iles already exists
	 */
	void ajouterPontEntreIles(Ile existingIle, Ile newIle, boolean isDouble)
			throws IllegalArgumentException;

	/**
	 * 
	 * Removes a (single) pont of the <code>ile</code> in the
	 * <code>direction</code> if there is a pont.
	 * 
	 * @param ile
	 *            of which the pont should be removed
	 * @param direction
	 *            in which the pont should be removed
	 * @return true if the pont was removed
	 * @throws IllegalArgumentException
	 *             if ile or direction is null or there is no neighbor ile in
	 *             the direction
	 */
	boolean supprimerPont(Ile ile, Position.Direction direction) throws IllegalArgumentException;

	/**
	 * 
	 * Removes a (single) pont between the <code>ile</code> and the
	 * <code>otherIle</code> if there is a pont.
	 * 
	 * @param ile
	 *            one end of the pont that is to be removed
	 * @param otherIle
	 *            other end of the pont that is to be removed
	 * @return true if the pont was removed
	 * @throws IllegalArgumentException
	 *             if one of the iles is null or the iles are not neighbors to
	 *             each other
	 */
	boolean supprimerPontEntre(Ile ile, Ile otherIle) throws IllegalArgumentException;

	/**
	 * 
	 * Removes a <code>Pont<code>ile<code>otherIle if there is a pont. If <code>doublePont<code>Pont<code>Pont<strong>Important:<code>Pont<code>Pont<code>doublePont<code>IllegalArgumentException<code>Pont<code>doublePont
	 * 
	 * @param ile
	 *            one end of the pont that is to be removed
	 * @param otherIle
	 *            other end of the pont that is to be removed
	 * @param doublePont
	 *            true if the pont to be removed is a double pont, otherwise a
	 *            single pont is going to be removed
	 * @return true if the pont was removed
	 * @throws IllegalArgumentException
	 *             if one of the iles is null, the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles)
	 */
	boolean supprimerPontEntre(Ile ile, Ile otherIle, boolean doublePont)
			throws IllegalArgumentException;

	/**
	 * 
	 * Removes a <code>pont if there is a pont. If
	 * <code>doublePont<code>Pont<code>Pont<strong>Important:<code>Pont<code>Pont<code>doublePont<code>IllegalArgumentException<code>Pont<code>doublePont
	 * 
	 * @param pont
	 *            to be removed
	 * @param doublePont
	 *            true if the pont to be removed is a double pont, otherwise a
	 *            single pont is going to be removed
	 * @return true if the pont was removed
	 * @throws IllegalArgumentException
	 *             if one of the iles is null, the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles)
	 */
	boolean supprimerPont(Pont pont, boolean doublePont) throws IllegalArgumentException;

	/**
	 * 
	 * Removes a <code>pont if there is a pont and resets number of
	 * ponts required by ile at both ends accordingly, i.e., minus number of
	 * ponts removed. If <code>doublePont<code>Pont<code>Pont<strong>Important:<code>Pont<code>Pont<code>doublePont<code>IllegalArgumentException<code>Pont<code>doublePont
	 * 
	 * @param pont
	 *            to be removed
	 * @param doublePont
	 *            true if the pont to be removed is a double pont, otherwise a
	 *            single pont is going to be removed
	 * @return true if the pont was removed
	 * @throws IllegalArgumentException
	 *             if one of the iles is null, the iles are not neighbors to
	 *             each other (this can also mean that there is a pont crossing
	 *             between them connecting two other iles)
	 */
	boolean supprimerPontEntreIles(Pont oldPont, boolean doublePont) throws IllegalArgumentException;

	/**
	 * Removes all ponts from the puzzle.
	 */
	void supprimerPonts();

}
