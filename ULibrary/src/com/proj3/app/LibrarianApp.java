package com.proj3.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.CopyStatus;

public class LibrarianApp {
	private Database db;

	public LibrarianApp(Database db) {
		this.db = db;
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

				b.addCopy(new BookCopy(callNum, copyNo, status));
			}
		}

		return (Book[]) books.values().toArray();
	}
}
