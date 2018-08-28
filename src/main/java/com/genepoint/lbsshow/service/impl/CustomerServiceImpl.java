package com.genepoint.lbsshow.service.impl;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.utils.PositionConvertUtil;
import com.genepoint.lbsshow.dao.FlowByHourMapper;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.dao.ShopMapper;
import com.genepoint.lbsshow.model.FlowByHour;
import com.genepoint.lbsshow.model.Shop;
import com.genepoint.lbsshow.model.vo.FlowByHourVO;
import com.genepoint.lbsshow.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	RedisDao redisDao;
	@Autowired
	ShopMapper shopDao;
	@Autowired
	FlowByHourMapper flowByHourDao;
	
	@Override
	public JSONArray getRealtimeData(String building,String floor) {
		String key = building+"_user_hashset";
		Map<String, String> users = redisDao.getUsers(key);
		JSONObject obj = null;
		JSONArray arr = new JSONArray(); 
		for(String user:users.keySet()) {
			String pos = redisDao.getUser(building+"_"+user);
			System.out.println(pos);
			if(pos == null) {
				continue;
			}
			if(Strings.isNotBlank(pos)) {
				obj = JSON.parseObject(pos);
				if(obj.getString("building").equals(building) && obj.getString("floor").equals(floor)) {
					double[] newPos = PositionConvertUtil.convert(building, obj.getDouble("corx"), obj.getDouble("cory"));
					obj.put("corx", newPos[0]);
					obj.put("cory", newPos[1]);
					arr.add(obj);
				}
			}
		}
		return arr;
	}

	@Override
	public JSONObject getRealtimeCustomerAndShopData(String building) {
		String key = building+"_user_hashset";
		Map<String, String> users = redisDao.getUsers(key);
		JSONObject obj = null;
		Map<Integer, Integer> activeShopMap = new HashMap<Integer, Integer>();
		int count = 0;
		for(String user:users.keySet()) {	
			String pos = redisDao.getUser(building+"_"+user);	
			if(pos == null) {
				continue;
			}
			if(Strings.isNotBlank(pos)) {
				obj = JSON.parseObject(pos);
				if(obj.getString("building").equals(building)) {
					activeShopMap.put(obj.getInteger("shopId"), activeShopMap.getOrDefault(obj.getInteger("shopId"), 0) + 1);
					
					count++;
				}
			}
		}
		List<Map.Entry<Integer, Integer>> list = new ArrayList<>();
		list.addAll(activeShopMap.entrySet());
		//大到小排序
		list.sort(new Comparator<Map.Entry<Integer, Integer>>() {
			@Override
			public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});
		int maxShopSize = list.size() > 10 ? 10 : list.size();
		JSONArray shopRank = new JSONArray();
		List<Shop> allShop = shopDao.getAllShop();
		Map<Integer,Shop> shopMap = new HashMap<>();
		for(Shop shop:allShop) {
			shopMap.put(shop.getShopId(), shop);
		}
		for (int i = 0; i < maxShopSize; i++) {
			int shopId = list.get(i).getKey();
			int activeNum = list.get(i).getValue();
			Shop info = shopMap.get(shopId);
			if(info != null) {
				JSONObject shop = new JSONObject();
				shop.put("name", info.getShopName());
				shop.put("floor", info.getFloor());
				shop.put("activeNum", activeNum);
				shopRank.add(shop);
			}
		}
		JSONObject json = new JSONObject();
		json.put("count", count);
		json.put("shopRank", shopRank);
		return json;
	}

	@Override
	public List<FlowByHourVO> getHistoryCustomerFlow(String building,Long duration, Long durationEnd, Long durationStart) {
		Long timeNow,timeTail;
		if(durationEnd != null && durationEnd != 0L && durationStart != 0L && durationStart != null) {
			timeNow = durationEnd * 1000L;
			timeTail = durationStart * 1000L;
		}else {
			timeNow = System.currentTimeMillis();
			timeTail = timeNow - duration * 1000L;
		}
		FlowByHour flowByHour = new FlowByHour();
		flowByHour.setBuilding(building);
		flowByHour.setTimeStart(timeTail);
		flowByHour.setTimeEnd(timeNow);
		List<FlowByHour> historyCustomer = flowByHourDao.getHistoryCustomer(flowByHour);
		List<FlowByHourVO> fList = new ArrayList<>();
		for(FlowByHour flow :historyCustomer) {
			FlowByHourVO flowByHourVO = new FlowByHourVO();
			flowByHourVO.setTimeEnd(flow.getTimeEnd());
			flowByHourVO.setTime();
			flowByHourVO.setValue(flow.getValue());
			fList.add(flowByHourVO);
		}
		
		return fList;
	}


}
