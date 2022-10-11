package com.example.moviepj.persistance.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "studio", schema = "movie_copy",indexes = {
        @Index(name = "IDX_MYSTD", columnList = "name") })
public class StudioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 191)
    private String name;

    @OneToMany(mappedBy = "studio",fetch = FetchType.LAZY)
    private List<MovieEntity> movies;

    public StudioEntity(String studioName) {
        this.name = studioName;
    }

    public StudioEntity() {

    }

    public StudioEntity(Long id, String studio) {
        this.id = id;
        this.name = studio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MovieEntity> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        StudioEntity studio = (StudioEntity) o;
        return Objects.equals(getName(), studio.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
