package com.genepoint.lbsshow.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.model.Building;
import com.genepoint.lbsshow.service.BuildingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;

@Api("building")
@RestController
@RequestMapping("/building")
public class BuildingController {

	@Autowired
	BuildingService buildingService;
	@Autowired
	private HttpSession session;
	
	@GetMapping("/get_all_building")
	public ResultUtils getBuildingAll() {
		List<Building> allBuilding = buildingService.getAllBuilding();
		if(allBuilding.size() <= 0) {
			return ResultUtils.create(201, "未查到结果");
		}
		return ResultUtils.create(200, "查询成功", allBuilding);
	}
	
	@PostMapping("/switch_building")
	@ApiImplicitParam(name="building",value="building",dataType="String",required=true,paramType="query")
	public ResultUtils switchBuilding(@RequestParam("building") String building){
		if(Strings.isBlank(building)) {
			return ResultUtils.create(500,"building不能为null");
		}
		Building switchBuilding = buildingService.switchBuilding(building);
		if(switchBuilding == null) {
			return ResultUtils.create(201,"未查到结果");
		}
		
		session.setAttribute("buildingCode", building);
		session.setAttribute("buildingName", switchBuilding.getBuildingName());
		session.setAttribute("floorList", switchBuilding.getFloors());
		return ResultUtils.create(200,"查询成功",switchBuilding);
	}
	
}
