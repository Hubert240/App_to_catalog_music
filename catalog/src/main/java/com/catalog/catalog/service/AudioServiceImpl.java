package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.repository.AudioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.catalog.catalog.specification.AudioSpecification;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;

    @Override
    public Page<Audio> getAudio(Pageable pageable) {
        return audioRepository.findAll(pageable);
    }

    @Override public Integer getAudioNumber(Long userId)
    { return audioRepository.countByUserId(userId); }

    @Override
    public Page<Audio> getFilteredAudio(String title, Integer year, String artist, String album, Long userId, Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "id")
            );
        }

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
