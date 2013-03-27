package com.proj3.model;

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
	
	private Set<BookCopy> copies;

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
	
	public Set<BookCopy> getCopies() {
		return copies;
	}

	public void setCopies(Set<BookCopy> copies) {
		this.copies = copies;
	}
	
	public void addCopy(BookCopy copy) {
		copies.add(copy);
	}
}
