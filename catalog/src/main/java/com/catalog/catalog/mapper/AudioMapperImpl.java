package com.catalog.catalog.mapper;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.rest.dto.AudioDto;
import com.catalog.catalog.rest.dto.CreateAudioRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AudioMapperImpl implements AudioMapper{


    @Override
    public AudioDto toAudioDto(Audio audio) {
        if (audio == null) {
            return null;
        }

        User user = audio.getUser();
        if (user != null) {
            return new AudioDto(audio.getId(), audio.getTitle(), audio.getArtist(), audio.getAudioFile(),audio.getCoverArt(), user);
        } else {
            return new AudioDto(audio.getId(), audio.getTitle(), audio.getArtist(), audio.getAudioFile(), audio.getCoverArt(), null);
        }


    }


    @Override
    public Audio toAudio(CreateAudioRequest createAudioRequest, User user) {
        if (createAudioRequest == null) {
            return null;
        }

        byte[] audioFileBytes = null;
        if (createAudioRequest.getAudioFile() != null) {
            try {
                audioFileBytes = createAudioRequest.getAudioFile().getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert audio file", e);
            }
        }
        byte[] coverArtBytes = null;
        if (createAudioRequest.getCoverArt() != null) {
            try {
                coverArtBytes = createAudioRequest.getCoverArt().getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert cover art", e);
            }
        }


        return new Audio(createAudioRequest.getArtist(), createAudioRequest.getTitle(), audioFileBytes,coverArtBytes, user);
    }

}