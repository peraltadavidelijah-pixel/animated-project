import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * This class is an example of how to create an animation by changing the height and/or width. In this particular
 * example, the sine function is used to determine size, which provides for a natural 'pulsing' effect (i.e. sprite
 * does not shrink or expand at constant rates; rates slow near maximum / minimum size
 */
public class SunSprite implements DisplayableSprite {
	
	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;

	private long elapsedTime = 0;
	private final static double PERIOD = 5;		//# of second per pulse
	private final static double RADIANS_PER_SECOND = (2 * Math.PI) / PERIOD;	//derived rate of radians / second
	
	public SunSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/sun.png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}		
		}		
	}

	public Image getImage() {
		return image;
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
		
		elapsedTime += actual_delta_time;

		/*
		 * set size based on a sine wave with given period. note that java trig functions work with radians (360° = 2π)
		 */
		double sizeFactor = Math.sin(elapsedTime * 0.001 * RADIANS_PER_SECOND );
		
		this.width = 150 + (sizeFactor * 25);
		this.height = 150 + (sizeFactor * 25);

		
	}

}
