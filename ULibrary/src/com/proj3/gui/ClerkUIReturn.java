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

@SuppressWarnings("serial")
public class ClerkUIReturn extends JPanel implements ActionListener {
	
	private MainJFrame mainFrame;
	
	private static final String CALLNUMBER_STRING = "CALL NUMBER";
	private static final String COPYNUMBER_STRING = "COPY NUMBER";
	
	private JTextField callNumberField, copyNumberField;	
	private JLabel callNumberFieldLabel, copyNumberFieldLabel;

	private static final int MAX_CN = Integer.MAX_VALUE;
	private static final int MIN_CN = 0;
	
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
	
	public String getCallNumber() {
		return callNumberField.getText();
	}
	
	public String getCopyNumber() {
		return copyNumberField.getText();
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createCallNumberField() {

		callNumberField = new JTextField(255);		
		callNumberField.setName(CALLNUMBER_STRING);
		callNumberField.addFocusListener(new MyFocusListener());		

		callNumberFieldLabel = new JLabel(CALLNUMBER_STRING + ":");
		callNumberFieldLabel.setLabelFor(callNumberField);        

	}

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
			
			//Throws exception if the bid is out of range of not a number
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
		createCallNumberField();
		createCopyNumberField();	
		createSubmit();

		//Insert to the panel orderly
		GridBagConstraints gridc;		

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(callNumberFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(callNumberField, gridc);

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
		this.add(copyNumberFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 2;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(copyNumberField, gridc);

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

						//TODO INSERT METHOD HERE
						// USE displayItems(String str)
						// BELOW IS AN EXAMPLE
						displayOutput("Thread Started");
						displayOutput("Call Number: "+getCallNumber());
						displayOutput("Copy Number: "+getCopyNumber());
						Thread.sleep(3000);						
						displayOutput("Thread Ended");

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
