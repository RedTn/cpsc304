package com.proj3.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.proj3.model.Book;
import com.proj3.model.BookCopy;
import com.proj3.model.Borrower;
import com.proj3.model.BorrowerType;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;
import com.proj3.model.Fine;
import com.proj3.model.HoldRequest;

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

	// Clerk Transaction
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

			ps.setDate(7, new java.sql.Date(expiryDate.getTime()));

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
		try {
			ps = con.prepareStatement("INSERT INTO Book VALUES (?,?,?,?,?,?)");

			ps.setString(1, callNumber);

			ps.setString(2, isbn);

			ps.setString(3, title);

			ps.setString(4, mainAuthor);

			ps.setString(5, publisher);

			ps.setString(6, year);

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

	public boolean insertHasAuthor(String callNumber, String authorName) {

		try {
			ps = con.prepareStatement("INSERT INTO HasAuthor VALUES (?,?)");

			ps.setString(1, callNumber);

			ps.setString(2, authorName);

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

	public boolean insertHasSubject(String callNumber, String subject) {

		try {
			ps = con.prepareStatement("INSERT INTO HasSubject VALUES (?,?)");

			ps.setString(1, callNumber);

			ps.setString(2, subject);

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

	public boolean insertBookCopy(String callNumber, int copyNo,
			CopyStatus status) {
		try {
			ps = con.prepareStatement("INSERT INTO BookCopy VALUES (?,?,?)");

			ps.setString(1, callNumber);

			ps.setInt(2, copyNo);

			ps.setString(3, status.getStatus());

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

	public boolean insertHoldRequest(int bid, String callNumber, Date issuedDate) {
		try {
			ps = con.prepareStatement("INSERT INTO HoldRequest VALUES (hid_counter.nextval,?,?,?)");

			ps.setInt(1, bid);

			ps.setString(2, callNumber);

			ps.setDate(3, new java.sql.Date(issuedDate.getTime()));

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

	// Clerk transaction
	public boolean insertBorrowing(int bid, String callNumber,
			int copyNo, Date outDate, Date inDate) {
		try {
			ps = con.prepareStatement("INSERT INTO Borrowing VALUES (borid_counter.nextval,?,?,?,?,?)");

			ps.setInt(1, bid);

			ps.setString(2, callNumber);

			ps.setInt(3, copyNo);

			ps.setDate(4, new java.sql.Date(outDate.getTime()));

			if (inDate != null) {
			ps.setDate(5, new java.sql.Date (inDate.getTime()));
			}
			else{
				ps.setNull(5, java.sql.Types.DATE);
			}

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

	public boolean insertFine(float amount, Date issuedDate, Date paidDate, int borid) {
		try {
			ps = con.prepareStatement("INSERT INTO Fine VALUES (fid_counter.nextval,?,?,?,?)");

			ps.setFloat(1, amount);

			ps.setDate(2, new java.sql.Date(issuedDate.getTime()));

			if(paidDate != null){
			ps.setDate(3, new java.sql.Date(paidDate.getTime()));
			}
			else{
				ps.setNull(3, java.sql.Types.DATE);
			}

			ps.setInt(4, borid);

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

	public boolean deleteBorrower(int bid) {
		try {
			ps = con.prepareStatement("DELETE FROM Borrower WHERE bid = ?");

			ps.setInt(1, bid);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteBook(String callNumber) {
		try {
			ps = con.prepareStatement("DELETE FROM Book WHERE callNumber = ?");

			ps.setString(1, callNumber);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteHasAuthor(String callNumber, String name) {
		try {
			ps = con.prepareStatement("DELETE FROM HasAuthor WHERE callNumber = ? AND name = ?");

			ps.setString(1, callNumber);

			ps.setString(2, name);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteHasSubject(String callNumber, String subject) {
		try {
			ps = con.prepareStatement("DELETE FROM HasSubject WHERE callNumber = ? AND subject = ?");

			ps.setString(1, callNumber);

			ps.setString(2, subject);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteBookCopy(String callNumber, int copyNo) {
		try {
			ps = con.prepareStatement("DELETE FROM BookCopy WHERE callNumber = ? AND copyNo = ?");

			ps.setString(1, callNumber);

			ps.setInt(2, copyNo);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteHoldRequest(int hid) {
		try {
			ps = con.prepareStatement("DELETE FROM HoldRequest WHERE hid = ?");

			ps.setInt(1, hid);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteBorrowing(int borid) {
		try {
			ps = con.prepareStatement("DELETE FROM Borrowing WHERE borid = ?");

			ps.setInt(1, borid);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public boolean deleteFine(int fid) {
		try {
			ps = con.prepareStatement("DELETE FROM Fine WHERE fid = ?");

			ps.setInt(1, fid);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				// Does not exist
				return false;
			}

			con.commit();

			ps.close();

			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
			return false;
		}
	}

	public Book[] selectBooksByKeyword(String keyword) {
		ResultSet rs = null;

		Set<Book> books = new HashSet<Book>();

		String kwRegex = "%" + keyword + "%";
		try {
			ps = con.prepareStatement("SELECT * FROM Book WHERE title LIKE ? OR mainAuthor LIKE ? ");

			ps.setString(1, kwRegex);
			ps.setString(2, kwRegex);

			rs = ps.executeQuery();

			while (rs.next()) {
				Book book = Book.getInstance(rs);

				if (!books.contains(book)) {
					book = constructBook(rs);
					books.add(book);
				}
			}

			ps.close();

			ps = con.prepareStatement("SELECT * FROM Book b, HasAuthor a WHERE name LIKE ? AND b.callNumber = a.callNumber ");

			ps.setString(1, kwRegex);

			rs = ps.executeQuery();

			while (rs.next()) {
				Book book = Book.getInstance(rs);

				if (!books.contains(book)) {
					book = constructBook(rs);
					books.add(book);
				}
			}

			ps.close();
			ps = con.prepareStatement("SELECT * FROM Book b, HasSubject s WHERE subject LIKE ? AND b.callNumber = s.callNumber ");

			ps.setString(1, kwRegex);

			rs = ps.executeQuery();

			while (rs.next()) {
				Book book = Book.getInstance(rs);

				if (!books.contains(book)) {
					book = constructBook(rs);
					books.add(book);
				}
			}

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

		return books.toArray(new Book[books.size()]);
	}

	public BookCopy[] selectBookCopiesByBook(Book book) {
		ResultSet rs = null;
		List<BookCopy> copies = new ArrayList<BookCopy>();

		try {
			ps = con.prepareStatement("SELECT copyNo, status FROM BookCopy WHERE callNumber = ?");

			ps.setString(1, book.getCallNumber());

			rs = ps.executeQuery();

			while (rs.next()) {
				copies.add(BookCopy.getInstance(rs, book));
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return copies.toArray(new BookCopy[copies.size()]);
	}

	public Borrowing[] selectUnreturnedBorrowingsByBorrower(Borrower borrower) {
		ResultSet rs = null;
		List<Borrowing> borrowings = new ArrayList<Borrowing>();

		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE bid = ? AND inDate IS NULL");

			ps.setInt(1, borrower.getId());

			rs = ps.executeQuery();

			while (rs.next()) {
				String callNumber = rs.getString("callNumber");
				int copyNo = rs.getInt("copyNo");

				BookCopy copy = selectCopyByCallAndCopyNumber(callNumber,
						copyNo);
				borrowings.add(Borrowing.getInstance(rs, borrower, copy));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrowings.toArray(new Borrowing[borrowings.size()]);
	}

	public Borrowing[] selectBooksBorrowedInAYear(Date yearAfter, Date yearBefore) {
		ResultSet rs = null;
		List<Borrowing> borrows = new ArrayList<Borrowing>();

		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE outDate > ? and outDate < ? ");


			ps.setDate(1, new java.sql.Date(yearBefore.getTime()));
			ps.setDate(2, new java.sql.Date(yearAfter.getTime()));

			rs = ps.executeQuery();

			while (rs.next()) {
				borrows.add(constructBorrowing(rs));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrows.toArray(new Borrowing[borrows.size()]);
	}

	private Borrowing constructBorrowing(ResultSet rs) throws SQLException {
		Borrower borrower = selectBorrowerById(rs.getInt("bid"));
		BookCopy copy = selectCopyByCallAndCopyNumber(
				rs.getString("callNumber"), rs.getInt("copyNo"));
		return Borrowing.getInstance(rs, borrower, copy);
	}

	// Display all books
	public Book[] selectAllBooks() {
		ResultSet rs = null;
		List<Book> books = new ArrayList<Book>();
		try {
			ps = con.prepareStatement("SELECT * FROM Book");

			rs = ps.executeQuery();

			while (rs.next()) {
				books.add(constructBook(rs));
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return books.toArray(new Book[books.size()]);
	}

	public Book selectBookByCallNumber(String callNumber) {
		ResultSet rs = null;
		Book book = null;
		try {
			ps = con.prepareStatement("SELECT * FROM Book WHERE callNumber = ?");

			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			if (rs.next()) {
				book = constructBook(rs);
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return book;
	}

	private Book constructBook(ResultSet rs) throws SQLException {
		Book book = Book.getInstance(rs);
		String[] authors = selectAdditionalAuthorsByCallNumber(book
				.getCallNumber());
		book.addAllAuthors(authors);

		String[] subjects = selectSubjectsByCallNumber(book.getCallNumber());
		book.addAllSubjects(subjects);

		return book;
	}

	public String[] selectAdditionalAuthorsByCallNumber(String callNumber) {
		ResultSet rs = null;
		List<String> authors = new ArrayList<String>();
		try {
			ps = con.prepareStatement("SELECT name FROM hasAuthor WHERE callNumber = ?");

			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				authors.add(rs.getString("name"));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return authors.toArray(new String[authors.size()]);
	}

	public String[] selectSubjectsByCallNumber(String callNumber) {
		ResultSet rs = null;
		List<String> subjects = new ArrayList<String>();
		try {
			ps = con.prepareStatement("SELECT subject FROM hasSubject WHERE callNumber = ?");

			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				subjects.add(rs.getString("subject"));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return subjects.toArray(new String[subjects.size()]);
	}

	public BookCopy selectCopyByBookAndCopyNumber(Book book, int copyNo) {
		BookCopy copy = selectCopyByCallAndCopyNumberWithoutBook(
				book.getCallNumber(), copyNo);

		copy.setBook(book);
		return copy;
	}

	public BookCopy selectCopyByCallAndCopyNumber(String callNumber, int copyNo) {
		BookCopy copy = selectCopyByCallAndCopyNumberWithoutBook(callNumber,
				copyNo);

		Book book = selectBookByCallNumber(callNumber);
		copy.setBook(book);
		return copy;
	}

	public int selectMaxCopyNumberForBook(Book book) {
		ResultSet rs = null;
		int max = 0;
		try {
			ps = con.prepareStatement("SELECT MAX(copyNo) FROM BookCopy WHERE callNumber = ?");

			ps.setString(1, book.getCallNumber());

			rs = ps.executeQuery();

			if (rs.next()) {
				max = rs.getInt("max(copyNo)");
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return max;
	}

	private BookCopy selectCopyByCallAndCopyNumberWithoutBook(
			String callNumber, int copyNo) {
		ResultSet rs = null;
		BookCopy copy = null;
		try {
			ps = con.prepareStatement("SELECT * FROM BookCopy WHERE callNumber = ? AND copyNo = ?");

			ps.setString(1, callNumber);
			ps.setInt(2, copyNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				copy = BookCopy.getInstance(rs, null);
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return copy;
	}

	public Borrower selectBorrowerByIdAndPassword(int bid, String pw) {
		ResultSet rs = null;
		Borrower borrower = null;
		try {
			ps = con.prepareStatement("SELECT * FROM Borrower WHERE bid = ? AND password = ?");

			ps.setInt(1, bid);
			ps.setString(2, pw);

			rs = ps.executeQuery();

			if (rs.next()) {
				borrower = Borrower.getInstance(rs);
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrower;
	}

	public HoldRequest[] selectHoldRequestsByBorrower(Borrower borrower) {
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("SELECT * FROM HoldRequest WHERE bid = ?");
			ps.setInt(1, borrower.getId());

			rs = ps.executeQuery();

			List<HoldRequest> holds = new ArrayList<HoldRequest>();

			while (rs.next()) {
				Book book = selectBookByCallNumber(rs.getString("callNumber"));

				holds.add(HoldRequest.getInstance(rs, book, borrower));
			}

			ps.close();

			return holds.toArray(new HoldRequest[holds.size()]);

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return new HoldRequest[0];
	}

	public HoldRequest[] selectHoldRequestsByCall(Book book) {
		ResultSet rs = null;
		List<HoldRequest> holds = new ArrayList<HoldRequest>();

		try {
			ps = con.prepareStatement("SELECT * FROM HoldRequest WHERE callNumber = ?");
			ps.setString(1, book.getCallNumber());

			rs = ps.executeQuery();

			while (rs.next()) {
				Borrower borrower = selectBorrowerById(rs.getInt("bid"));
				holds.add(HoldRequest.getInstance(rs, book, borrower));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return holds.toArray(new HoldRequest[holds.size()]);
	}

	public Fine[] selectOutstandingFineByBorrower(Borrower borrower) {
		ResultSet rs = null;

		List<Fine> fines = new ArrayList<Fine>();
		try {
			ps = con.prepareStatement("SELECT f.* FROM Fine f, Borrowing bor WHERE f.borid = bor.borid AND bor.bid = ? AND f.amount > 0");

			ps.setInt(1, borrower.getId());

			rs = ps.executeQuery();

			while (rs.next()) {
				Borrowing borrow = searchBorrowingsByClerk(rs.getInt("borid"));
				fines.add(Fine.getInstance(rs, borrow));
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return fines.toArray(new Fine[fines.size()]);
	}

	private Fine selectFineById(int fid) {
		ResultSet rs = null;
		Fine fine = null;
		
		try {
			ps = con.prepareStatement("SELECT * FROM Fine WHERE fid = ?");
			ps.setInt(1, fid);

			rs = ps.executeQuery();

			if(rs.next()) {
				fine = Fine.getInstance(rs, null);
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return fine;
	}
	
	public Fine selectFineByBorid(int borid) {
		ResultSet rs = null;
		Fine fine = null;
		
		try {
			ps = con.prepareStatement("SELECT * FROM Fine WHERE borid = ? AND paidDate IS NULL");
			ps.setInt(1, borid);

			rs = ps.executeQuery();

			if(rs.next()) {
				fine = Fine.getInstance(rs, null);
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return fine;
	}
	
	public boolean updateFineAmountField(int fid, float amount) {
		Fine fine = selectFineById(fid);
		fine.decreaseAmountBy(amount);
		
		try {
			ps = con.prepareStatement("UPDATE Fine SET amount=? WHERE fid=?");

			ps.setFloat(1, fine.getAmount());
			ps.setInt(2, fid);

			ps.executeUpdate();
			con.commit();

			ps.close();
			return true;

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException e) {
				System.out.println("Message: " + ex.getMessage());
			}

		}

		return false;
	}

	public boolean updateFirstCopyStatus(String status, String callNumber) {
		try {
			ps = con.prepareStatement("UPDATE TOP (1) BookCopy SET status=? WHERE callNumber=?");

			ps.setString(1, status);
			ps.setString(2, callNumber);

			ps.executeUpdate();
			con.commit();

			ps.close();
			return true;

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException e) {
				System.out.println("Message: " + ex.getMessage());
			}

		}

		return false;
	}

	public Borrowing searchBorrowingsByClerk(int borid) {
		ResultSet rs = null;
		Borrowing b = null;
		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE borid = ? AND inDate IS NULL");
			ps.setInt(1, borid);

			rs = ps.executeQuery();

			if (rs.next()) {
				b = constructBorrowing(rs);
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return b;
	}

	public Borrowing[] selectAllBorrowings() {
		ResultSet rs = null;
		List<Borrowing> borrows = new ArrayList<Borrowing>();

		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing");

			rs = ps.executeQuery();

			while (rs.next()) {
				borrows.add(constructBorrowing(rs));

			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrows.toArray(new Borrowing[borrows.size()]);
	}

	public Borrowing[] selectAllBorrowingsByKeyword(String keyword) {
		ResultSet rs = null;
		List<Borrowing> borrows = new ArrayList<Borrowing>();

		//String percentage = "%" + keyword + "%";
		
		try {
			ps = con.prepareStatement("SELECT * FROM Book, Borrowing, HasSubject WHERE borrowing.callnumber = book.callnumber and book.callnumber = hassubject.callnumber and hassubject.subject = ? ");
			
			ps.setString(1, keyword);

			rs = ps.executeQuery();

			while (rs.next()) {
				borrows.add(constructBorrowing(rs));

			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrows.toArray(new Borrowing[borrows.size()]);
	}
	
	public Integer[] selectAllBorrowingsByBid(int bid) {
		ResultSet rs = null;
		List<Integer> borrows = new ArrayList<Integer>();

		//String percentage = "%" + keyword + "%";
		
		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE bid = ? ");
			
			ps.setInt(1, bid);

			rs = ps.executeQuery();

			while (rs.next()) {
				borrows.add(rs.getInt("borid"));
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrows.toArray(new Integer[borrows.size()]);
	}
	
	// Exclusive for Clerk
	public Borrower selectBorrowerById(int bid) {
		ResultSet rs = null;
		Borrower borrower = null;
		try {
			ps = con.prepareStatement("SELECT * FROM Borrower WHERE bid = ?");

			ps.setInt(1, bid);

			rs = ps.executeQuery();

			if (rs.next()) {
				borrower = Borrower.getInstance(rs);
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrower;
	}

	/*
	 * public ResultSet searchBorrowerTypeByType(BorrowerType type) { ResultSet
	 * rs = null;
	 * 
	 * try { ps =
	 * con.prepareStatement("SELECT * FROM BorrowerType WHERE type = ?");
	 * ps.setString(1, type.getType());
	 * 
	 * rs = ps.executeQuery();
	 * 
	 * ps.close();
	 * 
	 * } catch (SQLException ex) { System.out.println("Message: " +
	 * ex.getMessage()); }
	 * 
	 * return rs; }
	 */

	public Borrowing[] selectOverDueBorrowingByDate(Date dueDate) {
		ResultSet rs = null;

		List<Borrowing> bs = new ArrayList<Borrowing>();
		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE inDate IS NULL AND outDate < ?");
			
			ps.setDate(1, new java.sql.Date(dueDate.getTime()));

			rs = ps.executeQuery();

			while (rs.next()) {
				bs.add(constructBorrowing(rs));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return bs.toArray(new Borrowing[bs.size()]);
	}

	public Borrower[] selectAllBorrowers() {
		ResultSet rs = null;
		List<Borrower> borrowers = new ArrayList<Borrower>();

		try {
			ps = con.prepareStatement("SELECT * FROM Borrower");

			rs = ps.executeQuery();

			while (rs.next()) {
				borrowers.add(Borrower.getInstance(rs));
			}
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return borrowers.toArray(new Borrower[borrowers.size()]);
	}

	public BookCopy selectCopyByCallAndStatus(
			String callNumber, CopyStatus status) {
		ResultSet rs = null;
		BookCopy copy = null;
		try {
			ps = con.prepareStatement("SELECT * FROM BookCopy WHERE callNumber = ? AND status = ?");

			ps.setString(1, callNumber);
			ps.setString(2, status.getStatus());

			rs = ps.executeQuery();

			if (rs.next()) {
				copy = BookCopy.getInstance(rs, null);
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return copy;
	}
	public BookCopy selectCopy(
			String callNumber, CopyStatus status, int copyNo) {
		ResultSet rs = null;
		BookCopy copy = null;
		try {
			ps = con.prepareStatement("SELECT * FROM BookCopy WHERE callNumber = ? AND status = ? AND copyNo = ?");

			ps.setString(1, callNumber);
			ps.setString(2, status.getStatus());
			ps.setInt(3, copyNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				copy = BookCopy.getInstance(rs, null);
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return copy;
	}
	public boolean updateCopyStatus(CopyStatus status, int copyNo, String callNumber) {
		try {
			ps = con.prepareStatement("UPDATE BookCopy SET status=? WHERE callNumber=? AND copyNo = ?");

			ps.setString(1, status.getStatus());
			ps.setString(2, callNumber);
			ps.setInt(3, copyNo);

			ps.executeUpdate();
			con.commit();

			ps.close();
			return true;

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException e) {
				System.out.println("Message: " + ex.getMessage());
			}

		}

		return false;
	}
	public BookCopy[] selectBookCopiesByBookAndStatus(Book book, CopyStatus status) {
		ResultSet rs = null;
		List<BookCopy> copies = new ArrayList<BookCopy>();

		try {
			ps = con.prepareStatement("SELECT * FROM BookCopy WHERE callNumber = ? AND status = ?");

			ps.setString(1, book.getCallNumber());
			ps.setString(2, status.getStatus());

			rs = ps.executeQuery();

			while (rs.next()) {
				copies.add(BookCopy.getInstance(rs, book));
			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return copies.toArray(new BookCopy[copies.size()]);
	}
	
	public boolean updateFirstCopyStatus(CopyStatus status, String callNumber) {
		try {
			ps = con.prepareStatement("UPDATE TOP (1) BookCopy SET status=? WHERE callNumber=?)");

			ps.setString(1, status.getStatus());
			ps.setString(2, callNumber);

			ps.executeUpdate();
			con.commit();

			ps.close();
			return true;

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException e) {
				System.out.println("Message: " + ex.getMessage());
			}

		}

		return false;
	}
	
	public boolean updateBorrowingByIndate(int borid, Date inDate) {
		Date formatedDate = new java.sql.Date(inDate.getTime());
		try {
			ps = con.prepareStatement("UPDATE Borrowing SET inDate=? WHERE borid=?");

			ps.setDate(1, (java.sql.Date)formatedDate);
			ps.setInt(2, borid);

			ps.executeUpdate();
			con.commit();

			ps.close();
			return true;

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException e) {
				System.out.println("Message: " + ex.getMessage());
			}

		}

		return false;
	}

	public int getCopyCountByCallNumberAndStatus(String callNumber,
			String status) {
		ResultSet rs = null;
		int count = 0;

		try {
			ps = con.prepareStatement("SELECT COUNT(copyNo) FROM BookCopy WHERE callnumber = ? and status=?");			
			ps.setString(1, callNumber);
			ps.setString(2, status);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("Count(copyNo)");

			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return count;
	}
	public int getCopyCountByCallNumber(String callNumber) {
		ResultSet rs = null;
		int count = 0;

		try {
			ps = con.prepareStatement("SELECT COUNT(copyNo) FROM BookCopy WHERE callnumber = ?");			
			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("Count(copyNo)");

			}

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return count;
	}
}
