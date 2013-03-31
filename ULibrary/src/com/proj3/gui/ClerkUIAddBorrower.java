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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.proj3.database.Database;
import com.proj3.model.BorrowerType;

@SuppressWarnings("serial")
public class ClerkUIAddBorrower extends JPanel implements ActionListener {

	private MainJFrame mainFrame;
	
	private static final String BID_STRING = "ID";
	private static final String PASSWORD_STRING = "PASSWORD";
	private static final String PASSWORDCONFIRMATION_STRING = "CONFIRM PASSWORD";
	private static final String NAME_STRING = "NAME";
	private static final String ADDRESS_STRING = "ADDRESS";
	private static final String PHONE_STRING = "PHONE";
	private static final String EMAILADDRESS_STRING = "EMAIL ADDRESS";
	private static final String SINORSTNO_STRING = "SIN OR STUDENT NO";
	private static final String EXPIREDATE_STRING = "EXPIRE DATE";
	private static final String TYPE_STRING = "TYPE";

	private static final int MAX_BID = Integer.MAX_VALUE;
	private static final int MIN_BID = 0;

	private JTextField bidField, passwordField, passwordConfirmationField,
	nameField, addressField, phoneField, emailField, sinField,
	expireField, typeField;

	private JLabel bidFieldLabel, passwordFieldLabel, passwordConfirmationFieldLabel,
	nameFieldLabel, addressFieldLabel, phoneFieldLabel,
	emailFieldLabel, sinFieldLabel, expireFieldLabel, typeFieldLabel;

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
	
	public String getBID() {
		return bidField.getText();
	}
	
