package lushc.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lushc.questions.PersistentQuestions;
import lushc.questions.MultipleChoiceQuestion;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Uses the JCommon and JFreeChart libraries to draw a bar chart from a
 * data set.
 * 
 * @author Chris Lush
 */
public class BarChart extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private Paint[] colours = { Color.red, Color.blue, Color.green, Color.yellow, Color.magenta };
	private String[] series = { "Answer A", "Answer B", "Answer C", "Answer D" };	
	private PersistentQuestions question;
	private int numOfParticipants;
	private int numOfParticipantsServed;
	private Timer countdownTimer;
	private int timeRemaining = 60;
	private JLabel countdownLabel;
	private List<Integer> results;

	public BarChart(MainInterface GUI) {

		super();
		question = GUI.getCurrentlySentQuestion();
		countdownTimer = new Timer(1000, new CountdownTimerListener());
		numOfParticipants = GUI.getDeviceTableModel().getRowCount();

		setTitle("Q" + (question.getID() + 1) + " results");
		setPreferredSize(new Dimension(640, 480));
		addWindowListener(new WindowCloseListener());
		pack();
		setLocationRelativeTo(null);
		setState(Frame.ICONIFIED);

		int size = ((MultipleChoiceQuestion) question).getAnswers().length;
		
		results = Collections.synchronizedList(new ArrayList<Integer>(size));
		
		// Initalise the ArrayList with response counts of 0
		for (int i = 0; i < size; i++) {

			results.add(i, 0);
		}

	}

	public void run() {

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.insets = new Insets(-50, 0, 0, 0);

		countdownLabel = new JLabel();
		countdownLabel.setText(String.valueOf(timeRemaining));
		countdownLabel.setFont(new Font("Dialog", Font.BOLD, 342));
		JButton skipCountdown = new JButton();
		skipCountdown.setText("Show results now");
		skipCountdown.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				endCountdown();
			}
		});

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		jPanel.add(countdownLabel, gridBagConstraints);
		jPanel.add(skipCountdown, gridBagConstraints1);
		setContentPane(jPanel);

		// Display the window and start the countdown
		setVisible(true);
		countdownTimer.start();
	}

	public synchronized void incrementResponse(int index) {

		if (index > -1) {

			int i = results.get(index);
			i++;
			results.set(index, i);
		}

		numOfParticipantsServed++;

		if (numOfParticipantsServed == numOfParticipants) {

			// All participants have answered
			endCountdown();
		}
		if (timeRemaining == 0) {
			
			// Redraw the chart to update responses
			displayChart();
		}
	}

	private CategoryDataset createDataset() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		/*
		 * Each index in the results List represents a series,
		 * so simply go through each index, entering it into
		 * the data-set used by JFreeChart
		 */
		synchronized (results) {

			int totalNumOfResponses = 0;

			for (int i = 0; i < results.size(); i++) {

				int seriesResponseTotal = results.get(i);
				dataset.addValue(seriesResponseTotal, series[i], "");
				totalNumOfResponses += seriesResponseTotal;
			}

			dataset.addValue((numOfParticipants - totalNumOfResponses), "Skipped/Unanswered", "");
		}

		return dataset;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {

		JFreeChart chart = ChartFactory.createBarChart(
				question.getQuestionText(),	// Chart title
				"",               			// X axis label
				"Number of responses",		// Y axis label
				dataset,					// Data
				PlotOrientation.VERTICAL,	// Orientation
				true,						// Show legend
				false,
				false
		);

		chart.setBackgroundPaint(new Color(238, 238, 238));

		// Customise the plot colours
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);

		// Range axis should display integers only
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setUpperMargin(0.12);

		CategoryItemRenderer renderer = plot.getRenderer();
		for (int i = 0; i < (results.size() + 1); i++) {

			renderer.setSeriesPaint(i, colours[i]);
		}

		// Show % for each series
		DecimalFormat decimalFormat = new DecimalFormat("##,###");
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{3}", decimalFormat));
		renderer.setItemLabelsVisible(true);
		renderer.setItemLabelFont(new Font("Dialog", Font.BOLD, 36));
		renderer.setBaseItemLabelsVisible(true);
		chart.getCategoryPlot().setRenderer(renderer);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		return chart;

	}

	private void displayChart() {

		CategoryDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(640, 480));
		setContentPane(chartPanel);
		setVisible(true);
	}

	private void endCountdown() {

		if (getState() == Frame.ICONIFIED) {

			setState(Frame.NORMAL);
		}

		countdownTimer.stop();
		timeRemaining = 0;
		displayChart();
	}

	private void closeWindow() {

		dispose();
		countdownTimer.stop();
	}

	private class CountdownTimerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (--timeRemaining > 0) {

				if ((getState() == Frame.ICONIFIED) && (timeRemaining == 30)) {

					setState(Frame.NORMAL);
				}

				countdownLabel.setText(String.valueOf(timeRemaining));
			}
			else {

				endCountdown();
			}
		}
	}

	private class WindowCloseListener extends WindowAdapter {

		public void windowClosing(WindowEvent e) {

			closeWindow();
		}
	}

}