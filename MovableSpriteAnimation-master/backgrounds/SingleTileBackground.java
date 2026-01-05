import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SingleTileBackground implements Background {
	
    private Image image;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public SingleTileBackground() {
    	try {
    		this.image = ImageIO.read(new File("res/backgrounds/black-screen-background.png"));
    		backgroundWidth = AnimationFrame.STANDARD_SCREEN_WIDTH;
    		backgroundHeight = AnimationFrame.STANDARD_SCREEN_HEIGHT;
    		
    	}
    	catch (IOException e) {
    		System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {

		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;
		
		if (row == 0 && col == 0) {
			newTile = new Tile(image, x, y, backgroundWidth, backgroundHeight, false);			
		}
		else {
			newTile = new Tile(null, x, y, backgroundWidth, backgroundHeight, false);			
		}

		return newTile;
	}
	
	public int getCol(double x) {

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


