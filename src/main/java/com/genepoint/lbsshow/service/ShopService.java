package com.genepoint.lbsshow.service;

import java.util.List;
import java.util.Map;

import com.genepoint.lbsshow.model.InshopByDay;
import com.genepoint.lbsshow.model.Shop;

public interface ShopService {
	/**
	 * 获取商场列表
	 * @param building
	 * @return
	 */
	List<Shop> getShopList(String building);
	/**
	 * 获取店铺排行，进店率
	 * @param action
	 * @param building
	 * @param time
	 * @return
	 */
	String getShopRank(String action,String building,Long time);
	/**
	 * 进店率统计
	 * @param shopId
	 * @param building
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	List<Map<String,String>> getRateInShop(Integer shopId,String building,Long timeStart,Long timeEnd);
	/**
	 * 停留时间统计
	 * @param inshopByDay
	 * @return
	 */
	InshopByDay getStayTimeInShop(InshopByDay inshopByDay);
}
