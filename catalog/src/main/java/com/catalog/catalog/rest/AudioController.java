package com.catalog.catalog.rest;

import com.catalog.catalog.mapper.AudioMapper;
import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.repository.AudioRepository;
import com.catalog.catalog.rest.dto.*;
import com.catalog.catalog.service.AudioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.catalog.catalog.repository.UserRepository;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.mpatric.mp3agic.*;
import org.springframework.web.multipart.MultipartFile;

import static com.catalog.catalog.config.SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioService audioService;

    private final AudioMapper audioMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioRepository audioRepository;
/*
    @Operation(security = {@SecurityRequirement(name=BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping
    public List<AudioDto> getAudio(@RequestParam(value ="title", required = false) String title){
        List<Audio> audio = (title ==null) ? audioService.getAudio() : audioService.getAudioContainingTitle(title);
        return audio.stream()
                .map(audioMapper::toAudioDto)
                .collect(Collectors.toList());

    }
*/


    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping
    public List<AudioDto> getAudioByUserId(
            @RequestParam Long userId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "album", required = false) String album) {

        List<Audio> audio;

        if (title == null && year == null && artist == null && album == null) {
            audio = audioService.getAudioByUserId(userId);
        } else if (title != null && year != null && artist != null && album != null) {
            audio = audioService.getAudioByTitleArtistAlbumYearAndUserId(title, artist, album, year, userId);
        } else if (title != null && artist != null && album != null) {
            audio = audioService.getAudioByTitleArtistAlbumAndUserId(title, artist, album, userId);
        } else if (artist != null && album != null && year != null) {
            audio = audioService.getAudioByArtistAlbumYearAndUserId(artist, album, year, userId);
        } else if (title != null && year != null) {
            audio = audioService.getAudioByTitleYearAndUserId(title, year, userId);
        } else if (title != null && artist != null) {
            audio = audioService.getAudioContainingTitleAndUserId(title, userId);
        } else if (year != null) {
            audio = audioService.getAudioByYearAndUserId(year, userId);
        } else if (artist != null) {
            audio = audioService.getAudioByArtistAndUserId(artist, userId);
        } else if (album != null) {
            audio = audioService.getAudioByAlbumAndUserId(album, userId);
        } else {
            audio = audioService.getAudioByUserId(userId);
        }

        return audio.stream()
                .map(audioMapper::toAudioDto)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name=BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/audio/{id}")
    public ResponseEntity<Audio> getAudioById(@PathVariable Long id) {
        Optional<Audio> audio = audioRepository.findById(id);
        if (audio.isPresent()){
            return ResponseEntity.ok(audio.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }

    }


    @Operation(security = {@SecurityRequirement(name= BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"},path="/add")
    public AudioDto createAudio(@Valid @ModelAttribute CreateAudioRequest createAudioRequest){
        Long userId = createAudioRequest.getUserId();

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        Audio audio = audioMapper.toAudio(createAudioRequest,user);

        return audioMapper.toAudioDto(audioService.saveAudio(audio));
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"}, path = "/upload")
    public AudioDto uploadAudio(@Valid @ModelAttribute UploadAudioRequest uploadAudioRequest) {
        Long userId = uploadAudioRequest.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Audio audio = new Audio();
        MultipartFile audioFile = uploadAudioRequest.getAudioFile();

        if (!audioFile.isEmpty()) {
            audio = audioMapper.toUploadAudio(uploadAudioRequest, user);

            File tempFile = null;
            try {
                tempFile = File.createTempFile("audio_", ".mp3");
                audioFile.transferTo(tempFile);

                Mp3File mp3file = new Mp3File(tempFile);
                if (mp3file.hasId3v2Tag()) {
                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                    audio.setTitle(id3v2Tag.getTitle());
                    audio.setArtist(id3v2Tag.getArtist());
                    audio.setTrack(id3v2Tag.getTrack());
                    audio.setAlbum(id3v2Tag.getAlbum());
                    audio.setYear(Integer.parseInt(id3v2Tag.getYear()));
                    audio.setGenre(String.valueOf(id3v2Tag.getGenre()));
                    audio.setComment(id3v2Tag.getComment());
                    audio.setLyrics(id3v2Tag.getLyrics());
                    audio.setComposer(id3v2Tag.getComposer());
                    audio.setPublisher(id3v2Tag.getPublisher());
                    audio.setOriginalArtist(id3v2Tag.getOriginalArtist());
                    audio.setAlbumArtist(id3v2Tag.getAlbumArtist());
                    audio.setCopyright(id3v2Tag.getCopyright());
                    audio.setUrl(id3v2Tag.getUrl());
                    audio.setEncoder(id3v2Tag.getEncoder());
                    byte[] imageData = id3v2Tag.getAlbumImage();
                    if (imageData != null) {
                        String mimeType = id3v2Tag.getAlbumImageMimeType();

                        RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
                        file.write(imageData);
                        file.close();
                    }
                    audio.setCoverArt(imageData);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to read audio metadata");
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }

        audio.setUser(user);
        return audioMapper.toAudioDto(audioService.saveAudio(audio));
    }




    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteAudio(@PathVariable Long id) {
        Optional<Audio> audioOptional = audioRepository.findById(id);
        if(audioOptional.isPresent()){
            audioService.deleteAudio(audioOptional.get());
        }
        else {
            throw new EntityNotFoundException("Nie znaleziono obiektu o takim id:"+ id);
        }
    }



}
