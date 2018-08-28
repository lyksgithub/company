package com.genepoint.lbsshow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.genepoint.lbsshow.dao.BuildingMapper;
import com.genepoint.lbsshow.model.Building;
import com.genepoint.lbsshow.service.UserPortraitService;

public class UserPortraitServiceImpl implements UserPortraitService {

	@Autowired
	BuildingMapper buildingDao;
	
	@Override
	public String getMarketData(String building) {
		
		@SuppressWarnings("unused")
		Building build = buildingDao.getBuildingCodeAuto(building);
		//没有building_personas 表
		
		return null;
	}

	@Override
	public String getShopData(String shopId) {

		return null;
	}

}
