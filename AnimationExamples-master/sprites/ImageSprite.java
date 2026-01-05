import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSprite implements DisplayableSprite {

	//A sprite that only functions as a fixed image.

	//As there is likely only a single instance of this sprite for each specific image, the Image is stored in
	//an instance variable. Thus, other instances of this sprite can have different images
	private Image image;
	
	//For sprites such as these, it may make sense to store the geometry in four values representing the top-left
	//and bottom-right corners.
	private double minX = 0;
	private double minY = 0;
	private double maxX = 0;
	private double maxY = 0;

	private boolean dispose;
	
	public ImageSprite(double minX, double minY, double maxX, double maxY, String imagePath) {

		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		
    	try {
    		this.image = ImageIO.read(new File(imagePath));
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}	    		
	}

	public Image getImage() {
		return image;
	}
	
	//DISPLAYABLE
	
	public boolean getVisible() {
		return true;
	}
	
	//For sprites such as these, it may make sense to store the geometry in four values representing the top-left
	//and bottom-right corners.
	public double getMinX() {
		return this.minX;
	}

	public double getMaxX() {
		return this.maxX;
	}

	public double getMinY() {
		return this.minY;
	}

	public double getMaxY() {
		return this.maxY;
	}

	public double getHeight() {
		return maxY - minY;
	}

	public double getWidth() {
		return maxX - minX;
	}

	public double getCenterX() {
		return (maxX + minX) / 2;
	};

	public double getCenterY() {
		return (maxY + minY) / 2;
	};
	
	
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}


	public void update(Universe universe, long actual_delta_time) {		
		//do nothing				
	}

}
