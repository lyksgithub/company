package com.genepoint.lbsshow.model;

public class Track {

	private Integer id;
	
	private String mac;
	
	private String building;
	
	private String floor;
	
	private Double corx;
	
	private Double cory;
	
	private Long time;
	
	private Integer shopId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public Double getCorx() {
		return corx;
	}

	public void setCorx(Double corx) {
		this.corx = corx;
	}

	public Double getCory() {
		return cory;
	}

	public void setCory(Double cory) {
		this.cory = cory;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

}
