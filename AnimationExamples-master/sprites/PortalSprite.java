import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PortalSprite implements DisplayableSprite {

	private static Image image;
	private boolean visible = true;
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
		
	/*
	 * A slightly different way of passing dimensions of a sprite... this will allow the sprite to be
	 * sizable but always maintain the same height/width ratio
	 */
	public PortalSprite(double minX, double minY, double height, boolean visible) {
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/portal.png"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		this.height = height;
		this.width = height / 2;
		this.centerX = minX + (width / 2);
		this.centerY = minY + (height / 2);
		this.visible = visible;
		
	}
	

	public Image getImage() {
		return image;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	//DISPLAYABLE
	
	public boolean getVisible() {
		return this.visible;
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
	}

}
