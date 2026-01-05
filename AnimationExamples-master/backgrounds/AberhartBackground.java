import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AberhartBackground implements Background {

	/*
	 * An example of how you can create a patterned background using some basic math.
	 */
	
    private Image aberhart;
    private Image blank;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public AberhartBackground() {
    	try {
    		this.aberhart = ImageIO.read(new File("res/backgrounds/aberhart-tiles/aberhart.png"));
    		this.blank = ImageIO.read(new File("res/backgrounds/aberhart-tiles/blank.png"));
    		backgroundWidth = aberhart.getWidth(null);
    		backgroundHeight = blank.getHeight(null);
    		
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {

		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;

		/*
		 * The code below creates a checkerboard pattern, but can be easily modified to create alternating
		 * rows or coluns, or some other fanciful pattern.
		 */
		if (((col + row) % 2) == 0 ) {
			newTile = new Tile(aberhart, x, y, backgroundWidth, backgroundHeight, false);
		} else {
			newTile = new Tile(blank, x, y, backgroundWidth, backgroundHeight, false);
		}
			
		
		
		return newTile;
	}
	
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


