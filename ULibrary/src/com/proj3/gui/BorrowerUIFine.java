package com.proj3.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BorrowerUIFine extends JPanel implements ActionListener {
	
	private MainJFrame mainFrame;

	private static final String FID_STRING = "FINE ID";
	private static final String FIDLIST_STRING = "FINE LIST";
	private static final String AMOUNT_STRING = "AMOUNT($)";

	private JTextArea fineArea;
	
	private JScrollPane fineAreaPane;
	
	private JTextField fIDField, amountField;

	private JLabel fIDFieldLabel, amountFieldLabel;

	private JButton submitButton;
	
	private static final int MAX_BID = Integer.MAX_VALUE;
	private static final int MIN_BID = 0;

	public JPanel getThisPanel() {
		return this;
	}

	public void setBorderRed(JTextField field, Boolean b) {
		if (b)
			field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.pink));
		else
			field.setBorder(BorderFactory.createEtchedBorder());
	}

	public void displayFineList(String str) {
		fineArea.append(str + "\n");
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public String getFID() {
		return fIDField.getText();
	}
	
	public String getAmount() {
		return amountField.getText();
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	private void createFIDField() {

		fIDField = new JTextField();		
		fIDField.setName(FID_STRING);
		fIDField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing		
			}

			//Format/Error Checking. Error indicated by red border.
			public void focusLost(FocusEvent e) {
				try {
					if (!((JTextField)e.getComponent()).getText().isEmpty()) {					
						bidFieldListenerAction(e);
						setBorderRed(fIDField, false);
					}
					else 					
						throw new NullPointerException("FID can not be null");
				} catch (NumberFormatException ex) {
					setBorderRed(fIDField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(fIDField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(fIDField, true);
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

		fIDFieldLabel = new JLabel(FID_STRING + ":");
		fIDFieldLabel.setLabelFor(fIDField);        

	}
	
	private void createAmountField() {

		amountField = new JTextField();		
		amountField.setName(AMOUNT_STRING);
		amountField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing		
			}

			//Format/Error Checking. Error indicated by red border.
			public void focusLost(FocusEvent e) {
				try {
					if (!((JTextField)e.getComponent()).getText().isEmpty()) {					
						bidFieldListenerAction(e);
						setBorderRed(amountField, false);
					}
					else 					
						throw new NullPointerException("Amount can not be null");
				} catch (NumberFormatException ex) {
					setBorderRed(amountField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(amountField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(amountField, true);
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

		amountFieldLabel = new JLabel(AMOUNT_STRING + ":");
		amountFieldLabel.setLabelFor(amountField);        

	}

	private void createFinePane() {

		fineArea = new JTextArea();
		fineArea.setEditable(false);

		fineAreaPane = new JScrollPane(fineArea);
		fineAreaPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(FIDLIST_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		fineAreaPane.setName(FIDLIST_STRING);	

	}
	
	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public BorrowerUIFine(MainJFrame f) {	     

		mainFrame = f;

		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Pay Fine"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createFIDField();
		createAmountField();
		createFinePane();
		createSubmit();
		
		GridBagConstraints gridc;
		
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(fineAreaPane, gridc);
		
		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 1;
		this.add(Box.createVerticalStrut(5), gridc);
		
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 0;
		gridc.gridy = 2;
		gridc.weightx = 0;
		this.add(fIDFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 1;
		gridc.gridy = 2;
		gridc.weightx = 1;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(fIDField, gridc);
						
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.EAST;
		gridc.gridx = 2;
		gridc.gridy = 2;
		gridc.weightx = 0;
		this.add(amountFieldLabel, gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 3;
		gridc.gridy = 2;
		gridc.weightx = 1;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(amountField, gridc);
		
		//Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 3;
		this.add(Box.createVerticalStrut(10), gridc);

		//Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.gridy = 4;
		gridc.weightx = 1;
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
						displayFineList("Thread Started");
						displayFineList("FID: "+getFID());			
						displayOutput("FID: "+getFID());
						Thread.sleep(3000);						
						displayFineList("Thread Ended");

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
