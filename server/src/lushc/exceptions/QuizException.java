package lushc.exceptions;

/**
 * Models an exception that may be thrown when initialising a quiz.
 *
 * @author Chris Lush
 * @see lushc.structures.Quiz
 */
public class QuizException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Sole constructor that allows a custom error message to be
	 * specified.
	 *
	 * @param message	a detail message that enlightens the user
	 * 					as to what caused the error
	 */
	public QuizException(String message) {

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
