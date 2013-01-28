package lushc.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import lushc.GUI.MainInterface;
import lushc.communication.*;

/**
 * A class that handles a client connection in a separate
 * thread
 * 
 * @author Chris Lush
 */
public class ClientHandler implements Runnable {

	private StreamConnection clientConnection;
	private DeviceTableModel deviceModel;
	private MainInterface GUI;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean listening = true;
	private String address;

	public ClientHandler(StreamConnection clientConnection, MainInterface GUI) {

		this.clientConnection = clientConnection;
		this.GUI = GUI;

		deviceModel = GUI.getDeviceTableModel();

		try {

			// Retrieve the device's bluetooth address
			address = RemoteDevice.getRemoteDevice(clientConnection).getBluetoothAddress();
		}
		catch (IOException e) {

			address = "Anonymous";
		}

		deviceModel.add(this);
	}

	public void run() {

		try {

			// Open streams
			in = clientConnection.openDataInputStream();
			out = clientConnection.openDataOutputStream();

			int statusCode;

			while (listening) {

				statusCode = in.readInt();

				switch (statusCode) {

				case Status.TRANSFER:
					// The client is transferring some data

					byte typeCode = in.readByte();

					switch (typeCode) {

					case Type.MULTIPLE_CHOICE_QUESTION:

						int questionID = in.readInt();

						// Make sure the response is for the current question
						if (questionID == GUI.getCurrentlySentQuestion().getID()) {

							int answer = in.readInt();

							if (answer > -1) {

								deviceModel.update(this, new Object[] { address, "Answered question", true });
							}
							else {

								deviceModel.update(this, new Object[] { address, "Skipped question", true });
							}

							// Register the users response
							GUI.getCurrentBarChart().incrementResponse(answer);
						}

						out.writeInt(Status.OK);
						out.flush();
						break;
					}

					break;

				case Status.WAIT:

					// The client is waiting on user input
					deviceModel.update(this, new Object[] { address, "Received question", false });
					break;
				}
			}
		}
		catch (IOException e) {

			listening = false;
			deviceModel.remove(this);
		}
		finally {

			closeStreams();
		}
	}

	public void write(byte[] bytes) {

		try {

			out.writeInt(Status.TRANSFER);
			out.write(bytes);
			out.flush();
		}
		catch (IOException e) {

			deviceModel.update(this, new Object[] { address, "Send/receive error", false });
		}
	}

	public String getAddress() {

		return address;
	}

	public void closeStreams() {

		if (clientConnection != null) {

			try {

				// Close all the streams
				in.close();
				out.close();
				clientConnection.close();
			}
			catch (IOException e) {

				/*
				 * Do nothing, if an error is thrown, the streams have
				 * already been terminated (by the client)
				 */
			}
		}
	}

}
