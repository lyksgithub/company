package com.genepoint.lbsshow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.lbsshow.dao.InshopByDayMapper;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.dao.ShopMapper;
import com.genepoint.lbsshow.model.InshopByDay;
import com.genepoint.lbsshow.model.Shop;
import com.genepoint.lbsshow.model.vo.InshopByDayVO;
import com.genepoint.lbsshow.service.ShopService;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	ShopMapper shopDao;
	@Autowired
	RedisDao redisDao;
	@Autowired
	InshopByDayMapper inshopByDayDao;
	
	@Override
	public List<Shop> getShopList(String building) {
		List<Shop> allShop = shopDao.getShopByBuilding(building);
		return allShop;
	}

	@Override
	@Cacheable(value= "mycache",key = "#action+'_'+#building+'_'+#time")
	public String getShopRank(String action,String building, Long time) {
		InshopByDay inshopByDay = new InshopByDay();
		inshopByDay.setBuilding(building);
		inshopByDay.setTime(time);
		List<InshopByDay> shopList = inshopByDayDao.getInshopByDay(inshopByDay);
		System.out.println(shopList.toString());
		shopList.sort(new Comparator<InshopByDay>() {
			@Override
			public int compare(InshopByDay o1, InshopByDay o2) {
				return o2.getCount() - o1.getCount();
			}
		});
		//封装成JSONArray，加入缓存
		List<Shop> allShop = shopDao.getAllShop();
		Map<Integer,Shop> shopMap = new HashMap<>(); 
		for(Shop shop:allShop) {
			shopMap.put(shop.getShopId(), shop);
		}
		JSONArray shopArray = new JSONArray();
		for (InshopByDay bean : shopList) {
			JSONObject obj = new JSONObject();
			obj.put("id", bean.getShopId());
			obj.put("name", shopMap.get(bean.getShopId()).getShopName());
			obj.put("floor", shopMap.get(bean.getShopId()).getFloor());
			if (bean.getCount() == 0 || bean.getCountInshop() == 0) {
				obj.put("rate", "0.0");
			} else {
				obj.put("rate", String.format("%.2f", bean.getCountInshop() * 1.0 / bean.getCount()));
			}
			obj.put("stayTime", bean.getAverageStayTime());
			obj.put("flowSize", bean.getCount());
			shopArray.add(obj);
		}
		
		return shopArray.toString();
	}

	@Override
	public List<Map<String,String>> getRateInShop(Integer shopId,String building,Long timeStart, Long timeEnd) {
		InshopByDayVO inshopByDayVO = new InshopByDayVO();
		inshopByDayVO.setShopId(shopId);
		inshopByDayVO.setTimeStart(timeStart);
		inshopByDayVO.setTimeEnd(timeEnd);
		inshopByDayVO.setBuilding(building);
		List<InshopByDay> rateInShop = inshopByDayDao.getRateInShop(inshopByDayVO);
		List<Map<String,String>> inshopList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for(InshopByDay inshop:rateInShop) {
			
			Map<String, String> temp = new HashMap<>();
			temp.put("time", sdf.format(new Date(inshop.getTime())));
			int rate = 0;
			if(inshop.getCount() != 0 && inshop.getCountInshop() != 0) {
				rate = (int)(inshop.getCountInshop() *1.0/inshop.getCount() * 100);
			}
			temp.put("rate",rate+"");
			inshopList.add(temp);
		}
		return inshopList;
	}

	@Override
	public InshopByDay getStayTimeInShop(InshopByDay inshopByDay) {
		InshopByDay stayTimeType = inshopByDayDao.getStayTimeType(inshopByDay);
		return stayTimeType;
	}
	
	
}
