package com.proj3.gui;

import com.proj3.app.BorrowerApp;
import com.proj3.database.Database;
import com.proj3.model.Borrower;
import com.proj3.model.Borrowing;
import com.proj3.model.HoldRequest;

public class TestApp {

	public static void main(String[] args) {
		Database db = new Database("passwordfile");
		db.connect();
		BorrowerApp app = new BorrowerApp(db);

		Borrower borrower = app.login(3, "123456");
		HoldRequest[] holds = app.getHolds();
		Borrowing[] borrows = app.getBorrowings();
	}
}
