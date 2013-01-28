package lushc.dialogs;

import java.io.IOException;
import com.blogspot.lwuit.InfiniteProgressIndicator;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;

/**
 * Concrete class to create an informative dialog that alerts the user
 * to various events during the client's lifetime
 * 
 * @author Chris Lush 
 */
public class ConnectionDialog extends Dialog implements Dialogs {

	private Label text;
	private String message;
	private boolean showIndicator;
	private InfiniteProgressIndicator indicator;

	public ConnectionDialog(String message, boolean showIndicator) {

		super("Please Wait");
		this.message = message;
		this.showIndicator = showIndicator;
		init();
	}

	public ConnectionDialog(String message, boolean showIndicator, String title) {

		super(title);
		this.message = message;
		this.showIndicator = showIndicator;
		init();
	}

	/**
	 * Changes the dialog's message
	 */
	public void updateMessage(String message) {

		// Reset the text position
		text.stopTicker();

		if (message.length() > 12) {

			text.startTicker(20, true);
		}

		text.setText(message);

	}

	private void init() {

		setLayout(new BorderLayout());

		if (showIndicator) {
			
			Container flow = new Container(new FlowLayout(Component.CENTER));

			try {

				indicator = new InfiniteProgressIndicator(Image.createImage("/wait-circle.png"));

				// Create the animation at the top of the dialog
				flow.addComponent(indicator);
				registerAnimated(indicator);
				indicator.setAlignment(Component.CENTER);
			} 
			catch (IOException e) {

				indicator = null;
			}

			addComponent(BorderLayout.NORTH, flow);
		}

		text = new Label(message);
		text.setAlignment(Component.CENTER);
		text.setTickerEnabled(true);

		addComponent(BorderLayout.CENTER, text);
	}
}