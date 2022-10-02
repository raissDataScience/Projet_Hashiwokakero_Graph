package controller;

import graphe.IInputListener;

/**
 * This class is an extension of <code>Thread{@link #run() run} method is called) automatically solves a Hashiwokakeru
 * puzzle by adding ponts until it is either interrupted or no more pont can
 * be added. For a pont to be added, it certainly must be added based on the
 * current state of the puzzle (see {@link PontHandler#makeSureMove()
 * makeSureMove()} method).
 */
public class GrilleThread extends Thread {

    private PontHandler pontAdder;
    private IInputListener view;

    /**
     * Constructs an instance of a <code>PuzzleSolver</code>.
     * 
     * @param pontAdder
     *            used to add ponts to the puzzle
     * @param view
     *            that is refreshed if pont has been added or state of the
     *            <code>this</code> solver changes
     */
    public GrilleThread(PontHandler pontAdder, IInputListener view) {
        this.pontAdder = pontAdder;
        this.view = view;
    }

    /**
     * Runs the <code>this thread. Ponts are added until no more ponts
     * can be added (see {@link PontHandler#makeSureMove() makeSureMove()} method)
     * or <code>this<code>this
     */
    @Override
    public void run() {
        try {
            while (pontAdder.makeSureMove()) {
                view.refresh();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            this.interrupt(); // Necessary to get correct state of solver
        }
    }

    /**
     * Starts <code>this</code> thread.
     */
    public void solvePuzzleWithPauses() {
        this.start();
    }

}
