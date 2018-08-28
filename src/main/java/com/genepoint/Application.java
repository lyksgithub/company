package com.genepoint;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.genepoint.common.utils.PositionConvertUtil;
import com.genepoint.lbsshow.AuthRealm;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableSwagger2
@SpringBootApplication
@MapperScan("com.genepoint.*.dao")
public class Application {
	
	public static void main(String[] args) {
		String path = Application.class.getResource("/").getPath().substring(1);
		try {
			PositionConvertUtil.loadConverters(path + "conf/converters.xml", path + "converter");
			System.out.println("---------"+"converters:path="+path + "conf/converters.xml");
			System.out.println("---------"+"converter:path="+path + "converter");
			System.out.println("---------"+"坐标转换工具加载成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("---------"+"坐标转换文件加载失败");
		}
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public Docket createRestApi() {
	     return new Docket(DocumentationType.SWAGGER_2)
	              .apiInfo(apiInfo())
	              .select()
	              .apis(RequestHandlerSelectors.basePackage("com.genepoint.lbsshow.controller"))
	              .paths(PathSelectors.any())
	              .build();
	}

	private ApiInfo apiInfo() {
	      return new ApiInfoBuilder()
	              .title("LBSSHOW")
	              .description("")
	              .termsOfServiceUrl("http://www.powerlbs.com")
	              .version("1.0")
	              .build();
	}    
	
	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login.jsp");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/building.jsp");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/auth/login", "anon");
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/swagger-resources", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		// <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		System.out.println("Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}
    
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		AuthRealm authRealm = new AuthRealm();
		securityManager.setRealm(authRealm);
		return securityManager;
	}
}
