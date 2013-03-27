package com.proj3.database;

import java.awt.GridLayout;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import oracle.sql.DATE;

import com.proj3.model.BorrowerType;
import com.proj3.model.CopyStatus;

public class Database {
	private String address, username, password;
	private Connection con;
	private PreparedStatement ps;
	
	public Database(String address, String username, String password) {
		this.address = address;
		this.username = username;
		this.password = password;

		try {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
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
								String phone, String email, String sinOrStNo, 
								Date expiryDate, BorrowerType type) {
		try
		{
		  ps = con.prepareStatement("INSERT INTO Borrower VALUES (bid_counter.nextval,?,?,?,?,?,?,?,?)");

		  ps.setString(1, password);
		  
		  ps.setString(2, name);
		
		  ps.setString(3, address);
		 
		  ps.setString(4, phone);
		  
		  ps.setString(5, email);
		 
		  ps.setString(6, sinOrStNo);
		  
		  ps.setDate(7, (java.sql.Date)expiryDate);
		  
		  ps.setString(8, type.getType());

		  ps.executeUpdate();

		  // commit work 
		  con.commit();

		  ps.close();
		  
		  return true;
		}
	
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		    try 
		    {
			// undo the insert
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }
			return false;
		}
		

	}

	public boolean insertBook(String callNumber, String isbn, String title, 
							String mainAuthor, String publisher, String year) {
		//yet to be implemented
		return false;		
	}
	
	public boolean insertHasAuthor(String callNumber, String authorName) {
		
		return false;
	}
	
	public boolean insertHasSubject(String callNumber, String subject) {
		
		return false;
	}
	
	public boolean insertBookCopy(String callNumber, String copyNo, CopyStatus status) {
		return false;
	}
	
	public boolean insertHoldRequest(String bid, String callNumber, DATE issuedDate) {
		return false;
	}

	public boolean insertBorrowing(String bid, String callNumber, String copyNo, DATE outDate, DATE inDate) {
		return false;
	}
	
	public boolean insertFine(double amount, DATE issuedDate, DATE paidDate, String borid) {
		return false;
	}
}
