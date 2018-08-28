package com.genepoint;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.genepoint.common.utils.PositionConvertUtil;

public class SpringBootStartApplication extends SpringBootServletInitializer {
	 
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
    	String path = Application.class.getResource("/").getPath();
		try {
			System.out.println("========="+"converters:path="+path + "conf/converters.xml");
			System.out.println("========="+"converter:path="+path + "converter");
			PositionConvertUtil.loadConverters(path + "conf/converters.xml", path + "converter");
			System.out.println("========="+"坐标转换工具加载成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("========="+"坐标转换文件加载失败");
		}
        return builder.sources(Application.class);
    }
}