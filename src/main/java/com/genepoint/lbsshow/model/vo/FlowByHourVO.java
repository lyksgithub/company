package com.genepoint.lbsshow.model.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genepoint.lbsshow.model.FlowByHour;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowByHourVO extends FlowByHour {

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.time = sdf.format(new Date(super.getTimeEnd()));
	}
	
	
}
