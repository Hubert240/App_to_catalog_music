package com.catalog.catalog;

import com.catalog.catalog.model.Song;
import com.catalog.catalog.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Dbinit implements CommandLineRunner {

    private final SongRepository songRepository;

    @Autowired
    public Dbinit(SongRepository songRepository){
        this.songRepository = songRepository;
    }
    @Override
    public void run(String... args) throws Exception{
        songRepository.saveAll((List.of(
                new Song("a", "b", "https://muzikercdn.com/uploads/products/10521/1052154/e7aed8a5.jpg"),
                new Song("a", "b", "https://muzikercdn.com/uploads/products/10521/1052154/e7aed8a5.jpg"),
                new Song("a", "b", "https://muzikercdn.com/uploads/products/10521/1052154/e7aed8a5.jpg")
        )));

    }
}
