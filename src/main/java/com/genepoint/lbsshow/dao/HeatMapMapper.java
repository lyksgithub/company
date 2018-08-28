package com.genepoint.lbsshow.dao;

import java.util.List;

import com.genepoint.lbsshow.model.HetMapPosition;
import com.genepoint.lbsshow.model.vo.HeatMapVO;

public interface HeatMapMapper {

	List<HetMapPosition> getHistoryHeatMap(HeatMapVO heatMapVO);
}
