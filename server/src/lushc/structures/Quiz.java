package lushc.structures;

import java.util.List;


import lushc.exceptions.QuizException;
import lushc.questions.PersistentQuestions;

/**
 * A quiz models a collection of PersistentQuestions. Users can return the
 * current question, next question and previous question in the quiz. Users
 * can also view whether there are more questions to come or previous questions
 * inside the quiz before calling these methods.
 * 
 * This class is based on original group project work in the CS2010 module.
 * 
 * @author Jibril
 * @author garlicmj
 * @author Chris Lush
 */
public class Quiz {

	private String title;
	private int nextQuestionNumber;
	private int currentQuestionNumber;
	private List<PersistentQuestions> questions;

	public Quiz(String title, List<PersistentQuestions> questions) throws QuizException {

		if (title == null || title.trim().length() == 0) {

			throw new QuizException("Your quiz must have a title");
		}
		else {

			this.title = title;
		}

		if (questions == null || questions.size() == 0) {

			throw new QuizException("Your quiz must contain at least 1 question");
		}
		else {

			this.questions = questions;
		}

		nextQuestionNumber = 0;
		currentQuestionNumber = 0;
	}

	public String getTitle() {

		return title;
	}

	public int getCurrentQuestionNumber() {

		return currentQuestionNumber;
	}

	public List<PersistentQuestions> getQuestions() {

		return questions;
	}

	public PersistentQuestions getCurrentQuestion() {

		return questions.get(currentQuestionNumber);
	}

	public void updateCurrentQuestion(PersistentQuestions question) {

		questions.set(currentQuestionNumber, question);
	}

	public PersistentQuestions getNextQuestion() {

		PersistentQuestions fetched = null;

		if (nextQuestionNumber < questions.size()) {

			fetched = questions.get(nextQuestionNumber);

			// Position is shifted forward by 1
			currentQuestionNumber = nextQuestionNumber;
			nextQuestionNumber++;
		}

		return fetched;
	}

	public PersistentQuestions getPreviousQuestion() {

		PersistentQuestions fetched = null;

		if (currentQuestionNumber > 0) {

			// Position is shifted backwards by 1
			nextQuestionNumber = currentQuestionNumber;
			currentQuestionNumber--;

			fetched = questions.get(currentQuestionNumber);
		}

		return fetched;
	}

	public boolean hasMoreQuestions() {

		if (nextQuestionNumber < questions.size()) {

			return true;
		}
		else {

			return false;
		}
	}

	public boolean hasPreviousQuestions() {

		if (currentQuestionNumber != 0) {

			return true;
		}
		else {

			return false;
		}
	}

	public int getNumOfQuestions() {

		return questions.size();
	}

	@Override
	public String toString() {

		String str = title + "\n";

		for (int i = 0; i < questions.size(); i++) {

			PersistentQuestions q = questions.get(i);

			str += "\nQ" + (i + 1) + ": " + q.toString() + "\n";
		}

		return str;
	}
}
