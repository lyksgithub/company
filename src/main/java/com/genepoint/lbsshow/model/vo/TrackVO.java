package com.genepoint.lbsshow.model.vo;

import com.genepoint.lbsshow.model.Track;

public class TrackVO extends Track {

	private String tableName;

	private Long startTime;
	
	private Long endTime;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	
}
