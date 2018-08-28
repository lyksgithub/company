package com.genepoint.lbsshow.model.vo;

import com.genepoint.lbsshow.model.InshopByDay;

public class InshopByDayVO extends InshopByDay {
	
	private Long timeStart;
	
	private Long timeEnd;

	private Integer rate;
	
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

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "InshopByDayVO [timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", rate=" + rate + "]";
	}
	
	
}
