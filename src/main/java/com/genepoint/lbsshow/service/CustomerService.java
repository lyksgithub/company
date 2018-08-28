package com.genepoint.lbsshow.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.lbsshow.model.vo.FlowByHourVO;


public interface CustomerService {
	/**
	 * 获取实时客流量
	 * @param building
	 * @param floor
	 * @return
	 */
	JSONArray getRealtimeData(String building,String floor);
	/**
	 * 获取实时商店客流量
	 * @param building
	 * @return
	 */
	JSONObject getRealtimeCustomerAndShopData(String building);
	/**
	 * 获取历史客流量
	 * @param building
	 * @param duration
	 * @param durationEnd
	 * @param durationStart
	 * @return
	 */
	List<FlowByHourVO> getHistoryCustomerFlow(String building,Long duration,Long durationEnd,Long durationStart);

}
