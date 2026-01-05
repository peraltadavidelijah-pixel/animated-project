import java.util.ArrayList;

public class MovableSpriteUniverse implements Universe {

	private boolean complete = false;	
	private ArrayList<Background> backgrounds = new ArrayList<Background>();	
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private int phase = 0;
	double centerX = AnimationFrame.STANDARD_SCREEN_WIDTH * 0.5;
	double centerY = AnimationFrame.STANDARD_SCREEN_HEIGHT * 0.5;

	private final double VELOCITY = 200;	

	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();


	public MovableSpriteUniverse () {

		SingleTileBackground background = new SingleTileBackground();
		backgrounds.add(background);

		player1 = new DEPSprite(300,0);
		sprites.add(player1);
		phase = 13;

	}

	public double getScale() {
		return 1;
	}

	public double getXCenter() {
		return centerX;
	}

	public double getYCenter() {
		return centerY;
	}

	public void setXCenter(double xCenter) {
	}

	public void setYCenter(double yCenter) {
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
	}

	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}

	public DisplayableSprite getPlayer1() {
		return player1;
	}

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}

	public boolean centerOnPlayer() {
		return false;
	}		

	public void update(Animation animation, long actual_delta_time) {

		double velocityX = 0;
		double velocityY = 0;

		for (int i = 0; i < sprites.size(); i++) {

			DisplayableSprite sprite = sprites.get(i);
			MovableSprite movable = (MovableSprite) sprite;

			//transition function
			if (phase == 0 && sprite.getCenterX()  > 175 ) {
				phase = 1;
			} else if (phase == 1 && sprite.getCenterY() < 175) {
				phase = 2;
			} else if (phase == 2 && sprite.getCenterX() > 400) {
				phase = 3;
			} else if (phase == 3 && sprite.getCenterY() > 575) {
				phase = 4;
			} else if (phase == 4 && sprite.getCenterX() < 100) {
				phase = 5;
			} else if (phase == 5 && sprite.getCenterY() > 710) {
				phase = 6;
			} else if (phase == 6 && sprite.getCenterX() > 790) {
				phase = 7;
			} else if (phase == 7 && sprite.getCenterY() < 500) {
				phase = 8;
			} else if (phase == 8 && sprite.getCenterX() < 565) {
				phase = 9;
			} else if (phase == 9 && sprite.getCenterY() < 305) {
				phase = 10;
			} else if (phase == 10 && sprite.getCenterX() > 790) {
				phase = 11;
			} else if (phase == 11 && sprite.getCenterY() < 85) {
				phase = 12;
			} else if (phase == 12 && sprite.getCenterX() < 495) {
				phase = 13;
			} else if (phase == 13 && sprite.getCenterY() <= 0) {
				movable.setCenterX(25);		// near left hand edge of screen
				movable.setCenterY(375);		// near center of screen height
				phase = 0;
			} else {
				// should not occur
			}


			//movement based on phase
			if (phase == 0 || phase == 2 || phase == 6 || phase == 10) {
				// RIGHT
				velocityX = VELOCITY;
			}
			else if (phase == 1 || phase == 7 || phase == 9 || phase == 11 || phase == 13) {
				//UP
				velocityY = -VELOCITY;						
			}
			else if (phase == 4 || phase == 8 || phase == 12) {			
				//LEFT	
				velocityX = -VELOCITY;			
			}
			else {
				// DOWN
				velocityY += VELOCITY;
			}


			if (sprite instanceof MovableSprite) {
				movable.setVelocityX(velocityX);
				movable.setVelocityY(velocityY);
			}

			sprite.update(this, actual_delta_time);
		}    	

	}

	public String toString() {		
		return MovableSpriteUniverse.class.getCanonicalName();
	}

}
