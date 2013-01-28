package lushc.forms;

import lushc.communication.Type;
import lushc.dialogs.QuestionDialog;
import lushc.lists.AnswersList;
import lushc.logic.BlueAnswerMIDlet;
import lushc.questions.MultipleChoiceQuestion;
import lushc.questions.PersistentQuestions;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * 
 * 
 * @author Chris Lush
 */
public class QuestionForm extends Form implements ActionListener {

	private PersistentQuestions question;
	private int typeCode;
	private BlueAnswerMIDlet MIDlet;
	
	public QuestionForm(PersistentQuestions question, BlueAnswerMIDlet MIDlet) {
		
		super("Answers");
		this.question = question;
		this.MIDlet = MIDlet;
		
		setLayout(new BorderLayout());
		setScrollable(false);
		
		// Add soft buttons
		addCommand(new Command("Skip"));
		addCommand(new Command("View Q"));
		setCommandListener(this);
		
		if (question instanceof MultipleChoiceQuestion) {
			
			handleMCQ();
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getCommand().getCommandName().equals("Skip")) {
			
			// Send an index not applicable to the answer list
			listSelectionCallback(-1);
		}
		else if (ae.getCommand().getCommandName().equals("View Q")) {
			
			// User wishes to view the question again
			new QuestionDialog(question.getQuestionText()).showModeless();
		}
	}
	
	/**
	 * Sends the index of the selected item in the AnswersList
	 * 
	 * @param selectedIndex
	 */
	public void listSelectionCallback(int selectedIndex) {
		
		MIDlet.getClient().sendResponse(typeCode, selectedIndex);
	}
	
	/**
	 * Helper method to correctly display a multiple choice question
	 */
	private void handleMCQ() {
		
		typeCode = Type.MULTIPLE_CHOICE_QUESTION;
		MultipleChoiceQuestion q = (MultipleChoiceQuestion) question;
		addComponent(BorderLayout.CENTER, new AnswersList(q.getAnswers(), this));
	}

}
