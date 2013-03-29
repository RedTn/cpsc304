package com.proj3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Fine {
	private int fid;
	private float amount;
	private Date issuedDate;
	private Date paidDate;
	private Borrowing borrow;
	
	public static Fine getInstance(ResultSet rs, Borrower borrower, BookCopy copy) throws SQLException {
		Borrowing bor = Borrowing.getInstance(rs, borrower, copy);
		
		return getInstance(rs, bor);
	}
	
	public static Fine getInstance(ResultSet rs, Borrowing borrow) throws SQLException {
		Fine fine = new Fine();
		
		fine.setFid(rs.getInt("fid"));
		fine.setAmount(rs.getFloat("amount"));
		fine.setIssuedDate(rs.getDate("issuedDate"));
		fine.setPaidDate(rs.getDate("paidDate"));
		fine.setBorrowing(borrow);
		return fine;
	}
	
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public Date getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	public Borrowing getBorrowing() {
		return borrow;
	}
	public void setBorrowing(Borrowing borrowing) {
		this.borrow = borrowing;
	}
}
