package ru.memman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/front/welcome").setViewName("Welcome");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/front/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/front/images/**").addResourceLocations("classpath:/images/");
//        registry.addResourceHandler("/front/welcome/**").addResourceLocations("/front/welcome/");
    }
}



