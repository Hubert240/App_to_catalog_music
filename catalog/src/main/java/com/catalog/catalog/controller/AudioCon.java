package com.catalog.catalog.controller;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.repository.AudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AudioCon{

    @Autowired
    private AudioRepository audioRepository;

    @GetMapping("/audio")
    public List<Audio> getAllAudio() {
        return audioRepository.findAll();
    }
}
