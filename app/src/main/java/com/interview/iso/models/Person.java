package com.interview.iso.models;

public class Person {
	private int nID=0;
	private String strLastName;
	private String strFirstName;
	private String strGender;
	private String strAdd;	
	private String strBirthday;
	private String strTime;
	private String strAvatar = null;

	public int getInterview_type() {
		return interview_type;
	}

	public void setInterview_type(int interview_type) {
		this.interview_type = interview_type;
	}

	private int interview_type;
	public String getAvatarPath(){
		return strAvatar;
	}
	public void setAvatar(String path){
		this.strAvatar = path;
	}
	public Person(){
		 nID=0;
		 strLastName="";
		strFirstName="";
		strGender="";
		 strAdd="";
		 strBirthday="";
		 strTime="";
	}
	public String getFirstName() {
		return strFirstName;
	}
	public void setFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}
	public String getLastName() {
		return strLastName;
	}
	public void setLastName(String strLastName) {
		this.strLastName = strLastName;
	}

	public String getGender() {
		return strGender;
	}
	public void setGender(String strGender) {
		this.strGender = strGender;
	}
	public String getAdd() {
		return strAdd;
	}
	public void setAdd(String strAdd) {
		this.strAdd = strAdd;
	}
	
	public int getID() {
		return nID;
	}
	public void setID(int strID) {
		this.nID = strID;
	}
	public String getBirthDay() {
		return strBirthday;
	}
	public void setBirthDay(String strBirthday) {
		this.strBirthday = strBirthday;
	}
	public String getTime() {
		return strTime;
	}
	public void setTime(String strTime) {
		this.strTime = strTime;
	}
}
