package com.proj3.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;
import com.proj3.model.Fine;
import com.proj3.model.HoldRequest;

public class ClerkApp {
	private Database db;
	private Calendar cal;
	private Date currDate;
	private Calendar expCal;
	private String note;

	public ClerkApp(Database db) {
		this.db = db;
		this.note = null;

		// Automatically initialized to current date
		cal = Calendar.getInstance();
		expCal = Calendar.getInstance();
		currDate = new Date();

		// Due date for current books are a week before
		cal.add(Calendar.DATE, -7);
		expCal.add(Calendar.DATE, 7);
	}

	
	public String checkOutItems(int bid, String bookline) {
		if(("").equals(bookline)) {
			return "Please enter callNumbers";
		}
		List<String> books = new ArrayList<String>();
		Scanner scan = new Scanner(bookline);
		
		//WINDOWS
		//scan.useDelimiter(",|\\r\n");
		
		//UNIX
		scan.useDelimiter(",|\\n");
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
		
		Integer[] checkborrows = db.selectAllBorrowingsByBid(bid);
		for(int j = 0; j<checkborrows.length; j++) {
			Fine fine = db.selectFineByBorid(checkborrows[j].intValue());
			if (fine != null) {
				return "Borrower must pay fine of : $" + fine.getAmount();
			}
		}
		
		StringBuilder record = new StringBuilder();
		for (int i=0; i<books.size(); i++) {
			Book book = new Book();
			book.setCallNumber(books.get(i));
			HoldRequest[] hr = db.selectHoldRequestsByCall(book);
			if(hr.length == 0) {
				//This book is available
				Book checkbook = db.selectBookByCallNumber(book.getCallNumber());
				if(checkbook == null) {
					return "Error, Book: " + book.getCallNumber() + " is not in database, please check spelling";
				}
				BookCopy bc = db.selectCopyByCallAndStatus(book.getCallNumber(), CopyStatus.in);
				record.append("COPYNO: ");
				if(bc == null) {
					int count = db.getCopyCountByCallNumber(book.getCallNumber());
					count++;
					if(!db.insertBookCopy(book.getCallNumber(), count, CopyStatus.out)){
						return "Error, new bookcopy not inserted";
					}
					if(!db.insertBorrowing(bid, book.getCallNumber(),  count, 
							currDate, null)){
						return "Error, borrowing record not created(1)";
					}
					record.append(count);
				}
				else {
				if(!db.updateCopyStatus(CopyStatus.out, bc.getCopyNo(), book.getCallNumber())){
					return "Error, bookcopy not checked out";
				}
				
				if(!db.insertBorrowing(bid, book.getCallNumber(),  bc.getCopyNo(), 
						currDate, null)){
					return "Error, borrowing record not created(2)";
				}
				record.append(bc.getCopyNo());
				}
				
				record.append("; CALLNUMBER: ");
				record.append(book.getCallNumber());
				record.append("; CHECKEDOUT, DUE: ");
				Date formatedDate = expCal.getTime();
				record.append(formatedDate);
				record.append("\n");
			}
			else {
				record.append("BOOK: ");
				record.append(book.getCallNumber());
				record.append(", is currently on-hold\n");
			}

		}
		setNote(record.toString());
		return "Done check-out";
	}

	public String processReturn(int borid) {
		Borrowing b = db.searchBorrowingsByClerk(borid);
		

		String callNumber = b.getCallNumber();
		Calendar borrowDate = Calendar.getInstance();
		borrowDate.setTime(b.getOutDate());

		StringBuilder record = new StringBuilder();
		
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
				return "Fine not inserted";
			}
		}

		BookCopy[] bc = db.selectBookCopiesByBookAndStatus(b.getBook(), CopyStatus.out);
		HoldRequest[] hold = db.selectHoldRequestsByCall(b.getBook());
		if((hold.length == 0) && (bc.length != 0)) {
			if(!db.updateFirstCopyStatus(CopyStatus.in, callNumber)) {
				return "Error, bookcopy not updated(1)";
			}
			if(!db.updateBorrowingByIndate(borid,currDate)){
				return "Error, borrowing record not inserted";
			}
			
		}else if ((hold.length != 0) && (bc.length != 0)){
			if(!db.updateFirstCopyStatus(CopyStatus.onhold, callNumber)) {
				return "Error, bookcopy not updated(2)";
			}
			Borrower borrower = db.selectBorrowerById(b.getBid());
			//TODO: GUI sends message to borrower who made hold request

		}
		else {
		Book book = b.getBook();
		 BookCopy[] inbooks = db.selectBookCopiesByBookAndStatus(b.getBook(), CopyStatus.in);
		int lastindex = inbooks.length;
		lastindex++;
			if(!db.insertBookCopy(callNumber, lastindex, CopyStatus.in)) {
				return "Insert new bookcopy failed";
			}
		}
		return "Done processing returns";

	}



	public Borrowing[] checkOverdueItems() {
		return db.selectOverDueBorrowingByDate(cal.getTime());

	}
	private void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
}
