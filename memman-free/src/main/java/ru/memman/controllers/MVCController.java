package ru.memman.controllers;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/free/page")
@AllArgsConstructor
public class MVCController {


    @GetMapping("/about")
    public String getAbout(){
        return "AboutPage";
    }
    @GetMapping("/front")
    public String getFrontPage() {
        return "MainPage";

    }
}
