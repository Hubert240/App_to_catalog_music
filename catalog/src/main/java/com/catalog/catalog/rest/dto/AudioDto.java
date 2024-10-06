package com.catalog.catalog.rest.dto;

import com.catalog.catalog.model.User;

public record AudioDto(
        Long id,
        String title,
        String artist,
        byte[] audioFile,
        byte[] coverArt,
        User user,
        String track,
        String album,
        String year,
        String genre,
        String comment,
        String lyrics,
        String composer,
        String publisher,
        String originalArtist,
        String albumArtist,
        String copyright,
        String url,
        String encoder
) {
}
