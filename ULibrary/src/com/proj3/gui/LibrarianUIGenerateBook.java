package com.proj3.gui;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.proj3.app.LibrarianApp;
import com.proj3.model.Borrowing;

@SuppressWarnings("serial")
public class LibrarianUIGenerateBook extends JPanel implements ActionListener{

	private MainJFrame mainFrame;

	private static final String SUBJECT_STRING = "SUBJECT";
	private static final String BOOKLIST_STRING = "BOOKS";
	private static final String OUTDATE_STRING = "OUT DATE";
	private static final String DUEDATE_STRING = "DUE DATE";
	private static final String OVERDUE_STRING = "OVERDUE";

	private JTextField subjectField;

	private JLabel subjectFieldLabel;
	
	private JTextArea bookListArea, outDateArea, dueDateArea, overDueArea;
	private JScrollPane bookListPane, outDatePane, dueDatePane, overDuePane;

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
	
	public void displayBooks(String str) {
		bookListArea.append(str+"\n");
	}
	
	public void displayCheckOutDate(String str) {
		outDateArea.append(str+"\n");
	}
	
	public void displayDueDate(String str) {
		dueDateArea.append(str+"\n");
	}
	
	public void displayOverDueFlag(Boolean isOverDue) {
		if (isOverDue == true)
			overDueArea.append("Overdue \n");
		else
			overDueArea.append("-\n");
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getSubject() {
		return subjectField.getText();
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}

	private void createSubjectField() {

		subjectField = new JTextField(255);		
		subjectField.setName(SUBJECT_STRING);
		subjectField.addKeyListener(new MyTextFieldKeyListener());		

		subjectFieldLabel = new JLabel(SUBJECT_STRING + ":");
		subjectFieldLabel.setLabelFor(subjectField);        

	}

	private void createBookListPane() {

		bookListArea = new JTextArea();
		bookListArea.setEditable(false);

		bookListPane = new JScrollPane(bookListArea);
		bookListPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BOOKLIST_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		bookListPane.setName(BOOKLIST_STRING);	

	}
	
	private void createOutDatePane() {

		outDateArea = new JTextArea();
		outDateArea.setEditable(false);

		outDatePane = new JScrollPane(outDateArea);
		outDatePane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(OUTDATE_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		outDatePane.setName(OUTDATE_STRING);	

	}
	
	private void createDueDatePane() {

		dueDateArea = new JTextArea();
		dueDateArea.setEditable(false);

		dueDatePane = new JScrollPane(dueDateArea);
		dueDatePane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(DUEDATE_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		dueDatePane.setName(DUEDATE_STRING);	

	}
	
	private void createOverDuePane() {

		overDueArea = new JTextArea();
		overDueArea.setEditable(false);

		overDuePane = new JScrollPane(overDueArea);
		overDuePane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(OVERDUE_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		overDuePane.setName(OVERDUE_STRING);	

	}
	
	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public LibrarianUIGenerateBook(MainJFrame f) {	     

		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Generate Report for Checked Out Books"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createSubjectField();
		createBookListPane();
		createOutDatePane();
		createDueDatePane();
		createOverDuePane();
		createSubmit();

		GridBagConstraints gridc;
		
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(subjectFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(subjectField, gridc);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		//Insert output panes
		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.weightx = 4;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(bookListPane, gridc);
		
		gridc = new GridBagConstraints();
		gridc.gridx = 1;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(outDatePane, gridc);
		
		gridc = new GridBagConstraints();
		gridc.gridx = 2;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(dueDatePane, gridc);
		
		gridc = new GridBagConstraints();
		gridc.gridx = 3;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(overDuePane, gridc);

		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.gridy = 1;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.gridheight = 5;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(panel, gridc);

		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 6;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 7;
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
						Borrowing[] bookReport = app.generateCheckedOutBooksReport();
						System.out.println("why");
						for (int i = 0; i < bookReport.length; i++){
							System.out.println(bookReport[i].getBook().getTitle());
						}
						for (int i = 0; i < bookReport.length; i++){
							displayBooks(bookReport[i].getBook().getTitle());
							displayCheckOutDate("bookReport[i].getOutDate()");
							displayDueDate("bookReport[i].getInDate()");	
						}
											
						/*if (bookListArea.getLineCount()%2==0)
							displayOverDueFlag(true);
						else
							displayOverDueFlag(false);*/
						
						displayOutput("Subject: "+getSubject());
						Thread.sleep(1000);						
						//displayBooks("Thread Ended");

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
