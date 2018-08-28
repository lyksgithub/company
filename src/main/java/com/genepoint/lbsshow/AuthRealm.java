package com.genepoint.lbsshow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.genepoint.common.utils.FastJsonUtil;
import com.genepoint.common.utils.HttpClientUtils;
import com.genepoint.lbsshow.model.Buildings;
import com.genepoint.lbsshow.model.DataResult;


public class AuthRealm extends AuthorizingRealm{
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		Set<String> buildings = new HashSet<>();
		String data = "{\"email\":\"" + token.getPrincipal() + "\",\"encryptedPwd\":\"" + new String((char[])token.getCredentials()) + "\"}";
		String client = "browser";
		Map<String, String> param = new HashMap<String, String>();
		param.put("data", data);
		param.put("client", client);
		String loginCheck = "http://location.gene-point.com/platform/netdeveloper/netlbsshowlogin";
		String userinfo = HttpClientUtils.doPost(loginCheck, param);
		DataResult dataResult = FastJsonUtil.toBean(userinfo, DataResult.class);
		if (dataResult.getStatus() == 1) {
			List<Buildings> buildings2 = dataResult.getBuildings();
			for (int i = 0; i < buildings2.size(); i++) {
				buildings.add(buildings2.get(i).getBuildingCode());
			}
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("buildings", buildings);
			session.setAttribute("userinfo", userinfo);
		}
		SimpleAuthenticationInfo authenticationInfo = null;
		if(dataResult.getStatus() == 1) {
			authenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(),token.getCredentials(),this.getName());
		}
		return authenticationInfo;
	}

}
