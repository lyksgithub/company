package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.FlowByHour;

public interface FlowByHourMapper {
		
	List<FlowByHour> getHistoryCustomer(FlowByHour flowByHour);
}
