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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.catalog.catalog.repository.UserRepository;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<AudioSummaryDto> getFilteredAudio(
            @RequestParam Long userId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {


        Pageable pageable = PageRequest.of(page, size);

        Page<Audio> audioPage = audioService.getFilteredAudio(title, year, artist, album, userId, pageable);


        System.out.println("Fetched " + audioPage.getContent().size() + " audio items for page " + page + " of size " + size);


        return audioPage.getContent().stream()
                .map(audioMapper::toAudioSummaryDto)
                .collect(Collectors.toList());
    }


    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/audio/{id}")
    public ResponseEntity<Audio> getAudioById(@PathVariable Long id) {
        Optional<Audio> audio = audioRepository.findById(id);
        if (audio.isPresent()) {
            return ResponseEntity.ok(audio.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {"multipart/form-data"}, path = "/add")
    public AudioDto createAudio(@Valid @ModelAttribute CreateAudioRequest createAudioRequest) {
        Long userId = createAudioRequest.getUserId();

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        Audio audio = audioMapper.toAudio(createAudioRequest, user);

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
                String originalFilename = audioFile.getOriginalFilename();
                String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".audio";
                tempFile = File.createTempFile("audio_", fileExtension);
                audioFile.transferTo(tempFile);

                // Wczytanie metadanych za pomocą JAudioTagger
                org.jaudiotagger.audio.AudioFile jaudioFile = org.jaudiotagger.audio.AudioFileIO.read(tempFile);
                org.jaudiotagger.tag.Tag tag = jaudioFile.getTag();

                if (tag != null) {
                    audio.setTitle(tag.getFirst(org.jaudiotagger.tag.FieldKey.TITLE));
                    audio.setArtist(tag.getFirst(org.jaudiotagger.tag.FieldKey.ARTIST));
                    audio.setTrack(tag.getFirst(org.jaudiotagger.tag.FieldKey.TRACK));
                    audio.setAlbum(tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM));

                    String yearString = tag.getFirst(org.jaudiotagger.tag.FieldKey.YEAR);
                    if (!yearString.isEmpty()) {
                        audio.setYear(Integer.parseInt(yearString));
                    }

                    audio.setGenreDescription(tag.getFirst(org.jaudiotagger.tag.FieldKey.GENRE));
                    audio.setComment(tag.getFirst(org.jaudiotagger.tag.FieldKey.COMMENT));
                    audio.setLyrics(tag.getFirst(org.jaudiotagger.tag.FieldKey.LYRICS));
                    audio.setComposer(tag.getFirst(org.jaudiotagger.tag.FieldKey.COMPOSER));
                    audio.setOriginalArtist(tag.getFirst(org.jaudiotagger.tag.FieldKey.ORIGINAL_ARTIST));
                    audio.setAlbumArtist(tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM_ARTIST));
                    audio.setUrl(tag.getFirst(org.jaudiotagger.tag.FieldKey.URL_OFFICIAL_RELEASE_SITE));
                    audio.setEncoder(tag.getFirst(org.jaudiotagger.tag.FieldKey.ENCODER));

                    // Obsługa okładki albumu (cover art)
                    List<org.jaudiotagger.tag.images.Artwork> artworkList = tag.getArtworkList();
                    if (!artworkList.isEmpty()) {
                        org.jaudiotagger.tag.images.Artwork artwork = artworkList.get(0);
                        byte[] imageData = artwork.getBinaryData();
                        if (imageData != null) {
                            audio.setCoverArt(imageData);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to read audio metadata", e);
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
    public void deleteAudio(@PathVariable Long id) {
        Optional<Audio> audioOptional = audioRepository.findById(id);
        if (audioOptional.isPresent()) {
            audioService.deleteAudio(audioOptional.get());
        } else {
            throw new EntityNotFoundException("Nie znaleziono obiektu o takim id:" + id);
        }
    }


    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAudioFile(@PathVariable Long id) {
        try {
            Optional<Audio> audioOptional = audioRepository.findById(id);

            if (audioOptional.isPresent()) {
                Audio audio = audioOptional.get();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + audio.getTitle() + ".mp3")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(audio.getAudioFile());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}