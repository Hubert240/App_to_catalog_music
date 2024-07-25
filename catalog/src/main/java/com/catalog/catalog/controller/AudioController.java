package com.catalog.catalog.controller;


import com.catalog.catalog.model.Audio;
import com.catalog.catalog.repository.AudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class AudioController {

    @Autowired
    private AudioRepository audioRepository;

    @GetMapping("/newSong")
    public String index() {
        return "newSong";
    }


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        Audio audio = new Audio();
        audio.setName(file.getOriginalFilename());
        audio.setFile(file.getBytes());
        audioRepository.save(audio);
        model.addAttribute("message", "File uploaded successfully!");
        return "newSong";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("id") Long id) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + audio.getName());

        return new ResponseEntity<>(audio.getFile(), headers, HttpStatus.OK);
    }

    @GetMapping("/view")
    public String viewFile(@RequestParam("id") Long id, Model model) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        String content = new String(audio.getFile(), StandardCharsets.UTF_8);
        model.addAttribute("fileName", audio.getName());
        model.addAttribute("fileContent", content);
        return "viewFile";
    }

    @GetMapping("/play")
    public String playFile(@RequestParam("id") Long id, Model model) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        model.addAttribute("fileName", audio.getName());
        model.addAttribute("fileId", id);
        return "playFile";
    }




    @GetMapping("/{id}")
    public String getFile(@PathVariable Long id, Model model) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        System.out.println("File Entity: " + audio);
        model.addAttribute("file", audio);
        return "play";
    }


    @GetMapping("/audio/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getAudio(@PathVariable Long id) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        ByteArrayResource resource = new ByteArrayResource(audio.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}