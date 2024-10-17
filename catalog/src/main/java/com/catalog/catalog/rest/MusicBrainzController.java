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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.RandomAccessFile;

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
    public RecordingsResponse getRecording(@RequestParam String title,@RequestParam String artist) {
        return musicBrainzService.getRecordingInfo(title, artist);
    }



    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"}, path = "/uploadFile")
    public AudioDto uploadSearchAudio(@Valid @ModelAttribute UploadAudioRequest uploadAudioRequest) {
        Long userId = uploadAudioRequest.getUserId();
        System.out.println("test");

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
                    audio.setTitle(result.getRecordings().get(0).getTitle());
                    audio.setArtist(result.getRecordings().get(0).getArtistCredit().get(0).getArtist().getName());
                    audio.setAlbum(result.getRecordings().get(0).getReleases().get(0).getTitle());
                    audio.setComment(result.getRecordings().get(0).getArtistCredit().get(0).getArtist().getDisambiguation());
                    audio.setComposer(result.getRecordings().get(0).getArtistCredit().get(0).getArtist().getSortName());
                    audio.setYear(result.getRecordings().get(0).getReleases().get(0).getDate());
                    audio.setTrack(result.getRecordings().get(0).getReleases().get(0).getMedia().get(0).getTrack().get(0).getNumber());

                } else {
                    System.out.println("No recordings found.");
                }
            }



            }

            audio.setUser(user);
            return audioMapper.toAudioDto(audioService.saveAudio(audio));
        }}


