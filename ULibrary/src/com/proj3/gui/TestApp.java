package com.proj3.gui;

import java.sql.Date;

import com.proj3.app.BorrowerApp;
import com.proj3.app.LibrarianApp;
import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;

public class TestApp {

	public static void main(String[] args) {
		Database db = new Database("passwordfile");
		db.connect();
		BorrowerApp app = new BorrowerApp(db);
		LibrarianApp app2 = new LibrarianApp(db);

		
		// TEST: ADD NEW BOOK
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
			System.out.println("New Book Added:" + newBookinDB.getTitle());
		}
		else 
			System.out.println("Book already exists.");
		
		// TEST: ADD NEW BOOK COPY
	 	
		String callNumber2 = "MARTIN GameOFThronesFeast";
		String isbn2 = "475323523";
		String title2 = "Game of Thrones: A Feast For Crows";
		String mainAuthor2 = "Martin";
		String publisher2 = "someone";
		String year2 = "1984";

		Book unaddedBook = new Book(); // new book

		unaddedBook.setCallNumber(callNumber2);
		unaddedBook.setIsbn(isbn2);
		unaddedBook.setTitle(title2);
		unaddedBook.setMainAuthor(mainAuthor2);
		unaddedBook.setPublisher(publisher2);
		unaddedBook.setYear(year2);
		
		CopyStatus newStatus = CopyStatus.in;
		int copyNo = 4; // HOW TO MAKE THIS UNIQUE? (should just get replaced)
		
		BookCopy newBookCopy = new BookCopy(newBook, copyNo, newStatus);
		boolean newCopyAdded = app2.addNewBookCopy(newBookCopy);
		if (newCopyAdded){
			BookCopy newBookCopyinDB = db.selectCopyByBookAndCopyNumber(newBook, copyNo);
			System.out.println(newBookCopyinDB.getBook().getTitle() + " COPY");
		}
		else 
			System.out.println("Book does not exist in database yet. Add new book instead.");
		

		BookCopy newUnaddedBookCopy = new BookCopy(unaddedBook, copyNo, newStatus); // should not work
		boolean newUnaddedCopyAdded = app2.addNewBookCopy(newUnaddedBookCopy);
		if (newUnaddedCopyAdded){
			BookCopy newUnaddedBookCopyinDB = db.selectCopyByBookAndCopyNumber(newBook, copyNo);
			System.out.println(newUnaddedBookCopyinDB.getBook().getTitle() + " COPY");
		}
		else 
			System.out.println("Unadded Book does not exist in database yet. Add new book instead.");
		
		
		// TEST: GENERATE CHECKED OUT BOOKS REPORT
		
		Borrowing[] bookReport = app2.generateCheckedOutBooksReport();
		for (int i = 0; i < bookReport.length; i++){
			System.out.println(bookReport[i].getBook().getTitle());
		}
		
		Borrowing[] bookKeywordReport = app2.generateCheckedOutBooksReport("fiction");
		for (int i = 0; i < bookKeywordReport.length; i++){
			System.out.println(bookKeywordReport[i].getBook().getTitle());
		}
		
	
		//Date newDate = new Date(2017, 10, 21);
		//Book[] popularBooks = app2.generatePopularBooksReport(newDate, 2);
		//for (int i = 0; i < popularBooks.length; i++){
			//System.out.println(popularBooks[i].getTitle());
		//}
		
		//Borrower borrower = app.login(4001, "123456");
		//HoldRequest[] holds = app.getHolds();
		//Borrowing[] borrows = app.getBorrowings();
		//Book[] booksBySubject = app.searchBooksByKeyword("fiction");


	}
}
