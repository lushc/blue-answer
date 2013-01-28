package lushc.server;

import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

import lushc.GUI.MainInterface;
import lushc.exceptions.ServerException;

public class Server {

	// Service UUID
	private final UUID uuid = new UUID("4cd807069e4a47d8b3c240d2371783c1", false);

	// Server connection URL
	private final String url = "btspp://localhost:" + uuid + ";name=server;authenticate=false;encrypt=false;";

	// The Graphical User Interface
	private MainInterface GUI;

	// This server
	private StreamConnectionNotifier server;

	// The connection listener
	private ConnectionListener listener;

	// Local Bluetooth manager
	private LocalDevice localDevice;

	// Thread pool to handle incoming requests
	private ExecutorService pool;


	public Server(MainInterface GUI) throws ServerException {

		try {

			this.GUI = GUI;

			// Make the server discoverable
	        localDevice = LocalDevice.getLocalDevice();
	        localDevice.setDiscoverable(DiscoveryAgent.GIAC);

			// Create connection notifier for the server & a service record
			server = (StreamConnectionNotifier) Connector.open(url);

			// Create the listener so we can accept incoming connections without blocking this thread
			listener = new ConnectionListener();

			// Create a thread pool to handle incoming requests
			pool = Executors.newCachedThreadPool();

			// Start waiting for connections
			new Thread(listener).start();
		}
		catch (BluetoothStateException e) {

			throw new ServerException("Please enable/install Bluetooth on this machine");
		}
		catch (IOException e) {

			throw new ServerException("Unable to open streams to allow clients to connect");
		}
	}

	private class ConnectionListener implements Runnable {

		private boolean listening = true;

		public void run() {

			while (listening) {

				try {

					// Spawn a new thread to handle the connection
					pool.execute(new ClientHandler(server.acceptAndOpen(), GUI));
				}
				catch (IOException e) {

					System.out.println("Error @ listener: " + e.toString());
				}
			}
		}
	}

}
