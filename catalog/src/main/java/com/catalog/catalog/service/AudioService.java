package com.catalog.catalog.service;

import com.catalog.catalog.model.Audio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AudioService {

    /**
     * Pobiera wszystkie rekordy z paginacją.
     */
    Page<Audio> getAudio(Pageable pageable);

    /**
     * Zapisuje nowy rekord Audio.
     */
    Audio saveAudio(Audio audio);

    /**
     * Usuwa podany rekord Audio.
     */
    void deleteAudio(Audio audio);

    /**
     * Pobiera rekordy Audio na podstawie dynamicznych kryteriów z paginacją.
     *
     * @param title Tytuł utworu (może być null).
     * @param year Rok utworu (może być null).
     * @param artist Artysta utworu (może być null).
     * @param album Album utworu (może być null).
     * @param userId ID użytkownika (nie może być null).
     * @param pageable Obiekt paginacji.
     * @return Strona rekordów Audio spełniających kryteria.
     */
    Page<Audio> getFilteredAudio(String title, Integer year, String artist, String album, Long userId, Pageable pageable);
}
