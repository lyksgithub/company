package com.genepoint.lbsshow.service;

import java.util.List;

import com.genepoint.lbsshow.model.Building;

public interface BuildingService {
	/**
	 * 获取Building列表（与session中的对比有的就拿出来）
	 * @return
	 */
	List<Building> getAllBuilding();
	/**
	 * 根据buildingId来获取building信息加入Session
	 * @param building
	 * @return
	 */
	Building switchBuilding(String building);
}
