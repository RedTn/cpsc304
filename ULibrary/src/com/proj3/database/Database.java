package com.proj3.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import oracle.sql.DATE;

import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.BorrowerType;
import com.proj3.model.CopyStatus;
//import java.awt.GridLayout;

public class Database {
	private String address, username, password;
	private Connection con;
	private PreparedStatement ps;

	public Database() {
		try {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
	}

	public Database(String address, String username, String password) {
		this();
		this.address = address;
		this.username = username;
		this.password = password;

	}

	public Database(String filePath) {
		this();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));

			String line = br.readLine();

			if (line != null) {
				this.address = line;
				line = br.readLine();
			}

			if (line != null) {
				this.username = line;
				line = br.readLine();
			}

			if (line != null) {
				this.password = line;
			}

			br.close();
		} catch (IOException e) {
			System.out.print(e.getMessage());
		} finally {
		}
	}

	public boolean connect() {

		try {
			con = DriverManager.getConnection(address, username, password);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			return false;
		}
	}

	public boolean insertBorrower(String password, String name, String address,
			String phone, String email, String sinOrStNo, Date expiryDate,
			BorrowerType type) {
		try {
			ps = con.prepareStatement("INSERT INTO Borrower VALUES (bid_counter.nextval,?,?,?,?,?,?,?,?)");

			ps.setString(1, password);

			ps.setString(2, name);

			ps.setString(3, address);

			ps.setString(4, phone);

			ps.setString(5, email);

			ps.setString(6, sinOrStNo);

			ps.setDate(7, (java.sql.Date) expiryDate);

			ps.setString(8, type.getType());

			ps.executeUpdate();

			// commit work
			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			try {
				// undo the insert
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}
		return false;
	}

	public boolean insertBook(String callNumber, String isbn, String title,
			String mainAuthor, String publisher, String year) {
		// yet to be implemented
		return false;
	}

	public boolean insertHasAuthor(String callNumber, String authorName) {

		return false;
	}

	public boolean insertHasSubject(String callNumber, String subject) {

		return false;
	}

	public boolean insertBookCopy(String callNumber, String copyNo,
			CopyStatus status) {
		return false;
	}

	public boolean insertHoldRequest(String bid, String callNumber,
			DATE issuedDate) {
		return false;
	}

	public boolean insertBorrowing(String bid, String callNumber,
			String copyNo, DATE outDate, DATE inDate) {
		return false;
	}

	public boolean insertFine(double amount, DATE issuedDate, DATE paidDate,
			String borid) {
		return false;
	}
	
	public Book[] searchBooksByKeyword(String keyword) {
		ResultSet rs;
		
		try {
			ps = con.prepareStatement("SELECT * FROM Book b, HasAuthor a, HasSubject s WHERE b.callNumber = a.callNumber AND b.callNumber = s.callNumber AND (b.title LIKE '%?%' OR b.mainAuthor LIKE '%?%' OR a.name LIKE '%?%' OR s.subject '%?%')");

			ps.setString(1, keyword);
			ps.setString(2, keyword);
			ps.setString(3, keyword);
			ps.setString(4, keyword);
			rs = ps.executeQuery();

			ps.close();
			
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
			
			return (Book[])books.values().toArray();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			try {
				// undo the insert
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}
		return new Book[0];
		
	}
	
	public BookCopy[] searchForBookCopiesByCallNumber(String callNumber) {
		return new BookCopy[0];
	}
}
