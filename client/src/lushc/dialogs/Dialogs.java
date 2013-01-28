package lushc.dialogs;

/**
 * An interface that most dialog types use
 * to provide abstraction 
 * 
 * @author Chris Lush
 */
public interface Dialogs {
	
	// Stops the dialog from blocking the thread of execution
	public void showModeless();
	
	// Destroys the dialog
	public void dispose();
	
	// Changes the message that the dialog displays
	public void updateMessage(String message);
}
