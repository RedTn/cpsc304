package com.proj3.gui;

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

import com.proj3.app.ClerkApp;
import com.proj3.database.Database;
import com.proj3.model.Borrowing;


@SuppressWarnings("serial")
public class ClerkUIOverdue extends JPanel implements ActionListener {

	private MainJFrame mainFrame;
	
	private static final String CHECKOVERDUE_STRING = "Check Overdue Items";
	private static final String ITEMLIST_STRING = "Items";
	private static final String BORROWERLIST_STRING = "Borrowers";

	private JScrollPane itemListPane, borrowerListPane;
	private JTextArea itemField, borrowerField;
	
	JButton submitButton;

	public JPanel getThisPanel() {
		return this;
	}
	
	public void displayItems(String s) {
		itemField.append(s + "\n");
	}
	
	public void displayBorrowers(String s) {
		borrowerField.append(s + "\n");
	}
		
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createItemListPane() {

		itemField = new JTextArea();
		itemField.setEditable(false);

		itemListPane = new JScrollPane(itemField);
		itemListPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(ITEMLIST_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		itemListPane.setName(ITEMLIST_STRING);	

	}
	
	private void createBorrowerListPane() {

		borrowerField = new JTextArea();
		borrowerField.setEditable(false);

		borrowerListPane = new JScrollPane(borrowerField);
		borrowerListPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BORROWERLIST_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		borrowerListPane.setName(BORROWERLIST_STRING);	

	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText(CHECKOVERDUE_STRING);
		submitButton.addActionListener(this);
	}

	public ClerkUIOverdue(MainJFrame f) {	     

		mainFrame = f;
		
		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Check Overdue Items"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create fields for inputs
		createItemListPane();
		createBorrowerListPane();		
		createSubmit();

		//Insert to the panel orderly
		GridBagConstraints gridc;		

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 0;		
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridheight = 5;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(itemListPane, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridheight = 5;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(borrowerListPane, gridc);

		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 5;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridwidth = 2;
		gridc.gridy = 6;
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

						
						Database db = mainFrame.getDB();
						ClerkApp ca = new ClerkApp(db);	
						Borrowing[] borrowing = ca.checkOverdueItems();
						StringBuilder items = new StringBuilder();
						StringBuilder borrowers =  new StringBuilder();
						StringBuilder emails = new StringBuilder();
						
						if (borrowing.length == 0) {
							displayOutput("There are no overdue items.");
						}
						else {
							for(int i = 0; i < borrowing.length; i++){
								items.append(borrowing[i].getCallNumber() + "\n");
								borrowers.append(borrowing[i].getBorrower().getName() + ", Bid:" + borrowing[i].getBid() + "\n");
								emails.append(borrowing[i].getBorrower().getEmail() + "\n");
							}
						}
						
						displayItems(items.toString());
						displayBorrowers(borrowers.toString());
						
						//TODO GUI: sends emails to everyone who has overdue item
						System.out.println(emails.toString());

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

}
