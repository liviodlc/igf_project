package edu.arizona;

import java.awt.Rectangle;

public class Player extends GameObject {

	// ----------------- DEFINITIONS -------------------------------

	private static final int GRAVITY = 5;
	private static final int MAX_FALL_SPEED = 20;

	private static final int RUN_ACCEL = 2;
	private static final int MAX_RUN_SPEED = 10;

	private static final int AIR_FRICTION = 2;
	private static final int EXTRA_GROUND_FRICTION = 1;

	private static final int JUMP_SPEED = -20;

	// ---------------- INSTANCE VARS -------------------------------

	// keyboard states:
	public boolean keyUp;
	public boolean keyRight;
	public boolean keyDown;
	public boolean keyLeft;
	public boolean keySpace;
	private boolean keySpaceLast;

	// other states:
	private boolean onGround;

	// -------------- FUNCTIONS / METHODS ------------------

	public Player(int x, int y) {
		super(x, y, "player.png");
	}

	@Override
	public void updateState() {
		oldBounds = (Rectangle) newBounds.clone();

		// left-right controls
		if (keyRight) {
			speedX += RUN_ACCEL;
			if (speedX > MAX_RUN_SPEED)
				speedX = MAX_RUN_SPEED;
		}
		if (keyLeft) {
			speedX -= RUN_ACCEL;
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
		if (keySpace && !keySpaceLast) {// && onGround){
			speedY = JUMP_SPEED;
		} else {
			speedY += GRAVITY;
			if (speedY > MAX_FALL_SPEED)
				speedY = MAX_FALL_SPEED;
		}
		if (keySpace)
			keySpaceLast = true;
		else
			keySpaceLast = false;

		// update position
		newBounds.translate(speedX, speedY);
	}
}
