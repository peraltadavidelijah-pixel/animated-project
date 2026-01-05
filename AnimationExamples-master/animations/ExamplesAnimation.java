
public class ExamplesAnimation implements Animation {

	private int universeCount = 0;
	private Universe current = null;
	private boolean universeSwitched = false;
	private boolean animationComplete = false;

	private JumpingSpriteUniverse jumpingSpriteUniverse0 = null;
	private JumpingSpriteUniverse jumpingSpriteUniverse1 = null;
	
	public ExamplesAnimation() {
		switchUniverse(SimpleSpriteUniverse.class.toString());
	}
	
	@Override
	public Universe switchUniverse(Object event) {

		String type;
		try {
			type = event.toString();
		} catch (Exception e) {
			type = "";
		}

		// This particular method implementation takes a String object indicating the universe to switch to.
		// There are many other ways to do this (note that the parameter is an Object, so anything can be passed);
		// However, it makes sense here as the name of the classes may change, but the use of the .class.toString() method
		// ensures that this would not "break" the functionality if the name does change
		
		if (type.equals(SimpleSpriteUniverse.class.toString())) {
			this.current = new SimpleSpriteUniverse();
		}
		else if (type.equals(CollidingSpritesUniverse.class.toString())) {
			this.current = new CollidingSpritesUniverse();
		}
		else if (type.equals(JumpingSpriteUniverse.class.toString())) {
			// An animation instance can easily maintain multiple instances;
			// This may be very useful for a maze game or a game with multiple levels
			//
			// Note that by default only one universe (the current) is updated;
			// this implies that any non-current universe is 'frozen' and when returned
			// to, 're-animated'. That is, the state of the universe is
			// preserved while it is not being updated, and when it becomes
			// the current universe, it will be in the exact state it was left
			// If you want multiple universes to be simultaneously updated (but not rendered)
			// you could add code to the update method in this class.
			
			if (this.current == jumpingSpriteUniverse0) {
				// switch from 0 (which by design is always the initial instance) to 1
				if (jumpingSpriteUniverse1 == null) {
					// only instantiate the universe if not yet done
					jumpingSpriteUniverse1 = new JumpingSpriteUniverse(1);
				}
				jumpingSpriteUniverse1.transportPlayer((JumpingSprite) jumpingSpriteUniverse0.getPlayer1());
				this.current = jumpingSpriteUniverse1;
				
			} else {				
				// either instantiate 0 or switch from 1 back to 0
				if (jumpingSpriteUniverse0 == null) {
					jumpingSpriteUniverse0 = new JumpingSpriteUniverse(0);
				}
				if (jumpingSpriteUniverse1 != null) {
					jumpingSpriteUniverse0.transportPlayer((JumpingSprite) jumpingSpriteUniverse1.getPlayer1());
				}
				this.current = jumpingSpriteUniverse0;
			}
		}
		else if (type.equals(AnimatedSpritesUniverse.class.toString())) {
			this.current = new AnimatedSpritesUniverse();
		}
		else if (type.equals(SateliteSpriteUniverse.class.toString())) {
			this.current = new SateliteSpriteUniverse();
		}				
		else if (type.equals(PatternedUniverse.class.toString())) {
			this.current = new PatternedUniverse();
		}
		else if (type.equals(MappedUniverse.class.toString())) {
			this.current = new MappedUniverse();
		}
		else if (type.equals(MultipleBackgroundUniverse.class.toString())) {
			this.current = new MultipleBackgroundUniverse();
		}
		else if (type.equals(StarfieldUniverse.class.toString())) {
			this.current = new StarfieldUniverse();
		}
		else {
			this.current = new SimpleSpriteUniverse();	
		}
		
		universeSwitched = true;
		return this.current;

	}

	public Universe getCurrentUniverse() {
		return this.current;
	}

	@Override
	public boolean getUniverseSwitched() {
		return universeSwitched;
	}

	@Override
	public void acknowledgeUniverseSwitched() {
		this.universeSwitched = false;		
	}

	@Override
	public boolean isComplete() {
		return animationComplete;
	}

	@Override
	public void setComplete(boolean complete) {
		this.animationComplete = true;		
	}

	@Override
	public void update(AnimationFrame frame, long actual_delta_time) {
	}
	
}
