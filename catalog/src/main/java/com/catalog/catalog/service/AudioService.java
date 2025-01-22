package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AudioService {

    Integer getAudioNumber(Long userId);
    Page<Audio> getAudio(Pageable pageable);

    Audio saveAudio(Audio audio);

    void deleteAudio(Audio audio);

    Page<Audio> getFilteredAudio(String title, Integer year, String artist, String album, Long userId, Pageable pageable);
}
