package lushc.dialogs;

import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionListener;

public class DiscoveryDialog extends ConnectionDialog {
	
	public DiscoveryDialog(String message, ActionListener exitListener) {
		
		super(message, true);
		
		addCommand(new Command("Exit"));
		setCommandListener(exitListener);
	}
}