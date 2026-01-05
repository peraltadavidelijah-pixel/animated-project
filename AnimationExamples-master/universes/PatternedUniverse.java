import java.util.ArrayList;

public class PatternedUniverse implements Universe {

	private boolean complete = false;	
	private ArrayList<Background> backgrounds = null;
	private Background background = null;	
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	
	public PatternedUniverse () {
	
		background = new AberhartBackground();
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background);

		player1 = new SimpleSprite(0,0);
		sprites.add(player1);
	
	}
		
	public double getScale() {
		return 1;
	}	
	
	public double getXCenter() {
		return this.player1.getCenterX();
	}

	public double getYCenter() {
		return this.player1.getCenterY();
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
	}
	
	public String toString() {
		return "PatternedUniverse";
	}
	
}
