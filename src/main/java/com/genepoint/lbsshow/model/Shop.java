package com.genepoint.lbsshow.model;

public class Shop {

	private String building;
	
	private String floor;
	
	private Integer shopId;
	
	private String shopName;
	
	private String description;
	
	private String path;

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

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Shop [building=" + building + ", floor=" + floor + ", shopId=" + shopId + ", shopName=" + shopName
				+ ", description=" + description + ", path=" + path + "]";
	}
	
	
}
