import java.awt.Rectangle;
import java.util.ArrayList;

public class JumpingSpriteUniverse implements Universe {

	private boolean complete = false;	
	private JumpingSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private PortalSprite portal = null;
	private boolean inPortal = false;
	private int level = 0;
	
	private JumpingSpriteUniverse() {
		this(0);
	}
	
	public JumpingSpriteUniverse (int level) {
		
		this.level = level;
		
		//top platform
		sprites.add(new BarrierSprite(-50,-14, 50, 0, true));
		//bottom platform
		sprites.add(new BarrierSprite(-150,184, 150, 200, true));
		//bottom
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2,AnimationFrame.STANDARD_SCREEN_HEIGHT / 2 - 16, AnimationFrame.STANDARD_SCREEN_WIDTH / 2, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2, true));
		//left
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2,AnimationFrame.STANDARD_SCREEN_HEIGHT / -2, AnimationFrame.STANDARD_SCREEN_WIDTH / -2 + 16, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2, true));
		//right
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / 2 - 16,AnimationFrame.STANDARD_SCREEN_HEIGHT / -2, AnimationFrame.STANDARD_SCREEN_WIDTH / 2, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2, true));

		/*
		 * It is possible to have one universe class act for multiple permutations.
		 * This might make sense if much of the functionality is the same within
		 * each universe. E.g. if there are multiple levels in a maze or jumping game.
		 * In this case, based on the parameter passed into the constructor, the
		 * player and the portal are placed in different places.
		 */
		if (level == 0) {
			portal = new PortalSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / 2 - 50, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2 - 100, 100, true);
			player1 = new JumpingSprite(0,AnimationFrame.STANDARD_SCREEN_HEIGHT / 2 - 100);
		}
		else {
			portal = new PortalSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2 - 100, 100, true);
			player1 = new JumpingSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2 + 100, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2 - 50);
		}

    	// The order in the sprites list determines the order of rendering.
		// Thus, by adding these sprites last, it ensures that they will be rendered
    	// over top of other sprites such as the PortalSprite
		for (int i = 0; i < 5; i++) {
			BouncingSprite sprite = new BouncingSprite(i * 100 - 200 , -200 , 200, 0);
			sprite.setAccelerationY(500);
			sprites.add(sprite);			
		}

    	sprites.add(portal); 
		sprites.add(player1);
		
	}
	
	public double getScale() {
		return 1;
	}

	public double getXCenter() {
		return 0;
	}

	public double getYCenter() {
		return 0;
	}
	
	public void setXCenter(double xCenter) {
		this.setXCenter(xCenter);
	}

	public void setYCenter(double yCenter) {
		this.setYCenter(yCenter);
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	public ArrayList<Background> getBackgrounds() {
		return null;
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

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
    	}
				
		// universe can do collision detection - in this case it makes sense, as the universe has references to the two sprites
		// whose collision it needs to detect, and direct access to the parent animation
		if (CollisionDetection.overlaps(player1, portal)) {
			// we do only want to detect if the player moved into the portal... if they
			// were already there, then force them to completely exit first...
			// this prevents universes from switching continuously
			if (inPortal == false) {
				inPortal = true;
				// you can switch to a different universe by invoking the transition function of the parent animation
				animation.switchUniverse(JumpingSpriteUniverse.class.toString());
			}
		}
		else {
			inPortal  = false;
		}
		
	}
	
	/*
	 * This method allows for a transition between universes. The transition can be designed
	 * in many ways, but in this case we simply want the movement of the sprite in the other
	 * universe to be copied over. Perhaps other attributes (e.g. points, health) could also
	 * be copied over
	 * 
	 * Note that it would also be possible to move the other sprite into this universe, but in
	 * that design, you need to be careful to also add the sprite to the sprites list, and to
	 * potentially remove them as well
	 */
	protected void transportPlayer(JumpingSprite other) {
		this.player1.setVelocityX(other.getVelocityX());
		this.player1.setVelocityY(other.getVelocityY());
		this.player1.setCenterY(other.getCenterY());
	}
			
	public String toString() {
		return "JumpingSpriteUniverse" + Integer.toString(level);
	}	
	
}
