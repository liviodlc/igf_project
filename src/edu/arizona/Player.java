package edu.arizona;

import java.awt.Rectangle;

public class Player extends GameObject {

	// ----------------- DEFINITIONS -------------------------------

	private static final double GRAVITY = 0.25;
	private static final double MAX_FALL_SPEED = 6;

	private static final double AIR_RUN_ACCEL = 0.25;
	private static final double EXTRA_GROUND_RUN_ACCEL = 1;
	private static final double MAX_RUN_SPEED = 10;

	private static final double AIR_FRICTION = 0.15;
	private static final double EXTRA_GROUND_FRICTION = 1;

	private static final double JUMP_SPEED = -10;

	private static final int HIT_WIDTH = 32;
	private static final int HIT_HEIGHT = 32;
	
	private static final int SUB_IMG_WIDTH = 32;
	private static final int SUB_IMG_HEIGHT = 32;

	// ---------------- INSTANCE VARS -------------------------------

	// keyboard states:
	public boolean keyUp;
	private boolean keyUpLast;
	public boolean keyRight;
	public boolean keyDown;
	public boolean keyLeft;

	// -------------- FUNCTIONS / METHODS ------------------

	public Player(int x, int y) {
		newBounds = new Rectangle(x, y, HIT_WIDTH, HIT_HEIGHT);
		loadImage("player.png", SUB_IMG_WIDTH, SUB_IMG_HEIGHT);
		isActive = true;
		isSolid = true;
	}

	@Override
	public void updateState() {
		oldBounds = (Rectangle) newBounds.clone();

		// left-right controls
		if (keyRight) {
			speedX += AIR_RUN_ACCEL;
			if(onGround)
				speedX += EXTRA_GROUND_RUN_ACCEL;
			if (speedX > MAX_RUN_SPEED)
				speedX = MAX_RUN_SPEED;
		}
		if (keyLeft) {
			speedX -= AIR_RUN_ACCEL;
			if(onGround)
				speedX -= EXTRA_GROUND_RUN_ACCEL;
			if (speedX < -MAX_RUN_SPEED)
				speedX = -MAX_RUN_SPEED;
		}
		if (!keyRight && !keyLeft) {
			if (speedX > 0) {
				speedX -= AIR_FRICTION;
				if (onGround)
					speedX -= EXTRA_GROUND_FRICTION;
				if (speedX < 0)
					speedX = 0;
			} else if (speedX < 0) {
				speedX += AIR_FRICTION;
				if (onGround)
					speedX += EXTRA_GROUND_FRICTION;
				if (speedX > 0)
					speedX = 0;
			}
		}

		// vertical controls
		if (keyUp && !keyUpLast) {// && onGround){
			speedY = JUMP_SPEED;
		} else {
			speedY += GRAVITY;
			if (speedY > MAX_FALL_SPEED)
				speedY = MAX_FALL_SPEED;
		}
		if (keyUp)
			keyUpLast = true;
		else
			keyUpLast = false;

		// update position
		newBounds.translate((int) speedX, (int) speedY);
	}
}
