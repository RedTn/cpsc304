package com.proj3.model;

public class BookCopy {
	private String callNumber;
	private int copyNo;
	private CopyStatus status;
	
	public String getCallNumber() {
		return callNumber;
	}
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	
	public CopyStatus getStatus() {
		return status;
	}
	public void setStatus(CopyStatus status) {
		this.status = status;
	}
	
	public int getCopyNo() {
		return copyNo;
	}
	public void setCopyNo(int copyNo) {
		this.copyNo = copyNo;
	}
	
	public int hashcode() {
		return callNumber.hashCode()*51859+copyNo;
	}
}
