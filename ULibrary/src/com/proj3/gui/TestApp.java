package com.proj3.gui;

import com.proj3.app.BorrowerApp;
import com.proj3.app.LibrarianApp;
import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;
import com.proj3.model.HoldRequest;

public class TestApp {

	public static void main(String[] args) {
		Database db = new Database("passwordfile");
		db.connect();
		BorrowerApp app = new BorrowerApp(db);

		LibrarianApp app2 = new LibrarianApp(db);

		String callNumber = "TOLKIEN LordOTRings";
		String isbn = "47532639";
		String title = "Lord of the Rings";
		String mainAuthor = "Tolkien";
		String publisher = "someone";
		String year = "1984";

		Book newBook = new Book(); // new book

		newBook.setCallNumber(callNumber);
		newBook.setIsbn(isbn);
		newBook.setTitle(title);
		newBook.setMainAuthor(mainAuthor);
		newBook.setPublisher(publisher);
		newBook.setYear(year);

		boolean newBookAdded = app2.addNewBook(newBook);
		System.out.println(newBook.getTitle() + " man");

		if (newBookAdded) {
			Book newBookinDB = db.selectBookByCallNumber(callNumber);
			System.out.println(newBookinDB.getTitle());
		}
		
		CopyStatus newStatus = CopyStatus.in;
		int copyNo = 2;
		BookCopy newBookCopy = new BookCopy(newBook, copyNo, newStatus);
		boolean newCopyAdded = app2.addNewBookCopy(newBookCopy);
		
		if (newCopyAdded){
			BookCopy newBookCopyinDB = db.selectCopyByBookAndCopyNumber(newBook, copyNo);
			System.out.println(newBookCopyinDB.getBook().getTitle() + " COPY");
		}
		
		Borrower borrower = app.login(3, "123456");
		HoldRequest[] holds = app.getHolds();
		Borrowing[] borrows = app.getBorrowings();
		Book[] booksBySubject = app.searchBooksByKeyword("fiction");


	}
}
