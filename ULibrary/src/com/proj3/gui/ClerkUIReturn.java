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
import javax.swing.JTextField;

import com.proj3.app.ClerkApp;
import com.proj3.database.Database;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrowing;
import com.proj3.model.Fine;

@SuppressWarnings("serial")
public class ClerkUIReturn extends JPanel implements ActionListener {
	
	private MainJFrame mainFrame;
	
	private static final String borid_STRING = "BORID";
	//private static final String COPYNUMBER_STRING = "COPY NUMBER";
	
	private JTextField boridField;
	private JLabel boridFieldLabel;

//	private static final int MAX_CN = Integer.MAX_VALUE;
	//private static final int MIN_CN = 0;
	
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
	
	public String getborid() {
		return boridField.getText();
	}
	/*
	public String getCopyNumber() {
		return copyNumberField.getText();
	}
	*/
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createboridField() {

		boridField = new JTextField(255);		
		boridField.setName(borid_STRING);
		boridField.addFocusListener(new MyFocusListener());		

		boridFieldLabel = new JLabel(borid_STRING + ":");
		boridFieldLabel.setLabelFor(boridField);        

	}
/*
	private void createCopyNumberField() {

		copyNumberField = new JTextField();		
		copyNumberField.setName(COPYNUMBER_STRING);
		copyNumberField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing		
			}

			//Format/Error Checking. Error indicated by red border.
			public void focusLost(FocusEvent e) {
				try {
					if (!((JTextField)e.getComponent()).getText().isEmpty()) {					
						copyNumberFieldListenerAction(e);
						setBorderRed(copyNumberField, false);
					}
					else 					
						throw new NullPointerException("BID can not be null");
				} catch (NumberFormatException ex) {
					setBorderRed(copyNumberField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(copyNumberField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(copyNumberField, true);
					throw new IllegalArgumentException("Unknown Argument Exception.");
				}
			}
			*/
			//Throws exception if the bid is out of range of not a number
	/*
			public void copyNumberFieldListenerAction(FocusEvent e) {
				try {					
					int input = Integer.parseInt(((JTextField) e.getSource()).getText());
					if (input < MIN_CN || input > MAX_CN) {
						throw new IllegalArgumentException("Out of Range. Max BID = "+MAX_CN+". Min BID = "+MIN_CN+".");
					}
				} catch (NumberFormatException ex) {
					throw new NumberFormatException("Only numbers are permitted.");
				}
			}			

		});	
		
		copyNumberFieldLabel = new JLabel(COPYNUMBER_STRING + ":");
		copyNumberFieldLabel.setLabelFor(copyNumberField);        
	}	
*/
	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public ClerkUIReturn(MainJFrame f) {
		
		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Process Return"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create fields for inputs
		createboridField();
		//createCopyNumberField();	
		createSubmit();

		//Insert to the panel orderly
		GridBagConstraints gridc;		

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(boridFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(boridField, gridc);

		//Insert spacing between the fields
		gridc = new GridBagConstraints();
		gridc.gridy = 1;
		this.add(Box.createVerticalStrut(10), gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 2;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		//this.add(copyNumberFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 2;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		//this.add(copyNumberField, gridc);

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

		
						Database db = mainFrame.getDB();
						ClerkApp ca = new ClerkApp(db);	
						
						//FOR DEMO
						//Borid: before1 3016(on hold),show tables 3013 (new book copy), before2 3015(in + fine)
						/*
						Borrowing before1 = db.searchBorrowingsByClerkNull(3016);
						BookCopy bc1 = db.selectCopyByCallAndCopyNumber(before1.getCallNumber(), before1.getCopy().getCopyNo());
						System.out.println(before1.toStringForClerk() + "\n" + bc1.toStringForClerk()); 
						*/
						
						/*
						Borrowing before2 = db.searchBorrowingsByClerkNull(3015);
						BookCopy bc2 = db.selectCopyByCallAndCopyNumber(before2.getCallNumber(), before2.getCopy().getCopyNo());
						System.out.println(before2.toStringForClerk() + "\n" + bc2.toStringForClerk());
						*/
						
						String message = ca.processReturn(Integer.parseInt(getborid()));
						displayOutput(message);
						

						//FOR DEMO
						//Borid: 3015(fine + in)
						/*
						Borrowing after1 = db.searchBorrowingsByClerk(3016);
						BookCopy ac1 = db.selectCopyByCallAndCopyNumber(after1.getCallNumber(), after1.getCopy().getCopyNo());
						System.out.println(after1.toStringForClerk() + "\n" + ac1.toStringForClerk());
						*/
						
						/*
					    Borrowing after2 = db.searchBorrowingsByClerk(3015);
						BookCopy ac2 = db.selectCopyByCallAndCopyNumber(after2.getCallNumber(), after2.getCopy().getCopyNo());
						System.out.println(after2.toStringForClerk() + "\n" + ac2.toStringForClerk());
						//WIll throw null if not correct, make sure uncomment ONLY for demo
						Fine fine = db.selectFineByBorid(3015);	
						System.out.println(fine.toStringForClerk());
						*/
						
						//TODO: GUI sends email to user who has a hold
						//Test with DB reset, then BORID = 3014
						if (ca.getEmail() != null) {
							System.out.println(ca.getEmail());
						}

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
