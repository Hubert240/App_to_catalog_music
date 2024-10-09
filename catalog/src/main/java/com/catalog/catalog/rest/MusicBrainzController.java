package com.catalog.catalog.rest;

import com.catalog.catalog.rest.dto.RecordingsResponse;
import com.catalog.catalog.service.MusicBrainzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MusicBrainzController {

    private final MusicBrainzService musicBrainzService;

    @Autowired
    public MusicBrainzController(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

    @GetMapping("/music")
    public RecordingsResponse getRecording(@RequestParam String title, String artist) {
        return musicBrainzService.getRecordingInfo(title, artist);
    }
}
