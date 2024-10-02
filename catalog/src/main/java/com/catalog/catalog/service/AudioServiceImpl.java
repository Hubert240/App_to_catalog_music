package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.repository.AudioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AudioServiceImpl implements AudioService{

    private final AudioRepository audioRepository;

    @Transactional
    @Override
    public List<Audio> getAudio() {
        return audioRepository.findAll();

    }

    @Transactional
    @Override
    public Audio saveAudio(Audio audio){return audioRepository.save(audio);}

    @Transactional
    @Override
    public void deleteAudio(Audio audio){audioRepository.delete(audio);}

    @Transactional
    @Override
    public List<Audio> getAudioContainingTitle(String title){
        return audioRepository.findByTitleContainingIgnoreCase(title);

    }
}
