import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * This class creates a sprite that is static (does not move or change dimensions) and is visually not rectangular.
 * The image used has a transparency layer, and even though the bounds of this sprite are defined as a rectangle by
 * minX, minY, maxX, and maxY, the intended bound is the circumference of the pinball.
 */
public class PinballSprite implements DisplayableSprite {

	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	

	//if height and width will not change, then set these as constants
	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	//PIXELS PER SECOND PER SECOND
	private double accelerationX = 0;
	private double accelerationY = 0;		
	private double velocityX = 0;
	private double velocityY = 0;
	
	public PinballSprite(double centerX, double centerY) {

		super();
		this.centerX = centerX;
		this.centerY = centerY;	

		if (image == null) {
			try {
				image = ImageIO.read(new File("res/pinball.png"));
			}
			catch (IOException e) {
				System.err.println(e.toString());
			}
		}

		this.height = HEIGHT;
		this.width = WIDTH;

	}

	//DISPLAYABLE
	public Image getImage() {
		return image;
	}
		
	public boolean getVisible() {
		return true;
	}
	
	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};
		
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}

	//Allow other objects to get / set velocity and acceleration
	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public void update(Universe universe, long actual_delta_time) {
		
	/*
	 * This sprite is static, and does not change its location nor bounds
	 */
	}

}

