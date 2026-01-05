import java.util.ArrayList;

public class MappedUniverse implements Universe {

	private boolean complete = false;
	private ArrayList<Background> backgrounds = null;
	private MappedBackground background = null;	
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private boolean inPortal = false;
	/*
	 * An example of having the universe center correspond to the player location, but
	 * with smoothing implemented on the motion. You can also think of this as a 'camera'
	 * following the player with a slight delay, and moving slower as it gets closer
	 * from the player. See the use of these variables and constant in the getCenterX/Y
	 * methods and the update method. Credit to AM for the contribution.
	 */
	private double centerX;
	private double centerY;
	private static final double SMOOTHING_FACTOR = 0.03;

	public MappedUniverse () {

		background = new MappedBackground();
		ArrayList<DisplayableSprite> barriers = ((MappedBackground)background).getBarriers();
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background);
		
		player1 = new CollidingSprite(MappedBackground.TILE_HEIGHT * 2, MappedBackground.TILE_WIDTH * 2, MappedBackground.TILE_HEIGHT * 0.9, MappedBackground.TILE_HEIGHT * 0.9);
		
		sprites.add(player1);
		sprites.addAll(barriers);
		sprites.add(background.getExit());
		
		
	}

	public double getScale() {
		return 1;
	}	
	
	public double getXCenter() {
		return this.centerX;
	}

	public double getYCenter() {
		return this.centerY;
	}
	
	public void setXCenter(double xCenter) {
	}

	public void setYCenter(double yCenter) {
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}
		
	public boolean centerOnPlayer() {
		return true;
	}		
	
	public void update(Animation animation, long actual_delta_time) {
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
    	}
		
		// universe can do collision detection - in this case it makes sense, as the universe has references to the two sprites
		// whose collision it needs to detect, and direct access to the parent animation
		if (CollisionDetection.overlaps(player1, background.getExit())) {
			// we do only want to detect if the player moved into the portal... if they
			// were already there, then force them to completely exit first...
			// this prevents universes from switching continuously
			if (inPortal == false) {
				inPortal = true;
				// you can switch to a different universe by invoking the transition function of the parent animation
				animation.switchUniverse(PatternedUniverse.class.toString());
			}
		}
		else {
			inPortal = false;
		}
		
        double playerX = player1.getCenterX();
        double playerY = player1.getCenterY();
        
        this.centerX += (playerX - this.centerX) * SMOOTHING_FACTOR; //implemented linear-interpolation smoothing based on online formulas and ChatGPT
        this.centerY += (playerY - this.centerY) * SMOOTHING_FACTOR; //:D
		
		
	}

	public String toString() {
		return "MappedUniverse";
	}	

}
