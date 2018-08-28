package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.MacTopk;

public interface MacTopkMapper {
	
	List<MacTopk> getMacTopk(String building);
	
}
