package com.genepoint.lbsshow.model;

public class InshopByDay {

	private String building;
	
	private Integer shopId;
	
	private Integer count;
	
	private Integer countInshop;
	
	private Integer averageStayTime;
	
	private Long time;

	private double rateInShop;
	
	private String stayTimeTypeTemp;
	
	private int[] stayTimeType;
			
	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCountInshop() {
		return countInshop;
	}

	public void setCountInshop(Integer countInshop) {
		this.countInshop = countInshop;
	}

	public Integer getAverageStayTime() {
		return averageStayTime;
	}

	public void setAverageStayTime(Integer averageStayTime) {
		this.averageStayTime = averageStayTime;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public double getRateInShop() {
		return rateInShop;
	}

	public void setRateInShop(double rateInShop) {
		this.rateInShop = rateInShop;
	}

	public String getStayTimeTypeTemp() {
		return stayTimeTypeTemp;
	}

	public void setStayTimeTypeTemp(String stayTimeTypeTemp) {
		this.stayTimeTypeTemp = stayTimeTypeTemp;
		setStayTimeType();
	}

	public int[] getStayTimeType() {
		return stayTimeType;
	}

	private void setStayTimeType() {
		String temp = this.stayTimeTypeTemp;
		String[] split = temp.split(",");
		int [] t =new int [split.length];
		for(int i =0; i<split.length; i++) {
			t[i] = Integer.parseInt(split[i]);
		}
		this.stayTimeType = t;
	}
	
}
