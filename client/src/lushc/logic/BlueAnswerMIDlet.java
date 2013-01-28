package lushc.logic;

import java.io.IOException;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import lushc.dialogs.DiscoveryDialog;
import lushc.dialogs.Dialogs;
import lushc.dialogs.ErrorDialog;
import lushc.dialogs.QuestionDialog;
import lushc.forms.MainScreen;
import lushc.forms.QuestionForm;
import lushc.logic.Client;
import lushc.questions.PersistentQuestions;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;

/**
 * Entry point for the mobile phone's software manager.
 * Runs off a system thread, so a new thread has to be
 * created for the logic, to free up the thread for 
 * displaying the user interface
 * 
 * @author Chris Lush
 */
public class BlueAnswerMIDlet extends MIDlet {

	private Client client;
	private Form form;
	private Dialogs dialog;

	public BlueAnswerMIDlet() {

		// Initiate LWUIT and set a theme
		Display.init(this);
		setTheme();
		
		dialog = new DiscoveryDialog("Initiating", exitListener());
		dialog.showModeless();
						
		client = new Client(this);
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {

		// Gracefully close all listeners and streams
		client.closeDown();
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {

		new Thread(client).start();
	}

	public Form getCurrentForm() {

		return form;
	}

	public Dialogs getCurrentDialog() {

		return dialog;
	}

	public Client getClient() {

		return client;
	}
	
	public void displayMainScreen() {
		
		dialog.dispose();
		form = new MainScreen(exitListener());
		form.show();
	}
	
	/**
	 * Shows a question dialog with the question's text
	 * 
	 * @param question
	 */
	public void displayQuestion(PersistentQuestions question) {
		
		form = new QuestionForm(question, this);
		new QuestionDialog(question.getQuestionText()).show();
		form.show();
	}
	
	/**
	 * Displays an error message, exit soft button will shut
	 * down the application
	 * 
	 * @param errorMessage
	 */
	public void displayErrorDialog(String errorMessage) {
		
		new ErrorDialog(errorMessage, exitListener()).showModeless();
	}
	
	/**
	 * Listener used by some dialogs and the main screen to gracefully exit the
	 * application
	 */
	public ActionListener exitListener() {

		return (new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				if (ae.getCommand().getCommandName().equals("Exit")) {

					try {
						
						destroyApp(true);
					} 
					catch (MIDletStateChangeException e) {

						e.printStackTrace();
					}

					notifyDestroyed();
				}
			}
		});
	}

	private void setTheme() {
		
		try {
			
			Resources res = Resources.open("/theme.res");
			UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}

}
