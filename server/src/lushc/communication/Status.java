package lushc.communication;

/**
 * Part of the BlueAnswer communication protocol; provides references
 * to static <code>int</code> values, via meaningfully named variables,
 * that describe the current status of communication between two parties
 * (namely, the client and server.)
 *
 * These values are often written to input and output streams,
 * where logic classes such as <code>ClientHandler</code> are
 * expected to <code>switch</code> upon their values to determine
 * what action(s) are required.
 *
 * @author Chris Lush
 */
public class Status {

	/**
	 * Data transfer between client and server.
	 */
	public static final int TRANSFER = 100;

	/**
	 * Confirms that data was received by the other party.
	 */
	public static final int OK = 200;

	/**
	 * Client has received data from server and is waiting on user input.
	 */
	public static final int WAIT = 202;

	// Reserved for possible use in future revisions
	public static final int PARTIAL_CONTENT = 206;
	public static final int ERROR = 500;

}
