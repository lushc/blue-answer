package lushc.forms;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * This form is displayed when a user is waiting
 * for a question
 * 
 * @author Chris Lush
 */
public class MainScreen extends Form {

	public MainScreen(ActionListener listener) {
		
		super("Welcome");
		setLayout(new BorderLayout());
		
		Command exit = new Command("Exit");
		addCommand(exit);
		setCommandListener(listener);
		
		Label text = new Label("Waiting for a question...");
		text.setAlignment(Component.CENTER);
		text.setTickerEnabled(true);
		
		addComponent(BorderLayout.CENTER, text);
		
		text.startTicker(20, true);
	}
}
