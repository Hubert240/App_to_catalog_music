package com.catalog.catalog.repository;

import com.catalog.catalog.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudioRepository extends JpaRepository<Audio, Long>, JpaSpecificationExecutor<Audio> {

}
