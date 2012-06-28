import java.util.ArrayList;


public class Player extends GameObject{
	
	private int jumping;

	public Player(int x, int y, Room r) {
		super(x, y, r, true, "player.png"); //player should collide with collidable things
		//start jumping at 0
		jumping = 0; //no jumping is happening
		//TODO: pull in the sprite sheet
		//get the first pic loaded
	}
	
	//method to determine if a player can move in a given direction
	public boolean canMove(char direction){
		//dig out coords from the room and make sure there isn't a collidable obj
		//in the given direction
		Room r = super.getRoom();
		ArrayList<GameObject> objs = r.getObjs();
		
		switch(direction){
			case 'l':
				int leftCheck = super.getXCoord();
				for (GameObject o : objs){
					//see if the object is collidable:
					if(!o.isSolid() || o == this){
						continue;
					}
					//get the farthest right side of the object in question
					int xVal = o.getXCoord()+32;//this uses a hard coded value!!!!!
					
					//see if moving left 1 pixel will cause a conflict
					if((leftCheck-1) <= xVal){
						return false;
					}
				}
				break;
			case 'r':
				int rightCheck = super.getXCoord()+32;//this uses a hard coded value!!!
				for (GameObject o : objs){
					//see if the object is collidable:
					if(!o.isSolid() || o == this){
						continue;
					}
					//get the left side of the obj
					int xVal = o.getXCoord();
					
					//see if moving right 1 pixel will cause a conflict
					if((rightCheck+1) >= xVal){
						return false;
					}
				}
				break;
			case 'u':
				int upCheck = super.getYCoord();
				for(GameObject o : objs){
					//see if the player can collide
					if(!o.isSolid() || o == this){
						continue;
					}
					//get the bottom part of the object
					int yVal = o.getYCoord()+32; //uses a hard coded value!!!
					
					//see if moving up one pixel will cause a conflict
					if((upCheck-1) <= yVal){
						return false;
					}
				}
				break;
			case 'd':
				int downCheck = super.getYCoord()+32; //uses hard coded value!!!
				for(GameObject o : objs){
					//see if player can collide:
					if(!o.isSolid() || o == this){
						continue;
					}
					//get the top of the object
					int yVal = o.getYCoord();
					
					//see if a collision will occur in a move of 1 pixel:
					if((downCheck+1) >= yVal){
						return false;
					}
				}
				break;
		}
		return true;
	}
	
	//method to actually move the player 1 pixel in the given direction
	//returns true if the move occurred successfully
	public boolean move(char direction){
		if(canMove(direction)){
			switch(direction){
				case 'l':
					super.xcoord = super.xcoord-1;
					return true;
				case 'r':
					super.xcoord = super.xcoord+1;
					return true;
				case 'u':
					super.ycoord = super.ycoord-1;
					return true;
				case 'd':
					super.ycoord = super.ycoord+1;
					return true;
			}
		}
		return false;
	}
	
	@Override
	public void update(){
		//update position:
		//if jumping
		if(jumping > 0){
			for(int i = 0; i<3; i++){//loop to mimic velocity
				if(!move('u')){//if you couldn't move up, you hit something, so stop jumping
					jumping = 0;
					break;
				}
			}
			jumping--;
		}
		//if not jumping
		else{
			for(int i = 0; i<3; i++){//fall, if it's possible
				if(!move('d')){
					break;
				}
			}
		}
		//graphics update code:
		changeGraphics();
	}

}
