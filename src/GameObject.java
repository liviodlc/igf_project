import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The base class that all items must extend in order to be rendered in the game Note that some
 * extensions of this class may not necessarily need the hardwired vis value (and in fact will have
 * more than one vis range)
 * 
 * @author Teresa
 * 
 */
public class GameObject {

	// private int visRange;
	protected int xcoord;
	protected int ycoord;
	private int xSize;
	private int ySize;
	protected Image currImage;
	private BufferedImage spriteSheet;
	private Room room;
	private boolean solid;
	private boolean dead;

	public GameObject(int x, int y, Room r, boolean s, String pic) {
		xcoord = x; // starting position
		ycoord = y; // starting position
		xSize = ySize = 32; // hard coded pixel size
		room = r; // this has to be here for collision testing purposes
		solid = s; // to allow collision checking
		dead = false; // allow us to remove items from the list

		// hard coded to get the picture
		String place = System.getProperty("user.dir");
		try {
			BufferedImage i = ImageIO.read(new File(place + "/data.images/" + pic));
			setSpriteSheet(i);// set the main sprite

			// set the current sprite(at visual range 3 (or visible light))
			currImage = i.getSubimage(32 * 3, 0, 32, 32);
		} catch (IOException e) {
			System.out.println("Unable to load resource: " + pic);
		}

	}

	public void setSpriteSheet(BufferedImage i) {
		spriteSheet = i;
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	// public int getVisibility(){
	// return visRange;
	// }

	public int getXCoord() {
		return xcoord;
	}

	public int getYCoord() {
		return ycoord;
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public Image getImage() {
		return currImage;
	}

	public boolean isDead() {
		return dead;
	}

	public void setRoom(Room r) {
		room = r;
	}

	public Room getRoom() {
		return room;
	}

	protected void setDead(boolean d) {
		dead = d;
	}

	public boolean isSolid() {
		return solid;
	}

	protected void changeGraphics() {
		// get the current visibility

		int range = room.getVisRange();
		// System.out.println("Range: "+range);
		int range1 = range / 5;
		// updating its main sprite to match

		// if we have a straight picture w/no modification or blending to other spectra
		if (range % 5 == 0) {// HARD CODE!!!!
			switch (range1) {
			case 0:
				// System.out.println("Radio State");
				currImage = spriteSheet.getSubimage(0 * 32, 0, 32, 32);
				break;
			case 1:
				// System.out.println("Microwave State");
				currImage = spriteSheet.getSubimage(1 * 32, 0, 32, 32);
				break;
			case 2:
				// System.out.println("Infared State");
				currImage = spriteSheet.getSubimage(2 * 32, 0, 32, 32);
				break;
			case 3:
				// System.out.println("Visible State");
				currImage = spriteSheet.getSubimage(3 * 32, 0, 32, 32);
				break;
			case 4:
				// System.out.println("UV State");
				currImage = spriteSheet.getSubimage(4 * 32, 0, 32, 32);
				break;
			case 5:
				// System.out.println("X-Ray State");
				currImage = spriteSheet.getSubimage(5 * 32, 0, 32, 32);
				break;
			case 6:
				// System.out.println("Gamma State");
				currImage = spriteSheet.getSubimage(6 * 32, 0, 32, 32);
				break;
			}
		}
		//
		else {
			// NOTE - the blend code is a protected method in GameObject
			// so that all objs can blend images if/when they need to

			// get the image before you
			int befX = range / 5;
			BufferedImage before = spriteSheet.getSubimage(befX * 32, 0, 32, 32);
			// get the image behind you
			int behX = (range + 5) / 5;
			BufferedImage behind = spriteSheet.getSubimage(behX * 32, 0, 32, 32);

			switch (range % 5) {// HARD CODED VALUE!!!!
			case 1:
				// System.out.println("15/85 mix");
				currImage = blend(before, behind, .85);
				break;
			case 2:
				// System.out.println("40/60 mix");
				currImage = blend(before, behind, .60);
				break;
			case 3:
				// System.out.println("60/40 mix");
				currImage = blend(before, behind, .40);
				break;
			case 4:
				// System.out.println("85/15 mix");
				currImage = blend(before, behind, .15);
				break;
			}

		}

	}

	// WARNING!! This method is intended to blend two images that are assumed (and as of
	// this writing it's basically guaranteed...) to be the same height and width
	// Don't try this if you don't know what size you're images are!!!
	protected BufferedImage blend(BufferedImage b1, BufferedImage b2, double weight) {
		// create a new image and get its graphics
		BufferedImage blended = new BufferedImage(b1.getWidth(), b1.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = blended.createGraphics();

		// draw the new image w/appropriate blending
		g2.drawImage(b1, null, 0, 0);
		g2
				.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
						(float) (1.0 - weight)));
		g2.drawImage(b2, null, 0, 0);
		g2.dispose();

		return blended;
	}

	// other classes should override this
	public void update() {
	}
}
