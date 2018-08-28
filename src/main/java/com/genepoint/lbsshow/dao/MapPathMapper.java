package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.MapPath;

public interface MapPathMapper {

	List<MapPath> getMapPath(String building);
	
}
