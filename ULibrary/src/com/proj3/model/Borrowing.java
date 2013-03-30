package com.proj3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class Borrowing {
	
	private Borrower borrower;
	private BookCopy copy;
	private int bid,borid;
	private Date outDate, inDate;
	
	public Borrowing() {
		
	}
	
	public Borrowing(int borid, int bid, BookCopy copy, Borrower borrower, Date outDate, Date inDate) {
		this.setBorid(borid);
		this.setBorrower(borrower);
		this.setBid(bid);
		this.setCopy(copy);
		this.setOutDate(outDate);
		this.setInDate(inDate);
	}

	public static Borrowing getInstance(ResultSet rs, Borrower borrower, BookCopy copy) throws SQLException {
		Borrowing b = new Borrowing();

		int borid = rs.getInt("borid");
		int bid = rs.getInt("bid");
		String callNumber = rs.getString("callNumber");
		
		Date outDate = rs.getDate("outDate");
		Date inDate = rs.getDate("inDate");

		if (rs.wasNull()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(outDate);
			cal.add(Calendar.DATE, borrower.getType().getBorrowingLimit());
			inDate = cal.getTime();
		}
		
		b.setBid(borid);
		b.setBid(bid);
		b.setBorrower(borrower);
		b.setCopy(copy);
		b.setOutDate(outDate);
		b.setInDate(inDate);
		
		return b;
	}
	
	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public BookCopy getCopy() {
		return copy;
	}

	public void setCopy(BookCopy copy) {
		this.copy = copy;
	}
	
	public Book getBook() {
		return copy.getBook();
	}
	
	public void setBorid(int borid) {
		this.borid = borid;
	}
	
	public int getBorid() {
		return borid;

	}
	
	public String getCallNumber() {
		return copy.getCallNumber();
	}
}
