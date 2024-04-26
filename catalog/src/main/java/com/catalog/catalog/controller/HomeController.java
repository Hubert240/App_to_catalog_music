package com.catalog.catalog.controller;


import com.catalog.catalog.model.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("song",new Song("a","b","c"));
        return "home";
    }
}
