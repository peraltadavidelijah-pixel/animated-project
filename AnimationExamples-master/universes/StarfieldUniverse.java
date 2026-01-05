import java.awt.Rectangle;
import java.util.ArrayList;

public class StarfieldUniverse implements Universe {

	private boolean complete = false;	
	private Background background = null;	
	private ArrayList<Background> backgrounds = null;
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	ArrayList<DisplayableSprite> disposedSprites = new ArrayList<DisplayableSprite>();
	
	public StarfieldUniverse () {
	
		player1 = new SpaceShipSprite(0,0);
		background = new SingleTileBackground("res/backgrounds/starfield.jpg");
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background);
		
		this.sprites.add(player1);
	
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
		
		disposeSprites();

	}
	
    protected void disposeSprites() {
        
    	
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
    		if (sprite.getDispose() == true) {
    			disposedSprites.add(sprite);
    		}
    	}
		for (int i = 0; i < disposedSprites.size(); i++) {
			DisplayableSprite sprite = disposedSprites.get(i);
			sprites.remove(sprite);
    	}
    	if (disposedSprites.size() > 0) {
    		disposedSprites.clear();
    	}
    }	
	
	
	public String toString() {
		return "StarfieldUniverse";
	}	
	
}
