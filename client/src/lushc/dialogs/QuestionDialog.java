package lushc.dialogs;

import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * Displays the text of a question in a scrollable
 * text area. This dialog merely disposes instead
 * of exiting the application
 * 
 * @author Chris Lush
 */
public class QuestionDialog extends Dialog {

	public QuestionDialog(String questionText) {
		
		super("Question");
		setScrollable(false);
		
		TextArea question = new TextArea(questionText);
		question.setEditable(false);
		question.setRows(4);
		setLayout(new BorderLayout());
		addComponent(BorderLayout.CENTER, question);
		
		addCommand(new Command("Hide"));
		setCommandListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				if (ae.getCommand().getCommandName().equals("Hide")) {
					
					dispose();
				}
			}	
		});
	}
}
