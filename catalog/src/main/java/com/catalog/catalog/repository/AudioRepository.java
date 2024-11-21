package com.catalog.catalog.repository;

import com.catalog.catalog.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.List;

public interface AudioRepository extends JpaRepository<Audio, Long> {

    List<Audio> findByTitleContainingIgnoreCase(String title);
    List<Audio> findByUserId(Long userId);
    List<Audio> findByTitleContainingIgnoreCaseAndUserId(String title, Long userId);
    List<Audio> findByYearAndUserId(int year, Long userId);
    List<Audio> findByTitleContainingAndYearAndUserId(String title, int year, Long userId);

    List<Audio> findByArtistContainingIgnoreCaseAndUserId(String artist, Long userId);
    List<Audio> findByAlbumContainingIgnoreCaseAndUserId(String album, Long userId);
    List<Audio> findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndYearAndUserId(
            String title, String artist, String album, int year, Long userId
    );
    List<Audio> findByTitleContainingAndArtistContainingAndAlbumContainingAndUserId(
            String title, String artist, String album, Long userId
    );
    List<Audio> findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndYearAndUserId(
            String title, String artist, int year, Long userId
    );
    List<Audio> findByTitleContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndYearAndUserId(
            String title, String album, int year, Long userId
    );
    List<Audio> findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndUserId(
            String title, String artist, Long userId
    );
    List<Audio> findByTitleContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndUserId(
            String title, String album, Long userId
    );
    List<Audio> findByArtistContainingIgnoreCaseAndAlbumContainingIgnoreCaseAndYearAndUserId(
            String artist, String album, int year, Long userId
    );
}
