public class CollidingSpriteAnimationFrame extends AnimationFrame{
	
	/*
	 * This class is an example of the use of inheritance. By extending the superclass and overriding the
	 * updateControls method, this class will render an Animation while adding its own custom message to the
	 * top label
	 */
	
	public CollidingSpriteAnimationFrame(Animation animation) {
		super(animation);				
	}

	protected void updateControls() {

		System.out.println("Override!");
		
		CollidingSprite player1 = ((CollidingSpriteUniverse)universe).getPlayer1();
		
		this.lblTop.setText(String.format("Time: %9.3f;  Score: %6d; Message: %s", total_elapsed_time / 1000.0, player1.getScore(), player1.getProximityMessage() ) );	
		if (universe != null) {
			this.lblBottom.setText(universe.toString());
		}

	}
	
}