package com.genepoint.lbsshow.controller;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.model.vo.FlowByHourVO;
import com.genepoint.lbsshow.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("customer")
@RestController
@RequestMapping("/customer")
public class CustomerController {

	
	RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private CustomerService customerService; 
	
	/**
	 * 实时顾客位置
	 * @param building
	 * @param floor
	 * @return
	 */
	@GetMapping("/get_person_realtime")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="floor",value="floor",dataType="String",required=true,paramType="query")})
	public ResultUtils getRealtimeDate(String building,String floor) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500,"building不能为null");
		}
		if(Strings.isBlank(floor)) {
			return ResultUtils.create(500,"floor不能为null");
		}
		JSONArray arr = customerService.getRealtimeData(building,floor);
		return ResultUtils.create(200, "查询成功",JSON.parse(arr.toString()));
	}
	
	/**
	 * 获取商场实时顾客数量
	 * @param building
	 * @return
	 */
	@GetMapping("/get_realtime_customer_shop")
	@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query")
	public ResultUtils getRealtimeCustomerAndShopData(String building) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500,"building不能为null");
		}
		JSONObject obj = customerService.getRealtimeCustomerAndShopData(building);
		return ResultUtils.create(200, "查询成功",JSON.parse(obj.toString()));
	}
	
	@GetMapping("/get_customer_flow_history")
	public ResultUtils getHistoryCustomerFlow(String building,Long duration,Long durationEnd,Long durationStart) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500,"building不能为null");
		}
		if(duration == 0L || duration == null) {
			return ResultUtils.create(500,"duration不能为null");
		}
		if(duration == -1 ) {
			if(durationEnd < durationStart) {
				return ResultUtils.create(500,"日期不合法");
			}
		}
		List<FlowByHourVO> historyCustomerFlow = customerService.getHistoryCustomerFlow(building,duration, durationEnd, durationStart);
		if(historyCustomerFlow.size() <= 0) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功", historyCustomerFlow);
	}
}
