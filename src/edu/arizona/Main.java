package edu.arizona;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The program's point of entry! This class extends JFrame, which is basically like a Window for any
 * program. This class also runs the gameloop.
 * 
 * @author Livio
 */
public class Main extends JFrame implements KeyListener, ChangeListener, MouseListener {

	// ----------------- DEFINITIONS -------------------------------

	private static final double FPS = 30.0;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	private static final int SPECTRUM_MIN = 0;
	private static final int SPECTRUM_MAX = 30;
	public static final int SPECTRUM_INIT = 15;
	public static final int SPECTRUM_MAJOR_TICKS = 5;

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
	private JSlider spectrum;
	private boolean mouseDown;

	// -------------- CONSTRUCTORS & INIT METHODS ------------------

	public Main() {
		super("IGF Prototype Phase 2");

		initWindow();
		initSpectrum();
		initLevel();
		initGameLoop();

		this.validate();
	}

	private void initWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.setFocusable(true);
	}
	
	public void initSpectrum() {
		// create the slider:
		spectrum = new JSlider(JSlider.HORIZONTAL, SPECTRUM_MIN, SPECTRUM_MAX, SPECTRUM_INIT);

		// temp code to set the slider's details
		spectrum.setMajorTickSpacing(SPECTRUM_MAJOR_TICKS);
		spectrum.setPaintTicks(true);
		spectrum.setFocusable(false);
		spectrum.addChangeListener(this);
		spectrum.addMouseListener(this);

		spectrum.setVisible(true);
		cp.add(spectrum, BorderLayout.SOUTH);
	}

	private void initLevel() {
		room = new Room();
		room.visRange = spectrum.getValue();
		cp.add(room, BorderLayout.CENTER);

		player = new Player(50, 200);
		room.addGameObject(player);
		
		//floor:
		for(int i = 0; i < 10; i++){
			Wall w = new Wall(i * 32, 500);
			room.addGameObject(w);
		}
		
		//vertical wall
		for(int i = 1; i < 10; i++){
			Wall w = new Wall(0, 500 - (i * 32));
			room.addGameObject(w);
		}
	}

	private void initGameLoop() {
		cp.addKeyListener(this);
		
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
		int value = spectrum.getValue();

		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			player.keyUp = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			player.keyRight = true;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			player.keyDown = true;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			player.keyLeft = true;
			break;
		case KeyEvent.VK_Z:
			if (value > spectrum.getMinimum()) {
				spectrum.setValue(value - spectrum.getMajorTickSpacing());
				room.visRange = value - spectrum.getMajorTickSpacing();
				room.repaint();
			}
			break;
		case KeyEvent.VK_C:
			if (value < spectrum.getMaximum()) {
				spectrum.setValue(value + spectrum.getMajorTickSpacing());
				room.visRange = value + spectrum.getMajorTickSpacing();
				room.repaint();
			}
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

	// ----------- SPECTRUM HANDLER STUFF --------------

	@Override
	public void stateChanged(ChangeEvent arg0) {
		room.visRange = spectrum.getValue();
		room.repaint();
	}

	/**
	 * JSlider has a jump to tick function, but since the spacing is actually 35 ticks, the function
	 * will likely jump to unmarked ticks and not major ticks. This class makes the jumping to major
	 * ticks (absolute spectrum places, like visible/infared/etc) but allows the user to drag the
	 * slider between and see things between the spectra
	 * 
	 * @author Teresa
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {

		int place = spectrum.getValue();
		int diff = place % SPECTRUM_MAJOR_TICKS;

		if (diff >= 3) {
			spectrum.setValue(place + (SPECTRUM_MAJOR_TICKS - diff));
		} else {
			spectrum.setValue(place - diff);
		}
		room.repaint();
		mouseDown = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseDown = true;
	}

	/* Methods that don't need to be implemented */

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	// -------------- GAME LOOP STUFF ------------------

	private void updateGame() {
		room.updateState();
	}

	private void drawGame() {
		room.repaint();
	}

	// ------------- PRIVATE CLASSES -------------------
}
