package com.catalog.catalog.repository;

import com.catalog.catalog.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AudioRepository extends JpaRepository<Audio, Long>, JpaSpecificationExecutor<Audio> {

    @Query("SELECT COUNT(a) FROM Audio a WHERE a.user.id = :userId")
    Integer countByUserId(@Param("userId") Long userId);
}
