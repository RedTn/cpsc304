package com.proj3.model;

public enum CopyStatus {
	onhold("on-hold"),
	in("in"),
	out("out");

	private String status;
	
	private CopyStatus(String status){
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public static CopyStatus get(String str) {
		if ("on-hold".equals(str)) {
			return onhold;
		} else if ("in".equals(str)) {
			return in;
		} else if ("out".equals(str)) {
			return out;
		}
		
		return null;
	}
}
