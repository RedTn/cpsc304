package com.proj3.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java.sql.Timestamp;

import com.proj3.database.Database;
import com.proj3.gui.ClerkUICheckOut;
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

	
	public String checkOutItems(int bid, String bookline) {
		List<String> books = new ArrayList<String>();
		Scanner scan = new Scanner(bookline);
		
		//WINDOWS
		scan.useDelimiter(",|\\r\n");
		
		//UNIX
		//scan.useDelimiter((",|\\n");
		 while(scan.hasNext()){
			 books.add(scan.next());
	 }
		 
		Borrower aBorrower = db.selectBorrowerById(bid);
		if (aBorrower == null) {
			return "No Borrower found";
		}
		if (currDate.after(aBorrower.getExpiryDate())){
			return "Error, borrower is expired";
		}

		for (int i=0; i<books.size(); i++) {
			Book book = new Book();
			book.setCallNumber(books.get(i));
			HoldRequest[] hr = db.selectHoldRequestsByCall(book);
			if(hr.length == 0) {
				//This book is available
				BookCopy bc = db.selectCopyByCallAndStatus(book.getCallNumber(), CopyStatus.in);
				
				if(!db.updateCopyStatus(CopyStatus.out, bc.getCopyNo(), book.getCallNumber())){
					System.out.println("Error, bookcopy not checked out");
				}
				if(!db.insertBorrowing(bid, book.getCallNumber(),  bc.getCopyNo(), 
						cal.getTime(), null)){
					System.out.println("Error, borrowing record not created");
				}
				
				//TODO: Gui prints THIS borrowing record
				//and expiry date: expCal.getTime();
			}
			else {
				return "Book %s is on-hold" + book.getCallNumber();
			}

		}
		return "Done check-out";
	}

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
