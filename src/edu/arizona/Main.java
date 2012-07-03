package edu.arizona;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

/**
 * The program's point of entry! This class extends JFrame, which is basically like a Window for any
 * program. This class also runs the gameloop.
 * 
 * @author Livio
 */
public class Main extends JFrame implements KeyListener {

	// ----------------- DEFINITIONS -------------------------------

	private static final double FPS = 30.0;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	// -------------- USELESS STATIC STUFF -------------------------

	// We don't need this, but it makes some warnings go away
	private static final long serialVersionUID = 1L;

	/**
	 * When the program is executed, this is the starting point.
	 * 
	 * @param args
	 *            - command line arguments
	 */
	public static void main(String[] args) {
		(new Main()).setVisible(true); // create new Main then make visible.
	}

	// ---------------- INSTANCE VARS -------------------------------

	private Container cp;
	private Room room; // Assuming there's only one room right now
	private Timer timer;
	private Player player;

	// -------------- CONSTRUCTORS & INIT METHODS ------------------

	public Main() {
		super("IGF Prototype Phase 2");

		initWindow();
		initLevel();
		initGameLoop();

		this.validate();
	}

	private void initWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	private void initLevel() {
		cp = getContentPane();
		cp.setFocusable(true);
		cp.addKeyListener(this);

		room = new Room();
		cp.add(room);
		
		player = new Player(50, 50);
	}

	private void initGameLoop() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				updateGame();
				drawGame();
			}
		}, 0, (long) (1000 / FPS));
	}
	
	// -------------- KEY HANDLER METHODS ------------------

	@Override
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.keyUp = true;
			break;
		case KeyEvent.VK_RIGHT:
			player.keyRight = true;
			break;
		case KeyEvent.VK_DOWN:
			player.keyDown = true;
			break;
		case KeyEvent.VK_LEFT:
			player.keyLeft = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.keyUp = false;
			break;
		case KeyEvent.VK_RIGHT:
			player.keyRight = false;
			break;
		case KeyEvent.VK_DOWN:
			player.keyDown = false;
			break;
		case KeyEvent.VK_LEFT:
			player.keyLeft = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	// -------------- GAME LOOP STUFF ------------------

	private void updateGame() {
		
	}

	private void drawGame() {
		room.repaint();
	}
}
