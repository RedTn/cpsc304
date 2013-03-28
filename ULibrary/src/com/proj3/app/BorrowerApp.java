package com.proj3.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.Borrowing;
import com.proj3.model.Fine;
import com.proj3.model.HoldRequest;

public class BorrowerApp {
	private Database db;
	private Borrower currBorrower;

	public BorrowerApp(Database db) {
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

	public HoldRequest[] getHolds() throws SQLException {
		if (currBorrower == null) {
			return new HoldRequest[0];
		}

		ResultSet rs = db.selectHoldRequestsByBorrower(currBorrower.getId());

		List<HoldRequest> holds = new ArrayList<HoldRequest>();

		while (rs.next()) {
			ResultSet bookRs = db.selectBookByCallNumber(rs.getString("callNumber"));
			Book book = Book.getInstance(bookRs);
			
			holds.add(HoldRequest.getInstance(rs, book, currBorrower));
		}

		return (HoldRequest[]) holds.toArray();
	}
	
	public Fine[] getFines() throws SQLException {
		if (currBorrower == null) {
			return new Fine[0];
		}
		
		ResultSet rs = db.selectFineAndBorrowingByBorrower(currBorrower.getId());
		
		List<Fine> fines = new ArrayList<Fine>();
		
		while(rs.next()) {

			String callNumber = rs.getString("callNumber");
			int copyNo = rs.getInt("copyNo");
			
			ResultSet copyRs = db.selectCopyByCallAndCopyNumber(callNumber, copyNo);
			ResultSet bookRs = db.selectBookByCallNumber(callNumber);
			
			Book book = Book.getInstance(bookRs);
			BookCopy copy = BookCopy.getInstance(copyRs, book);

			fines.add(Fine.getInstance(rs, currBorrower, copy));
		}
		
		return (Fine[])fines.toArray();
	}
	
	public Borrowing[] getBorrowings() throws SQLException {
		if (currBorrower == null) {
			return new Borrowing[0];
		}

		ResultSet rs = db.selectUnreturnedBorrowingsByBorrower(currBorrower.getId());

		List<Borrowing> borrows = new ArrayList<Borrowing>();

		while (rs.next()) {
			String callNumber = rs.getString("callNumber");
			int copyNo = rs.getInt("copyNo");
			
			ResultSet copyRs = db.selectCopyByCallAndCopyNumber(callNumber, copyNo);
			ResultSet bookRs = db.selectBookByCallNumber(callNumber);
			
			Book book = Book.getInstance(bookRs);
			BookCopy copy = BookCopy.getInstance(copyRs, book);
			Borrowing b = Borrowing.getInstance(rs, currBorrower, copy);
			borrows.add(b);
		}

		return (Borrowing[]) borrows.toArray();
	}

	public Book[] searchBooksByKeyword(String keyword) throws SQLException {
		ResultSet rs = db.selectBooksByKeyword(keyword);

		Map<String, Book> books = new HashMap<String, Book>();
		while (rs.next()) {

			Book book;
			String callNumber = rs.getString("callNumber");
			if (!books.containsKey(callNumber)) {
				book = Book.getInstance(rs);
				books.put(book.getCallNumber(), book);
			} else {
				book = books.get(callNumber);
			}

			String author = rs.getString("name");
			if (!rs.wasNull()) {
				book.addAuthor(author);
			}

			String subject = rs.getString("subject");
			if (!rs.wasNull()) {
				book.addSubject(subject);
			}
		}

		for (Book b : books.values()) {
			String callNum = b.getCallNumber();
			ResultSet copies = db.selectBookCopiesByCallNumber(callNum);

			while (copies.next()) {
				b.addCopy(BookCopy.getInstance(copies, b));
			}
		}

		return (Book[]) books.values().toArray();
	}

}
