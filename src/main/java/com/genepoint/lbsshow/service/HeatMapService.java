package com.genepoint.lbsshow.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.genepoint.lbsshow.model.HetMapPosition;

public interface HeatMapService {
	/**
	 * 获取实时热力图
	 * @param building
	 * @param floor
	 * @return
	 */
	JSONArray getRealtimeData(String building,String floor);
	
	String getHistoryData(String building, String floor, Long startTime,Long endTime);
	/**
	 * 获取历史热力图
	 * @param building
	 * @param floor
	 * @param start
	 * @param end
	 * @param minutes
	 * @return
	 */
	List<List<HetMapPosition>> getHistoryHeatmap(String building,String floor,String start,String end,String minutes);
}
