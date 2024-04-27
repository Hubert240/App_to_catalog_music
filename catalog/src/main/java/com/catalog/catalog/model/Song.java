package com.catalog.catalog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Song {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String artist;
    private String album;

    public Song(String name, String artist, String album){
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

}
