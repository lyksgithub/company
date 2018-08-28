package com.genepoint.lbsshow.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genepoint.lbsshow.dao.BuildingMapper;
import com.genepoint.lbsshow.model.Building;
import com.genepoint.lbsshow.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {
	
	@Autowired
	BuildingMapper buildingDao;
	@Autowired
	private HttpSession session;
	
	@Override
	public List<Building> getAllBuilding() {
		List<Building> allBuilding = buildingDao.getAllBuilding();
		@SuppressWarnings("unchecked")
		Set<String> bSet = (Set<String>) session.getAttribute("buildings");
		List<Building> bList = new ArrayList<>();
		for(Building building:allBuilding) {
			if(bSet.contains(building.getBuildingCode()) == true) {
				bList.add(building);
			}
		}
		return bList;
	}

	@Override
	public Building switchBuilding(String building) {
		Building switchBuilding = buildingDao.switchBuilding(building);
		return switchBuilding;
	}
	
	
}
