package catan;

import java.awt.Graphics;

/*
 * Interface used to draw all the gameboard pieces in its paintComponent function
 * also used as a tagging interface for all the pieces so that the game can hold one
 * "selected piece" regardless of what type it is.
 */

public interface Drawable {
	    public void draw(Graphics g);

		public void unselected();
}

