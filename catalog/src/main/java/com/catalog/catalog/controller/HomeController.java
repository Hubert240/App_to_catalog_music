package com.catalog.catalog.controller;


import com.catalog.catalog.model.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private List<Song> songs = List.of(
            new Song("a","b","https://muzikercdn.com/uploads/products/10521/1052154/e7aed8a5.jpg"),
            new Song("a","b","https://muzikercdn.com/uploads/products/10521/1052154/e7aed8a5.jpg"),
            new Song("a","b","https://muzikercdn.com/uploads/products/10521/1052154/e7aed8a5.jpg")
    );


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("songs",songs);
        return "home";
    }
}
