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
import com.proj3.model.CopyStatus;

public class LibrarianApp {
	private Database db;

	// private Borrower currBorrower;

	public LibrarianApp(Database db) {
		this.db = db;
	}

	public boolean addNewBook(Book book) throws SQLException {
		ResultSet rs = db.selectBookByCallNumber(book.getCallNumber());
		if (rs.next()) {
			return false;
		}
		return db.insertBook(book.getCallNumber(), book.getIsbn(),
				book.getTitle(), book.getMainAuthor(), book.getPublisher(),
				book.getYear());

	}

	public boolean addNewBookCopy(BookCopy bookCopy) {
		// return db.insertBookCopy(bookCopy.gwfscallNumber,
		// bookCopy.getCopyNo(),
		// bookCopy.getStatus());
		return true; // just for now, not actually

	}

	public Borrowing[] generateCheckedOutBooksReport() throws SQLException {
		ResultSet rs = db.selectAllUnreturnedBorrowings();
		
		List<Borrowing> checkedOuts = new ArrayList<Borrowing>();

		while (rs.next()) {
			String callNumber = rs.getString("callNumber");
			int copyNo = rs.getInt("copyNo");
			int bid = rs.getInt("bid");
			
			ResultSet bookRs = db.selectBookByCallNumber(callNumber);
			ResultSet copyRs = db.selectCopyByCallAndCopyNumber(callNumber, copyNo);
			ResultSet borrowerRs = db.selectBorrowerById(bid);
			
			Book book = Book.getInstance(bookRs);
			BookCopy copy = BookCopy.getInstance(copyRs, book);
			Borrower borrower = Borrower.getInstance(borrowerRs);
			
			if (copy.getStatus() != CopyStatus.in) {
				Borrowing borr =Borrowing.getInstance(rs, borrower, copy);
				checkedOuts.add(borr);
			}
		}

		return (Borrowing[]) checkedOuts.toArray();
	}

	public BookCopy[] generateCheckedOutBooksReport(String subject)
			throws SQLException {
		ResultSet rs = db.selectBooksByKeyword(subject);
		CopyStatus checkedOut = CopyStatus.out;
		List<BookCopy> checkedOutBooks = new ArrayList<BookCopy>();

		while (rs.next()) {
			Book book = Book.getInstance(rs);
			BookCopy copy = BookCopy.getInstance(rs, book);
			if (copy.getStatus() == checkedOut) {
				checkedOutBooks.add(copy);
				// if(book. indate = null && todays date > ){
				// flag as overdue
				// }
			}
		}
		// print list of each book with DATE CHECKED OUT and DUE DATE

		return (BookCopy[]) checkedOutBooks.toArray();
	}

	private Book[] getTopNBooks(Map<Book, Integer> popularBooks, int n) {
		Map<Integer, ArrayList<Book>> sorted = new HashMap<Integer, ArrayList<Book>>();

		int max = 0;
		for (Book b : popularBooks.keySet()) {
			int times = popularBooks.get(b);

			if (times > max) {
				max = times;
			}

			ArrayList<Book> listOfBooks = sorted.get(times);
			if (listOfBooks == null) {
				listOfBooks = new ArrayList<Book>();
				sorted.put(times, listOfBooks);
			}
			listOfBooks.add(b);
		}

		Book[] sortedBooks = new Book[n];
		int j = 0;
		for (int i = max; i >= 0; i--) {
			ArrayList<Book> list = sorted.get(i);
			if (list != null) {
				for (Book b : list) {
					sortedBooks[j++] = b;
					if (j == n) {
						break;
					}
				}
			}
		}

		return sortedBooks;
	}

	public Book[] generatePopularBooksReport(int year, int n)
			throws SQLException {

		ResultSet rs = db.selectBooksBorrowedInAYear(year);
		Map<Book, Integer> popularBooks = new HashMap<Book, Integer>();
		
		while (rs.next()) {
			String callNumber = rs.getString("callNumber");
			ResultSet bookRs = db.selectBookByCallNumber(callNumber);
			Book book = Book.getInstance(bookRs);

			if (popularBooks.containsKey(book)) {
				int timesCheckedOut = popularBooks.get(book);
				popularBooks.put(book, timesCheckedOut + 1);

			} else {
				popularBooks.put(book, 1);
			}

		}

		return getTopNBooks(popularBooks, n);


	}

}
