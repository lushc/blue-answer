package lushc.lists;

import lushc.forms.QuestionForm;

import com.blogspot.lwuit.TickerRenderer;
import com.sun.lwuit.List;

/**
 * Models a list of answers, whose entries will scroll across the screen
 * depending on their width. Key events are captured for purposes such
 * as pausing scrolling text or sending a response.
 * 
 * Additional code to correctly use the TickerRender is found at
 * http://lwuit.blogspot.com/2008/06/implementing-selected-item-ticker-in.html
 * 
 * @author Chris Lush
 */
public class AnswersList extends List {

	private long tickTime;
	private int lastSelection;
	private boolean paused;
	private QuestionForm form;
	private TickerRenderer renderer;
	private String[] identifiers = { "A", "B", "C", "D" };
	
	public AnswersList(String[] answers, QuestionForm form) {
		
		tickTime = System.currentTimeMillis();
		lastSelection = -1;
		paused = false;
		this.form = form;
		renderer = new TickerRenderer();
		
		// Custom renderer so entries ticker across the screen
		setListCellRenderer(renderer);
		
		// Allows the user to cycle repeatedly through the list
		setFixedSelection(List.FIXED_NONE_CYCLIC);
		
		// Append a letter prefix 
		for (int i = 0; i < answers.length; i++) {

			addItem(identifiers[i] + ") " + answers[i]);
		}
	}
	
	public boolean animate() {
		
		boolean val = super.animate();
		
		if (hasFocus() && !paused) {
			
			long currentTime = System.currentTimeMillis();
			
			if(currentTime - tickTime > 300) {
				
				if (lastSelection == getSelectedIndex()) {
					
					renderer.incrementPosition();
				} 
				else {
					
					lastSelection = getSelectedIndex();
					renderer.resetPosition();
				}
				
				return true;
			}
		}
		
		return val;
	}
	
	public void keyPressed(int keyCode) {
		
		switch (keyCode) {
	
		// Accessibility option to pause the ticker
		case '*':
		case '#':
			paused = true;
			break;
			
		default:
			super.keyPressed(keyCode);
			break;
		}
	}
	
	public void keyReleased(int keyCode) {

		switch (keyCode) {
		
		// User has selected an entry to respond with
		case -5:
			form.listSelectionCallback(getSelectedIndex());
			break;
			
		case '*':
		case '#':
			paused = false;
			break;
			
		default:
			super.keyReleased(keyCode);
			break;
		}
	}
}
