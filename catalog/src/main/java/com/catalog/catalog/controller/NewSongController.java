package com.catalog.catalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewSongController {
    @GetMapping("/newSong")
    private String newSong(){
        return "addsong";
    }
}
