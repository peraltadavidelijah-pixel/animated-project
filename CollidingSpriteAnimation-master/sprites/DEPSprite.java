import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DEPSprite implements DisplayableSprite, MovableSprite, CollidingSprite {
    private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;
	
	

	private final double VELOCITY = 200;
	private long score = 0; 
	private boolean atExit = false;
	private String proximityMessage = "";

	public DEPSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}

	
	public DEPSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/toby.png"));
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

	public boolean collidesWith(DisplayableSprite other) {
        return this.getMaxX() > other.getMinX() &&
               this.getMinX() < other.getMaxX() &&
               this.getMaxY() > other.getMinY() &&
               this.getMinY() < other.getMaxY();
    }


	public void update(Universe universe, long actual_delta_time) {
		
		double velocityX = 0;
		double velocityY = 0;
		
		KeyboardInput keyboard = KeyboardInput.getKeyboard();

		//LEFT	
		if (keyboard.keyDown(37)) {
			velocityX = -VELOCITY;
		}
		//UP
		if (keyboard.keyDown(38)) {
			velocityY = -VELOCITY;			
		}
		// RIGHT
		if (keyboard.keyDown(39)) {
			velocityX += VELOCITY;
		}
		// DOWN
		if (keyboard.keyDown(40)) {
			velocityY += VELOCITY;			
		}

		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;


		double oldX = centerX;
    	double oldY = centerY;

        this.centerX += deltaX;
        this.centerY += deltaY;
		
		for (DisplayableSprite sprite : universe.getSprites()) {
            if (sprite != this && sprite instanceof BarrierSprite) {
                if (collidesWith(sprite)) {
                    // Undo movement if collision
                    this.centerX = oldX;
                    this.centerY = oldY;
                    break;
                }
            }
        }
		    for (DisplayableSprite sprite : universe.getSprites()) {
        if (sprite != this && sprite instanceof CoinSprite) {
            CoinSprite coin = (CoinSprite) sprite;
            if (collidesWith(coin) && coin.getVisible()) {
                coin.setVisible(false);
                coin.setDispose(true);
				 score += 100; 
                
                }
            }
        }
		        // ---------------- EXIT DETECTION ----------------
        atExit = false;
        for (DisplayableSprite sprite : universe.getSprites()) {
            if (sprite != this && sprite instanceof PortalSprite) {
                if (this.getMinX() >= sprite.getMinX() &&
                    this.getMaxX() <= sprite.getMaxX() &&
                    this.getMinY() >= sprite.getMinY() &&
                    this.getMaxY() <= sprite.getMaxY()) {
                    atExit = true;
                    break;
                }
            }
        }

        // ---------------- PROXIMITY MESSAGE ----------------
        proximityMessage = "";
        for (DisplayableSprite sprite : universe.getSprites()) {
            if (sprite != this &&
                !(sprite instanceof CoinSprite) &&
                !(sprite instanceof BarrierSprite) &&
                !(sprite instanceof PortalSprite)) {

                double dx = sprite.getCenterX() - this.getCenterX();
                double dy = sprite.getCenterY() - this.getCenterY();
                double distance = Math.sqrt(dx*dx + dy*dy);

                if (distance <= 100) {
                    proximityMessage = "A sprite is nearby!";
                    break;
                }
            }
        }
    }







	@Override
	public void setDispose(boolean dispose) {
		this.dispose = true;
	}


    @Override
    public double getVelocityX() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getVelocityY() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void setCenterX(double centerX) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setCenterY(double centerY) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setVelocityX(double pixelsPerSecond) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setVelocityY(double pixelsPerSecond) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public boolean getIsAtExit() {
        // TODO Auto-generated method stub
        return atExit;
    }


    @Override
    public String getProximityMessage() {
        // TODO Auto-generated method stub
        return proximityMessage;
    }


    @Override
    public long getScore() {
        // TODO Auto-generated method stub
        return score;
    }

}
