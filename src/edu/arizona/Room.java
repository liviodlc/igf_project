package edu.arizona;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This class keeps track of all the objects in a room, and handles things like
 * updating, and knows what the current visiblity is.
 * 
 * This represents both the model and the view of a Room. Livio thinks this
 * makes more sense conceptually, even though it goes against the Model-View-
 * Controller design pattern.
 * 
 * @author Teresa, Livio
 * 
 */
public class Room extends JPanel {

	// ----------------- DEFINITIONS -------------------------------

	private static final Color[] BG_COLORS = { Color.gray, Color.yellow,
			Color.red, Color.white, Color.blue, Color.black, Color.gray };

	// -------------- USELESS STATIC STUFF -------------------------

	// We don't need this, but it makes some warnings go away
	private static final long serialVersionUID = 1L;

	// ---------------- INSTANCE VARS -------------------------------

	/**
	 * Contains every item that is currently in this room.
	 */
	private ArrayList<GameObject> myContents;

	// -------------- FUNCTIONS / METHODS ---------------------------

	public Room() {
		myContents = new ArrayList<GameObject>();
	}

	/**
	 * This override of the paint method is what is currently causing customized
	 * drawing To get this code to run just call the RoomPanel's repaint method
	 * on the instance.
	 * 
	 * @author Teresa
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // IMPORTANT!!! DO NOT GET RID OF THIS LINE!!

		Graphics2D g2 = (Graphics2D) g;
		int vis = 15; // TODO: this is default value, make it change
		int range = vis % 5; // THIS IS HARD CODED!!!

		// if we're exactly on one spectra
		if (range == 0) {
			Rectangle2D background = new Rectangle2D.Double(0, 0, this
					.getWidth(), this.getHeight());
			g2.draw(background);
			g2.setPaint(BG_COLORS[vis / 5]);// ALSO HARD CODED!!!
			g2.fill(background);
		}
		// compute a middle value for the bg
		else {
			// get the color to the left
			int left = vis - range;
			Color cleft = BG_COLORS[left / 5];

			// get the color to the right
			int right = vis + (5 - range);
			Color cright = BG_COLORS[right / 5];

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
			Rectangle2D background = new Rectangle2D.Double(0, 0, this
					.getWidth(), this.getHeight());
			g2.draw(background);
			g2.setPaint(mix);
			g2.fill(background);
		}
		// iterate over objects in the room, compute their visibilty and draw
		// them

		for (GameObject o : myContents) {
			// get o's current image
//			Image oImage = o.getImage();
//			int xCoord = o.getXCoord();
//			int yCoord = o.getYCoord();

			// draw the image:
//			g.drawImage(oImage, xCoord, yCoord, null);
		}
	}
}
