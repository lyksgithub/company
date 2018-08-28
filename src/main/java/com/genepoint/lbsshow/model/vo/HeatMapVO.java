package com.genepoint.lbsshow.model.vo;

import com.genepoint.lbsshow.model.HetMapPosition;

public class HeatMapVO extends HetMapPosition {
	
	private Long start;
	
	private Long end;
	
	private String building;
	
	private String floor;

	private String tableName;
	
	private Long step;
	
	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getStep() {
		return step;
	}

	public void setStep(Long step) {
		this.step = step;
	}
	
}
