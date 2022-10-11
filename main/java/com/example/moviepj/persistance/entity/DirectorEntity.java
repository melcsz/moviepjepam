package com.example.moviepj.persistance.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "director", schema = "movie_copy",indexes = { @Index(name = "IDX_MYDIR", columnList = "name") })
public class DirectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 191)
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<MovieEntity> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movies = movies;
    }

    @OneToMany(mappedBy = "director",fetch = FetchType.LAZY)
    private List<MovieEntity> movies;

    public DirectorEntity(String fullName) {
        this.fullName = fullName;
    }

    public DirectorEntity() {

    }
}
