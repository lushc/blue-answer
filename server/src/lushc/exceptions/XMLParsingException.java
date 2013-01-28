package lushc.exceptions;

/**
 * Models an exception that may be thrown when parsing an XML file.
 *
 * @author Chris Lush
 * @see lushc.structures.Quiz
 */
public class XMLParsingException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Default constructor that models a basic detail message.
	 */
	public XMLParsingException() {

		this.message = "Unrecognized file type";
	}

	/**
	 * Constructor that allows a custom error message to be
	 * specified.
	 *
	 * @param message	a detail message that enlightens the user
	 * 					as to what caused the error
	 */
	public XMLParsingException(String message) {

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
