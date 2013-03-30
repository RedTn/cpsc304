package com.proj3.app;

import java.util.*;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.Borrower;
import com.proj3.model.Borrowing;
import com.proj3.model.CopyStatus;
import com.proj3.model.Fine;
import com.proj3.model.HoldRequest;

public class BorrowerApp {
	private Database db;
	private Borrower currBorrower;

	public BorrowerApp(Database db) {
		this.db = db;
	}

	public Borrower login(int bid, String password){
		currBorrower = db.selectBorrowerByIdAndPassword(bid, password);
		
		return currBorrower;
	}

	public boolean isLoggedIn() {
		return currBorrower != null;
	}
	
	public HoldRequest[] getHolds() {
		if (currBorrower == null) {
			return new HoldRequest[0];
		}

		HoldRequest[] holds = db.selectHoldRequestsByBorrower(currBorrower);

		return holds;
	}
	
	
	public boolean payFine(int fid, float amount) {
	
		return db.updateFineAmountField(fid, amount);
	}
	
	public boolean placeHold(Book book) {
		return placeHold(book.getCallNumber());
	}
	
	public boolean placeHold(String callNumber) {
		return db.insertHoldRequest(currBorrower.getId(), callNumber, new Date());
	}
	
	public Fine[] getFines() {
		if (currBorrower == null) {
			return new Fine[0];
		}
		
		return db.selectOutstandingFineByBorrower(currBorrower);
	}
	
	public Borrowing[] getBorrowings(){
		if (currBorrower == null) {
			return new Borrowing[0];
		}

		return db.selectUnreturnedBorrowingsByBorrower(currBorrower);
	}

	public Book[] searchBooksByKeyword(String keyword) {
		return db.selectBooksByKeyword(keyword);
	}

	public void logout() {
		currBorrower = null;
	}

	public int getNumCopiesByStatus(Book book, CopyStatus in) {
		return db.getCopyCountByCallNumberAndStatus(book.getCallNumber(), in.getStatus());
	}

}
