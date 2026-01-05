import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;

/*
 * This class represents the 'graphical user interface' or 'presentation' layer or 'frame'. Its job is to continuously 
 * read input from the user (i.e. keyboard, mouse) and to render (draw) a universe or 'logical' layer. Also, it
 * continuously prompts the logical layer to update itself based on the number of milliseconds that have elapsed.
 * 
 * The presentation layer has access to data within the logical layer but generally only reads this data. Conversely, the logic
 * layer does not have access to the presentation layer. This is known as a tiered architecture. This makes it easier to modify
 * either without affecting the other
 */

public class AnimationFrame extends JFrame {

	protected final int FRAMES_PER_SECOND = 60;
	protected final long REFRESH_TIME = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS

	// An exception to the rule above... these variables are static so that they can be referenced by the logic layer, which may
	// want to place sprites relative to the screen boundaries
	protected static final int STANDARD_SCREEN_HEIGHT = 800;
	protected static final int STANDARD_SCREEN_WIDTH = 1200;

	protected static int screenHeight = STANDARD_SCREEN_HEIGHT;
	protected static int screenWidth = STANDARD_SCREEN_WIDTH;

	//These variables control where the screen is centered in relation to the logical center of universe.
	//Generally it makes sense to have these start at half screen width and height, so that the logical
	//center is rendered in the center of the screen. Changing them will 'pan' the screen.
	protected int screenOffsetX = screenWidth / 2;
	protected int screenOffsetY = screenHeight / 2;

	protected boolean showScreenGrid = false;
	protected boolean showLogicalGrid = false;
	protected boolean displayTiming = false;

	//scale at which to render the universe. When 1, each logical unit represents 1 pixel in both x and y dimension
	protected double scale = 1;
	//used to keep record of the scale if the frame is resized
	protected double previousScale = scale;
	//point in universe on which the screen will center
	protected double logicalCenterX = 0;		
	protected double logicalCenterY = 0;

	//basic controls on interface... these are protected so that subclasses can access
	protected JPanel panel = null;
	protected JButton btnPauseRun;
	protected JLabel lblTop;
	protected JLabel lblBottom;
	
	/* 
	 * These three variables provide information on the state of the animation and the frame.
	 * -stopApplication can be used to signal exactly this. This may be used to trigger other threads
	 * (e.g. the Main class) to close all other resources.
	 * -stopAnimation is intended to signal the end of an animation, but may still allow other elements
	 * to continue running (e.g. a high score frame, a play again frame, etc).
	 * -windowClosed indicates that the window has been closed by used action, which may trigger
	 * the end of application or end of animation
	 */

	protected boolean stopApplication = false;
	protected boolean stopAnimation = false;
	protected boolean windowClosed = false;

	protected long total_elapsed_time = 0;
	protected long lastRefreshTime = 0;
	protected long deltaTime = 0;
	protected boolean isPaused = false;

	protected KeyboardInput keyboard = KeyboardInput.getKeyboard();
	protected Universe universe = null;

	//local (and direct references to various objects in universe ... should reduce lag by avoiding dynamic lookup
	protected Animation animation = null;
	protected ArrayList<DisplayableSprite> sprites = null;
	protected ArrayList<Background> backgrounds = null;
	protected Background background = null;

	protected final Color DARK_GRAY = Color.DARK_GRAY;
	protected final Color LIGHT_GRAY = Color.LIGHT_GRAY;
 	protected final Color DARK_YELLOW = new Color(128, 128, 0);
 	protected final Color YELLOW = Color.YELLOW;
	
