package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.Shop;

public interface ShopMapper {
	
	List<Shop> getAllShop();
	
	List<Shop> getShopByBuilding(String building);
}
