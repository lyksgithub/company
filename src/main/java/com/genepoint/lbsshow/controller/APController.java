package com.genepoint.lbsshow.controller;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.model.APStatusResult;
import com.genepoint.lbsshow.service.APService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("ap")
@RestController
@RequestMapping("/ap")
public class APController {

	@Autowired
	APService apSevice;
	
	@GetMapping("/get_ap_position")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query")})
	public ResultUtils getApPosition(String building,String floor) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(floor)) {
			return ResultUtils.create(500, "floor不能为null");
		}
		JSONObject obj = apSevice.getApPosition(building, floor);
		if(obj == null) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功", JSON.parse(obj.toString()));
	}
	
	@GetMapping("/get_ap_echarts")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="time",value="time",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query")})
	public ResultUtils getApEcharts(String building,String floor,String time) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(Strings.isBlank(floor)) {
			return ResultUtils.create(500, "floor不能为null");
		}
		if(Strings.isBlank(time)) {
			return ResultUtils.create(500, "time不能为null");
		}
		APStatusResult apEcharts = apSevice.getApEcharts(building, floor, time);
		if(apEcharts == null) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功",apEcharts);
	}
	
}
