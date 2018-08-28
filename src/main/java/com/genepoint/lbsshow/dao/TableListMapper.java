package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.Table;
import com.genepoint.lbsshow.model.vo.TableVO;

public interface TableListMapper {
	/**
	 * 获取表名，参数为开始时间和结束时间,表名前缀
	 * @param tableVO
	 * @return
	 */
	List<Table> getTableList(TableVO tableVO);
	
}
