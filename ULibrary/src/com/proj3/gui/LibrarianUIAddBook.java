package com.proj3.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.proj3.app.LibrarianApp;
import com.proj3.database.Database;
import com.proj3.model.Book;

@SuppressWarnings("serial")
public class LibrarianUIAddBook extends JPanel implements ActionListener {

	private MainJFrame mainFrame;

	private static final String CALLNUMBER_STRING = "CALL NUMBER";
	private static final String ISBN_STRING = "ISBN";
	private static final String TITLE_STRING = "TITLE";
	private static final String MAINAUTHOR_STRING = "MAIN AUTHOR";
	private static final String PUBLISHER_STRING = "PUBLISHER";
	private static final String YEAR_STRING = "YEAR";

	private JTextField callNumberField, isbnField, titleField, mainAuthorField, 
	publisherField, yearField;

	private JLabel callNumberFieldLabel, isbnFieldLabel, titleFieldLabel, mainAuthorFieldLabel,
	publisherFieldLabel, yearFieldLabel;

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

	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getISBN() {
		return isbnField.getText();
	}
	
	public String getTitle() {
		return titleField.getText();
	}
	
	public String getMainAuthor() {
		return mainAuthorField.getText();
	}
	
	public String getPublisher() {
		return publisherField.getText();
	}
	
	public String getYear() {
		return yearField.getText();
	}
	
	public String getCallNumber() {
		return callNumberField.getText();
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createCallNumberField() {

		callNumberField = new JTextField(255);		
		callNumberField.setName(CALLNUMBER_STRING);
		callNumberField.addFocusListener(new MyFocusListener());		
		callNumberField.addKeyListener(new MyTextFieldKeyListener());

		callNumberFieldLabel = new JLabel(CALLNUMBER_STRING + ":");
		callNumberFieldLabel.setLabelFor(callNumberField);        

	}
	
	private void createIsbnField() {

		isbnField = new JTextField(13);		
		isbnField.setName(ISBN_STRING);
		isbnField.addFocusListener(new MyFocusListener());		
		isbnField.addKeyListener(new MyTextFieldKeyListener());

		isbnFieldLabel = new JLabel(ISBN_STRING + ":");
		isbnFieldLabel.setLabelFor(isbnField);        

	}
	
	private void createTitleField() {

		titleField = new JTextField(255);		
		titleField.setName(TITLE_STRING);
		titleField.addFocusListener(new MyFocusListener());		
		titleField.addKeyListener(new MyTextFieldKeyListener());

		titleFieldLabel = new JLabel(TITLE_STRING + ":");
		titleFieldLabel.setLabelFor(titleField);        

	}
	
	private void createmainAuthorField() {

		mainAuthorField = new JTextField(255);		
		mainAuthorField.setName(MAINAUTHOR_STRING);
		mainAuthorField.addFocusListener(new MyFocusListener());	
		mainAuthorField.addKeyListener(new MyTextFieldKeyListener());

		mainAuthorFieldLabel = new JLabel(MAINAUTHOR_STRING + ":");
		mainAuthorFieldLabel.setLabelFor(mainAuthorField);        

	}
	
	private void createPublisherField() {

		publisherField = new JTextField(255);		
		publisherField.setName(PUBLISHER_STRING);
		publisherField.addFocusListener(new MyFocusListener());
		publisherField.addKeyListener(new MyTextFieldKeyListener());

		publisherFieldLabel = new JLabel(PUBLISHER_STRING + ":");
		publisherFieldLabel.setLabelFor(publisherField);        

	}
	
	private void createYearField() {

		yearField = new JTextField(4);		
		yearField.setName(YEAR_STRING);
		yearField.addFocusListener(new MyFocusListener());
		yearField.addKeyListener(new MyTextFieldKeyListener());

		yearFieldLabel = new JLabel(YEAR_STRING + ":");
		yearFieldLabel.setLabelFor(yearField);        

	}

	private void insertToGrid(JLabel[] labels, JTextField[] textFields, 
			Container container) {

		GridBagConstraints gridc;		

		for (int i = 0; i < labels.length; i++) {
			gridc = new GridBagConstraints();
			gridc.anchor = GridBagConstraints.EAST;
			gridc.gridx = 0;
			gridc.gridy = i;
			gridc.weightx = 0;
			gridc.gridwidth = GridBagConstraints.RELATIVE;
			container.add(labels[i], gridc);

			gridc = new GridBagConstraints();
			gridc.anchor = GridBagConstraints.WEST;
			gridc.gridx = 1;
			gridc.gridy = i;
			gridc.weightx = 1;
			gridc.gridwidth = GridBagConstraints.REMAINDER;
			gridc.fill = GridBagConstraints.HORIZONTAL;
			container.add(textFields[i], gridc);
		}
	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public LibrarianUIAddBook(MainJFrame f) {	     

		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Add Book"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createCallNumberField();
		createIsbnField();
		createTitleField();
		createmainAuthorField();
		createPublisherField();
		createYearField();
		createSubmit();

		//Group labels and text fields
		JTextField[] textFields = {callNumberField, isbnField, titleField, mainAuthorField, 
				publisherField, yearField};
		JLabel[] labels = {callNumberFieldLabel, isbnFieldLabel, titleFieldLabel, mainAuthorFieldLabel,
				publisherFieldLabel, yearFieldLabel};

		//Insert to the panel orderly
		insertToGrid(labels, textFields, this);

		//Insert spacing between the fields and the submit button
		GridBagConstraints gridc = new GridBagConstraints();
		gridc.gridy = textFields.length;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridwidth = 2;
		gridc.gridy = textFields.length+1;
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
						
						String callNumber = getCallNumber();
						String isbn = getISBN();
						String title = getTitle();
						String mainAuthor = getMainAuthor();
						String publisher = getPublisher();
						String year = getYear();

						Book newBook = new Book(); // new book

						newBook.setCallNumber(callNumber);
						newBook.setIsbn(isbn);
						newBook.setTitle(title);
						newBook.setMainAuthor(mainAuthor);
						newBook.setPublisher(publisher);
						newBook.setYear(year);
						
						
						LibrarianApp app = new LibrarianApp(mainFrame.getDB());
						app.addNewBook(newBook);
						
						displayOutput("New Book Successfully Added");
						displayOutput("CALL NUMBER: " +getCallNumber());
						displayOutput("ISBN: "+getISBN());
						displayOutput("TITLE: "+getTitle());
						displayOutput("MAIN AUTHOR: "+getMainAuthor());	
						displayOutput("PUBLISHER: "+getPublisher());
						displayOutput("YEAR: "+getYear());
						Thread.sleep(3000);						
				

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
