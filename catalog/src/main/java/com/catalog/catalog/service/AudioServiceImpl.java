package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.repository.AudioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;

    @Transactional
    @Override
    public List<Audio> getAudio() {
        return audioRepository.findAll();
    }

    @Transactional
    @Override
    public List<Audio> getAudioByUserId(Long userId) {
        return audioRepository.findByUserId(userId);
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

    @Transactional
    @Override
    public List<Audio> getAudioContainingTitle(String title) {
        return audioRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleAndUserId(String title, Long userId) {
        return audioRepository.findByTitleContainingIgnoreCaseAndUserId(title, userId);
    }

    @Transactional
    @Override
    public List<Audio> getAudioByYearAndUserId(int year, Long userId) {
        return audioRepository.findByYearAndUserId(year, userId);
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleYearAndUserId(String title, int year, Long userId) {
        return audioRepository.findByTitleContainingAndYearAndUserId(title, year, userId);
    }

    // Nowe metody

    @Transactional
    @Override
    public List<Audio> getAudioByArtistAndUserId(String artist, Long userId) {
        return audioRepository.findByArtistContainingIgnoreCaseAndUserId(artist, userId);
    }

    @Transactional
    @Override
    public List<Audio> getAudioByAlbumAndUserId(String album, Long userId) {
        return audioRepository.findByAlbumContainingIgnoreCaseAndUserId(album, userId);
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleArtistAlbumYearAndUserId(String title, String artist, String album, int year, Long userId) {
        return audioRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndYearAndUserId(
                title, artist, album, year, userId
        );
    }



    @Transactional
    @Override
    public List<Audio> getAudioByTitleArtistYearAndUserId(String title, String artist, int year, Long userId) {
        return audioRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndYearAndUserId(
                title, artist, year, userId
        );
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleAlbumYearAndUserId(String title, String album, int year, Long userId) {
        return audioRepository.findByTitleContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndYearAndUserId(
                title, album, year, userId
        );
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleArtistAndUserId(String title, String artist, Long userId) {
        return audioRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndUserId(
                title, artist, userId
        );
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleAlbumAndUserId(String title, String album, Long userId) {
        return audioRepository.findByTitleContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndUserId(
                title, album, userId
        );
    }

    @Transactional
    @Override
    public List<Audio> getAudioByArtistAlbumYearAndUserId(String artist, String album, int year, Long userId) {
        return audioRepository.findByArtistContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndYearAndUserId(
                artist, album, year, userId
        );
    }

    @Transactional
    @Override
    public List<Audio> getAudioByTitleArtistAlbumAndUserId(String title, String artist, String album, Long userId){
        return audioRepository.findByTitleContainingAndArtistContainingAndAlbumContainingAndUserId(

                title, artist, album, userId
        );
    }

}
