package com.genepoint.lbsshow.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genepoint.common.result.ResultUtils;
import com.genepoint.lbsshow.AuthRealm;

import io.swagger.annotations.Api;

@Api("auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private HttpSession httpSession;
	
	@PostMapping("/login")
	public ResultUtils login(String email,String encryptedPwd) {
		AuthRealm authRealm = new AuthRealm();
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(authRealm);
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(email,encryptedPwd);
		try {
			subject.login(token);
		}catch(Exception e) {
			return ResultUtils.create(500, "登陆失败");
		}
		if(subject.isAuthenticated()) {
			return ResultUtils.create(200, "登陆成功");
		}
		return ResultUtils.create(500, "登陆失败");
	}
	
	@GetMapping("/logout")
	public ResultUtils logout() {
		AuthRealm authRealm = new AuthRealm();
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(authRealm);
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		httpSession.invalidate();
		return ResultUtils.create(200, "登出成功");
	}
}
