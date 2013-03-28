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
	
	public Borrowing[] getBorrowings() throws SQLException {
		if (currBorrower == null) {
			return new Borrowing[0];
		}
		
		ResultSet rs = db.searchBorrowingsByBorrower(currBorrower.getId());
		
		List<Borrowing> borrows = new ArrayList<Borrowing>();
		
		while (rs.next()) {
			Borrowing b = new Borrowing();
			
			int borid = rs.getInt("borid");
			String callNumber = rs.getString("callNumber");
			int copyNo = rs.getInt("copyNo");
			Date outDate = rs.getDate("outDate");
			
			Calendar cal = Calendar.getInstance();
	        cal.setTime(outDate);
	        cal.add(Calendar.DATE, currBorrower.getType().getBorrowingLimit()); //minus number would decrement the days
	        Date inDate = cal.getTime();
	        
			b.setBid(currBorrower.getId());
			b.setBorid(borid);
			b.setCallNumber(callNumber);
			b.setCopyNo(copyNo);
			b.setOutDate(outDate);
			b.setInDate(inDate);
			
			borrows.add(b);
		}
		
		return (Borrowing[])borrows.toArray();
	}
	
	public Book[] searchBooksByKeyword(String keyword) throws SQLException {
		ResultSet rs = db.searchBooksByKeyword(keyword);

		Map<String, Book> books = new HashMap<String, Book>();
		while (rs.next()) {
			String callNumber = rs.getString("callNumber");
			Book book;
			if (!books.containsKey(callNumber)) {
				book = new Book(callNumber);

				String isbn = rs.getString("isbn");
				String title = rs.getString("title");
				String mainAuthor = rs.getString("mainAuthor");
				String publisher = rs.getString("publisher");
				String year = rs.getString("year");

				book.setIsbn(isbn);
				book.setTitle(title);
				book.setMainAuthor(mainAuthor);
				book.setPublisher(publisher);
				book.setYear(year);
				books.put(callNumber, book);
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
			ResultSet copies = db.searchForBookCopiesByCallNumber(callNum);

			while (copies.next()) {
				int copyNo = copies.getInt("copyNo");
				CopyStatus status = CopyStatus.get(rs.getString("status"));

				b.addCopy(new BookCopy(b, copyNo, status));
			}
		}

		return (Book[]) books.values().toArray();
	}
	
	
}
