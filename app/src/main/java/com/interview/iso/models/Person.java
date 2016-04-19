package com.interview.iso.models;

public class Person {
	private int nID=0;
	private String strLastName;
	private String strFirstName;
	private String strGender;
	private String strAddress;
	private String strPosition;
	private String strCity;
	private String strTelphone;
	private String strLang;
	private String strInterviewDate;
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
		strLastName = "";
		strFirstName = "";
		strGender = "Female";
		strLang = "cn";
		strAddress = "";
		strPosition = "";
		strCity = "";
		strTelphone = "";
		strInterviewDate = "";
	}

	public int getnID() {
		return nID;
	}

	public void setnID(int nID) {
		this.nID = nID;
	}

	public String getStrLastName() {
		return strLastName;
	}

	public void setStrLastName(String strLastName) {
		this.strLastName = strLastName;
	}

	public String getStrFirstName() {
		return strFirstName;
	}

	public void setStrFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}

	public String getStrGender() {
		return strGender;
	}

	public void setStrGender(String strGender) {
		this.strGender = "Female";
	}

	public String getStrAddress() {
		return strAddress;
	}

	public void setStrAddress(String strAddress) {
		this.strAddress = strAddress;
	}

	public String getStrPosition() {
		return strPosition;
	}

	public void setStrPosition(String strPosition) {
		this.strPosition = strPosition;
	}

	public String getStrCity() {
		return strCity;
	}

	public void setStrCity(String strCity) {
		this.strCity = strCity;
	}

	public String getStrTelphone() {
		return strTelphone;
	}

	public void setStrTelphone(String strTelphone) {
		this.strTelphone = strTelphone;
	}

	public String getStrInterviewDate() {
		return strInterviewDate;
	}

	public void setStrInterviewDate(String strInterviewDate) {
		this.strInterviewDate = strInterviewDate;
	}

	public void setLang( String str_lang ) {
		this.strLang = str_lang;
	}

	public String getLang() {
		return strLang;
	}

}
