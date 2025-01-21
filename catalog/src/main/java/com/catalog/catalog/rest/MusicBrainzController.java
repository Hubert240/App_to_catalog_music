package com.catalog.catalog.rest;

import com.catalog.catalog.mapper.AudioMapper;
import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.repository.AudioRepository;
import com.catalog.catalog.repository.UserRepository;
import com.catalog.catalog.rest.dto.AudioDto;
import com.catalog.catalog.rest.dto.RecordingsResponse;
import com.catalog.catalog.rest.dto.UploadAudioRequest;
import com.catalog.catalog.service.AudioService;
import com.catalog.catalog.service.MusicBrainzService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

import static com.catalog.catalog.config.SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME;



@RequestMapping("/music")
@RestController
public class MusicBrainzController {

    private final MusicBrainzService musicBrainzService;
    private final AudioService audioService;
    private final AudioMapper audioMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    public MusicBrainzController(MusicBrainzService musicBrainzService, AudioService audioService, AudioMapper audioMapper) {
        this.musicBrainzService = musicBrainzService;
        this.audioService = audioService;
        this.audioMapper = audioMapper;
    }

    @GetMapping
    public RecordingsResponse getRecording(@RequestParam String title, @RequestParam String artist) {
        return musicBrainzService.getRecordingInfo(title, artist);
    }


    @GetMapping("/{releaseId}")
    public ResponseEntity<String> getThumbnail250(@PathVariable String releaseId) {
        String thumbnail250 = musicBrainzService.getThumbnail250(releaseId);
        return ResponseEntity.ok(thumbnail250);
    }


    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"}, path = "/uploadFile")
    public AudioDto uploadSearchAudio(@Valid @ModelAttribute UploadAudioRequest uploadAudioRequest) {
        Long userId = uploadAudioRequest.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Audio audio = new Audio();
        MultipartFile audioFile = uploadAudioRequest.getAudioFile();

        if (!audioFile.isEmpty()) {
            audio = audioMapper.toUploadAudio(uploadAudioRequest, user);

            String name = audioFile.getOriginalFilename();
            name.replaceAll("\\s+", "");
            if (name.contains("-")) {
                name = name.split("\\.")[0];
                String[] array = name.split("-");
                String artist = array[0];
                String title = array[1];
                RecordingsResponse result = musicBrainzService.getRecordingInfoJson(title, artist);

                if (result != null && result.getRecordings() != null && !result.getRecordings().isEmpty()) {
                    var recording = result.getRecordings().get(0);
                    audio.setTitle(recording.getTitle());
                    audio.setArtist(recording.getArtistCredit().get(0).getArtist().getName());
                    audio.setAlbum(recording.getReleases().get(0).getTitle());
                    audio.setComment(recording.getArtistCredit().get(0).getArtist().getDisambiguation());
                    audio.setComposer(recording.getArtistCredit().get(0).getArtist().getSortName());

                    String date = recording.getReleases().get(0).getDate();
                    if (date != null && !date.isEmpty()) {
                        String yearStr = date.split("-")[0];
                        try {
                            audio.setYear(Integer.parseInt(yearStr));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid year format: " + yearStr);
                        }
                    }

                    audio.setTrack(recording.getReleases().get(0).getMedia().get(0).getTrack().get(0).getNumber());
                    String releaseId = recording.getReleases().get(0).getId();

                    if (releaseId != null) {
                        String coverArtUrl = musicBrainzService.getThumbnail250(releaseId);
                        if (coverArtUrl != null) {
                            try {
                                byte[] coverArtData = downloadImage(coverArtUrl);
                                audio.setCoverArt(coverArtData);
                            } catch (Exception e) {
                                System.out.println("Failed to download cover art: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Cover art URL not available for release ID: " + releaseId);
                        }
                    } else {
                        System.out.println("Release ID not available in recording data.");
                    }
                } else {
                    System.out.println("No recordings found for title: " + title + ", artist: " + artist);
                }
            }
        }


        audio.setUser(user);
        return audioMapper.toAudioDto(audioService.saveAudio(audio));
    }

    private byte[] downloadImage(String imageUrl) throws Exception {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            return inputStream.readAllBytes();
        }
    }

}


