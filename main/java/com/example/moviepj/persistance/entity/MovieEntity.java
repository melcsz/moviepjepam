package com.example.moviepj.persistance.entity;


import com.example.moviepj.persistance.entity.status.FileStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "movie", schema = "movie_copy",
        indexes = {
                @Index(name = "IDX_MYTIT", columnList = "title"),
                @Index(columnList = "director_id",name = "release_year")
        })
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is mandatory")
    @Column(name = "title",length = 191)
    private String title;

    @Column(name = "critic_rating")
    private Double criticRating;

    @Column(name = "release_year")
    private Integer releaseYear;

    @OneToMany(mappedBy = "movie", targetEntity = ReviewEntity.class,fetch = FetchType.LAZY)
    private List<ReviewEntity> reviews;

    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private FileEntity file;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private DirectorEntity director;

    public MovieEntity(Long id) {
        this.id = id;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "studio_id")
    private StudioEntity studio;

    @Column(name = "url_s")
    private String urlS;

    @Column(name = "url_m")
    private String urlM;

    @Column(name = "url_l")
    private String urlL;

    public MovieEntity(String title, Double criticRating, int releaseYear, DirectorEntity director, GenreEntity genre,
                       StudioEntity studio, String urlS, String urlM, String urlL) {
        this.title = title;
        this.criticRating = criticRating;
        this.releaseYear = releaseYear;
        this.director = director;
        this.genre = genre;
        this.studio = studio;
        this.urlS = urlS;
        this.urlM = urlM;
        this.urlL = urlL;
    }

    public MovieEntity() {

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getCriticRating() {
        return criticRating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public DirectorEntity getDirector() {
        return director;
    }

    public StudioEntity getStudio() {
        return studio;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCriticRating(Double criticRating) {
        this.criticRating = criticRating;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public void setDirector(DirectorEntity director) {
        this.director = director;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
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

}
