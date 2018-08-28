package com.genepoint.lbsshow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genepoint.common.utils.PositionConvertUtil;
import com.genepoint.lbsshow.dao.APMonitorMapper;
import com.genepoint.lbsshow.dao.RedisDao;
import com.genepoint.lbsshow.model.APMonitor;
import com.genepoint.lbsshow.model.APStatusResult;
import com.genepoint.lbsshow.service.APService;

@Service
public class APServiceImpl implements APService {

	@Autowired
	RedisDao redisDao;
	@Autowired
	APMonitorMapper apMonitorDao;
	
	@Override
	public JSONObject getApPosition(String building, String floor) {
		Map<String, String> aps = redisDao.hgetAll(building + "_AP_LOCATION");
		Map<String, String> aps2 = redisDao.hgetAll(building + "_AP_MONITOR_SCAN_NUM");
		Map<String, List<JSONObject>> map = new HashMap<>();
		JSONObject t = null;

		for (String mac : aps.keySet()) {
			String values = aps.get(mac);
			String[] arr = values.split(",");
			if (arr[0].equals(floor)) {
				double x = Double.parseDouble(arr[1]);
				double y = Double.parseDouble(arr[2]);
				// 坐标转换插件
				double[] newPos = PositionConvertUtil.convert(building, x, y);
				List<JSONObject> list = map.get(floor);
				if (list == null) {
					list = new ArrayList<>();
				}
				t = new JSONObject();
				t.put("mac", mac);
				t.put("x", newPos[0]);
				t.put("y", newPos[1]);
				if (aps2.get(mac) == null) {
					t.put("count", 0);
				} else {
					t.put("count", Integer.parseInt(aps2.get(mac)));
				}
				list.add(t);
				map.put(floor, list);
			}
		}
		JSONObject dataJson = new JSONObject();
		for (String floors : map.keySet()) {
			if (floors.equals(floor)) {
				JSONArray jsonArray = new JSONArray();
				for (JSONObject jsonObject : map.get(floor)) {
					jsonArray.add(jsonObject);
				}
				dataJson.put(floor, jsonArray);
			}
		}
		return dataJson;
	}

	@Override
	public APStatusResult getApEcharts(String building,String floor,String time) {
		APStatusResult apStatusResult = new APStatusResult();
		// 横坐标
		List<String> hourAndMin = new ArrayList<>();
		List<String> APs = new ArrayList<>();
		@SuppressWarnings("rawtypes")
		List<List> arrayData = new ArrayList<>();
		List<Integer> maxList = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date;
		try {
			date = dateFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		APMonitor apMonitor = new APMonitor();
		apMonitor.setBuilding(building);
		apMonitor.setFloor(floor);
		apMonitor.setDate(new java.sql.Date(date.getTime()));
		List<APMonitor> apList = apMonitorDao.getAPMonitor(apMonitor);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SS");	
		int num = 0;
		for(APMonitor ap:apList) {
			String apmac = ap.getApmac(); 
			if (apmac != null) {
				APs.add(apmac);
			}	
			String datas = ap.getDatas();
			if (datas != null) {

				// 20170731 15:52:01:00#315,20170731 15:57:01:00#291
				String[] split = datas.split(",");
				if (split != null) {
					for (String string : split) {
						String[] split2 = string.split("#");
						Date date2;
						try {
							date2 = sdf.parse(split2[0]);
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date2);

						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);

						String minuteString = null;
						String hourString = null;
						if (hour<10) {
							hourString = "0"+hour;
						}else {
							hourString = hour+"";
						}
						if(minute < 10){
							minuteString = "0"+minute;
						}else {
							minuteString = minute+"";
						}

						String times = hourString + ":" + minuteString;

						if (num == 0) {
							hourAndMin.add(times);
							maxList.add(Integer.parseInt(split2[1]));
						}

						ArrayList<Object> hourAndMinAndCounts = new ArrayList<>();

						hourAndMinAndCounts.add(num);
						hourAndMinAndCounts.add(times);
						hourAndMinAndCounts.add(Integer.parseInt(split2[1]));

						arrayData.add(hourAndMinAndCounts);

					}
				}

			}

			num++;
		}
		apStatusResult.setAPs(APs);
		apStatusResult.setData(arrayData);
		apStatusResult.setHours(hourAndMin);
		apStatusResult.setNum(num);
		if (maxList.size()>1) {
			Collections.sort(maxList);
			apStatusResult.setMax(maxList.get(maxList.size()-1));
		}else{
			apStatusResult.setMax(0);
		}

		return apStatusResult;
	}

}
