package com.proj3.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import oracle.sql.DATE;

import com.proj3.model.*;

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

	//Clerk Transaction
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

	//Clerk transaction
	public boolean insertBorrowing(String bid, String callNumber,
			String copyNo, Date outDate, Date inDate) {
		try {
			ps = con.prepareStatement("INSERT INTO Borrowing VALUES (borid_counter.nextval,?,?,?,?,?)");

			ps.setString(1, bid);

			ps.setString(2, callNumber);

			ps.setString(3, copyNo);

			ps.setDate(4, (java.sql.Date)outDate);
			
			//TODO, Null condition for inDate
			ps.setDate(5, (java.sql.Date)inDate);
			
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

	public boolean insertFine(double amount, DATE issuedDate, DATE paidDate,
			String borid) {
		return false;
	}

	public ResultSet selectBooksByKeyword(String keyword) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT * FROM Book b, HasAuthor a, HasSubject s WHERE b.callNumber = a.callNumber AND b.callNumber = s.callNumber AND (b.title LIKE '%?%' OR b.mainAuthor LIKE '%?%' OR a.name LIKE '%?%' OR s.subject '%?%')");

			ps.setString(1, keyword);
			ps.setString(2, keyword);
			ps.setString(3, keyword);
			ps.setString(4, keyword);
			rs = ps.executeQuery();

			ps.close();

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

		return rs;
	}

	public ResultSet selectBookCopiesByCallNumber(String callNumber) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT copyNo, status FROM BookCopy WHERE callNumber = ?");

			ps.setString(1, callNumber);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;
	}
	
	public ResultSet selectUnreturnedBorrowingsByBorrower(int bid) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE bid = ? AND inDate IS NULL");

			ps.setInt(1, bid);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;
	}
	
	public ResultSet selectBookByCallNumber(String callNumber) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT * FROM Book WHERE callNumber = ?");

			ps.setString(1, callNumber);
			
			rs = ps.executeQuery();

			ps.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;
	}
	
	public ResultSet selectCopyByCallAndCopyNumber(String callNumber, int copyNo) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT * FROM BookCopy WHERE callNumber = ? AND copyNo = ?");

			ps.setString(1, callNumber);
			ps.setInt(2, copyNo);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;
	}
	
	public ResultSet selectBorrowerByIdAndPassword(int bid, String pw) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT * FROM Borrower WHERE bid = ? AND password = ?");

			ps.setInt(1, bid);
			ps.setString(2, pw);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;	
	}
	
	public ResultSet selectHoldRequestsByBorrower(int bid) {
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement("SELECT * FROM HoldRequest WHERE bid = ?");
			ps.setInt(1, bid);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;	
	}
	
	public ResultSet selectOutstandingFineAndBorrowByBorrower(int bid) {
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement("SELECT * FROM Fine f, Borrowing bor WHERE f.borid = bor.borid AND bor.bid = ? AND f.amount > 0");

			ps.setInt(1, bid);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;
	}
	
	public boolean updateFineAmountField(int fid, float amount) {
		try {
			ps = con.prepareStatement("UPDATE Fine SET amount=? WHERE fid=?");
			
			ps.setFloat(1, amount);
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
	
	public ResultSet searchBorrowingsByClerk(int bid) {
		ResultSet rs  = null;

		try {
			ps = con.prepareStatement("SELECT * FROM Borrowing WHERE bid = ? AND inDate IS NULL");
			ps.setInt(1, bid);
			
			rs = ps.executeQuery();

			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return rs;
	}
}
