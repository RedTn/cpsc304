package com.proj3;

import com.proj3.app.BorrowerApp;
import com.proj3.app.ClerkApp;
import com.proj3.app.LibrarianApp;
import com.proj3.database.Database;
import com.proj3.gui.MainJFrame;

public class Launcher {

	public static void main(String[] args) {
		Database db = new Database("passwordfile");
		
		if (db.connect()) {
			final BorrowerApp b = new BorrowerApp(db);
			final LibrarianApp l = new LibrarianApp(db);
			final ClerkApp c = new ClerkApp(db);
			
			javax.swing.SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					new MainJFrame(b, l, c).createAndShowGUI();
				}
			});	
		} else {
			System.out.println("Failed to connect to database");
		}
		
	}

}
