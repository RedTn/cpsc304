//src --> pacakge "com" --> folder "proj3"
package com.proj3.gui;

//Gui package
import javax.swing.*;
//We need to import the java.sql package to use JDBC
import java.sql.*;

//for reading from the command line
import java.io.*;

//for the login window
import javax.swing.*;

import com.proj3.database.Database;

import java.awt.*;
import java.awt.event.*;

import java.awt.*;
import java.awt.event.*;

public class Clerk extends JFrame {
	private JLabel label;
	private JTextField add_text,check_text,process_text,overdue_text;
	private JButton add_but,check_but,process_but,overdue_but;
	
	//Instantiate new buffereader 'in'
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    private Database db;
	public Clerk(Database db) {
		this.db = db;
		//super(frame, "Clerk Mode", true);
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
		//Button event for Add Borrower
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
			//JLabel bid,password,name, address, phone,  emailAddress, sinOrStNo, expiryDate, type;
			//JTextField _bid,_password,_name, _address, _phone,  _emailAddress, _sinOrStNo, _expiryDate, _type;
			PreparedStatement  ps;
			
			String bpassword;
			String bname;
			String baddress;
			String bphone;
			String bemail;
			String bsin;
			String bdate;
			String btype;
			
			HideDialog();
			setLayout(new GridLayout(10,2));
	
			//TODO make this work
			//db.insertBorrower(bpassword, bname, baddress, bphone, bemail, bsin, bdate, btype);
		    
		}
	}
}