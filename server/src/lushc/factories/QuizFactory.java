package lushc.factories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;

import lushc.exceptions.QuizException;
import lushc.exceptions.XMLParsingException;
import lushc.questions.MultipleChoiceQuestion;
import lushc.questions.PersistentQuestions;
import lushc.structures.Quiz;
import lushc.xml.Parser;

public class QuizFactory {

	public static Quiz makeQuiz(String title, List<PersistentQuestions> questions) throws QuizException {

		return new Quiz(title, questions);
	}

	/**
	 * Parses a quiz XML file found at the file location, creating a quiz object
	 * 
	 * @param file
	 * @return a new quiz
	 * @throws XMLParsingException
	 * @throws QuizException
	 */
	public static Quiz makeQuiz(File file) throws XMLParsingException, QuizException {

		// Parse the file
		Document doc = Parser.readFromFile(file);
		Element root = doc.getRootElement();

		// Check that the parsed XML represents a quiz
		if (root.toXML().startsWith("<quiz>")) {

			String title = root.getChild(3).getValue();

			// Get a list of <question> nodes
			Elements questionsList = root.getFirstChildElement("questions")
											.getChildElements("question");

			List<PersistentQuestions> questions = new ArrayList<PersistentQuestions>(questionsList.size());

			// Iterate through each <question> node
			for (int i = 0; i < questionsList.size(); i++) {

				Node questionNode = null;
				String questionText = null;
				String[] answers = null;

				Element currentQuestion = questionsList.get(i);

				// Iterate through the child nodes found in the <question> element
				for (int j = 1; j < currentQuestion.getChildCount(); j += 2) {

					// This is the current child node to process
					questionNode = currentQuestion.getChild(j);

					if (questionNode.toXML().startsWith("<questText>")) {

						questionText = questionNode.getValue();
					}

					if (questionNode.toXML().startsWith("<answers>")) {

						Node answerNode = null;
						answers = new String[(questionNode.getChildCount() / 2) - 1];

						for (int m = 1; m < questionNode.getChildCount(); m += 2) {

							answerNode = questionNode.getChild(m);

							if (answerNode.toXML().startsWith("<answer>")) {


								int index = Integer.parseInt(answerNode.getChild(1).getValue());
								answers[index - 1] = answerNode.getChild(3).getValue();
							}
						}
					}

				}

				questions.add(new MultipleChoiceQuestion(i, questionText, answers));
			}

			return new Quiz(title, questions);
		}
		else {

			throw new XMLParsingException("The selected file isn't a quiz");
		}
	}

}
