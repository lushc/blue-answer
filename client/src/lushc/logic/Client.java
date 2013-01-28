package lushc.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lushc.communication.*;
import lushc.dialogs.Dialogs;
import lushc.questions.MultipleChoiceQuestion;
import lushc.questions.PersistentQuestions;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * Attempts to retrieve a connection URL to connect to,
 * and once successfully connected, starts listening for
 * server communication
 * 
 * @author Chris Lush
 */
public class Client implements Runnable {

	private StreamConnection connection;
	private DataInputStream in;
	private DataOutputStream out;
	private Dialogs dialog;
	private boolean listening;
	private BlueAnswerMIDlet MIDlet;
	private ServerDiscovery discovery;
	private PersistentQuestions currentQuestion;

	public Client(BlueAnswerMIDlet MIDlet) {

		this.MIDlet = MIDlet;
		dialog = MIDlet.getCurrentDialog();
		discovery = new ServerDiscovery(MIDlet);
		listening = false;
	}

	public void run() {

		try {
			
			// Begin server discovery
			String URL = discovery.findConnectionURL();

			if (URL != null) {

				try {

					dialog.updateMessage("Connecting");

					connection = (StreamConnection) Connector.open(URL);
					in = connection.openDataInputStream();
					out = connection.openDataOutputStream();
					
					// Hide connecting dialog
					dialog.dispose();
					
					// Begin waiting for a question
					MIDlet.displayMainScreen();
					listen();
				}

				catch (IOException e) {

					MIDlet.displayErrorDialog(e.getMessage());
				}
				catch (SecurityException e) {
					
					dialog.updateMessage("You must allow the application to make a connection to the server");
				}
			}
			else {

				dialog.updateMessage("Server not found, please exit the application and try again");
			}
		}
		catch (BluetoothStateException e) {

			dialog.updateMessage("Please enable Bluetooth on this device");
		}
	}

	public void listen() {

		listening = true;
		int statusCode;

		while (listening) {

			try {

				// Blocks until an int is read
				statusCode = in.readInt();

				switch (statusCode) {

				case Status.TRANSFER:
					// Server is transferring some data
					
					byte typeCode = in.readByte();

					switch (typeCode) {

					case Type.MULTIPLE_CHOICE_QUESTION:
						
						// Create a new MCQ and resurrect it
						currentQuestion = new MultipleChoiceQuestion();
						currentQuestion.resurrect(in);
						
						// Inform the server that the question has been received
						out.writeInt(Status.WAIT);
						out.flush();
						
						// Display the question
						MIDlet.displayQuestion(currentQuestion);
						break;
					}
					
					break;
					
				case Status.OK:
					
					// Response has been received, wait for a new question
					currentQuestion = null;
					MIDlet.displayMainScreen();
					break;
				}
			} 
			catch (IOException e) {

				listening = false;
				MIDlet.displayErrorDialog("Disconnected");
			}
		}

	}
	
	/**
	 * Sends the selected index in the AnswerList 
	 * (i.e. the user's response) to the server
	 * 
	 * @param typeCode
	 * @param selectedIndex
	 */
	public void sendResponse(int typeCode, int selectedIndex) {
		
		try {
			
			switch (typeCode) {
			
			case Type.MULTIPLE_CHOICE_QUESTION:
				
				out.writeInt(Status.TRANSFER);
				out.writeByte(Type.MULTIPLE_CHOICE_QUESTION);
				out.writeInt(currentQuestion.getID());
				out.writeInt(selectedIndex);
				out.flush();
			}
		}
		catch (IOException e) {
			
			MIDlet.displayErrorDialog("Sending error");
		}
	}

	/**
	 * Attempt to close all listeners and streams that could
	 * prevent the application from exiting gracefully
	 */
	public void closeDown() {

		if (connection == null) {

			discovery.cancelSearch();
		}

		if (listening) {

			if (connection != null) {

				try {

					in.close();
					out.close();
					connection.close();
				} 
				catch (IOException e) {

					e.printStackTrace();
				}
			}	
		}
	}

}


