package com.genepoint.lbsshow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.utils.PositionConvertUtil;
import com.genepoint.lbsshow.dao.MacTopkMapper;
import com.genepoint.lbsshow.dao.MapPathMapper;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.dao.TableListMapper;
import com.genepoint.lbsshow.dao.TrackMapper;
import com.genepoint.lbsshow.model.MacTopk;
import com.genepoint.lbsshow.model.MapPath;
import com.genepoint.lbsshow.model.Point;
import com.genepoint.lbsshow.model.Table;
import com.genepoint.lbsshow.model.Track;
import com.genepoint.lbsshow.model.vo.TableVO;
import com.genepoint.lbsshow.model.vo.TrackVO;
import com.genepoint.lbsshow.service.TrackService;

@Service
public class TrackServiceImpl implements TrackService {

	@Autowired
	RedisDao redisDao;
	@Autowired
	TableListMapper tableListDao;
	@Autowired
	TrackMapper trackDao;
	@Autowired
	MacTopkMapper macTopkDao;
	@Autowired
	MapPathMapper mapPathDao;
	
    private static Logger logger = LoggerFactory.getLogger(HeatMapServiceImpl.class);
	
	@Override
	public List<Map<String, String>> getOnlineUserList(String building) {
		Map<String, String> users = redisDao.getUsers(building + "_user_hashset");
		Map<String,String> obj= new HashMap<>();
		List<Map<String,String>> macList= new ArrayList<Map<String,String>>();
		for(String user:users.keySet()) {
			obj.put("mac", user);
			macList.add(obj);
		}
		return macList;
	}

	@Override
	public JSONObject getRealtimeData(String building,String mac) {
		List<String> posList = redisDao.lrange(building+"_"+mac, 0, 4);
		Map<String, String> users = redisDao.getUsers(building+ "_user_hashset");
		JSONArray userArr = new JSONArray();
		for (String user : users.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("mac", user);
			userArr.add(obj);
		}
		
		JSONArray arr = new JSONArray();
		if (posList != null) {
			for (String pos : posList) {
				JSONObject obj = JSONObject.parseObject(pos);
				if (obj.getString("building").equals(building)) {
					double[] newPos = PositionConvertUtil.convert(building, obj.getDouble("corx"), obj.getDouble("cory"));
					obj.put("corx", newPos[0]);
					obj.put("cory", newPos[1]);
					arr.add(obj);
				}
			}
		}

		JSONObject data = new JSONObject();
		data.put("userList", userArr);
		data.put("posList", arr);
		
		return data;
	}

	@Override
	@Cacheable(value = "mycache", key = "#building+'_'+#floor+'_'+#mac+'_'+#timeStart+'_'+#timeEnd")
	public String getHistoryTrack(String building,String floor,String mac,Long timeStart,Long timeEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		TableVO tableVO = new TableVO();
		tableVO.setStartTime(sdf.format(new Date(timeStart)));
		tableVO.setEndTime(sdf.format(new Date(timeEnd)));
		logger.info("timeStart:"+sdf.format(new Date(timeStart)));
		logger.info("timeEnd:"+sdf.format(new Date(timeEnd)));
		tableVO.setTablePreFix("track_"+building+"_");
		List<Table> tableList = tableListDao.getTableList(tableVO);
		JSONArray arr = new JSONArray();
		logger.info("tableList长度:"+tableList.size());
		
		for(Table table:tableList) {
			String tableName=table.getTableName();
			TrackVO trackVO = new TrackVO();
			trackVO.setMac(mac);
			trackVO.setFloor(floor);
			trackVO.setTableName(tableName);
			trackVO.setStartTime(timeStart);
			trackVO.setEndTime(timeEnd);
			logger.info("tableName:"+tableName);
			logger.info("floor:"+floor);
			logger.info("timeStart:"+timeStart);
			logger.info("timeEnd:"+timeEnd);
			logger.info("mac:"+mac);
			List<Track> trackList = trackDao.getTrack(trackVO);
			logger.info("trackList长度:"+trackList.size());
			for(Track track:trackList) {
				JSONObject obj = new JSONObject();
				double x = track.getCorx();
				double y = track.getCory();
				double[] newPos = PositionConvertUtil.convert(building, x, y);
				obj.put("x", newPos[0]);
				obj.put("y", newPos[1]);
				obj.put("time", track.getTime());
				arr.add(obj);
			}
		}
		JSONObject obj = new JSONObject();
		if(arr != null ) {
			obj.put("data", arr);
			obj.put("timeStart", timeStart);
			obj.put("timeEnd", timeEnd);
		}
		return obj.toString();
	}

