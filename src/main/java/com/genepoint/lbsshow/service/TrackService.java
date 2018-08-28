package com.genepoint.lbsshow.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface TrackService {
	
	List<Map<String,String>> getOnlineUserList(String building);
	/**
	 * 用户实时坐标
	 * @param building
	 * @param mac
	 * @return
	 */
	JSONObject getRealtimeData(String building,String mac);
	/**
	 * 获取单楼层轨迹
	 * @param building
	 * @param floor
	 * @param mac
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	String getHistoryTrack(String building,String floor,String mac,Long timeStart,Long timeEnd);
	/**
	 * 多人轨迹图
	 * @param building
	 * @param floor
	 * @param startTime
	 * @param endTime
	 * @param stepSize
	 * @return
	 */
	JSONArray getTrackTrend(String building,String floor,Long startTime,Long endTime,Integer stepSize);
	/**
	 * 历史热力图
	 * @param building
	 * @param floor
	 * @param startTime
	 * @param endTime
	 * @param stepSize
	 * @return
	 */
	JSONArray getHeatMapTrend(String building,String floor,Long startTime,Long endTime,Integer stepSize);
	/**
	 * 活跃用户
	 * @param building
	 * @return
	 */
	JSONArray getTopKMAC(String building);
	
	JSONObject getMapPath(String building);
	/**
	 * 单人轨迹
	 * @param building
	 * @param mac
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	JSONObject getHistoryPersonTrack(String building,String mac,Long startTime,Long endTime);
}
