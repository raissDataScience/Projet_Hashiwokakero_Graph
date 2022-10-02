package graphe;

import modele.Position.Direction;

import modele.Ile;

/**
 * The <code>IInputListener</code> interface forms the main interface of the
 * controller to the view. A class that implements the interface is responsible
 * for receiving inputs from components of the view and processing them.
 * 
 * After processing an input, the state of the model of the Hashiwokakeru puzzle
 * is updated and relevant components of the view are refreshed if model changed
 * as a result of the processing.
 */
public interface IInputListener {
	/**
	 * Makes a move, i.e. adds or removes a (single) pont from the field of the
	 * puzzle, if possible, meaning that there is a neighbor to the
	 * <code>ile</code> in the <code>directionOfClick</code> to which a pont
	 * can be built or to which a pont is existing that can be removed. If
	 * <code>addPont</code> is true, a pont is to be added, otherwise a pont
	 * is to be removed.
	 * 
	 * <p>
	 * <strong>Important:</strong> If the puzzle is currently being solved
	 * automatically, no move can be made by calling this method.
	 * </p>
	 * 
	 * @param ile
	 *            to which to add a pont or from which to remove a pont
	 * @param directionOfClick
	 *            direction in which to add or to remove pont
	 * @param addPont
	 *            true if pont is to be added, false if a pont is to be
	 *            removed
	 */
	void makeMove(Ile ile, Direction directionOfClick, boolean addPont);

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
	 * <li>the puzzle is currently being solved automatically,</li>
	 * <li>the puzzle is unsolvable and this was recognized by the application,</li>
	 * <li>the puzzle is invalid or already solved.
	 * </ul>
	 * </p>
	 * 
	 * @return true if a pont was added, false otherwise
	 */
	boolean addNextPont();

	/**
	 * Starts automatic solving of the puzzle if the puzzle is currently not being
	 * solved automatically. Otherwise the automatic solving of the puzzle is
	 * stopped. Each time after a pont was added, a short pause is made and the
	 * view refreshed.
	 * 
	 * <p>
	 * The automatic solving of a puzzle stops automatically if
	 * <ul>
	 * <li>no pont can be found that must certainly be built based <strong>on the
	 * current state</strong> of the puzzle (see {@link #addNextPont()
	 * addNextPont} method),</li>
	 * <li>the puzzle is restarted, i.e. all ponts are removed from the
	 * puzzle,</li>
	 * <li>a new puzzle is loaded from a file,</li>
	 * <li>the puzzle is saved to a file,</li>
	 * <li>a new puzzle is generated or</li>
	 * <li>the application is quit.</li>
	 * </p>
	 * 
	 * <p>
	 * <strong>Important:</strong> While the puzzle is being solved automatically,
	 * the user can neither add nor remove any ponts "manually" or add ponts by
	 * using the {@link #addNextPont() addNextPont} method.
	 * </p>
	 * 
	 */
	void startAndStopSolving();

	/**
	 * Restarts the puzzle, i.e. removes all ponts from the puzzle and resets the
	 * puzzle state to {@link model.PuzzleState#NOT_YET_SOLVED}.
	 * 
	 * <p>
	 * <strong>Important:</strong> If the puzzle is currently being solved
	 * automatically, this is stopped by calling this method.
	 * </p>
	 * 
	 */
	void restartPuzzle();

	/**
	 * Generates a new puzzle that replaces the existing one. If the existing one
	 * was not saved before, it is lost.
	 * 
	 * <p>
	 * <strong>Important:</strong> If the puzzle is currently being solved
	 * automatically, this is stopped by calling this method.
	 * </p>
	 */
	void generatePuzzle();

	/**
	 * Generates a new puzzle of size <code>width</code> x <code>height</code> that
	 * replaces the existing one. If the existing one was not saved before, it is
	 * lost.
	 * 
	 * <p>
	 * <strong>Important:</strong> If the puzzle is currently being solved
	 * automatically, this is stopped by calling this method.
	 * </p>
	 * 
	 * @param width
	 *            of new puzzle
	 * @param height
	 *            of new puzzle
	 */
	void generatePuzzle(int width, int height);

	/**
	 * Generates a new puzzle of size <code>width</code> x <code>height</code> that
	 * has <code>noOfIles</code> number of iles and replaces the existing one.
	 * If the existing one was not saved before, it is lost.
	 * 
	 * <p>
	 * <strong>Important:</strong> If the puzzle is currently being solved
	 * automatically, this is stopped by calling this method.
	 * </p>
	 * 
	 * @param width
	 *            of new puzzle
	 * @param height
	 *            of new puzzle
	 * @param noOfIles
	 *            of new puzzle
	 */
	void generatePuzzle(int width, int height, int noOfIles);
        
        /**
         * methode qui raffraichit les composants
         */
	public void refresh();
}
