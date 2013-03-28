package com.proj3.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Borrower {
	private int bid;
	private String name, address, phone, email, sinOrStNo;
	private BorrowerType type;
	
	public Borrower(int bid, String name, String address, String phone, 
			String email, String sinOrStNo, BorrowerType type) {
		this.setName(name);
		this.setAddress(address);
		this.setPhone(phone);
		this.setEmail(email);
		this.setSinOrStNo(sinOrStNo);
		this.type = type;
	}
	
	public Borrower() {
		
	}
	
	public static Borrower getInstance(ResultSet rs) throws SQLException {
		Borrower currBorrower = new Borrower();
		currBorrower.setId(rs.getInt("bid"));
		currBorrower.setName(rs.getString("name"));
		currBorrower.setAddress(rs.getString("address"));
		currBorrower.setPhone(rs.getString("phone"));
		currBorrower.setEmail(rs.getString("emailAddress"));
		currBorrower.setSinOrStNo(rs.getString("sinOrStNo"));
		BorrowerType type = BorrowerType.get(rs.getString("type"));
		currBorrower.setType(type);
		
		return currBorrower;
	}
	
	public int getId() {
		return bid;
	}
	
	public void setId(int id) {
		bid = id;
	}

	public BorrowerType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSinOrStNo() {
		return sinOrStNo;
	}

	public void setSinOrStNo(String sinOrStNo) {
		this.sinOrStNo = sinOrStNo;
	}

	public void setType(BorrowerType type) {
		this.type = type;
		
	}

}
