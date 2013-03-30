package com.proj3.app;

import java.util.*;

import com.proj3.database.Database;
import com.proj3.model.Book;
import com.proj3.model.Borrower;
import com.proj3.model.Borrowing;
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
	
	public float payFine(Fine fine, float amount) {
		float remaining = 0;
		if (fine.getAmount() > amount) {
			remaining = fine.getAmount() - amount;
		}
		
		boolean success = db.updateFineAmountField(fine.getFid(), remaining);
	
		if (success) {
			return remaining;
		}
		
		return fine.getAmount();
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

}
