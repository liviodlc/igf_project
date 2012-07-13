package edu.arizona;

import java.awt.Rectangle;

public class Wall extends GameObject {

	// ----------------- DEFINITIONS -------------------------------

	private static final int HIT_WIDTH = 32;
	private static final int HIT_HEIGHT = 32;

	private static final int SUB_IMG_WIDTH = 32;
	private static final int SUB_IMG_HEIGHT = 32;

	// ---------------- INSTANCE VARS -------------------------------

	/*none!*/
	
	// -------------- FUNCTIONS / METHODS ------------------

	public Wall(int x, int y) {
		newBounds = new Rectangle(x, y, HIT_WIDTH, HIT_HEIGHT);
		oldBounds = (Rectangle) newBounds.clone();
		loadImage("wall.png", SUB_IMG_WIDTH, SUB_IMG_HEIGHT, 1);
		isActive = false;
		isSolid = true;
		currentFrame= 1;
		isFacingLeft= false;
	}

}
