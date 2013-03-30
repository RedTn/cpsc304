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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.proj3.model.HoldRequest;

@SuppressWarnings("serial")
public class BorrowerUIHold extends JPanel implements ActionListener{

	private MainJFrame mainFrame;

	private static final String CALLNUMBER_STRING = "CALL NUMBER";
	private static final String DATE_STRING = "DATE";

	private JTextField callNumberField, dateField;

	private JLabel callNumberFieldLabel, dateFieldLabel;

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
	
	public void displayHolds(HoldRequest[] holds) {
		//TODO
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getCallNumber() {
		return callNumberField.getText();
	}
	
	public String getDate() {
		return dateField.getText();
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createcallNumberField() {

		callNumberField = new JTextField(255);		
		callNumberField.setName(CALLNUMBER_STRING);
		callNumberField.addFocusListener(new MyFocusListener());
		callNumberField.addKeyListener(new MyTextFieldKeyListener());		

		callNumberFieldLabel = new JLabel(CALLNUMBER_STRING + ":");
		callNumberFieldLabel.setLabelFor(callNumberField);        

	}

	private void createDateField() {

		dateField = new JFormattedTextField(java.util.Calendar.getInstance().getTime());
		dateField.setName(DATE_STRING);
		dateField.addFocusListener(new MyFocusListener());
		
		dateFieldLabel = new JLabel(DATE_STRING + ":");
		dateFieldLabel.setLabelFor(dateField);

	}
	
	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public BorrowerUIHold(MainJFrame f) {	     

		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Place Hold Request"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createcallNumberField();
		createDateField();
		createSubmit();
		
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
		
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 1;
		gridc.weightx = 0;
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(dateFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 1;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(dateField, gridc);
				
		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 2;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridwidth = 2;
		gridc.gridy = 3;
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

						String msg = "Failed to place the hold request";
						if (mainFrame.bApp().placeHold(getCallNumber())) {
							msg = "Successfully placed the hold request";
						}
						
						displayOutput(msg);
						displayHolds(mainFrame.bApp().getHolds());

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
