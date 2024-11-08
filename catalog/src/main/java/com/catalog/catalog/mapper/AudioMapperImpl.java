package com.catalog.catalog.mapper;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.rest.dto.AudioDto;
import com.catalog.catalog.rest.dto.CreateAudioRequest;
import com.catalog.catalog.rest.dto.UploadAudioRequest;
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
        return new AudioDto(
                audio.getId(),
                audio.getTitle(),
                audio.getArtist(),
                audio.getAudioFile(),
                audio.getCoverArt(),
                user,
                audio.getTrack(),
                audio.getAlbum(),
                audio.getYear(),
                audio.getGenre(),
                audio.getComment(),
                audio.getLyrics(),
                audio.getComposer(),
                audio.getPublisher(),
                audio.getOriginalArtist(),
                audio.getAlbumArtist(),
                audio.getCopyright(),
                audio.getUrl(),
                audio.getEncoder()
        );
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


        return new Audio(
                createAudioRequest.getArtist(),
                createAudioRequest.getTitle(),
                audioFileBytes,
                coverArtBytes,
                user,
                createAudioRequest.getTrack(),
                createAudioRequest.getAlbum(),
                createAudioRequest.getYear(),
                createAudioRequest.getGenre(),
                createAudioRequest.getComment(),
                createAudioRequest.getLyrics(),
                createAudioRequest.getComposer(),
                createAudioRequest.getPublisher(),
                createAudioRequest.getOriginalArtist(),
                createAudioRequest.getAlbumArtist(),
                createAudioRequest.getCopyright(),
                createAudioRequest.getUrl(),
                createAudioRequest.getEncoder()
        );
    }

    @Override
    public Audio toUploadAudio(UploadAudioRequest uploadAudioRequest, User user) {
        if (uploadAudioRequest == null) {
            return null;
        }

        byte[] audioFileBytes = null;
        if (uploadAudioRequest.getAudioFile() != null) {
            try {
                audioFileBytes = uploadAudioRequest.getAudioFile().getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert audio file", e);
            }
        }

        return new Audio(
                "Unknown Artist",
                "Unknown Title",
                audioFileBytes,
                null,
                user,
                "Unknown Track",
                "Unknown Album",
                0,
                "Unknown Genre",
                "No Comment",
                "No Lyrics",
                "Unknown Composer",
                "Unknown Publisher",
                "Unknown Original Artist",
                "Unknown Album Artist",
                "No Copyright",
                "No URL",
                "Unknown Encoder"  
        );
    }


}