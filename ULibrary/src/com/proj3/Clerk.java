//src --> pacakge "com" --> folder "proj3"
package com.proj3;

//Gui package
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Clerk extends JDialog {
	private JLabel label;
	private JTextField add_text,check_text,process_text,overdue_text;
	private JButton add_but,check_but,process_but,overdue_but;
	
	public Clerk(JFrame frame) {
		super(frame, "Clerk Mode", true);
		setLayout(new FlowLayout());
		
		label = new JLabel("Select operation");
		add(label);
		
		add_but = new JButton("Add a new borrower");
		add(add_but);
		
		check_but = new JButton("Check-out borrower's items");
		add(check_but);
		
		process_but = new JButton("Process a return");
		add(process_but);
		
		overdue_but = new JButton("Check overdue items");
		add(overdue_but);
		
		addBorrower _add = new addBorrower();
		add_but.addActionListener(_add);
		
		
	}
	public void HideDialog()
	{
		add_but.setVisible(false);
		check_but.setVisible(false);
		process_but.setVisible(false);
		overdue_but.setVisible(false);
		label.setVisible(false);
	}
	
	public class addBorrower implements ActionListener {
		public void actionPerformed(ActionEvent _add) {
			JLabel bid,password,name, address, phone,  emailAddress, sinOrStNo, expiryDate, type;
			JTextField _bid,_password,_name, _address, _phone,  _emailAddress, _sinOrStNo, _expiryDate, _type;
			
			HideDialog();
			setLayout(new GridLayout(10,2));
			
			bid = new JLabel ("Bid:");
			add(bid);
			
			_bid = new JTextField(38);
			add(_bid);
			
			password = new JLabel("Password:");
			add(password);
			
			_password = new JTextField(255);
			add(_password);
		}
	}
}