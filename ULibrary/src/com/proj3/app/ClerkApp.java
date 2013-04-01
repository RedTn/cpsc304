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
	private String email;

	public ClerkApp(Database db) {
		this.db = db;
		this.note = null;
		this.email = null;

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
		try {
			while(scan.hasNext()){
				books.add(scan.next());
			}
		}finally {
			scan.close();
		}
		
		Borrower aBorrower = db.selectBorrowerById(bid);
		if (aBorrower == null) {
			return "No Borrower found.";
		}
		if (currDate.after(aBorrower.getExpiryDate())){
			return "Borrower is expired.";
		}

		Integer[] checkborrows = db.selectAllBorrowingsByBid(bid);
		for(int j = 0; j<checkborrows.length; j++) {
			Fine fine = db.selectFineByBorid(checkborrows[j].intValue());
			if (fine != null) {
				return "Borrower must pay fine of : $" + String.format("%.2f", fine.getAmount());
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
					return "Error, Book: " + book.getCallNumber() + " is not in database, please check spelling.";
				}
				BookCopy bc = db.selectCopyByCallAndStatus(book.getCallNumber(), CopyStatus.in);
				record.append("COPYNO: ");
				if(bc == null) {
					int count = db.getCopyCountByCallNumber(book.getCallNumber());
					count++;
					if(!db.insertBookCopy(book.getCallNumber(), count, CopyStatus.out)){
						return "Error, new bookcopy not inserted.";
					}
					if(!db.insertBorrowing(bid, book.getCallNumber(),  count, 
							currDate, null)){
						return "Error, borrowing record not created(1).";
					}
					record.append(count);
				}
				else {
					if(!db.updateCopyStatus(CopyStatus.out, bc.getCopyNo(), book.getCallNumber())){
						return "Error, bookcopy not checked out.";
					}

					if(!db.insertBorrowing(bid, book.getCallNumber(),  bc.getCopyNo(), 
							currDate, null)){
						return "Error, borrowing record not created(2).";
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
				record.append(", is currently on-hold.\n");
			}

		}
		setNote(record.toString());
		return "Done check-out";
	}

	public String processReturn(int borid) {
		Borrowing b = db.searchBorrowingsByClerk(borid);
		if (b == null) {
			return "Borid is invalid, or book is already returned.";
		}

		Borrower borrower = db.selectBorrowerById(b.getBid());
		String callNumber = b.getCallNumber();
		Date borrowDate = new Date();
		borrowDate = b.getOutDate();

		StringBuilder sb = new StringBuilder();

		if (borrowDate.before(cal.getTime())) {

			Date curDate = cal.getTime();
			Date bookDate = borrowDate;
			long curTime = curDate.getTime();
			long bookTime = bookDate.getTime();
			long diffTime = curTime - bookTime;
			long diffDays = diffTime / (1000 * 60 * 60 * 24);
			float amount = diffDays * 1;
			if (!db.insertFine(amount, cal.getTime(), null, borid)) {
				return "Fine not inserted.";
			}
			sb.append("Overdue, Fine of $");
			sb.append(String.format("%.2f", amount));
			sb.append(".\n");
		}

		BookCopy bc = db.selectCopy(callNumber, CopyStatus.out,  b.getCopy().getCopyNo());
		HoldRequest[] hold = db.selectHoldRequestsByCall(b.getBook());
		if((hold.length == 0) && (bc != null)) {
			if(!db.updateCopyStatus(CopyStatus.in, b.getCopy().getCopyNo(), callNumber)) {
				return "Error, bookcopy not updated(1).";
			}
			if(!db.updateBorrowingByIndate(borid,currDate)){
				return "Error, borrowing record not inserted(1).";
			}
			sb.append("Book returned.");
		}else if ((hold.length != 0) && (bc != null)){
			if(!db.updateCopyStatus(CopyStatus.onhold, b.getCopy().getCopyNo(), callNumber)) {
				return "Error, bookcopy not updated(2).";
			}
			if(!db.updateBorrowingByIndate(borid,currDate)){
				return "Error, borrowing record not inserted(2).";
			}
			sb.append("Returned.\nBOOK: " + callNumber + "\nis available for holder\nBID:" + b.getBid());
			if(borrower == null){
				return "Error, borrower BID not found.";
			}
			else{
				setEmail(borrower.getEmail());
			}
		}
		else if ((hold.length == 0) && (bc == null)){
			int inbooks = db.getCopyCountByCallNumber(callNumber);
			inbooks++;
			if(!db.insertBookCopy(callNumber, inbooks, CopyStatus.in)) {
				return "Insert new bookcopy failed.";
			}
			if(!db.updateBorrowingByIndate(borid,currDate)){
				return "Error, borrowing record not inserted(2).";
			}
			sb.append("New bookcopy inserted\ncopyNo: " + inbooks);
		}
		else {
			int inbooks = db.getCopyCountByCallNumber(callNumber);
			inbooks++;
			if(!db.insertBookCopy(callNumber, inbooks, CopyStatus.onhold)) {
				return "Insert new bookcopy failed.";
			}
			if(!db.updateBorrowingByIndate(borid,currDate)){
				return "Error, borrowing record not inserted(2).";
			}
			sb.append("New bookcopy inserted\ncopyNo: " + inbooks + "\nBOOK: "+ callNumber + "\nis available for holder\nBID:" + b.getBid());
			if(borrower == null){
				return "Error, borrower BID not found.";
			}
			else{
				setEmail(borrower.getEmail());
			}
		}
		sb.append("\nEND");
		return sb.toString();
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
	private void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}
