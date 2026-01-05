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
 * This class is an example of how to create an animation by iterating through a sequence of images that were
 * generated at design time, and which represnt the frames of an animation. In this case, an animated GIF was
 * deconstructed into individual images using online tools.
 */
public class AnimatedSprite implements DisplayableSprite {
	
	private int currentFrame = 0;
	private long elapsedTime = 0;
	private final static int FRAMES = 150;
	private double framesPerSecond = 30;
	private double milliSecondsPerFrame = 1000 / framesPerSecond;
	private static Image[] frames = new Image[FRAMES];
	private static boolean framesLoaded = false;	
	
	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 200;
	private double height = 200;
	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	
	public AnimatedSprite(double centerX, double centerY, double framesPerSecond) {

		this.centerX = centerX;
		this.centerY = centerY;		

		this.framesPerSecond = framesPerSecond;
		this.milliSecondsPerFrame = 1000 / framesPerSecond;
		long startTime = System.currentTimeMillis();
		
		/*
		 * Loading of the images into an array. The image files are named in sequence by the online tool that split
		 * the animated GIF, and String formatting is used to re-create the correct file name. Note the use of
		 * a static array to store the images... this ensures that only a single copy of each rotated image is
		 * stored, even if there are multiple instances of this sprite.
		 */
		if (framesLoaded == false) {
			for (int frame = 0; frame < FRAMES; frame++) {
				String filename = "res/animated-earth/frame_" + String.format("%03d", frame) + "_delay-0.04s.gif";
				try {
					frames[frame] = ImageIO.read(new File(filename));
				}
				catch (IOException e) {
					System.err.println(e.toString());
				}		
			}
			
			if (frames[0] != null) {
				framesLoaded = true;
			}
		}		
	}

	public Image getImage() {
		return frames[currentFrame];
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
		 * Calculation for which image to display
		 * 1. calculate how many periods of time have elapsed since this sprite was instantiated?
		 * 2. calculate which image (aka 'frame') of the sprite animation should be shown out of the cycle of images
		 */

		elapsedTime += actual_delta_time;
		long elapsedFrames = (long) (elapsedTime / milliSecondsPerFrame);
		currentFrame = (int) (elapsedFrames % FRAMES);
	
	}
}
