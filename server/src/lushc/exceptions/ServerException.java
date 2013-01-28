package lushc.exceptions;

/**
 * Models a chained exception that may be thrown when starting the server.
 *
 * @author Chris Lush
 * @see lushc.server.Server
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Sole constructor that allows a custom error message to be
	 * specified.
	 *
	 * @param message	a detail message that enlightens the user
	 * 					as to what caused the error
	 */
	public ServerException(String message) {

		this.message = message;
	}

	/**
	 * Gets the exception's message. Used for displaying the error
	 * in a GUI.
	 *
	 * @return	the detail message
	 */
	public String toString() {

		return message;
	}
}
