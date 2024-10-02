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

    @Lob
    private byte[] audioFile;

    @Lob
    private byte[] coverArt;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    public Audio(String artist, String title, byte[] audioFile,byte[] coverArt,User user) {
        this.artist = artist;
        this.title = title;
        this.audioFile = audioFile;
        this.user=user;
        this.coverArt=coverArt;
    }
}
