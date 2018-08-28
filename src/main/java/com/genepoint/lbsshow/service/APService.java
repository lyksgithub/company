package com.genepoint.lbsshow.service;

import com.alibaba.fastjson.JSONObject;
import com.genepoint.lbsshow.model.APStatusResult;


public interface APService {

	/**
	 * 获取Ap定位信息
	 * @param building
	 * @param floor
	 * @return
	 */
	JSONObject getApPosition(String building,String floor);
	/**
	 * 获取Ap工作状态
	 * @param building
	 * @param floor
	 * @param time
	 * @return
	 */
	APStatusResult getApEcharts(String building,String floor,String time);
	
}
