package com.proj3.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.BorrowerType;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;
import com.proj3.model.HoldRequest;

public class ClerkApp {
	private Database db;
	private Borrower currBorrower;
	
	public ClerkApp(Database db) {
		this.db = db;
	}
	
	public Borrower login(int bid, String password) throws SQLException {
		ResultSet rs = db.selectBorrowerByIdAndPassword(bid, password);
		currBorrower = null;
		while (rs.next()) {
			currBorrower = Borrower.getInstance(rs);
		}
		return currBorrower;
	}
	
	public void addBorrower() throws SQLException {
		if (currBorrower == null) {
			return;
		}
		
		String password;
		String name;
		String address;
		String phone;
		String email;
		String sinOrStNo;
		Date expiryDate;
		BorrowerType type;
		Boolean result;
		
		/*
		 * TODO: GUI inserts values for function
		result = insertBorrower(password, name, address,
				phone, email, sinOrStNo, expiryDate,
				type);
				*/
	}
	
	public void checkOutItems(int bid, Book[] books) throws SQLException {
		if (currBorrower == null) {
			return;
		}
		Date currDate;
	    Borrower aBorrower = null;
		
		ResultSet rs = db.selectBorrowerById(bid);
		
		while (rs.next()) {
			aBorrower = Borrower.getInstance(rs);
		}
		
		
		/*
		 * TODO: GUI checks out item
		insertBorrowing(bid, callNumber,
			copyNo, outDate, inDate);
			*/
	}
}
