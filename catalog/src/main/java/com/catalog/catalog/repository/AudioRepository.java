package com.catalog.catalog.repository;


import com.catalog.catalog.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, Long> {
}
