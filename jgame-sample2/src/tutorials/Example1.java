package tutorials;

import jgame.JGColor;
import jgame.JGPoint;
import jgame.platform.JGEngine;

public class Example1 extends JGEngine {
	
	double textTimer = 0;
	
	public static void main(String [] args){
		new Example1(new JGPoint(640, 480));
	}
	
	public Example1(){
		initEngineApplet(); //starts engine as applet
	}
	
	public Example1(JGPoint size){
		initEngine(size.x, size.y); //starts engine as application
	}	

	/**
	 * NOTE:
	 * As far as I can currently tell, there is no way around this
	 * tile-style setup of the canvas.  Judging on other tutorials,
	 * we may be able to slap a background image on and call it good
	 * despite the fact that this is inherently tile based.
	 */
	@Override
	public void initCanvas() {
		setCanvasSettings(20, //canvas width in tiles
						  15, //canvas height in tiles
						  16, //width of one tile
						  16, //height of one tile
						  null, //foreground color -> default to white
						  null, //background color -> default to black
						  null); //default font
	}

	/**
	 * NOTE:
	 * This would be where all the *primary* loading would be done,
	 * but not necessarily where on the fly loading would be done
	 */
	@Override
	public void initGame() {
		setFrameRate(35, //frame rate
					 2); //skip at most 2 before the next display
	}
	
	/**
	 * This method is what handles any/all game logic
	 * Of course we can modularize to our hearts content, but the
	 * calls to those modules/methods/classes must come from here
	 * Graphics are handled separately. See below
	 */
	public void doFrame(){
		textTimer += 0.05;
	}
	
	/**
	 *paintFrame is recommended by the first tutorial to be used for drawing things
	 *such as:
	 *status information
	 *HUD information and elements
	 *This doesn't seem to include things like player/enemy sprites or background
	 *According to my research it looks like objects (ex: the player) would
	 *draw themselves with a paint method in their class
	 */
	public void paintFrame(){
		setColor(JGColor.yellow);
		//drawing text that moves in a circle
		drawString("Hello World", //text to draw
				   viewWidth()/2 + 50*Math.sin(textTimer), //xpos
				   viewHeight()/2 + 50*Math.cos(textTimer), //ypos
				   0);
	}

}
