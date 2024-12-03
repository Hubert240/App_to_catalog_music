package com.catalog.catalog.mapper;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.rest.dto.AudioDto;
import com.catalog.catalog.rest.dto.AudioSummaryDto;
import com.catalog.catalog.rest.dto.CreateAudioRequest;
import com.catalog.catalog.rest.dto.UploadAudioRequest;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
                audio.getYear() != null ? audio.getYear() : 0,
                audio.getGenreDescription(),
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
    public AudioSummaryDto toAudioSummaryDto(Audio audio) {
        if (audio == null) {
            return null;
        }

        User user = audio.getUser();
        return new AudioSummaryDto(
                audio.getId(),
                audio.getTitle(),
                audio.getArtist(),
                user,
                audio.getTrack(),
                audio.getAlbum(),
                audio.getYear() != null ? audio.getYear() : 0,
                audio.getGenreDescription(),
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

        File tempFile = null;
        try {
            tempFile = File.createTempFile("audio_", ".mp3");
            createAudioRequest.getAudioFile().transferTo(tempFile);

            Mp3File mp3file = new Mp3File(tempFile);

            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();

                id3v2Tag.setTitle(createAudioRequest.getTitle());
                id3v2Tag.setArtist(createAudioRequest.getArtist());
                id3v2Tag.setAlbum(createAudioRequest.getAlbum());
                id3v2Tag.setYear(String.valueOf(createAudioRequest.getYear()));
                String genreDescription = createAudioRequest.getGenreDescription();
                if (genreDescription != null && !genreDescription.isEmpty()) {
                    id3v2Tag.setGenreDescription(genreDescription);
                } else {
                    id3v2Tag.setGenreDescription("Other");
                }
                    id3v2Tag.setComment(createAudioRequest.getComment());
                id3v2Tag.setLyrics(createAudioRequest.getLyrics());
                id3v2Tag.setComposer(createAudioRequest.getComposer());
                id3v2Tag.setPublisher(createAudioRequest.getPublisher());
                id3v2Tag.setOriginalArtist(createAudioRequest.getOriginalArtist());
                id3v2Tag.setAlbumArtist(createAudioRequest.getAlbumArtist());
                id3v2Tag.setCopyright(createAudioRequest.getCopyright());
                id3v2Tag.setUrl(createAudioRequest.getUrl());
                id3v2Tag.setEncoder(createAudioRequest.getEncoder());

                if (coverArtBytes != null) {
                    id3v2Tag.setAlbumImage(coverArtBytes, "image/jpeg");
                }

                File modifiedFile = new File(tempFile.getParent(), "modified_audio.mp3");
                mp3file.save(modifiedFile.getAbsolutePath());

                try (InputStream is = new FileInputStream(modifiedFile)) {
                    audioFileBytes = is.readAllBytes();
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
                    createAudioRequest.getYear() != null ? createAudioRequest.getYear() : 0,
                    createAudioRequest.getGenreDescription(),
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

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read audio metadata", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
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
                null,
                null,
                audioFileBytes,
                null,
                user,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


}