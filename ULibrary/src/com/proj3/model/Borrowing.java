package com.proj3.model;

import java.util.Date;

public class Borrowing {
	
	private int borid, bid, copyNo;
	private String callNumber;
	private Date outDate, inDate;
	
	public Borrowing() {
		
	}
	
	public Borrowing(int borid, int bid, String callNumber, int copyNo, Date outDate, Date inDate) {
		this.setBorid(borid);
		this.setBid(bid);
		this.setCallNumber(callNumber);
		this.setCopyNo(copyNo);
		this.setOutDate(outDate);
		this.setInDate(inDate);
	}

	public int getBorid() {
		return borid;
	}

	public void setBorid(int borid) {
		this.borid = borid;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getCopyNo() {
		return copyNo;
	}

	public void setCopyNo(int copyNo) {
		this.copyNo = copyNo;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
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
	
	
}
