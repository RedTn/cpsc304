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
			currBorrower = new Borrower();
			currBorrower.setId(bid);
			currBorrower.setName(rs.getString("name"));
			currBorrower.setAddress(rs.getString("address"));
			currBorrower.setPhone(rs.getString("phone"));
			currBorrower.setEmail(rs.getString("emailAddress"));
			currBorrower.setSinOrStNo(rs.getString("sinOrStNo"));
			BorrowerType type = BorrowerType.get(rs.getString("type"));
			currBorrower.setType(type);
		}
		return currBorrower;
	}
	
	public void addBorrower() throws SQLException {
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
	
}
