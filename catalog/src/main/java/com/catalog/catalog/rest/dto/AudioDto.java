package com.catalog.catalog.rest.dto;

import com.catalog.catalog.model.User;

public record AudioDto(Long id, String title, String artist, byte[] audioFile,byte[] coverArt, User user) {
}
