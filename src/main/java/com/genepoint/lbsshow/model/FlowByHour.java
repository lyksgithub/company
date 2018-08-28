package com.genepoint.lbsshow.model;

public class FlowByHour {

	private Integer id;
	
	private String building;
	
	private Long timeStart;
	
	private Long timeEnd;
	
	private Integer value;
	
	private String macs;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public Long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}

	public Long getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Long timeEnd) {
		this.timeEnd = timeEnd;
		
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getMacs() {
		return macs;
	}

	public void setMacs(String macs) {
		this.macs = macs;
	}
	
	
}
