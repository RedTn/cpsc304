package com.proj3.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class HoldRequest {

	private int hid;
	private Borrower borrower;
	private Book book;
	private Date issuedDate;

	public static HoldRequest getInstance(ResultSet rs, Book book,
			Borrower borrower) throws SQLException {
		HoldRequest h = new HoldRequest();
		h.setBorrower(borrower);
		h.setBook(book);
		h.setHid(rs.getInt("hid"));
		h.setIssuedDate(rs.getDate("issuedDate"));

		return h;
	}

	public int getHid() {
		return hid;
	}

	public void setHid(int hid) {
		this.hid = hid;
	}

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String toStringForBorrower() {
		return "issued: "
				+ new SimpleDateFormat("yyyy-MM-dd").format(issuedDate) + " "
				+ book.getTitle() + " (" + book.getCallNumber() + ")";
	}
	
	public String toStringForClerk() {
		return "\nHid: " + getHid() + "\nBid: " + getBorrower().getId() + "\nCallNumber: "
	+ getBook().getCallNumber() + "\nIssuedDate: " + getIssuedDate();
	}

}
