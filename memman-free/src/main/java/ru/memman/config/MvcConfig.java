package ru.memman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/free/page/about").setViewName("AboutPage");
        registry.addViewController("/free/page/main").setViewName("MainPage");
     }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/free/js/**").addResourceLocations("classpath:/js/");
//        registry.addResourceHandler("/free/imj/**").addResourceLocations("classpath:/images/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/images/");
//        registry.addResourceHandler("/texts/ru/**").addResourceLocations("/resources/texts/ru");
//        registry.addResourceHandler("/texts/en/**").addResourceLocations("/resources/texts/en");
    }
}