	/*
	 * Much of the following constructor uses a library called Swing to create various graphical controls. You do not need
	 * to modify this code to create an animation, but certainly many custom controls could be added.
	 */
	public AnimationFrame(Animation animation)
	{
		super("");
		this.setVisible(false);

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				frameResized();
			}
		});		
		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				thisContentPane_mousePressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				thisContentPane_mouseReleased(e);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				contentPane_mouseExited(e);
			}
		});

		this.animation = animation;
		this.setFocusable(true);
		this.setSize(screenWidth + 20, screenHeight + 36);
		this.setMinimumSize(new Dimension(screenWidth + 20 - 100, screenHeight + 36 - 100));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
			public void windowClosed(WindowEvent e) {
				this_windowClosed(e);
			}

		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				contentPane_mouseMoved(e);
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				contentPane_mouseMoved(e);
			}
		});



		Container cp = getContentPane();
		cp.setBackground(Color.BLACK);
		cp.setLayout(null);

		panel = new AnimationPanel();
		panel.setLayout(null);
		panel.setSize(screenWidth, screenHeight);
		getContentPane().add(panel, BorderLayout.CENTER);

		btnPauseRun = new JButton("||");
		btnPauseRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnPauseRun_mouseClicked(arg0);
			}
		});

		btnPauseRun.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPauseRun.setBounds(screenWidth - 64, 20, 48, 32);
		btnPauseRun.setFocusable(false);
		getContentPane().add(btnPauseRun);
		getContentPane().setComponentZOrder(btnPauseRun, 0);

		lblTop = new JLabel("Time: ");
		lblTop.setForeground(Color.WHITE);
		lblTop.setFont(new Font("Consolas", Font.BOLD, 20));
		lblTop.setBounds(16, 22, screenWidth - 16, 30);
		getContentPane().add(lblTop);
		getContentPane().setComponentZOrder(lblTop, 0);

		lblBottom = new JLabel("Status");
		lblBottom.setForeground(Color.WHITE);
		lblBottom.setFont(new Font("Consolas", Font.BOLD, 30));
		lblBottom.setBounds(16, screenHeight - 30 - 16, screenWidth - 16, 36);
		lblBottom.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblBottom);
		getContentPane().setComponentZOrder(lblBottom, 0);

		repositionComponents();
	}
	
	protected void repositionComponents() {
		
		/*
		 * If the window can be resized, you may want to have this method to also
		 * reposition your components relative to the new dimensions. This method is
		 * called initially by the constructor and also by the frameResized event handler
		 */
		
		// careful that the components have been instantiated...
		if (lblBottom != null) {
			btnPauseRun.setBounds(screenWidth - 64, 20, 48, 32);
			lblTop.setBounds(16, 22, screenWidth - 16, 30);
			lblBottom.setBounds(16, screenHeight - 30 - 16, screenWidth - 16, 36);
		}
			
		
	}

	/* 
	 * The entry point into an Animation. The presentation (gui) and the logical layers should run on separate
	 * threads. This allows the presentation layer to remain responsive to user input while the logical is updating
	 * its state. The universe (a.k.a. logical) thread is created below.
	 */	
	public void start()
	{
		System.out.println("start() start");
		Thread thread = new Thread("animation loop")
		{
			public void run()
			{
				System.out.println("run() start");
				animationLoop();
				System.out.println("run() complete");
			}
		};

		thread.start();
		animationInitialize();

		System.out.println("start() complete");

	}

	/*
	 * You can add code for displaying gui elements when the animation initializes using a JDialog or similar
	 * 
	 * Note that this method runs on a separate thread from the animation (i.e. the logic layer) and the
	 * animation will start to run in parallel.

	 * This may be useful is your animation takes some time to instantiate, and you want to display
	 * a 'splash' screen while it does so.
	 */
	protected void animationInitialize() {

	}

	/*
	 * You can add code for displaying gui elements when the animation starts using a JDialog or similar
	 * 
	 * Note that this method runs on the same thread as the animation (i.e. the logic layer) and the
	 * animation will therefore not start until this method is complete.
	 * 
	 * E.g. if you display a modal dialog here, the animation will not run until that dialogue is closed
	 */
	protected void animationStart() {		
	}

	/*
	 * You can add code for displaying gui elements when there is a switch in universe (e.g. a next level dialogue)
	 */


	protected void universeSwitched() {
	}

	/*
	 * You can add code for displaying gui elements when the animation is fully complete (e.g. display a high score screen)
	 */
	protected void animationEnd() {

	}

	private void setLocalObjectVariables() {
		/*
		 * the following code is added to improve performance...
		 * by creating local object variables to the current universe / backgrounds / sprites, there is less redirection
		 * 
		 */
		universe = animation.getCurrentUniverse();
		sprites = universe.getSprites();
		backgrounds = universe.getBackgrounds();
		this.scale = universe.getScale();
		this.previousScale = this.scale;
	}


	/*
	 * You can overload this method to add additional rendering logic 
	 */
	protected void paintAnimationPanel(Graphics g) {

	}

	/*
	 * The animationLoop runs on the logical thread, and is only active when the universe needs to be
	 * updated. There are actually two loops here. The outer loop cycles through all universes as provided
	 * by the animation. Whenever a universe is 'complete', the animation is asked for the next universe;
	 * if there is none, then the loop exits and this method terminates
	 * 
	 * The inner loop attempts to update the universe regularly, whenever enough milliseconds have
	 * elapsed to move to the next 'frame' (i.e. the refresh rate). Once the universe has updated itself,
	 * the code then moves to a rendering phase where the universe is rendered to the gui and the
	 * controls updated. These two steps may take several milliseconds, but hopefully no more than the refresh rate.
	 * When the refresh has finished, the loop (and thus the thread) goes to sleep until the next
	 * refresh time. 
	 */
	private void animationLoop() {

		System.out.println("animationLoop() start");

		lastRefreshTime = System.currentTimeMillis();		
		setLocalObjectVariables();

		animationStart();

		//it may be that the animation is stopped before it even started... in this case, do not ever make the frame visible
		if (stopAnimation == false) {
			this.setVisible(true);
		}

		/* 
		 * outer game loop, which will run until stop is signaled or the animation is complete
		 */
		while (stopAnimation == false && animation.isComplete() == false) {

			//before the next universe is animated, allow other actions to take place
			universeSwitched();
			// the gui needs to acknowledge that the universe switch was detected
			// so that the gui can reset the switched flag
			animation.acknowledgeUniverseSwitched();			
			setLocalObjectVariables();
			//(re)set the keyboard to current state
			keyboard.reset();
			keyboard.poll();

			// inner game loop which will animate the current universe until stop is signaled or until the universe is complete / switched
			while (stopAnimation == false && animation.isComplete() == false && universe.isComplete() == false && animation.getUniverseSwitched() == false) {

				if (displayTiming == true) System.out.println(String.format("animation loop: %10s @ %6d", "sleep", System.currentTimeMillis() % 1000000));

				//adapted from http://www.java-gaming.org/index.php?topic=24220.0
				long target_wake_time = System.currentTimeMillis() + REFRESH_TIME;
				//sleep until the next refresh time
				while (System.currentTimeMillis() < target_wake_time)
				{
					//allow other threads (i.e. the Swing thread) to do its work
					Thread.yield();

					try {
						Thread.sleep(1);
					}
					catch(Exception e) {    					
					} 

				}

				if (displayTiming == true) System.out.println(String.format("animation loop: %10s @ %6d  (+%4d ms)", "wake", System.currentTimeMillis() % 1000000, System.currentTimeMillis() - lastRefreshTime));

				//track time that has elapsed since the last update, and note the refresh time
				deltaTime = (isPaused ? 0 : System.currentTimeMillis() - lastRefreshTime);
				lastRefreshTime = System.currentTimeMillis();
				total_elapsed_time += deltaTime;

				//read input
				keyboard.poll();
				handleKeyboardInput();

				//update logical
				animation.update(this, deltaTime);
				universe.update(animation, deltaTime);

				if (displayTiming == true) System.out.println(String.format("animation loop: %10s @ %6d  (+%4d ms)", "logic", System.currentTimeMillis() % 1000000, System.currentTimeMillis() - lastRefreshTime));

				//update interface
				updateControls();

				//create local copies of values from the universe. this seems to improve performance substantially
				this.logicalCenterX = universe.getXCenter();
				this.logicalCenterY = universe.getYCenter();

				MouseInput.logicalX = translateToLogicalX(MouseInput.screenX);
				MouseInput.logicalY = translateToLogicalY(MouseInput.screenY);

				this.repaint();

			}

		}

		AudioPlayer.setStopAll(true);
		this.setVisible(false);
		animation.setComplete(true);
		

		//there seems to be an issue whereby if the frame (window) is closed by the user,
		//it caused the dispose() method to hang. thus, only call if the loop terminated
		//for other reasons
		if (windowClosed == false) {
			dispose();
		}

		System.out.println("animationLoop() complete");

	}

	protected void updateControls() {

		this.lblTop.setText(String.format("Time: %9.3f;  offsetX: %5d; offsetY: %5d;  scale: %3.3f", total_elapsed_time / 1000.0, screenOffsetX, screenOffsetY, scale));
		if (universe != null) {
			this.lblBottom.setText(universe.toString());
		}

	}

	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
			this.btnPauseRun.setText("||");
		}
		else {
			isPaused = true;
			this.btnPauseRun.setText(">");
		}
	}

	protected void handleKeyboardInput() {

		if (keyboard.keyDownOnce(KeyboardInput.KEY_SPACE)) {
			btnPauseRun_mouseClicked(null);
		}
		if (keyboard.keyDown(KeyboardInput.KEY_F1)) {
			scale *= 1.01;
			previousScale = scale;
			contentPane_mouseMoved(null);
		}
		if (keyboard.keyDown(KeyboardInput.KEY_F2)) {
			scale /= 1.01;
			previousScale = scale;
			contentPane_mouseMoved(null);
		}

		if (keyboard.keyDown(KeyboardInput.KEY_A)) {
			screenOffsetX += 5;
		}
		if (keyboard.keyDown(KeyboardInput.KEY_D)) {
			screenOffsetX -= 5;
		}
		if (keyboard.keyDown(KeyboardInput.KEY_W)) {
			screenOffsetY += 5;
		}
		if (keyboard.keyDown(KeyboardInput.KEY_S)) {
			screenOffsetY -= 5;
		}
		if (keyboard.keyDownOnce(KeyboardInput.KEY_P)) {
			this.showScreenGrid = !this.showScreenGrid;
		}
		if (keyboard.keyDownOnce(KeyboardInput.KEY_L)) {
			this.showLogicalGrid = !this.showLogicalGrid;
		}
		if (keyboard.keyDownOnce(KeyboardInput.KEY_T)) {
			this.displayTiming = !this.displayTiming;
		}
	}

	/*
	 * This method will run whenever the universe needs to be rendered. The animation loop calls it
	 * by invoking the repaint() method.
	 * 
	 * The work is reasonably simple. First, all backgrounds are rendered from "furthest" to "closest"
	 * Then, all sprites are rendered in order. Observe that the logical coordinates are continuously
	 * being translated to screen coordinates. Thus, how the universe is rendered is determined by
	 * the gui, but what is being rendered is determined by the universe. In other words, a sprite may
	 * be in a given logical location, but where it is rendered also depends on scale and camera placement
	 */
	class AnimationPanel extends JPanel {

		public void paintComponent(Graphics g)
		{	

			if (universe == null) {
				return;
			}

			if (backgrounds != null) {
				for (Background background: backgrounds) {
					paintBackground(g, background);
				}
			}

			if (sprites != null) {
				for (DisplayableSprite activeSprite : sprites) {
					DisplayableSprite sprite = activeSprite;
					if (sprite.getVisible()) {
						if (sprite.getImage() != null) {
							g.drawImage(sprite.getImage(), translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()), null);
						}
						else {
							g.setColor(Color.BLUE);
							g.fillRect(translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()));
						}
					}
				}				
			}

			drawScreenGrid(g);
			drawLogicalGrid(g);

			paintAnimationPanel(g);

			if (displayTiming == true) System.out.println(String.format("animation loop: %10s @ %6d  (+%4d ms)", "interface", System.currentTimeMillis() % 1000000, System.currentTimeMillis() - lastRefreshTime));

		}
		
		private void drawScreenGrid(Graphics g) {
			
			if (showScreenGrid) {
				Graphics2D g2 = (Graphics2D) g;				
				for (int x = 0; x <= screenWidth; x+=50) {
					if (x % 100 == 0) {
						g.setColor(Color.GRAY);						
					} else {
						g.setColor(Color.DARK_GRAY);						
					}			
					g.drawLine(x, 0, x, screenHeight);
					g2.drawString(String.format("%3d", x),
							x,
							15);
				}
				for (int y = 0; y <= screenHeight; y+= 50) {
					if (y % 100 == 0) {
						g.setColor(Color.GRAY);						
					} else {
						g.setColor(Color.DARK_GRAY);						
					}
					g.drawLine(0, y, screenWidth, y);
					g2.drawString(String.format("%3d", y),
							0,
							y);
				}
			}
			
		}
	
		
		private void drawLogicalGrid(Graphics g) {

			if (showLogicalGrid) {
			
				Graphics2D g2 = (Graphics2D) g;
				
				int minX = (int) (Math.floor(translateToLogicalX(0) / 50) * 50);
				int minY = (int) (Math.floor(translateToLogicalY(0) / 50) * 50);
				int maxX = (int) (Math.floor(translateToLogicalX(screenWidth) / 50) * 50);
				int maxY = (int) (Math.floor(translateToLogicalY(screenHeight) / 50) * 50);
				int midX = (minX + maxX) / 2;
				int midY = (minY + maxY) / 2;
	
				for (int x = minX; x <= maxX; x+=50) {
					if (x % 100 == 0) {
						g.setColor(YELLOW);						
					} else {
						g.setColor(DARK_YELLOW);						
					}					
					g.drawLine(translateToScreenX(x) , 0, translateToScreenX(x), screenHeight);
					g2.drawString(String.format("%5d", x),
							translateToScreenX(x),
							15);
				}
				for (int y = minY; y <= maxY; y+= 50) {
					if (y % 100 == 0) {
						g.setColor(YELLOW);						
					} else {
						g.setColor(DARK_YELLOW);						
					}
					g.drawLine(0, translateToScreenY(y), screenWidth, translateToScreenY(y));
					g2.drawString(String.format("%5d", y),
							0,
							translateToScreenY(y));
				}						
			}
		}

		/*
		 * The algorithm for rendering a background may appear complex, but you can think of it as
		 * 'tiling' the screen from top left to bottom right. Each time, the gui determines a screen coordinate
		 * that has not yet been covered. It then asks the background (which is part of the universe) for the tile
		 * that would cover the equivalent logical coordinate. This tile has height and width, which allows
		 * the gui to draw the tile and to then move to the screen coordinate at the same minY and to the right of this tile.
		 * Again, the background is asked for the tile that would cover this coordinate.
		 * When eventually this coordinate is off the right hand edge of the screen, then move to the left of the screen
		 * but below the previously drawn tile. Repeat until the entire panel is covered.
		 */
		private void paintBackground(Graphics g, Background background) {

			if ((g == null) || (background == null)) {
				return;
			}

			//what tile covers the top-left corner?
			double logicalLeft = (logicalCenterX  - (screenOffsetX / scale) - background.getShiftX());
			double logicalTop =  (logicalCenterY - (screenOffsetY / scale) - background.getShiftY()) ;

			int row = background.getRow((int)(logicalTop - background.getShiftY() ));
			int col = background.getCol((int)(logicalLeft - background.getShiftX()  ));
			Tile tile = background.getTile(col, row);

			boolean rowDrawn = false;
			boolean screenDrawn = false;
			while (screenDrawn == false) {
				while (rowDrawn == false) {
					tile = background.getTile(col, row);
					if (tile.getWidth() <= 0 || tile.getHeight() <= 0) {
						//no increase in width; will cause an infinite loop, so consider this screen to be done
						g.setColor(Color.GRAY);
						g.fillRect(0,0, screenWidth, screenHeight);					
						rowDrawn = true;
						screenDrawn = true;						
					}
					else {
						Tile nextTile = background.getTile(col+1, row+1);
						int width = translateToScreenX(nextTile.getMinX()) - translateToScreenX(tile.getMinX());
						int height = translateToScreenY(nextTile.getMinY()) - translateToScreenY(tile.getMinY());
						g.drawImage(tile.getImage(), translateToScreenX(tile.getMinX() + background.getShiftX()), translateToScreenY(tile.getMinY() + background.getShiftY()), width, height, null);
					}					
					//does the RHE of this tile extend past the RHE of the visible area?
					if (translateToScreenX(tile.getMinX() + background.getShiftX() + tile.getWidth()) > screenWidth || tile.isOutOfBounds()) {
						rowDrawn = true;
					}
					else {
						col++;
					}
				}
				//does the bottom edge of this tile extend past the bottom edge of the visible area?
				if (translateToScreenY(tile.getMinY() + background.getShiftY() + tile.getHeight()) > screenHeight || tile.isOutOfBounds()) {
					screenDrawn = true;
				}
				else {
					col = background.getCol(logicalLeft);
					row++;
					rowDrawn = false;
				}
			}
		}				
	}

	protected int translateToScreenX(double logicalX) {
		return screenOffsetX + scaleLogicalX(logicalX - logicalCenterX);
	}		
	protected int scaleLogicalX(double logicalX) {
		return (int) Math.round(scale * logicalX);
	}
	protected int translateToScreenY(double logicalY) {
		return screenOffsetY + scaleLogicalY(logicalY - logicalCenterY);
	}		
	protected int scaleLogicalY(double logicalY) {
		return (int) Math.round(scale * logicalY);
	}

	protected double translateToLogicalX(int screenX) {
		double offset = screenX - screenOffsetX;
		return (offset / scale) + (universe != null ? universe.getXCenter() : 0);
	}
	protected double translateToLogicalY(int screenY) {
		double offset = screenY - screenOffsetY ;
		return (offset / scale) + (universe != null ? universe.getYCenter() : 0);		
	}

	protected void contentPane_mouseMoved(MouseEvent e) {
		Point point = this.getContentPane().getMousePosition();
		if (point != null) {
			MouseInput.screenX = point.x;		
			MouseInput.screenY = point.y;
			MouseInput.logicalX = translateToLogicalX(MouseInput.screenX);
			MouseInput.logicalY = translateToLogicalY(MouseInput.screenY);
		}
		else {
			MouseInput.screenX = -1;		
			MouseInput.screenY = -1;
			MouseInput.logicalX = Double.NaN;
			MouseInput.logicalY = Double.NaN;
		}
	}

	protected void thisContentPane_mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			MouseInput.leftButtonDown = true;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			MouseInput.rightButtonDown = true;
		} else {
			//DO NOTHING
		}
	}
	protected void thisContentPane_mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			MouseInput.leftButtonDown = false;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			MouseInput.rightButtonDown = false;
		} else {
			//DO NOTHING
		}
	}

	protected void frameResized() {

		// Credit to CK for the original code
		if (this.panel == null) {
			return;
		}

		int newHeight = this.getBounds().height - 36;
		int newWidth = this.getBounds().width - 20;

		//TODO... if the screen is maximized, then scaled, then brought back to original size, the scale is lost

		double heightScale = (double)newHeight / STANDARD_SCREEN_HEIGHT;
		double widthScale = (double)newWidth / STANDARD_SCREEN_WIDTH;

		if (heightScale > widthScale) {
			this.scale = previousScale * heightScale;
		}
		else {
			this.scale = previousScale * widthScale;			
		}
		
		this.panel.setSize(this.getBounds().width - 20, this.getBounds().height - 36);

		this.screenHeight = newHeight;
		this.screenWidth = newWidth;
		screenOffsetX = screenWidth / 2;
		screenOffsetY = screenHeight / 2;
		
		repositionComponents();		

	}

	protected void this_windowClosing(WindowEvent e) {
		System.out.println("AnimationFrame.windowClosing()");
		stopAnimation = true;
		windowClosed = true;
	}

	protected void this_windowClosed(WindowEvent e) {
		System.out.println("AnimationFrame.windowClosed()");
		stopAnimation = true;
		windowClosed = true;
	}
	protected void contentPane_mouseExited(MouseEvent e) {
		contentPane_mouseMoved(e);
	}
	
	public boolean getStopApplication() {
		return stopApplication;
	}

	public boolean getStopAnimation() {
		return stopAnimation;
	}

	public boolean getWindowClosed() {
		return windowClosed;
	}
	

}