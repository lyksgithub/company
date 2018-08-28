package com.genepoint.lbsshow.model.vo;

import com.genepoint.lbsshow.model.Table;

public class TableVO  extends Table{
	
	private String startTime;
	
	private String endTime;

	private String tablePreFix;
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTablePreFix() {
		return tablePreFix;
	}

	public void setTablePreFix(String tablePreFix) {
		this.tablePreFix = tablePreFix;
	}
	
	
}
