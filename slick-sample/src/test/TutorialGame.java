package test;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TutorialGame extends BasicGame{
	double textTimer;

	public TutorialGame(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.setColor(Color.yellow);
		g.drawString("Hello World!",
				(int)(arg0.getWidth()/2+ 50*Math.sin(textTimer)),
				(int)(arg0.getHeight()/2+ 50*Math.cos(textTimer)));
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		textTimer = 100;
		arg0.setTargetFrameRate(80);
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		textTimer += 0.05;
	}

}
