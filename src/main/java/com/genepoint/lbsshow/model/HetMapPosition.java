package com.genepoint.lbsshow.model;

import java.util.Date;

public class HetMapPosition {

	private double x;
	private double y;
	private Integer value;
	private Date dateTime;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	
	@Override
	public String toString() {
		return "HetMapPosition [x=" + x + ", y=" + y + ", value=" + value + ",dateTime="+dateTime+"]";
	}
	
	
}
