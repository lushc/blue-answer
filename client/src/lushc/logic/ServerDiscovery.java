package lushc.logic;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

import lushc.dialogs.Dialogs;

/**
 * Searches for all devices that are computers and 
 * stores them in a Vector. Iterates through each
 * device in the Vector until it either finds the
 * server's service or has ran out of devices to search
 * 
 * @author Chris Lush
 */
public class ServerDiscovery {

	private String connectionURL;
	private Dialogs dialog;
	private LocalDevice local;
	private DiscoveryAgent agent;
	private Object searchLock;
	private Vector devices;
	private Listener listener;
	
	public ServerDiscovery(BlueAnswerMIDlet MIDlet) {
		
		searchLock = new Object();
		devices = new Vector();
		listener = this.new Listener();
		dialog = MIDlet.getCurrentDialog();
	}
	
	public String findConnectionURL() throws BluetoothStateException {
		
		try {
			
			local = LocalDevice.getLocalDevice();
			agent = local.getDiscoveryAgent();
			
			// Start the discovery process
			new Thread(listener).start();
			
			// Stop the method from returning 
			synchronized (searchLock) {
				
				searchLock.wait();
			}
		} 
		catch (InterruptedException e) {
			
		}
		
		return connectionURL;
	}
	
	public void cancelSearch() {
		
		if (agent != null) {
			
			// Prepare the Vector for GC
			devices.removeAllElements();
			
			// Inform the Bluetooth radio to stop inquiring devices
			agent.cancelInquiry(listener);
			
			// Allow the findConnectionURL method to return
			synchronized (searchLock) {
				
				searchLock.notify();
			}
		}
	}
	
	/**
	 * Does the bulk of the work in discovering the server
	 * 
	 * @author Chris Lush
	 */
	private class Listener implements DiscoveryListener, Runnable {
		
		private UUID[] uuidSet;
		private int count;
		private Object discoveryLock;
		private Object serviceLock;
		
		public Listener() {
			
			uuidSet = new UUID[] { new UUID("4cd807069e4a47d8b3c240d2371783c1", false) };
			count = 0;
			discoveryLock = new Object();
			serviceLock = new Object();
		}
		
		/**
		 * Called when a device has been discovered by the Bluetooth radio
		 */
		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
	
			if (! devices.contains(btDevice) && cod.getMajorDeviceClass() == 0x0100) {
				
				devices.addElement(btDevice);
				dialog.updateMessage("Discovering (" + devices.size() + " found)");
			}
		}
	
		/**
		 * Called when the Bluetooth radio has finished its inquiry
		 */
		public void inquiryCompleted(int discType) {
	
			synchronized (discoveryLock) {
				
				discoveryLock.notify();
			}
		}
	
		/**
		 * Called when there are no more services to search for on the device
		 */
		public void serviceSearchCompleted(int transID, int respCode) {
			
			// Notify that current service search has ended
			synchronized (serviceLock) {
				
				serviceLock.notify();
			}
		}
	
		/**
		 * Called when services matching the provided UUIDs have been found
		 */
		public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
	
			if (servRecord != null && servRecord.length > 0) {

				// Only provide one UUID, so get the first index
				connectionURL = servRecord[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			}
		}
		
		public void run() {
			
			dialog.updateMessage("Discovering");
			
			try {
				
				agent.startInquiry(DiscoveryAgent.GIAC, this);
				
				// Halt program flow until the device inquiry is complete
				synchronized (discoveryLock) {
					
					discoveryLock.wait();
				}
				
				if (devices.size() > 0) {
					
					for (Enumeration en = devices.elements(); en.hasMoreElements(); ) {
						
						search(((RemoteDevice) en.nextElement()));

						if (connectionURL != null) {
							
							break;
						}
					}
				}
				
				// Allow the findConnectionURL method to return
				synchronized (searchLock) {
					
					searchLock.notify();
				}
			} 
			catch (BluetoothStateException e) {

				e.printStackTrace();
			}
			catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		private void search(RemoteDevice dev) throws BluetoothStateException, InterruptedException {
			
			String deviceIdentifier;
				
			try {
				
				deviceIdentifier = dev.getFriendlyName(false);
			} 
			catch (IOException e) {

				deviceIdentifier = dev.getBluetoothAddress();
			}

			dialog.updateMessage("(" + (count + 1) + "/" + devices.size() + ") " + "Searching " + deviceIdentifier);
			count++;
			
			// Allow only one service search to occur at a time
			synchronized (serviceLock) {
				
				agent.searchServices(null, uuidSet, dev, this);	
				serviceLock.wait();
			}
		}
	}

}
