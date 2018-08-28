package com.genepoint.lbsshow.model;

import java.util.List;

public class APStatusResult {

	private  List<String> hours;
	private List<String> APs;
	@SuppressWarnings("rawtypes")
	private List<List> data;
	private Integer  num;

	private Integer max;


	public Integer getMax() {
		return max;
	}


	public void setMax(Integer max) {
		this.max = max;
	}


	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public List<String> getHours() {
		return hours;
	}
	public List<String> getAPs() {
		return APs;
	}
	public void setAPs(List<String> aPs) {
		APs = aPs;
	}
	@SuppressWarnings("rawtypes")
	public List<List> getData() {
		return data;
	}
	public void setData(@SuppressWarnings("rawtypes") List<List> data) {
		this.data = data;
	}
	public void setHours(List<String> hours) {
		this.hours = hours;
	}


}
