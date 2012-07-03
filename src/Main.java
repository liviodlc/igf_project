import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * This class contains:
 * 1) the main function that starts the program running
 * 2) the base set of graphics code for the window
 */
public class Main {

	public static void main(String[] args) {
		new Main();
	}

	private JFrame window;
	private Container c;
	private RoomPanel roomView;
	private Room room;
	private JSlider spectrum;
	private int controlSet;

	private boolean mousePressed;

	public Main() {
		controlSet = 0; // initialize to 0
		setWindow();
		setRooms();
		setSlider();
		mousePressed = false;
		window.validate();
		room.startUpdates();
	}

	public void setWindow() {
		// create the window
		window = new JFrame("Le window");
		c = window.getContentPane();

		// set the window properties
		window.setSize(800, 600);
		window.setResizable(false);// don't let people resize the window
		window.setLayout(null); // may need an actual layout manager
		window.setVisible(true);
		
		// exit the program when the window closes:
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setRooms() {
		// code for the backend
		room = new Room(15); // hard coded value..
		room.setRoom();

		// code for the frontend
		roomView = new RoomPanel(room);
		roomView.setLocation(0, 0);
		roomView.setSize(window.getWidth(), 515);
		roomView.setVisible(true);
		roomView.setFocusable(true);
		roomView.addKeyListener(new ControlListener());
		c.add(roomView);
	}

	public void setSlider() {
		// create the slider:
		int min = 0;
		int max = 30;
		int init = 15;
		spectrum = new JSlider(JSlider.HORIZONTAL, min, max, init);

		// temp code to set the slider's details
		spectrum.setMajorTickSpacing(5);
		spectrum.setPaintTicks(true);
		spectrum.setFocusable(false);
		spectrum.addChangeListener(new SpectrumListener());
		spectrum.addMouseListener(new SpectrumMouseListener());

		spectrum.setLocation(0, window.getHeight() - 80);
		spectrum.setSize(window.getWidth(), spectrum.getPreferredSize().height);
		spectrum.setVisible(true);
		c.add(spectrum);
	}

	private class ControlListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent arg0) {
			if (mousePressed) {
				return;
			}
			if (controlSet == 0) {
				int value = spectrum.getValue();
				switch (arg0.getKeyChar()) {
				case 'z':
					if (value > spectrum.getMinimum()) {
						spectrum.setValue(value
								- spectrum.getMajorTickSpacing());
						room.changeVis(value - spectrum.getMajorTickSpacing());
						roomView.repaint();
					}
					break;
				case 'x':
					break;
				case 'c':
					if (value < spectrum.getMaximum()) {
						spectrum.setValue(value
								+ spectrum.getMajorTickSpacing());
						room.changeVis(value + spectrum.getMajorTickSpacing());
						roomView.repaint();
					}
					break;
				case 'a':
					// move player left
					System.out.println("Moving left!");
					room.movePlayer('l');
					break;
				case 'd':
					// move player right
					System.out.println("Moving right!");
					room.movePlayer('r');
					break;
				case 'w':
					break;
				default:
				}
			} else {
				System.out.println("Control value is not 0");
			}
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		/* Methods that don't need to be implemented */
		@Override
		public void keyReleased(KeyEvent arg0) {
		}
	}

	private class SpectrumListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			int newVis = spectrum.getValue();
			room.changeVis(newVis);
			roomView.repaint();
		}
	}

	/**
	 * JSlider has a jump to tick function, but since the spacing is actually 35
	 * ticks, the function will likely jump to unmarked ticks and not major
	 * ticks. This class makes the jumping to major ticks (absolute spectrum
	 * places, like visible/infared/etc) but allows the user to drag the slider
	 * between and see things between the spectra
	 */
	private class SpectrumMouseListener implements MouseListener {

		@Override
		public void mouseReleased(MouseEvent arg0) {

			int place = spectrum.getValue();
			int diff = place % 5;

			if (diff >= 3) {
				spectrum.setValue(place + (5 - diff));
			} else {
				spectrum.setValue(place - diff);
			}
			roomView.repaint();
			mousePressed = false;
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			mousePressed = true;
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
	}
}