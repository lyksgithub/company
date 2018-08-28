package com.genepoint.lbsshow.controller;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.model.HetMapPosition;
import com.genepoint.lbsshow.service.HeatMapService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("heatmap")
@RestController
@RequestMapping("/heatmap")
public class HeatMapController {

	@Autowired
	HeatMapService heatMapService;
	
	/**
	 * 获取实时热力图
	 * @param building
	 * @param floor
	 * @return
	 */
	@GetMapping("/get_heatmap_realtime")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query")})
	public ResultUtils getRealtimeData(String building,String floor) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(floor)) {
			return ResultUtils.create(500, "floor不能为null");
		}
		JSONArray arr = heatMapService.getRealtimeData(building, floor);
		return ResultUtils.create(200, "查询成功", JSON.parse(arr.toString()));
	}
	
	@GetMapping("/get_heatmap_history")
	public String getHistoryData(String building,String floor,Long duration,Long durationEnd,Long durationStart) {
		
		return "";
	}
	
	@PostMapping("/get_history_heatmap")
	@ApiImplicitParams({
		@ApiImplicitParam(name="floor",value="floor",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="start",value="start",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="end",value="end",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="minutes",value="minutes",dataType="Long",required=true,paramType="query")})
	public ResultUtils getHistoryHeatmap(String building,String floor,String start,String end,String minutes) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(floor)) {
			return ResultUtils.create(500, "floor不能为null");
		}
		if(Strings.isBlank(start)) {
			return ResultUtils.create(500, "start不能为null");
		}
		if(Strings.isBlank(end)) {
			return ResultUtils.create(500, "end不能为null");
		}
		if(Long.parseLong(end) <= Long.parseLong(start)) {
			return ResultUtils.create(500, "时间输入不合理");
		}
		if(Strings.isBlank(minutes)) {
			return ResultUtils.create(500, "minutes不能为null");
		}
		if(Integer.parseInt(minutes) <= 0) {
			return ResultUtils.create(500, "minutes输入有误");
		}
		List<List<HetMapPosition>> historyHeatmap = heatMapService.getHistoryHeatmap(building, floor, start, end, minutes);
		if(historyHeatmap == null) {
			return ResultUtils.create(201, "未查询到结果");
		}
		return ResultUtils.create(200, "查询成功",historyHeatmap);
	}
}
