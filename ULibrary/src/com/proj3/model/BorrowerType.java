package com.proj3.model;

public enum BorrowerType {
	faculty ("fal"),
	student ("stu"),
	staff ("sta");
	
	private String type;
	
	private BorrowerType(String type){
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
