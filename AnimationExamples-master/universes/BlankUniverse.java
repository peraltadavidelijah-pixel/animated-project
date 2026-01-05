import java.util.ArrayList;

public class BlankUniverse implements Universe {

	@Override
	public double getScale() {
		return 1;
	}

	@Override
	public double getXCenter() {
		return 0;
	}

	@Override
	public double getYCenter() {
		return 0;
	}

	@Override
	public void setXCenter(double xCenter) {
	}

	@Override
	public void setYCenter(double yCenter) {
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public void setComplete(boolean complete) {
	}

	@Override
	public ArrayList<DisplayableSprite> getSprites() {
		return null;
	}

	@Override
	public ArrayList<Background> getBackgrounds() {
		return null;
	}

	@Override
	public void update(Animation animation, long actual_delta_time) {
	}

	public String toString() {
		return "Blank";
	}
	
}
