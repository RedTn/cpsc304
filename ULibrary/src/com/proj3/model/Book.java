package com.proj3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Book {
	private String callNumber;
	private String isbn;
	private String title;
	private String mainAuthor;
	private String publisher;
	private String year;
	private Set<String> authors;
	private Set<String> subjects;

	public Book(String callNumber) {
		this();
		
		this.callNumber = callNumber;
	}
	public Book() {
		authors = new HashSet<String>();
		subjects = new HashSet<String>();
	}
	
	public static Book getInstance(ResultSet rs) throws SQLException {
		String callNumber = rs.getString("callNumber");
		Book book = new Book(callNumber);

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

		return book;
	}
	
	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMainAuthor() {
		return mainAuthor;
	}

	public void setMainAuthor(String mainAuthor) {
		this.mainAuthor = mainAuthor;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Set<String> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<String> authors) {
		this.authors = authors;
	}

	public void addAuthor(String author) {
		authors.add(author);
	}
	
	public Set<String> getSubjects() {
		return subjects;
	}

	
	public void setSubjects(Set<String> subjects) {
		this.subjects = subjects;
	}

	public void addSubject(String subject) {
		subjects.add(subject);
	}
	
	public int hashcode() {
		return callNumber.hashCode();
	}
}
