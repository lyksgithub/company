package com.genepoint.lbsshow.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.utils.PositionConvertUtil;
import com.genepoint.lbsshow.dao.HeatMapMapper;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.model.HetMapPosition;
import com.genepoint.lbsshow.model.vo.HeatMapVO;
import com.genepoint.lbsshow.service.HeatMapService;

@Service
public class HeatMapServiceImpl implements HeatMapService {

	@Autowired
	RedisDao redisDao;
	@Autowired
	HeatMapMapper heatMapDao;
	
    private static Logger logger = LoggerFactory.getLogger(HeatMapServiceImpl.class);

	/**
	 * 这个方法跟customerServiceImpl 中的 getRealtimeData() 一样
	 */
	@Override
	public JSONArray getRealtimeData(String building, String floor) {
		String key = building + "_user_hashset";
		Map<String, String> users = redisDao.getUsers(key);
		JSONObject obj = null;
		JSONArray arr = new JSONArray();
		for(String user:users.keySet()) {
			String pos = redisDao.getUser(building+"_"+user);
			if(pos == null) {
				continue;
			}
			if(Strings.isNotBlank(pos) ) {
				obj =JSON.parseObject(pos);
				if(obj.getString("building").equals(building) && obj.getString("floor").equals(floor)) {
					double[] newPos = PositionConvertUtil.convert(building, obj.getDouble("corx"), obj.getDouble("cory"));
					obj.put("corx", newPos[0]);
					obj.put("cory", newPos[1]);
					arr.add(obj);
				}
			}
		}
		return arr;
	}

	@Override
	public String getHistoryData(String building, String floor, Long startTime,Long endTime) {
		// TODO 缓存中取数据
		
		return null;
	}

	@Override
	public List<List<HetMapPosition>> getHistoryHeatmap(String building, String floor, String start, String end, String minutes) {
		Long startTime = Long.parseLong(start); 
		Long endTime = Long.parseLong(end); 
		Long step = Long.parseLong(minutes);
		List<List<HetMapPosition>> resultList = new ArrayList<>();
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date(startTime));
		String tableName = "track_" + building + "_" + date;
		HeatMapVO heatMapVO = new HeatMapVO();
		heatMapVO.setBuilding(building);
		heatMapVO.setFloor(floor);
		heatMapVO.setStart(startTime);
		heatMapVO.setEnd(endTime);
		heatMapVO.setTableName(tableName);
		heatMapVO.setStep(step);
		List<HetMapPosition> HeatMap = null;
		try {
			HeatMap = heatMapDao.getHistoryHeatMap(heatMapVO);
		}catch(Exception e) {
			return null;
		}
		ArrayList<HetMapPosition> hList = null;
		Date temp= null;
		for(HetMapPosition heatMap:HeatMap) {
			logger.info("数据库查询结果：x="+heatMap.getX()+",y="+heatMap.getY());
			double[] convert = PositionConvertUtil.convert(building, heatMap.getX(), heatMap.getY());
			logger.info("get_history_heatmap:building="+building+",x="+convert[0]+",y="+convert[1]);
			HetMapPosition hetMapPosition = new HetMapPosition();
			if(temp == null || (!temp.equals(heatMap.getDateTime()))) {
				if(hList != null) {
					resultList.add(hList);
				}
				hList = new ArrayList<>();
				temp = heatMap.getDateTime();
			}
			hetMapPosition.setX(new Double(convert[0]).intValue());
			hetMapPosition.setY(new Double(convert[1]).intValue());
			hetMapPosition.setValue(heatMap.getValue());
			hList.add(hetMapPosition);
		}
		return resultList;
	}

}