	@Override
	public JSONArray getTrackTrend(String building,String floor,Long startTime,Long endTime,Integer stepSize) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		TableVO tableVO = new TableVO();
		tableVO.setStartTime(sdf.format(new Date(startTime)));
		tableVO.setEndTime(sdf.format(new Date(endTime)));
		tableVO.setTablePreFix("track_"+building+"_");
		List<Table> tableList = tableListDao.getTableList(tableVO);
		JSONArray arr = new JSONArray();
		for(Table table:tableList) {
			TrackVO trackVO = new TrackVO();
			trackVO.setStartTime(startTime);
			trackVO.setEndTime(endTime);
			trackVO.setFloor(floor);
			trackVO.setTableName(table.getTableName());;
			List<Track> tlist = trackDao.getTrackByFloorAndTime(trackVO);
			long timeTemp = 0;
			JSONArray arrGroup = null;
			// 记录窗口内MAC出现的次数
			Map<String, Integer> countMap = null;
			// 记录窗口内MAC最后一次的位置
			Map<String, JSONObject> posMap = null;
			for(Track track:tlist) {
				String mac = track.getMac();
				long time = track.getTime();
				if (timeTemp == 0) {
					timeTemp = time;
					arrGroup = new JSONArray();
					countMap = new HashMap<>();
					posMap = new HashMap<>();
				}
				JSONObject obj = new JSONObject();
				double[] newPos = PositionConvertUtil.convert(building,track.getCorx(),track.getCory());
				obj.put("x", newPos[0]);
				obj.put("y", newPos[1]);
				if (time - timeTemp > stepSize * 1000L) {
					for (String key : countMap.keySet()) {
						if (countMap.get(key) > 0) {
							arrGroup.add(posMap.get(key));
						}
					}
					JSONObject tmp = new JSONObject();
					tmp.put("data", arrGroup);
					tmp.put("time", time);
					arr.add(tmp);
					arrGroup = new JSONArray();
					arrGroup.add(obj);
					timeTemp = time;
					
					countMap.clear();
					countMap.put(mac, 1);
					posMap.clear();
					posMap.put(mac, obj);
				}else {
					countMap.put(mac, countMap.getOrDefault(mac, 0) + 1);
					posMap.put(mac, obj);
				}
			}
			
		}
		
