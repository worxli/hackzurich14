package com.example.android_client;

public class LCard {
	
	private String namestring;
	private String uuid;
	
	public LCard (String namestring, String uuid) {
		this.namestring = namestring;
		this.uuid = uuid;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getNameString() {
		return namestring;
	}
	public void setNameString(String namestring) {
		this.namestring = namestring;
	}
	
}
