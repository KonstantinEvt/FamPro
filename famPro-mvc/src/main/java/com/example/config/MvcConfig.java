package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/cabinet").setViewName("WelcomeRu");
        registry.addViewController("/cabinetRu").setViewName("WelcomeRuOld");
//        registry.addViewController("/cabinet/localisation").setViewName("Welcome");
        registry.addViewController("/cabinet/create").setViewName("Create-user");
        registry.addViewController("/base").setViewName("Connect-to-base");
        registry.addViewController("/cabinet/edit").setViewName("Edit-user");
        registry.addViewController("/family_member/add").setViewName("CreateNewFamilyMember");
        registry.addViewController("/family_member/edit").setViewName("EditFamilyMember");
        registry.addViewController("/rules").setViewName("RuRules");
//        registry.addViewController("/cabinet/localisation").setViewName("Welcome");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/cabinet/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/cabinet/images/**").addResourceLocations("classpath:/images/");
    }
}



