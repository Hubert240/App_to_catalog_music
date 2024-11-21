package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;

import java.util.List;

public interface AudioService {

    List<Audio> getAudio();

    List<Audio> getAudioByUserId(Long userId);

    List<Audio> getAudioContainingTitle(String title);

    List<Audio> getAudioByTitleAndUserId(String title,Long userId);

    List<Audio> getAudioByTitleYearAndUserId(String title, int year, Long userId);

    List<Audio> getAudioByYearAndUserId(int year, Long userId);
    List<Audio> getAudioByArtistAndUserId(String artist, Long userId);
    List<Audio> getAudioByAlbumAndUserId(String album, Long userId);
    List<Audio> getAudioByTitleArtistAlbumYearAndUserId(String title, String artist, String album, int year, Long userId);


    List<Audio> getAudioByTitleArtistYearAndUserId(String title, String artist, int year, Long userId);
    List<Audio> getAudioByTitleAlbumYearAndUserId(String title, String album, int year, Long userId);
    List<Audio> getAudioByTitleArtistAndUserId(String title, String artist, Long userId);
    List<Audio> getAudioByTitleAlbumAndUserId(String title, String album, Long userId);
    List<Audio> getAudioByArtistAlbumYearAndUserId(String artist, String album, int year, Long userId);

    Audio saveAudio(Audio audio);

    void deleteAudio(Audio audio);

    List<Audio> getAudioByTitleArtistAlbumAndUserId(String title, String artist, String album, Long userId);
}
