package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.Building;

public interface BuildingMapper {
	
	List<Building> getAllBuilding();
	
	Building switchBuilding(String building);
	
	Building getBuildingCodeAuto(String building);
}
