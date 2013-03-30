package com.proj3.app;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.proj3.database.Database;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.BorrowerType;
import com.proj3.model.Borrowing;

public class ClerkApp {
	private Database db;
	private Borrower currBorrower;
	private Calendar cal;

	public ClerkApp(Database db) {
		this.db = db;

		// Automatically initialized to current date
		cal = Calendar.getInstance();

		// Due date for current books are a week before
		cal.add(Calendar.DATE, -7);
	}
	
	public void addBorrower() {
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
		 * TODO: GUI inserts values for function result =
		 * insertBorrower(password, name, address, phone, email, sinOrStNo,
		 * expiryDate, type);
		 */
	}

	public void checkOutItems(int bid, String[] callNumbers) {
		int numbooks;

		if (currBorrower == null) {
			return;
		}
		Borrower aBorrower = db.selectBorrowerById(bid);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Automatically initialized to current date
		Date currDate = new Date();
		// Format dates
		sdf.format(currDate);
		sdf.format(aBorrower.getExpiryDate());

		if (currDate.after(aBorrower.getExpiryDate())) {
			// Borrower id is expired
			return;
		}

		// //Determine if books are borrowable
		// TOFINISH
		numbooks = callNumbers.length;

		BookCopy bc = null;

		/*
		 * what is this? for (int i = 0; i < numbooks; i++) { rs =
		 * db.selectBookCopiesByCallNumber(callNumbers[i]); while (rs.next()) {
		 * bc = BookCopy.getInstance(rs, null);
		 * 
		 * } }
		 */
		// //

		// Add book to borrowing

		/*
		 * TODO: GUI checks out item for (int i = 0; i < numbooks, i++) {
		 * insertBorrowing(bid, callNumber[i], copyNo, outDate, inDate); }
		 */
	}

	public void processReturn(int borid) {
		/*
		 * if (currBorrower == null) { return; }
		 */
		Borrowing b = db.searchBorrowingsByClerk(borid);

		String callNumber = b.getCallNumber();
		Calendar borrowDate = Calendar.getInstance();
		borrowDate.setTime(b.getOutDate());

		if (borrowDate.getTime().before(cal.getTime())) {
			System.out.println("Expired");
			Date curDate = cal.getTime();
			Date bookDate = borrowDate.getTime();
			long curTime = curDate.getTime();
			long bookTime = bookDate.getTime();
			long diffTime = curTime - bookTime;
			long diffDays = diffTime / (1000 * 60 * 60 * 24);
			System.out.format("Differnce in days is: %d" + diffDays);
			float amount = diffDays * 1;
			if (!db.insertFine(amount, cal.getTime(), borid)) {
				System.out.println("Fine not inserted");
			}

		}

		/* I don't understand this
		 * ResultSet holdRs = db.selectHoldRequestsByCall(b.getBook());
		ResultSet copyRs = db.selectBookCopiesByBook(b.getBook());
		if (holdRs.next()) {
			// There is a hold
			if (copyRs.next()) {
				BookCopy bc = BookCopy.getInstance(rs, null);
				if (bc.getStatus().equals("out")) {
					if (!db.updateFirstCopyStatus("on-hold", callNumber)) {
						return;
					}
					// TODO: Send message to Borrower

				}
			}
		} else {
			// There is no hold
			if (copyRs.next()) {
				BookCopy bc = BookCopy.getInstance(rs, null);
				if (bc.getStatus().equals("out")) {
					if (!db.updateFirstCopyStatus("in", callNumber)) {
						return;
					}
				}
			}
		}
	*/
	}

	public Borrowing[] checkOverdueItems() {
		return db.selectOverDueBorrowingByDate(cal.getTime());

	}
}
