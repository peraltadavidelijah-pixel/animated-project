import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/*
 * This class is an example of how to create an animation by rotating a single image at run time and storing the resultant images
 * in an array. The rotations are done upon instantiation, which does mean that the instantiation may be relatively slow,
 * but little processing time is subsequently needed to access the correct image. 
 */

public class RotatingSprite implements DisplayableSprite {
	
	private double currentAngle = 90;
	private double ROTATION_SPEED = 72;	//degrees per second
	private final static int FRAMES = 360;
	private static Image[] rotatedImages = new Image[FRAMES];
	private static boolean framesLoaded = false;

	private double centerX = 0;
	private double centerY = 0;
	private double width = 200;
	private double height = 200;
	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	
	public RotatingSprite(double centerX, double centerY) {

		super();
		this.centerX = centerX;
		this.centerY = centerY;				

		Image defaultImage = null;

		/*
		 * Loading of the original image, and the creation of an array of rotated images. Note the use of
		 * a static array to store the images... this ensures that only a single copy of each rotated image is
		 * stored, even if there are multiple instances of this sprite
		 */
		if (framesLoaded == false) {
			try {
				defaultImage = ImageIO.read(new File("res/earth-polar-view.png"));
								
				for (int i = 0; i < FRAMES; i++) {
					rotatedImages[i] = ImageRotator.rotate(defaultImage, i);
				}

			}
			catch (IOException e) {
			}
			framesLoaded = true;
		}		
	}

	public Image getImage() {
		/* 
		 * Convert angle into the correct image. The angle should already be >=0 and <= 360, but
		 * we want to be careful when indexing into an array.
		 * 
		 * Note the use of floorMod instead of mod, as per https://en.wikipedia.org/wiki/Modulo
		 */
		return rotatedImages[Math.floorMod((int)currentAngle, 360)];
	}
	
	//DISPLAYABLE
	
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

	public void update(Universe universe, long actual_delta_time) {

		/*
		 * Calculation for which image to display: an instance variable is maintained with the current
		 * angle, and this is updated on current rotation speed and time elapsed. This allows the rotation speed
		 * to change if desired. Note the logic for keeping angle within range 0 < angle < 359
		 */
		
		currentAngle -= (ROTATION_SPEED * (actual_delta_time * 0.001));
		
		//keep the angle within standard range		
	    if (currentAngle >= 360) {
	    	currentAngle -= 360;
	    }
	    if (currentAngle < 0) {
	    	currentAngle += 360;
	    }
	    
	}
}
