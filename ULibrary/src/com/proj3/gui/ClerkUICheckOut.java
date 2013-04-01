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

import com.proj3.app.ClerkApp;
import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.HoldRequest;


@SuppressWarnings("serial")
public class ClerkUICheckOut extends JPanel implements ActionListener {

	private MainJFrame mainFrame;
	
	private static final String BID_STRING = "ID";
	private static final String CALLNUMBER_STRING = "CALL NUMBER";
	private static final String NOTE_STRING = "NOTE";

	private static final int MAX_BID = Integer.MAX_VALUE;
	private static final int MIN_BID = 0;

	private JTextField bidField;
	
	private JTextArea noteField, callNumberField;
	
	private JLabel bidFieldLabel;

	private JScrollPane callNumberPane, notePane;

	JButton submitButton;

	public JPanel getThisPanel() {
		return this;
	}

	public void setBorderRed(JTextField field, Boolean b) {
		if (b)
			field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.pink));
		else
			field.setBorder(BorderFactory.createEtchedBorder());
	}
	
	public void setBorderRed(JTextArea field, Boolean b) {
		if (b)
			field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.pink));
		else
			field.setBorder(BorderFactory.createEtchedBorder());
	}

	public void displayCallNumbers(String s) {
		callNumberField.append(s + "\n");
	}
	
	public void displayNote(String str) {
		noteField.append(str+"\n");
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getBID() {
		return bidField.getText();
	}
	
	public String getCallNumbers() {
		return callNumberField.getText();
	}
		
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createBidField() {

		bidField = new JTextField();		
		bidField.setName(BID_STRING);
		bidField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing		
			}

			//Format/Error Checking. Error indicated by red border.
			public void focusLost(FocusEvent e) {
				try {
					if (!((JTextField)e.getComponent()).getText().isEmpty()) {					
						bidFieldListenerAction(e);
						setBorderRed(bidField, false);
					}
					else 					
						throw new NullPointerException("BID can not be null");
				} catch (NumberFormatException ex) {
					setBorderRed(bidField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(bidField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(bidField, true);
					throw new IllegalArgumentException("Unknown Argument Exception.");
				}
			}

			//Throws exception if the bid is out of range of not a number
			public void bidFieldListenerAction(FocusEvent e) {
				try {					
					int input = Integer.parseInt(((JTextField) e.getSource()).getText());
					if (input < MIN_BID || input > MAX_BID) {
						throw new IllegalArgumentException("Out of Range. Max BID = "+MAX_BID+". Min BID = "+MIN_BID+".");
					}
				} catch (NumberFormatException ex) {
					throw new NumberFormatException("Only numbers are permitted.");
				}
			}			

		});		

		bidFieldLabel = new JLabel(BID_STRING + ":");
		bidFieldLabel.setLabelFor(bidField);        

	}

	private void createCallNumberField() {

		callNumberField = new JTextArea();
		callNumberField.setEditable(true);
		callNumberField.addFocusListener(new MyFocusListener());
		callNumberField.setLineWrap(true);

		callNumberPane = new JScrollPane(callNumberField);
		callNumberPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(CALLNUMBER_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		callNumberPane.setName(CALLNUMBER_STRING);
	}	

	private void createNotePane() {

		noteField = new JTextArea();
		noteField.setEditable(false);

		notePane = new JScrollPane(noteField);
		notePane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(NOTE_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		notePane.setName(NOTE_STRING);	

	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public ClerkUICheckOut(MainJFrame f) {	     

		mainFrame = f;
		
		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Check Out Items"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create fields for inputs
		createBidField();
		createCallNumberField();
		createNotePane();		
		createSubmit();

		//Insert to the panel orderly
		GridBagConstraints gridc;		

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(bidFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(bidField, gridc);

		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 1;
		this.add(Box.createVerticalStrut(10), gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 2;		
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.gridheight = 5;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(callNumberPane, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 7;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.gridheight = 5;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(notePane, gridc);

		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 13;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridwidth = 2;
		gridc.gridy = 14;
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

						//ClerkApp
						String callNumbers = getCallNumbers();
						Database db = mainFrame.getDB();
						
						//FOR DEMO
						//BID 4005: Legit
						//BID 4006: Expired
						//BID 4002: Fine
						//BID 6969: No borrower
						//TEST: Bible (on-hold), Dawkins (new copy), real (legit process)
						BookCopy before = db.selectCopyByCallAndCopyNumber("BIBLE", 1);
						BookCopy before2 = db.selectCopyByCallAndCopyNumber("DAWKINS Selfish", 1);
						BookCopy before3 = db.selectCopyByCallAndCopyNumber("REAL Ultimate", 1);
						Book book = db.selectBookByCallNumber("BIBLE");
						Borrower borrower = db.selectBorrowerById(4001);
						HoldRequest hr = db.selectHoldRequestsByHid(10019,book,borrower);
						System.out.println(before.toStringForClerk() + "\n" + before2.toStringForClerk() + "\n" + before3.toStringForClerk() + hr.toStringForClerk());
						
						ClerkApp ca = new ClerkApp(db);
						String message = ca.checkOutItems(Integer.parseInt(getBID()), callNumbers);
						displayOutput(message);
						if(ca.getNote() != null) {
						displayNote(ca.getNote());
						}

						//FOR DEMO
						BookCopy after = db.selectCopyByCallAndCopyNumber("BIBLE", 1);
						BookCopy after2 = db.selectCopyByCallAndCopyNumber("DAWKINS Selfish", 2);
						BookCopy after3 = db.selectCopyByCallAndCopyNumber("REAL Ultimate", 1);
						System.out.println(after.toStringForClerk() + "\n" + after2.toStringForClerk() + "\n" + after3.toStringForClerk());
						
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
			try {
				if (((JTextField)e.getComponent()).getText()==null || ((JTextField)e.getComponent()).getText().isEmpty()) {
					setBorderRed(((JTextField)e.getComponent()), true);
					throw new NullPointerException(((JTextField)e.getComponent()).getName() + " can not be null.");
				}
				setBorderRed(((JTextField)e.getComponent()), false);
			} catch (Exception ex) {
				if (((JTextArea)e.getComponent()).getText()==null || ((JTextArea)e.getComponent()).getText().isEmpty()) {
					setBorderRed(((JTextArea)e.getComponent()), true);
					throw new NullPointerException(((JTextArea)e.getComponent()).getName() + " can not be null.");
				}
				setBorderRed(((JTextArea)e.getComponent()), false);
			}
		}

	}

}
