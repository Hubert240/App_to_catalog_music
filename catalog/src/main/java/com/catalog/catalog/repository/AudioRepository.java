package com.catalog.catalog.repository;

import com.catalog.catalog.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioRepository extends JpaRepository<Audio,Long> {

    List<Audio> findByTitleContainingIgnoreCase(String title);

    List<Audio> findByUserId(Long userId);

    List<Audio> findByTitleContainingIgnoreCaseAndUserId(String title,Long userId);


}
