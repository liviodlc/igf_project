package edu.arizona;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameObject {

	// ----------------- DEFINITIONS -------------------------------

	private static final int DEFAULT_WIDTH = 32;
	private static final int DEFAULT_HEIGT = 32;

	private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/data.images/";

	// ---------------- INSTANCE VARS -------------------------------

	// bounds:
	protected boolean isSolid; // whether or not to test for collisions
	protected Rectangle oldBounds; // useful during collision detection
	protected Rectangle newBounds;

	// movement:
	protected boolean isActive; // whether or not to update movement
	protected int speedX;
	protected int speedY;

	// visuals:
	protected Image currImage;
	private BufferedImage spriteSheet;

	// -------------- FUNCTIONS / METHODS ------------------

	public GameObject(int x, int y, String img) {
		initImg(img);

		newBounds = new Rectangle(x, y, DEFAULT_WIDTH, DEFAULT_HEIGT);
	}

	private void initImg(String img) {
		try {
			spriteSheet = ImageIO.read(new File(IMAGE_DIRECTORY + img));

			// set the current sprite(at visual range 3 (or visible light))
			currImage = spriteSheet.getSubimage(32 * 3, 0, 32, 32);
		} catch (IOException e) {
			System.out.println("Unable to load resource: " + img);
		}
	}

	// -------------- GAMELOOP STUFF ------------------

	public void updateState() {

	}
}
