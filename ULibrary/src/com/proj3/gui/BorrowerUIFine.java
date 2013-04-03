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
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.proj3.model.Fine;

@SuppressWarnings("serial")
public class BorrowerUIFine extends BorrowerPanel implements ActionListener {

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

	public void populateInfo() {
		// Run the SQL method on a separate thread
		Thread thread = new Thread(new Runnable() {
			
				
			JProgressBar progressBar = new JProgressBar();

			public void run() {
				setAllFieldsNotRed();
				/**
				 * try-finally so that it is guaranteed the submit button is
				 * re-enabled and progress bar is deleted at the end.
				 */
				try {
					submitButton.setEnabled(false);
					// Indeterminate progress bar
					addProgressBar(progressBar);
					displayFineList(bApp().getFines());

				} catch (Exception e) {
					displayErrorMessage(e.getMessage());
				} finally {
					submitButton.setEnabled(true);
					getThisPanel().remove(progressBar);
					getThisPanel().validate();
					getThisPanel().repaint();
				}

			}

		});

		thread.start();
	}

	private void displayFineList(Fine[] fines) {
		fineArea.setText("");
		for (int i = 0; i < fines.length; i++) {
			fineArea.append(fines[i].toStringForBorrower() + "\n");
		}
	}

	public int getFID() {
		String rawString = fIDField.getText();
		try {
			setBorderRed(fIDField, false);
			Integer id = Integer.parseInt(rawString);
			return id;
		} catch (NumberFormatException nfe) {
			setBorderRed(fIDField, true);
			displayErrorMessage("The fine ID you entered is not correct. It should be a number.");
		}

		return -1;
	}

	public float getAmount() {
		String rawString = amountField.getText();
		try {
			setBorderRed(amountField, false);
			float amount = Float.parseFloat(rawString);
			if (amount <= 0) {
				displayErrorMessage("Please input a positive amount.");
			} else {
				return amount;
			}
		} catch (NumberFormatException nfe) {
			setBorderRed(amountField, true);
			displayErrorMessage("The amount you entered is not a number.");
		}

		return -1;
	}

	private void createFIDField() {

		fIDField = new JTextField();
		fIDField.setName(FID_STRING);
		fIDField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// Do Nothing
			}

			// Format/Error Checking. Error indicated by red border.
			public void focusLost(FocusEvent e) {
				try {
					if (!((JTextField) e.getComponent()).getText().isEmpty()) {
						bidFieldListenerAction(e);
						setBorderRed(fIDField, false);
					} else
						throw new NullPointerException("FID can not be null");
				} catch (NumberFormatException ex) {
					setBorderRed(fIDField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(fIDField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(fIDField, true);
					throw new IllegalArgumentException(
							"Unknown Argument Exception.");
				}
			}

			// Throws exception if the bid is out of range of not a number
			public void bidFieldListenerAction(FocusEvent e) {
				try {
					int input = Integer.parseInt(((JTextField) e.getSource())
							.getText());
					if (input < MIN_BID || input > MAX_BID) {
						throw new IllegalArgumentException(
								"Out of Range. Max BID = " + MAX_BID
										+ ". Min BID = " + MIN_BID + ".");
					}
				} catch (NumberFormatException ex) {
					throw new NumberFormatException(
							"Only numbers are permitted.");
				}
			}

		});

		fIDFieldLabel = new JLabel(FID_STRING + ":");
		fIDFieldLabel.setLabelFor(fIDField);

	}

	private void createAmountField() {

		amountField = new JTextField();
		amountField.setName(AMOUNT_STRING);
		amountField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// Do Nothing
			}

			// Format/Error Checking. Error indicated by red border.
			public void focusLost(FocusEvent e) {
				try {
					if (!((JTextField) e.getComponent()).getText().isEmpty()) {
						amountFieldListenerAction(e);
						setBorderRed(amountField, false);
					} else
						throw new NullPointerException("Amount can not be null");
				} catch (NumberFormatException ex) {
					setBorderRed(amountField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(amountField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(amountField, true);
					throw new IllegalArgumentException(
							"Unknown Argument Exception.");
				}
			}

			// Throws exception if the bid is out of range of not a number
			public void amountFieldListenerAction(FocusEvent e) {
				try {
					float input = Float.parseFloat(((JTextField) e.getSource())
							.getText());
					if (input < MIN_BID || input > MAX_BID) {
						throw new IllegalArgumentException(
								"Out of Range. Max BID = " + MAX_BID
										+ ". Min BID = " + MIN_BID + ".");
					}
				} catch (NumberFormatException ex) {
					throw new NumberFormatException(
							"Only numbers are permitted.");
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
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		fineAreaPane.setName(FIDLIST_STRING);

	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public BorrowerUIFine(MainJFrame f) {

		setMainFrame(f);

		// Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Pay Fine"),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		// Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		// Create text fields for inputs
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

		// Insert spacing between the fields and the submit button
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

		// Insert spacing between the fields and the submit button
		gridc = new GridBagConstraints();
		gridc.gridy = 3;
		this.add(Box.createVerticalStrut(10), gridc);

		// Insert the submit button
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.gridy = 4;
		gridc.weightx = 1;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(submitButton, gridc);
	}

	private void setAllFieldsNotRed() {
		setBorderRed(fIDField, false);
		setBorderRed(amountField, false);
	}
	/**
	 * Invoked when the submit button is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			// Re-run all listeners to make sure all format checking is valid.
			for (Component c : this.getComponents()) {
				for (FocusListener f : c.getFocusListeners()) {
					f.focusLost(new FocusEvent(c, FocusEvent.FOCUS_LOST));
				}
			}

			// Run the SQL method on a separate thread
			Thread thread = new Thread(new Runnable() {
				
					
				JProgressBar progressBar = new JProgressBar();

				public void run() {
					setAllFieldsNotRed();
					/**
					 * try-finally so that it is guaranteed the submit button is
					 * re-enabled and progress bar is deleted at the end.
					 */
					try {
						submitButton.setEnabled(false);
						// Indeterminate progress bar
						addProgressBar(progressBar);

						int fid = getFID();
						float amount = getAmount();
						
						if (fid < -1) {
							setBorderRed(fIDField, true);
							displayOutput("The fine ID cannot be found.");
						} else if (amount < -1) {
							setBorderRed(amountField, true);
							displayOutput("Please enter a positive number for the amount to pay.");
						} else if (bApp().payFine(fid, amount)) {
							populateInfo();
							displayOutput("Your fine was paid successfully.");
						} else {
							displayOutput("Your fine was not paid.");
						}
						Thread.sleep(3000);

					} catch (Exception e) {
						displayErrorMessage(e.getMessage());
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
			if (((JTextField) e.getComponent()).getText() == null
					|| ((JTextField) e.getComponent()).getText().isEmpty()) {
				setBorderRed(((JTextField) e.getComponent()), true);
				throw new NullPointerException(
						((JTextField) e.getComponent()).getName()
								+ " can not be null.");
			}
			setBorderRed(((JTextField) e.getComponent()), false);
		}

	}
}
