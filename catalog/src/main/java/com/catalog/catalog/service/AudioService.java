package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;

import java.util.List;

public interface AudioService {

    List<Audio> getAudio();

    List<Audio> getAudioByUserId(Long userId);

    List<Audio> getAudioContainingTitle(String title);

    List<Audio> getAudioContainingTitleAndUserId(String title,Long userId);

    Audio saveAudio(Audio audio);

    void deleteAudio(Audio audio);
}