		return arr;
	}

	@Override
	public JSONArray getHeatMapTrend(String building,String floor,Long startTime,Long endTime,Integer stepSize) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		TableVO tableVO = new TableVO();
		tableVO.setStartTime(sdf.format(new Date(startTime)));
		tableVO.setEndTime(sdf.format(new Date(endTime)));
		tableVO.setTablePreFix("track_"+building+"_");
		List<Table> tList = tableListDao.getTableList(tableVO);
		JSONArray arr = new JSONArray();
		for(Table table:tList) {
			TrackVO trackVO = new TrackVO();
			trackVO.setBuilding(building);
			trackVO.setFloor(floor);
			trackVO.setStartTime(startTime);
			trackVO.setEndTime(endTime);
			trackVO.setTableName(table.getTableName());
			List<Track> trackList = trackDao.getTrackByBuilding(trackVO);
			long timeTemp = 0;
			JSONArray arrGroup = null;
			// 记录窗口内某网格命中的次数
			Map<String, Integer> countMap = null;
			long time = 0;
			for(Track track:trackList) {
				time = track.getTime();
				if (timeTemp == 0) {
					timeTemp = time;
					arrGroup = new JSONArray();
					countMap = new HashMap<>();
				}
				int x = Integer.parseInt(track.getCorx().toString());
				int y =Integer.parseInt(track.getCory().toString());
				x= x/30*30;
				y= y/30*30;
				String keyStr = x + "_" + y;
				if (time - timeTemp > stepSize * 1000L) {
					for (String key : countMap.keySet()) {
						String[] tmpArr = key.split("_");
						JSONObject obj = new JSONObject();
						obj.put("x", Double.parseDouble(tmpArr[0]));
						obj.put("y", Double.parseDouble(tmpArr[1]));
						obj.put("value", countMap.get(key));
						arrGroup.add(obj);
					}
					JSONObject tmp = new JSONObject();
					tmp.put("data", arrGroup);
					tmp.put("time", time);
					arr.add(tmp);
					arrGroup = new JSONArray();
					timeTemp = time;
					countMap.clear();
					countMap.put(keyStr, 1);
				} else {
					countMap.put(keyStr, countMap.getOrDefault(keyStr, 0) + 1);
				}
				for (String key : countMap.keySet()) {
					String[] tmpArr = key.split("_");
					JSONObject obj = new JSONObject();
					obj.put("x", Double.parseDouble(tmpArr[0]));
					obj.put("y", Double.parseDouble(tmpArr[1]));
					obj.put("value", countMap.get(key));
					arrGroup.add(obj);
				}
				JSONObject tmp = new JSONObject();
				tmp.put("data", arrGroup);
				tmp.put("time", time);
				arr.add(tmp);		
			}	
		}
		return arr;
	}

	@Override
	public JSONArray getTopKMAC(String building) {
		String redisKey = building + "_mac_topk_sortset";
		Set<String> macs = redisDao.zrevrange(redisKey, 0, 99);
		JSONArray arr = new JSONArray();
		if (macs != null && macs.size() > 0) {
			Iterator<String> it = macs.iterator();
			while (it.hasNext()) {
				arr.add(it.next());
			}
		} else {
			List<MacTopk> mlist = macTopkDao.getMacTopk(building);
			for(MacTopk mactopk:mlist) {
				arr.add(mactopk.getMac());
			}
		}
		return arr;
	}

	@Override
	public JSONObject getMapPath(String building) {
		List<MapPath> mlist = mapPathDao.getMapPath(building);
		// 按楼层拆分路径数据
		Map<String, List<MapPath>> mapPaths = new HashMap<>();
		// 按楼层拆分节点数据
		Map<String, Map<String, Point>> mapNodes = new HashMap<>();
		List<MapPath> list = null;
		Map<String, Point> map = null;
		for(MapPath mapPath:mlist) {
			list = mapPaths.get(mapPath.getFloor());
			map = mapNodes.get(mapPath.getFloor());
			if (list == null) {
				list = new ArrayList<>();
				mapPaths.put(mapPath.getFloor(), list);
			}
			if(map==null){
				map = new HashMap<>();
				mapNodes.put(mapPath.getFloor(), map);
			}
			list.add(mapPath);
			double startX= mapPath.getStartCorx();
			double startY= mapPath.getStartCory();
			double endX = mapPath.getEndCorx();
			double endY = mapPath.getEndCory();
			map.put(Integer.toString(mapPath.getStartId()), new Point((float)startX,(float)startY));
			map.put(Integer.toString(mapPath.getEndId()), new Point((float) endX, (float) endY));
		}
		JSONObject obj = new JSONObject(); 
		obj.put("mapPaths", JSON.toJSON(mapPaths));
		obj.put("mapNodes", JSON.toJSON(mapNodes));
		return obj;
	}

	@Override
	public JSONObject getHistoryPersonTrack(String building,String mac,Long startTime,Long endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		TableVO tableVO = new TableVO();
		tableVO.setTablePreFix("track_" + building + "_");
		tableVO.setStartTime(sdf.format(new Date(startTime)));
		tableVO.setEndTime(sdf.format(new Date(endTime)));
		logger.info("timeStart:"+sdf.format(new Date(startTime)));
		logger.info("timeEnd:"+sdf.format(new Date(endTime)));
		List<Table> tList = tableListDao.getTableList(tableVO);
		logger.info("tableList长度:"+tList.size());
		double timeDuration ;
		@SuppressWarnings("unused")
		long allTimeDuration = 0;
		JSONArray floorTime=new JSONArray();
		JSONArray arr = new JSONArray();
		JSONObject result = new JSONObject();
		for(Table table:tList) {
			TrackVO trackVO = new TrackVO();
			trackVO.setTableName(table.getTableName());
			trackVO.setStartTime(startTime);
			trackVO.setEndTime(endTime);
			trackVO.setMac(mac);
			logger.info("tableName:"+table.getTableName());
			logger.info("timeStart:"+startTime);
			logger.info("timeEnd:"+endTime);
			logger.info("mac:"+mac);
			List<Track> trackList = trackDao.getPersonTrack(trackVO);
			logger.info("trackList长度:"+trackList.size());
			boolean firstFlag=true;
			long floorStartTime = 0;
			long floorEndTime = 0;
			String lastFloor = null;
			
			JSONArray temporary = null;
			boolean flagNull = false;
			for(Track track:trackList) {
				flagNull = true ;
				double x = track.getCorx();
				double y = track.getCory();
				double[] newPos = PositionConvertUtil.convert(building, x, y);
				long time = track.getTime();
				endTime = time ;
				String floor = track.getFloor();
				if(firstFlag){
					floorStartTime=time;
					startTime = floorStartTime;
					lastFloor = floor;
					firstFlag=false;
					temporary = new JSONArray();
				}
				JSONObject obj = new JSONObject();
				obj.put("x", newPos[0]);
				obj.put("y", newPos[1]);
				obj.put("time", time);
				obj.put("floor", floor);
				/**
				 * 定位轨迹按楼层分组
				 */
				if(!floor.equals(lastFloor)){
			    	 timeDuration = floorEndTime-floorStartTime;
			    	 if(timeDuration >= 60000){
			    		 arr.add(temporary);
			    		 JSONObject jsonObject=new JSONObject();
						 allTimeDuration += timeDuration;
						 jsonObject.put("floor", lastFloor);
						 jsonObject.put("timeDuration",timeDuration);
						 jsonObject.put("floorEndTime", floorEndTime);
						 jsonObject.put("floorStartTime", floorStartTime);
						 floorTime.add(jsonObject);
			    	 }
			    	 temporary=new JSONArray();
			    	 floorStartTime=time;
			    }
			     temporary.add(obj);
				 floorEndTime=time;
				 lastFloor = floor;
			}
			timeDuration = floorEndTime-floorStartTime;
            if (flagNull == true && timeDuration >= 60000) {
				 arr.add(temporary);
				 JSONObject jsonObject=new JSONObject();
				 jsonObject.put("floor", lastFloor);
				 jsonObject.put("floorEndTime", floorEndTime);
				 jsonObject.put("floorStartTime", floorStartTime);
				 timeDuration = floorEndTime-floorStartTime;
				 allTimeDuration += timeDuration ;
				 jsonObject.put("timeDuration",timeDuration);
				 floorTime.add(jsonObject);
			}
		}
		//String tempfloor = null;
		int arrLen = arr.size();
		if (arrLen > 0){
			/**
			 * 合并前后相同的楼层数据，同时楼层时间信息数组也需要合并更新
			 */
			JSONArray arrNew = new JSONArray();
			arrNew.add(arr.getJSONArray(0));
			JSONArray floorTimeNew = new JSONArray();
			floorTimeNew.add(floorTime.getJSONObject(0));
			String preFloor = floorTime.getJSONObject(0).getString("floor");
			for(int i=1;i<arrLen;i++){
				JSONObject curFloorTime = floorTime.getJSONObject(i);
				String curFloor = curFloorTime.getString("floor");
				if(curFloor.equals(preFloor)){
					//拷贝当前楼层的轨迹到前一个楼层的数组中(类似追加操作）
					for(int j=0;j<arr.getJSONArray(i).size();j++){
						arrNew.getJSONArray(arrNew.size()-1).add(arr.getJSONArray(i).getJSONObject(j));
					}
					//更新楼层的结束时间和时间长度值
					long newFloorEndTime = curFloorTime.getLong("floorEndTime");
					long floorStartTime = floorTimeNew.getJSONObject(floorTimeNew.size()-1).getLong("floorStartTime");
					floorTimeNew.getJSONObject(floorTimeNew.size()-1).put("floorEndTime", newFloorEndTime);
					floorTimeNew.getJSONObject(floorTimeNew.size()-1).put("timeDuration", newFloorEndTime-floorStartTime);
				}else{
					//前后楼层不一致时，直接把当前楼层轨迹数组和楼层时间信息JSON拷贝到容器中，并更新下一次待比较的楼层编号
					arrNew.add(arr.getJSONArray(i));
					floorTimeNew.add(curFloorTime);
					preFloor = curFloor;
				}
			}
			//合并完毕后修改指向，从而兼容后续代码
			arr = arrNew;
			floorTime = floorTimeNew;

            long w =0;
            double ww = 0;
            JSONArray  time = new JSONArray();
			for(int i = 0;i< floorTime.size();i++){
				JSONObject width = new JSONObject();
				JSONObject jOb = floorTime.getJSONObject(i);

				width.put("floor", jOb.get("floor"));
				width.put("floorEndTime", jOb.get("floorEndTime"));
				width.put("floorStartTime", jOb.get("floorStartTime"));
				w = jOb.getLong("timeDuration");
				ww = (w*100.0/(endTime - startTime))+308.0;
				width.put("width", ww);
				time.add(width);
			}
			result.put("timeQueryStart", startTime);
			result.put("timeQueryEnd", endTime);
			result.put("endTime", endTime);
			result.put("startTime", startTime);
			result.put("data", arr);
			result.put("floorOrder", time);
		}
		return result;
	}
	
	
}
