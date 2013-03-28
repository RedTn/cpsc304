package com.proj3.model;

public enum BorrowerType {
	faculty ("fal", 84),
	student ("stu", 14),
	staff ("sta", 42);
	
	private String type;
	private int days;
	
	private BorrowerType(String type, int days){
		this.type = type;
		this.days = days;
	}
	
	public String getType() {
		return type;
	}
	
	public int getBorrowingLimit() {
		return days;
	}
	
	public static BorrowerType get(String str) {
		if ("fal".equals(str)) {
			return faculty;
		} else if ("sta".equals(str)) {
			return staff;
		} else if ("stu".equals(str)) {
			return student;
		}
		
		return null;
	}
}
