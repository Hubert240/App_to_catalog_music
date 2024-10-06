package com.catalog.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="audio")
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String track;
    private String album;
    private String year;
    private String genre;
    private String comment;
    private String lyrics;
    private String composer;
    private String publisher;
    private String originalArtist;
    private String albumArtist;
    private String copyright;
    private String url;
    private String encoder;

    @Lob
    private byte[] audioFile;

    @Lob
    private byte[] coverArt;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    public Audio(String artist, String title, byte[] audioFile, byte[] coverArt, User user, String track, String album, String year, String genre, String comment, String lyrics, String composer, String publisher, String originalArtist, String albumArtist, String copyright, String url, String encoder) {
        this.artist = artist;
        this.title = title;
        this.audioFile = audioFile;
        this.coverArt = coverArt;
        this.user = user;
        this.track = track;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.comment = comment;
        this.lyrics = lyrics;
        this.composer = composer;
        this.publisher = publisher;
        this.originalArtist = originalArtist;
        this.albumArtist = albumArtist;
        this.copyright = copyright;
        this.url = url;
        this.encoder = encoder;
    }
}
