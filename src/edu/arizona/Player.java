package edu.arizona;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

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

	private static final int HIT_WIDTH = 37;
	private static final int HIT_HEIGHT = 132;

	private static final int SUB_IMG_WIDTH = 100;
	private static final int SUB_IMG_HEIGHT = 138;
	private static final int IMG_OFFSET_X = -30;
	private static final int IMG_OFFSET_Y = -2;
	private static final double IMG_SCALE = 0.5;
	private static final int IMG_NUM_FRAMES = 8;
	private static final int ANIM_DELAY = 4;

	// ---------------- INSTANCE VARS -------------------------------

	// keyboard states:
	public boolean keyUp;
	private boolean keyUpLast;
	public boolean keyRight;
	public boolean keyDown;
	public boolean keyLeft;

	// visual states:
	private int delayCount;

	// -------------- FUNCTIONS / METHODS ------------------

	public Player(int x, int y) {
		newBounds = new Rectangle(x, y, HIT_WIDTH, HIT_HEIGHT);
		currentFrame = 1;
		delayCount = ANIM_DELAY;
		loadImage("pc_walk_for_prototype.png", SUB_IMG_WIDTH, SUB_IMG_HEIGHT, IMG_SCALE);
		imgOffsetX = IMG_OFFSET_X;
		imgOffsetY = IMG_OFFSET_Y;
		isActive = true;
		isSolid = true;
	}

	@Override
	protected void updateSubImage() {
		int subx = subImgWidth * (visRange / Main.SPECTRUM_MAJOR_TICKS);
		int suby = subImgHeight * (currentFrame - 1);
		currImage = spriteSheet.getSubimage(subx, suby, subImgWidth, subImgHeight);
		
		if (isFacingLeft) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-currImage.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			if (currImage instanceof BufferedImage)
				currImage = op.filter((BufferedImage) currImage, null);
			else
				System.err
						.println("Error: [Player.java > updateSubImage()] currImage is not BufferedImage");
		}
	}

	@Override
	public void updateState() {
		oldBounds = (Rectangle) newBounds.clone();

		updateLeftRightControls();
		updateVerticalControls();
		updateVisuals();

		// update position
		newBounds.translate((int) speedX, (int) speedY);
	}

	private void updateLeftRightControls() {
		if (keyRight) {
			speedX += AIR_RUN_ACCEL;
			if (onGround)
				speedX += EXTRA_GROUND_RUN_ACCEL;
			if (speedX > MAX_RUN_SPEED)
				speedX = MAX_RUN_SPEED;
			isFacingLeft = false;
		}
		if (keyLeft) {
			speedX -= AIR_RUN_ACCEL;
			if (onGround)
				speedX -= EXTRA_GROUND_RUN_ACCEL;
			if (speedX < -MAX_RUN_SPEED)
				speedX = -MAX_RUN_SPEED;
			isFacingLeft = true;
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
	}

	private void updateVerticalControls() {
		if (keyUp && !keyUpLast && onGround) {
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
		onGround = false;
	}

	private void updateVisuals() {
		int horizSpeed = (int) Math.abs(speedX);
		if (horizSpeed == 0) {
			currentFrame = 6;
		} else if (horizSpeed < 5) {
			currentFrame = 7;
		} else if (delayCount > 0) {
			delayCount--;
		} else {
			delayCount = ANIM_DELAY;
			currentFrame++;
			if (currentFrame > IMG_NUM_FRAMES)
				currentFrame = 1;
		}
		updateSubImage();
	}
}
