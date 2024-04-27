package com.catalog.catalog.controller;

import com.catalog.catalog.model.Song;
import com.catalog.catalog.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewSongController {

    private final SongRepository songRepository;

    @Autowired
    public NewSongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping("/newSong")
    private String newSong(){
        return "addsong";
    }

    @PostMapping
    private String addSong(Song song){
        songRepository.save(song);
        return "redirect:/";

    };
}