	public String getPassword() {
		return passwordField.getText();
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public String getAddress() {
		return addressField.getText();
	}
	
	public String getPhoneNumber() {
		return phoneField.getText();
	}
	
	public String getEmailAddress() {
		return emailField.getText();
	}
	
	public String getSinOrStNo() {
		return sinField.getText();
	}
	
	public String getExpireDate() {
		return expireField.getText();
	}
	
	public String getType() {
		return typeField.getText();
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

	private void createPasswordField() {

		passwordField = new JPasswordField(255);
		passwordField.setName(PASSWORD_STRING);
		passwordField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing
			}

			public void focusLost(FocusEvent e) {	
				try {
					if (((JPasswordField)passwordField).getPassword().length != 0) {
						passwordFieldListenerAction(e);						
						setBorderRed(passwordField, false);
						setBorderRed(passwordConfirmationField, false);
					}
					else
						throw new NullPointerException("Password can not be null.");
				} catch (IllegalArgumentException ex) {
					setBorderRed(passwordField, true);
					setBorderRed(passwordConfirmationField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(passwordField, true);
					setBorderRed(passwordConfirmationField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(passwordField, true);
					setBorderRed(passwordConfirmationField, true);
					throw new IllegalArgumentException("Unknown Argument Exception.");
				}
			}

			public void passwordFieldListenerAction(FocusEvent e) {
				char[] pw = ((JPasswordField)passwordConfirmationField).getPassword();
				char[] pwConfirm = ((JPasswordField)e.getComponent()).getPassword(); 
				if (!Arrays.equals(pw, pwConfirm))
					throw new IllegalArgumentException("Please Confirm the password");
			}

		});
		passwordFieldLabel = new JLabel(PASSWORD_STRING + ":");
		passwordFieldLabel.setLabelFor(passwordField);

	}

	private void createPasswordConfirmationField() {

		passwordConfirmationField = new JPasswordField(255);
		passwordConfirmationField.setName(PASSWORDCONFIRMATION_STRING);
		passwordConfirmationField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing
			}

			public void focusLost(FocusEvent e) {
				try {
					if (((JPasswordField)passwordField).getPassword().length != 0) {
						passwordFieldListenerAction(e);
						setBorderRed(passwordField, false);
						setBorderRed(passwordConfirmationField, false);
					}
					else
						throw new NullPointerException("Password can not be null.");
				} catch (IllegalArgumentException ex) {
					setBorderRed(passwordField, true);
					setBorderRed(passwordConfirmationField, true);
					throw ex;
				} catch (NullPointerException ex) {
					setBorderRed(passwordField, true);
					setBorderRed(passwordConfirmationField, true);
					throw ex;
				} catch (Exception ex) {
					setBorderRed(passwordField, true);
					setBorderRed(passwordConfirmationField, true);
					throw new IllegalArgumentException("Unknown Argument Exception.");
				}
			}

			public void passwordFieldListenerAction(FocusEvent e) {
				char[] pw = ((JPasswordField)passwordField).getPassword();
				char[] pwConfirm = ((JPasswordField)e.getComponent()).getPassword(); 
				if (!Arrays.equals(pw, pwConfirm))
					throw new IllegalArgumentException("Password does not match.");
			}

		});

		passwordConfirmationFieldLabel = new JLabel(PASSWORDCONFIRMATION_STRING + ":");
		passwordConfirmationFieldLabel.setLabelFor(passwordConfirmationField);

	}

	private void createNameField() {

		nameField = new JTextField(255);
		nameField.setName(NAME_STRING);
		nameField.addFocusListener(new MyFocusListener());		
		nameFieldLabel = new JLabel(NAME_STRING + ":");
		nameFieldLabel.setLabelFor(nameField);

	}

	private void createAddressField() {

		addressField = new JTextField(255);
		addressField.setName(ADDRESS_STRING);
		addressField.addFocusListener(new MyFocusListener());		
		addressFieldLabel = new JLabel(ADDRESS_STRING + ":");
		addressFieldLabel.setLabelFor(addressField);

	}

	private void createPhoneField() {

		phoneField = new JTextField(16);
		phoneField.setName(PHONE_STRING);
		phoneField.addFocusListener(new MyFocusListener());
		phoneField.addKeyListener(new MyTextFieldKeyListener());
		
		phoneFieldLabel = new JLabel(PHONE_STRING + ":");
		phoneFieldLabel.setLabelFor(phoneField);

	}

	private void createEmailField() {

		emailField = new JTextField(255);
		emailField.setName(EMAILADDRESS_STRING);
		emailField.addFocusListener(new MyFocusListener());
		emailFieldLabel = new JLabel(EMAILADDRESS_STRING + ":");
		emailFieldLabel.setLabelFor(emailField);

	}

	private void createSinField() {

		sinField = new JTextField(20);
		sinField.setName(SINORSTNO_STRING);
		sinField.addFocusListener(new MyFocusListener());
		sinField.addKeyListener(new MyTextFieldKeyListener());
		
		sinFieldLabel = new JLabel(SINORSTNO_STRING + ":");
		sinFieldLabel.setLabelFor(sinField);

	}

	private void createExpireField() {
		Date date = java.util.Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		expireField = new JFormattedTextField(sdf.format(date));
		expireField.setName(EXPIREDATE_STRING);
		expireField.addFocusListener(new MyFocusListener());
		expireFieldLabel = new JLabel(EXPIREDATE_STRING + ":");
		expireFieldLabel.setLabelFor(expireField);

	}

	private void createTypeField() {

		typeField = new JTextField(3);
		typeField.setName(TYPE_STRING);
		typeField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				// Do Nothing
			}

			public void focusLost(FocusEvent e) {
				String type = ((JTextField)e.getComponent()).getText();
				if (type == null || type.isEmpty()) {
					setBorderRed(((JTextField)e.getComponent()), true);
					throw new IllegalArgumentException("Type can not be null");
				}
					
				if (!(type.equals("stu") || type.equals("fal") || type.equals("sta"))) {
					setBorderRed(((JTextField)e.getComponent()), true);
					throw new IllegalArgumentException("Type must be one of stu, fal, sta");
				}
				setBorderRed(((JTextField)e.getComponent()), false);
			}
			
		});
		
		typeFieldLabel = new JLabel(TYPE_STRING + ":");
		typeFieldLabel.setLabelFor(typeField);

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

	public ClerkUIAddBorrower(MainJFrame f) {	     

		mainFrame = f;
		
		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Add New Borrower"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createBidField();
		createPasswordField();
		createPasswordConfirmationField();
		createNameField();
		createAddressField();
		createPhoneField();
		createEmailField();
		createSinField();
		createExpireField();
		createTypeField();
		createSubmit();

		//Group labels and text fields
		JTextField[] textFields = {bidField, passwordField, passwordConfirmationField,
				nameField, addressField, phoneField, emailField, sinField,
				expireField, typeField};
		JLabel[] labels = {bidFieldLabel, passwordFieldLabel, passwordConfirmationFieldLabel,
				nameFieldLabel, addressFieldLabel, phoneFieldLabel,
				emailFieldLabel, sinFieldLabel, expireFieldLabel, typeFieldLabel};

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
						displayOutput("Thread Started");
						Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(getExpireDate());
						Database db = mainFrame.getDB();
						db.insertBorrower(getPassword(), getName(), getAddress(), getPhoneNumber(), getEmailAddress(), getSinOrStNo(),
								date, BorrowerType.get(getType()));		
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
