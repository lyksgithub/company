package com.genepoint.lbsshow.model;

import java.util.List;


/**
*@author agint_bin
*@version 创建时间：2017年5月15日下午9:24:58
*/

public class DataResult {

	private String message;
	private Integer status;
	private List<Buildings> buildings;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Buildings> getBuildings() {
		return buildings;
	}
	public void setBuildings(List<Buildings> buildings) {
		this.buildings = buildings;
	}
	@Override
	public String toString() {
		return "Test1 [message=" + message + ", status=" + status + ", buildings=" + buildings + "]";
	}
	
	
	
}
