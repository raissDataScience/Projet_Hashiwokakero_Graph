package modele;

public class Position implements Comparable<Position> {
	
    public final int x;
    public final int y;

    /**
     * Constructs an instance of <code>Coordinates</code>.
     * 
     * @param x coordinate
     * @param y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Compares <code>this</code> instance to <code>otherCoords</code> based on
     * first the x coordinate and second the y coordinate.
     * <strong>Important:</strong> The {@link Object#hashCode() hashcode} method is
     * not overriden so that two coordinates with the same x and the same y
     * coordinate have different hashcodes.
     * 
     * @param otherCoords to be compared
     * @result a negative integer, zero, or a positive integer as these coordinates
     *         are less than, equal to, or greater than the specified coordinates
     */
    @Override
    public int compareTo(Position otherCoords) {
        if (x == otherCoords.x) {
            if (y == otherCoords.y) {
                return 0;
            } else if (y < otherCoords.y) {
                return -1;
            } else {
                return 1;
            }
        } else if (x < otherCoords.x) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * <strong>Important:</strong> The {@link Object#hashCode() hashcode} method is
     * not overriden so that two coordinates with the same x and the same y
     * coordinate have different hashcodes.
     * 
     * @param obj the reference object with which to compare.
     * @result true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Position && compareTo((Position) obj) == 0;
    }

    /**
     * Returns the coordinates next to the coordinates represented by
     * <code>this Position<code>direction. Figuratively, the method takes one step in the
     * <code>direction
     * 
     * @param direction the direction in which the next coordinates are to be
     *                  determined.
     * @return next coordinates in direction.
     */
    public Position getNextCoordsIn(Direction direction) {
            return getCoordsForNoStepsInDirection(direction, 1);
    }

    private Position getCoordsForNoStepsInDirection(Direction direction, int steps) {
        int newX = x;
        int newY = y;
        switch (direction) {
        case NORD:
            newY -= steps;
            break;
        case EST:
            newX += steps;
            break;
        case SUD:
            newY += steps;
            break;
        case OUEST:
            newX -= steps;
        }
        return new Position(newX, newY);
    }

    /**
     * Returns the direction of the specified coordinates relative to the
     * coordinates represented by <code>this Position
     * 
     * @param coords the coordinates of which the direction is to be determined.@throws IllegalArgumentException if the direction of the specified
     *                                  coordinates relative to the coordinates
     *                                  represented by <code>this Position
     */
    public Direction getDirectionOfCoord(Position coords) throws IllegalArgumentException {
        if (this.x == coords.x) {
            if (this.y < coords.y) {
                return Direction.SUD;
            } else if (this.y > coords.y) {
                return Direction.NORD;
            }
        } else if (this.y == coords.y) {
            if (this.x < coords.x) {
                return Direction.EST;
            } else if (this.x > coords.x) {
                return Direction.OUEST;
            }
        }
        throw new IllegalArgumentException("Invalide direction.");
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public enum Direction {
        NORD,

        EST,

        SUD, 

        OUEST;
    }
}
