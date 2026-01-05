import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/*
 * This class is an example of how to implement gravity, friction, and jumping behaviour, in addition to bouncing.
 * Refer to the update() method for a detailed explanation.
 */
public class JumpingSprite implements DisplayableSprite {

	/*
	 * The following constants control the specific physics of this sprite's movement. All velocity values represent pixels per second 
	 * and all acceleration values represent pixes per second per second
	 */
	private final double ACCCELERATION_X = 300;
	private final double ACCCELERATION_Y = 600;
	private final double MAX_VELOCITY_X = 300;
	private final double DEACCELERATION_X = 300;
	private final double MINIMUM_X_VELOCITY = 1;
	private final double INITIAL_JUMP_VELOCITY = 600;
	
	//required for advanced collision detection
	private CollisionDetection collisionDetection;
	private VirtualSprite virtual = new VirtualSprite();

	private static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	
	public JumpingSprite(double centerX, double centerY) {

		super();
		this.centerX = centerX;
		this.centerY = centerY;		

		/*
		 * Instantiation of the CollisionDetection object. As this sprite uses the calculate2DBounce method, it needs to set certain
		 * characteristics
		 */
		collisionDetection = new CollisionDetection();
		// change behaviour of bounces, so that only 50% of energy is 'preserved' in horizontal bounce and 0% of energy is preserved in vertical bounce
		collisionDetection.setBounceFactorX(0.5);
		collisionDetection.setBounceFactorY(0);
		// create a list of types for which the CollisionDetection object will calculate bounces. 
		// Only those types added to the list will collide; any other type will not;
		// passing a null list implies that any other type will collide
		ArrayList<Class> collidingTypes = new ArrayList<Class>();
		collidingTypes.add(BouncingSprite.class);
		collidingTypes.add(BarrierSprite.class);		
		collisionDetection.setCollisionTargetTypes(collidingTypes);
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/simple-sprite.png"));
			}
			catch (IOException e) {
				System.err.println(e.toString());
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

		KeyboardInput keyboard = KeyboardInput.getKeyboard();		
		boolean onGround = isOnGround(universe);

		//while this sprite is not on the ground, keyboard controls are ignored
		if (onGround) {

			/*
			 * The jumping behaviour is a 1-time subtraction of a constant amount of velocity in the y dimension. However, the
			 * sprite is designed to only be able to jump when it is on the ground. Note the function which determines this state.
			 */
			if (keyboard.keyDown(KeyboardInput.KEY_UP_ARROW)) {
				this.velocityY -= INITIAL_JUMP_VELOCITY;
				onGround = false;
			}
			// RIGHT ARROW
			if (keyboard.keyDown(39)) {
				/*
				 * The horizontal acceleration behaviour is continuous addition of an acceleration based on time elapsed. The x velocity
				 * will thus increase if the right arrow key is pressed, until it reaches a maximum; vice versa for the left arrow key
				 */
				velocityX += actual_delta_time * 0.001 * ACCCELERATION_X;
				if (velocityX > MAX_VELOCITY_X) {
					velocityX = MAX_VELOCITY_X;
				}
			}
			// LEFT ARROW
			else if (keyboard.keyDown(37)) {
				velocityX -= actual_delta_time * 0.001 * ACCCELERATION_X;
				if (velocityX < - MAX_VELOCITY_X) {
					velocityX = - MAX_VELOCITY_X;
				}
			}
			else {
				/*
				 * The sliding behaviour is continuous application of a 'friction' factor to the x velocity when the sprite is not being
				 * accelerated to the left or right. The x velocity will thus decrease until it reaches a minimum, at which point it is set
				 * to zero
				 */
				if (Math.abs(this.velocityX) > MINIMUM_X_VELOCITY) {
					this.velocityX -= actual_delta_time * 0.001 *  DEACCELERATION_X * Math.signum(this.velocityX);
				}
				else {
					this.velocityX = 0;
				}
			}
		}
		
		/*
		 * After this sprite's velocity has been calculated, use the 2D bounce method to handle collision detection.
		 * Note the use of a list of sprite types that will cause collisions. See the constructor.
		 */		
		collisionDetection.calculate2DBounce(virtual, this, universe.getSprites(), velocityX, velocityY, actual_delta_time);
		this.centerX = virtual.getCenterX();
		this.centerY = virtual.getCenterY();
		this.velocityX = virtual.getVelocityX();
		this.velocityY = virtual.getVelocityY();

		/*
		 * Gravity is simulated by the application of a constant acceleration in the y dimension (remember that the y dimension increases
		 * towards the bottom, so gravity is a positive constant.
		 */		
		if (onGround == true) {
			this.velocityY = 0;
		} else {
			this.velocityY = this.velocityY + ACCCELERATION_Y * 0.001 * actual_delta_time;
		}

	}
	
	private boolean isOnGround(Universe universe) {
		boolean onGround = false;
		for (DisplayableSprite sprite: universe.getSprites()) {
			if (sprite instanceof BarrierSprite) {
				//does the bottom of this sprite touch the top of another sprite?
				boolean bottomColiding = Math.abs(this.getMaxY() - sprite.getMinY()) < 5;
				//is this sprite at least partially overlapping this other sprite in the x dimension? If not, then this sprite is simply
				//at the same vertical level, but not over top.
				boolean toRight = this.getMinX() > sprite.getMaxX();
				boolean toLeft = this.getMaxX() < sprite.getMinX();
				boolean withinRange = (toRight == false) && (toLeft == false);
				
				if (bottomColiding && withinRange) {
					onGround = true;
					break;
				}
			}
		}
		return onGround;
	}

	/* 
	 * The following getters and setters are provided to allow the setting of
	 * movement by another object. See the JumpingSpriteUniverse.transportPlayer()
	 * method for an example
	 */
	protected double getVelocityX() {
		return velocityX;
	}

	protected void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	protected double getVelocityY() {
		return velocityY;
	}

	protected void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	protected void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	protected void setCenterY(double centerY) {
		this.centerY = centerY;
	}
}
