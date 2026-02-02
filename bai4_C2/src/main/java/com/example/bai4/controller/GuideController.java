package com.example.bai4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuideController {

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/guide")
    public String guide() {
        return "guide";
    }
}
