package com.proj3.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.proj3.model.Borrower;

@SuppressWarnings("serial")
public class BorrowerUILogin extends BorrowerPanel {

	private JTextField bidField;
	private JPasswordField passwordField;
	private JButton loginButton;


	public int getBID() {
		String rawString = bidField.getText();
		try {
			setBorderRed(bidField, false);
			Integer id = Integer.parseInt(rawString);
			return id;
		} catch (NumberFormatException nfe) {
			setBorderRed(bidField, true);
			displayErrorMessage("The borrower ID you entered is not correct. It should be a number.");
		}
		
		return -1;
	}
	
	public String getPassword() {
		char[] password = passwordField.getPassword();
		return new String(password);
	}
	
	private Container createLoginPanel() {

		JLabel bidLabel = new JLabel("Enter ID: ");
		JLabel passwordLabel = new JLabel("Enter password: ");
		loginButton = new JButton("Log In");

		loginButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {

				if (!bApp().isLoggedIn()) {
					int bid = getBID();
					
					if (bid > 0) {
						String password = getPassword();
						Borrower borrower = bApp().login(bid, password);
						if (borrower != null) {
							loginButton.setText("Log Out");
							bidField.setEnabled(false);
							passwordField.setEnabled(false);

							displayOutput("Welcome, "+borrower.getName()+".");
						} else {
							displayOutput("Log in failed. Check your ID and password.");
						}
					}
					
				} else {
					setBorderRed(bidField, false);
					loginButton.setText("Log In");
					bidField.setEnabled(true);
					passwordField.setEnabled(true);
					bidField.setText("");
					passwordField.setText("");
					
					displayOutput("Goodbye, "+bApp().getCurrentBorrowerName());
					bApp().logout();

					displayOutput("Logged out successfully");
				}
			}

		});

		bidField = new JTextField(10);
		bidField.addFocusListener(new MyFocusListener());

		passwordField = new JPasswordField(10);
		passwordField.setEchoChar('*');

		GridBagLayout gridb = new GridBagLayout();
		GridBagConstraints gridc = new GridBagConstraints();

		JPanel contentPane = new JPanel(gridb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.setOpaque(true);		

		// place the username label 
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		gridc.insets = new Insets(10, 10, 5, 0);
		contentPane.add(bidLabel, gridc);

		// place the text field for the username 
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.insets = new Insets(10, 0, 5, 10);
		contentPane.add(bidField, gridc);

		// place password label
		gridc.gridwidth = GridBagConstraints.RELATIVE;
		gridc.insets = new Insets(0, 10, 10, 0);
		contentPane.add(passwordLabel, gridc);

		// place the password field 
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.insets = new Insets(0, 0, 10, 10);
		contentPane.add(passwordField, gridc);

		// place the login button
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.insets = new Insets(5, 10, 10, 10);
		gridc.anchor = GridBagConstraints.CENTER;
		contentPane.add(loginButton, gridc);

		return contentPane;
	}

	public BorrowerUILogin(MainJFrame f) {
		setMainFrame(f);

		//Setting the border line around the panel
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Borrower Log In"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		this.add(createLoginPanel());
	}

}

