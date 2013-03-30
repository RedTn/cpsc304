package com.proj3.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BorrowerUILogin extends JPanel {

	MainJFrame mainFrame;

	private JTextField bidField;
	private JPasswordField passwordField;
	private JButton loginButton;

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
	
	@SuppressWarnings("deprecation")
	public String getPassword() {
		return passwordField.getText();
	}
	
	private Container createLoginPanel() {

		JLabel bidLabel = new JLabel("Enter ID: ");
		JLabel passwordLabel = new JLabel("Enter password: ");
		loginButton = new JButton("Log In");

		loginButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {

				if (!mainFrame.bApp().isLoggedIn()) {
					loginButton.setText("Log Out");
					bidField.setEnabled(false);
					passwordField.setEnabled(false);
					mainFrame.bApp().login(Integer.parseInt(bidField.getText()), new String(passwordField.getPassword()));
				} else {
					loginButton.setText("Log In");
					bidField.setEnabled(true);
					passwordField.setEnabled(true);
					mainFrame.bApp().logout();
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
		mainFrame = f;

		//Setting the border line around the panel
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Borrower Log In"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		this.add(createLoginPanel());
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

