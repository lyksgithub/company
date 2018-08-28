package com.genepoint.lbsshow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.genepoint.lbsshow.model.APMonitor;

@Mapper
public interface APMonitorMapper {

	List<APMonitor> getAPMonitor(APMonitor apMonitor);
	
}
