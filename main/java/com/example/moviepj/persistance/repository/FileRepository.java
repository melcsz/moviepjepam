package com.example.moviepj.persistance.repository;

import com.example.moviepj.persistance.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.cache.annotation.Cacheable;
import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {


    @Query("SELECT f FROM FileEntity f where f.fileStatus = 'IN_PROGRESS'")
    List<FileEntity> getAllInProgress();

    FileEntity getFileEntityById(Long id);

    @Query("SELECT f FROM FileEntity f where f.id = :id")
    FileEntity getPath(@Param("id") Long id);

}
