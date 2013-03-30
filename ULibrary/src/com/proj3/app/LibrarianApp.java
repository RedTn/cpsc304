package com.proj3.app;

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
			return false;
		}
		return db.insertBook(book.getCallNumber(), book.getIsbn(),
				book.getTitle(), book.getMainAuthor(), book.getPublisher(),
				book.getYear());

	}

	public boolean addNewBookCopy(BookCopy bookCopy) {
		Book existBook = db.selectBookByCallNumber(bookCopy.getCallNumber());
		if (existBook == null) {
			return false;
		}
		
		int copyNo = db.selectMaxCopyNumberForBook(existBook); 
		
		return db.insertBookCopy(bookCopy.getBook().getCallNumber(), copyNo+1, bookCopy.getStatus());

	}

	public Borrowing[] generateCheckedOutBooksReport() {
		Borrowing borrs[] = db.selectAllUnreturnedBorrowings();

		List<Borrowing> checkedOuts = new ArrayList<Borrowing>();

		for (int i = 0; i < borrs.length; i++) {
			if (borrs[i].getCopy().getStatus() != CopyStatus.in) {
				checkedOuts.add(borrs[i]);
			}
		}

		return checkedOuts.toArray(new Borrowing[checkedOuts.size()]);
	}

	public BookCopy[] generateCheckedOutBooksReport(String subject) {
		Book books[] = db.selectBooksByKeyword(subject);

		List<BookCopy> checkedOutBooks = new ArrayList<BookCopy>();

		for (int i = 0; i < books.length; i++) {

			BookCopy[] copies = db.selectBookCopiesByBook(books[i]);
			for (int j = 0; j < copies.length; j++) {
				if (copies[i].getStatus() == CopyStatus.out) {
					checkedOutBooks.add(copies[i]);
					// if(book. indate = null && todays date > ){
					// flag as overdue
					// }
				}
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

	public Book[] generatePopularBooksReport(int year, int n) {

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
