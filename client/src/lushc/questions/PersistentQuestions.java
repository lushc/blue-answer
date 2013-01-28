package lushc.questions;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * An interface for building persistent Question objects, using methodology described
 * at http://java.sun.com/developer/J2METechTips/2002/tt0226.html#tip2
 *
 * CLDC doesn't support serialization, so we have to implement our own way
 * of conveniently sending objects over as bytes
 *
 * @author Chris Lush
 */
public interface PersistentQuestions {

	public int getID();

	public String getQuestionText();

	public String toString();

	byte[] persist() throws IOException;

	void resurrect(DataInputStream din) throws IOException;
}
