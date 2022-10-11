package com.example.moviepj.persistance.entity;

import javax.persistence.*;

import com.example.moviepj.persistance.entity.status.FileStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

@Entity
@Table(name = "image")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long createdTimeInMillis;

    public FileEntity(String path, String thumbnail_path, String uuid, String url, String jpg, long currentTimeMillis, MovieEntity byUrlL, FileStatus inProgress) {
        this.path = path;
        this.thumbnailPath = thumbnail_path;
        this.name = uuid;
        this.UrlL = url;
        this.type = jpg;
        this.createdTimeInMillis = currentTimeMillis;
        this.movie = byUrlL;
        this.fileStatus = inProgress;
    }

    public Long getCreatedTimeInMillis() {
        return createdTimeInMillis;
    }

    public void setCreatedTimeInMillis(Long currentTimeInMillis) {
        this.createdTimeInMillis = currentTimeInMillis;
    }

    @Column
    private String path;

    @Column
    private String thumbnailPath;

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Column
    private String type;

    @Column
    private String UrlL;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public String getUrlL() {
        return UrlL;
    }

    public void setUrlL(String urlL) {
        UrlL = urlL;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;

    public void setId(Long id) {
        this.id = id;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public FileEntity() {
    }

    public FileEntity(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
