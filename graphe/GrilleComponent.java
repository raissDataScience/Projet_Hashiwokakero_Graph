package graphe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import modele.Position;
import modele.Position.Direction;
import modele.Pont;
import modele.Ile;
import modele.GrilleInterface;

/**
 * 
 * JFieldPanel is a JPanel containing the field of a Hashiwokakeru puzzle (its
 * iles and ponts).
 * 
 * <p>
 * The field is horizontally and vertically centered inside the panel keeping a
 * minimum distance of one row to the top and bottom border and a minimum
 * distance of one column to the left and right border of the panel. When the
 * panel is resized, the field scales up or down to fit the space inside the
 * panel while keeping the minimum distance to the borders.
 * </p>
 * 
 * <p>
 * By clicking on an ile on the field, an instance of an
 * <code>IInputListener</code> is informed to try to add (left mouse button) or
 * remove (right mouse button) a pont in the direction indicated by the click.
 * If direction is ambiguous or there is no ile at the coordinates of the
 * click, the click is ignored.
 * </p>
 */
public final class GrilleComponent extends JPanel {

    public static final Color COLOR_ISLAND_MISSING_BRIDGE = Color.GRAY;
    public static final Color COLOR_ISLAND_WITH_ALL_BRIDGES = Color.CYAN;
    public static final Color COLOR_INVALID_ISLAND = Color.RED;
    public static final Color COLOR_BRIDGE_LAST_INSERTED = Color.BLACK;

    private int userWidth, userHeight;
    private final int DIST_BETW_ADJ_GRID_POINTS = 100; // distance between adjacent grid points
    private final int ISLAND_RADIUS = 50; // DIST_BETW_ADJ_GRID_POINTS / 2
    private final int CLICK_TOLERANCE = 50; // DIST_BETW_ADJ_GRID_POINTS / 2
    private final int DIST_BETWEEN_BRIDGES = 20; // belonging to double pont

    private GrilleInterface hashiModel;
    private final IInputListener inputListener;

    private double scaleFactor;
    private double translation;

    private boolean showNoOfMissingPonts;

    /**
     * 
     * Constructs an instance of a JFieldPanel that has a preferred size of 500x500
     * containing the field of the Hashiwokakeru puzzle represented by the
     * <code>hashiModel</code> that informs the <code>inputListener</code> if a left
     * or right mouse-click on the field occurs.
     * 
     * @param hashiModel
     *            that represents the Hashiwokakeru puzzle to be painted
     * @param inputListener
     *            that is informed when a click on the field occurs
     */
    public GrilleComponent(GrilleInterface hashiModel, IInputListener inputListener) {
        setPuzzleSituationModel(hashiModel);
        this.inputListener = inputListener;
        setPreferredSize(new Dimension(500, 300));
        addMouseListener(getMouseAdapterNotifyingListener());
    }

    /**
     * 
     * Set <code>hashiModel<code>GrilleInterface
     * 
     * @param hashiModel
     *            containing the width and height of the field as well as the iles and ponts on the field
     */
    //@Override
    public void setPuzzleSituationModel(GrilleInterface hashiModel) {
        this.hashiModel = hashiModel;
        this.userWidth = (hashiModel.getWidth() + 1) * DIST_BETW_ADJ_GRID_POINTS; // +1 to add left border
        this.userHeight = (hashiModel.getHeight() + 1) * DIST_BETW_ADJ_GRID_POINTS; // + 1 to add top border
    }

    private MouseListener getMouseAdapterNotifyingListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) 
                    reTransformCoordinatesAndNotifyListener(e.getX(), e.getY(), SwingUtilities.isLeftMouseButton(e));
            }
        };
    }

    /**
     * 
     * Sets the number of ponts painted inside each ile.
     * 
     * @param showNoOfPontsMissing
     *            if true, the number of ponts missing, i.e. number of ponts
     *            that yet need to be added, is shown, otherwise the total of number
     *            of ponts that the ile requires is shown
     */
    public void setIleString(boolean showNoOfPontsMissing) {
        this.showNoOfMissingPonts = showNoOfPontsMissing;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        transformGraphicsToCenterGridOnPanel(g2);
        transformGraphicsToScaleToUserCoordinateSystem(g2);
        drawIlesAndPonts(g2);
    }

    private void transformGraphicsToCenterGridOnPanel(Graphics2D g2) {
        double horDistanceBetweenGridPoints = (double) getWidth() / (double) (hashiModel.getWidth() + 1);
        double verDistanceBetweenGridPoints = (double) getHeight() / (double) (hashiModel.getHeight() + 1);
        double differenceBetweenDistances = horDistanceBetweenGridPoints - verDistanceBetweenGridPoints;
        if (differenceBetweenDistances > 0) { 
            translation = differenceBetweenDistances * (double) (hashiModel.getWidth() + 1) / 2.0; // / 2.0 to center
            g2.transform(AffineTransform.getTranslateInstance(translation, 0));
        } else { 
            translation = differenceBetweenDistances * (double) (hashiModel.getHeight() + 1) / 2.0;
            g2.transform(AffineTransform.getTranslateInstance(0, -translation));
        }
    }

    private void transformGraphicsToScaleToUserCoordinateSystem(Graphics2D g2) {
        double scaleX = (double) getWidth() / (double) userWidth;
        double scaleY = (double) getHeight() / (double) userHeight;
        scaleFactor = Math.min(scaleX, scaleY);
        AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        g2.transform(at);
    }

    private void drawIlesAndPonts(Graphics2D g2) {
        activateAntialiasingForSmootherLines(g2);
        if (hashiModel.getDernierPontAjouter() != null)
            drawLastInsertedPont(g2, hashiModel.getDernierPontAjouter());
        hashiModel.getIles().stream().forEachOrdered(ile -> {
            drawEastAndSouthPont(g2, ile);
            drawIle(g2, ile); //return ile;
        });
    }

    private void activateAntialiasingForSmootherLines(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawLastInsertedPont(Graphics2D g2, Pont pont) {
        g2.setStroke(new BasicStroke(10.0f)); // default 1.0f
        g2.setColor(Color.BLACK);
        drawPont(g2, pont);
    }

    private void drawPont(Graphics2D g2, Pont pont) {
        if (pont.isDouble()) 
            drawDoublePont(g2, pont.getStart().getCoords(), pont.getEnd().getCoords(), pont.isVertical());
        else drawSimplePont(g2, pont.getStart().getCoords(), pont.getEnd().getCoords());
    }

    private void drawSimplePont(Graphics2D g2, Position start, Position end) {
        g2.drawLine((start.x + 1) * 100, (start.y + 1) * 100, (end.x + 1) * 100, (end.y + 1) * 100);
    }

    private void drawDoublePont(Graphics2D g2, Position start, Position end, boolean isVertical) {
        if (isVertical) {
            g2.drawLine((start.x + 1) * 100 - DIST_BETWEEN_BRIDGES / 2, (start.y + 1) * 100,
                            (end.x + 1) * 100 - DIST_BETWEEN_BRIDGES / 2, (end.y + 1) * 100);
            g2.drawLine((start.x + 1) * 100 + DIST_BETWEEN_BRIDGES / 2, (start.y + 1) * 100,
                            (end.x + 1) * 100 + DIST_BETWEEN_BRIDGES / 2, (end.y + 1) * 100);
        } else {
            g2.drawLine((start.x + 1) * 100, (start.y + 1) * 100 - DIST_BETWEEN_BRIDGES / 2, (end.x + 1) * 100,
                            (end.y + 1) * 100 - DIST_BETWEEN_BRIDGES / 2);
            g2.drawLine((start.x + 1) * 100, (start.y + 1) * 100 + DIST_BETWEEN_BRIDGES / 2, (end.x + 1) * 100,
                            (end.y + 1) * 100 + DIST_BETWEEN_BRIDGES / 2);
        }
    }

    private final Direction[] EAST_AND_SOUTH = { Direction.EST, Direction.SUD };

    private void drawEastAndSouthPont(Graphics2D g2, Ile ile) {
        g2.setStroke(new BasicStroke(2.0f));
        g2.setColor(Color.BLACK);
        Pont pont;
        for (Direction direction : EAST_AND_SOUTH) {
            if ((pont = hashiModel.getPont(ile, direction)) != null) 
                drawPont(g2, pont);
        }
    }

    private void drawIle(Graphics2D g2, Ile ile) {
        int x = (ile.getCoords().x + 1) * 100 - ISLAND_RADIUS;
        int y = (ile.getCoords().y + 1) * 100 - ISLAND_RADIUS;
        int noOfPontsMissing = ile.getNoOfPontsMissing();
        setIleColor(g2, noOfPontsMissing);
        g2.fillOval(x, y, 2 * ISLAND_RADIUS, 2 * ISLAND_RADIUS);
        drawIleString(g2, ile, x, y, noOfPontsMissing);
    }

    private void setIleColor(Graphics2D g2, int noOfPontsMissing) {
        if (noOfPontsMissing == 0) g2.setColor(COLOR_ISLAND_WITH_ALL_BRIDGES);
        else if (noOfPontsMissing > 0) g2.setColor(COLOR_ISLAND_MISSING_BRIDGE);
        else g2.setColor(COLOR_INVALID_ISLAND);
    }

    private void drawIleString(Graphics2D g2, Ile ile, int x, int y, int noOfPontsMissing) {
        g2.setColor(Color.BLACK);
        g2.setFont(getFont().deriveFont((float) ISLAND_RADIUS)); 
        FontMetrics fm = g2.getFontMetrics();
        int noOfPontsToBeDrawn = showNoOfMissingPonts ? noOfPontsMissing : ile.getNoOfPontsRequired();
        String noString = Integer.toString(noOfPontsToBeDrawn);
        g2.drawString(noString, x + ISLAND_RADIUS - fm.stringWidth(noString) / 2,
                        y + ISLAND_RADIUS - fm.getHeight() / 2 + fm.getAscent());
    }

    private void reTransformCoordinatesAndNotifyListener(int panelX, int panelY, boolean isLeftMouseButton) throws IllegalArgumentException {
        double reTranslatedX = translation > 0.0 ? (double) panelX - translation : (double) panelX;
        double reTranslatedY = translation > 0.0 ? (double) panelY : (double) panelY + translation;
        double reScaledX = reTranslatedX / scaleFactor;
        double reScaledY = reTranslatedY / scaleFactor;
        int ileX = Math.toIntExact(Math.round(reScaledX / (double) DIST_BETW_ADJ_GRID_POINTS)) - 1;
        int ileY = Math.toIntExact(Math.round(reScaledY / (double) DIST_BETW_ADJ_GRID_POINTS)) - 1;
        double ileCenterX = (double) (ileX + 1) * 100.0;
        double ileCenterY = (double) (ileY + 1) * 100.0;
        if (ileX >= 0 && ileX < hashiModel.getWidth() && ileY >= 0 && ileY < hashiModel.getHeight() && hashiModel.ileAt(ileX, ileY)) {
            Ile ile = hashiModel.getIleAt(ileX, ileY);
            Direction directionOfClick = getDirectionOfClick(reScaledX, reScaledY, ileCenterX, ileCenterY);
            inputListener.makeMove(ile, directionOfClick, isLeftMouseButton);
        }
    }

    private Direction getDirectionOfClick(double reScaledX, double reScaledY, double ileCenterX, double ileCenterY) throws IllegalArgumentException {
        if (ileCenterY > reScaledY && reScaledY >= ileCenterY - CLICK_TOLERANCE
                && Math.abs(reScaledX - ileCenterX) <= Math.abs(reScaledY - ileCenterY)) 
            return Direction.NORD;

        if (ileCenterX > reScaledX && reScaledX >= ileCenterX - CLICK_TOLERANCE
                && Math.abs(reScaledY - ileCenterY) <= Math.abs(reScaledX - ileCenterX)) 
            return Direction.OUEST;

        if (ileCenterY <= reScaledY && reScaledY < ileCenterY + CLICK_TOLERANCE
                && Math.abs(reScaledX - ileCenterX) < Math.abs(reScaledY - ileCenterY)) 
            return Direction.SUD;

        if (ileCenterX <= reScaledX && reScaledX < ileCenterX + CLICK_TOLERANCE
                && Math.abs(reScaledY - ileCenterY) < Math.abs(reScaledX - ileCenterX)) 
            return Direction.EST;
        throw new IllegalArgumentException("Direction of click is ambiguous.");
    }
}
