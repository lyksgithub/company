package com.genepoint.lbsshow.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Building {

	private Integer id;
	
	private String buildingCode;
	
	private String buildingName;
	
	private String buildingCodeAuto;
	
	private String floors;
	
	private Long addTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingCodeAuto() {
		return buildingCodeAuto;
	}

	public void setBuildingCodeAuto(String buildingCodeAuto) {
		this.buildingCodeAuto = buildingCodeAuto;
	}

	public String getFloors() {
		return floors;
	}

	public void setFloors(String floors) {
		this.floors = floors;
	}

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

}
