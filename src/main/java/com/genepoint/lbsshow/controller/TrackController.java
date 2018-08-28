package com.genepoint.lbsshow.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.service.TrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("track")
@RestController
@RequestMapping("/track")
public class TrackController {
	@Autowired
	TrackService trackService;
	@Autowired
	RedisDao redisDao;
	/**
	 * 获取当前在线用户
	 * @param building
	 * @return
	 */
	@GetMapping("/get_all_online_user")
	@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query")
	public ResultUtils getOnlineUserList(String building) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		List<Map<String, String>> onlineUserList = trackService.getOnlineUserList(building);
		if(onlineUserList.size() <= 0) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功", onlineUserList);
	}
	
	@GetMapping("/get_all_offline_user")
	public ResultUtils getOfflineUserList() {
		
		// TODO 
		return ResultUtils.create(200, "查询成功", null);
	}
	
	
	@GetMapping("/get_track_realtime")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="mac",value="mac",dataType="String",required=true,paramType="query")})
	public ResultUtils getRealtimeData(String building,String mac) {
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(mac)){
			return ResultUtils.create(500, "mac不能为null");
		}
		JSONObject realtimeData = trackService.getRealtimeData(building, mac);
		return ResultUtils.create(200, "查询成功",JSON.parse(realtimeData.toString()));
	}
	/**
	 * 查询单楼层轨迹  
	 * @param building
	 * @param mac
	 * @param duration
	 * @param durationEnd
	 * @param durationStart
	 * @return
	 */
	@GetMapping("/get_track_history")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="mac",value="mac",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="duration",value="duration",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="durationEnd",value="durationEnd",dataType="Long",required=false,paramType="query"),
		@ApiImplicitParam(name="durationStart",value="durationStart",dataType="Long",required=false,paramType="query")})
	public ResultUtils getHistoryTrack(String building,String floor,String mac,Long duration,Long durationEnd,Long durationStart){
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(mac)){
			return ResultUtils.create(500, "mac不能为null");
		}
		if(duration == 0L || duration == null){
			return ResultUtils.create(500, "duration不能为null");
		}
		if(duration == -1) {
			if(durationEnd < durationStart) {
				return ResultUtils.create(500, "时间不合法");
			}
		}
		long startTime, endTime;
		if (duration == -1) {
			endTime =  durationEnd * 1000L;
			startTime =  durationStart * 1000L;
		} else {
			endTime = System.currentTimeMillis();
			startTime = endTime - duration * 1000L;
		}
		String historyTrack = trackService.getHistoryTrack(building, floor, mac, startTime, endTime);
		boolean setTimeout = redisDao.setTimeout("mycache::"+building+"_"+floor+"_"+mac+"_"+startTime+"_"+endTime, 3000L);
		if(setTimeout) {
			System.out.println("缓存时间设置成功");
		}
		return ResultUtils.create(200, "查询成功",JSON.parse(historyTrack));
	}
	/**
	 * 获取多人轨迹图
	 * @param building
	 * @param floor
	 * @param duration
	 * @param durationEnd
	 * @param durationStart
	 * @param stepSize
	 * @return
	 */
	@GetMapping("/get_track_trend")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="duration",value="duration",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="durationEnd",value="durationEnd",dataType="Long",required=false,paramType="query"),
		@ApiImplicitParam(name="stepSize",value="stepSize",dataType="Integer",required=true,paramType="query"),
		@ApiImplicitParam(name="durationStart",value="durationStart",dataType="Long",required=false,paramType="query")})
	public ResultUtils getTrackTrend(String building,String floor,Long duration,Long durationEnd,Long durationStart,Integer stepSize) {
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(floor)){
			return ResultUtils.create(500, "floor不能为null");
		}
		if(duration == 0L || duration == null){
			return ResultUtils.create(500, "duration不能为null");
		}
		if(stepSize == 0 || stepSize == null) {
			return ResultUtils.create(500, "stepSize不能为null");
		}
		if(duration == -1) {
			if(durationEnd < durationStart) {
				return ResultUtils.create(500, "时间不合法");
			}
		}
		long startTime, endTime;
		if (duration == -1) {
			endTime =  durationEnd * 1000L;
			startTime =  durationStart * 1000L;
		} else {
			endTime = System.currentTimeMillis();
			startTime = endTime - duration * 1000L;
		}
		JSONArray trackTrend = trackService.getTrackTrend(building, floor, startTime, endTime, stepSize);
		return ResultUtils.create(200, "查询成功",JSON.parse(trackTrend.toString()));
	}
	/**
	 * 查询历史热力图
	 * @param building
	 * @param floor
	 * @param duration
	 * @param durationEnd
	 * @param durationStart
	 * @param stepSize
	 * @return
	 */
	@GetMapping("/get_heatmap_trend")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="duration",value="duration",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="durationEnd",value="durationEnd",dataType="Long",required=false,paramType="query"),
		@ApiImplicitParam(name="stepSize",value="stepSize",dataType="Integer",required=true,paramType="query"),
		@ApiImplicitParam(name="durationStart",value="durationStart",dataType="Long",required=false,paramType="query")})
	public ResultUtils getHeatMapTrend(String building,String floor,Long duration,Long durationEnd,Long durationStart,Integer stepSize) {
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(floor)){
			return ResultUtils.create(500, "floor不能为null");
		}
		if(duration == 0L || duration == null){
			return ResultUtils.create(500, "duration不能为null");
		}
		if(stepSize == 0 || stepSize == null) {
			return ResultUtils.create(500, "stepSize不能为null");
		}
		if(duration == -1) {
			if(durationEnd < durationStart) {
				return ResultUtils.create(500, "时间不合法");
			}
		}
		long startTime, endTime;
		if (duration == -1) {
			endTime =  durationEnd * 1000L;
			startTime =  durationStart * 1000L;
		} else {
			endTime = System.currentTimeMillis();
			startTime = endTime - duration * 1000L;
		}
		JSONArray heatMapTrend = trackService.getHeatMapTrend(building, floor, startTime, endTime, stepSize);
		return ResultUtils.create(200, "查询成功",JSON.parse(heatMapTrend.toString()));
	}
	/**
	 * 获取topk_mac
	 * @param building
	 * @return
	 */
	@GetMapping("/get_topk_mac")
	@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query")
	public ResultUtils getTopKMAC(String building){
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		JSONArray topKMAC = trackService.getTopKMAC(building);
		if(topKMAC == null) {
			return ResultUtils.create(201, "未查询到结果", null);
		}
		return ResultUtils.create(200, "查询成功",JSON.parse(topKMAC.toString()));
	}
	
	/**
	 * 没用到
	 * @param building
	 * @return
	 */
	@GetMapping("/get_map_path_forbidden_region")
	@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query")
	public ResultUtils getMapPath(String building){
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		JSONObject mapPath = trackService.getMapPath(building);
		if(mapPath == null) {
			return ResultUtils.create(201, "未查询到结果",null);
		}
		return ResultUtils.create(200, "查询成功",JSON.parse(mapPath.toString()));
	}
	/**
	 * 获取单人轨迹图
	 * @param building
	 * @param mac
	 * @param duration
	 * @param durationEnd
	 * @param durationStart
	 * @return
	 */
	@GetMapping("/get_history_person_track")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="mac",value="mac",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="duration",value="duration",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="durationEnd",value="durationEnd",dataType="Long",required=false,paramType="query"),
		@ApiImplicitParam(name="durationStart",value="durationStart",dataType="Long",required=false,paramType="query")})
	public ResultUtils getHistoryPersonTrack(String building,String mac,Long duration,Long durationEnd,Long durationStart) {
		if(Strings.isBlank(building)){
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(mac)){
			return ResultUtils.create(500, "mac不能为null");
		}
		if(duration == 0L || duration == null){
			return ResultUtils.create(500, "duration不能为null");
		}
		if(duration == -1) {
			if(durationEnd < durationStart) {
				return ResultUtils.create(500, "时间不合法");
			}
		}
		long startTime, endTime;
		if (duration == -1) {
			endTime =  durationEnd * 1000L;
			startTime =  durationStart * 1000L;
		} else {
			endTime = System.currentTimeMillis();
			startTime = endTime - duration * 1000L;
		}
		JSONObject historyPersonTrack = trackService.getHistoryPersonTrack(building, mac, startTime, endTime);
		if(historyPersonTrack == null) {
			return ResultUtils.create(201, "未查询到结果",null);
		}
		return ResultUtils.create(200, "查询成功",JSON.parse(historyPersonTrack.toString()));
	}
}
