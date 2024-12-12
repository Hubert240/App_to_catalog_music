package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.repository.AudioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.catalog.catalog.specification.AudioSpecification;

@RequiredArgsConstructor
@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;

    @Override
    public Page<Audio> getAudio(Pageable pageable) {
        return audioRepository.findAll(pageable);
    }


    @Override
    public Page<Audio> getFilteredAudio(String title, Integer year, String artist, String album, Long userId, Pageable pageable) {
        return audioRepository.findAll(
                AudioSpecification.filterByCriteria(title, year, artist, album, userId),
                pageable
        );
    }


    @Transactional
    @Override
    public Audio saveAudio(Audio audio) {
        return audioRepository.save(audio);
    }

    @Transactional
    @Override
    public void deleteAudio(Audio audio) {
        audioRepository.delete(audio);
    }


}
