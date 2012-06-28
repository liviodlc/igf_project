import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

/**
 * This class keeps track of all the objects in a room,
 * and handles things like updating, and knows what the
 * current visiblity is.
 * @author Teresa
 *
 */
public class Room extends Observable{
	
	private ArrayList<GameObject> allObjs;
	private ArrayList<Observer> observers;
	private int visRange;
	private Player p;
	
	public Room(int vis){
		visRange = vis;
		allObjs = new ArrayList<GameObject>();
		observers = new ArrayList<Observer>();
	}
	
	public void addObserver(Observer o){
		observers.add(o);
	}
	
	public ArrayList<GameObject> getObjs(){
		return allObjs;
	}
	
	public int getVisRange(){
		return visRange;
	}
	
	public void addObj(GameObject o){
		allObjs.add(o);
	}
	
	public void changeVis(int vis){
		visRange = vis;
	}
	
	//current, highly simplistic update
	public void update(){
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		
		for(GameObject o : allObjs){
			if(!o.isDead()){ //if the element isn't flagged as dead, update it
				o.setRoom(this);
				o.update();
			}
			else{//otherwise, mark it's position for deletion
				toRemove.add(allObjs.indexOf(o));
			}
		}
		//remove all the dead elements
		for(Integer i : toRemove){
			allObjs.remove(i);
		}
		
		//pass this code array back to the observers:
		for(Observer o: observers){
			o.update(this, null);
		}
	}
	
	//temporary method to put things in the room
	public void setRoom(){
		//add the walls
		for(int i = 0; i<32; i++){
			allObjs.add(new Wall((32*i), 683, this));
		}
		//add the player:
		p = new Player(10, 10, this);
		allObjs.add(p);
		
		//add sound generators
		
		//add microwave generators
	}
	
	//code pertaining specifically to updating the player
	public void movePlayer(char direction){
		p.move(direction); //try to move the player
	}
	
	//code for the main update loop
	public void startUpdates(){
		new runUpdates().run(); //start the update loop running
	}
	
	private class runUpdates extends Thread{
		
		//This is the method that gets called when the thread starts
		public void run(){
			while (true){
				update();
				try {
					this.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
		}
	}
	
	

}
