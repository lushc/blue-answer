package com.blogspot.lwuit;

import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.List;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 * Renders a list item that is wider than the width of the device's screen,
 * so that it appears to be scrolling.
 * 
 * Adapted from http://lwuit.blogspot.com/2008/06/implementing-selected-item-ticker-in.html
 * 
 * @author Shai Almong
 */
public class TickerRenderer extends DefaultListCellRenderer {

	private int position;
	
	public TickerRenderer() {
		
		setShowNumbers(false);
	}
	
	public void resetPosition() {
		position = 0;
	}
	public void incrementPosition() {
		position += 5;
		
	}

	public void paint(Graphics g) {
		if(hasFocus()) {
			String txt = getText();
			Style s = getStyle();
			int width = s.getFont().stringWidth(txt);
			if(getWidth() >= width) {
				super.paint(g);
				return;
			}
			UIManager.getInstance().getLookAndFeel().setFG(g, this);
			int actualPosition = position % width ;
			g.translate(-actualPosition, 0);
			g.drawString(txt, getX() + s.getPadding(LEFT), getY());
			g.translate(width + 20, 0);
			g.drawString(txt, getX(), getY());
			g.translate(actualPosition - 20 - width , 0);
		} else {
			super.paint(g);
		}
	}

	public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {

		super.getListCellRendererComponent(list, value, index, isSelected);
		return this;
	}

}
