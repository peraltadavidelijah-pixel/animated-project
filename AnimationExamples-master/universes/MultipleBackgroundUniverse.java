import java.util.ArrayList;

public class MultipleBackgroundUniverse implements Universe {

	/*
	 * An example of using multiple backgrounds that are shifted at different ratios to achieve a "parallax" effect
	 */
	
	private static final int GROUND_MINY = 250;
	private boolean complete = false;	
	private Background mountainBackground = null;	
	private Background forestBackground = null;	
	private Background skyBackground = null;	
	private ArrayList<Background> backgrounds = null;
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();

	public MultipleBackgroundUniverse () {

		/*
		 * Each of these backgrounds has a transparency layer. They are added in order of their depth of field. That is,
		 * the furthest is added first, and the nearest last. Thus, when rendered, the nearest will sit over top of the
		 * furthest 
		 */
		skyBackground = new NightSkyBackground();
		mountainBackground = new MountainBackground();
		forestBackground = new ForestBackground();

		backgrounds = new ArrayList<Background>();
		backgrounds.add(skyBackground);
		backgrounds.add(mountainBackground);
		backgrounds.add(forestBackground);
		
		
		/*
		 * The universe contains a sprite on a near-infinite barrier which is the ground. 
		 */
		this.setXCenter(0);
		this.setYCenter(0);
		player1 = new JumpingSprite(0, -250);
		sprites.add(player1);
		sprites.add(new BarrierSprite(-1000000, 0, 1000000, 500, false));
			
	}

	public double getScale() {
		return 1;
	}

	/*
	 * The camera should follow the player in the x dimension, but remain on the ground in the y dimension, which
	 * allows the player to jump without moving the camera
	 */
	public double getXCenter() {
		return this.player1.getCenterX();
	}

	public double getYCenter() {
		return -GROUND_MINY;
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

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}

	public boolean centerOnPlayer() {
		return false;
	}		

	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	
	
	public void update(Animation animation, long actual_delta_time) {

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, actual_delta_time);
    	}

		/*
		 * The parallax effect is achieved by shifting each background by a fraction of the player's movement on the
		 * x axis. By shifting the furthest background (the sky) at the same rate as the player, it remains "in place"
		 * while the player moves. The background appears far away
		 * 
		 * The middle background (mountains) is shifted at a ratio close to 1, which again makes it appear far away, but
		 * there is some relative motion. As the player moves left or right, it appears to slowly move left or right relative
		 * to the mountains, which gives the sense that the image would be very large if it was closer
		 * 
		 * The foreground (forest) is shifted at a lesser ratio again, which allows the player to move quickly relative
		 * to it
		 * 
		 * Note that the background could be shifted in the y dimension but are not.
		 */
		this.skyBackground.setShiftX((player1.getCenterX() * 1));
		this.mountainBackground.setShiftX((player1.getCenterX() * 0.85));
		this.forestBackground.setShiftX((player1.getCenterX() * 0.5));
		
	}

	public String toString() {
		return "MultiBackgroundUniverse";
	}
}
