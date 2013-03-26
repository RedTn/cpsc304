//src --> pacakge "com" --> folder "proj3"
package com.proj3.gui;

//Gui package
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
//We need to import the java.sql package to use JDBC
import java.sql.*;

//for reading from the command line
import java.io.*;

//for the login window
import javax.swing.*;

import com.proj3.branch;

import java.awt.*;
import java.awt.event.*;

public class Gui extends JFrame {
	JMenuBar menubar;
	JMenu user;
	JMenuItem clerk;
	private static Connection con;
	
	public Gui() {
		setLayout(new FlowLayout());
		
		menubar = new JMenuBar();
		add(menubar);
		
		user = new JMenu("User");
		menubar.add(user);
		
		clerk = new JMenuItem("Clerk");
		user.add(clerk);
		
		setJMenuBar(menubar);
		
		event e = new event();
		
		//Button event for Clerk
		clerk.addActionListener(e);
		
	}
	public class event implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Clerk newclerk = new Clerk(Gui.this);
			newclerk.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			newclerk.setSize(500,300);
			newclerk.setLocation(800,300);
			newclerk.setVisible(true);
		}
	}
	public static Connection getCon() {
		//Getter for branch's login connection class
		return con;
	}
	
	public static void setCon(Connection con_) {
		//Setter for Con
		con = con_;
	}
	
	public static void main (String args[]) {
		branch b = new branch();
		 Gui mygui = new Gui();
			
		//CLose on "X"
		mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		mygui.setSize(500, 300);
		mygui.setLocation(700,400);
		mygui.setVisible(true);
		mygui.setTitle("Main Menu");
		//Set con
		setCon(b.getConnection());

	}
	
}
	