package com.catalog.catalog.controller;


import com.catalog.catalog.model.Song;
import com.catalog.catalog.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final SongRepository songRepository;

    @Autowired
    public HomeController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("songs",songRepository.findAll());
        return "home";
    }
}
