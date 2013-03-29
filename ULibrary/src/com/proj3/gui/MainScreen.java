//src --> pacakge "com" --> folder "proj3"
package com.proj3.gui;

//Gui package
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.proj3.database.Database;
import com.proj3.model.Borrowing;
import com.proj3.app.*;

//We need to import the java.sql package to use JDBC
//for reading from the command line
//for the login window

public class MainScreen extends JFrame {
	private Database db;
	
	public MainScreen() {
		JLabel banner = new JLabel("ULibrary User Portal");
		banner.setFont(new Font("whatever", Font.BOLD, 20));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(banner);
		panel.add(Box.createRigidArea(new Dimension(0, 50)));
		panel.add(new LoginChooser(this));
		
		add(panel);
	}

	public MainScreen(Database db) {
		this();
		this.db = db;
	}
	public void showClerkScreen() {
		Clerk newclerk = new Clerk(db);
		newclerk.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		newclerk.setSize(500, 300);
		newclerk.setLocation(800, 300);
		newclerk.setVisible(true);
	}
	
	public void showLibrarianScreen() {
		//TODO implement this
	}
	
	public void showBorrowerScreen() {
		//TODO implement this
	}
	
	public static void main(String args[]) throws SQLException {
		 
		Database db = new Database("passwordfile");
		if (db.connect()) {
			MainScreen mygui = new MainScreen(db);

			// CLose on "X"
			mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			mygui.setSize(500, 300);
			mygui.setLocation(700, 400);
			mygui.setVisible(true);
			mygui.setTitle("Login");
			
			ClerkApp ca = new ClerkApp(db);
			Borrowing[] b = ca.checkOverdueItems();
			for (int i=0; i<b.length; i++)
				System.out.println(b[i]);
		} else {
			
		}

	}

}
