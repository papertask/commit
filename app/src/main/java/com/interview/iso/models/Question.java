package com.interview.iso.models;

public class Question {
	public int id;
	public String question_vn ="";
	public String question_cn;
	public String question_my;
	public String question_lao;
	public String question_km;

	public Question(){}
	public Question(String question_cn,String question_vn,String question_my,String question_lao,String question_km){
		this.question_vn = question_vn;
		this.question_cn = question_cn;
		this.question_my = question_my;
		this.question_lao = question_lao;
		this.question_km = question_km;
	}
}
