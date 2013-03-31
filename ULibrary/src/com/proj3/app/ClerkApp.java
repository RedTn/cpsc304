package com.proj3.app;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import java.sql.Timestamp;

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
	private Calendar cal;
	private Date currDate;
	private Calendar expCal;

	public ClerkApp(Database db) {
		this.db = db;

		// Automatically initialized to current date
		cal = Calendar.getInstance();
		expCal = Calendar.getInstance();
		currDate = new Date();

		// Due date for current books are a week before
		cal.add(Calendar.DATE, -7);
		expCal.add(Calendar.DATE, 7);
	}

	/*
	public boolean checkOutItems(int bid, String bookline) {
		Scanner scan = new Scanner(bookline);
		scan.useDelimiter(",|\\n");
		 while(scan.hasNext()){
	          System.out.println(scan.next());

	 }
		 
		Borrower aBorrower = db.selectBorrowerById(bid);

		if (currDate.after(aBorrower.getExpiryDate())){
			System.out.println("Error, borrower is expired");
			return false;
		}

		for (int i=0; i<books.length; i++) {
			HoldRequest[] hr = db.selectHoldRequestsByCall(books[i]);
			if(hr.length == 0) {
				//This book is available
				BookCopy bc = db.selectCopyByCallAndStatus(books[i].getCallNumber(), CopyStatus.in);
				
				if(!db.updateCopyStatus(CopyStatus.out, bc.getCopyNo(), books[i].getCallNumber())){
					System.out.println("Error, bookcopy not checked out");
				}
				if(!db.insertBorrowing(bid, books[i].getCallNumber(),  bc.getCopyNo(), 
						cal.getTime(), null)){
					System.out.println("Error, borrowing record not created");
				}
				
				//TODO: Gui prints THIS borrowing record
				//and expiry date: expCal.getTime();
			}
			else {
				System.out.format("Book %s is on-hold", books[i].getCallNumber());
				return false;
			}

		}
		return true;
	}
*/
	public boolean processReturn(int borid) {
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
			System.out.println("Differnce in days is:");
			System.out.format("%d%n", diffDays);
			float amount = diffDays * 1;
			if (!db.insertFine(amount, cal.getTime(), null, borid)) {
				System.out.println("Fine not inserted");
				return false;
			}
		}

		BookCopy[] bc = db.selectBookCopiesByBookAndStatus(b.getBook(), CopyStatus.out);
		HoldRequest[] hold = db.selectHoldRequestsByCall(b.getBook());
		if((hold.length == 0) && (bc.length != 0)) {
			if(!db.updateFirstCopyStatus(CopyStatus.in, callNumber)) {
				System.out.println("Error, bookcopy not updated(1)");
				return false;
			}
			if(!db.updateBorrowingByIndate(borid,currDate)){
				System.out.println("Error, borrowing record not inserted");
				return false;
			}
			
		}else if ((hold.length != 0) && (bc.length != 0)){
			if(!db.updateFirstCopyStatus(CopyStatus.onhold, callNumber)) {
				System.out.println("Error, bookcopy not updated(2)");
				return false;
			}
			Borrower borrower = db.selectBorrowerById(b.getBid());
			//TODO: GUI sends message to borrower who made hold request

		}
		else {
		 BookCopy[] inbooks = db.selectBookCopiesByBookAndStatus(b.getBook(), CopyStatus.in);
		int lastindex = inbooks.length;
		lastindex++;
			if(!db.insertBookCopy(callNumber, lastindex, CopyStatus.in)) {
				System.out.println("Insert new bookcopy failed");
				return false;
			}
		}
		return true;

	}



	public Borrowing[] checkOverdueItems() {
		return db.selectOverDueBorrowingByDate(cal.getTime());

	}
}
