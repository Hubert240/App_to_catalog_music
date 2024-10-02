package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;

import java.util.List;

public interface AudioService {

    List<Audio> getAudio();

    List<Audio> getAudioContainingTitle(String title);

    Audio saveAudio(Audio audio);

    void deleteAudio(Audio audio);
}
