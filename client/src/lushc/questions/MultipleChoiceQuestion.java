package lushc.questions;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lushc.communication.Type;

/**
 * Class that models a multiple choice question. Constructed
 * out of primitives for Java ME optimisation.
 * 
 * @author Chris Lush
 */
public class MultipleChoiceQuestion implements PersistentQuestions {

	private int ID;
	private String question;
	private String[] answers;

	public MultipleChoiceQuestion(int ID, String question, String[] answers) {

		this.ID = ID;
		this.question = question;
		this.answers = answers;
	}

	/**
	 * Null constructor, due to lack of reflection, required to create
	 * a prototype object which the resurrect method initializes
	 */
	public MultipleChoiceQuestion() {

	}

	public int getID() {

		return ID;
	}

	public String[] getAnswers() {

		return answers;
	}

	public String getQuestionText() {

		return question;
	}

	public String toString() {

		String str = question;

		for (int i = 0; i < answers.length; i++) {

			str += "\nA" + (i + 1) +": " + answers[i];
		}

		return str;
	}

	/**
	 * Converts this object into a stream of bytes
	 */
	public byte[] persist() throws IOException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);

		dout.writeByte(Type.MULTIPLE_CHOICE_QUESTION);

		/*
		 * Here we write each component of the object to the
		 * output stream. The order is unimportant outside the scope
		 * of this class, as it's only required to reference the
		 * sequence when implementing the companion resurrect method
		 */
		dout.writeInt(ID);
		dout.writeUTF(question);
		dout.writeInt(answers.length);

		for (int i = 0; i < answers.length; i++) {

			dout.writeUTF(answers[i]);
		}

		dout.flush();

		return bout.toByteArray();
	}

	/**
	 * Initialises this object with values read from a stream of bytes
	 */
	public void resurrect(DataInputStream din) throws IOException {

		ID = din.readInt();
		question = din.readUTF();

		int numOfAnswers = din.readInt();
		answers = new String[numOfAnswers];

		for (int i = 0; i < numOfAnswers; i++) {

			answers[i] = din.readUTF();
		}

	}
}
