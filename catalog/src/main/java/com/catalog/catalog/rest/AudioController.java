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
import org.springframework.web.bind.annotation.*;
import com.catalog.catalog.repository.UserRepository;

import java.io.File;
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

    @Operation(security = {@SecurityRequirement(name=BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping
    public List<AudioDto> getAudio(@RequestParam(value ="title", required = false) String title){
        List<Audio> audio = (title ==null) ? audioService.getAudio() : audioService.getAudioContainingTitle(title);
        return audio.stream()
                .map(audioMapper::toAudioDto)
                .collect(Collectors.toList());

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
