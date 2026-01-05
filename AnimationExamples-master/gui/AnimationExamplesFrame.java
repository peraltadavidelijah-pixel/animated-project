import javax.swing.*;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;


public class AnimationExamplesFrame extends AnimationFrame{

	JComboBox cboUniverse;	

	public AnimationExamplesFrame(Animation animation) {

		super(animation);

		cboUniverse  = new JComboBox();
		cboUniverse.setBounds(800,20,192,32);
		cboUniverse.addItem("SimpleSprite");
		cboUniverse.addItem("CollidingSpritesUniverse");
		cboUniverse.addItem("JumpingSpriteUniverse");
		cboUniverse.addItem("AnimatedSpritesUniverse");
		cboUniverse.addItem("SateliteSpriteUniverse");
		cboUniverse.addItem("PatternedUniverse");
		cboUniverse.addItem("MappedUniverse");
		cboUniverse.addItem("MultipleBackgroundUniverse");
		cboUniverse.addItem("StarfieldUniverse");
		cboUniverse.setFocusable(false);
		getContentPane().add(cboUniverse);
		getContentPane().setComponentZOrder(cboUniverse, 0);
		cboUniverse.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cboUniverse_itemStateChanged(e);
			}
		});			

	}

	private void cboUniverse_itemStateChanged(ItemEvent e) {

		/*
		 * In Java Swing, you have to be very careful with events, as one
		 * user action can trigger multiple events. In this case, changing
		 * the value of a combobox with trigger both a DESELECTED and 
		 * SELECTED event
		 */
		if (e.getStateChange() == ItemEvent.SELECTED) {

			if (this.cboUniverse.getSelectedIndex() == 0) {
				this.animation.switchUniverse(SimpleSpriteUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 1) {
				this.animation.switchUniverse(CollidingSpritesUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 2) {
				this.animation.switchUniverse(JumpingSpriteUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 3) {
				this.animation.switchUniverse(AnimatedSpritesUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 4) {
				this.animation.switchUniverse(SateliteSpriteUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 5) {
				this.animation.switchUniverse(PatternedUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 6) {
				this.animation.switchUniverse(MappedUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 7) {
				this.animation.switchUniverse(MultipleBackgroundUniverse.class.toString());		
			}
			else if (cboUniverse.getSelectedIndex() == 8) {
				this.animation.switchUniverse(StarfieldUniverse.class.toString());		
			}
			else {			
			}

			this.universe = animation.getCurrentUniverse();
			this.sprites = universe.getSprites();
			this.backgrounds = universe.getBackgrounds();
			this.scale = universe.getScale();
		}
	}

	protected void paintAnimationPanel(Graphics g) {

		/*
		 * An example of how to draw other graphical elements (lines, text) on the screen. This is here done with
		 * the MouseInput but you can also easily do so using the location of a given sprite.
		 * 
		 * This is also an example of how you can extend the functionality of the AnimationFrame without modifying
		 * that class directly. As many projects in the Animation code base rely on the AnimationFrame, we may not want
		 * to add functionality specific to one project in what is intended to be a generic class. 
		 */
		if (MouseInput.rightButtonDown
				&& translateToScreenX(MouseInput.logicalX) > 0
				&& translateToScreenY(MouseInput.logicalY) > 0) {

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.GREEN);
			g.setFont(new Font(null, 0, 12));

			double angle = TrigonometryUtility.calculateStandardAngle(
					logicalCenterX,
					logicalCenterY,
					MouseInput.logicalX,
					MouseInput.logicalY);
			
			
			
			int screenMidpointX = translateToScreenX((logicalCenterX + MouseInput.logicalX)/2);
			int screenMidpointY = translateToScreenY((logicalCenterY + MouseInput.logicalY)/2);

		     /*
		      * Draw a line from the logical center to the current location of the mouse pointer. Note that both
		      * of these are accessible as logical coordinates, so they need to be translated to screen coordinates
		      */

			g2.setColor(Color.GREEN);
			g2.setStroke(new BasicStroke(2));
		    g2.drawLine(translateToScreenX(logicalCenterX),
		    		translateToScreenY(logicalCenterY),
		    		translateToScreenX(MouseInput.logicalX),
		    		translateToScreenY(MouseInput.logicalY));				
			
		    /*
		     * Add the angle as a text object, at the end point of a line segment that is perpendicular
		     * to the first line, extending from the midpoint, and with length 25. This simply ensures that
		     * the text is not over top of the first line, which would make it difficult to read.
		     * Observe that we can simply add 90° to the angle. The java cosine and sine functions can work
		     * with any angle (i.e negative angles or those > 360°), but remember to convert to radians first.
		     */
			int offsetX = (int) (25 * Math.cos(Math.toRadians(angle + 90)));
			int offsetY = (int) (25 * Math.sin(Math.toRadians(angle + 90)));
										
			g2.drawString(String.format("%3.2f°", angle),
					screenMidpointX + offsetX,
					screenMidpointY + offsetY);
			
		}
	}	

}