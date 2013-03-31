package com.proj3.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.proj3.app.LibrarianApp;
import com.proj3.model.Book;

@SuppressWarnings("serial")
public class LibrarianUIGenerateItem extends JPanel implements ActionListener {

	private MainJFrame mainFrame;

	private static final String YEAR_STRING = "YEAR";
	private static final String NUMBER_STRING = "NUMBER OF ITEMS";
	private static final String ITEM_STRING = "MOST POPULAR ITEMS";

	private JTextField yearField, numberField;

	private JLabel yearFieldLabel, numberFieldLabel;

	private JTextArea bookListArea;
	private JScrollPane bookListPane;

	private JButton submitButton;

	public JPanel getThisPanel() {
		return this;
	}

	public void setBorderRed(JTextField field, Boolean b) {
		if (b)
			field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.pink));
		else
			field.setBorder(BorderFactory.createEtchedBorder());
	}

	public void displayItems(String str) {
		bookListArea.append(str+"\n");
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getYear() {
		return yearField.getText();
	}
	
	public String getNumberOfItems() {
		return numberField.getText();
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}

	private void createYearField() {

		yearField = new JTextField(4);		
		yearField.setName(YEAR_STRING);
		yearField.addFocusListener(new MyFocusListener());		
		yearField.addKeyListener(new MyTextFieldKeyListener());

		yearFieldLabel = new JLabel(YEAR_STRING + ":");
		yearFieldLabel.setLabelFor(yearField);        

	}

	private void createNumberField() {

		numberField = new JTextField(255);		
		numberField.setName(NUMBER_STRING);
		numberField.addFocusListener(new MyFocusListener());		
		numberField.addKeyListener(new MyTextFieldKeyListener());

		numberFieldLabel = new JLabel(NUMBER_STRING + ":");
		numberFieldLabel.setLabelFor(numberField);        

	}

	private void createBookListPane() {

		bookListArea = new JTextArea();
		bookListArea.setEditable(false);

		bookListPane = new JScrollPane(bookListArea);
		bookListPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(ITEM_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		bookListPane.setName(ITEM_STRING);	

	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public LibrarianUIGenerateItem(MainJFrame f) {	     

		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Generate Report for Most Popular Items"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createYearField();
		createNumberField();
		createBookListPane();
		createSubmit();

		GridBagConstraints gridc;

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(yearFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(yearField, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 1;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(numberFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 1;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(numberField, gridc);

		//Insert output panes
		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.gridy = 2;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(bookListPane, gridc);

		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 3;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 4;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(submitButton, gridc);

	}

	/**
	 * Invoked when the submit button is clicked.	 
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			// Re-run all listeners to make sure all format checking is valid.
			for (Component c: this.getComponents()) {
				for (FocusListener f: c.getFocusListeners()) {
					f.focusLost(new FocusEvent(c, FocusEvent.FOCUS_LOST));									
				}
			}

			//Run the SQL method on a separate thread
			Thread thread = new Thread(new Runnable(){

				public void run() {
					
					JProgressBar progressBar = new JProgressBar();
					
					/**
					 *  try-finally so that it is guaranteed the submit button is 
					 *  re-enabled and progress bar is deleted at the end.
					 */
					try {
						submitButton.setEnabled(false);
						//Indeterminate progress bar
						
						progressBar.setIndeterminate(true);
						GridBagConstraints gridc = new GridBagConstraints();
						gridc.anchor = GridBagConstraints.CENTER;
						gridc.gridx = 0;
						gridc.gridy = GridBagConstraints.PAGE_END;
						gridc.weightx = 1;
						gridc.gridwidth = GridBagConstraints.REMAINDER;
						gridc.fill = GridBagConstraints.HORIZONTAL;
						getThisPanel().add(progressBar, gridc);
						getThisPanel().validate();
						getThisPanel().repaint();

						//TODO INSERT METHOD HERE
						// USE displayItems(String str)
						// BELOW IS AN EXAMPLE
						
						LibrarianApp app = new LibrarianApp(mainFrame.getDB());
						Calendar cal = Calendar.getInstance();
						Calendar cal2 = Calendar.getInstance();
						int year = Integer.parseInt(getYear());
						cal.set(year, 12, 31);
						cal2.set(year-1, 12, 31);
						Book[] popularBooks = app.generatePopularBooksReport(cal.getTime(), cal2.getTime(), Integer.parseInt(getNumberOfItems()));
						for (int i = 0; i < popularBooks.length; i++){
							displayItems(popularBooks[i].getTitle());
						}
						
						displayOutput("Year: "+getYear());
						displayOutput("Number of Items: "+getNumberOfItems());

					} catch (Exception e) {
						mainFrame.displayErrorMessage(e.getMessage());
					} finally {
						submitButton.setEnabled(true);
						getThisPanel().remove(progressBar);
						getThisPanel().validate();
						getThisPanel().repaint();
					}

				}

			});

			thread.start();

		} catch (Exception ex) {
			throw new IllegalArgumentException("Please check the inputs again.");
		}
	}

	// ActionListener for enforcing not null constraint
	class MyFocusListener implements FocusListener {

		public void focusGained(FocusEvent e) {
			// Do Nothing
		}

		public void focusLost(FocusEvent e) {	
			if (((JTextField)e.getComponent()).getText()==null || ((JTextField)e.getComponent()).getText().isEmpty()) {
				setBorderRed(((JTextField)e.getComponent()), true);
				throw new NullPointerException(((JTextField)e.getComponent()).getName() + " can not be null.");
			}
			setBorderRed(((JTextField)e.getComponent()), false);
		}

	}
}
