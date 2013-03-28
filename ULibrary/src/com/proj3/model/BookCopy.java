package com.proj3.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookCopy {
	private Book book;
	private int copyNo;
	private CopyStatus status;
	
	public BookCopy(Book book, int copyNo, CopyStatus status) {
		this.book = book;
		this.copyNo = copyNo;
		this.status = status;
	}
	
	public static BookCopy getInstance(ResultSet rs, Book book) throws SQLException {
		int copyNo = rs.getInt("copyNo");
		CopyStatus status = CopyStatus.get(rs.getString("status"));

		return new BookCopy(book, copyNo, status);
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
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
		return book.hashCode()*51859+copyNo;
	}
}
