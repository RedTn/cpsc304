package com.proj3.gui;

import java.sql.SQLException;

import com.proj3.app.LibrarianApp;
import com.proj3.database.Database;
import com.proj3.model.BookCopy;

public class TestApp {

	public static void main(String[] args) {
		Database db = new Database("passwordfile");
		db.connect();
		LibrarianApp app = new LibrarianApp(db);
		
		try {
			BookCopy[] borrows = app.generateCheckedOutBooksReport("fiction");
			
			for (int i=0; i< borrows.length; i++) {
				System.out.println(borrows[i].getBook().getTitle());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
