package com.genepoint.lbsshow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genepoint.common.result.ResultUtils;

import io.swagger.annotations.Api;

@Api("UserPortrait")
@RestController
@RequestMapping("/userportrait")
public class UserPortraitController {

	@PostMapping("/")
	public ResultUtils getUserPortrait(String building,String data,String shopId) {
		if(data.equals("marketData")) {
			
		}else {
			
		}
		
		return ResultUtils.create(200, "成功");
	}
	
}
