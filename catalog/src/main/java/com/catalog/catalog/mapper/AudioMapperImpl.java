package com.catalog.catalog.mapper;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.rest.dto.*;
import com.catalog.catalog.service.AudioService;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.catalog.catalog.service.MusicBrainzService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class AudioMapperImpl implements AudioMapper{

    private final MusicBrainzService musicBrainzService;

    @Autowired
    public AudioMapperImpl(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

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


    @Override
    public Audio toSearchData(UploadAudioRequest uploadAudioRequest, User user) {
        if (uploadAudioRequest == null || uploadAudioRequest.getAudioFile() == null) {
            throw new IllegalArgumentException("UploadAudioRequest or audio file is null");
        }

        byte[] audioFileBytes;
        try {
            audioFileBytes = uploadAudioRequest.getAudioFile().getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert audio file to bytes", e);
        }

        MultipartFile audioFile = uploadAudioRequest.getAudioFile();
        String name = audioFile.getOriginalFilename();
        name.replaceAll("\\s+", "");
            name = name.split("\\.")[0];
            String[] array = name.split("-");
            String artist = array[0];
            String title = array[1];

        RecordingsResponse result = musicBrainzService.getRecordingInfoJson(title, artist);
        byte[] coverArtBytes = null;

        File tempFile = null;
        try {
            tempFile = File.createTempFile("audio_", ".mp3");
            audioFile.transferTo(tempFile);

            Mp3File mp3file = new Mp3File(tempFile);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();


                id3v2Tag.setTitle(title);
                id3v2Tag.setArtist(artist);


                if (result != null && result.getRecordings() != null && !result.getRecordings().isEmpty()) {
                    var recording = result.getRecordings().get(0);

                    if (!recording.getArtistCredit().isEmpty()) {
                        var artistCredit = recording.getArtistCredit().get(0).getArtist();
                        id3v2Tag.setComposer(artistCredit.getSortName());
                        id3v2Tag.setComment(artistCredit.getDisambiguation());
                    }

                    if (!recording.getReleases().isEmpty()) {
                        var release = recording.getReleases().get(0);
                        id3v2Tag.setAlbum(release.getTitle());

                        String date = release.getDate();
                        if (date != null && !date.isEmpty()) {
                            try {
                                id3v2Tag.setYear(String.valueOf(Integer.parseInt(date.split("-")[0])));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid year format: " + date);
                            }
                        }
                        if (!release.getMedia().isEmpty() && !release.getMedia().get(0).getTrack().isEmpty()) {
                            id3v2Tag.setTrack(release.getMedia().get(0).getTrack().get(0).getNumber());
                        }


                        if (coverArtBytes != null) {
                            id3v2Tag.setAlbumImage(coverArtBytes, "image/jpeg");
                        }

                        String releaseId = release.getId();
                        if (releaseId != null) {
                            String coverArtUrl = musicBrainzService.getThumbnail250(releaseId);
                            if (coverArtUrl != null) {
                                try {
                                    coverArtBytes = downloadImage(coverArtUrl);
                                    id3v2Tag.setAlbumImage(coverArtBytes, "image/jpeg");
                                } catch (Exception e) {
                                    System.out.println("Failed to download cover art: " + e.getMessage());
                                }
                            }
                        }
                    }



                }



                File modifiedFile = new File(tempFile.getParent(), "modified_audio.mp3");
                mp3file.save(modifiedFile.getAbsolutePath());

                try (InputStream is = new FileInputStream(modifiedFile)) {
                    audioFileBytes = is.readAllBytes();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing audio file", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        Audio audio = new Audio(
                null,
                title,
                audioFileBytes,
                coverArtBytes,
                user,
                artist,
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

        if (result != null && result.getRecordings() != null && !result.getRecordings().isEmpty()) {
            var recording = result.getRecordings().get(0);

            audio.setTitle(recording.getTitle());
            if (!recording.getArtistCredit().isEmpty()) {
                var artistCredit = recording.getArtistCredit().get(0).getArtist();
                audio.setArtist(artistCredit.getName());
                audio.setComposer(artistCredit.getSortName());
                audio.setComment(artistCredit.getDisambiguation());
            }

            if (!recording.getReleases().isEmpty()) {
                var release = recording.getReleases().get(0);
                audio.setAlbum(release.getTitle());

                String date = release.getDate();
                if (date != null && !date.isEmpty()) {
                    try {
                        audio.setYear(Integer.parseInt(date.split("-")[0]));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid year format: " + date);
                    }
                }

                if (!release.getMedia().isEmpty() && !release.getMedia().get(0).getTrack().isEmpty()) {
                    audio.setTrack(release.getMedia().get(0).getTrack().get(0).getNumber());
                }

                String releaseId = release.getId();
                if (releaseId != null) {
                    String coverArtUrl = musicBrainzService.getThumbnail250(releaseId);
                    if (coverArtUrl != null) {
                        try {
                            coverArtBytes = downloadImage(coverArtUrl);
                            audio.setCoverArt(coverArtBytes);
                        } catch (Exception e) {
                            System.out.println("Failed to download cover art: " + e.getMessage());
                        }
                    }
                }
            }
        } else {
            System.out.println("No recordings found for title: " + title + ", artist: " + artist);
        }

        return audio;
    }



    private byte[] downloadImage(String imageUrl) throws Exception {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            return inputStream.readAllBytes();
        }
    }

}