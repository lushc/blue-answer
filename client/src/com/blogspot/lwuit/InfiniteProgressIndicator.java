package com.blogspot.lwuit;

import com.sun.lwuit.Image;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.plaf.Style;

/**
 * A "washing machine" progress animation that rotates the given image as an
 * animation. The image for rotation must be square.
 * 
 * Adapted from http://lwuit.blogspot.com/2008/11/round-round-infinite-progress-and.html
 *
 * @author Shai Almog
 */
public class InfiniteProgressIndicator extends Label {
    
	private Image[] angles;
    private int angle;
    private long lastInvoke;
    
    public InfiniteProgressIndicator(Image image) {
        
    	Image fourtyFiveDeg = image.rotate(45);
        
    	angles = new Image[] {image, fourtyFiveDeg, image.rotate(90), fourtyFiveDeg.rotate(90),
            image.rotate(180), fourtyFiveDeg.rotate(180), image.rotate(270), fourtyFiveDeg.rotate(270)};
        
        getStyle().setBgTransparency(0);
    }
    
    protected Dimension calcPreferredSize() {
        
    	Style s = getStyle();
       
    	return new Dimension(angles[0].getWidth() + s.getPadding(LEFT) + s.getPadding(RIGHT), 
            angles[0].getHeight() + s.getPadding(TOP) + s.getPadding(BOTTOM));	
    }
    
    public void paint(Graphics g) {
        
    	Style s = getStyle();
    	
    	g.drawImage(angles[Math.abs(angle % angles.length)], getX() + s.getPadding(LEFT), getY() + s.getPadding(TOP));
    }
    
    public boolean animate() {
        
        long current = System.currentTimeMillis();
        
        if (current - lastInvoke > 50) {
            
        	lastInvoke = current;
            angle++;
            return true;
        }
        
        return false;
    }
}