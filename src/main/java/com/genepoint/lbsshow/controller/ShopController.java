package com.genepoint.lbsshow.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.model.InshopByDay;
import com.genepoint.lbsshow.model.Shop;
import com.genepoint.lbsshow.service.ShopService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("shop")
@RestController
@RequestMapping("/shop")
public class ShopController {
	
	@Autowired
	ShopService shopService;
	@Autowired
	RedisDao redisDao;
	/**
	 * 根据building获取场所的店铺列表
	 * @param building
	 * @return
	 */
	@GetMapping("/get_shop_list")
	@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query")
	public ResultUtils getShopList(String building) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		List<Shop> shopList = shopService.getShopList(building);
		if(shopList.size() <= 0) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功", shopList);
	}
	/**
	 * 店铺排行接口 客流量排行
	 * @param building
	 * @param time
	 * @return
	 */
	@GetMapping("/get_shop_rank")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="time",value="time",dataType="Date",required=true,paramType="query")})
	public ResultUtils getShopRank(String building,Long time) {
		String action ="get_shop_rank";
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(time == 0L || time == null) {
			return ResultUtils.create(500, "time不能为null");
		}
		String shopRank = shopService.getShopRank(action,building,time * 1000L);
		boolean setTimeout = redisDao.setTimeout("mycache::"+action+"_"+building+"_"+time*1000L, 3000L);
		if(setTimeout) {
			System.out.println("缓存时间设置成功");
		}
		if(shopRank == null) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功", JSON.parse(shopRank));
	}
	/**
	 * 进店率统计接口
	 * @param shopId
	 * @param duration
	 * @param durationEnd
	 * @param durationStart
	 * @return
	 */
	@GetMapping("/get_rate_in_shop")
	@ApiImplicitParams({
		@ApiImplicitParam(name="shopId",value="shopId",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="duration",value="duration",dataType="Long",required=true,paramType="query"),
		@ApiImplicitParam(name="durationEnd",value="durationEnd",dataType="Long",required=false,paramType="query"),
		@ApiImplicitParam(name="durationStart",value="durationStart",dataType="Long",required=false,paramType="query")})
	public ResultUtils getRateInShop(Integer shopId,String building,Long duration,Long durationEnd,Long durationStart) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(duration == 0L || duration == null) {
			return ResultUtils.create(500, "duration不能为null");
		}
		if(duration == -1) {
			if(durationEnd < durationStart) {
				return ResultUtils.create(500, "输入时间不合法");
			}
		}
		Long timeStart,timeEnd;
		if (duration == -1) {
			timeEnd = durationEnd * 1000L;
			timeStart = durationStart * 1000L;
		} else {
			timeEnd = System.currentTimeMillis();
			timeStart = timeEnd - duration * 1000L;
		}
		List<Map<String, String>> rateInShop = shopService.getRateInShop(shopId,building,timeStart, timeEnd);
		if(rateInShop.size() <= 0) {
			return ResultUtils.create(201, "未查询成功");
		}
		return ResultUtils.create(200, "查询成功", rateInShop);
	}
	/**
	 *  
	 * @param building
	 * @param shopId
	 * @param time
	 * @return
	 */
	@GetMapping("/get_staytime_in_shop")
	@ApiImplicitParams({
		@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query"),
		@ApiImplicitParam(name="shopId",value="shopId",dataType="Integer",required=true,paramType="query"),
		@ApiImplicitParam(name="time",value="time",dataType="Long",required=true,paramType="query")})
	public ResultUtils getStayTimeInShop(String building,Integer shopId,Long time) {
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500, "building不能为null");
		}
		if(time == 0L || time == null) {
			return ResultUtils.create(500, "time不能为null");
		}
		InshopByDay inshopByDay = new InshopByDay();
		inshopByDay.setBuilding(building);
		inshopByDay.setShopId(shopId);
		inshopByDay.setTime(time * 1000L);
		InshopByDay stayTimeInShop = shopService.getStayTimeInShop(inshopByDay);
		if(stayTimeInShop == null) {
			return ResultUtils.create(201, "未查询成功");
		}	
		return ResultUtils.create(200, "查询成功", stayTimeInShop);
	}
}
