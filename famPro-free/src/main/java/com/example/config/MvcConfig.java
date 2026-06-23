package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/free").setViewName("about");
     }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/integration/*").addResourceLocations("classpath:/template/");
        registry.addResourceHandler("/free/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/free/images/**").addResourceLocations("classpath:/images/");
    }
}



