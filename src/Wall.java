/**
 * An ultimately colliable obj that will be able to appear
 * in most (if not all) spectra
 * @author Teresa
 *
 */
public class Wall extends GameObject{

	public Wall(int x, int y, Room r) {
		super(x, y, r, true, "wall.png"); //hard coded collision value (walls should always be collidable)
	}
	
	@Override
	public void update(){
		changeGraphics();
	}
}
