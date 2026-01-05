import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SingleTileBackground implements Background {

	/*
	 * This class provides a generic repeating background with a sinbgle image. The image used
	 * is a 'wallpaper', which means the right hand edge matches the left hand edge, and/or
	 * the top edge matches the bottom edge. In other words, a tesselation!
	 * 
	 * Normally, the image that is used is larger than the screen, so that the repetition is
	 * not obvious. However, the tile size is set based on the image, and a smaller tesselated image
	 * (e.g. of a stone floor) can work equally well.
	 */
	
    private Image starfield;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public SingleTileBackground(String imagePath) {
    	try {
    		this.starfield = ImageIO.read(new File(imagePath));
    		backgroundWidth = starfield.getWidth(null);
    		backgroundHeight = starfield.getHeight(null);
    		
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {
		/*
		 * col and row represent the index in the x and y dimension of an infinite 2D grid of tiles
		 * all using the same image
		 */
		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;
		
		newTile = new Tile(starfield, x, y, backgroundWidth, backgroundHeight, false);

		return newTile;
	}

	/*
	 * columns and row are indexed starting at 0, and increase to the right and down respectively. Negative 
	 * indexes can also exist, to the left and/or above of the origin.
	 */
	
	public int getCol(double x) {
		//which col is x sitting at?
		int col = 0;
		if (backgroundWidth != 0) {
			col = (int) (x / backgroundWidth);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}
	
	public int getRow(double y) {
		//which row is y sitting at?
		int row = 0;
		
		if (backgroundHeight != 0) {
			row = (int) (y / backgroundHeight);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}
	
	@Override
	public double getShiftX() {
		return 0;
	}

	@Override
	public double getShiftY() {
		return 0;
	}

	@Override
	public void setShiftX(double shiftX) {
		//ignore
	}

	@Override
	public void setShiftY(double shiftY) {
		//ignore
	}

	@Override
	public void update(Universe universe, long actual_delta_time) {
		//ignore
	}
	
}


