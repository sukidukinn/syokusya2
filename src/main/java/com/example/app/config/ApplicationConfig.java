package com.example.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
	@Value("${upload.path}")
    private String uploadPath;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String uploadDir = uploadPath;
		//uploadDir = "file:///C:/Users/zd1T04/Desktop/test/飯写システム/uploads/";
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:///" + uploadPath.replace("\\", "/"));
	}
}