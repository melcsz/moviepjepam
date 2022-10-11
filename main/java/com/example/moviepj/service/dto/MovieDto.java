package com.example.moviepj.service.dto;

import com.example.moviepj.persistance.entity.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public class MovieDto {

    private Long id;

    private String title;

    private Double criticRating;

    private Integer releaseYear;

    private List<ReviewDto> reviews;

    private DirectorEntity director;

    private GenreEntity genre;

    private StudioEntity studio;

    private String urlS;

    private String urlM;

    private String urlL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCriticRating() {
        return criticRating;
    }

    public void setCriticRating(Double criticRating) {
        this.criticRating = criticRating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }

    public DirectorEntity getDirector() {
        return director;
    }

    public void setDirector(DirectorEntity director) {
        this.director = director;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    public StudioEntity getStudio() {
        return studio;
    }

    public void setStudio(StudioEntity studio) {
        this.studio = studio;
    }

    public String getUrlS() {
        return urlS;
    }

    public void setUrlS(String urlS) {
        this.urlS = urlS;
    }

    public String getUrlM() {
        return urlM;
    }

    public void setUrlM(String urlM) {
        this.urlM = urlM;
    }

    public String getUrlL() {
        return urlL;
    }

    public void setUrlL(String urlL) {
        this.urlL = urlL;
    }

    public MovieDto(Long id,
                        String title,
                        Integer releaseYear) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
    }
}
