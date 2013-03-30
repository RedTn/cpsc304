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

		if (newBookAdded) {
			Book newBookinDB = db.selectBookByCallNumber(callNumber);
			System.out.println(newBookinDB.getTitle());
		}
		else 
			System.out.println("Book already exists.");
		
		
		String callNumber2 = "MARTIN GameOFThronesFeast";
		String isbn2 = "475323523";
		String title2 = "Game of Thrones: A Feast For Crows";
		String mainAuthor2 = "Martin";
		String publisher2 = "someone";
		String year2 = "1984";

		Book unaddedBook = new Book(); // new book

		newBook.setCallNumber(callNumber2);
		newBook.setIsbn(isbn2);
		newBook.setTitle(title2);
		newBook.setMainAuthor(mainAuthor2);
		newBook.setPublisher(publisher2);
		newBook.setYear(year2);
		
		CopyStatus newStatus = CopyStatus.in;
		int copyNo = 3; // HOW TO MAKE THIS UNIQUE?
		//BookCopy newBookCopy = new BookCopy(newBook, copyNo, newStatus);
		BookCopy newBookCopy = new BookCopy(unaddedBook, copyNo, newStatus);
		boolean newCopyAdded = app2.addNewBookCopy(newBookCopy);
		
		if (newCopyAdded){
			BookCopy newBookCopyinDB = db.selectCopyByBookAndCopyNumber(newBook, copyNo);
			System.out.println(newBookCopyinDB.getBook().getTitle() + " COPY");
		}
		else 
			System.out.println("Book does not exist in database yet. Add new book instead.");
		
		Borrower borrower = app.login(3, "123456");
		HoldRequest[] holds = app.getHolds();
		Borrowing[] borrows = app.getBorrowings();
		Book[] booksBySubject = app.searchBooksByKeyword("fiction");


	}
}
