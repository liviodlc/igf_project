package test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class TestMain {
	
	public static void main(String[] args){
		TutorialGame game = new TutorialGame("Slick Tutorial");
		try{
			AppGameContainer container = new AppGameContainer(game);
			container.start();
		}
		catch(SlickException e){
			e.printStackTrace();
		}
	}

}
