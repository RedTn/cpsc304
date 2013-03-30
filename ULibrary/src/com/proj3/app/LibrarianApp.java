package com.proj3.app;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;

public class LibrarianApp {
	private Database db;

	public LibrarianApp(Database db) {
		this.db = db;
	}

	public boolean addNewBook(Book book) {
		Book existBook = db.selectBookByCallNumber(book.getCallNumber());
		if (existBook != null) {
			throw new RuntimeException("Book already exists. Add new copy.");
			//return false;
		}
		return db.insertBook(book.getCallNumber(), book.getIsbn(),
				book.getTitle(), book.getMainAuthor(), book.getPublisher(),
				book.getYear());

	}

	public boolean addNewBookCopy(BookCopy bookCopy) {
		Book existBook = db.selectBookByCallNumber(bookCopy.getCallNumber());
		if (existBook == null) {
			//throw new RuntimeException();
			return false;
		}
		
		int copyNo = db.selectMaxCopyNumberForBook(existBook); 
		CopyStatus defaultStatus = CopyStatus.in;
		
		return db.insertBookCopy(bookCopy.getBook().getCallNumber(), copyNo+1, defaultStatus);

	}

	public Borrowing[] generateCheckedOutBooksReport() {
		Borrowing borrs[] = db.selectAllBorrowings();

		List<Borrowing> checkedOuts = new ArrayList<Borrowing>();

		for (int i = 0; i < borrs.length; i++) {
			if (borrs[i].getStatus() != CopyStatus.in) {
				checkedOuts.add(borrs[i]);
			}
		}

		return checkedOuts.toArray(new Borrowing[checkedOuts.size()]);
	}

	public Borrowing[] generateCheckedOutBooksReport(String subject) {
		Borrowing borrs[] = db.selectAllBorrowingsByKeyword(subject);

		List<Borrowing> checkedOuts = new ArrayList<Borrowing>();

		for (int i = 0; i < borrs.length; i++) {
			if (borrs[i].getStatus() != CopyStatus.in) {
				checkedOuts.add(borrs[i]);
			}
		}

		return checkedOuts.toArray(new Borrowing[checkedOuts.size()]);
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

	public Book[] generatePopularBooksReport(Date year, int n) {

		Borrowing[] borrows = db.selectBooksBorrowedInAYear(year);
		Map<Book, Integer> popularBooks = new HashMap<Book, Integer>();

		for (int i=0; i<borrows.length; i++) {
			Book book = borrows[i].getBook();
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
