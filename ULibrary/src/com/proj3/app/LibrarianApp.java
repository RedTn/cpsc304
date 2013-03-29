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
import com.proj3.model.CopyStatus;

public class LibrarianApp {
	private Database db;

	// private Borrower currBorrower;

	public LibrarianApp(Database db) {
		this.db = db;
	}

	public boolean addNewBook(Book book) {
		return db.insertBook(book.getCallNumber(), book.getIsbn(),
				book.getTitle(), book.getMainAuthor(), book.getPublisher(),
				book.getYear());

	}

	public boolean addNewBookCopy(BookCopy bookCopy) {
		// return db.insertBookCopy(bookCopy.gwfscallNumber,
		// bookCopy.getCopyNo(),
		// bookCopy.getStatus());
		return true;  // just for now, not actually

	}

	public BookCopy[] generateCheckedOutBooksReport() throws SQLException {
		ResultSet rs = db.selectAllBooks();
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
	
	public BookCopy[] generateCheckedOutBooksReport(String subject) throws SQLException {
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
	
	public Book[] generatePopularBooksReport(int year, int n) throws SQLException{
		
		// TO DO: fix implementation
		
		ResultSet rs = db.selectBooksBorrowedInAYear(year);
		List<Book> allBooks = new ArrayList<Book>();
		Map<Integer, Book> popularBooks = new HashMap<Integer, Book>();

		while (rs.next()) {
			Book book = Book.getInstance(rs);
			BookCopy copy = BookCopy.getInstance(rs, book);
			for ( Book b : allBooks ){
				if (book.getTitle() == b.getTitle()) {
					//Integer i = allBooks.get(b); -- HOW TO USE HASHMAP PROPERLY
				}
				else {
					allBooks.add(book);
				}
			}
		}
		
				//popularBooks.add(book);  -- for the top n integers add the books
			
		// print list popular books in order from most borrowed to least

		return (Book[]) allBooks.toArray(); // WRONG 
		
	}

}
