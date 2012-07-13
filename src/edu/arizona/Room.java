package edu.arizona;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This class keeps track of all the objects in a room, and handles things like updating, and knows
 * what the current visiblity is.
 * 
 * This represents both the model and the view of a Room. Livio thinks this makes more sense
 * conceptually, even though it goes against the Model-View- Controller design pattern.
 * 
 * @author Teresa, Livio
 * 
 */
public class Room extends JPanel {

	// ----------------- DEFINITIONS -------------------------------

	private static final Color[] BG_COLORS = { Color.gray, Color.yellow, Color.red, Color.white,
			Color.blue, Color.black, Color.gray };

	private static final int SUB_IMG_WIDTH = 2199;
	private static final int SUB_IMG_HEIGHT = 1465;

	// -------------- USELESS STATIC STUFF -------------------------

	// We don't need this, but it makes some warnings go away
	private static final long serialVersionUID = 1L;

	// ---------------- INSTANCE VARS -------------------------------

	// Contains every item that is currently in this room.
	private ArrayList<GameObject> myContents;

	// The current value of the spectrum slider
	private int visRange;

	protected Image currImage;
	protected BufferedImage spriteSheet;
	protected int subImgWidth;
	protected int subImgHeight;

	// -------------- FUNCTIONS / METHODS ---------------------------

	public Room() {
		myContents = new ArrayList<GameObject>();
		visRange = Main.SPECTRUM_INIT;
		// loadImage("style_forest_for_prototype.png", SUB_IMG_WIDTH, SUB_IMG_HEIGHT);
	}

	public void addGameObject(GameObject obj) {
		myContents.add(obj);
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
	protected void loadImage(String img, int subImgWidth, int subImgHeight) {
		this.subImgWidth = subImgWidth;
		this.subImgHeight = subImgHeight;
		try {
			spriteSheet = ImageIO.read(new File(GameObject.IMAGE_DIRECTORY + img));
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

	// --------------- SPECTRUM IMAGE STUFF ---------------------

	/**
	 * Updates the Image of the GameObject depending on the new spectrum value.
	 * 
	 * NOTICE: This code has been duplicated from GameObject
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

		// if (visRange % Main.SPECTRUM_MAJOR_TICKS == 0) {
		// updateSubImage();
		// } else {
		// // NOTE - the blend code is a protected method in GameObject so that all objs can blend
		// // images if/when they need to
		//
		// // get the image before you
		// int befX = visRange / Main.SPECTRUM_MAJOR_TICKS;
		// BufferedImage before = spriteSheet.getSubimage(befX * subImgWidth, 0, subImgWidth,
		// subImgHeight);
		//
		// // get the image behind you
		// int behX = (visRange + Main.SPECTRUM_MAJOR_TICKS) / Main.SPECTRUM_MAJOR_TICKS;
		// BufferedImage behind = spriteSheet.getSubimage(behX * subImgWidth, 0, subImgWidth,
		// subImgHeight);
		//
		// // blend the two images
		// currImage = blend(before, behind, (5 - (visRange % Main.SPECTRUM_MAJOR_TICKS)) / 5.0);
		// }
	}

	/**
	 * WARNING!! This method is intended to blend two images that are assumed (and as of this
	 * writing it's basically guaranteed...) to be the same height and width. Don't try this if you
	 * don't know what size you're images are!!!
	 * 
	 * NOTICE: This code has been duplicated from GameObject
	 * 
	 * @author Teresa
	 */
	protected BufferedImage blend(BufferedImage b1, BufferedImage b2, double weight) {
		// create a new image and get its graphics
		BufferedImage blended = new BufferedImage(b1.getWidth(), b1.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
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

	// -------------- PAINTING STUFF ------------------

	/**
	 * This override of the paint method is what is currently causing customized drawing. To get
	 * this code to run just call the Room's repaint method on the instance.
	 * 
	 * @author Teresa
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // IMPORTANT!!! DO NOT GET RID OF THIS LINE!!
		paintSelf(g);
		paintContents(g);
	}

	private void paintSelf(Graphics g) {
		// updateSubImage();
		// g.drawImage(currImage, 0, 0, null);

		Graphics2D g2 = (Graphics2D) g;
		int range = visRange % Main.SPECTRUM_MAJOR_TICKS;

		// if we're exactly on one spectra
		if (range == 0) {
			Rectangle2D background = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			g2.draw(background);
			g2.setPaint(BG_COLORS[visRange / Main.SPECTRUM_MAJOR_TICKS]);
			g2.fill(background);
		} else { // compute a middle value for the bg
			// get the color to the left
			int left = visRange - range;
			Color cleft = BG_COLORS[left / Main.SPECTRUM_MAJOR_TICKS];

			// get the color to the right
			int right = visRange + (Main.SPECTRUM_MAJOR_TICKS - range);
			Color cright = BG_COLORS[right / Main.SPECTRUM_MAJOR_TICKS];

			int red;
			int green;
			int blue;
			switch (range) {
			case 4:
				red = (int) ((cleft.getRed() * .15) + (cright.getRed() * .85));
				green = (int) ((cleft.getGreen() * .15) + (cright.getGreen() * .85));
				blue = (int) ((cleft.getBlue() * .15) + (cright.getBlue() * .85));
				break;
			case 3:
				red = (int) ((cleft.getRed() * .40) + (cright.getRed() * .60));
				green = (int) ((cleft.getGreen() * .40) + (cright.getGreen() * .60));
				blue = (int) ((cleft.getBlue() * .40) + (cright.getBlue() * .60));
				break;
			case 2:
				red = (int) ((cleft.getRed() * .60) + (cright.getRed() * .40));
				green = (int) ((cleft.getGreen() * .60) + (cright.getGreen() * .40));
				blue = (int) ((cleft.getBlue() * .60) + (cright.getBlue() * .40));
				break;
			case 1:
				red = (int) ((cleft.getRed() * .85) + (cright.getRed() * .15));
				green = (int) ((cleft.getGreen() * .85) + (cright.getGreen() * .15));
				blue = (int) ((cleft.getBlue() * .85) + (cright.getBlue() * .15));
				break;
			default:
				red = (int) ((cleft.getRed() * .5) + (cright.getRed() * .5));
				green = (int) ((cleft.getGreen() * .5) + (cright.getGreen() * .5));
				blue = (int) ((cleft.getBlue() * .5) + (cright.getBlue() * .5));
			}
			Color mix = new Color(red, green, blue);
			Rectangle2D background = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			g2.draw(background);
			g2.setPaint(mix);
			g2.fill(background);
		}
	}

	/**
	 * Iterate over objects in the room, compute their visibilty and draw them.
	 * 
	 * @author Teresa
	 */
	private void paintContents(Graphics g) {
		for (GameObject o : myContents) {
			o.setVisRange(visRange); // make sure visRange is up to date
			Image oImage = o.getImage(); // get o's current image
			Rectangle oBounds = o.newBounds; // get x and y location
			g.drawImage(oImage, oBounds.x + o.imgOffsetX, oBounds.y + o.imgOffsetY, null);
		}
	}

	// -------------- GAME LOOP STUFF ------------------

	/**
	 * Updates the state of all objects in room, then does collision detection.
	 */
	public void updateState() {
		for (GameObject o : myContents) {
			if (o.isActive) {
				o.updateState();
				for (GameObject p : myContents) {
					if (p != o) {
						o.collisionTest(p);
					}
				}
			}
		}
	}
}
