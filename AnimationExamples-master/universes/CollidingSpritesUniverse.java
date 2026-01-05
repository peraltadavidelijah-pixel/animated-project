import java.util.ArrayList;

public class CollidingSpritesUniverse implements Universe {

	private boolean complete = false;	
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();

	public CollidingSpritesUniverse () {

		this.setXCenter(0);
		this.setYCenter(0);

		player1 = new CollidingSprite(0,250);
		sprites.add(player1);
		
		sprites.add(new PinballSprite(0,0));
				
		//add five other sprites, spread horizontally across the screen, with initial velocity
		for (int i = 0; i < 5; i++) {
			BouncingSprite sprite = new BouncingSprite(i * 100 - 200 , -100 , 200, 200);			
			sprites.add(sprite);
		}
		
		//top
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2,AnimationFrame.STANDARD_SCREEN_HEIGHT / -2, AnimationFrame.STANDARD_SCREEN_WIDTH / 2, AnimationFrame.STANDARD_SCREEN_HEIGHT / -2 + 16, true));
		//bottom
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2,AnimationFrame.STANDARD_SCREEN_HEIGHT / 2 - 16, AnimationFrame.STANDARD_SCREEN_WIDTH / 2, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2, true));
		//left
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / -2,AnimationFrame.STANDARD_SCREEN_HEIGHT / -2, AnimationFrame.STANDARD_SCREEN_WIDTH / -2 + 16, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2, true));
		//right
		sprites.add(new BarrierSprite(AnimationFrame.STANDARD_SCREEN_WIDTH / 2 - 16,AnimationFrame.STANDARD_SCREEN_HEIGHT / -2, AnimationFrame.STANDARD_SCREEN_WIDTH / 2, AnimationFrame.STANDARD_SCREEN_HEIGHT / 2, true));
		
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
		return null;
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
		
		
	}

	public String toString() {
		return "CollidingSpritesUniverse";
	}	


}
