package lushc.GUI;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.ComponentOrientation;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Font;
import java.awt.Color;

import lushc.exceptions.ServerException;
import lushc.factories.QuizFactory;
import lushc.questions.MultipleChoiceQuestion;
import lushc.questions.PersistentQuestions;
import lushc.server.DeviceTableModel;
import lushc.server.Server;
import lushc.structures.Quiz;

import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private Quiz quiz;
	private List<JTextField> answerFields = new ArrayList<JTextField>(4);
	private DocumentListener docListener = setDocumentListener();
	private ActionListener navListener = setNavigationListener();
	private BarChart barChart;
	private PersistentQuestions currentSentQuestion;
	private boolean currentQuestionModified;
	private JPanel jContentPane = null;
	private JMenuBar blueAnswerMenuBar = null;
	private JMenu helpMenu = null;
	private JMenuItem aboutMenuItem = null;
	private JPanel devicePanel = null;
	private JScrollPane deviceScrollPane = null;
	private JTable deviceTable = null;
	private DeviceTableModel deviceModel = null;
	private JPanel questionPanel = null;
	private JPanel editPanel = null;
	private JTextField titleTextField = null;
	private JLabel questionTitleLabel = null;
	private JPanel answersPanel = null;
	private JTextField answer1TextField = null;
	private JTextField answer3TextField = null;
	private JTextField answer4TextField = null;
	private JTextField answer2TextField = null;
	private JLabel answer1Label = null;
	private JLabel answer3Label = null;
	private JLabel answer2Label = null;
	private JLabel answer4Label = null;
	private JButton sendButton = null;
	private JPanel quizPanel = null;
	private JTextField quizNameTextField = null;
	private JPanel quizNavPanel = null;
	private JButton backNavButton = null;
	private JLabel questionCountLabel = null;
	private JButton forwardNavButton = null;
	private JMenu fileMenu = null;
	private JMenuItem openMenuItem = null;
	private JLabel numOfQuestionsLabel = null;
	private JPanel quizTitlePanel = null;
	
	/**
	 * This method initializes blueAnswerMenuBar
	 *
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getBlueAnswerMenuBar() {
		
		if (blueAnswerMenuBar == null) {
			
			blueAnswerMenuBar = new JMenuBar();
			blueAnswerMenuBar.setLayout(new BoxLayout(getBlueAnswerMenuBar(), BoxLayout.X_AXIS));
			blueAnswerMenuBar.add(getFileMenu());
			blueAnswerMenuBar.add(getHelpMenu());
		}
		
		return blueAnswerMenuBar;
	}

	/**
	 * This method initializes helpMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		
		if (helpMenu == null) {
			
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		
		return helpMenu;
	}

	/**
	 * This method initializes aboutMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		
		if (aboutMenuItem == null) {
			
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About BlueAnswer");
		}
		
		return aboutMenuItem;
	}

	/**
	 * This method initializes devicePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getDevicePanel() {
		
		if (devicePanel == null) {
			
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setVgap(0);
			borderLayout.setHgap(0);
			
			devicePanel = new JPanel();
			devicePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			devicePanel.setComponentOrientation(ComponentOrientation.UNKNOWN);
			devicePanel.setLayout(borderLayout);
			devicePanel.add(getDeviceScrollPane(), BorderLayout.WEST);
		}
		
		return devicePanel;
	}

	/**
	 * This method initializes deviceScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getDeviceScrollPane() {
		
		if (deviceScrollPane == null) {
			deviceScrollPane = new JScrollPane();
			deviceScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Device list", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			deviceScrollPane.setPreferredSize(new Dimension(400, 444));
			
			getDeviceTableModel();
		}
		
		return deviceScrollPane;
	}

	/**
	 * Initializes the table that displays devices
	 * 
	 * @return DeviceTableModel
	 */
	public DeviceTableModel getDeviceTableModel() {

		if (deviceModel == null) {

			deviceModel = new DeviceTableModel();

			if (deviceTable == null) {

				deviceTable = new JTable(deviceModel);
				getDeviceScrollPane().setViewportView(deviceTable);
				deviceTable.setEnabled(false);
			}
		}

		return deviceModel;
	}

	/**
	 * This method initializes questionPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getQuestionPanel() {
		
		if (questionPanel == null) {
			
			questionPanel = new JPanel();
			questionPanel.setLayout(new BorderLayout());
			questionPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
			questionPanel.add(getEditPanel(), BorderLayout.SOUTH);
			questionPanel.add(getQuizPanel(), BorderLayout.NORTH);
		}
		
		return questionPanel;
	}

	/**
	 * This method initializes editPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getEditPanel() {
		
		if (editPanel == null) {
			
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.anchor = GridBagConstraints.CENTER;
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridx = 1;
			
			questionTitleLabel = new JLabel();
			questionTitleLabel.setText("Title:");
			questionTitleLabel.setName("questionTitleLabel");
			
			editPanel = new JPanel();
			editPanel.setLayout(new GridBagLayout());
			editPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Current question", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			editPanel.add(getAnswersPanel(), gridBagConstraints10);
			editPanel.add(getSendButton(), gridBagConstraints11);
			editPanel.add(getQuizTitlePanel(), gridBagConstraints19);
		}
		
		return editPanel;
	}

	/**
	 * This method initializes titleTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTitleTextField() {
		
		if (titleTextField == null) {
			
			titleTextField = new JTextField();
			titleTextField.setPreferredSize(new Dimension(400, 20));
			titleTextField.getDocument().addDocumentListener(docListener);
		}
		
		return titleTextField;
	}

	/**
	 * This method initializes answersPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getAnswersPanel() {
		
		if (answersPanel == null) {
			
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.gridy = 1;
			answer4Label = new JLabel();
			answer4Label.setText("D:");
			
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.gridy = 0;
			answer2Label = new JLabel();
			answer2Label.setText("B:");
			
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			answer3Label = new JLabel();
			answer3Label.setText("C:");
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			answer1Label = new JLabel();
			answer1Label.setText("A:");
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			
			answersPanel = new JPanel();
			answersPanel.setLayout(new GridBagLayout());
			answersPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Answers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			answersPanel.setName("answersPanel");
			answersPanel.add(getAnswer1TextField(), gridBagConstraints1);
			answersPanel.add(getAnswer2TextField(), gridBagConstraints4);
			answersPanel.add(getAnswer3TextField(), gridBagConstraints2);
			answersPanel.add(getAnswer4TextField(), gridBagConstraints3);
			answersPanel.add(answer1Label, gridBagConstraints);
			answersPanel.add(answer2Label, gridBagConstraints6);
			answersPanel.add(answer3Label, gridBagConstraints5);
			answersPanel.add(answer4Label, gridBagConstraints7);
		}
		
		return answersPanel;
	}

	/**
	 * This method initializes answer1TextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getAnswer1TextField() {
		
		if (answer1TextField == null) {
			
			answer1TextField = new JTextField();
			answer1TextField.setPreferredSize(new Dimension(150, 20));
			answer1TextField.getDocument().addDocumentListener(docListener);
			answerFields.add(answer1TextField);
		}
		
		return answer1TextField;
	}

	/**
	 * This method initializes answer3TextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getAnswer3TextField() {
		
		if (answer3TextField == null) {
			
			answer3TextField = new JTextField();
			answer3TextField.setPreferredSize(new Dimension(150, 20));
			answer3TextField.getDocument().addDocumentListener(docListener);
			answerFields.add(answer3TextField);
		}
		
		return answer3TextField;
	}

	/**
	 * This method initializes answer4TextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getAnswer4TextField() {
		
		if (answer4TextField == null) {
			
			answer4TextField = new JTextField();
			answer4TextField.setPreferredSize(new Dimension(150, 20));
			answer4TextField.getDocument().addDocumentListener(docListener);
			answerFields.add(answer4TextField);
		}
		
		return answer4TextField;
	}

	/**
	 * This method initializes answer2TextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getAnswer2TextField() {
		
		if (answer2TextField == null) {
			
			answer2TextField = new JTextField();
			answer2TextField.setPreferredSize(new Dimension(150, 20));
			answer2TextField.getDocument().addDocumentListener(docListener);
			answerFields.add(answer2TextField);
		}
		
		return answer2TextField;
	}

	/**
	 * This method initializes sendButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSendButton() {
		
		if (sendButton == null) {
			
			sendButton = new JButton();
			sendButton.setPreferredSize(new Dimension(200, 40));
			sendButton.setEnabled(false);
			sendButton.setToolTipText("To send a question you need a title plus answers A & B filled in");
			sendButton.setText("Send question");
			sendButton.addActionListener(setSendButtonListener());
		}
		
		return sendButton;
	}

	/**
	 * This method initializes quizPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getQuizPanel() {
		
		if (quizPanel == null) {
			
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 5;
			gridBagConstraints16.anchor = GridBagConstraints.CENTER;
			gridBagConstraints16.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints16.gridy = 2;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints14.gridx = 5;
			
			quizPanel = new JPanel();
			quizPanel.setLayout(new GridBagLayout());
			quizPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Quiz management", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			quizPanel.setEnabled(true);
			quizPanel.setVisible(true);
			quizPanel.add(getQuizNameTextField(), gridBagConstraints14);
			quizPanel.add(getQuizNavPanel(), gridBagConstraints16);
		}
		
		return quizPanel;
	}

	/**
	 * This method initializes quizNameTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getQuizNameTextField() {
		
		if (quizNameTextField == null) {
			
			quizNameTextField = new JTextField();
			quizNameTextField.setEditable(false);
			quizNameTextField.setText("No XML quiz file loaded!");
			quizNameTextField.setToolTipText("You can load a quiz by going to " + fileMenu.getText() + " -> " + openMenuItem.getText());
		}
		
		return quizNameTextField;
	}

	/**
	 * This method initializes quizNavPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getQuizNavPanel() {
		
		if (quizNavPanel == null) {
			
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.insets = new Insets(8, 3, 8, 5);
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.gridx = 3;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.insets = new Insets(5, 0, 5, 2);
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.gridx = 2;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.insets = new Insets(5, 3, 5, 0);
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.insets = new Insets(8, 5, 8, 2);
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.gridx = 0;
			
			numOfQuestionsLabel = new JLabel();
			numOfQuestionsLabel.setText("/1");
			numOfQuestionsLabel.setFont(new Font("Dialog", Font.BOLD, 24));
			questionCountLabel = new JLabel();
			questionCountLabel.setText("Q1");
			questionCountLabel.setFont(new Font("Dialog", Font.BOLD, 24));
			quizNavPanel = new JPanel();
			quizNavPanel.setLayout(new GridBagLayout());
			quizNavPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Navigation", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			quizNavPanel.setVisible(false);
			quizNavPanel.add(getBackNavButton(), gridBagConstraints13);
			quizNavPanel.add(questionCountLabel, gridBagConstraints15);
			quizNavPanel.add(numOfQuestionsLabel, gridBagConstraints17);
			quizNavPanel.add(getForwardNavButton(), gridBagConstraints18);
		}
		
		return quizNavPanel;
	}

	/**
	 * This method initializes backNavButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBackNavButton() {
		
		if (backNavButton == null) {
			
			backNavButton = new JButton();
			backNavButton.setText("<");
			backNavButton.setPreferredSize(new Dimension(41, 26));
			backNavButton.setEnabled(false);
			backNavButton.addActionListener(navListener);
		}
		
		return backNavButton;
	}

	/**
	 * This method initializes forwardNavButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getForwardNavButton() {
		
		if (forwardNavButton == null) {
			
			forwardNavButton = new JButton();
			forwardNavButton.setText(">");
			forwardNavButton.setEnabled(false);
			forwardNavButton.addActionListener(navListener);
		}
		
		return forwardNavButton;
	}

	/**
	 * This method initializes fileMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		
		if (fileMenu == null) {
			
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getOpenMenuItem());
		}
		
		return fileMenu;
	}

	/**
	 * This method initializes openMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOpenMenuItem() {
		
		if (openMenuItem == null) {
			
			openMenuItem = new JMenuItem();
			openMenuItem.setText("Open quiz...");

			openMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					loadFile();
				}
			});
		}
		
		return openMenuItem;
	}

	/**
	 * This method initializes quizTitlePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getQuizTitlePanel() {
		
		if (quizTitlePanel == null) {
			
			quizTitlePanel = new JPanel();
			quizTitlePanel.setLayout(new FlowLayout());
			quizTitlePanel.add(questionTitleLabel, null);
			quizTitlePanel.add(getTitleTextField(), null);
		}
		
		return quizTitlePanel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				MainInterface thisClass = new MainInterface();
				thisClass.pack();
				thisClass.setResizable(false);
				thisClass.setLocationRelativeTo(null);
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);

				try {

					new Server(thisClass);
				}
				catch (ServerException e) {

					JOptionPane.showMessageDialog(thisClass, e.toString(), "Runtime error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public MainInterface() {
		
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		
		this.setPreferredSize(new Dimension(900, 400));
		this.setJMenuBar(getBlueAnswerMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("BlueAnswer");
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		
		if (jContentPane == null) {
			
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(4);
			borderLayout1.setVgap(0);
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout1);
			jContentPane.add(getDevicePanel(), BorderLayout.WEST);
			jContentPane.add(getQuestionPanel(), BorderLayout.CENTER);

		}
		
		return jContentPane;
	}

	/**
	 * Open a file dialog box, parsing the chosen XML file and loading
	 * the quiz into the system
	 */
	private void loadFile() {

		FileDialog fileDialog = new FileDialog(this, "Select your quiz file", FileDialog.LOAD);

		fileDialog.setVisible(true);

		String filename = fileDialog.getFile();

		if (filename != null) {

			try {

				quiz = QuizFactory.makeQuiz(new File(fileDialog.getDirectory() + filename));
				quizNameTextField.setText(quiz.getTitle());
				quizNameTextField.setToolTipText("");
				numOfQuestionsLabel.setText("/" + quiz.getNumOfQuestions());
				quizNavPanel.setVisible(true);

				displayQuestion(quiz.getNextQuestion());
			}
			catch (Exception e) {

				JOptionPane.showMessageDialog(this, e.toString(), "Invalid state", JOptionPane.WARNING_MESSAGE);
			}
		}

		fileDialog.dispose();
	}

	/**
	 * Update the question form to the latest question
	 * @param question
	 */
	private void displayQuestion(PersistentQuestions question) {

		titleTextField.setText(question.getQuestionText());
		titleTextField.setCaretPosition(0);

		String[] answers = ((MultipleChoiceQuestion) question).getAnswers();

		for (int i = 0; i < answers.length; i++) {

			JTextField tmp = answerFields.get(i);
			tmp.setText(answers[i]);
			tmp.setCaretPosition(0);
		}

		currentQuestionModified = false;

		backNavButton.setEnabled(quiz.hasPreviousQuestions());
		forwardNavButton.setEnabled(quiz.hasMoreQuestions());
		questionCountLabel.setText("Q" + (quiz.getCurrentQuestionNumber() + 1));
	}

	/**
	 * Listen for changes to the question form
	 * @return
	 */
	private DocumentListener setDocumentListener() {

		return new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {

				checkFieldValues();
			}

			public void insertUpdate(DocumentEvent e) {

				checkFieldValues();
			}

			public void removeUpdate(DocumentEvent e) {

				checkFieldValues();
			}

			private void checkFieldValues() {

				if (titleTextField.getText().trim().length() > 0
						&& answer1TextField.getText().trim().length() > 0
						&& answer2TextField.getText().trim().length() > 0) {

					// User has filled in the title and two answers
					sendButton.setEnabled(true);
					sendButton.setToolTipText("");
					currentQuestionModified = true;

				}
				else {

					// User has not filled out all required fields
					sendButton.setEnabled(false);
				}
			}
		};
	}

	/**
	 * Load next or previous questions based on the button pressed
	 * @return
	 */
	private ActionListener setNavigationListener() {

		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Object source = e.getSource();

				if (source == backNavButton) {

					displayQuestion(quiz.getPreviousQuestion());
				}
				else if (source == forwardNavButton) {

					displayQuestion(quiz.getNextQuestion());
				}
			}
		};
	}

	/**
	 * Send the current question to all connected devices
	 * @return
	 */
	private ActionListener setSendButtonListener() {

		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// Check if there are actually any users to send the question to
				if (deviceModel.getRowCount() > 0) {

					currentSentQuestion = null;

					try {

						if (currentQuestionModified) {

							// Create a new question from scratch
							List<String> answers = new ArrayList<String>(4);

							for (JTextField answer : answerFields) {

								String tmp = answer.getText().trim();

								if (tmp.length() > 0) {

									answers.add(tmp);
								}
							}

							int questionID;

							if (quiz != null) {

								questionID = quiz.getCurrentQuestionNumber();
							}
							else {

								// No quiz is loaded, question will always be Q1
								questionID = 0;
							}

							currentSentQuestion = new MultipleChoiceQuestion(questionID, getTitleTextField().getText().trim(), answers.toArray(new String[answers.size()]));
						}
						else {

							// Question has not been modified
							currentSentQuestion = quiz.getCurrentQuestion();
						}

						// Send the question to all clients and start collecting results
						deviceModel.sendPersistedQuestion(currentSentQuestion.persist());
						displayBarChart();

					}
					catch (IOException e1) {

						JOptionPane.showMessageDialog(null, "There was an error in preparing the question for sending", "Invalid state", JOptionPane.WARNING_MESSAGE);
					}

				}
				else {

					JOptionPane.showMessageDialog(null, "There has to be at least one connected device for the question to be sent", "Did you know?", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
	}

	/**
	 * Opens a new results window in a new thread,
	 * allowing the lecturer to keep earlier results
	 * open
	 */
	private void displayBarChart() {

		barChart = new BarChart(this);
		new Thread(barChart).start();
	}

	public BarChart getCurrentBarChart() {

		return barChart;
	}

	/**
	 * Used for consistency checking when a user sends a response
	 * @return
	 */
	public PersistentQuestions getCurrentlySentQuestion() {

		return currentSentQuestion;
	}

}
