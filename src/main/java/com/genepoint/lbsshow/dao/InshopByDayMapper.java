package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.InshopByDay;
import com.genepoint.lbsshow.model.vo.InshopByDayVO;

public interface InshopByDayMapper {

	List<InshopByDay> getInshopByDay(InshopByDay inshopByDay);
	
	List<InshopByDay> getRateInShop(InshopByDayVO  inshopByDayVO);
	
	InshopByDay getStayTimeType(InshopByDay inshopByDay);
}
