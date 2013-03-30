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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BorrowerUICheck extends JPanel implements ActionListener{

	private MainJFrame mainFrame;

	private static final String BORROWEDBOOKS_STRING = "BORROWED BOOKS";
	private static final String FINES_STRING = "FINES";
	private static final String HOLDREQUESTS_STRING = "HOLD REQUESTS";
	
	private JTextArea borrowedBookArea, fineArea, holdRequestArea;
	private JScrollPane borrowedBookPane, finePane, holdRequestPane;

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
	
	public void displayBorrowedBooks(String str) {
		borrowedBookArea.append("Overdue \n");
	}
	
	public void displayFine(String str) {
		fineArea.append("Overdue \n");
	}
	
	public void displayHoldRequests(String str) {
		holdRequestArea.append("Overdue \n");
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}	

	private void createBorrowedBookPane() {

		borrowedBookArea = new JTextArea();
		borrowedBookArea.setEditable(false);

		borrowedBookPane = new JScrollPane(borrowedBookArea);
		borrowedBookPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BORROWEDBOOKS_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		borrowedBookPane.setName(BORROWEDBOOKS_STRING);	

	}

	private void createFinePane() {

		fineArea = new JTextArea();
		fineArea.setEditable(false);

		finePane = new JScrollPane(fineArea);
		finePane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(FINES_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		finePane.setName(FINES_STRING);	

	}

	private void createCopyOutPane() {

		holdRequestArea = new JTextArea();
		holdRequestArea.setEditable(false);

		holdRequestPane = new JScrollPane(holdRequestArea);
		holdRequestPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(HOLDREQUESTS_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		holdRequestPane.setName(HOLDREQUESTS_STRING);	

	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public BorrowerUICheck(MainJFrame f) {	     

		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Check Account"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createBorrowedBookPane();
		createFinePane();
		createCopyOutPane();
		createSubmit();
		
		GridBagConstraints gridc;
		
		//Insert output panes
		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(borrowedBookPane, gridc);

		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.gridy = 1;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(finePane, gridc);

		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.gridy = 2;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(holdRequestPane, gridc);
		
		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 3;
		this.add(Box.createVerticalStrut(10), gridc);

		
		//Insert the submit button
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 4;
		gridc.weightx = 1;
		gridc.gridwidth = 2;
		gridc.gridy = GridBagConstraints.PAGE_END;
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
						displayBorrowedBooks("Thread Started");
						displayFine("Fine");
						displayHoldRequests("Request ");						
						Thread.sleep(3000);						
						displayOutput("Thread Ended");

					} catch (Exception e) {
						throw new RuntimeException("Error while processing [" +
								getThisPanel().getName() + "] Method.");
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
