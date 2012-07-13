package edu.arizona;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

abstract class GameObject {

	// ----------------- DEFINITIONS -------------------------------

	public static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + File.separator
			+ "data.images" + File.separator;

	// ---------------- INSTANCE VARS -------------------------------

	// bounds:
	protected boolean isSolid; // whether or not to test for collisions
	protected Rectangle oldBounds; // useful during collision detection
	protected Rectangle newBounds;

	// movement:
	public boolean isActive; // whether or not to update movement
	protected double speedX;
	protected double speedY;
	protected boolean onGround;

	// visuals:
	protected int visRange;
	protected Image currImage;
	protected BufferedImage spriteSheet;
	protected int subImgWidth;
	protected int subImgHeight;
	public int imgOffsetX;
	public int imgOffsetY;

	// ----------------- CONSTRUCTORS ------------------------

	/**
	 * It is the responsibility of subclasses to handle the construction of the GameObject. Follow
	 * these steps:
	 * 
	 * (1.) Create a new Rectangle and store in newBounds. This object will describe the current
	 * position and rectangular dimensions of the GameObject.
	 * 
	 * (2.) Create a clone of newBounds and store it into oldBounds.
	 * 
	 * (3.) Call the loadImage() method to load a spritesheet.
	 */
	public GameObject() {
		visRange = Main.SPECTRUM_INIT;
	}

	// --------------- SPRITESHEET STUFF ---------------------

	/**
	 * The image must be located in the space defined by GameObject.IMAGE_DIRECTORY.
	 * 
	 * The image is assumed to be a sprite sheet where the sub-images are aligned horizontally, and
	 * there's at least one sub-image for each spectrum. After loading, the default sub-image is
	 * chosen and stored into currImage.
	 * 
	 * @param img
	 *            - a string representing the name of the image file.
	 * @param subImgWidth
	 *            - The width of each sub-image in the spritesheet.
	 * @param subImgHeight
	 *            - The height of each sub-image in the spritesheet.
	 * @param scale
	 *            - A number between 0 and 1 describing how much to resize the image.
	 */
	protected void loadImage(String img, int subImgWidth, int subImgHeight, double scale) {
		this.subImgWidth = subImgWidth;
		this.subImgHeight = subImgHeight;
		try {
			spriteSheet = ImageIO.read(new File(IMAGE_DIRECTORY + img));
			
			int newWidth = new Double(spriteSheet.getWidth() * scale).intValue();
			int newHeight = new Double(spriteSheet.getHeight() * scale).intValue();
			
			BufferedImage resized = new BufferedImage(newWidth, newHeight, spriteSheet.getType());
		    Graphics2D g = resized.createGraphics();
		    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g.drawImage(spriteSheet, 0, 0, newWidth, newHeight, 0, 0, spriteSheet.getWidth(), spriteSheet.getHeight(), null);
		    g.dispose();
		    
		    spriteSheet = resized;

			updateSubImage();
		} catch (IOException e) {
			System.out.println("Unable to load resource: " + img);
		}
	}

	/**
	 * Will update the current image with the appropriate one stored in the spritesheet for the
	 * current spectrum.
	 */
	protected void updateSubImage() {
		currImage = spriteSheet.getSubimage(subImgWidth * (visRange / Main.SPECTRUM_MAJOR_TICKS),
			0, subImgWidth, subImgHeight);
	}

	/**
	 * @return an Image that represents this GameObject, so that it can be drawn by the Room.
	 */
	public Image getImage() {
		return currImage;
	}

	// --------------- SPECTRUM IMAGE STUFF ---------------------

	/**
	 * Updates the Image of the GameObject depending on the new spectrum value.
	 * 
	 * @param vis
	 *            - the current value of the spectrum slider.
	 * 
	 * @author Teresa, Livio
	 */
	public void setVisRange(int vis) {
		if (vis == visRange)
			return;

		visRange = vis;
		if (visRange % Main.SPECTRUM_MAJOR_TICKS == 0) {
			updateSubImage();
		} else {
			// NOTE - the blend code is a protected method in GameObject so that all objs can blend
			// images if/when they need to

			// get the image before you
			int befX = visRange / Main.SPECTRUM_MAJOR_TICKS;
			BufferedImage before = spriteSheet.getSubimage(befX * subImgWidth, 0, subImgWidth,
					subImgHeight);

			// get the image behind you
			int behX = (visRange + Main.SPECTRUM_MAJOR_TICKS) / Main.SPECTRUM_MAJOR_TICKS;
			BufferedImage behind = spriteSheet.getSubimage(behX * subImgWidth, 0, subImgWidth,
					subImgHeight);

			// blend the two images
			currImage = blend(before, behind, (5 - (visRange % Main.SPECTRUM_MAJOR_TICKS)) / 5.0);
		}
	}

	/**
	 * WARNING!! This method is intended to blend two images that are assumed (and as of this
	 * writing it's basically guaranteed...) to be the same height and width. Don't try this if you
	 * don't know what size you're images are!!!
	 * 
	 * @author Teresa
	 */
	protected BufferedImage blend(BufferedImage b1, BufferedImage b2, double weight) {
		// create a new image and get its graphics
		BufferedImage blended = new BufferedImage(b1.getWidth(), b1.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = blended.createGraphics();

		// draw the new image w/appropriate blending
		g2.drawImage(b1, null, 0, 0);
		AlphaComposite inst = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				(float) (1.0 - weight));
		g2.setComposite(inst);
		g2.drawImage(b2, null, 0, 0);
		g2.dispose();

		return blended;
	}

	// -------------- GAMELOOP STUFF ------------------

	/**
	 * Should only be called if this.isActive
	 */
	public void updateState() {
		if (!this.isActive)
			return;
		oldBounds = (Rectangle) newBounds.clone();
		newBounds.translate((int) speedX, (int) speedY);
	}

	/**
	 * Should only be called if this.isActive
	 */
	public void collisionTest(GameObject other) {
		if (!this.isActive)
			return;

		Rectangle intersection = newBounds.intersection(other.newBounds);

		// solid collisions
		if (this.isSolid && other.isSolid && !intersection.isEmpty()) {
			if (intersection.width > intersection.height) { // vertical collision
				if (!other.isActive) {
					if (this.oldBounds.getCenterY() < other.oldBounds.getCenterY()) {
						this.newBounds.translate(0, -intersection.height);
						this.onGround = true;
					} else { // other is on top
						this.newBounds.translate(0, intersection.height);
					}
				} else {
					// TODO: something that will handle the physics of two active objects pushing
					// each other vertically.
				}
			} else { // horizontal collision
				if (!other.isActive) {
					if (this.oldBounds.getCenterX() < other.oldBounds.getCenterX()) {
						this.newBounds.translate(-intersection.width, 0);
					} else { // other is on top
						this.newBounds.translate(intersection.width, 0);
					}
				} else {
					// TODO: something that will handle the physics of two active objects pushing
					// each other vertically.
				}
			}
		}
	}
}
