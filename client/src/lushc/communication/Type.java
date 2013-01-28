package lushc.communication;

/**
 * Part of the BlueAnswer communication protocol; provides references
 * to static <code>byte</code> values, via meaningfully named variables,
 * that describe the type of data a receiving party (namely, a client or server)
 * can expect to receive during communications.
 *
 * These values are often written to input and output streams,
 * where logic classes such as <code>ClientHandler</code> are
 * expected to <code>switch</code> upon their values to determine
 * what action(s) are required.
 *
 * @author Chris Lush
 */
public class Type {

	/**
	 * A persisted object is of type <code>MultipleChoiceQuestion</code>
	 *
	 * @see lushc.questions.MultipleChoiceQuestion
	 */
	public static final byte MULTIPLE_CHOICE_QUESTION = 1;

	// Reserved for possible use in future revisions
	public static final byte NULL = 0;
	public static final byte RESPONSE = 2;

}
