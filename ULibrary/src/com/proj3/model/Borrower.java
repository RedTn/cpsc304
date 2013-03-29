package com.proj3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Borrower {
	private int bid;
	private String name, address, phone, email, sinOrStNo;
	private Date expiryDate;
	private BorrowerType type;
	
	public Borrower(int bid, String name, String address, String phone, 
			String email, String sinOrStNo, Date expiryDate, BorrowerType type) {
		this.setName(name);
		this.setAddress(address);
		this.setPhone(phone);
		this.setEmail(email);
		this.setSinOrStNo(sinOrStNo);
		this.setExpiryDate(expiryDate);
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
		currBorrower.setExpiryDate(rs.getDate("expiryDate"));
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
	
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}

}
