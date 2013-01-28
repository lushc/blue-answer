package lushc.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Used by a JTable to interrogate data for display
 * 
 * @author Chris Lush
 */
public class DeviceTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	// Give the columns initial values
	private String[] headers = {"Address", "Status", "Ready?"};

	// List to simulate rows in a table
	private List<Object[]> rows;

	// List storing references to clients
	private List<ClientHandler> handlers;

	public DeviceTableModel() {

		rows = new ArrayList<Object[]>();

		// Make our List thread-safe
		handlers = Collections.synchronizedList(new ArrayList<ClientHandler>());
		
	}

	public int getColumnCount() {

		return headers.length;
	}

	public int getRowCount() {

		return rows.size();
	}

	public String getColumnName(int col) {

		return headers[col];
	}


	public Object getValueAt(int rowIndex, int columnIndex) {

		return rows.get(rowIndex)[columnIndex];
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int col) {

		if (getRowCount() >= 1) {

			Object value = getValueAt(0, col);
			return (value == null ? Object.class : value.getClass());
		}

		return null;
	}

	public void add(ClientHandler client) {

		synchronized (handlers) {

			handlers.add(client);
			int row = handlers.indexOf(client);
			Object[] values = { client.getAddress(), "Connected", true };
			rows.add(row, values);
			fireTableRowsInserted(row, (getRowCount() - 1));
		}
	}

	public void update(ClientHandler client, Object[] values) {

		synchronized (handlers) {

			int row = handlers.indexOf(client);
			rows.set(row, values);
			fireTableRowsUpdated(row, row);
		}
	}

	public void remove(ClientHandler client) {

		synchronized (handlers) {

			int row = handlers.indexOf(client);
			handlers.remove(row);
			rows.remove(row);
			fireTableDataChanged();
		}
	}
	
	public void sendPersistedQuestion(byte[] bytes) {

		synchronized (handlers) {

			for (ClientHandler client : handlers) {

				client.write(bytes);
			}
		}
	}



}
