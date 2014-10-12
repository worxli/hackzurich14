package com.example.android_client;

import java.util.HashMap;

public class BCard {
	
	private String name;
	private String first_name;
	private String dob;
	private String address;
	private String postcode;
	private String city;
	private String land;
	private String email_address;
	private String phone_number;
	private String facebook;
	private String twitter;
	private String linkedin;
	private String xing;
	private int uid;
	private String uuid;
	
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLand() {
		return land;
	}
	public void setLand(String land) {
		this.land = land;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getLinkedin() {
		return linkedin;
	}
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}
	public String getXing() {
		return xing;
	}
	public void setXing(String xing) {
		this.xing = xing;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public HashMap<String,String> getAll() {
		HashMap<String, String> data = new HashMap<String, String>();
		
		data.put("name", first_name + " " + name);
		//setTitle("My new title");
		data.put("dob", dob);
		data.put("address", address);
		data.put("postcode", postcode);
		data.put("city", city);
		data.put("country", land);
		data.put("email_address", email_address);
		data.put("phone_number", phone_number);
		data.put("facebook", facebook);
		data.put("twitter", twitter);
		data.put("linkedin", linkedin);
		data.put("xing", xing);
		//data.put("UUID", uuid);
		//data.put("UID", Integer.toString(uid));
		
		return data;
	}
	
}
