package lushc.dialogs;

import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionListener;

public class ErrorDialog extends ConnectionDialog {
	
	public ErrorDialog(String message, ActionListener exitListener) {
		
		super(message, false, "Error");
		
		addCommand(new Command("Exit"));
		setCommandListener(exitListener);
	}
}